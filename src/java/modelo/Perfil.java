package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Perfil {

    private int id;
    private String nombre;
    private Conexion con;

    public Perfil(Conexion con) {
        this.con = con;
    }

    public Perfil(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Perfil(int id, String nombre, Conexion con) {
        this.id = id;
        this.nombre = nombre;
        this.con = con;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Perfil\"(\n"
                + "	\"nombre\")\n"
                + "	VALUES (?)";
        int id = con.EjecutarInsert(consulta, "id", nombre);
        this.id = id;
        return id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Perfil\"\n"
                + "	SET \"nombre\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, nombre, id);
    }

    public void delete() throws SQLException {
        String consulta = "DELETE FROM public.\"Perfil\"\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT * FROM public.\"Perfil\"\n"
                + "ORDER BY \"nombre\" ASC ";
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

    public Perfil buscar(int id) throws SQLException {
        String consulta = "SELECT * FROM public.\"Perfil\"\n"
                + "	WHERE \"id\"=?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Perfil m = new Perfil(con);
        if (rs.next()) {
            m.setId(rs.getInt("id"));
            m.setNombre(rs.getString("nombre"));
            return m;
        }
        return null;
    }

    public JSONObject toJSONObject() throws SQLException, JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("nombre", nombre);
        return obj;
    }

    ////////////////////////////////////////////////////////////////////////////
}
