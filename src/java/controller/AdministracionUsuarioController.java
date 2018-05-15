package controller;

import conexion.Conexion;
import java.io.File;
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
import util.SisEventos;
import util.StringMD;

@WebServlet(name = "AdministracionUsuarioController", urlPatterns = {"/AdministracionUsuarioController"})
public class AdministracionUsuarioController extends HttpServlet {

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
                case "init":
                    html = init(request, con);
                    break;
                case "guardarUsuario":
                    html = guardarUsuario(request, con);
                    break;
                case "eliminarUsuario":
                    html = eliminarUsuario(request, con);
                    break;
                case "cambiarEstadoUsuario":
                    html = cambiarEstadoUsuario(request, con);
                    break;
                case "datosUsuario":
                    html = datosUsuario(request, con);
                    break;
                case "updateEstado":
                    html = updateEstado(request, con);
                    break;
                case "updatePerfil":
                    html = updatePerfil(request, con);
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
        json.put("perfiles", new Perfil(con).todos());
        json.put("usuarios", new Usuario(con).todosConPerfil());
        return json.toString();
    }

    private String guardarUsuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        int accion = Integer.parseInt(request.getParameter("accion"));
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");        
        String ci = request.getParameter("ci");
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String direccion = request.getParameter("direccion");
        String celular = request.getParameter("celular");
        Date fechaNac;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fechaNac = f.parse(fechaNacimiento);
        } catch (ParseException e) {
            fechaNac = null;
        }
        String sexo = request.getParameter("sexo");
        Usuario u;
        String pass;
        switch (accion) {
            case 0: // crear
                pass = StringMD.getStringMessageDigest(contrasena, StringMD.SHA512);
                u = new Usuario(usuario,pass, nombres,apellidos,fechaNac, ci, celular, direccion, sexo, con.getUsuario().getId(), con.getUsuario().getId(), con);
                u.setCon(con);
                u.insert();
                return u.toJSONObject().toString();
                //return "true";
            case 1: // modificar
                u = new Usuario(con).buscar(id);
                if (u == null) {
                    return "false";
                }
                u.setIdPerfil(3);
                u.setNombres(nombres);
                u.setDireccion(direccion);
                u.setTelefono(celular);
                u.setApellidos(apellidos);
                u.setFechaNacimiento(fechaNac);
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

    private String datosUsuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario u = new Usuario(con).buscar(id);
        if (u == null) {
            return "false";
        }
        return u.toJSONObject().toString();
    }

    private String eliminarUsuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario u = new Usuario(con);
        u.setId(id);
        u.delete();
        return "true";
    }

    private String cambiarEstadoUsuario(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int estado = Integer.parseInt(request.getParameter("estado"));
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        Usuario u = new Usuario(con).buscar(idUsuario);
        if (u == null) {
            return "false";
        }
        u.setEstado(estado);
        u.updateEstado();
        return "true";
    }
    
//    private String guardar_producto(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        String codigo = request.getParameter("codigo");
//        String descripcion = request.getParameter("descripcion");
//        double precio_venta = Double.parseDouble(request.getParameter("precio_venta"));
//        int id_categoria = Integer.parseInt(request.getParameter("id_categoria"));
//        String imagen = null;
//        String ruta = null;
//        Part file = request.getPart("imagen");
//        SisEventos ev = new SisEventos();
//        boolean ok_subir = false;
//        String ruta_folder = this.getServletContext().getRealPath("/img") + File.separator + "productos" + File.separator;
//        String ruta_web = this.getServletContext().getContextPath() + "/img/productos/";
//        Date d = new Date();
//        if (id > 0) {
//            if (file != null && file.getSize() > 0 && file.getContentType().contains("image")) {
//                String tipo[] = file.getContentType().split("/");
//                if (tipo.length > 1) {
//                    imagen = ruta_web + "producto_" + id + "_" + d.getTime() + "." + tipo[1];
//                    ruta = ruta_folder + "producto_" + id + "_" + d.getTime() + "." + tipo[1];
//                } else {
//                    imagen = ruta_web + "producto_" + id + "_" + d.getTime() + "_" + file.getSubmittedFileName();
//                    ruta = ruta_folder + "producto_" + id + "_" + d.getTime() + "_" + file.getSubmittedFileName();
//                }
//                ok_subir = ev.guardarImagenEnElSistemaDeFicheros(file.getInputStream(), ruta);
//            }
//            PRODUCTO p = new PRODUCTO(con).buscar(id);
//            if (p == null) {
//                return "false";
//            }
//            if (!ok_subir) {
//                imagen = p.getIMAGEN();
//            }
//            String antImagen = p.getIMAGEN();
//            p.setCODIGO(codigo);
//            p.setNOMBRE(descripcion);
//            p.setIMAGEN(imagen);
//            p.setPRECIO_VENTA(precio_venta);
//            p.setID_CATEGORIA_PRODUCTO(id_categoria);
//            try {
//                p.update();
//            } catch (Exception e) {
//                ev.eliminarImagenEnElSistemaDeFicheros(ruta);
//                if (e.getMessage().contains("uq_producto_codigo")) {
//                    return ERROR_CODIGO_REPETIDO;
//                } else {
//                    throw e;
//                }
//            }
//
//            if (antImagen != null && ok_subir) {
//                String ims[] = antImagen.split("/");
//                if (ims.length > 0) {
//                    ev.eliminarImagenEnElSistemaDeFicheros(ruta_folder + ims[ims.length - 1]);
//                }
//            }
//            return p.toJSONObject().toString();
//        } else {
//            PRODUCTO p = new PRODUCTO(id, codigo, descripcion, null, precio_venta, id_categoria);
//            p.setCon(con);
//            try {
//                id = p.insert();
//            } catch (Exception e) {
//                if (e.getMessage().contains("uq_producto_codigo")) {
//                    return ERROR_CODIGO_REPETIDO;
//                } else {
//                    throw e;
//                }
//            }
//            if (file != null && file.getSize() > 0 && file.getContentType().contains("image")) {
//                String tipo[] = file.getContentType().split("/");
//                if (tipo.length > 1) {
//                    imagen = ruta_web + "producto_" + id + "_" + d.getTime() + "." + tipo[1];
//                    ruta = ruta_folder + "producto_" + id + "_" + d.getTime() + "." + tipo[1];
//                } else {
//                    imagen = ruta_web + "producto_" + id + "_" + d.getTime() + "_" + file.getSubmittedFileName();
//                    ruta = ruta_folder + "producto_" + id + "_" + d.getTime() + "_" + file.getSubmittedFileName();
//                }
//                ok_subir = ev.guardarImagenEnElSistemaDeFicheros(file.getInputStream(), ruta);
//            }
//            if (ok_subir) {
//                p.setIMAGEN(imagen);
//                p.setID(id);
//                p.update();
//            }
//            return p.toJSONObject().toString();
//        }
//    }

    private String updateEstado(HttpServletRequest request, Conexion con) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        int estado = Integer.parseInt(request.getParameter("estado"));
        Usuario us = new Usuario(con);
        us.setId(id);
        us.setEstado(estado);
        us.updateEstado();
        return "true";
    }
    private String updatePerfil(HttpServletRequest request, Conexion con) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        int tipo = Integer.parseInt(request.getParameter("tipo"));
        Usuario us = new Usuario(con);
        us.setId(id);
        us.setIdPerfil(tipo);
        us.updateIdPerfil();
        return "true";
    }

}
