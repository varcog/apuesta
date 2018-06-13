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
    public void setDatos(int id, int idUsuario, Double debe, Double haber, int idBilletera, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.debe = debe;
        this.haber = haber;
        this.idBilletera = idBilletera;
        this.fecha = fecha;
    }

    public JSONArray todosPrestatarios() throws SQLException, JSONException, ParseException {
        String consulta = "SELECT\n"
                + "    \"Prestamo\".\"idUsuario\",\n"
                + "    \"Usuario\".\"nombres\" || ' ' || \"Usuario\".\"apellidos\" AS nombre,\n"
                + "    SUM(\"Prestamo\".\"debe\") AS debe,\n"
                + "    SUM(\"Prestamo\".\"haber\") AS haber\n"
                + "    FROM public.\"Prestamo\"\n"
                + "         INNER JOIN public.\"Usuario\" ON \"Usuario\".\"id\" = \"Prestamo\".\"idUsuario\"\n"
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
            if (deuda < 0) {
                deuda = SisEventos.acomodarDosDecimalesD(deuda * -1);
                obj = new JSONObject();
                obj.put("idUsuario", rs.getInt("idUsuario"));
                obj.put("nombre", rs.getString("nombre"));
                obj.put("deuda", deuda);
                json.put(obj);
            }
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject getPrestatario(int idUsuario) throws SQLException, JSONException, ParseException {
        String consulta = "SELECT\n"
                + "    \"Prestamo\".\"idUsuario\",\n"
                + "    \"Usuario\".\"nombres\" || ' ' || \"Usuario\".\"apellidos\" AS nombre,\n"
                + "    SUM(\"Prestamo\".\"debe\") AS debe,\n"
                + "    SUM(\"Prestamo\".\"haber\") AS haber\n"
                + "    FROM public.\"Prestamo\"\n"
                + "         INNER JOIN public.\"Usuario\" ON \"Usuario\".\"id\" = \"Prestamo\".\"idUsuario\"\n"
                + "    WHERE \"Prestamo\".\"idUsuario\" = ?\n"
                + "    GROUP BY \"Prestamo\".\"idUsuario\",\n"
                + "             \"Usuario\".\"nombres\",\n"
                + "             \"Usuario\".\"apellidos\"\n"
                + "    ORDER BY \"Usuario\".\"nombres\",\n"
                + "             \"Usuario\".\"apellidos\";";
        PreparedStatement ps = con.statametObject(consulta, idUsuario);
        ResultSet rs = ps.executeQuery();
        JSONObject json = null;
        double deuda;
        if (rs.next()) {
            deuda = rs.getDouble("haber") - rs.getDouble("debe");
            deuda = SisEventos.acomodarDosDecimalesD(deuda);
            if (deuda < 0) {
                deuda = SisEventos.acomodarDosDecimalesD(deuda * -1);
                json = new JSONObject();
                json.put("idUsuario", rs.getInt("idUsuario"));
                json.put("nombre", rs.getString("nombre"));
                json.put("deuda", deuda);
            }
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject getPrestamoUsuarioPerfil(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    SUM(\"Prestamo\".\"debe\") AS debe,\n"
                + "    SUM(\"Prestamo\".\"haber\") AS haber\n"
                + "    FROM public.\"Prestamo\"\n"
                + "    WHERE \"Prestamo\".\"idUsuario\" = ?\n";
        PreparedStatement ps = con.statametObject(consulta, idUsuario);
        ResultSet rs = ps.executeQuery();
        JSONObject json = null;
        if (rs.next()) {
            json = new JSONObject();
            json.put("debe", rs.getDouble("debe"));
            json.put("haber", rs.getDouble("haber"));
        }
        rs.close();
        ps.close();
        return json;
    }

    public double getDeuda(int idUsuario) throws SQLException, ParseException {
        String consulta = "SELECT\n"
                + "    SUM(\"Prestamo\".\"debe\") AS debe,\n"
                + "    SUM(\"Prestamo\".\"haber\") AS haber\n"
                + "    FROM public.\"Prestamo\"\n"
                + "    WHERE \"Prestamo\".\"idUsuario\" = ?\n";
        PreparedStatement ps = con.statametObject(consulta, idUsuario);
        ResultSet rs = ps.executeQuery();
        double deuda = 0;
        if (rs.next()) {
            deuda = rs.getDouble("haber") - rs.getDouble("debe");
            deuda = SisEventos.acomodarDosDecimalesD(deuda);
            deuda = deuda < 0 ? SisEventos.acomodarDosDecimalesD(deuda * -1) : 0;
        }
        rs.close();
        ps.close();
        return deuda;
    }

    public JSONObject pagarPrestamoCredito(double monto, int idUsuario) throws SQLException, JSONException, ParseException {
        JSONObject json = new JSONObject();
        if (monto <= 0) {
            json.put("resp", "MONTO_0");
            return json;
        }
        Billetera b = new Billetera(con);
        double saldo = b.getCreditoDisponible(idUsuario);
        if (saldo < monto) {
            json.put("resp", "CREDITO_INSUFICIENTE");
            json.put("credito", saldo);
            return json;
        }
        double deuda = getDeuda(idUsuario);
        if (deuda <= 0) {
            json.put("resp", "DEUDA_0");
            return json;
        }
        monto = SisEventos.acomodarDosDecimalesD(monto);
        double pago = deuda < monto ? deuda : monto;
        pago = SisEventos.acomodarDosDecimalesD(pago);
        Date fechaInsert = new Date();
        b.setDatos(0, new Usuario(con).getIdCasa(), pago, idUsuario, Billetera.TIPO_TRANSACCION_PAGO_PRESTAMO, 0, 0, fechaInsert);
        b.insert();
        setDatos(0, idUsuario, 0.0, pago, b.getId(), fechaInsert);
        insert();
        json.put("balance", b.getBalanceUsuario(idUsuario));
        json.put("prestamos", getPrestamoUsuarioPerfil(idUsuario));
        json.put("transacciones", b.getTransaccionesUsuariosPerfil(idUsuario));
        json.put("credito", b.getCreditoDisponible(idUsuario));
        json.put("creditoUsado", pago);
        return json;
    }

    public JSONObject pagarPrestamoEfectivo(double monto, int idUsuarioDa, int idUsuarioRecibo) throws SQLException, JSONException, ParseException {
        JSONObject json = new JSONObject();
        if (monto < 0) {
            json.put("resp", "MONTO_0");
            return json;
        }
        double deuda = getDeuda(idUsuarioDa);
        if (deuda <= 0) {
            json.put("resp", "DEUDA_0");
            return json;
        }
        monto = SisEventos.acomodarDosDecimalesD(monto);
        double pago = deuda < monto ? deuda : monto;
        pago = SisEventos.acomodarDosDecimalesD(pago);
        Date fechaInsert = new Date();
        setDatos(0, idUsuarioDa, 0.0, pago, 0, fechaInsert);
        insert();
        PagoEfectivo pe = new PagoEfectivo(0, getId(), 0, idUsuarioDa, pago, idUsuarioRecibo, fechaInsert, PagoEfectivo.TIPO_PAGO_PRESTAMO, con);
        pe.insert();
        json.put("prestatario", getPrestatario(idUsuarioDa));
        json.put("montoUsado", pago);
        return json;
    }

    public JSONObject prestar(int relacionador, double monto) throws SQLException, JSONException, ParseException {
        int idCasa = new Usuario(con).getIdCasa();
        Date hoy = new Date();
        Billetera b = new Billetera(0, relacionador, monto, idCasa, Billetera.TIPO_TRANSACCION_PRESTAMO, 0, 0, hoy, con);
        b.insert();
        setDatos(0, relacionador, monto, 0.0, b.getId(), hoy);
        insert();
        JSONObject json = getPrestatario(relacionador);
        return json;
    }

    public JSONArray getDetallePrestamo(int idUsuario) throws SQLException, JSONException, ParseException {
        String consulta = "SELECT\n"
                + "	\"Prestamo\".\"idUsuario\",\n"
                + "	--\"Usuario\".\"nombres\" || ' ' || \"Usuario\".\"apellidos\" AS nombre,\n"
                + "	\"Prestamo\".\"fecha\",\n"
                + "	\"Prestamo\".\"debe\",\n"
                + "	\"Prestamo\".\"haber\",\n"
                + "    \"Prestamo\".\"idBilletera\",\n"
                + "    Custodio.\"nombres\" || ' ' || Custodio.\"apellidos\" AS custodio\n"
                + "FROM public.\"Prestamo\"\n"
                + "	-- INNER JOIN public.\"Usuario\" ON \"Usuario\".\"id\" = \"Prestamo\".\"idUsuario\"\n"
                + "	 LEFT JOIN public.\"PagoEfectivo\" ON \"PagoEfectivo\".\"idPrestamo\" = \"Prestamo\".\"id\"\n"
                + "     LEFT JOIN public.\"Usuario\" AS Custodio ON Custodio.\"id\" = \"PagoEfectivo\".\"idUsuarioRecibe\"\n"
                + "WHERE \"Prestamo\".\"idUsuario\" = ?\n"
                + "ORDER BY \"Prestamo\".\"fecha\"\n";
        PreparedStatement ps = con.statametObject(consulta, idUsuario);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        double debeM, haberM;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("fecha", f.format(rs.getDate("fecha")));
            debeM = rs.getDouble("debe");
            haberM = rs.getDouble("haber");
            if (debeM > 0) {
                // Prestamo
                obj.put("monto", debeM);
                obj.put("tipo", "Recibio Prestamo");
            } else {
                // Pago de Prestamo
                obj.put("monto", haberM);
                obj.put("tipo", "Pago Prestamo");
                if (rs.getInt("idBilletera") > 0) {
                    obj.put("custodio", "Paga con Credito");
                } else {
                    obj.put("custodio", rs.getString("custodio"));
                }
            }
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }
}
