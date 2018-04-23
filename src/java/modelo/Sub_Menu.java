package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sub_Menu {

    private int id;
    private String nombre;
    private String imagen;
    private String url;
    private int id_menu;
    private boolean visible;
    private Conexion con;

    public Sub_Menu(Conexion con) {
        this.con = con;
    }

    public Sub_Menu(int id, String nombre, String imagen, String url, int id_menu, boolean visible) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.url = url;
        this.id_menu = id_menu;
        this.visible = visible;
    }

    public Sub_Menu(int id, String nombre, String url, int id_menu, Conexion con) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.id_menu = id_menu;
        this.visible = true;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Sub_Menu\"(\n"
                + "	\"nombre\", \"imagen\", \"url\", \"id_menu\")\n"
                + "	VALUES (?,?,?,?)";
        int id = con.EjecutarInsert(consulta, "id", nombre, imagen, url, id_menu);
        this.id = id;
        return id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Sub_Menu\"\n"
                + "	SET \"nombre\"=?, \"imagen\"=?, \"url\"=?, \"id_menu\"=?, \"visible\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, nombre, imagen, url, id_menu, visible, id);
    }

    public void delete() throws SQLException {
        String consulta = "DELETE FROM public.\"Sub_Menu\"\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT * FROM public.\"Sub_Menu\"\n"
                + "ORDER BY \"nombre\" ASC ";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            obj.put("imagen", rs.getString("imagen"));
            obj.put("url", rs.getString("url"));
            obj.put("id_menu", rs.getInt("id_menu"));
            obj.put("visible", rs.getBoolean("visible"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public Sub_Menu buscar(int id) throws SQLException {
        String consulta = "SELECT * FROM public.\"Sub_Menu\"\n"
                + "	WHERE \"id\"=?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Sub_Menu m = new Sub_Menu(con);
        if (rs.next()) {
            m.setId(rs.getInt("id"));
            m.setNombre(rs.getString("nombre"));
            m.setImagen(rs.getString("imagen"));
            m.setUrl(rs.getString("url"));
            m.setId_menu(rs.getInt("id_menu"));
            m.setVisible(rs.getBoolean("visible"));
            return m;
        }
        return null;
    }

    public JSONObject toJSONObject() throws SQLException, JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("nombre", nombre);
        obj.put("imagen", imagen);
        obj.put("url", url);
        obj.put("id_menu", id_menu);
        obj.put("visible", visible);
        return obj;
    }

    ////////////////////////////////////////////////////////////////////////////
    public JSONArray bucarSubMenuXCargoVisible(int idCargo) throws SQLException, JSONException {
        String consulta = "SELECT \"Sub_Menu\".\"nombre\",\n"
                + "               \"Sub_Menu\".\"url\",\n"
                + "               \"Sub_Menu\".\"imagen\"\n"
                + "	FROM public.\"Sub_Menu\",\n"
                + "         public.\"Permiso\"\n"
                + "    WHERE \"Permiso\".\"id_perfil\" = " + idCargo + "\n"
                + "    	  AND \"Permiso\".\"id_sub_menu\" = \"Sub_Menu\".\"id\"\n"
                + "    	  AND \"Sub_Menu\".\"visible\" = true\n";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("nombre", rs.getString("nombre"));
            obj.put("url", rs.getString("url"));
            obj.put("imagen", rs.getString("imagen"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;

    }

    public JSONArray bucarSubMenuXMenu(int idMenu) throws SQLException, JSONException {
        String consulta = "SELECT \"Sub_Menu\".\"nombre\",\n"
                + "               \"Sub_Menu\".\"url\",\n"
                + "               \"Sub_Menu\".\"imagen\",\n"
                + "               \"Sub_Menu\".\"visible\",\n"
                + "               \"Sub_Menu\".\"id\"\n"
                + "	FROM public.\"Sub_Menu\"\n"
                + "    WHERE \"Sub_Menu\".\"id_menu\" = " + idMenu + "\n";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("nombre", rs.getString("nombre"));
            obj.put("url", rs.getString("url"));
            obj.put("imagen", rs.getString("imagen"));
            obj.put("visible", rs.getBoolean("visible"));
            obj.put("id", rs.getInt("id"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;

    }

    public JSONArray bucarSubMenuXMenuXCargoVisible(int idMenu, int id_perfil) throws SQLException, JSONException {
        String consulta = "SELECT DISTINCT \"Sub_Menu\".\"nombre\",\n"
                + "               \"Sub_Menu\".\"url\",\n"
                + "               \"Sub_Menu\".\"imagen\"\n"
                + "	FROM public.\"Sub_Menu\", public.\"Permiso\"\n"
                + "    WHERE \"Sub_Menu\".\"id_menu\" = " + idMenu + "\n"
                + "          AND \"Permiso\".\"id_perfil\" = " + id_perfil + "\n"
                + "          AND \"Permiso\".\"id_sub_menu\" = \"Sub_Menu\".\"id\"\n"
                + "          AND \"Sub_Menu\".\"visible\" = true\n"
                + "    ORDER BY \"Sub_Menu\".\"nombre\"";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("nombre", rs.getString("nombre"));
            obj.put("url", rs.getString("url"));
            obj.put("imagen", rs.getString("imagen"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;

    }

    public void update_visible(int id, boolean visible) throws SQLException {
        String consulta = "UPDATE public.\"Sub_Menu\"\n"
                + "	SET \"visible\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, visible, id);
    }
}
