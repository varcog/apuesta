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

public class PagoEfectivo {

    private int id;
    private int idPrestamo;
    private int idBilletera;
    private int idUsuarioRecibe;
    private Double monto;
    private int idUsuarioDa;
    private Date fecha;
    private int tipo;
    private Conexion con = null;

    public PagoEfectivo(Conexion con) {
        this.con = con;
    }

    public PagoEfectivo(int id, int idPrestamo, int idBilletera, int idUsuarioRecibe, Double monto, int idUsuarioDa, Date fecha, int tipo, Conexion con) {
        this.id = id;
        this.idPrestamo = idPrestamo;
        this.idBilletera = idBilletera;
        this.idUsuarioRecibe = idUsuarioRecibe;
        this.monto = monto;
        this.idUsuarioDa = idUsuarioDa;
        this.fecha = fecha;
        this.tipo = tipo;
        this.con = con;
    }

    public PagoEfectivo(int id, int idPrestamo, int idBilletera, int idUsuarioRecibe, Double monto, int idUsuarioDa, Date fecha, int tipo) {
        this.id = id;
        this.idPrestamo = idPrestamo;
        this.idBilletera = idBilletera;
        this.idUsuarioRecibe = idUsuarioRecibe;
        this.monto = monto;
        this.idUsuarioDa = idUsuarioDa;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getIdBilletera() {
        return idBilletera;
    }

    public void setIdBilletera(int idBilletera) {
        this.idBilletera = idBilletera;
    }

    public int getIdUsuarioRecibe() {
        return idUsuarioRecibe;
    }

    public void setIdUsuarioRecibe(int idUsuarioRecibe) {
        this.idUsuarioRecibe = idUsuarioRecibe;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public int getIdUsuarioDa() {
        return idUsuarioDa;
    }

    public void setIdUsuarioDa(int idUsuarioDa) {
        this.idUsuarioDa = idUsuarioDa;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public void setDatos(int id, int idPrestamo, int idBilletera, int idUsuarioRecibe, Double monto, int idUsuarioDa, Date fecha, int tipo) {
        this.id = id;
        this.idPrestamo = idPrestamo;
        this.idBilletera = idBilletera;
        this.idUsuarioRecibe = idUsuarioRecibe;
        this.monto = monto;
        this.idUsuarioDa = idUsuarioDa;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"PagoEfectivo\"(\n"
                + "    \"idPrestamo\", \"idBilletera\", \"idUsuarioRecibe\", \"monto\", \"idUsuarioDa\", \"fecha\", \"tipo\")\n"
                + "    VALUES (?, ?, ?, ?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", idPrestamo > 0 ? idPrestamo : null, idBilletera > 0 ? idBilletera : null, idUsuarioRecibe > 0 ? idUsuarioRecibe : null, monto, idUsuarioDa > 0 ? idUsuarioDa : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), tipo);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"PagoEfectivo\"\n"
                + "    SET \"idPrestamo\" = ?, \"idBilletera\" = ?, \"idUsuarioRecibe\" = ?, \"monto\" = ?, \"idUsuarioDa\" = ?, \"fecha\" = ?, \"tipo\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, idPrestamo > 0 ? idPrestamo : null, idBilletera > 0 ? idBilletera : null, idUsuarioRecibe > 0 ? idUsuarioRecibe : null, monto, idUsuarioDa > 0 ? idUsuarioDa : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), tipo, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"PagoEfectivo\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"PagoEfectivo\".\"id\",\n"
                + "    \"PagoEfectivo\".\"idPrestamo\",\n"
                + "    \"PagoEfectivo\".\"idBilletera\",\n"
                + "    \"PagoEfectivo\".\"idUsuarioRecibe\",\n"
                + "    \"PagoEfectivo\".\"monto\",\n"
                + "    \"PagoEfectivo\".\"idUsuarioDa\",\n"
                + "    to_char(\"PagoEfectivo\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha,\n"
                + "    \"PagoEfectivo\".\"tipo\"\n"
                + "    FROM public.\"PagoEfectivo\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idPrestamo", rs.getInt("idPrestamo"));
            obj.put("idBilletera", rs.getInt("idBilletera"));
            obj.put("idUsuarioRecibe", rs.getInt("idUsuarioRecibe"));
            obj.put("monto", rs.getDouble("monto"));
            obj.put("idUsuarioDa", rs.getInt("idUsuarioDa"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("tipo", rs.getInt("tipo"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"PagoEfectivo\".\"id\",\n"
                + "    \"PagoEfectivo\".\"idPrestamo\",\n"
                + "    \"PagoEfectivo\".\"idBilletera\",\n"
                + "    \"PagoEfectivo\".\"idUsuarioRecibe\",\n"
                + "    \"PagoEfectivo\".\"monto\",\n"
                + "    \"PagoEfectivo\".\"idUsuarioDa\",\n"
                + "    to_char(\"PagoEfectivo\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha,\n"
                + "    \"PagoEfectivo\".\"tipo\"\n"
                + "    FROM public.\"PagoEfectivo\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("idPrestamo", rs.getInt("idPrestamo"));
            obj.put("idBilletera", rs.getInt("idBilletera"));
            obj.put("idUsuarioRecibe", rs.getInt("idUsuarioRecibe"));
            obj.put("monto", rs.getDouble("monto"));
            obj.put("idUsuarioDa", rs.getInt("idUsuarioDa"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("tipo", rs.getInt("tipo"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public PagoEfectivo buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"PagoEfectivo\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        PagoEfectivo obj = null;
        if (rs.next()) {
            obj = new PagoEfectivo(con);
            obj.setId(rs.getInt("id"));
            obj.setIdPrestamo(rs.getInt("idPrestamo"));
            obj.setIdBilletera(rs.getInt("idBilletera"));
            obj.setIdUsuarioRecibe(rs.getInt("idUsuarioRecibe"));
            obj.setMonto(rs.getDouble("monto"));
            obj.setIdUsuarioDa(rs.getInt("idUsuarioDa"));
            obj.setFecha(rs.getTimestamp("fecha"));
            obj.setTipo(rs.getInt("tipo"));
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
        obj.put("idPrestamo", idPrestamo);
        obj.put("idBilletera", idBilletera);
        obj.put("idUsuarioRecibe", idUsuarioRecibe);
        obj.put("monto", monto);
        obj.put("idUsuarioDa", idUsuarioDa);
        obj.put("fecha", fecha == null ? "" : f1.format(fecha));
        obj.put("tipo", tipo);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
    public static final int TIPO_RETIRO = 1;
    public static final int TIPO_PAGO_PRESTAMO = 2;
    public static final int TIPO_COMPRA = 3;
    public static final int TIPO_TRASPASO = 4;

    public double getEfectivoDisponible(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT (\n"
                + "	SELECT SUM(\"PagoEfectivo\".\"monto\")\n"
                + "    FROM public.\"PagoEfectivo\"\n"
                + "    WHERE \"idUsuarioRecibe\" = ?\n"
                + "      AND (\"Tipo\" = ? OR \"Tipo\" = ? OR \"Tipo\" = ?)\n"
                + ") AS \"recibe\",\n"
                + "(\n"
                + "	SELECT SUM(\"PagoEfectivo\".\"monto\")\n"
                + "    FROM public.\"PagoEfectivo\"\n"
                + "    WHERE \"idUsuarioDa\" = ?\n"
                + "      AND (\"Tipo\" = ? OR \"Tipo\" = ?)\n"
                + "\n"
                + ") AS \"da\";";
        PreparedStatement ps = con.statametObject(consulta, idUsuario, TIPO_PAGO_PRESTAMO, TIPO_TRASPASO, TIPO_COMPRA, idUsuario, TIPO_RETIRO, TIPO_TRASPASO);
        ResultSet rs = ps.executeQuery();
        double efectivo = 0;
        if (rs.next()) {
            efectivo = rs.getDouble("recibe") - rs.getDouble("da");
        }
        rs.close();
        ps.close();
        return efectivo;
    }

    public JSONObject TraspasoEfectivo(double monto, int idUsuarioDa, int idUsuarioRecibe) throws SQLException, JSONException {
        JSONObject json = new JSONObject();
        if (monto < 0) {
            json.put("resp", "MONTO_0");
            return json;
        }
        double saldo = getEfectivoDisponible(idUsuarioDa);
        if (saldo < monto) {
            json.put("resp", "CREDITO_INSUFICIENTE");
            json.put("credito", saldo);
            return json;
        }
        Date fechaInsert = new Date();
        setDatos(0, 0, 0, idUsuarioRecibe, monto, idUsuarioDa, fechaInsert, TIPO_TRASPASO);
        insert();
        json.put("efectivoUsado", getEfectivoDisponible(idUsuarioDa));
        return json;
    }
}
