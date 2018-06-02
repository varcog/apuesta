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

public class TipoEventoPartido {

    private int id;
    private int idTipoEvento;
    private int idPartido;
    private int idJugador;
    private Date fecha;
    private Conexion con = null;

    public TipoEventoPartido(Conexion con) {
        this.con = con;
    }

    public TipoEventoPartido(int id, int idTipoEvento, int idPartido, int idJugador, Date fecha, Conexion con) {
        this.id = id;
        this.idTipoEvento = idTipoEvento;
        this.idPartido = idPartido;
        this.idJugador = idJugador;
        this.fecha = fecha;
        this.con = con;
    }

    public TipoEventoPartido(int id, int idTipoEvento, int idPartido, int idJugador, Date fecha) {
        this.id = id;
        this.idTipoEvento = idTipoEvento;
        this.idPartido = idPartido;
        this.idJugador = idJugador;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTipoEvento() {
        return idTipoEvento;
    }

    public void setIdTipoEvento(int idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
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
    public void setDatos(int id, int idTipoEvento, int idPartido, int idJugador, Date fecha) {
        this.id = id;
        this.idTipoEvento = idTipoEvento;
        this.idPartido = idPartido;
        this.idJugador = idJugador;
        this.fecha = fecha;
    }

    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"TipoEventoPartido\"(\n"
                + "    \"idTipoEvento\", \"idPartido\", \"idJugador\", \"fecha\")\n"
                + "    VALUES (?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", idTipoEvento > 0 ? idTipoEvento : null, idPartido > 0 ? idPartido : null, idJugador > 0 ? idJugador : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()));
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"TipoEventoPartido\"\n"
                + "    SET \"idTipoEvento\" = ?, \"idPartido\" = ?, \"idJugador\" = ?, \"fecha\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, idTipoEvento > 0 ? idTipoEvento : null, idPartido > 0 ? idPartido : null, idJugador > 0 ? idJugador : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"TipoEventoPartido\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"TipoEventoPartido\".\"id\",\n"
                + "    \"TipoEventoPartido\".\"idTipoEvento\",\n"
                + "    \"TipoEventoPartido\".\"idPartido\",\n"
                + "    \"TipoEventoPartido\".\"idJugador\",\n"
                + "    to_char(\"TipoEventoPartido\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "    FROM public.\"TipoEventoPartido\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idTipoEvento", rs.getInt("idTipoEvento"));
            obj.put("idPartido", rs.getInt("idPartido"));
            obj.put("idJugador", rs.getInt("idJugador"));
            obj.put("fecha", rs.getString("fecha"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"TipoEventoPartido\".\"id\",\n"
                + "    \"TipoEventoPartido\".\"idTipoEvento\",\n"
                + "    \"TipoEventoPartido\".\"idPartido\",\n"
                + "    \"TipoEventoPartido\".\"idJugador\",\n"
                + "    to_char(\"TipoEventoPartido\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "    FROM public.\"TipoEventoPartido\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("idTipoEvento", rs.getInt("idTipoEvento"));
            obj.put("idPartido", rs.getInt("idPartido"));
            obj.put("idJugador", rs.getInt("idJugador"));
            obj.put("fecha", rs.getString("fecha"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public TipoEventoPartido buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"TipoEventoPartido\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        TipoEventoPartido obj = null;
        if (rs.next()) {
            obj = new TipoEventoPartido(con);
            obj.setId(rs.getInt("id"));
            obj.setIdTipoEvento(rs.getInt("idTipoEvento"));
            obj.setIdPartido(rs.getInt("idPartido"));
            obj.setIdJugador(rs.getInt("idJugador"));
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
        obj.put("idTipoEvento", idTipoEvento);
        obj.put("idPartido", idPartido);
        obj.put("idJugador", idJugador);
        obj.put("fecha", fecha == null ? "" : f1.format(fecha));
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
}
