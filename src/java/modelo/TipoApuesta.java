package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TipoApuesta {

    private int id;
    private int tipo;
    private int equipo1;
    private int equipo2;
    private Conexion con = null;

    public TipoApuesta(Conexion con) {
        this.con = con;
    }

    public TipoApuesta(int id, int tipo, int equipo1, int equipo2, Conexion con) {
        this.id = id;
        this.tipo = tipo;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.con = con;
    }

    public TipoApuesta(int id, int tipo, int equipo1, int equipo2) {
        this.id = id;
        this.tipo = tipo;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getEquipo1() {
        return equipo1;
    }

    public void setEquipo1(int equipo1) {
        this.equipo1 = equipo1;
    }

    public int getEquipo2() {
        return equipo2;
    }

    public void setEquipo2(int equipo2) {
        this.equipo2 = equipo2;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"TipoApuesta\"(\n"
                + "    \"tipo\", \"equipo1\", \"equipo2\")\n"
                + "    VALUES (?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", tipo, equipo1, equipo2);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"TipoApuesta\"\n"
                + "    SET \"tipo\" = ?, \"equipo1\" = ?, \"equipo2\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, tipo, equipo1, equipo2, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"TipoApuesta\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }
    public void delete(int id) throws SQLException {
        String consulta = "delete from public.\"TipoApuesta\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"TipoApuesta\".\"id\",\n"
                + "    \"TipoApuesta\".\"tipo\",\n"
                + "    \"TipoApuesta\".\"equipo1\",\n"
                + "    \"TipoApuesta\".\"equipo2\"\n"
                + "    FROM public.\"TipoApuesta\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("equipo1", rs.getInt("equipo1"));
            obj.put("equipo2", rs.getInt("equipo2"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"TipoApuesta\".\"id\",\n"
                + "    \"TipoApuesta\".\"tipo\",\n"
                + "    \"TipoApuesta\".\"equipo1\",\n"
                + "    \"TipoApuesta\".\"equipo2\"\n"
                + "    FROM public.\"TipoApuesta\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("equipo1", rs.getInt("equipo1"));
            obj.put("equipo2", rs.getInt("equipo2"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public TipoApuesta buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"TipoApuesta\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        TipoApuesta obj = null;
        if (rs.next()) {
            obj = new TipoApuesta(con);
            obj.setId(rs.getInt("id"));
            obj.setTipo(rs.getInt("tipo"));
            obj.setEquipo1(rs.getInt("equipo1"));
            obj.setEquipo2(rs.getInt("equipo2"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public JSONObject toJSONObject() throws JSONException {    
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("tipo", tipo);
        obj.put("equipo1", equipo1);
        obj.put("equipo2", equipo2);
        return obj;
    }

    

    /* ********************************************************************** */
    // Negocio
    
    public JSONArray todosGoles(int idPartido) throws SQLException, JSONException{
        String consulta = "SELECT *\n" +
                        "FROM\n" +
                        "\"public\".\"TipoApuesta\"\n" +
                        "WHERE\n" +
                        "\"public\".\"TipoApuesta\".tipo = 2\n" +
                        "ORDER BY\n" +
                        "\"public\".\"TipoApuesta\".\"id\" ASC";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("equipo1", rs.getInt("equipo1"));
            obj.put("equipo2", rs.getInt("equipo2"));
            obj.put("porcentaje", getPorcentaje(rs.getInt("id"), idPartido));
            json.put(obj);
        }
        rs.close();
        ps.close();        
        return json;                    
    }
    public JSONArray todosPartido(int idPartido) throws SQLException, JSONException{
        String consulta = "SELECT *\n" +
                        "FROM\n" +
                        "\"public\".\"TipoApuesta\"\n" +
                        "WHERE\n" +
                        "\"public\".\"TipoApuesta\".tipo = 1\n" +
                        "ORDER BY\n" +
                        "\"public\".\"TipoApuesta\".\"id\" ASC";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("equipo1", rs.getInt("equipo1"));
            obj.put("equipo2", rs.getInt("equipo2"));
            obj.put("porcentaje", getPorcentaje(rs.getInt("id"), idPartido));
            json.put(obj);
        }
        rs.close();
        ps.close();        
        return json;                    
    }
    public JSONArray todos(int idPartido) throws SQLException, JSONException{
        String consulta = "SELECT *\n" +
                        "FROM\n" +
                        "\"public\".\"TipoApuesta\"\n" +                        
                        "ORDER BY\n" +
                        "\"public\".\"TipoApuesta\".\"id\" ASC";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("equipo1", rs.getInt("equipo1"));
            obj.put("equipo2", rs.getInt("equipo2"));
            obj.put("porcentaje", getPorcentaje(rs.getInt("id"), idPartido));
            json.put(obj);
        }
        rs.close();
        ps.close();        
        return json;                    
    }
    private double getPorcentaje(int idTipoApuesta, int idPartido) throws SQLException{
        String consulta = "SELECT multiplicador\n" +
                            "FROM\n" +
                            "\"public\".\"ApuestaPartido\"\n" +
                            "WHERE\n" +
                            "\"public\".\"ApuestaPartido\".\"idTipoApuesta\" = ? AND\n" +
                            "\"public\".\"ApuestaPartido\".\"idPartido\" = ?";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, idTipoApuesta);
        ps.setInt(2, idPartido);
        ResultSet rs = ps.executeQuery();
        double res=0;
        if (rs.next()) {            
            res=rs.getDouble("multiplicador");
        }
        rs.close();
        ps.close();        
        return res;
    }
}
