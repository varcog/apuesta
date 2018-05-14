package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Equipos {

    private int id;
    private String nombre;
    private String icono;
    private int mundialesGanados;
    private Conexion con = null;

    public Equipos(Conexion con) {
        this.con = con;
    }

    public Equipos(int id, String nombre, String icono, int mundialesGanados, Conexion con) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.mundialesGanados = mundialesGanados;
        this.con = con;
    }

    public Equipos(int id, String nombre, String icono, int mundialesGanados) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.mundialesGanados = mundialesGanados;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre == null ? "" : nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono == null ? "" : icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public int getMundialesGanados() {
        return mundialesGanados;
    }

    public void setMundialesGanados(int mundialesGanados) {
        this.mundialesGanados = mundialesGanados;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Equipos\"(\n"
                + "    \"nombre\", \"icono\", \"mundialesGanados\")\n"
                + "    VALUES (?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", nombre, icono, mundialesGanados);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Equipos\"\n"
                + "    SET \"nombre\" = ?, \"icono\" = ?, \"mundialesGanados\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, nombre, icono, mundialesGanados, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Equipos\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Equipos\".\"id\",\n"
                + "    \"Equipos\".\"nombre\",\n"
                + "    \"Equipos\".\"icono\",\n"
                + "    \"Equipos\".\"mundialesGanados\"\n"
                + "    FROM public.\"Equipos\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            obj.put("icono", rs.getString("icono"));
            obj.put("mundialesGanados", rs.getInt("mundialesGanados"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }
    public JSONArray todosJugadores() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Equipos\".\"id\",\n"
                + "    \"Equipos\".\"nombre\",\n"
                + "    \"Equipos\".\"icono\",\n"
                + "    \"Equipos\".\"mundialesGanados\"\n"
                + "    FROM public.\"Equipos\" ORDER BY nombre;";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            obj.put("icono", rs.getString("icono"));
            obj.put("mundialesGanados", rs.getInt("mundialesGanados"));
            obj.put("jugadores", "");
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Equipos\".\"id\",\n"
                + "    \"Equipos\".\"nombre\",\n"
                + "    \"Equipos\".\"icono\",\n"
                + "    \"Equipos\".\"mundialesGanados\"\n"
                + "    FROM public.\"Equipos\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            obj.put("icono", rs.getString("icono"));
            obj.put("mundialesGanados", rs.getInt("mundialesGanados"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public Equipos buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Equipos\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Equipos obj = null;
        if (rs.next()) {
            obj = new Equipos(con);
            obj.setId(rs.getInt("id"));
            obj.setNombre(rs.getString("nombre"));
            obj.setIcono(rs.getString("icono"));
            obj.setMundialesGanados(rs.getInt("mundialesGanados"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public JSONObject toJSONObject() throws JSONException {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("nombre", nombre);
        obj.put("icono", icono);
        obj.put("mundialesGanados", mundialesGanados);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
}
