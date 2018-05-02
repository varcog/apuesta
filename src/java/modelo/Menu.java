package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Menu {

    private int id;
    private String nombre;
    private Conexion con;

    public Menu(Conexion con) {
        this.con = con;
    }

    public Menu(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    public Menu(int id, String nombre, Conexion con) {
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
        String consulta = "INSERT INTO public.\"Menu\"(\n"
                + "	\"nombre\")\n"
                + "	VALUES (?)";
        int id = con.EjecutarInsert(consulta, "id", nombre);
        this.id = id;
        return id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Menu\"\n"
                + "	SET \"nombre\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, nombre, id);
    }

    public void delete() throws SQLException {
        String consulta = "DELETE FROM public.\"Menu\"\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT * FROM public.\"Menu\"\n"
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

    public Menu buscar(int id) throws SQLException {
        String consulta = "SELECT * FROM public.\"Menu\"\n"
                + "	WHERE \"id\"=?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Menu m = new Menu(con);
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
    public JSONObject bucarMenuYSubMenuTodos() throws SQLException, JSONException {
        String consulta = "SELECT * FROM public.\"Menu\"\n"
                + "ORDER BY \"nombre\" ASC ";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject json = new JSONObject();
        SubMenu sub_menu = new SubMenu(con);
        while (rs.next()) {
            json.put(rs.getString("nombre"), sub_menu.bucarSubMenuXMenu(rs.getInt("id")));
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject bucarMenuYSubMenuXPerfilVisible(int idPerfil) throws SQLException, JSONException {
        String consulta = "SELECT DISTINCT \"Menu\".\"id\",\n"
                + "	     \"Menu\".\"nombre\"\n"
                + "	FROM public.\"Menu\", \n"
                + "    	     public.\"SubMenu\",\n"
                + "          public.\"Permiso\"\n"
                + "    WHERE \"Permiso\".\"idPerfil\" = " + idPerfil + "\n"
                + "    	     AND \"Permiso\".\"idSubMenu\" = \"SubMenu\".\"id\"\n"
                + "          AND \"SubMenu\".\"idMenu\" = \"Menu\".\"id\""
                + "    	     AND \"SubMenu\".\"visible\" = true\n"
                + "    ORDER BY \"Menu\".\"nombre\"";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject json = new JSONObject();
        SubMenu sub_menu = new SubMenu(con);
        while (rs.next()) {
            json.put(rs.getString("nombre"), sub_menu.bucarSubMenuXMenuXPerfilVisible(rs.getInt("id"), idPerfil));
        }
        rs.close();
        ps.close();
        return json;
    }
}
