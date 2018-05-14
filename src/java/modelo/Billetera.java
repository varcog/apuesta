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

public class Billetera {

    private int id;
    private int idUsuarioRecibe;
    private Double debe;
    private Double haber;
    private int idUsuarioDa;
    private int TipoTransaccion;
    private int idApuesta;
    private Date fecha;
    private Conexion con = null;

    public Billetera(Conexion con) {
        this.con = con;
    }

    public Billetera(int id, int idUsuarioRecibe, Double debe, Double haber, int idUsuarioDa, int TipoTransaccion, int idApuesta, Date fecha, Conexion con) {
        this.id = id;
        this.idUsuarioRecibe = idUsuarioRecibe;
        this.debe = debe;
        this.haber = haber;
        this.idUsuarioDa = idUsuarioDa;
        this.TipoTransaccion = TipoTransaccion;
        this.idApuesta = idApuesta;
        this.fecha = fecha;
        this.con = con;
    }

    public Billetera(int id, int idUsuarioRecibe, Double debe, Double haber, int idUsuarioDa, int TipoTransaccion, int idApuesta, Date fecha) {
        this.id = id;
        this.idUsuarioRecibe = idUsuarioRecibe;
        this.debe = debe;
        this.haber = haber;
        this.idUsuarioDa = idUsuarioDa;
        this.TipoTransaccion = TipoTransaccion;
        this.idApuesta = idApuesta;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuarioRecibe() {
        return idUsuarioRecibe;
    }

    public void setIdUsuarioRecibe(int idUsuarioRecibe) {
        this.idUsuarioRecibe = idUsuarioRecibe;
    }

    public double getDebe() {
        return debe;
    }

    public void setDebe(Double debe) {
        this.debe = debe;
    }

    public double getHaber() {
        return haber;
    }

    public void setHaber(Double haber) {
        this.haber = haber;
    }

    public int getIdUsuarioDa() {
        return idUsuarioDa;
    }

    public void setIdUsuarioDa(int idUsuarioDa) {
        this.idUsuarioDa = idUsuarioDa;
    }

    public int getTipoTransaccion() {
        return TipoTransaccion;
    }

    public void setTipoTransaccion(int TipoTransaccion) {
        this.TipoTransaccion = TipoTransaccion;
    }

    public int getIdApuesta() {
        return idApuesta;
    }

    public void setIdApuesta(int idApuesta) {
        this.idApuesta = idApuesta;
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
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Billetera\"(\n"
                + "    \"idUsuarioRecibe\", \"debe\", \"haber\", \"idUsuarioDa\", \"TipoTransaccion\", \"idApuesta\", \"fecha\")\n"
                + "    VALUES (?, ?, ?, ?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", idUsuarioRecibe > 0 ? idUsuarioRecibe : null, debe, haber, idUsuarioDa > 0 ? idUsuarioDa : null, TipoTransaccion, idApuesta > 0 ? idApuesta : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()));
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Billetera\"\n"
                + "    SET \"idUsuarioRecibe\" = ?, \"debe\" = ?, \"haber\" = ?, \"idUsuarioDa\" = ?, \"TipoTransaccion\" = ?, \"idApuesta\" = ?, \"fecha\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, idUsuarioRecibe > 0 ? idUsuarioRecibe : null, debe, haber, idUsuarioDa > 0 ? idUsuarioDa : null, TipoTransaccion, idApuesta > 0 ? idApuesta : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Billetera\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Billetera\".\"id\",\n"
                + "    \"Billetera\".\"idUsuarioRecibe\",\n"
                + "    \"Billetera\".\"debe\",\n"
                + "    \"Billetera\".\"haber\",\n"
                + "    \"Billetera\".\"idUsuarioDa\",\n"
                + "    \"Billetera\".\"TipoTransaccion\",\n"
                + "    \"Billetera\".\"idApuesta\",\n"
                + "    to_char(\"Billetera\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "    FROM public.\"Billetera\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioRecibe", rs.getInt("idUsuarioRecibe"));
            obj.put("debe", rs.getDouble("debe"));
            obj.put("haber", rs.getDouble("haber"));
            obj.put("idUsuarioDa", rs.getInt("idUsuarioDa"));
            obj.put("TipoTransaccion", rs.getInt("TipoTransaccion"));
            obj.put("idApuesta", rs.getInt("idApuesta"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Billetera\".\"id\",\n"
                + "    \"Billetera\".\"idUsuarioRecibe\",\n"
                + "    \"Billetera\".\"debe\",\n"
                + "    \"Billetera\".\"haber\",\n"
                + "    \"Billetera\".\"idUsuarioDa\",\n"
                + "    \"Billetera\".\"TipoTransaccion\",\n"
                + "    \"Billetera\".\"idApuesta\",\n"
                + "    to_char(\"Billetera\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "    FROM public.\"Billetera\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioRecibe", rs.getInt("idUsuarioRecibe"));
            obj.put("debe", rs.getDouble("debe"));
            obj.put("haber", rs.getDouble("haber"));
            obj.put("idUsuarioDa", rs.getInt("idUsuarioDa"));
            obj.put("TipoTransaccion", rs.getInt("TipoTransaccion"));
            obj.put("idApuesta", rs.getInt("idApuesta"));
            obj.put("fecha", rs.getString("fecha"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public Billetera buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Billetera\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Billetera obj = null;
        if (rs.next()) {
            obj = new Billetera(con);
            obj.setId(rs.getInt("id"));
            obj.setIdUsuarioRecibe(rs.getInt("idUsuarioRecibe"));
            obj.setDebe(rs.getDouble("debe"));
            obj.setHaber(rs.getDouble("haber"));
            obj.setIdUsuarioDa(rs.getInt("idUsuarioDa"));
            obj.setTipoTransaccion(rs.getInt("TipoTransaccion"));
            obj.setIdApuesta(rs.getInt("idApuesta"));
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
        obj.put("idUsuarioRecibe", idUsuarioRecibe);
        obj.put("debe", debe);
        obj.put("haber", haber);
        obj.put("idUsuarioDa", idUsuarioDa);
        obj.put("TipoTransaccion", TipoTransaccion);
        obj.put("idApuesta", idApuesta);
        obj.put("fecha", fecha == null ? "" : f1.format(fecha));
        return obj;
    }
    /* ********************************************************************** */
    // Negocio

    public JSONArray todosCreditosAsignados() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Billetera\".\"id\",\n"
                + "    \"Billetera\".\"idUsuarioApostador\",\n"
                + "    \"Billetera\".\"debe\",\n"
                + "    \"Billetera\".\"haber\",\n"
                + "    \"Billetera\".\"idUsuarioRelacionador\",\n"
                + "    \"Billetera\".\"TipoTransaccion\",\n"
                + "    \"Billetera\".\"idApuesta\",\n"
                + "    to_char(\"Billetera\".\"fecha\", 'DD/MM/YYYY') AS fecha\n"
                + "    FROM public.\"Billetera\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioApostador", rs.getInt("idUsuarioApostador"));
            obj.put("debe", rs.getDouble("debe"));
            obj.put("haber", rs.getDouble("haber"));
            obj.put("idUsuarioRelacionador", rs.getInt("idUsuarioRelacionador"));
            obj.put("TipoTransaccion", rs.getInt("TipoTransaccion"));
            obj.put("idApuesta", rs.getInt("idApuesta"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray getCreditoDisponible() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Billetera\".\"id\",\n"
                + "    \"Billetera\".\"idUsuarioApostador\",\n"
                + "    \"Billetera\".\"debe\",\n"
                + "    \"Billetera\".\"haber\",\n"
                + "    \"Billetera\".\"idUsuarioRelacionador\",\n"
                + "    \"Billetera\".\"TipoTransaccion\",\n"
                + "    \"Billetera\".\"idApuesta\",\n"
                + "    to_char(\"Billetera\".\"fecha\", 'DD/MM/YYYY') AS fecha\n"
                + "    FROM public.\"Billetera\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioApostador", rs.getInt("idUsuarioApostador"));
            obj.put("debe", rs.getDouble("debe"));
            obj.put("haber", rs.getDouble("haber"));
            obj.put("idUsuarioRelacionador", rs.getInt("idUsuarioRelacionador"));
            obj.put("TipoTransaccion", rs.getInt("TipoTransaccion"));
            obj.put("idApuesta", rs.getInt("idApuesta"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray getDisponibleCredito() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Billetera\".\"id\",\n"
                + "    \"Billetera\".\"idUsuarioApostador\",\n"
                + "    \"Billetera\".\"debe\",\n"
                + "    \"Billetera\".\"haber\",\n"
                + "    \"Billetera\".\"idUsuarioRelacionador\",\n"
                + "    \"Billetera\".\"TipoTransaccion\",\n"
                + "    \"Billetera\".\"idApuesta\",\n"
                + "    to_char(\"Billetera\".\"fecha\", 'DD/MM/YYYY') AS fecha\n"
                + "    FROM public.\"Billetera\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioApostador", rs.getInt("idUsuarioApostador"));
            obj.put("debe", rs.getDouble("debe"));
            obj.put("haber", rs.getDouble("haber"));
            obj.put("idUsuarioRelacionador", rs.getInt("idUsuarioRelacionador"));
            obj.put("TipoTransaccion", rs.getInt("TipoTransaccion"));
            obj.put("idApuesta", rs.getInt("idApuesta"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

}
