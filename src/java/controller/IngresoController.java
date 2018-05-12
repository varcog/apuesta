package controller;

import conexion.Conexion;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.Perfil;
import modelo.Menu;
import modelo.Parametros;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;
import util.SisEventos;

@MultipartConfig
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
        con.transacction();
        try {
            String html = "";
            String evento = request.getParameter("evento");
            switch (evento) {
                case "obtenerMenu":
                    html = obtenerMenu(request, con);
                    break;
                case "obtenerIngreso":
                    html = obtenerIngreso(request, con);
                    break;
                case "cambiarFotoPerfil":
                    html = cambiarFotoPerfil(request, con);
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

    private String obtenerMenu(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        Usuario usuario = con.getUsuario();
        Menu menu = new Menu(con);
        return menu.bucarMenuYSubMenuXPerfilVisible(usuario.getIdPerfil()).toString();
    }

    private String obtenerIngreso(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        Usuario usuario = con.getUsuario();
        Menu menu = new Menu(con);
        JSONObject json = new JSONObject();
        json.put("menu", menu.bucarMenuYSubMenuXPerfilVisible(usuario.getIdPerfil()));
        Usuario u = con.getUsuario();
        json.put("usuario", u.getNombreCompleto());
        json.put("foto", u.getFoto());
        Perfil c = new Perfil(con).buscar(u.getIdPerfil());
        if (c != null) {
            json.put("perfil", c.getNombre());
        }
        return json.toString();
    }

    private String cambiarFotoPerfil(HttpServletRequest request, Conexion con) throws IOException, ServletException, SQLException {
        Part peril=request.getPart("file_foto_perfil");
        String old = request.getParameter("old");
        String ruta=this.getServletContext().getRealPath("/");
        String nombre="";
        if(peril!=null){
            String rutaBk = new Parametros(con).getRutaBakup();
            nombre = peril.getContentType().split("/")[1];                                    
            new SisEventos().eliminarImagenEnElSistemaDeFicheros(ruta+old);
            new SisEventos().eliminarImagenEnElSistemaDeFicheros(rutaBk+old);
            nombre="img"+File.separator+"perfil"+File.separator+con.getUsuario().getId()+peril.getSubmittedFileName()+"."+nombre;
            new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), ruta+nombre);
            new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), rutaBk+nombre);
            con.getUsuario().updateFoto(nombre);
        }
        return nombre;
    }
        
    

}
