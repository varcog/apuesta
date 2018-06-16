package controller;

import conexion.Conexion;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.ApuestaAmigo;
import modelo.ApuestaPartido;
import modelo.Billetera;
import modelo.Equipos;
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
import ws.wsNotificacion;

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
                case "historialApuestas":
                    html = historialApuestas(request, con);
                    break;
                case "historialPendienteAmigo":
                    html = historialPendienteAmigo(request, con);
                    break;
                case "ApuestaAmigo":
                    html = ApuestaAmigo(request, con);
                    break;
                case "okAprobarApuestaAmigo":
                    html = okAprobarApuestaAmigo(request, con);
                    break;
                case "okRechazarApuestaAmigo":
                    html = okRechazarApuestaAmigo(request, con);
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
        json.put("historialApuesta", new ApuestaPartido(con).getHistorial(u.getId()));
        json.put("ApuestaAmigoPendiente", "");
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
            JSONArray eliminar = new JSONArray();
            JSONArray actualizar = new JSONArray();
            JSONObject objActualizar;
            Partidos partido = new Partidos(con);
            ApuestaPartido app = new ApuestaPartido(con);
            Billetera b = new Billetera(con);
            double credito = b.getCreditoDisponible(con.getUsuario().getId());
            double monto_total = 0;
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
                                    monto_total += monto;
                                    insert++;
                                } else {
                                    objActualizar = new JSONObject();
                                    objActualizar.put("idTipoApuesta", idTipoApuesta);
                                    objActualizar.put("idPartido", idPartido);
                                    objActualizar.put("idApuestaPartido", app.getId());
                                    objActualizar.put("porcentaje", app.getMultiplicador());
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
            credito = SisEventos.acomodarDosDecimalesD(credito);
            monto_total = SisEventos.acomodarDosDecimalesD(monto_total);
            if (credito < monto_total) {
                JSONObject json = new JSONObject();
                json.put("resp", "CREDITO_INSUFICIENTE");
                json.put("credito", b.getCreditoDisponible(con.getUsuario().getId()));
                return json.toString();
            } else if (insert == length) {
                // guardar
                for (int i = 0; i < length; i++) {
                    idApuestaPartido = Integer.parseInt(request.getParameter("lista[" + i + "][idApuestaPartido]"));
                    monto = Double.parseDouble(request.getParameter("lista[" + i + "][monto]"));
                    b.setDatos(0, new Usuario(con).getIdCasa(), monto, con.getUsuario().getId(), Billetera.TIPO_TRANSACCION_APUESTA, idApuestaPartido, 0, new Date());
                    b.insert();
                }
                JSONObject json = new JSONObject();
                json.put("resp", true);
                json.put("credito", b.getCreditoDisponible(con.getUsuario().getId()));
                return json.toString();
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

    private String historialApuestas(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        return new ApuestaPartido(con).getHistorial(con.getUsuario().getId()).toString();
    }

    private String historialPendienteAmigo(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        return new ApuestaAmigo(con).getPendientes(con.getUsuario().getId()).toString();
    }

    private String ApuestaAmigo(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        int length = Integer.parseInt(request.getParameter("length"));
        if (length > 0) {
            int insert = 0;
            int idApuestaPartido, idPartido, idTipoApuesta;
            double monto, porcentaje;
            JSONArray eliminar = new JSONArray();
            JSONArray actualizar = new JSONArray();
            JSONObject objActualizar;
            Partidos partido = new Partidos(con);
            ApuestaPartido app = new ApuestaPartido(con);
            Billetera b = new Billetera(con);
            double credito = b.getCreditoDisponible(con.getUsuario().getId());
            double monto_total = 0;
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
                                    monto_total += monto;
                                    insert++;
                                } else {
                                    objActualizar = new JSONObject();
                                    objActualizar.put("idTipoApuesta", idTipoApuesta);
                                    objActualizar.put("idPartido", idPartido);
                                    objActualizar.put("idApuestaPartido", app.getId());
                                    objActualizar.put("porcentaje", app.getMultiplicador());
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
            credito = SisEventos.acomodarDosDecimalesD(credito);
            monto_total = SisEventos.acomodarDosDecimalesD(monto_total);
            if (credito < monto_total) {
                JSONObject json = new JSONObject();
                json.put("resp", "CREDITO_INSUFICIENTE");
                json.put("credito", b.getCreditoDisponible(con.getUsuario().getId()));
                return json.toString();
            } else if (insert == length) {
                // guardar
                for (int i = 0; i < length; i++) {
                    idApuestaPartido = Integer.parseInt(request.getParameter("lista[" + i + "][idApuestaPartido]"));
                    monto = Double.parseDouble(request.getParameter("lista[" + i + "][monto]"));
                    b.setDatos(0, new Usuario(con).getIdCasa(), monto, con.getUsuario().getId(), Billetera.TIPO_TRANSACCION_APUESTA, idApuestaPartido, 0, new Date());
                    b.insert();
                }
                JSONObject json = new JSONObject();
                json.put("resp", true);
                json.put("credito", b.getCreditoDisponible(con.getUsuario().getId()));
                return json.toString();
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

    private String okAprobarApuestaAmigo(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        int idApuestaAmigo = Integer.parseInt(request.getParameter("idApuestaAmigo"));
        ApuestaAmigo aa = new ApuestaAmigo(con);
        Billetera b = new Billetera(con);
        if (aa.buscarSet(idApuestaAmigo)) {
            double credito = b.getCreditoDisponible(con.getUsuario().getId());
            double monto = aa.getMonto();
            credito = SisEventos.acomodarDosDecimalesD(credito);
            monto = SisEventos.acomodarDosDecimalesD(monto);
            if (credito < monto) {
                JSONObject json = new JSONObject();
                json.put("resp", "CREDITO_INSUFICIENTE");
                json.put("credito", b.getCreditoDisponible(con.getUsuario().getId()));
                return json.toString();
            }
            int idPartido = aa.getIdPartido();
            Partidos p = new Partidos(con);
            if (p.sePuedeApostar(idPartido)) {
                p = p.buscar(idPartido);
                if (p.getIdEquipo1() == aa.getIdEquipoRetador()) {
                    aa.setIdEquipoRetado(p.getIdEquipo2());
                } else {
                    aa.setIdEquipoRetado(p.getIdEquipo1());
                }
                aa.update();
                b.setDatos(0, new Usuario(con).getIdCasa(), monto, con.getUsuario().getId(), Billetera.TIPO_TRANSACCION_APUESTA, 0, aa.getId(), new Date());
                b.insert();
                String retado = con.getUsuario().getNombres() + " " + con.getUsuario().getApellidos();
                Equipos e = new Equipos(con).buscar(aa.getIdEquipoRetador());
                String nombre1 = e.getNombre();
                e = new Equipos(con).buscar(aa.getIdEquipoRetado());
                String nombre2 = e.getNombre();
                Notificaciones not = new Notificaciones(0, aa.getIdUsuarioRetador(), con.getUsuario().getId(), "El usuario " + retado + " acepta la apuesta del partido " + nombre1 + " vs " + nombre2 + " de la fecha " + p.getFechaS(), 0, 0, new Date(), con);
                not.insert();
                JSONObject obj = not.toJSONObject();
                obj.put("foto", con.getUsuario().getFoto());
                new wsNotificacion().notificar(obj);
                JSONObject json = new JSONObject();
                json.put("resp", true);
                json.put("credito", b.getCreditoDisponible(con.getUsuario().getId()));
                return json.toString();
            } else {
                return "PARTIDO_PASADO";
            }
        } else {
            return "APUESTA_NO_DISPONIBLE";
        }
    }

    private String okRechazarApuestaAmigo(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        int idApuestaAmigo = Integer.parseInt(request.getParameter("idApuestaAmigo"));
        ApuestaAmigo aa = new ApuestaAmigo(con);
        Billetera b = new Billetera(con);
        if (aa.buscarSet(idApuestaAmigo)) {
            b.deleteXApuestaAmigo(idApuestaAmigo);
            aa.delete();
            String retado = con.getUsuario().getNombres() + " " + con.getUsuario().getApellidos();
            Partidos p = new Partidos(con).buscar(aa.getIdPartido());
            int idEquipo = aa.getIdEquipoRetador();
            Equipos e = new Equipos(con).buscar(idEquipo);
            String nombre1 = e.getNombre();
            if (idEquipo == p.getIdEquipo1()) {
                idEquipo = p.getIdEquipo2();
            } else {
                idEquipo = p.getIdEquipo1();
            }
            e = new Equipos(con).buscar(idEquipo);
            String nombre2 = e.getNombre();
            Notificaciones not = new Notificaciones(0, aa.getIdUsuarioRetador(), con.getUsuario().getId(), "El usuario " + retado + " rechazo la puesta del partido " + nombre1 + " vs " + nombre2 + " de la fecha " + p.getFechaS(), 0, 0, new Date(), con);
            not.insert();
            JSONObject obj = not.toJSONObject();
            obj.put("foto", con.getUsuario().getFoto());
            new wsNotificacion().notificar(obj);
        }
        JSONObject json = new JSONObject();
        json.put("resp", true);
        json.put("credito", b.getCreditoDisponible(con.getUsuario().getId()));
        return json.toString();
    }

}
