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
import modelo.Permiso;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(name = "AdministracionPerfilController", urlPatterns = {"/AdministracionPerfilController"})
public class AdministracionPerfilController extends HttpServlet {

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
                case "init":
                    html = init(request, con);
                    break;
                case "guardar_perfil":
                    html = guardar_perfil(request, con);
                    break;
                case "eliminar_perfil":
                    html = eliminar_perfil(request, con);
                    break;
                case "todos_sub_menu_asignados":
                    html = todos_sub_menu_asignados(request, con);
                    break;
                case "asignar_desasignar_sub_menu":
                    html = asignar_desasignar_sub_menu(request, con);
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

    private String init(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        JSONObject json = new JSONObject();
        json.put("PERFILES", new Perfil(con).todos());
        json.put("MENUS", new Menu(con).bucarMenuYSubMenuTodos());
        return json.toString();
    }

    private String guardar_perfil(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String descripcion = request.getParameter("descripcion");
        if (id > 0) {
            Perfil c = new Perfil(con).buscar(id);
            if (c == null) {
                return "false";
            }
            c.setNombre(descripcion);
            c.update();
            return c.toJSONObject().toString();
        } else {
            Perfil c = new Perfil(id, descripcion, con);
            c.insert();
            return c.toJSONObject().toString();
        }
    }

    private String eliminar_perfil(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        Perfil c = new Perfil(con);
        c.setId(id);
        c.delete();
        return "true";
    }

    private String todos_sub_menu_asignados(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id_cargo = Integer.parseInt(request.getParameter("id_cargo"));
        return new Permiso(con).todosXPerfil(id_cargo).toString();
    }

    private String asignar_desasignar_sub_menu(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        boolean asignar = Boolean.parseBoolean(request.getParameter("asignar"));
        int id_cargo = Integer.parseInt(request.getParameter("id_cargo"));
        int id_sub_menu = Integer.parseInt(request.getParameter("id_sub_menu"));
        Permiso p = new Permiso(con);
        p.delete(id_cargo, id_sub_menu);
        if (asignar) {
            p.setIdPerfil(id_cargo);
            p.setIdSubMenu(id_sub_menu);
            p.setAlta(true);
            p.setBaja(true);
            p.setModificacion(true);
            p.insert();
        }
        return "true";
    }
}
