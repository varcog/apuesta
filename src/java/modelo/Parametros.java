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

public class Parametros {

    private int id;
    private String urlBackup;
    private Conexion con = null;

    public Parametros(Conexion con) {
        this.con = con;
    }

    public Parametros(int id, String urlBackup, Conexion con) {
        this.id = id;
        this.urlBackup = urlBackup;
        this.con = con;
    }

    public Parametros(int id, String urlBackup) {
        this.id = id;
        this.urlBackup = urlBackup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlBackup() {
        return urlBackup == null ? "" : urlBackup;
    }

    public void setUrlBackup(String urlBackup) {
        this.urlBackup = urlBackup;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Parametros\"("
                + "    \"urlBackup\")"
                + "    VALUES (?)";
        this.id = con.ejecutarInsert(consulta, "id", urlBackup);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Parametros\""
                + "    SET \"urlBackup\" = ?"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, urlBackup, id);
    }
    
    
    public void delete() throws SQLException {
        String consulta = "delete from public.\"Parametros\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT"
                + "    \"Parametros\".id,"
                + "    \"Parametros\".urlBackup"
                + "    FROM public.Parametros;";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("urlBackup", rs.getString("urlBackup"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT"
                + "    \"Parametros\".id,"
                + "    \"Parametros\".urlBackup"
                + "    FROM public.Parametros"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("urlBackup", rs.getString("urlBackup"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public Parametros buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *"
                + "    FROM public.Parametros"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Parametros obj = null;
        if (rs.next()) {
            obj = new Parametros(con);
            obj.setId(rs.getInt("id"));
            obj.setUrlBackup(rs.getString("urlBackup"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public JSONObject toJSONObject() throws JSONException {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("urlBackup", urlBackup);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
    
    public String getRutaBakup() throws SQLException{
        String consulta = "SELECT \"urlBackup\""
                + "    FROM public.\"Parametros\"";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        String ruta="";
        if(rs.next()) ruta=rs.getString("urlBackup");
        rs.close();
        ps.close();
        return ruta;
    }
}
