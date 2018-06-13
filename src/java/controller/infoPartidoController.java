package controller;

import conexion.Conexion;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ApuestaAmigo;
import modelo.Billetera;
import modelo.Equipos;
import modelo.Notificaciones;
import modelo.Partidos;
import modelo.TipoApuesta;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;
import util.SisEventos;
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

    private String cargar(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        JSONObject obj = new JSONObject();
        obj.put("partido", new Partidos(con).buscarCompleto(idPartido));
        obj.put("apuestasAmigos", new ApuestaAmigo(con).todos());
        obj.put("apuestas", new TipoApuesta(con).getApuestas(idPartido));
        return obj.toString();
    }

    private String buscarRetador(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        String usr = request.getParameter("usr");
        return new Usuario(con).Buscar(usr).toJSONObject().toString();
    }

    private String apostarCon(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        Billetera b = new Billetera(con);
        double credito = b.getCreditoDisponible(con.getUsuario().getId());
        double monto = Double.parseDouble(request.getParameter("monto"));
        credito = SisEventos.acomodarDosDecimalesD(credito);
        monto = SisEventos.acomodarDosDecimalesD(monto);
        if (credito < monto) {
            JSONObject json = new JSONObject();
            json.put("resp", "CREDITO_INSUFICIENTE");
            json.put("credito", b.getCreditoDisponible(con.getUsuario().getId()));
            return json.toString();
        }
        int idPartido = Integer.parseInt(request.getParameter("idPartido"));
        if (new Partidos(con).sePuedeApostar(idPartido)) {
            int idRetado = Integer.parseInt(request.getParameter("id"));
            int idEquipo = Integer.parseInt(request.getParameter("idEquipo"));
            ApuestaAmigo apu = new ApuestaAmigo(0, con.getUsuario().getId(), idRetado, monto, idPartido, idEquipo, 0, con);
            apu.insert();
            b.setDatos(0, new Usuario(con).getIdCasa(), monto, con.getUsuario().getId(), Billetera.TIPO_TRANSACCION_APUESTA, 0, apu.getId(), new Date());
            b.insert();
            String retador = con.getUsuario().getNombres() + " " + con.getUsuario().getApellidos();
            Partidos p = new Partidos(con).buscar(idPartido);
            Equipos e = new Equipos(con).buscar(idEquipo);
            String nombre1 = e.getNombre();
            if (idEquipo == p.getIdEquipo1()) {
                idEquipo = p.getIdEquipo2();
            } else {
                idEquipo = p.getIdEquipo1();
            }
            e = new Equipos(con).buscar(idEquipo);
            String nombre2 = e.getNombre();
            Notificaciones not = new Notificaciones(0, idRetado, con.getUsuario().getId(), "El usuario " + retador + " te apuesta a " + nombre1 + " en el partido contra " + nombre2 + " de la fecha " + p.getFechaS(), 0, 0, new Date(), con);
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
    }
}
