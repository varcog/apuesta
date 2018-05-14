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

public class Jugador {

    private int id;
    private String nombres;
    private String apellidos;
    private int idPosicion;
    private int idEquipo;
    private String detalle;
    private String foto;
    private Date fechaNacimiento;
    private Conexion con = null;

    public Jugador(Conexion con) {
        this.con = con;
    }

    public Jugador(int id, String nombres, String apellidos, int idPosicion, int idEquipo, String detalle, String foto, Date fechaNacimiento, Conexion con) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.idPosicion = idPosicion;
        this.idEquipo = idEquipo;
        this.detalle = detalle;
        this.foto = foto;
        this.fechaNacimiento = fechaNacimiento;
        this.con = con;
    }

    public Jugador(int id, String nombres, String apellidos, int idPosicion, int idEquipo, String detalle, String foto, Date fechaNacimiento) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.idPosicion = idPosicion;
        this.idEquipo = idEquipo;
        this.detalle = detalle;
        this.foto = foto;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres == null ? "" : nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos == null ? "" : apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getIdPosicion() {
        return idPosicion;
    }

    public void setIdPosicion(int idPosicion) {
        this.idPosicion = idPosicion;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getDetalle() {
        return detalle == null ? "" : detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getFoto() {
        return foto == null ? "" : foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Conexion getCon() {
        return this.con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        String consulta = "INSERT INTO public.\"Jugador\"(\n"
                + "    \"nombres\", \"apellidos\", \"idPosicion\", \"idEquipo\", \"detalle\", \"foto\", \"fechaNacimiento\")\n"
                + "    VALUES (?, ?, ?, ?, ?, ?, ?)\n";
        this.id = con.ejecutarInsert(consulta, "id", nombres, apellidos, idPosicion > 0 ? idPosicion : null, idEquipo > 0 ? idEquipo : null, detalle, foto, fechaNacimiento == null ? null : new java.sql.Date(fechaNacimiento.getTime()));
        return this.id;
    }

    public void update() throws SQLException {
        String consulta = "UPDATE public.\"Jugador\"\n"
                + "    SET \"nombres\" = ?, \"apellidos\" = ?, \"idPosicion\" = ?, \"idEquipo\" = ?, \"detalle\" = ?, \"foto\" = ?, \"fechaNacimiento\" = ?\n"
                + "    WHERE \"id\"=?";
        con.ejecutarSentencia(consulta, nombres, apellidos, idPosicion > 0 ? idPosicion : null, idEquipo > 0 ? idEquipo : null, detalle, foto, fechaNacimiento == null ? null : new java.sql.Date(fechaNacimiento.getTime()), id);
    }

    public void delete() throws SQLException {
        String consulta = "delete from public.\"Jugador\" where \"id\"= ?;";
        con.ejecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Jugador\".\"id\",\n"
                + "    \"Jugador\".\"nombres\",\n"
                + "    \"Jugador\".\"apellidos\",\n"
                + "    \"Jugador\".\"idPosicion\",\n"
                + "    \"Jugador\".\"idEquipo\",\n"
                + "    \"Jugador\".\"detalle\",\n"
                + "    \"Jugador\".\"foto\",\n"
                + "    to_char(\"Jugador\".\"fechaNacimiento\", 'DD/MM/YYYY') AS fechaNacimiento\n"
                + "    FROM public.\"Jugador\";";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("idPosicion", rs.getInt("idPosicion"));
            obj.put("idEquipo", rs.getInt("idEquipo"));
            obj.put("detalle", rs.getString("detalle"));
            obj.put("foto", rs.getString("foto"));
            obj.put("fechaNacimiento", rs.getString("fechaNacimiento"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public JSONObject buscarJSONObject(int id) throws SQLException, JSONException {
        String consulta = "SELECT\n"
                + "    \"Jugador\".\"id\",\n"
                + "    \"Jugador\".\"nombres\",\n"
                + "    \"Jugador\".\"apellidos\",\n"
                + "    \"Jugador\".\"idPosicion\",\n"
                + "    \"Jugador\".\"idEquipo\",\n"
                + "    \"Jugador\".\"detalle\",\n"
                + "    \"Jugador\".\"foto\",\n"
                + "    to_char(\"Jugador\".\"fechaNacimiento\", 'DD/MM/YYYY') AS fechaNacimiento\n"
                + "    FROM public.\"Jugador\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("id", rs.getInt("id"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("idPosicion", rs.getInt("idPosicion"));
            obj.put("idEquipo", rs.getInt("idEquipo"));
            obj.put("detalle", rs.getString("detalle"));
            obj.put("foto", rs.getString("foto"));
            obj.put("fechaNacimiento", rs.getString("fechaNacimiento"));
        }
        rs.close();
        ps.close();
        return obj;
    }

    public Jugador buscar(int id) throws SQLException, JSONException {
        String consulta = "SELECT *\n"
                + "    FROM public.\"Jugador\"\n"
                + "    WHERE \"id\" = ?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Jugador obj = null;
        if (rs.next()) {
            obj = new Jugador(con);
            obj.setId(rs.getInt("id"));
            obj.setNombres(rs.getString("nombres"));
            obj.setApellidos(rs.getString("apellidos"));
            obj.setIdPosicion(rs.getInt("idPosicion"));
            obj.setIdEquipo(rs.getInt("idEquipo"));
            obj.setDetalle(rs.getString("detalle"));
            obj.setFoto(rs.getString("foto"));
            obj.setFechaNacimiento(rs.getDate("fechaNacimiento"));
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
        obj.put("nombres", nombres);
        obj.put("apellidos", apellidos);
        obj.put("idPosicion", idPosicion);
        obj.put("idEquipo", idEquipo);
        obj.put("detalle", detalle);
        obj.put("foto", foto);
        obj.put("fechaNacimiento", fechaNacimiento == null ? "" : f.format(fechaNacimiento));
        return obj;
    }


    /* ********************************************************************** */
    // Negocio
}
