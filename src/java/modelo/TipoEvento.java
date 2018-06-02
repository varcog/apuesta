package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TipoEvento {

    private int id;
    private String evento;
    private Conexion con = null;

    public TipoEvento(Conexion con) {
        this.con = con;
    }

    public TipoEvento(int id, String evento, Conexion con) {
        this.id = id;
        this.evento = evento;
        this.con = con;
    }

    public TipoEvento(int id, String evento) {
        this.id = id;
        this.evento = evento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvento() {
        return evento == null ? "" : evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public void setDatos(int id, String evento) {
        this.id = id;
        this.evento = evento;
    }

    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"TipoEvento\"(\n"
                + "    \"evento\")\n"
                + "    VALUES (?)\n";
        this.id = con.ejecutarInsert(consulta, "id", evento);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"TipoEvento\"\n"
                + "    SET \"evento\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, evento, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"TipoEvento\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"TipoEvento\".\"id\",\n"
                + "    \"TipoEvento\".\"evento\"\n"
                + "    FROM public.\"TipoEvento\" ORDER BY id;";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("evento", rs.getString("evento"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"TipoEvento\".\"id\",\n"
                + "    \"TipoEvento\".\"evento\"\n"
                + "    FROM public.\"TipoEvento\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("evento", rs.getString("evento"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public TipoEvento buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"TipoEvento\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        TipoEvento obj = null;
        if (rs.next()) {
            obj = new TipoEvento(con);
            obj.setId(rs.getInt("id"));
            obj.setEvento(rs.getString("evento"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public JSONObject toJSONObject() throws JSONException {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("evento", evento);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
}
