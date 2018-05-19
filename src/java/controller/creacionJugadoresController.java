package controller;

import conexion.Conexion;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.Equipos;
import modelo.Estadio;
import modelo.Jugador;
import modelo.Parametros;
import modelo.Posicion;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;
import util.SisEventos;

/**
 *
 * @author equipo_2
 */
@MultipartConfig
@WebServlet(name = "creacionJugadoresController", urlPatterns = {"/creacionJugadoresController"})
public class creacionJugadoresController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, UnsupportedEncodingException {
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
                case "verJugadores":
                    html = verJugadores(request, con);
                    break;                             
                case "guardarPosicion":
                    html = guardarPosicion(request, con);
                    break;                             
                case "popRegistrarJugador":
                    html = popRegistrarJugador(request, con);
                    break;                             
                case "guardarDatos":
                    html = guardarDatos(request, con);
                    break;                             
                case "editarJugador":
                    html = editarJugador(request, con);
                    break;                             
                case "eliminar":
                    html = eliminar(request, con);
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
        } catch (ParseException ex) {
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
        return new Equipos(con).todos().toString();
    }

    private String verJugadores(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        return new Jugador(con).todos(id).toString();
    }

    private String guardarPosicion(HttpServletRequest request, Conexion con) throws UnsupportedEncodingException, SQLException {
        String nombrePosicion = SisEventos.decodeUTF8(request.getParameter("nombrePosicion"));
        Posicion p = new Posicion(0, nombrePosicion,con);
        return p.insert()+"";        
    }

    private String popRegistrarJugador(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        return new Posicion(con).todos().toString();
    }

    private String guardarDatos(HttpServletRequest request, Conexion con) throws UnsupportedEncodingException, IOException, ServletException, ParseException, SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idEqupo = Integer.parseInt(request.getParameter("idEquipo"));
        String nombres = SisEventos.decodeUTF8(request.getParameter("nombres"));
        String apellidos = SisEventos.decodeUTF8(request.getParameter("apellidos"));
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date dFechaNacimiento = formato.parse(fechaNacimiento);
        int idPosicion = Integer.parseInt(request.getParameter("posicion"));
        String detalle = SisEventos.decodeUTF8(request.getParameter("detalle"));        
        Part peril=request.getPart("foto");
        String old="";
        boolean nuevo=true;
        if(id!=0){
            nuevo=false;
            old = request.getParameter("old");                    
            String []a = old.split("/");
            if(a.length>3) old= a[3];
        }        
        String ruta=this.getServletContext().getRealPath("/");
        
        Jugador jug = new Jugador(id, nombres, apellidos, idPosicion, idEqupo, detalle, "", dFechaNacimiento, con);
        
        if(id==0) id=jug.insert();
        else {
            jug.update();
        }
        
        
        String nombref;        
        if(peril!=null){
            if(peril.getSubmittedFileName().length()>0){
                String rutaBk = new Parametros(con).getRutaBakup();     
                if(!nuevo){
                    if(old.length()>0){
                        new SisEventos().eliminarImagenEnElSistemaDeFicheros(ruta+File.separator+"img"+File.separator+"jugadores"+File.separator+old);
                        new SisEventos().eliminarImagenEnElSistemaDeFicheros(rutaBk+File.separator+"img"+File.separator+"jugadores"+File.separator+old);                
                    }
                }
                nombres = id+peril.getSubmittedFileName();
                nombref="img"+File.separator+"jugadores"+File.separator+id+peril.getSubmittedFileName();                
                jug.setFoto(nombres);
                new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), ruta+nombref);
                new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), rutaBk+nombref);                
                jug.updateFoto();
            }
        }                 
        return jug.buscarTarjeta(id).toString();
    }

    private String editarJugador(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int idJugador = Integer.parseInt(request.getParameter("idJugador"));
        JSONObject obj = new JSONObject();
        obj.put("jugador", new Jugador(con).buscar(idJugador).toJSONObject());
        obj.put("posiciones", new Posicion(con).todos());
        return obj.toString();
    }

    private String eliminar(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        new Jugador(con).delete(id);
        return true+"";
    }
}
