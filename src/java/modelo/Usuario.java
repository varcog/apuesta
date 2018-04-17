package modelo;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.StringMD;

public class Usuario {

    private int id;
    private String usuario;
    private String password;
    private String nombres;
    private String apellidos;
    private String foto;
    private Date fecha_nacimiento;
    private Date fecha_creacion;
    private Date fecha_anulacion;
    private String ci;
    private String telefono;
    private String direccion;
    private String sexo;
    private int id_perfil;
    private int id_usuario_creador;
    private int id_usuario_recomienda;
    private int id_usuario_aprueba;
    private int estado;
    private Conexion con;

    public static final int ESTADO_INACTIVO = 0;
    public static final int ESTADO_CREADO = 1;
    public static final int ESTADO_APROBADO = 2;

    public Usuario(Conexion con) {
        this.con = con;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Date getFecha_anulacion() {
        return fecha_anulacion;
    }

    public void setFecha_anulacion(Date fecha_anulacion) {
        this.fecha_anulacion = fecha_anulacion;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getId_perfil() {
        return id_perfil;
    }

    public void setId_perfil(int id_perfil) {
        this.id_perfil = id_perfil;
    }

    public int getId_usuario_creador() {
        return id_usuario_creador;
    }

    public void setId_usuario_creador(int id_usuario_creador) {
        this.id_usuario_creador = id_usuario_creador;
    }

    public int getId_usuario_recomienda() {
        return id_usuario_recomienda;
    }

    public void setId_usuario_recomienda(int id_usuario_recomienda) {
        this.id_usuario_recomienda = id_usuario_recomienda;
    }

    public int getId_usuario_aprueba() {
        return id_usuario_aprueba;
    }

    public void setId_usuario_aprueba(int id_usuario_aprueba) {
        this.id_usuario_aprueba = id_usuario_aprueba;
    }

    public int isEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    ////////////////////////////////////////////////////////////////////////////
    public int insert() throws SQLException {
        fecha_creacion = new Date();
        String consulta = "INSERT INTO public.\"Usuario\"(\n"
                + "	\"nombres\", \"apellidos\", \"foto\", \"fecha_nacimiento\", \"telefono\", \"sexo\", \"direccion\", \"fecha_creacion\", \"id_usuario_aprueba\", \"id_usuario_recomienda\", \"estado\", \"fecha_anulacion\", \"usuario\", \"password\", \"id_perfil\", \"ci\", \"id_usuario_creador\")\n"
                + "	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        this.id = con.EjecutarInsert(consulta, "id", nombres, apellidos, foto, fecha_nacimiento == null ? null : new java.sql.Date(fecha_nacimiento.getTime()), telefono, sexo, direccion, fecha_creacion == null ? null : new java.sql.Date(fecha_creacion.getTime()), id_usuario_aprueba > 0 ? id_usuario_aprueba : null, id_usuario_recomienda > 0 ? id_usuario_recomienda : null, ESTADO_CREADO, fecha_anulacion == null ? null : new java.sql.Date(fecha_anulacion.getTime()), usuario, password, id_perfil > 0 ? id_perfil : null, ci, id_usuario_creador > 0 ? id_usuario_creador : null);
        return this.id;
    }

    public void delete() throws SQLException {
        String consulta = "DELETE FROM public.\"Usuario\"\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, id);
    }

    public JSONArray todos() throws SQLException, JSONException {
        String consulta = "SELECT * FROM public.\"Usuario\"\n"
                + "ORDER BY \"usuario\" ASC ";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date aux;
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("usuario", rs.getString("usuario"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("foto", rs.getString("foto"));
            aux = rs.getDate("fecha_nacimiento");
            obj.put("fecha_nacimiento", aux == null ? "" : f.format(aux));
            aux = rs.getDate("fecha_creacion");
            obj.put("fecha_creacion", aux == null ? "" : f.format(aux));
            aux = rs.getDate("fecha_anulacion");
            obj.put("fecha_anulacion", aux == null ? "" : f.format(aux));
            obj.put("ci", rs.getString("ci"));
            obj.put("telefono", rs.getString("telefono"));
            obj.put("direccion", rs.getString("direccion"));
            obj.put("sexo", rs.getString("sexo"));
            obj.put("id_perfil", rs.getInt("id_perfil"));
            obj.put("id_usuario_creador", rs.getInt("id_usuario_creador"));
            obj.put("id_usuario_recomienda", rs.getInt("id_usuario_recomienda"));
            obj.put("id_usuario_aprueba", rs.getInt("id_usuario_aprueba"));
            obj.put("estado", rs.getInt("estado"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public Usuario buscar(int id) throws SQLException {
        String consulta = "SELECT * FROM public.\"usuario\"\n"
                + "	WHERE \"id\"=?;";
        PreparedStatement ps = con.statametObject(consulta, id);
        ResultSet rs = ps.executeQuery();
        Usuario u = new Usuario(con);
        if (rs.next()) {
            u.setId(rs.getInt("id"));
            u.setUsuario(rs.getString("usuario"));
            u.setNombres(rs.getString("nombres"));
            u.setApellidos(rs.getString("apellidos"));
            u.setFoto(rs.getString("foto"));
            u.setFecha_nacimiento(rs.getDate("fecha_nacimiento"));
            u.setFecha_creacion(rs.getDate("fecha_creacion"));
            u.setFecha_anulacion(rs.getDate("fecha_anulacion"));
            u.setCi(rs.getString("ci"));
            u.setTelefono(rs.getString("telefono"));
            u.setDireccion(rs.getString("direccion"));
            u.setSexo(rs.getString("sexo"));
            u.setId_perfil(rs.getInt("id_perfil"));
            u.setId_usuario_creador(rs.getInt("id_usuario_creador"));
            u.setId_usuario_recomienda(rs.getInt("id_usuario_recomienda"));
            u.setId_usuario_aprueba(rs.getInt("id_usuario_aprueba"));
            u.setEstado(rs.getInt("estado"));
            return u;
        }
        return null;
    }

    public JSONObject toJSONObject() throws SQLException, JSONException {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("usuario", usuario);
        obj.put("nombres", nombres);
        obj.put("apellidos", apellidos);
        obj.put("foto", foto);
        obj.put("fecha_nacimiento", fecha_nacimiento == null ? "" : f.format(fecha_nacimiento));
        obj.put("fecha_creacion", fecha_creacion == null ? "" : f.format(fecha_creacion));
        obj.put("fecha_anulacion", fecha_anulacion == null ? "" : f.format(fecha_anulacion));
        obj.put("ci", ci);
        obj.put("telefono", telefono);
        obj.put("direccion", direccion);
        obj.put("sexo", sexo);
        obj.put("id_perfil", id_perfil);
        obj.put("id_usuario_creador", id_usuario_creador);
        obj.put("id_usuario_recomienda", id_usuario_recomienda);
        obj.put("id_usuario_aprueba", id_usuario_aprueba);
        obj.put("estado", estado);
        return obj;
    }

    ////////////////////////////////////////////////////////////////////////////
    public Usuario Buscar(String usr, String pass) throws SQLException {
        pass = StringMD.getStringMessageDigest(pass, StringMD.SHA512);
        String consulta = "select * from public.\"usuario\" where \"usuario\" = (?) and \"password\" = (?) and \"estado\"=true";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, usr);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();
        List<Usuario> lista = Cargar(rs);
        if (lista.size() > 0) {
            return lista.get(0);
        } else {
            return null;
        }
    }

    public List<Usuario> Cargar(ResultSet rs) throws SQLException {
        List<Usuario> lista = new ArrayList<Usuario>();
        while (rs.next()) {
            Usuario u = new Usuario(con);
            u.setId(rs.getInt("id"));
            u.setUsuario(rs.getString("usuario"));
            u.setNombres(rs.getString("nombres"));
            u.setApellidos(rs.getString("apellidos"));
            u.setFoto(rs.getString("foto"));
            u.setFecha_nacimiento(rs.getDate("fecha_nacimiento"));
            u.setFecha_creacion(rs.getDate("fecha_creacion"));
            u.setFecha_anulacion(rs.getDate("fecha_anulacion"));
            u.setCi(rs.getString("ci"));
            u.setTelefono(rs.getString("telefono"));
            u.setDireccion(rs.getString("direccion"));
            u.setSexo(rs.getString("sexo"));
            u.setId_perfil(rs.getInt("id_perfil"));
            u.setId_usuario_creador(rs.getInt("id_usuario_creador"));
            u.setId_usuario_recomienda(rs.getInt("id_usuario_recomienda"));
            u.setId_usuario_aprueba(rs.getInt("id_usuario_aprueba"));
            u.setEstado(rs.getInt("estado"));
            lista.add(u);
        }
        return lista;
    }

    public JSONArray todosConPerfil() throws SQLException, JSONException {
        String consulta = "SELECT \"Usuario\".*, \"Perfil\".\"nombre\" AS perfil\n"
                + "     FROM public.\"Usuario\"\n"
                + "          LEFT JOIN public.\"Perfil\" ON \"Usuario\".\"id_perfil\" = \"Perfil\".\"id\"\n"
                + "ORDER BY \"Usuario\".\"usuario\" ASC ";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date aux;
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("usuario", rs.getString("usuario"));
            obj.put("nombres", rs.getString("nombres"));
            obj.put("apellidos", rs.getString("apellidos"));
            obj.put("foto", rs.getString("foto"));
            aux = rs.getDate("fecha_nacimiento");
            obj.put("fecha_nacimiento", aux == null ? "" : f.format(aux));
            aux = rs.getDate("fecha_creacion");
            obj.put("fecha_creacion", aux == null ? "" : f.format(aux));
            aux = rs.getDate("fecha_anulacion");
            obj.put("fecha_anulacion", aux == null ? "" : f.format(aux));
            obj.put("ci", rs.getString("ci"));
            obj.put("telefono", rs.getString("telefono"));
            obj.put("direccion", rs.getString("direccion"));
            obj.put("sexo", rs.getString("sexo"));
            obj.put("id_perfil", rs.getInt("id_perfil"));
            obj.put("perfil", rs.getInt("perfil"));
            obj.put("id_usuario_creador", rs.getInt("id_usuario_creador"));
            obj.put("id_usuario_recomienda", rs.getInt("id_usuario_recomienda"));
            obj.put("id_usuario_aprueba", rs.getInt("id_usuario_aprueba"));
            obj.put("estado", rs.getInt("estado"));
            json.put(obj);
        }
        rs.close();
        ps.close();
        return json;
    }

    public void updateDatos() throws SQLException {
        java.sql.Date fn = fecha_nacimiento == null ? null : new java.sql.Date(fecha_nacimiento.getTime());
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"nombres\"=?,\"apellidos\"=?,\"fecha_nacimiento\"=?,\"sexo\"=?,\"id_perfil\"=?,\"telefono\"=?,\"direccion\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, nombres, apellidos, fn, sexo, id_perfil, telefono, direccion, id);
    }

    public void updateContrasena() throws SQLException {
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"password\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, password, id);
    }

    public void updateEstado() throws SQLException {
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"estado\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, estado, id);
    }

    public void updateFoto() throws SQLException {
        String consulta = "UPDATE public.\"Usuario\"\n"
                + "	SET \"foto\"=?\n"
                + "	WHERE \"id\"=?;";
        con.EjecutarSentencia(consulta, estado, id);
    }

    public String getNombreCompleto() {
        String res = "";
        if (nombres != null) {
            res += nombres;
        }
        if (apellidos != null) {
            res += apellidos;
        }
        return res;
    }

    ////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws SQLException {
//        Conexion con = Conexion.getConeccion();
//        usuario u = new usuario(con);
//        if (u.Buscar("delivery", "delivery") == null) {
//            System.out.println("false");
//        } else {
//            System.out.println("true");
//        }

        Date dia = new Date();
        java.sql.Date name = null;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(f.format(name));
    }
}
