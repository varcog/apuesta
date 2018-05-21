/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import conexion.Conexion;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.Estadio;
import modelo.Parametros;
import modelo.TipoApuesta;
import modelo.Usuario;
import org.json.JSONException;
import util.SisEventos;

/**
 *
 * @author equipo_2
 */
@MultipartConfig
@WebServlet(name = "creacionApuestasController", urlPatterns = {"/creacionApuestasController"})
public class creacionApuestasController extends HttpServlet {

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
                case "popEditarApuestas":
                    html = popEditarApuestas(request, con);
                    break;                
                case "guardarDatos":
                    html = guardarDatos(request, con);
                    break;                
                case "eliminar":
                    html = eliminar(request, con);
                    break;                
            }
            con.commit();
            response.getWriter().write(html);
        } catch (SQLException | JSONException ex) {
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
        return new TipoApuesta(con).todos().toString();
    }

    private String popEditarApuestas(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        return new TipoApuesta(con).buscar(id).toJSONObject().toString();
    }

    private String guardarDatos(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        int tipoApuesta = Integer.parseInt(request.getParameter("tipoApuesta"));
        int equipo1 = Integer.parseInt(request.getParameter("equipo1"));
        int equipo2 = Integer.parseInt(request.getParameter("equipo2"));
        
        TipoApuesta ta = new TipoApuesta(0, tipoApuesta, equipo1, equipo2, con);
        if(id>0){
            ta.setId(id);
            ta.update();
        }else{
            id=ta.insert();
        }
        return new TipoApuesta(con).buscar(id).toJSONObject().toString();
    }

    private String eliminar(HttpServletRequest request, Conexion con) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        new TipoApuesta(con).delete(id);
        return true+"";
    }
}
