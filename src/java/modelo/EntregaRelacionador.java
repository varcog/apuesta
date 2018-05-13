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

public class EntregaRelacionador {

    private int id;
    private int idUsuarioRelacionador;
    private Double monto;
    private int idUsuarioEntrega;
    private Date fecha;
    private Conexion con = null;

    public EntregaRelacionador(Conexion con) {
        this.con = con;
    }

    public EntregaRelacionador(int id, int idUsuarioRelacionador, Double monto, int idUsuarioEntrega, Date fecha, Conexion con) {
        this.id = id;
        this.idUsuarioRelacionador = idUsuarioRelacionador;
        this.monto = monto;
        this.idUsuarioEntrega = idUsuarioEntrega;
        this.fecha = fecha;
        this.con = con;
    }

    public EntregaRelacionador(int id, int idUsuarioRelacionador, Double monto, int idUsuarioEntrega, Date fecha) {
        this.id = id;
        this.idUsuarioRelacionador = idUsuarioRelacionador;
        this.monto = monto;
        this.idUsuarioEntrega = idUsuarioEntrega;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuarioRelacionador() {
        return idUsuarioRelacionador;
    }

    public void setIdUsuarioRelacionador(int idUsuarioRelacionador) {
        this.idUsuarioRelacionador = idUsuarioRelacionador;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public int getIdUsuarioEntrega() {
        return idUsuarioEntrega;
    }

    public void setIdUsuarioEntrega(int idUsuarioEntrega) {
        this.idUsuarioEntrega = idUsuarioEntrega;
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
        String consulta = "INSERT INTO public.\"EntregaRelacionador\"(\n"
                + "    \"idUsuarioRelacionador\", \"monto\", \"idUsuarioEntrega\", \"fecha\")\n"
                + "    VALUES (?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", idUsuarioRelacionador > 0 ? idUsuarioRelacionador : null, monto, idUsuarioEntrega > 0 ? idUsuarioEntrega : null, fecha == null ? null : new java.sql.Date(fecha.getTime()));
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"EntregaRelacionador\"\n"
                + "    SET \"idUsuarioRelacionador\" = ?, \"monto\" = ?, \"idUsuarioEntrega\" = ?, \"fecha\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, idUsuarioRelacionador > 0 ? idUsuarioRelacionador : null, monto, idUsuarioEntrega > 0 ? idUsuarioEntrega : null, fecha == null ? null : new java.sql.Date(fecha.getTime()), id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"EntregaRelacionador\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"EntregaRelacionador\".\"id\",\n"
                + "    \"EntregaRelacionador\".\"idUsuarioRelacionador\",\n"
                + "    \"EntregaRelacionador\".\"monto\",\n"
                + "    \"EntregaRelacionador\".\"idUsuarioEntrega\",\n"
                + "    to_char(\"EntregaRelacionador\".\"fecha\", 'DD/MM/YYYY') AS fecha\n"
                + "    FROM public.\"EntregaRelacionador\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioRelacionador", rs.getInt("idUsuarioRelacionador"));
            obj.put("monto", rs.getDouble("monto"));
            obj.put("idUsuarioEntrega", rs.getInt("idUsuarioEntrega"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"EntregaRelacionador\".\"id\",\n"
                + "    \"EntregaRelacionador\".\"idUsuarioRelacionador\",\n"
                + "    \"EntregaRelacionador\".\"monto\",\n"
                + "    \"EntregaRelacionador\".\"idUsuarioEntrega\",\n"
                + "    to_char(\"EntregaRelacionador\".\"fecha\", 'DD/MM/YYYY') AS fecha\n"
                + "    FROM public.\"EntregaRelacionador\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioRelacionador", rs.getInt("idUsuarioRelacionador"));
            obj.put("monto", rs.getDouble("monto"));
            obj.put("idUsuarioEntrega", rs.getInt("idUsuarioEntrega"));
            obj.put("fecha", rs.getString("fecha"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public EntregaRelacionador buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"EntregaRelacionador\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        EntregaRelacionador obj = null;
        if (rs.next()) {
            obj = new EntregaRelacionador(con);
            obj.setId(rs.getInt("id"));
            obj.setIdUsuarioRelacionador(rs.getInt("idUsuarioRelacionador"));
            obj.setMonto(rs.getDouble("monto"));
            obj.setIdUsuarioEntrega(rs.getInt("idUsuarioEntrega"));
            obj.setFecha(rs.getDate("fecha"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public JSONObject toJSONObject() throws JSONException {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("idUsuarioRelacionador", idUsuarioRelacionador);
        obj.put("monto", monto);
        obj.put("idUsuarioEntrega", idUsuarioEntrega);
        obj.put("fecha", fecha == null ? "" : f.format(fecha));
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
    public JSONArray todosNombres() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"EntregaRelacionador\".\"id\",\n"
                + "    \"EntregaRelacionador\".\"idUsuarioRelacionador\",\n"
                + "    \"relacionador\".\"nombres\" || ' ' || \"relacionador\".\"apellidos\" as relacionador,"
                + "    \"EntregaRelacionador\".\"monto\",\n"
                + "    \"EntregaRelacionador\".\"idUsuarioEntrega\",\n"
                + "    \"entrega\".\"nombres\" || ' ' || \"entrega\".\"apellidos\" as entrega,"
                + "    to_char(\"EntregaRelacionador\".\"fecha\", 'DD/MM/YYYY') AS fecha\n"
                + "    FROM public.\"EntregaRelacionador\"\n"
                + "         INNER JOIN public.\"Usuario\" AS relacionador ON \"EntregaRelacionador\".\"idUsuarioRelacionador\" = \"relacionador\".\"id\"\n"
                + "         INNER JOIN public.\"Usuario\" AS entrega ON \"EntregaRelacionador\".\"idUsuarioEntrega\" = \"entrega\".\"id\"\n"
                + "    ORDER BY \"EntregaRelacionador\".\"fecha\"\n";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioRelacionador", rs.getInt("idUsuarioRelacionador"));
            obj.put("relacionador", rs.getString("relacionador"));
            obj.put("monto", rs.getDouble("monto"));
            obj.put("idUsuarioEntrega", rs.getInt("idUsuarioEntrega"));
            obj.put("entrega", rs.getString("entrega"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject BuscarConNombres() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"EntregaRelacionador\".\"id\",\n"
                + "    \"EntregaRelacionador\".\"idUsuarioRelacionador\",\n"
                + "    \"relacionador\".\"nombres\" || ' ' || \"relacionador\".\"apellidos\" as relacionador,"
                + "    \"EntregaRelacionador\".\"monto\",\n"
                + "    \"EntregaRelacionador\".\"idUsuarioEntrega\",\n"
                + "    \"entrega\".\"nombres\" || ' ' || \"entrega\".\"apellidos\" as entrega,"
                + "    to_char(\"EntregaRelacionador\".\"fecha\", 'DD/MM/YYYY') AS fecha\n"
                + "    FROM public.\"EntregaRelacionador\"\n"
                + "         INNER JOIN public.\"Usuario\" AS relacionador ON \"EntregaRelacionador\".\"idUsuarioRelacionador\" = \"relacionador\".\"id\"\n"
                + "         INNER JOIN public.\"Usuario\" AS entrega ON \"EntregaRelacionador\".\"idUsuarioEntrega\" = \"entrega\".\"id\"\n"
                + "    WHERE \"EntregaRelacionador\".\"id\" = " + id + " \n";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject json = null;
        if (rs.next()) {
            json = new JSONObject();
            json.put("id", rs.getInt("id"));
            json.put("idUsuarioRelacionador", rs.getInt("idUsuarioRelacionador"));
            json.put("relacionador", rs.getString("relacionador"));
            json.put("monto", rs.getDouble("monto"));
            json.put("idUsuarioEntrega", rs.getInt("idUsuarioEntrega"));
            json.put("entrega", rs.getString("entrega"));
            json.put("fecha", rs.getString("fecha"));
        }
        rs.close();
        ps.close();
        return json;
    }

}
