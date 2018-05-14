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

public class Estadio {

    private int id;
    private String nombre;
    private String descripcion;
    private String foto;
    private int capacidad;
    private Conexion con = null;

    public Estadio(Conexion con) {
        this.con = con;
    }

    public Estadio(int id, String nombre, String descripcion, String foto, int capacidad, Conexion con) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.capacidad = capacidad;
        this.con = con;
    }

    public Estadio(int id, String nombre, String descripcion, String foto, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.capacidad = capacidad;
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

    public String getDescripcion() {
        return descripcion == null ? "" : descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto == null ? "" : foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Estadio\"(\n"
                + "    \"nombre\", \"descripcion\", \"foto\", \"capacidad\")\n"
                + "    VALUES (?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", nombre, descripcion, foto, capacidad);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Estadio\"\n"
                + "    SET \"nombre\" = ?, \"descripcion\" = ?, \"foto\" = ?, \"capacidad\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, nombre, descripcion, foto, capacidad, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Estadio\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Estadio\".\"id\",\n"
                + "    \"Estadio\".\"nombre\",\n"
                + "    \"Estadio\".\"descripcion\",\n"
                + "    \"Estadio\".\"foto\",\n"
                + "    \"Estadio\".\"capacidad\"\n"
                + "    FROM public.\"Estadio\" ORDER BY nombre;";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            obj.put("descripcion", rs.getString("descripcion"));
            obj.put("foto", rs.getString("foto"));
            obj.put("capacidad", rs.getInt("capacidad"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Estadio\".\"id\",\n"
                + "    \"Estadio\".\"nombre\",\n"
                + "    \"Estadio\".\"descripcion\",\n"
                + "    \"Estadio\".\"foto\",\n"
                + "    \"Estadio\".\"capacidad\"\n"
                + "    FROM public.\"Estadio\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            obj.put("descripcion", rs.getString("descripcion"));
            obj.put("foto", rs.getString("foto"));
            obj.put("capacidad", rs.getInt("capacidad"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public Estadio buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Estadio\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Estadio obj = null;
        if (rs.next()) {
            obj = new Estadio(con);
            obj.setId(rs.getInt("id"));
            obj.setNombre(rs.getString("nombre"));
            obj.setDescripcion(rs.getString("descripcion"));
            obj.setFoto(rs.getString("foto"));
            obj.setCapacidad(rs.getInt("capacidad"));
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
        obj.put("descripcion", descripcion);
        obj.put("foto", foto);
        obj.put("capacidad", capacidad);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
}
