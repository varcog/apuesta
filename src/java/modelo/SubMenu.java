package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubMenu {

    private int id;
    private String nombre;
    private String imagen;
    private String url;
    private int idMenu;
    private boolean visible;
    private Conexion con;

    public SubMenu(Conexion con) {
        this.con = con;
    }

    public SubMenu(int id, String nombre, String imagen, String url, int idMenu, boolean visible) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.url = url;
        this.idMenu = idMenu;
        this.visible = visible;
    }

    public SubMenu(int id, String nombre, String url, int idMenu, Conexion con) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.idMenu = idMenu;
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

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
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
        String consulta = "INSERT INTO public.\"SubMenu\"(\n"
                + "	\"nombre\", \"imagen\", \"url\", \"idMenu\", \"visible\")\n"
                + "	VALUES (?,?,?,?,?)";
        int id = con.EjecutarInsert(consulta, "id", nombre, imagen, url, idMenu, visible);
        this.id = id;
        return id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"SubMenu\"\n"
                + "	SET \"nombre\"=?, \"imagen\"=?, \"url\"=?, \"idMenu\"=?, \"visible\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, nombre, imagen, url, idMenu, visible, id);
    }

    public void delete() throws SQLException {
        String consulta = "DELETE FROM public.\"SubMenu\"\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT * FROM public.\"SubMenu\"\n"
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
            obj.put("idMenu", rs.getInt("idMenu"));
            obj.put("visible", rs.getBoolean("visible"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public SubMenu buscar(int id) throws SQLException {
        String consulta = "SELECT * FROM public.\"SubMenu\"\n"
                + "	WHERE \"id\"=?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        SubMenu m = new SubMenu(con);
        if (rs.next()) {
            m.setId(rs.getInt("id"));
            m.setNombre(rs.getString("nombre"));
            m.setImagen(rs.getString("imagen"));
            m.setUrl(rs.getString("url"));
            m.setIdMenu(rs.getInt("idMenu"));
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
        obj.put("idMenu", idMenu);
        obj.put("visible", visible);
        return obj;
    }

    ////////////////////////////////////////////////////////////////////////////
    public JSONArray bucarSubMenuXPerfilVisible(int idPerfil) throws SQLException, JSONException {
        String consulta = "SELECT \"SubMenu\".\"nombre\",\n"
                + "               \"SubMenu\".\"url\",\n"
                + "               \"SubMenu\".\"imagen\"\n"
                + "	FROM public.\"SubMenu\",\n"
                + "         public.\"Permiso\"\n"
                + "    WHERE \"Permiso\".\"idPerfil\" = " + idPerfil + "\n"
                + "    	  AND \"Permiso\".\"idSubMenu\" = \"SubMenu\".\"id\"\n"
                + "    	  AND \"SubMenu\".\"visible\" = true\n";
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
        String consulta = "SELECT \"SubMenu\".\"nombre\",\n"
                + "               \"SubMenu\".\"url\",\n"
                + "               \"SubMenu\".\"imagen\",\n"
                + "               \"SubMenu\".\"visible\",\n"
                + "               \"SubMenu\".\"id\"\n"
                + "	FROM public.\"SubMenu\"\n"
                + "    WHERE \"SubMenu\".\"idMenu\" = " + idMenu + "\n";
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

    public JSONArray bucarSubMenuXMenuXPerfilVisible(int idMenu, int idPerfil) throws SQLException, JSONException {
        String consulta = "SELECT DISTINCT \"SubMenu\".\"nombre\",\n"
                + "               \"SubMenu\".\"url\",\n"
                + "               \"SubMenu\".\"imagen\"\n"
                + "	FROM public.\"SubMenu\", public.\"Permiso\"\n"
                + "    WHERE \"SubMenu\".\"idMenu\" = " + idMenu + "\n"
                + "          AND \"Permiso\".\"idPerfil\" = " + idPerfil + "\n"
                + "          AND \"Permiso\".\"idSubMenu\" = \"SubMenu\".\"id\"\n"
                + "          AND \"SubMenu\".\"visible\" = true\n"
                + "    ORDER BY \"SubMenu\".\"nombre\"";
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

    public void updateVisible(int id, boolean visible) throws SQLException {
        String consulta = "UPDATE public.\"SubMenu\"\n"
                + "	SET \"visible\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, visible, id);
    }
}
