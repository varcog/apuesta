package controller;

import conexion.Conexion;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.ApuestaPartido;
import modelo.Billetera;
import modelo.Perfil;
import modelo.Menu;
import modelo.Notificaciones;
import modelo.Parametros;
import modelo.Partidos;
import modelo.TipoApuesta;
import modelo.Usuario;
import org.json.JSONArray;
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
                case "okApostar":
                    html = okApostar(request, con);
                    break;
            }
            con.commit();
            response.getWriter().write(html);
        } catch (SQLException | JSONException | ParseException ex) {
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
        json.put("idUsuario", u.getId());
        json.put("usuario", u.getNombreCompleto());
        json.put("foto", u.getFoto());
        Perfil c = new Perfil(con).buscar(u.getIdPerfil());
        if (c != null) {
            json.put("perfil", c.getNombre());
        }
        Billetera b = new Billetera(con);
        json.put("credito", b.getCreditoDisponible(usuario.getId()));
        json.put("notificaciones", new Notificaciones(con).buscarJSONArray(con.getUsuario().getId()));
        return json.toString();
    }

    private String cambiarFotoPerfil(HttpServletRequest request, Conexion con) throws IOException, ServletException, SQLException {
        Part peril = request.getPart("file_foto_perfil");
        String old = request.getParameter("old");
        String ruta = this.getServletContext().getRealPath("/");
        String nombre = "";
        if (peril != null) {
            String rutaBk = new Parametros(con).getRutaBakup();
            nombre = peril.getContentType().split("/")[1];
            new SisEventos().eliminarImagenEnElSistemaDeFicheros(ruta + old);
            new SisEventos().eliminarImagenEnElSistemaDeFicheros(rutaBk + old);
            nombre = "img" + File.separator + "perfil" + File.separator + con.getUsuario().getId() + peril.getSubmittedFileName() + "." + nombre;
            new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), ruta + nombre);
            new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), rutaBk + nombre);
            con.getUsuario().updateFoto(nombre);
        }
        return nombre;
    }

    private String okApostar(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        int length = Integer.parseInt(request.getParameter("length"));
        if (length > 0) {
            int insert = 0;
            int idApuestaPartido, idPartido, idTipoApuesta;
            double monto, porcentaje;
            TipoApuesta ta = new TipoApuesta(con);
            JSONArray eliminar = new JSONArray();
            JSONArray actualizar = new JSONArray();
            JSONObject objActualizar;
            Partidos partido = new Partidos(con);
            ApuestaPartido app = new ApuestaPartido(con);
//            Map<String, String[]> listanga = request.getParameterMap();
//            for (Map.Entry<String, String[]> entry : listanga.entrySet()) {
//                String key = entry.getKey();
//                String[] value = entry.getValue();
//                System.out.println(key);
//            }
            for (int i = 0; i < length; i++) {
                idApuestaPartido = Integer.parseInt(request.getParameter("lista[" + i + "][idApuestaPartido]"));
                idPartido = Integer.parseInt(request.getParameter("lista[" + i + "][idPartido]"));
                idTipoApuesta = Integer.parseInt(request.getParameter("lista[" + i + "][idTipoApuesta]"));
                monto = Double.parseDouble(request.getParameter("lista[" + i + "][monto]"));
                porcentaje = Double.parseDouble(request.getParameter("lista[" + i + "][porcentaje]"));

                if (partido.sePuedeApostar(idPartido)) {
                    if (monto > 0) {
                        // Verificar si no ha cambiado el monto
                        if (app.buscarSet(idTipoApuesta, idPartido)) {
                            if (app.getMultiplicador() > 0) {
                                if (app.getMultiplicador() == porcentaje) {
                                    insert++;
                                } else {
                                    objActualizar = new JSONObject();
                                    objActualizar.put("idTipoApuesta", idTipoApuesta);
                                    objActualizar.put("idPartido", idPartido);
                                    objActualizar.put("idApuestaPartido", app.getId());
                                    objActualizar.put("multiplicador", app.getMultiplicador());
                                    actualizar.put(objActualizar);
                                }
                            } else {
                                eliminar.put(idApuestaPartido);
                            }
                        } else {
                            eliminar.put(idApuestaPartido);
                        }
                    }
                } else {
                    eliminar.put(idApuestaPartido);
                }
            }
            if (insert == length) {
                // guardar
                for (int i = 0; i < length; i++) {
                    idApuestaPartido = Integer.parseInt(request.getParameter("listaApuesta[" + i + "][idApuestaPartido]"));
//                    idPartido = Integer.parseInt(request.getParameter("listaApuesta[" + i + "][idPartido]"));
//                    idTipoApuesta = Integer.parseInt(request.getParameter("listaApuesta[" + i + "][idTipoApuesta]"));
                    monto = Double.parseDouble(request.getParameter("listaApuesta[" + i + "][monto]"));
//                    porcentaje = Double.parseDouble(request.getParameter("listaApuesta[" + i + "][procentaje]"));
                    Billetera b = new Billetera(0, new Usuario(con).getIdCasa(), monto, con.getUsuario().getId(), Billetera.TIPO_TRANSACCION_APUESTA, idApuestaPartido, new Date(), con);
                    b.insert();
                }
                return "true";
            } else {
                JSONObject json = new JSONObject();
                if (eliminar.length() > 0 || actualizar.length() > 0) {
                    json.put("eliminar", eliminar);
                    json.put("actualizar", actualizar);
                    return json.toString();
                } else {
                    return "LENGTH_0";
                }
            }
        } else {
            return "LENGTH_0";
        }
    }

}
