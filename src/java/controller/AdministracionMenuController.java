package controller;

import conexion.Conexion;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Menu;
import modelo.SubMenu;
import modelo.Usuario;
import org.json.JSONException;

@WebServlet(name = "AdministracionMenuController", urlPatterns = {"/AdministracionMenuController"})
public class AdministracionMenuController extends HttpServlet {

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
                case "todos":
                    html = todos(request, con);
                    break;
                case "guardarMenu":
                    html = guardarMenu(request, con);
                    break;
                case "eliminarMenu":
                    html = eliminarMenu(request, con);
                    break;
                case "todosSubMenu":
                    html = todosSubMenu(request, con);
                    break;
                case "guardarSubMenu":
                    html = guardarSubMenu(request, con);
                    break;
                case "eliminarSubMenu":
                    html = eliminarSubMenu(request, con);
                    break;
                case "cambiarEstadoSubMenu":
                    html = cambiarEstadoSubMenu(request, con);
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

    private String todos(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        return new Menu(con).todos().toString();
    }

    private String guardarMenu(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        if (id > 0) {
            Menu m = new Menu(con).buscar(id);
            if (m == null) {
                return "false";
            }
            m.setNombre(nombre);
            m.update();
            return m.toJSONObject().toString();
        } else {
            Menu m = new Menu(id, nombre, con);
            m.insert();
            return m.toJSONObject().toString();
        }
    }

    private String eliminarMenu(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        Menu m = new Menu(con);
        m.setId(id);
        m.delete();
        return "true";
    }

    private String todosSubMenu(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idMenu = Integer.parseInt(request.getParameter("idMenu"));
        return new SubMenu(con).bucarSubMenuXMenu(idMenu).toString();
    }

    private String guardarSubMenu(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idMenu = Integer.parseInt(request.getParameter("idMenu"));
        String nombre = request.getParameter("nombre");
        String url = request.getParameter("url");
        if (id > 0) {
            SubMenu sm = new SubMenu(con).buscar(id);
            if (sm == null) {
                return "false";
            }
            sm.setNombre(nombre);
            sm.setUrl(url);
            sm.update();
            return sm.toJSONObject().toString();
        } else {
            SubMenu sm = new SubMenu(id, nombre, url, idMenu, con);
            sm.insert();
            return sm.toJSONObject().toString();
        }
    }

    private String eliminarSubMenu(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        SubMenu sm = new SubMenu(con);
        sm.setId(id);
        sm.delete();
        return "true";
    }

    private String cambiarEstadoSubMenu(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idSubMenu = Integer.parseInt(request.getParameter("idSubMenu"));
        boolean visible = Boolean.parseBoolean(request.getParameter("visible"));
        SubMenu sm = new SubMenu(con);
        sm.updateVisible(idSubMenu, visible);
        return "true";
    }

}
