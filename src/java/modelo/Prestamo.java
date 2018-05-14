package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.SisEventos;

public class Prestamo {

    private int id;
    private int idUsuario;
    private Double debe;
    private Double haber;
    private int idBilletera;
    private Date fecha;
    private Conexion con = null;

    public Prestamo(Conexion con) {
        this.con = con;
    }

    public Prestamo(int id, int idUsuario, Double debe, Double haber, int idBilletera, Date fecha, Conexion con) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.debe = debe;
        this.haber = haber;
        this.idBilletera = idBilletera;
        this.fecha = fecha;
        this.con = con;
    }

    public Prestamo(int id, int idUsuario, Double debe, Double haber, int idBilletera, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.debe = debe;
        this.haber = haber;
        this.idBilletera = idBilletera;
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

    public int getIdBilletera() {
        return idBilletera;
    }

    public void setIdBilletera(int idBilletera) {
        this.idBilletera = idBilletera;
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
        String consulta = "INSERT INTO public.\"Prestamo\"(\n"
                + "    \"idUsuario\", \"debe\", \"haber\", \"idBilletera\", \"fecha\")\n"
                + "    VALUES (?, ?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", idUsuario > 0 ? idUsuario : null, debe, haber, idBilletera > 0 ? idBilletera : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()));
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Prestamo\"\n"
                + "    SET \"idUsuario\" = ?, \"debe\" = ?, \"haber\" = ?, \"idBilletera\" = ?, \"fecha\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, idUsuario > 0 ? idUsuario : null, debe, haber, idBilletera > 0 ? idBilletera : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Prestamo\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Prestamo\".\"id\",\n"
                + "    \"Prestamo\".\"idUsuario\",\n"
                + "    \"Prestamo\".\"debe\",\n"
                + "    \"Prestamo\".\"haber\",\n"
                + "    \"Prestamo\".\"idBilletera\",\n"
                + "    to_char(\"Prestamo\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "    FROM public.\"Prestamo\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuario", rs.getInt("idUsuario"));
            obj.put("debe", rs.getDouble("debe"));
            obj.put("haber", rs.getDouble("haber"));
            obj.put("idBilletera", rs.getInt("idBilletera"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Prestamo\".\"id\",\n"
                + "    \"Prestamo\".\"idUsuario\",\n"
                + "    \"Prestamo\".\"debe\",\n"
                + "    \"Prestamo\".\"haber\",\n"
                + "    \"Prestamo\".\"idBilletera\",\n"
                + "    to_char(\"Prestamo\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "    FROM public.\"Prestamo\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuario", rs.getInt("idUsuario"));
            obj.put("debe", rs.getDouble("debe"));
            obj.put("haber", rs.getDouble("haber"));
            obj.put("idBilletera", rs.getInt("idBilletera"));
            obj.put("fecha", rs.getString("fecha"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public Prestamo buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Prestamo\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Prestamo obj = null;
        if (rs.next()) {
            obj = new Prestamo(con);
            obj.setId(rs.getInt("id"));
            obj.setIdUsuario(rs.getInt("idUsuario"));
            obj.setDebe(rs.getDouble("debe"));
            obj.setHaber(rs.getDouble("haber"));
            obj.setIdBilletera(rs.getInt("idBilletera"));
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
        obj.put("debe", debe);
        obj.put("haber", haber);
        obj.put("idBilletera", idBilletera);
        obj.put("fecha", fecha == null ? "" : f1.format(fecha));
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
    public JSONArray todosPrestatarios() throws SQLException, JSONException, ParseException {
        String consulta = "SELECT\n"
                + "    \"Prestamo\".\"idUsuario\",\n"
                + "    \"Usuario\".\"nombres\" || ' ' || \"Usuario\".\"apellidos\" AS nombre,\n"
                + "    SUM(\"Prestamo\".\"debe\") AS debe,\n"
                + "    SUM(\"Prestamo\".\"haber\") AS haber\n"
                + "    FROM public.\"Prestamo\"\n"
                + "         INNER JOUN public.\"Usuario\" ON \"Usuario\".\"id\" = \"Prestamo\".\"idUsuario\"\n"
                + "    GROUP BY \"Prestamo\".\"idUsuario\",\n"
                + "             \"Usuario\".\"nombres\",\n"
                + "             \"Usuario\".\"apellidos\"\n"
                + "    ORDER BY \"Usuario\".\"nombres\",\n"
                + "             \"Usuario\".\"apellidos\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        double deuda;
        while (rs.next()) {
            deuda = rs.getDouble("haber") - rs.getDouble("debe");
            deuda = SisEventos.acomodarDosDecimalesD(deuda);
            if (deuda > 0) {
                obj = new JSONObject();
                obj.put("id", rs.getInt("id"));
                obj.put("idUsuario", rs.getInt("idUsuario"));
                obj.put("nombre", rs.getDouble("nombre"));
                obj.put("deuda", deuda);
                json.put(obj);
            }
        }
        rs.close();
        ps.close();
        return json;
    }

    public double getMontoPrestado() {
        return 0;
    }
}
