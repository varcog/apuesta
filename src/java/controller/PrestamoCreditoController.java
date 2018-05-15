package controller;

import conexion.Conexion;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Billetera;
import modelo.Menu;
import modelo.Prestamo;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;
import util.SisEventos;

@WebServlet(name = "PrestamoCreditoController", urlPatterns = {"/PrestamoCreditoController"})
public class PrestamoCreditoController extends HttpServlet {

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
                case "prestar":
                    html = prestar(request, con);
                    break;
                case "pagarEfectivo":
                    html = pagarEfectivo(request, con);
                    break;
                case "pagarBilletera":
                    html = pagarBilletera(request, con);
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

    private String init(HttpServletRequest request, Conexion con) throws SQLException, JSONException, ParseException {
        JSONObject json = new JSONObject();
        json.put("prestamos", new Prestamo(con).todosPrestatarios());
        json.put("relacionadores", new Usuario(con).todosRelacionadores());
        return json.toString();

    }

    private String prestar(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException, ParseException {
        int relacionador = Integer.parseInt(request.getParameter("relacionador"));
        double monto = Double.parseDouble(request.getParameter("monto"));
        int idCasa = new Usuario(con).getIdCasa();
        Date hoy = new Date();
        Billetera b = new Billetera(0, relacionador, monto, idCasa, Billetera.TIPO_TRANSACCION_PRESTAMO, 0, hoy, con);
        b.insert();
        Prestamo p = new Prestamo(0, relacionador, monto, 0.0, b.getId(), hoy, con);
        p.insert();
        JSONObject json = p.getPrestatario(relacionador);
        return json == null ? "null" : json.toString();
    }

    private String pagarEfectivo(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        double monto = Double.parseDouble(request.getParameter("monto"));
        int relacionador = Integer.parseInt(request.getParameter("relacionador"));
        int entrega = Integer.parseInt(request.getParameter("entrega"));
        String nombre = null;
        SisEventos.decodeUTF8(nombre);
        SisEventos.decodeUTF8(request.getParameter(""));
        return null;
    }

    private String pagarBilletera(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        double monto = Double.parseDouble(request.getParameter("monto"));
        int relacionador = Integer.parseInt(request.getParameter("relacionador"));
        int entrega = Integer.parseInt(request.getParameter("entrega"));
        String nombre = null;
        SisEventos.decodeUTF8(nombre);
        SisEventos.decodeUTF8(request.getParameter(""));
        return null;
    }

}
