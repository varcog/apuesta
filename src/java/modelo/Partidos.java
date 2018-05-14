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

public class Partidos {

    private int id;
    private Date fecha;
    private int idEquipo1;
    private int idEquipo2;
    private Conexion con = null;

    public Partidos(Conexion con) {
        this.con = con;
    }

    public Partidos(int id, Date fecha, int idEquipo1, int idEquipo2, Conexion con) {
        this.id = id;
        this.fecha = fecha;
        this.idEquipo1 = idEquipo1;
        this.idEquipo2 = idEquipo2;
        this.con = con;
    }

    public Partidos(int id, Date fecha, int idEquipo1, int idEquipo2) {
        this.id = id;
        this.fecha = fecha;
        this.idEquipo1 = idEquipo1;
        this.idEquipo2 = idEquipo2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdEquipo1() {
        return idEquipo1;
    }

    public void setIdEquipo1(int idEquipo1) {
        this.idEquipo1 = idEquipo1;
    }

    public int getIdEquipo2() {
        return idEquipo2;
    }

    public void setIdEquipo2(int idEquipo2) {
        this.idEquipo2 = idEquipo2;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Partidos\"(\n"
                + "    \"fecha\", \"idEquipo1\", \"idEquipo2\")\n"
                + "    VALUES (?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), idEquipo1 > 0 ? idEquipo1 : null, idEquipo2 > 0 ? idEquipo2 : null);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Partidos\"\n"
                + "    SET \"fecha\" = ?, \"idEquipo1\" = ?, \"idEquipo2\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), idEquipo1 > 0 ? idEquipo1 : null, idEquipo2 > 0 ? idEquipo2 : null, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Partidos\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n" +
                            "\"public\".\"Partidos\".\"id\",\n" +
                            "to_char(\"public\".\"Partidos\".\"fecha\",'DD/MM/YYYY') AS fecha,\n" +
                            "to_char(\"public\".\"Partidos\".\"fecha\",'HH24:MI') AS hora,\n" +
                            "\"public\".\"Partidos\".\"idEquipo1\",\n" +
                            "\"public\".\"Partidos\".\"idEquipo2\",\n" +
                            "\"public\".\"Equipos\".nombre as nombre1,\n" +
                            "\"public\".\"Equipos\".icono as icono1,\n" +
                            "eq.nombre as nombre2,\n" +
                            "eq.icono as icono2\n" +
                            "FROM\n" +
                            "\"public\".\"Partidos\"\n" +
                            "INNER JOIN \"public\".\"Equipos\" ON \"public\".\"Equipos\".\"id\" = \"public\".\"Partidos\".\"idEquipo1\"\n" +
                            "INNER JOIN \"public\".\"Equipos\" eq ON eq.\"id\" = \"public\".\"Partidos\".\"idEquipo2\"\n" +
                            "ORDER BY\n" +
                            "\"public\".\"Partidos\".fecha ASC;";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("fecha", rs.getString("fecha"));            
            obj.put("hora", rs.getString("hora"));
            obj.put("idEquipo1", rs.getInt("idEquipo1"));
            obj.put("idEquipo2", rs.getInt("idEquipo2"));
            obj.put("nombre1", rs.getString("nombre1"));
            obj.put("icono1", rs.getString("icono1"));
            obj.put("nombre2", rs.getString("nombre2"));
            obj.put("icono2", rs.getString("icono2"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Partidos\".\"id\",\n"
                + "    to_char(\"Partidos\".\"fecha\", 'DD/MM/YYYY HH24:MI') AS fecha,\n"
                + "    \"Partidos\".\"idEquipo1\",\n"
                + "    \"Partidos\".\"idEquipo2\"\n"
                + "    FROM public.\"Partidos\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("idEquipo1", rs.getInt("idEquipo1"));
            obj.put("idEquipo2", rs.getInt("idEquipo2"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public Partidos buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Partidos\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Partidos obj = null;
        if (rs.next()) {
            obj = new Partidos(con);
            obj.setId(rs.getInt("id"));
            obj.setFecha(rs.getDate("fecha"));
            obj.setIdEquipo1(rs.getInt("idEquipo1"));
            obj.setIdEquipo2(rs.getInt("idEquipo2"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public JSONObject toJSONObject() throws JSONException {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("fecha", fecha == null ? "" : f.format(fecha));
        obj.put("idEquipo1", idEquipo1);
        obj.put("idEquipo2", idEquipo2);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
}
