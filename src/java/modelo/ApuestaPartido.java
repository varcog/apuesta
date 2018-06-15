package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApuestaPartido {

    private int id;
    private int idTipoApuesta;
    private int idPartido;
    private Double multiplicador;
    private Conexion con = null;

    public ApuestaPartido(Conexion con) {
        this.con = con;
    }

    public ApuestaPartido(int id, int idTipoApuesta, int idPartido, Double multiplicador, Conexion con) {
        this.id = id;
        this.idTipoApuesta = idTipoApuesta;
        this.idPartido = idPartido;
        this.multiplicador = multiplicador;
        this.con = con;
    }

    public ApuestaPartido(int id, int idTipoApuesta, int idPartido, Double multiplicador) {
        this.id = id;
        this.idTipoApuesta = idTipoApuesta;
        this.idPartido = idPartido;
        this.multiplicador = multiplicador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTipoApuesta() {
        return idTipoApuesta;
    }

    public void setIdTipoApuesta(int idTipoApuesta) {
        this.idTipoApuesta = idTipoApuesta;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public double getMultiplicador() {
        return multiplicador;
    }

    public void setMultiplicador(Double multiplicador) {
        this.multiplicador = multiplicador;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"ApuestaPartido\"(\n"
                + "    \"idTipoApuesta\", \"idPartido\", \"multiplicador\")\n"
                + "    VALUES (?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", idTipoApuesta > 0 ? idTipoApuesta : null, idPartido > 0 ? idPartido : null, multiplicador);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"ApuestaPartido\"\n"
                + "    SET \"idTipoApuesta\" = ?, \"idPartido\" = ?, \"multiplicador\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, idTipoApuesta > 0 ? idTipoApuesta : null, idPartido > 0 ? idPartido : null, multiplicador, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"ApuestaPartido\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"ApuestaPartido\".\"id\",\n"
                + "    \"ApuestaPartido\".\"idTipoApuesta\",\n"
                + "    \"ApuestaPartido\".\"idPartido\",\n"
                + "    \"ApuestaPartido\".\"multiplicador\"\n"
                + "    FROM public.\"ApuestaPartido\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idTipoApuesta", rs.getInt("idTipoApuesta"));
            obj.put("idPartido", rs.getInt("idPartido"));
            obj.put("multiplicador", rs.getDouble("multiplicador"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"ApuestaPartido\".\"id\",\n"
                + "    \"ApuestaPartido\".\"idTipoApuesta\",\n"
                + "    \"ApuestaPartido\".\"idPartido\",\n"
                + "    \"ApuestaPartido\".\"multiplicador\"\n"
                + "    FROM public.\"ApuestaPartido\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("idTipoApuesta", rs.getInt("idTipoApuesta"));
            obj.put("idPartido", rs.getInt("idPartido"));
            obj.put("multiplicador", rs.getDouble("multiplicador"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public ApuestaPartido buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"ApuestaPartido\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        ApuestaPartido obj = null;
        if (rs.next()) {
            obj = new ApuestaPartido(con);
            obj.setId(rs.getInt("id"));
            obj.setIdTipoApuesta(rs.getInt("idTipoApuesta"));
            obj.setIdPartido(rs.getInt("idPartido"));
            obj.setMultiplicador(rs.getDouble("multiplicador"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public ApuestaPartido buscar(int idTipoApuesta, int idPartido) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"ApuestaPartido\"\n"
                + "    WHERE \"idTipoApuesta\" = ? AND \"idPartido\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, idTipoApuesta, idPartido);
        ResultSet rs = ps.executeQuery();
        ApuestaPartido obj = null;
        if (rs.next()) {
            obj = new ApuestaPartido(con);
            obj.setId(rs.getInt("id"));
            obj.setIdTipoApuesta(rs.getInt("idTipoApuesta"));
            obj.setIdPartido(rs.getInt("idPartido"));
            obj.setMultiplicador(rs.getDouble("multiplicador"));
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
        obj.put("idTipoApuesta", idTipoApuesta);
        obj.put("idPartido", idPartido);
        obj.put("multiplicador", multiplicador);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
    public boolean buscarSet(int idTipoApuesta, int idPartido) throws SQLException, JSONException {
        String consulta = "SELECT \"ApuestaPartido\".*\n"
                + "FROM \"public\".\"ApuestaPartido\"\n"
                + "where \"ApuestaPartido\".\"id\" = (\n"
                + " SELECT max(id) as id\n"
                + " FROM \"public\".\"ApuestaPartido\"\n"
                + " WHERE \"ApuestaPartido\".\"idTipoApuesta\" = ?\n"
                + " AND \"ApuestaPartido\".\"idPartido\" = ?)";
        PreparedStatement ps = con.statametObject(consulta, idTipoApuesta, idPartido);
        ResultSet rs = ps.executeQuery();
        boolean res = false;
        if (rs.next()) {
            setId(rs.getInt("id"));
            setIdTipoApuesta(rs.getInt("idTipoApuesta"));
            setIdPartido(rs.getInt("idPartido"));
            setMultiplicador(rs.getDouble("multiplicador"));
            res = true;
        }
        rs.close();
        ps.close();
        return res;
    }

    public JSONArray getHistorial(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT \"TipoApuesta\".\"id\" as idTipoApuesta,\n"
                + "	   \"TipoApuesta\".\"tipo\",\n"
                + "	   \"TipoApuesta\".\"equipo1\" as taequipo1,\n"
                + "	   \"TipoApuesta\".\"equipo2\" as taequipo2,\n"
                + "       \"ApuestaPartido\".\"multiplicador\" as porcentaje,\n"
                + "       \"Billetera\".\"monto\",\n"
                + "       Equipo1.\"id\" as idEquipo1,\n"
                + "       Equipo1.\"nombre\" as equipo1,\n"
                + "       Equipo2.\"id\" as idEquipo2,\n"
                + "       Equipo2.\"nombre\" as equipo2,\n"
                + "       to_char(\"Billetera\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "FROM public.\"Billetera\"\n"
                + "     INNER JOIN public.\"ApuestaPartido\" ON \"ApuestaPartido\".\"id\" = \"Billetera\".\"idApuestaPartido\"\n"
                + "     INNER JOIN public.\"Partidos\" ON \"Partidos\".\"id\" = \"ApuestaPartido\".\"idPartido\"\n"
                + "     INNER JOIN public.\"TipoApuesta\" ON \"TipoApuesta\".\"id\" = \"ApuestaPartido\".\"idTipoApuesta\"\n"
                + "     INNER JOIN public.\"Equipos\" AS Equipo1 ON Equipo1.\"id\" = \"Partidos\".\"idEquipo1\"\n"
                + "     INNER JOIN public.\"Equipos\" AS Equipo2 ON Equipo2.\"id\" = \"Partidos\".\"idEquipo2\"\n"
                + "WHERE \"Billetera\".\"tipoTransaccion\" = ?\n"
                + "	  AND \"Billetera\".\"idUsuarioDa\" = ?\n"
                + "ORDER BY \"Billetera\".\"fecha\"";
        PreparedStatement ps = con.statametObject(consulta, Billetera.TIPO_TRANSACCION_APUESTA, idUsuario);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            switch (rs.getInt("idTipoApuesta")) {
                case 1:
                    obj.put("titulo", rs.getString("equipo1"));
                    obj.put("tipo", "Resuldato Final");
                    break;
                case 2:
                    obj.put("titulo", "Empate");
                    obj.put("tipo", "Resuldato Final");
                    break;
                case 3:
                    obj.put("titulo", rs.getString("equipo2"));
                    obj.put("tipo", "Resuldato Final");
                    break;
                default:
                    obj.put("titulo", rs.getString("equipo1") + " " + rs.getInt("taequipo1") + " - " + rs.getInt("taequipo2") + " " + rs.getString("equipo2"));
                    obj.put("tipo", "Goles en el Partido");
            }
            obj.put("vs", rs.getString("equipo1") + " vs " + rs.getString("equipo2"));
            obj.put("porcentaje", rs.getDouble("porcentaje"));
            obj.put("monto", rs.getDouble("monto"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        consulta = "SELECT \"Billetera\".\"monto\",\n"
                + "	   \"ApuestaAmigo\".\"idUsuarioRetador\",\n"
                + "        UsuarioRetador.\"nombres\" || ' ' || UsuarioRetador.\"apellidos\" AS usuarioRetador,\n"
                + "        UsuarioRetado.\"nombres\" || ' ' || UsuarioRetado.\"apellidos\" AS usuarioRetado,\n"
                + "        \"ApuestaAmigo\".\"idUsuarioRetado\",\n"
                + "	   \"ApuestaAmigo\".\"idEquipoRetador\",\n"
                + "        \"ApuestaAmigo\".\"idEquipoRetado\",\n"
                + "        Equipo1.\"id\" as idEquipo1,\n"
                + "        Equipo1.\"nombre\" as equipo1,\n"
                + "        Equipo2.\"id\" as idEquipo2,\n"
                + "        Equipo2.\"nombre\" as equipo2,\n"
                + "        to_char(\"Billetera\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "FROM public.\"Billetera\"\n"
                + "     INNER JOIN public.\"ApuestaAmigo\" ON \"ApuestaAmigo\".\"id\" = \"Billetera\".\"idApuestaAmigo\"\n"
                + "     INNER JOIN public.\"Partidos\" ON \"Partidos\".\"id\" = \"ApuestaAmigo\".\"idPartido\"\n"
                + "     INNER JOIN public.\"Equipos\" AS Equipo1 ON Equipo1.\"id\" = \"Partidos\".\"idEquipo1\"\n"
                + "     INNER JOIN public.\"Equipos\" AS Equipo2 ON Equipo2.\"id\" = \"Partidos\".\"idEquipo2\"\n"
                + "     INNER JOIN public.\"Usuario\" AS UsuarioRetador ON UsuarioRetador.\"id\" = \"ApuestaAmigo\".\"idUsuarioRetador\"\n"
                + "     INNER JOIN public.\"Usuario\" AS UsuarioRetado ON UsuarioRetado.\"id\" = \"ApuestaAmigo\".\"idUsuarioRetado\"\n"
                + "WHERE \"Billetera\".\"tipoTransaccion\" = ?\n"
                + "	  AND \"Billetera\".\"idUsuarioDa\" = ?\n"
                + "       AND \"ApuestaAmigo\".\"idEquipoRetado\" IS NOT NULL\n"
                + "ORDER BY \"Billetera\".\"fecha\"";
        ps = con.statametObject(consulta, Billetera.TIPO_TRANSACCION_APUESTA, idUsuario);
        rs = ps.executeQuery();
        while (rs.next()) {
            obj = new JSONObject();
            if (idUsuario == rs.getInt("idUsuarioRetador")) {
                obj.put("tipo", "Apuesta con " + rs.getString("usuarioRetado"));
                if (rs.getInt("idEquipoRetador") == rs.getInt("idEquipo1")) {
                    obj.put("titulo", rs.getString("equipo1"));
                } else {
                    obj.put("titulo", rs.getString("equipo2"));
                }
            } else {
                obj.put("tipo", "Apuesta con " + rs.getString("usuarioRetador"));
                if (rs.getInt("idEquipoRetado") == rs.getInt("idEquipo1")) {
                    obj.put("titulo", rs.getString("equipo1"));
                } else {
                    obj.put("titulo", rs.getString("equipo2"));
                }
            }
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("vs", rs.getString("equipo1") + " vs " + rs.getString("equipo2"));
            obj.put("monto", rs.getDouble("monto"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }
}
