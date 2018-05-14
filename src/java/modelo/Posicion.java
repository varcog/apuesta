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

public class Posicion {

    private int id;
    private String nombre;
    private Conexion con = null;

    public Posicion(Conexion con) {
        this.con = con;
    }

    public Posicion(int id, String nombre, Conexion con) {
        this.id = id;
        this.nombre = nombre;
        this.con = con;
    }

    public Posicion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Posicion\"(\n"
                + "    \"nombre\")\n"
                + "    VALUES (?)\n";
        this.id = con.ejecutarInsert(consulta, "id", nombre);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Posicion\"\n"
                + "    SET \"nombre\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, nombre, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Posicion\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Posicion\".\"id\",\n"
                + "    \"Posicion\".\"nombre\"\n"
                + "    FROM public.\"Posicion\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Posicion\".\"id\",\n"
                + "    \"Posicion\".\"nombre\"\n"
                + "    FROM public.\"Posicion\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public Posicion buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Posicion\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Posicion obj = null;
        if (rs.next()) {
            obj = new Posicion(con);
            obj.setId(rs.getInt("id"));
            obj.setNombre(rs.getString("nombre"));
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
        obj.put("nombre", nombre);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
}
