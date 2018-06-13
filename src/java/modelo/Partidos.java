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

public class Partidos {

    private int id;
    private Date fecha;
    private int idEquipo1;
    private int idEquipo2;
    private int idUsuario;
    private int idEstadio;
    private int idGrupo;
    private Conexion con = null;

    public Partidos(Conexion con) {
        this.con = con;
    }

    public Partidos(int id, Date fecha, int idEquipo1, int idEquipo2, int idUsuario, int idEstadio, int idGrupo, Conexion con) {
        this.id = id;
        this.fecha = fecha;
        this.idEquipo1 = idEquipo1;
        this.idEquipo2 = idEquipo2;
        this.idUsuario = idUsuario;
        this.idEstadio = idEstadio;
        this.idGrupo = idGrupo;
        this.con = con;
    }

    public Partidos(int id, Date fecha, int idEquipo1, int idEquipo2, int idUsuario, int idEstadio, int idGrupo) {
        this.id = id;
        this.fecha = fecha;
        this.idEquipo1 = idEquipo1;
        this.idEquipo2 = idEquipo2;
        this.idUsuario = idUsuario;
        this.idEstadio = idEstadio;
        this.idGrupo = idGrupo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getFechaS() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return fecha == null ? "" : formato.format(fecha);
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdEquipo1() {
        return idEquipo1;
    }

    public void setIdEquipo1(int idEquipo1) {
        this.idEquipo1 = idEquipo1;
    }

    public int getIdEquipo2() {
        return idEquipo2;
    }

    public void setIdEquipo2(int idEquipo2) {
        this.idEquipo2 = idEquipo2;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(int idEstadio) {
        this.idEstadio = idEstadio;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Partidos\"(\n"
                + "    \"fecha\", \"idEquipo1\", \"idEquipo2\", \"idUsuario\", \"idEstadio\", \"idGrupo\")\n"
                + "    VALUES (?, ?, ?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), idEquipo1 > 0 ? idEquipo1 : null, idEquipo2 > 0 ? idEquipo2 : null, idUsuario > 0 ? idUsuario : null, idEstadio > 0 ? idEstadio : null, idGrupo > 0 ? idGrupo : null);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Partidos\"\n"
                + "    SET \"fecha\" = ?, \"idEquipo1\" = ?, \"idEquipo2\" = ?, \"idUsuario\" = ?, \"idEstadio\" = ?, \"idGrupo\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), idEquipo1 > 0 ? idEquipo1 : null, idEquipo2 > 0 ? idEquipo2 : null, idUsuario > 0 ? idUsuario : null, idEstadio > 0 ? idEstadio : null, idGrupo > 0 ? idGrupo : null, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Partidos\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Partidos\".\"id\",\n"
                + "    to_char(\"Partidos\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha,\n"
                + "    \"Partidos\".\"idEquipo1\",\n"
                + "    \"Partidos\".\"idEquipo2\",\n"
                + "    \"Partidos\".\"idUsuario\",\n"
                + "    \"Partidos\".\"idEstadio\",\n"
                + "    \"Partidos\".\"idGrupo\"\n"
                + "    FROM public.\"Partidos\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("idEquipo1", rs.getInt("idEquipo1"));
            obj.put("idEquipo2", rs.getInt("idEquipo2"));
            obj.put("idUsuario", rs.getInt("idUsuario"));
            obj.put("idEstadio", rs.getInt("idEstadio"));
            obj.put("idGrupo", rs.getInt("idGrupo"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public Partidos buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Partidos\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Partidos obj = null;
        if (rs.next()) {
            obj = new Partidos(con);
            obj.setId(rs.getInt("id"));
            obj.setFecha(rs.getTimestamp("fecha"));
            obj.setIdEquipo1(rs.getInt("idEquipo1"));
            obj.setIdEquipo2(rs.getInt("idEquipo2"));
            obj.setIdUsuario(rs.getInt("idUsuario"));
            obj.setIdEstadio(rs.getInt("idEstadio"));
            obj.setIdGrupo(rs.getInt("idGrupo"));
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
        obj.put("fecha", fecha == null ? "" : f1.format(fecha));
        obj.put("idEquipo1", idEquipo1);
        obj.put("idEquipo2", idEquipo2);
        obj.put("idUsuario", idUsuario);
        obj.put("idEstadio", idEstadio);
        obj.put("idGrupo", idGrupo);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "\"public\".\"Partidos\".\"id\",\n"
                + "to_char(\"public\".\"Partidos\".\"fecha\",'DD/MM/YYYY') AS fecha,\n"
                + "to_char(\"public\".\"Partidos\".\"fecha\",'HH24:MI') AS hora,\n"
                + "\"public\".\"Partidos\".\"idEquipo1\",\n"
                + "\"public\".\"Partidos\".\"idEquipo2\",\n"
                + "\"public\".\"Equipos\".nombre as nombre1,\n"
                + "\"public\".\"Grupo\".nombre as nombreGrupo,\n"
                + "\"public\".\"Equipos\".icono as icono1,\n"
                + "eq.nombre as nombre2,\n"
                + "eq.icono as icono2,\n"
                + "\"public\".\"Estadio\".id AS idEstadio,\n"
                + "\"public\".\"Estadio\".foto as fotoEstadio,\n"
                + "\"public\".\"Estadio\".nombre as nombreEstadio\n"
                + "FROM\n"
                + "\"public\".\"Partidos\"\n"
                + "INNER JOIN \"public\".\"Equipos\" ON \"public\".\"Equipos\".\"id\" = \"public\".\"Partidos\".\"idEquipo1\"\n"
                + "INNER JOIN \"public\".\"Equipos\" eq ON eq.\"id\" = \"public\".\"Partidos\".\"idEquipo2\"\n"
                + "INNER JOIN \"public\".\"Estadio\" ON \"public\".\"Partidos\".\"idEstadio\" = \"public\".\"Estadio\".\"id\"\n"
                + "INNER JOIN \"public\".\"Grupo\" ON \"public\".\"Partidos\".\"idGrupo\" = \"public\".\"Grupo\".\"id\"\n"
                + "WHERE \"public\".\"Partidos\".\"fecha\" >= CURRENT_TIMESTAMP\n"
                + "ORDER BY\n"
                + "\"public\".\"Partidos\".fecha ASC;";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("hora", rs.getString("hora"));
            obj.put("idEquipo1", rs.getInt("idEquipo1"));
            obj.put("idEquipo2", rs.getInt("idEquipo2"));
            obj.put("nombre1", rs.getString("nombre1"));
            obj.put("icono1", rs.getString("icono1"));
            obj.put("nombre2", rs.getString("nombre2"));
            obj.put("nombreGrupo", rs.getString("nombreGrupo"));
            obj.put("icono2", rs.getString("icono2"));
            obj.put("idEstadio", rs.getInt("idEstadio"));
            obj.put("nombreEstadio", rs.getString("nombreEstadio"));
            obj.put("fotoEstadio", rs.getString("fotoEstadio"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarCompleto(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "\"public\".\"Partidos\".\"id\",\n"
                + "to_char(\"public\".\"Partidos\".\"fecha\",'DD/MM/YYYY') AS fecha,\n"
                + "to_char(\"public\".\"Partidos\".\"fecha\",'HH24:MI') AS hora,\n"
                + "\"public\".\"Partidos\".\"idEquipo1\",\n"
                + "\"public\".\"Partidos\".\"idEquipo2\",\n"
                + "\"public\".\"Equipos\".nombre as nombre1,\n"
                + "\"public\".\"Equipos\".icono as icono1,\n"
                + "\"public\".\"Grupo\".nombre as nombreGrupo,\n"
                + "eq.nombre as nombre2,\n"
                + "eq.icono as icono2,\n"
                + "\"public\".\"Estadio\".id as idEstadio,\n"
                + "\"public\".\"Estadio\".foto as fotoEstadio,\n"
                + "\"public\".\"Estadio\".nombre as nombreEstadio\n"
                + "FROM\n"
                + "\"public\".\"Partidos\"\n"
                + "INNER JOIN \"public\".\"Equipos\" ON \"public\".\"Equipos\".\"id\" = \"public\".\"Partidos\".\"idEquipo1\"\n"
                + "INNER JOIN \"public\".\"Equipos\" eq ON eq.\"id\" = \"public\".\"Partidos\".\"idEquipo2\"\n"
                + "INNER JOIN \"public\".\"Estadio\" ON \"public\".\"Partidos\".\"idEstadio\" = \"public\".\"Estadio\".\"id\"\n"
                + "INNER JOIN \"public\".\"Grupo\" ON \"public\".\"Partidos\".\"idGrupo\" = \"public\".\"Grupo\".\"id\"\n"
                + "Where \"public\".\"Partidos\".id = ?\n"
                + "ORDER BY\n"
                + "\"public\".\"Partidos\".fecha ASC;";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        while (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("hora", rs.getString("hora"));
            obj.put("idEquipo1", rs.getInt("idEquipo1"));
            obj.put("idEquipo2", rs.getInt("idEquipo2"));
            obj.put("nombre1", rs.getString("nombre1"));
            obj.put("nombreGrupo", rs.getString("nombreGrupo"));
            obj.put("icono1", rs.getString("icono1"));
            obj.put("nombre2", rs.getString("nombre2"));
            obj.put("icono2", rs.getString("icono2"));
            obj.put("idEstadio", rs.getInt("idEstadio"));
            obj.put("nombreEstadio", rs.getString("nombreEstadio"));
            obj.put("fotoEstadio", rs.getString("fotoEstadio"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public JSONArray fixture() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "\"public\".\"Grupo\".nombre as grupo,\n"
                + "\"public\".\"Grupo\".id as idGrupo,\n"
                + "to_char(\"public\".\"Partidos\".fecha,'DD/MM/YYYY HH24:MI') as fecha,\n"
                + "\"public\".\"Partidos\".id as idPartido,\n"
                + "eq1.nombre as eq1,\n"
                + "eq1.icono as icono1,\n"
                + "eq2.nombre as eq2,\n"
                + "eq2.icono as icono2\n"
                + "FROM\n"
                + "\"public\".\"Grupo\"\n"
                + "INNER JOIN \"public\".\"Partidos\" ON \"public\".\"Partidos\".\"idGrupo\" = \"public\".\"Grupo\".\"id\" AND '' = ''\n"
                + "INNER JOIN \"public\".\"Equipos\" eq1 ON \"public\".\"Partidos\".\"idEquipo1\" = eq1.\"id\"\n"
                + "INNER JOIN \"public\".\"Equipos\" eq2 ON \"public\".\"Partidos\".\"idEquipo2\" = eq2.\"id\"\n"
                + "ORDER BY\n"
                + "\"public\".\"Grupo\".\"id\", \"public\".\"Partidos\".fecha ASC";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("idPartido", rs.getInt("idPartido"));
            obj.put("grupo", rs.getString("grupo"));
            obj.put("idGrupo", rs.getInt("idGrupo"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("eq1", rs.getString("eq1"));
            obj.put("eq2", rs.getString("eq2"));
            obj.put("icono1", rs.getString("icono1"));
            obj.put("icono2", rs.getString("icono2"));
            obj.put("goles1", 0);
            obj.put("goles2", 0);
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray relato(int idPartido) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "to_char(\"public\".\"TipoEventoPartido\".fecha, 'HH24:MI:SS') as hora,\n"
                + "to_char(\"public\".\"TipoEventoPartido\".fecha, 'DD/MM/YYYY HH24:MI:SS') as fecha,\n"
                + "\"public\".\"TipoEventoPartido\".id,\n"
                + "\"public\".\"Jugador\".nombres,\n"
                + "\"public\".\"Jugador\".apellidos,\n"
                + "\"public\".\"Equipos\".nombre as equipo,\n"
                + "\"public\".\"Equipos\".icono,\n"
                + "\"public\".\"Jugador\".foto,\n"
                + "\"public\".\"TipoEvento\".\"id\",\n"
                + "\"public\".\"TipoEvento\".evento\n"
                + "FROM\n"
                + "\"public\".\"TipoEventoPartido\"\n"
                + "INNER JOIN \"public\".\"TipoEvento\" ON \"public\".\"TipoEventoPartido\".\"idTipoEvento\" = \"public\".\"TipoEvento\".\"id\"\n"
                + "INNER JOIN \"public\".\"Jugador\" ON \"public\".\"TipoEventoPartido\".\"idJugador\" = \"public\".\"Jugador\".\"id\"\n"
                + "INNER JOIN \"public\".\"Equipos\" ON \"public\".\"Jugador\".\"idEquipo\" = \"public\".\"Equipos\".\"id\"\n"
                + "WHERE\n"
                + "\"public\".\"TipoEventoPartido\".\"idPartido\" = ? ORDER BY \"public\".\"TipoEventoPartido\".fecha DESC";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, idPartido);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("hora", rs.getString("hora"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("equipo", rs.getString("equipo"));
            obj.put("foto", rs.getString("foto"));
            obj.put("iconoEquipo", rs.getString("icono"));
            obj.put("idEvento", rs.getInt("id"));
            obj.put("evento", rs.getString("evento"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray relato(int idPartido, String ultimaFecha) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "to_char(\"public\".\"TipoEventoPartido\".fecha, 'HH24:MI:SS') as hora,\n"
                + "to_char(\"public\".\"TipoEventoPartido\".fecha, 'DD/MM/YYYY HH24:MI:SS') as fecha,\n"
                + "\"public\".\"TipoEventoPartido\".id,\n"
                + "\"public\".\"Jugador\".nombres,\n"
                + "\"public\".\"Jugador\".apellidos,\n"
                + "\"public\".\"Equipos\".nombre as equipo,\n"
                + "\"public\".\"Equipos\".icono,\n"
                + "\"public\".\"Jugador\".foto,\n"
                + "\"public\".\"TipoEvento\".\"id\",\n"
                + "\"public\".\"TipoEvento\".evento\n"
                + "FROM\n"
                + "\"public\".\"TipoEventoPartido\"\n"
                + "INNER JOIN \"public\".\"TipoEvento\" ON \"public\".\"TipoEventoPartido\".\"idTipoEvento\" = \"public\".\"TipoEvento\".\"id\"\n"
                + "INNER JOIN \"public\".\"Jugador\" ON \"public\".\"TipoEventoPartido\".\"idJugador\" = \"public\".\"Jugador\".\"id\"\n"
                + "INNER JOIN \"public\".\"Equipos\" ON \"public\".\"Jugador\".\"idEquipo\" = \"public\".\"Equipos\".\"id\"\n"
                + "WHERE\n"
                + "\"public\".\"TipoEventoPartido\".\"fecha\" > to_timestamp(?,'DD/MM/YYYY HH24:MI:SS:MS')\n"
                + "AND\n"
                + "\"public\".\"TipoEventoPartido\".\"idPartido\" = ? ORDER BY \"public\".\"TipoEventoPartido\".fecha DESC";
        PreparedStatement ps = con.statamet(consulta);
        ultimaFecha += ":999";
        ps.setString(1, ultimaFecha);
        ps.setInt(2, idPartido);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("hora", rs.getString("hora"));
            obj.put("fecha", rs.getString("fecha"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("equipo", rs.getString("equipo"));
            obj.put("foto", rs.getString("foto"));
            obj.put("iconoEquipo", rs.getString("icono"));
            obj.put("idEvento", rs.getInt("id"));
            obj.put("evento", rs.getString("evento"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarRelato(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "to_char(\"public\".\"TipoEventoPartido\".fecha, 'HH24:MI:SS') as hora,\n"
                + "\"public\".\"TipoEventoPartido\".id,\n"
                + "\"public\".\"Jugador\".nombres,\n"
                + "\"public\".\"Jugador\".apellidos,\n"
                + "\"public\".\"Equipos\".nombre as equipo,\n"
                + "\"public\".\"Equipos\".icono,\n"
                + "\"public\".\"Jugador\".foto,\n"
                + "\"public\".\"TipoEvento\".\"id\",\n"
                + "\"public\".\"TipoEvento\".evento\n"
                + "FROM\n"
                + "\"public\".\"TipoEventoPartido\"\n"
                + "INNER JOIN \"public\".\"TipoEvento\" ON \"public\".\"TipoEventoPartido\".\"idTipoEvento\" = \"public\".\"TipoEvento\".\"id\"\n"
                + "INNER JOIN \"public\".\"Jugador\" ON \"public\".\"TipoEventoPartido\".\"idJugador\" = \"public\".\"Jugador\".\"id\"\n"
                + "INNER JOIN \"public\".\"Equipos\" ON \"public\".\"Jugador\".\"idEquipo\" = \"public\".\"Equipos\".\"id\"\n"
                + "WHERE\n"
                + "\"public\".\"TipoEventoPartido\".\"id\" = ? ORDER BY \"public\".\"TipoEventoPartido\".fecha DESC";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = null;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("hora", rs.getString("hora"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("equipo", rs.getString("equipo"));
            obj.put("foto", rs.getString("foto"));
            obj.put("iconoEquipo", rs.getString("icono"));
            obj.put("idEvento", rs.getInt("id"));
            obj.put("evento", rs.getString("evento"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public boolean sePuedeApostar(int idPartido) throws SQLException, ParseException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Partidos\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, idPartido);
        ResultSet rs = ps.executeQuery();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        boolean resp = false;
        if (rs.next()) {
            Date fecha = rs.getTimestamp("fecha");
            if (fecha != null) {
                fecha = format.parse(format.format(fecha));
                Date hoy = format.parse(format.format(new Date()));
                if (fecha.after(hoy)) {
                    return true;
                }
            }
        }
        rs.close();
        ps.close();
        return resp;
    }
}
