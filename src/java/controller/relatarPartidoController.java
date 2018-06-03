package controller;

import conexion.Conexion;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Jugador;
import modelo.Partidos;
import modelo.TipoEvento;
import modelo.TipoEventoPartido;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author equipo_2
 */
@MultipartConfig
@WebServlet(name = "relatarPartidoController", urlPatterns = {"/relatarPartidoController"})
public class relatarPartidoController extends HttpServlet {
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        Usuario usuario = ((Usuario) request.getSession().getAttribute("usr"));
        if (usuario == null) {
            response.getWriter().write("false");
            return;
        }
        Conexion con = usuario.getCon();
        if (con == null) {
            response.getWriter().write("false");
            return;
        }
        con.transacction();
        try {
            String html = "";
            String evento = request.getParameter("evento");
            switch (evento) {
                case "cargarRelato":
                    html = cargarRelato(request, con);
                    break;
                case "cargarJugadores":
                    html = cargarJugadores(request, con);
                    break;
                case "crearEvento":
                    html = crearEvento(request, con);
                    break;
                case "eliminar":
                    html = eliminar(request, con);
                    break;
                case "traerFaltantes":
                    html = traerFaltantes(request, con);
                    break;
            }
            con.commit();
            response.getWriter().write(html);
        } catch (SQLException ex) {
            con.error(this, ex);
            response.getWriter().write("false");
        } catch (JSONException ex) {
            con.error(this, ex);
            response.getWriter().write("false");
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    private String cargarRelato(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        JSONObject obj = new JSONObject();
        obj.put("relato", new Partidos(con).relato(idPartido));
        obj.put("partido", new Partidos(con).buscarCompleto(idPartido));
        obj.put("eventos", new TipoEvento(con).todos());
        return obj.toString();
    }
    
    private String cargarJugadores(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idEquipo = Integer.parseInt(request.getParameter("idEquipo"));
        return new Jugador(con).todos(idEquipo).toString();
    }
    
    private String crearEvento(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        int idJugador = Integer.parseInt(request.getParameter("idJugador"));
        int idEvento = Integer.parseInt(request.getParameter("idEvento"));
        TipoEventoPartido tep = new TipoEventoPartido(0, idEvento, idPartido, idJugador, new Date(),con);
        tep.insert();
        return new Partidos(con).buscarRelato(tep.getId()).toString();
    }
    
    private String eliminar(HttpServletRequest request, Conexion con) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        return new TipoEventoPartido(con).delete(id)+"";
    }

    private String traerFaltantes(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        String ultimaHora = request.getParameter("ultimaHora");
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        if(ultimaHora==null) return new Partidos(con).relato(idPartido).toString();
        else return new Partidos(con).relato(idPartido,ultimaHora).toString();
    }
}