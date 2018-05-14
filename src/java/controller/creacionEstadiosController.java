/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import conexion.Conexion;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
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
import modelo.Parametros;
import modelo.Usuario;
import org.json.JSONException;
import util.SisEventos;

/**
 *
 * @author equipo_2
 */
@MultipartConfig
@WebServlet(name = "creacionEstadiosController", urlPatterns = {"/creacionEstadiosController"})
public class creacionEstadiosController extends HttpServlet {

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
                case "init":
                    html = init(request, con);
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
        return new Estadio(con).todos().toString();
    }
    
    private String guardarDatos(HttpServletRequest request, Conexion con) throws ParseException, SQLException, IOException, ServletException, JSONException {
        String nombre = SisEventos.decodeUTF8(request.getParameter("nombre"));
        String descripcion = SisEventos.decodeUTF8(request.getParameter("descripcion"));
        int id = Integer.parseInt(request.getParameter("id"));                
        int capacidad = Integer.parseInt(request.getParameter("capacidad"));                
        Part peril=request.getPart("foto");
        String old="";
        boolean nuevo=true;
        if(id!=0){
            nuevo=false;
            old = request.getParameter("old");        
            String []a = old.split("/");
            old= a[3];
        }        
        String ruta=this.getServletContext().getRealPath("/");
        
        Estadio es = new Estadio(id, nombre, descripcion, old,capacidad, con);        
        if(id==0) id=es.insert();
        else {
            es.update();
        }
        
        
        String nombref="";
        String nombres;
        if(peril!=null){
            if(peril.getSubmittedFileName().length()>0){
                String rutaBk = new Parametros(con).getRutaBakup();     
                if(!nuevo){
                    new SisEventos().eliminarImagenEnElSistemaDeFicheros(ruta+File.separator+"img"+File.separator+"estadios"+File.separator+old);
                    new SisEventos().eliminarImagenEnElSistemaDeFicheros(rutaBk+File.separator+"img"+File.separator+"estadios"+File.separator+old);                
                }
                nombres = id+peril.getSubmittedFileName();
                nombref="img"+File.separator+"estadios"+File.separator+id+peril.getSubmittedFileName();                
                es.setFoto(nombres);
                new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), ruta+nombref);
                new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), rutaBk+nombref);                
                es.update();
            }
        }                
        return es.toJSONObject().toString();
    }

    private String eliminar(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        Estadio e = new Estadio(con).buscar(id);
        String rutaBk = new Parametros(con).getRutaBakup();
        String ruta=this.getServletContext().getRealPath("/");
        if(e.getFoto().length()>0){
            new SisEventos().eliminarImagenEnElSistemaDeFicheros(ruta+File.separator+"img"+File.separator+"estadios"+File.separator+e.getFoto());
            new SisEventos().eliminarImagenEnElSistemaDeFicheros(rutaBk+File.separator+"img"+File.separator+"estadios"+File.separator+e.getFoto());        
        }        
        e.setId(id);
        e.delete();
        return true+"";
    }
}
