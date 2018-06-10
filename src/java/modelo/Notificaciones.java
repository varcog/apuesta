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

public class Notificaciones {

    private int id;
    private int idUsuario;
    private int idUsuarioEnvia;
    private String descripcion;
    private int tipo;
    private int estadoVisto;
    private Date fecha;
    private Conexion con = null;

    public Notificaciones(Conexion con) {
        this.con = con;
    }

    public Notificaciones(int id, int idUsuario, int idUsuarioEnvia, String descripcion, int tipo, int estadoVisto, Date fecha, Conexion con) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idUsuarioEnvia = idUsuarioEnvia;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.estadoVisto = estadoVisto;
        this.fecha = fecha;
        this.con = con;
    }

    public Notificaciones(int id, int idUsuario, int idUsuarioEnvia, String descripcion, int tipo, int estadoVisto, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idUsuarioEnvia = idUsuarioEnvia;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.estadoVisto = estadoVisto;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdUsuarioEnvia() {
        return idUsuarioEnvia;
    }

    public void setIdUsuarioEnvia(int idUsuarioEnvia) {
        this.idUsuarioEnvia = idUsuarioEnvia;
    }        

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getEstadoVisto() {
        return estadoVisto;
    }

    public void setEstadoVisto(int estadoVisto) {
        this.estadoVisto = estadoVisto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public void setDatos(int id, int idUsuario, int idUsuarioEnvia, String descripcion, int tipo, int estadoVisto, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idUsuarioEnvia = idUsuarioEnvia;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.estadoVisto = estadoVisto;
        this.fecha = fecha;
    }

    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Notificaciones\"(\n"
                + "    \"idUsuario\", \"idUsuarioEnvia\", \"descripcion\", \"tipo\", \"estadoVisto\", \"fecha\")\n"
                + "    VALUES (?, ?, ?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", idUsuario > 0 ? idUsuario : null, idUsuarioEnvia > 0 ? idUsuarioEnvia : null, descripcion, tipo, estadoVisto, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()));
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Notificaciones\"\n"
                + "    SET \"idUsuario\" = ?,\"idUsuarioEnvia\" = ?, \"descripcion\" = ?, \"tipo\" = ?, \"estadoVisto\" = ?, \"fecha\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, idUsuario > 0 ? idUsuario : null, idUsuarioEnvia > 0 ? idUsuarioEnvia : null, descripcion, tipo, estadoVisto, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), id);
    }
    public void updateVisto(int idUsuario) throws SQLException {
        String consulta = "update \"public\".\"Notificaciones\" set \"estadoVisto\" = 1\n" +
                            "where \"public\".\"Notificaciones\".\"idUsuario\" = ?;";
        con.ejecutarSentencia(consulta, idUsuario);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Notificaciones\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Notificaciones\".\"id\",\n"
                + "    \"Notificaciones\".\"idUsuario\",\n"
                + "    \"Notificaciones\".\"idUsuarioEnvia\",\n"
                + "    \"Notificaciones\".\"descripcion\",\n"
                + "    \"Notificaciones\".\"tipo\",\n"
                + "    \"Notificaciones\".\"estadoVisto\",\n"
                + "    to_char(\"Notificaciones\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "    FROM public.\"Notificaciones\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuario", rs.getInt("idUsuario"));
            obj.put("idUsuarioEnvia", rs.getInt("idUsuarioEnvia"));
            obj.put("descripcion", rs.getString("descripcion"));
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("estadoVisto", rs.getInt("estadoVisto"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Notificaciones\".\"id\",\n"
                + "    \"Notificaciones\".\"idUsuario\",\n"
                + "    \"Notificaciones\".\"idUsuarioEnvia\",\n"
                + "    \"Notificaciones\".\"descripcion\",\n"
                + "    \"Notificaciones\".\"tipo\",\n"
                + "    \"Notificaciones\".\"estadoVisto\",\n"
                + "    to_char(\"Notificaciones\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "    FROM public.\"Notificaciones\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuario", rs.getInt("idUsuario"));
            obj.put("idUsuarioEnvia", rs.getInt("idUsuarioEnvia"));
            obj.put("descripcion", rs.getString("descripcion"));
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("estadoVisto", rs.getInt("estadoVisto"));
            obj.put("fecha", rs.getString("fecha"));
        }
        rs.close();
        ps.close();
        return obj;
    }
    public JSONArray buscarJSONArray(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Notificaciones\".\"id\",\n"
                + "    \"Notificaciones\".\"idUsuario\",\n"
                + "    \"Notificaciones\".\"idUsuarioEnvia\",\n"
                + "    \"Notificaciones\".\"descripcion\",\n"
                + "    \"Notificaciones\".\"tipo\",\n"
                + "    \"Notificaciones\".\"estadoVisto\",\n"
                + "    to_char(\"Notificaciones\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "    FROM public.\"Notificaciones\"\n"
                + "    WHERE \"idUsuario\" = ? ORDER BY id desc;";
        PreparedStatement ps = con.statametObject(consulta, idUsuario);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();        
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuario", rs.getInt("idUsuario"));
            obj.put("idUsuarioEnvia", rs.getInt("idUsuarioEnvia"));
            obj.put("descripcion", rs.getString("descripcion"));
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("estadoVisto", rs.getInt("estadoVisto"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }
    
    public JSONArray todas(int idUsuario) throws SQLException, JSONException{
        String consulta = "SELECT\n" +
                        "\"public\".\"Usuario\".nombres||' '||\"public\".\"Usuario\".apellidos as nombre,\n" +
                        "\"public\".\"Usuario\".foto,\n" +
                        "to_char(\"public\".\"Notificaciones\".fecha, 'DD/MM/YYYY HH24:MI:SS') as fecha,\n" +
                        "\"public\".\"Notificaciones\".descripcion,\n" +
                        "\"public\".\"Notificaciones\".\"estadoVisto\",\n" +
                        "\"public\".\"Notificaciones\".tipo\n" +
                        "FROM\n" +
                        "\"public\".\"Usuario\"\n" +
                        "INNER JOIN \"public\".\"Notificaciones\" ON \"public\".\"Notificaciones\".\"idUsuarioEnvia\" = \"public\".\"Usuario\".\"id\"\n"+
                        "WHERE  \"public\".\"Notificaciones\".\"idUsuario\" = ? order by \"public\".\"Notificaciones\".id desc";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, idUsuario);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("nombre",rs.getString("nombre"));
            obj.put("foto",rs.getString("foto"));
            obj.put("fecha",rs.getString("fecha"));
            obj.put("descripcion",rs.getString("descripcion"));
            obj.put("estadoVisto",rs.getInt("estadoVisto"));
            obj.put("tipo",rs.getInt("tipo"));
            json.put(obj);
        }        
        rs.close();
        ps.close();
        return json;
    }

    public Notificaciones buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Notificaciones\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Notificaciones obj = null;
        if (rs.next()) {
            obj = new Notificaciones(con);
            obj.setId(rs.getInt("id"));
            obj.setIdUsuario(rs.getInt("idUsuario"));
            obj.setIdUsuario(rs.getInt("idUsuarioEnvia"));
            obj.setDescripcion(rs.getString("descripcion"));
            obj.setTipo(rs.getInt("tipo"));
            obj.setEstadoVisto(rs.getInt("estadoVisto"));
            obj.setFecha(rs.getTimestamp("fecha"));
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
        obj.put("idUsuario", idUsuario);
        obj.put("idUsuarioEnvia", idUsuarioEnvia);
        obj.put("descripcion", descripcion);
        obj.put("tipo", tipo);
        obj.put("estadoVisto", estadoVisto);
        obj.put("fecha", fecha == null ? "" : f1.format(fecha));
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
}
