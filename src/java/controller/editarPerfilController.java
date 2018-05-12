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
import modelo.Parametros;
import modelo.Usuario;
import org.json.JSONException;
import util.SisEventos;

/**
 *
 * @author equipo_2
 */
@MultipartConfig
@WebServlet(name = "editarPerfilController", urlPatterns = {"/editarPerfilController"})
public class editarPerfilController extends HttpServlet {

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
        return new Usuario(con).getPerfil(con.getUsuario().getId()).toString();
    }

    private String guardarDatos(HttpServletRequest request, Conexion con) throws ParseException, SQLException, IOException, ServletException {
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String fecNac = request.getParameter("fecNac");
        String telefono = request.getParameter("telefono");
        String sexo = request.getParameter("sexo");
        String direccion = request.getParameter("direccion");
        Usuario us = con.getUsuario();
        us.setNombres(nombres);
        us.setApellidos(apellidos);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = format.parse(fecNac);        
        us.setFechaNacimiento(fecha);
        us.setTelefono(telefono);
        us.setSexo(sexo);
        us.setDireccion(direccion);
        us.updateDatos();        
        Part peril=request.getPart("foto");
        String old = request.getParameter("old");
        String ruta=this.getServletContext().getRealPath("/");
        String nombre="";
        if(peril!=null){
            if(peril.getSubmittedFileName().length()>0){
                String rutaBk = new Parametros(con).getRutaBakup();
                nombre = peril.getContentType().split("/")[1];                                    
                new SisEventos().eliminarImagenEnElSistemaDeFicheros(ruta+old);
                new SisEventos().eliminarImagenEnElSistemaDeFicheros(rutaBk+old);
                nombre="img"+File.separator+"perfil"+File.separator+con.getUsuario().getId()+peril.getSubmittedFileName()+"."+nombre;
                new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), ruta+nombre);
                new SisEventos().guardarImagenEnElSistemaDeFicheros(peril.getInputStream(), rutaBk+nombre);
                con.getUsuario().updateFoto(nombre);
            }
        }
        return nombre;
    }
}
