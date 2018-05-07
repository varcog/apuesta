package controller;

import conexion.Conexion;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Perfil;
import modelo.Menu;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(name = "IngresoController", urlPatterns = {"/IngresoController"})
public class IngresoController extends HttpServlet {

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
        con.Transacction();
        try {
            String html = "";
            String evento = request.getParameter("evento");
            switch (evento) {
                case "obtener_menu":
                    html = obtener_menu(request, con);
                    break;
                case "obtener_ingreso":
                    html = obtener_ingreso(request, con);
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

    private String obtener_menu(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        Usuario usuario = con.getUsuario();
        Menu menu = new Menu(con);
        return menu.bucarMenuYSubMenuXPerfilVisible(usuario.getIdPerfil()).toString();
    }

    private String obtener_ingreso(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        Usuario usuario = con.getUsuario();
        Menu menu = new Menu(con);
        JSONObject json = new JSONObject();
        json.put("Menu", menu.bucarMenuYSubMenuXPerfilVisible(usuario.getIdPerfil()));
        Usuario u = con.getUsuario();
        json.put("Usuario", u.getNombreCompleto());
        Perfil c = new Perfil(con).buscar(u.getIdPerfil());
        if (c != null) {
            json.put("Perfil", c.getNombre());
        }
        return json.toString();
    }

}
