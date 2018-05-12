package controller;

import conexion.Conexion;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.EntregaRelacionador;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(name = "TraspasoCreditoController", urlPatterns = {"/TraspasoCreditoController"})
public class TraspasoCreditoController extends HttpServlet {

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
                case "guardarEntregaRelacionador":
                    html = guardarEntregaRelacionador(request, con);
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

    private String init(HttpServletRequest request, Conexion con) throws SQLException, JSONException {
        JSONObject json = new JSONObject();
        json.put("entregaRelacionador", new EntregaRelacionador(con).todosNombres());
        json.put("relacionadores", new Usuario(con).todosRelacionadores());
        json.put("entregas", new Usuario(con).todosCasa());
        return json.toString();

    }

    private String guardarEntregaRelacionador(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException, ServletException {
        double monto = Double.parseDouble(request.getParameter("monto"));
        int relacionador = Integer.parseInt(request.getParameter("relacionador"));
        int entrega = Integer.parseInt(request.getParameter("entrega"));
        EntregaRelacionador er = new EntregaRelacionador(0, relacionador, monto, entrega, new Date(), con);
        er.insert();
        return er.BuscarConNombres().toString();
    }

}
