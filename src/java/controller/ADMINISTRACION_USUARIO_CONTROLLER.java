package controller;

import conexion.Conexion;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Perfil;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;
import util.StringMD;

/**
 *
 * @author benja
 */
@WebServlet(name = "ADMINISTRACION_USUARIO_CONTROLLER", urlPatterns = {"/ADMINISTRACION_USUARIO_CONTROLLER"})
public class ADMINISTRACION_USUARIO_CONTROLLER extends HttpServlet {

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
                case "guardar_usuario":
                    html = guardar_usuario(request, con);
                    break;
                case "eliminar_usuario":
                    html = eliminar_usuario(request, con);
                    break;
                case "cambiar_estado_usuario":
                    html = cambiar_estado_usuario(request, con);
                    break;
                case "datos_usuario":
                    html = datos_usuario(request, con);
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
        json.put("CARGOS", new Perfil(con).todos());
        json.put("USUARIOS", new Usuario(con).todosConPerfil());
        return json.toString();
    }

    private String guardar_usuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        int accion = Integer.parseInt(request.getParameter("accion"));
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        int cargo = Integer.parseInt(request.getParameter("cargo"));
        String ci = request.getParameter("ci");
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String fecha_nacimiento = request.getParameter("fecha_nacimiento");
        Date fecha_nac;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fecha_nac = f.parse(fecha_nacimiento);
        } catch (ParseException e) {
            fecha_nac = null;
        }
        String sexo = request.getParameter("sexo");
        Usuario u;
        String pass;
        switch (accion) {
            case 0: // crear
                pass = StringMD.getStringMessageDigest(contrasena, StringMD.SHA512);
                u = new Usuario(0, usuario, pass, nombres, apellidos, fecha_nac, new Date(), ci, sexo, cargo, con.getUsuario().getId(), true);
                u.setCon(con);
                u.insert();
                return u.toJSONObject().toString();
            case 1: // modificar
                u = new Usuario(con).buscar(id);
                if (u == null) {
                    return "false";
                }
                u.setId_perfil(cargo);
                u.setNombres(nombres);
                u.setApellidos(apellidos);
                u.setFecha_nacimiento(fecha_nac);
                u.setSexo(sexo);
                u.updateDatos();
                return u.toJSONObject().toString();
            default: // cambiar contrasena
                u = new Usuario(con).buscar(id);
                if (u == null) {
                    return "false";
                }
                pass = StringMD.getStringMessageDigest(contrasena, StringMD.SHA512);
                u.setPassword(pass);
                u.updateContrasena();
                return u.toJSONObject().toString();
        }
    }

    private String datos_usuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario u = new Usuario(con).buscar(id);
        if (u == null) {
            return "false";
        }
        return u.toJSONObject().toString();
    }

    private String eliminar_usuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario u = new Usuario(con);
        u.setId(id);
        u.delete();
        return "true";
    }

    private String cambiar_estado_usuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int estado = Integer.parseInt(request.getParameter("estado"));
        int id_usuario = Integer.parseInt(request.getParameter("id_usuario"));
        Usuario u = new Usuario(con).buscar(id_usuario);
        if (u == null) {
            return "false";
        }
        u.setEstado(estado);
        u.updateEstado();
        return "true";
    }

}
