package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApuestaAmigo {

    private int id;
    private int idUsuarioRetador;
    private int idUsuarioRetado;
    private Double monto;
    private int idPartido;
    private int idEquipoRetador;
    private int idEquipoRetado;
    private Conexion con = null;

    public ApuestaAmigo(Conexion con) {
        this.con = con;
    }

    public ApuestaAmigo(int id, int idUsuarioRetador, int idUsuarioRetado, Double monto, int idPartido, int idEquipoRetador, int idEquipoRetado, Conexion con) {
        this.id = id;
        this.idUsuarioRetador = idUsuarioRetador;
        this.idUsuarioRetado = idUsuarioRetado;
        this.monto = monto;
        this.idPartido = idPartido;
        this.idEquipoRetador = idEquipoRetador;
        this.idEquipoRetado = idEquipoRetado;
        this.con = con;
    }

    public ApuestaAmigo(int id, int idUsuarioRetador, int idUsuarioRetado, Double monto, int idPartido, int idEquipoRetador, int idEquipoRetado) {
        this.id = id;
        this.idUsuarioRetador = idUsuarioRetador;
        this.idUsuarioRetado = idUsuarioRetado;
        this.monto = monto;
        this.idPartido = idPartido;
        this.idEquipoRetador = idEquipoRetador;
        this.idEquipoRetado = idEquipoRetado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuarioRetador() {
        return idUsuarioRetador;
    }

    public void setIdUsuarioRetador(int idUsuarioRetador) {
        this.idUsuarioRetador = idUsuarioRetador;
    }

    public int getIdUsuarioRetado() {
        return idUsuarioRetado;
    }

    public void setIdUsuarioRetado(int idUsuarioRetado) {
        this.idUsuarioRetado = idUsuarioRetado;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public int getIdEquipoRetador() {
        return idEquipoRetador;
    }

    public void setIdEquipoRetador(int idEquipoRetador) {
        this.idEquipoRetador = idEquipoRetador;
    }

    public int getIdEquipoRetado() {
        return idEquipoRetado;
    }

    public void setIdEquipoRetado(int idEquipoRetado) {
        this.idEquipoRetado = idEquipoRetado;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public void setDatos(int id, int idUsuarioRetador, int idUsuarioRetado, Double monto, int idPartido, int idEquipoRetador, int idEquipoRetado) {
        this.id = id;
        this.idUsuarioRetador = idUsuarioRetador;
        this.idUsuarioRetado = idUsuarioRetado;
        this.monto = monto;
        this.idPartido = idPartido;
        this.idEquipoRetador = idEquipoRetador;
        this.idEquipoRetado = idEquipoRetado;
    }

    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"ApuestaAmigo\"(\n"
                + "    \"idUsuarioRetador\", \"idUsuarioRetado\", \"monto\", \"idPartido\", \"idEquipoRetador\", \"idEquipoRetado\")\n"
                + "    VALUES (?, ?, ?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", idUsuarioRetador > 0 ? idUsuarioRetador : null, idUsuarioRetado > 0 ? idUsuarioRetado : null, monto, idPartido > 0 ? idPartido : null, idEquipoRetador > 0 ? idEquipoRetador : null, idEquipoRetado > 0 ? idEquipoRetado : null);
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"ApuestaAmigo\"\n"
                + "    SET \"idUsuarioRetador\" = ?, \"idUsuarioRetado\" = ?, \"monto\" = ?, \"idPartido\" = ?, \"idEquipoRetador\" = ?, \"idEquipoRetado\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, idUsuarioRetador > 0 ? idUsuarioRetador : null, idUsuarioRetado > 0 ? idUsuarioRetado : null, monto, idPartido > 0 ? idPartido : null, idEquipoRetador > 0 ? idEquipoRetador : null, idEquipoRetado > 0 ? idEquipoRetado : null, id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"ApuestaAmigo\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"ApuestaAmigo\".\"id\",\n"
                + "    \"ApuestaAmigo\".\"idUsuarioRetador\",\n"
                + "    \"ApuestaAmigo\".\"idUsuarioRetado\",\n"
                + "    \"ApuestaAmigo\".\"monto\",\n"
                + "    \"ApuestaAmigo\".\"idPartido\",\n"
                + "    \"ApuestaAmigo\".\"idEquipoRetador\",\n"
                + "    \"ApuestaAmigo\".\"idEquipoRetado\"\n"
                + "    FROM public.\"ApuestaAmigo\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioRetador", rs.getInt("idUsuarioRetador"));
            obj.put("idUsuarioRetado", rs.getInt("idUsuarioRetado"));
            obj.put("monto", rs.getDouble("monto"));
            obj.put("idPartido", rs.getInt("idPartido"));
            obj.put("idEquipoRetador", rs.getInt("idEquipoRetador"));
            obj.put("idEquipoRetado", rs.getInt("idEquipoRetado"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONArray todos(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "\"public\".\"ApuestaAmigo\".\"id\",\n"
                + "\"public\".\"ApuestaAmigo\".monto,\n"
                + "us1.nombres ||' '||us1.apellidos AS retador,\n"
                + "us.nombres || ' ' || us.apellidos AS retado,\n"
                + "eq.nombre as quipoRetador,\n"
                + "eq.icono as iconoRetador,\n"
                + "eq1.nombre as quipoRetado,\n"
                + "eq1.icono as iconoRetado,\n"
                + "to_char(ptd.fecha,'DD/MM/YYYY HH24:MI:SS') as fechaPartido\n"
                + "FROM\n"
                + "\"public\".\"ApuestaAmigo\"\n"
                + "INNER JOIN \"public\".\"Usuario\"  us ON \"public\".\"ApuestaAmigo\".\"idUsuarioRetado\" = us.\"id\"\n"
                + "INNER JOIN \"public\".\"Usuario\"  us1 ON \"public\".\"ApuestaAmigo\".\"idUsuarioRetador\" = us1.\"id\"\n"
                + "INNER JOIN \"public\".\"Equipos\"  eq ON \"public\".\"ApuestaAmigo\".\"idEquipoRetador\" = eq.\"id\"\n"
                + "LEFT JOIN \"public\".\"Equipos\"  eq1 ON \"public\".\"ApuestaAmigo\".\"idEquipoRetado\" = eq1.\"id\"\n"
                + "INNER JOIN \"public\".\"Partidos\"  ptd ON \"public\".\"ApuestaAmigo\".\"idPartido\" = ptd.\"id\"\n"
                + "\n"
                + "WHERE\n"
                + "\"public\".\"ApuestaAmigo\".\"idUsuarioRetador\" = ? OR\n"
                + "\"public\".\"ApuestaAmigo\".\"idUsuarioRetado\" = ?;";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, idUsuario);
        ps.setInt(2, idUsuario);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("monto", rs.getDouble("monto"));
            obj.put("retador", rs.getString("retador"));
            obj.put("equipoRetador", rs.getString("equipoRetador"));
            obj.put("iconoRetador", rs.getString("iconoRetador"));
            obj.put("retado", rs.getString("retado"));
            obj.put("equipoRetado", rs.getString("equipoRetado"));
            obj.put("iconoRetado", rs.getString("iconoRetado"));
            obj.put("fechaPartido", rs.getString("fechaPartido"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"ApuestaAmigo\".\"id\",\n"
                + "    \"ApuestaAmigo\".\"idUsuarioRetador\",\n"
                + "    \"ApuestaAmigo\".\"idUsuarioRetado\",\n"
                + "    \"ApuestaAmigo\".\"monto\",\n"
                + "    \"ApuestaAmigo\".\"idPartido\",\n"
                + "    \"ApuestaAmigo\".\"idEquipoRetador\",\n"
                + "    \"ApuestaAmigo\".\"idEquipoRetado\"\n"
                + "    FROM public.\"ApuestaAmigo\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("idUsuarioRetador", rs.getInt("idUsuarioRetador"));
            obj.put("idUsuarioRetado", rs.getInt("idUsuarioRetado"));
            obj.put("monto", rs.getDouble("monto"));
            obj.put("idPartido", rs.getInt("idPartido"));
            obj.put("idEquipoRetador", rs.getInt("idEquipoRetador"));
            obj.put("idEquipoRetado", rs.getInt("idEquipoRetado"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public ApuestaAmigo buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"ApuestaAmigo\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        ApuestaAmigo obj = null;
        if (rs.next()) {
            obj = new ApuestaAmigo(con);
            obj.setId(rs.getInt("id"));
            obj.setIdUsuarioRetador(rs.getInt("idUsuarioRetador"));
            obj.setIdUsuarioRetado(rs.getInt("idUsuarioRetado"));
            obj.setMonto(rs.getDouble("monto"));
            obj.setIdPartido(rs.getInt("idPartido"));
            obj.setIdEquipoRetador(rs.getInt("idEquipoRetador"));
            obj.setIdEquipoRetado(rs.getInt("idEquipoRetado"));
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
        obj.put("idUsuarioRetador", idUsuarioRetador);
        obj.put("idUsuarioRetado", idUsuarioRetado);
        obj.put("monto", monto);
        obj.put("idPartido", idPartido);
        obj.put("idEquipoRetador", idEquipoRetador);
        obj.put("idEquipoRetado", idEquipoRetado);
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
    public JSONArray getHistorial(int idUsuario) throws SQLException, JSONException {
        String consulta = "SELECT \"Billetera\".\"monto\",\n"
                + "	   \"ApuestaAmigo\".\"idUsuarioRetador\",\n"
                + "       UsuarioRetador.\"nombres\" || ' ' || UsuarioRetador.\"apellidos\" AS usuarioRetador,\n"
                + "       UsuarioRetado.\"nombres\" || ' ' || UsuarioRetado.\"apellidos\" AS usuarioRetado,\n"
                + "       \"ApuestaAmigo\".\"idUsuarioRetado\",\n"
                + "	   \"ApuestaAmigo\".\"idEquipoRetador\",\n"
                + "       \"ApuestaAmigo\".\"idEquipoRetado\",\n"
                + "       Equipo1.\"id\" as idEquipo1,\n"
                + "       Equipo1.\"nombre\" as equipo1,\n"
                + "       Equipo2.\"id\" as idEquipo2,\n"
                + "       Equipo2.\"nombre\" as equipo2,\n"
                + "       to_char(\"Billetera\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
                + "FROM public.\"Billetera\"\n"
                + "     INNER JOIN public.\"ApuestaAmigo\" ON \"ApuestaAmigo\".\"id\" = \"Billetera\".\"idApuestaAmigo\"\n"
                + "     INNER JOIN public.\"Partidos\" ON \"Partidos\".\"id\" = \"ApuestaAmigo\".\"idPartido\"\n"
                + "     INNER JOIN public.\"Equipos\" AS Equipo1 ON Equipo1.\"id\" = \"Partidos\".\"idEquipo1\"\n"
                + "     INNER JOIN public.\"Equipos\" AS Equipo2 ON Equipo2.\"id\" = \"Partidos\".\"idEquipo2\"\n"
                + "     INNER JOIN public.\"Usuario\" AS UsuarioRetador ON UsuarioRetador.\"id\" = \"ApuestaAmigo\".\"idUsuarioRetador\"\n"
                + "     INNER JOIN public.\"Usuario\" AS UsuarioRetado ON UsuarioRetado.\"id\" = \"ApuestaAmigo\".\"idUsuarioRetado\"\n"
                + "WHERE \"Billetera\".\"tipoTransaccion\" = 3\n"
                + "	  AND \"Billetera\".\"idUsuarioDa\" = 3\n"
                + "      --AND \"ApuestaAmigo\".\"idEquipoRetado\" IS NOT NULL\n"
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
                    break;
                case 2:
                    obj.put("titulo", "Empate");
                    break;
                case 3:
                    obj.put("titulo", rs.getString("equipo2"));
                    break;
                default:

            }
            obj.put("tipo", rs.getInt("tipo"));
            obj.put("vs", rs.getString("equipo1") + " vs " + rs.getString("equipo2"));
            obj.put("porcentaje", rs.getDouble("porcentaje"));
            obj.put("monto", rs.getDouble("monto"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }
}
