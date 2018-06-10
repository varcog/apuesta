package controller;

import conexion.Conexion;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ApuestaAmigo;
import modelo.Equipos;
import modelo.Notificaciones;
import modelo.Partidos;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;
import ws.wsNotificacion;

@MultipartConfig
@WebServlet(name = "infoPartidoController", urlPatterns = {"/infoPartidoController"})
public class infoPartidoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                case "cargar":
                    html = cargar(request, con);
                    break;                             
                case "buscarRetador":
                    html = buscarRetador(request, con);
                    break;                             
                case "apostarCon":
                    html = apostarCon(request, con);
                    break;                             
            }
            con.commit();
            response.getWriter().write(html);
        } catch (SQLException | JSONException ex) {
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

    private String cargar(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        JSONObject obj = new JSONObject();
        obj.put("partido", new Partidos(con).buscarCompleto(idPartido));
        obj.put("apuestasAmigos", new ApuestaAmigo(con).todos());
        return obj.toString();
    }   

    private String buscarRetador(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        String usr = request.getParameter("usr");
        return new Usuario(con).Buscar(usr).toJSONObject().toString();
    }

    private String apostarCon(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idRetado = Integer.parseInt(request.getParameter("id"));
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        int idEquipo = Integer.parseInt(request.getParameter("idEquipo"));
        ApuestaAmigo apu = new ApuestaAmigo(0, con.getUsuario().getId(), idRetado, 0.0, idPartido, idEquipo, 0,con);
        apu.insert();
        String retador = con.getUsuario().getNombres()+" "+con.getUsuario().getApellidos();
        Partidos p = new Partidos(con).buscar(idPartido);
        Equipos e = new Equipos(con).buscar(idEquipo);
        String nombre1=e.getNombre();
        if(idEquipo==p.getIdEquipo1()){
            idEquipo = p.getIdEquipo2();
        }else idEquipo=p.getIdEquipo1();
        e = new Equipos(con).buscar(idEquipo);
        String nombre2=e.getNombre();
        Notificaciones not = new Notificaciones(0, idRetado, con.getUsuario().getId(), "El usuario "+retador+" te apuesta a "+nombre1+" en el partido contra "+nombre2+" de la fecha "+p.getFechaS(), 0, 0, new Date(),con);
        not.insert();        
        JSONObject obj = not.toJSONObject();
        obj.put("foto",con.getUsuario().getFoto());
        new wsNotificacion().notificar(obj);
        return "true";
    }
}
