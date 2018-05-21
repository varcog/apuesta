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
}
