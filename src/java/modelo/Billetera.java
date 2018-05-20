package modelo;

import conexion.Conexion;
import java.math.BigDecimal;
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
    private Double monto;
    private int idUsuarioDa;
    private int tipoTransaccion;
    private int idApuesta;
    private Date fecha;
    private Conexion con = null;

    public Billetera(Conexion con) {
        this.con = con;
    }

    public Billetera(int id, int idUsuarioRecibe, Double monto, int idUsuarioDa, int tipoTransaccion, int idApuesta, Date fecha, Conexion con) {
        this.id = id;
        this.idUsuarioRecibe = idUsuarioRecibe;
        this.monto = monto;
        this.idUsuarioDa = idUsuarioDa;
        this.tipoTransaccion = tipoTransaccion;
        this.idApuesta = idApuesta;
        this.fecha = fecha;
        this.con = con;
    }

    public Billetera(int id, int idUsuarioRecibe, Double monto, int idUsuarioDa, int tipoTransaccion, int idApuesta, Date fecha) {
        this.id = id;
        this.idUsuarioRecibe = idUsuarioRecibe;
        this.monto = monto;
        this.idUsuarioDa = idUsuarioDa;
        this.tipoTransaccion = tipoTransaccion;
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

    public int getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(int tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
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
                + "    \"idUsuarioRecibe\", \"monto\", \"idUsuarioDa\", \"tipoTransaccion\", \"idApuesta\", \"fecha\")\n"
                + "    VALUES (?, ?, ?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", idUsuarioRecibe > 0 ? idUsuarioRecibe : null, monto, idUsuarioDa > 0 ? idUsuarioDa : null, tipoTransaccion, idApuesta > 0 ? idApuesta : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()));
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Billetera\"\n"
                + "    SET \"idUsuarioRecibe\" = ?, \"monto\" = ?, \"idUsuarioDa\" = ?, \"tipoTransaccion\" = ?, \"idApuesta\" = ?, \"fecha\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, idUsuarioRecibe > 0 ? idUsuarioRecibe : null, monto, idUsuarioDa > 0 ? idUsuarioDa : null, tipoTransaccion, idApuesta > 0 ? idApuesta : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Billetera\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Billetera\".\"id\",\n"
                + "    \"Billetera\".\"idUsuarioRecibe\",\n"
                + "    \"Billetera\".\"monto\",\n"
                + "    \"Billetera\".\"idUsuarioDa\",\n"
                + "    \"Billetera\".\"tipoTransaccion\",\n"
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
            obj.put("monto", rs.getDouble("monto"));
            obj.put("idUsuarioDa", rs.getInt("idUsuarioDa"));
            obj.put("tipoTransaccion", rs.getInt("tipoTransaccion"));
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
                + "    \"Billetera\".\"monto\",\n"
                + "    \"Billetera\".\"idUsuarioDa\",\n"
                + "    \"Billetera\".\"tipoTransaccion\",\n"
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
            obj.put("monto", rs.getDouble("monto"));
            obj.put("idUsuarioDa", rs.getInt("idUsuarioDa"));
            obj.put("tipoTransaccion", rs.getInt("tipoTransaccion"));
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
            obj.setMonto(rs.getDouble("monto"));
            obj.setIdUsuarioDa(rs.getInt("idUsuarioDa"));
            obj.setTipoTransaccion(rs.getInt("tipoTransaccion"));
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
        obj.put("monto", monto);
        obj.put("idUsuarioDa", idUsuarioDa);
        obj.put("tipoTransaccion", tipoTransaccion);
        obj.put("idApuesta", idApuesta);
        obj.put("fecha", fecha == null ? "" : f1.format(fecha));
        return obj;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Negocio
    public static final int TIPO_TRANSACCION_PRESTAMO = 1;
    public static final int TIPO_TRANSACCION_TRASPASO = 2;
    public static final int TIPO_TRANSACCION_APUESTA = 3;
    public static final int TIPO_TRANSACCION_GANANCIA = 4;
    public static final int TIPO_TRANSACCION_PAGO_PRESTAMO = 5;
    public static final int TIPO_TRANSACCION_RETIRO = 6;
    public static final int TIPO_TRANSACCION_COMPRA = 7;
    // ESPECIALES PARA LA VISTA
    public static final int TIPO_TRANSACCION_TRASPASO_DA = -1;
    public static final int TIPO_TRANSACCION_TRASPASO_RECIBE = -2;

    public double getCreditoDisponible(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT (\n"
                + "	SELECT SUM(\"Billetera\".\"monto\")\n"
                + "    FROM public.\"Billetera\"\n"
                + "    WHERE \"idUsuarioRecibe\" = ?\n"
                + ") - (\n"
                + "	SELECT SUM(\"Billetera\".\"monto\")\n"
                + "    FROM public.\"Billetera\"\n"
                + "    WHERE \"idUsuarioDa\" = ?\n"
                + "\n"
                + ") AS \"monto\";";
        PreparedStatement ps = con.statametObject(consulta, idUsuario, idUsuario);
        ResultSet rs = ps.executeQuery();
        double credito = rs.next() ? rs.getDouble("monto") : 0;
        rs.close();
        ps.close();
        return credito;
    }

    public JSONObject getBalanceUsuario(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT (\n"
                + "            SELECT SUM(\"Billetera\".\"monto\") AS \"monto\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "            WHERE \"idUsuarioRecibe\" = " + idUsuario + "\n"
                + "    		  AND \"tipoTransaccion\" = 1\n"
                + "       ) AS \"prestamo\",\n"
                + "	   (\n"
                + "            SELECT SUM(\"Billetera\".\"monto\") AS \"monto\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "            WHERE \"idUsuarioRecibe\" = " + idUsuario + "\n"
                + "    		  AND \"tipoTransaccion\" = 2\n"
                + "       ) AS \"recibe\",\n"
                + "       (\n"
                + "            SELECT SUM(\"Billetera\".\"monto\") AS \"monto\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "            WHERE \"idUsuarioDa\" = " + idUsuario + "\n"
                + "              AND \"tipoTransaccion\" = 2\n"
                + "       ) AS \"da\",\n"
                + "       (\n"
                + "            SELECT SUM(\"Billetera\".\"monto\") AS \"monto\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "            WHERE \"idUsuarioDa\" = " + idUsuario + "\n"
                + "              AND \"tipoTransaccion\" = 3\n"
                + "       ) AS \"apuesta\",\n"
                + "       (\n"
                + "            SELECT SUM(\"Billetera\".\"monto\") AS \"monto\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "            WHERE \"idUsuarioRecibe\" = " + idUsuario + "\n"
                + "    		  AND \"tipoTransaccion\" = 4\n"
                + "       ) AS \"ganancia\",\n"
                + "       (\n"
                + "            SELECT SUM(\"Billetera\".\"monto\") AS \"monto\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "            WHERE \"idUsuarioDa\" = " + idUsuario + "\n"
                + "              AND \"tipoTransaccion\" = 5\n"
                + "       ) AS \"pagoPrestamo\",\n"
                + "       (\n"
                + "            SELECT SUM(\"Billetera\".\"monto\") AS \"monto\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "            WHERE \"idUsuarioDa\" = " + idUsuario + "\n"
                + "              AND \"tipoTransaccion\" = 6\n"
                + "       ) AS \"retiro\",\n"
                + "       (\n"
                + "            SELECT SUM(\"Billetera\".\"monto\") AS \"monto\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "            WHERE \"idUsuarioRecibe\" = " + idUsuario + "\n"
                + "    		  AND \"tipoTransaccion\" = 7\n"
                + "       ) AS \"compra\"";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject json = new JSONObject();
        if (rs.next()) {
            json.put("prestamo", rs.getDouble("prestamo"));
            json.put("recibe", rs.getDouble("recibe"));
            json.put("da", rs.getDouble("da"));
            json.put("apuesta", rs.getDouble("apuesta"));
            json.put("ganancia", rs.getDouble("ganancia"));
            json.put("pagoPrestamo", rs.getDouble("pagoPrestamo"));
            json.put("retiro", rs.getDouble("retiro"));
            json.put("compra", rs.getDouble("compra"));
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray detalleBalanceTipo(int tipo) throws SQLException {
        String consulta = "";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray getUsuariosCredito() throws SQLException, JSONException {
        String consulta = "SELECT \"Usuario\".\"id\",\n"
                + "	   \"Usuario\".\"nombres\" || ' ' || \"Usuario\".\"apellidos\"  AS nombre,\n"
                + "	   \"Credito\".\"monto\"\n"
                + "FROM public.\"Usuario\"\n"
                + "	INNER JOIN (\n"
                + "        SELECT SUM(\"tabla\".\"monto\") AS monto,\n"
                + "               \"tabla\".\"idUsuario\"\n"
                + "        FROM (\n"
                + "            SELECT \"Billetera\".\"monto\",\n"
                + "                   \"Billetera\".\"idUsuarioRecibe\" as \"idUsuario\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "            UNION\n"
                + "            SELECT \"Billetera\".\"monto\" * -1 as \"monto\",\n"
                + "                   \"Billetera\".\"idUsuarioDa\" as \"idUsuario\"\n"
                + "            FROM public.\"Billetera\"\n"
                + "        ) as tabla\n"
                + "            GROUP BY \"tabla\".\"idUsuario\"\n"
                + "    ) AS \"Credito\" ON \"Usuario\".\"id\" = \"Credito\".\"idUsuario\"\n"
                + "ORDER BY \"Usuario\".\"nombres\",\n"
                + "		 \"Usuario\".\"apellidos\"";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nombre", rs.getString("nombre"));
            obj.put("monto", rs.getDouble("monto"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray getTransaccionesUsuarios(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "     \"Billetera\".\"id\",\n"
                + "     \"Billetera\".\"idUsuarioRecibe\",\n"
                + "     \"Billetera\".\"monto\",\n"
                + "     \"Billetera\".\"idUsuarioDa\",\n"
                + "     \"Billetera\".\"TipoTransaccion\",\n"
                + "     \"Billetera\".\"idApuesta\",\n"
                + "     to_char(\"Billetera\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha,\n"
                + "     \"recibe\".\"nombres\" || ' ' || \"recibe\".\"apellidos\"  AS recibe,\n"
                + "	\"da\".\"nombres\" || ' ' || \"da\".\"apellidos\"  AS da\n"
                + "FROM public.\"Billetera\"\n"
                + "	INNER JOIN public.\"Usuario\" recibe ON \"recibe\".\"id\" = \"Billetera\".\"idUsuarioRecibe\"\n"
                + "	INNER JOIN public.\"Usuario\" da ON \"da\".\"id\" = \"Billetera\".\"idUsuarioDa\"\n"
                + "WHERE \"Billetera\".\"idUsuarioRecibe\" = ?\n"
                + "   OR \"Billetera\".\"idUsuarioDa\" = ?\n"
                + "ORDER BY \"Billetera\".\"fecha\"\n";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("da", rs.getString("da"));
            obj.put("recibe", rs.getString("recibe"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("monto", rs.getDouble("monto"));
            obj.put("tipoTransaccion", rs.getInt("tipoTransaccion"));
            switch (rs.getInt("tipoTransaccion")) {
                case TIPO_TRANSACCION_APUESTA:
                    obj.put("transaccion", "Apuesta");
                    break;
                case TIPO_TRANSACCION_GANANCIA:
                    obj.put("transaccion", "Ganancia");
                    break;
                case TIPO_TRANSACCION_PAGO_PRESTAMO:
                    obj.put("transaccion", "Pago a Prestamo");
                    break;
                case TIPO_TRANSACCION_PRESTAMO:
                    obj.put("transaccion", "Prestamo");
                    break;
                case TIPO_TRANSACCION_RETIRO:
                    obj.put("transaccion", "Retiro");
                    break;
                case TIPO_TRANSACCION_TRASPASO:
                    obj.put("transaccion", "Traspaso");
                    break;
                default:
                    obj.put("transaccion", "");
            }
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

}
