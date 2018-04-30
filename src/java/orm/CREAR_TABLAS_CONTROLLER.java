package orm;

import conexion.Conexion;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;

@WebServlet(name = "CREAR_TABLAS_CONTROLLER", urlPatterns = {"/CREAR_TABLAS_CONTROLLER"})
public class CREAR_TABLAS_CONTROLLER extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        Conexion con = new Conexion();
        try {
            String html = "";
            String evento = request.getParameter("evento");

            switch (evento) {
                case "cargar":
                    html = cargar(request, con);
                    break;
                case "crear_tablas_seleccionadas":
                    html = crear_tablas_seleccionadas(request, con);
                    break;
            }
            con.commit();
            response.getWriter().write(html);
        } catch (SQLException ex) {
            con.rollback();
            ex.printStackTrace();
            response.getWriter().write("false");
        }
    }

    private String cargar(HttpServletRequest request, Conexion con) throws SQLException {
        List<String> tablas = DataBase.tablas(con, "BROKER");
        JSONArray json = new JSONArray();
        for (String tabla : tablas) {
            json.put(tabla);
        }
        return json.toString();
    }

    private String crear_tablas_seleccionadas(HttpServletRequest request, Conexion con) {
        String nombre[] = request.getParameterValues("nombre[]");
        for (String nom : nombre) {
            Escribir.escribirClases(nom, "BROKER", con);
        }
        return true + "";
    }
}
