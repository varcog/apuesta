package  modelo;

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
	private int idUsuario;
	private Double debe;
	private Double haber;
	private int idUsuarioResponsable;
	private Date fecha;
	private Conexion con = null;



public PagoEfectivo(Conexion con) {
this.con=con;
}



public PagoEfectivo( int id, int idPrestamo, int idBilletera, int idUsuario, Double debe, Double haber, int idUsuarioResponsable, Date fecha, Conexion con)
{
this.id = id;
this.idPrestamo = idPrestamo;
this.idBilletera = idBilletera;
this.idUsuario = idUsuario;
this.debe = debe;
this.haber = haber;
this.idUsuarioResponsable = idUsuarioResponsable;
this.fecha = fecha;
this.con=con;
}



public PagoEfectivo( int id, int idPrestamo, int idBilletera, int idUsuario, Double debe, Double haber, int idUsuarioResponsable, Date fecha)
{
this.id = id;
this.idPrestamo = idPrestamo;
this.idBilletera = idBilletera;
this.idUsuario = idUsuario;
this.debe = debe;
this.haber = haber;
this.idUsuarioResponsable = idUsuarioResponsable;
this.fecha = fecha;
}



public int getId(){
return id;
}
public void setId(int id){
 this.id = id;
}
public int getIdPrestamo(){
return idPrestamo;
}
public void setIdPrestamo(int idPrestamo){
 this.idPrestamo = idPrestamo;
}
public int getIdBilletera(){
return idBilletera;
}
public void setIdBilletera(int idBilletera){
 this.idBilletera = idBilletera;
}
public int getIdUsuario(){
return idUsuario;
}
public void setIdUsuario(int idUsuario){
 this.idUsuario = idUsuario;
}
public double getDebe(){
return debe;
}
public void setDebe(Double debe){
 this.debe = debe;
}
public double getHaber(){
return haber;
}
public void setHaber(Double haber){
 this.haber = haber;
}
public int getIdUsuarioResponsable(){
return idUsuarioResponsable;
}
public void setIdUsuarioResponsable(int idUsuarioResponsable){
 this.idUsuarioResponsable = idUsuarioResponsable;
}
public Date getFecha() {
return fecha;
}public void setFecha(Date fecha){
 this.fecha = fecha;
}
public Conexion getCon(){
return this.con;
}
public void setCon(Conexion con){
 this.con=con;
}



    ////////////////////////////////////////////////////////////////////////////

public int insert() throws SQLException {
String consulta = "INSERT INTO public.\"PagoEfectivo\"(\n"
+ "    \"idPrestamo\", \"idBilletera\", \"idUsuario\", \"debe\", \"haber\", \"idUsuarioResponsable\", \"fecha\")\n"
+ "    VALUES (?, ?, ?, ?, ?, ?, ?)\n";
this.id  = con.ejecutarInsert(consulta, "id", idPrestamo > 0 ? idPrestamo : null, idBilletera > 0 ? idBilletera : null, idUsuario > 0 ? idUsuario : null, debe, haber, idUsuarioResponsable > 0 ? idUsuarioResponsable : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()));
return this.id;
}


public void update() throws SQLException {
String consulta = "UPDATE public.\"PagoEfectivo\"\n"
+ "    SET \"idPrestamo\" = ?, \"idBilletera\" = ?, \"idUsuario\" = ?, \"debe\" = ?, \"haber\" = ?, \"idUsuarioResponsable\" = ?, \"fecha\" = ?\n"
+ "    WHERE \"id\"=?";
con.ejecutarSentencia(consulta, idPrestamo > 0 ? idPrestamo : null, idBilletera > 0 ? idBilletera : null, idUsuario > 0 ? idUsuario : null, debe, haber, idUsuarioResponsable > 0 ? idUsuarioResponsable : null, fecha == null ? null : new java.sql.Timestamp(fecha.getTime()), id);
}


public void delete()  throws SQLException {
String consulta = "delete from public.\"PagoEfectivo\" where \"id\"= ?;";
con.ejecutarSentencia(consulta);
}


public JSONArray todos()  throws SQLException, JSONException{
String consulta = "SELECT\n"
+ "    \"PagoEfectivo\".\"id\",\n"
+ "    \"PagoEfectivo\".\"idPrestamo\",\n"
+ "    \"PagoEfectivo\".\"idBilletera\",\n"
+ "    \"PagoEfectivo\".\"idUsuario\",\n"
+ "    \"PagoEfectivo\".\"debe\",\n"
+ "    \"PagoEfectivo\".\"haber\",\n"
+ "    \"PagoEfectivo\".\"idUsuarioResponsable\",\n"
+ "    to_char(\"PagoEfectivo\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
+ "    FROM public.\"PagoEfectivo\";";
PreparedStatement ps=con.statamet(consulta);
ResultSet rs=ps.executeQuery();
JSONArray json = new JSONArray();
JSONObject obj;
while(rs.next()){
obj= new JSONObject();
obj.put("id",rs.getInt("id"));
obj.put("idPrestamo",rs.getInt("idPrestamo"));
obj.put("idBilletera",rs.getInt("idBilletera"));
obj.put("idUsuario",rs.getInt("idUsuario"));
obj.put("debe",rs.getDouble("debe"));
obj.put("haber",rs.getDouble("haber"));
obj.put("idUsuarioResponsable",rs.getInt("idUsuarioResponsable"));
obj.put("fecha",rs.getString("fecha"));
json.put(obj);
}
rs.close();
ps.close();
return json;
}


public JSONObject buscarJSONObject(int id)  throws SQLException, JSONException{
String consulta = "SELECT\n"
+ "    \"PagoEfectivo\".\"id\",\n"
+ "    \"PagoEfectivo\".\"idPrestamo\",\n"
+ "    \"PagoEfectivo\".\"idBilletera\",\n"
+ "    \"PagoEfectivo\".\"idUsuario\",\n"
+ "    \"PagoEfectivo\".\"debe\",\n"
+ "    \"PagoEfectivo\".\"haber\",\n"
+ "    \"PagoEfectivo\".\"idUsuarioResponsable\",\n"
+ "    to_char(\"PagoEfectivo\".\"fecha\", 'DD/MM/YYYY HH24:MI:SS') AS fecha\n"
+ "    FROM public.\"PagoEfectivo\"\n"
+ "    WHERE \"id\" = ?;";
PreparedStatement ps = con.statametObject(consulta, id);
ResultSet rs=ps.executeQuery();
JSONObject obj = new JSONObject();
if(rs.next()){
obj.put("id",rs.getInt("id"));
obj.put("idPrestamo",rs.getInt("idPrestamo"));
obj.put("idBilletera",rs.getInt("idBilletera"));
obj.put("idUsuario",rs.getInt("idUsuario"));
obj.put("debe",rs.getDouble("debe"));
obj.put("haber",rs.getDouble("haber"));
obj.put("idUsuarioResponsable",rs.getInt("idUsuarioResponsable"));
obj.put("fecha",rs.getString("fecha"));
}
rs.close();
ps.close();
return obj;
}


public PagoEfectivo buscar(int id)  throws SQLException, JSONException{
String consulta = "SELECT *\n"
+ "    FROM public.\"PagoEfectivo\"\n"
+ "    WHERE \"id\" = ?;";
PreparedStatement ps = con.statametObject(consulta, id);
ResultSet rs=ps.executeQuery();
PagoEfectivo obj = null;
if(rs.next()){
obj = new PagoEfectivo(con);
obj.setId(rs.getInt("id"));
obj.setIdPrestamo(rs.getInt("idPrestamo"));
obj.setIdBilletera(rs.getInt("idBilletera"));
obj.setIdUsuario(rs.getInt("idUsuario"));
obj.setDebe(rs.getDouble("debe"));
obj.setHaber(rs.getDouble("haber"));
obj.setIdUsuarioResponsable(rs.getInt("idUsuarioResponsable"));
obj.setFecha(rs.getTimestamp("fecha"));
}
rs.close();
ps.close();
return obj;
}


public JSONObject toJSONObject()  throws JSONException{
SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
JSONObject obj = new JSONObject();
obj.put("id",id);
obj.put("idPrestamo",idPrestamo);
obj.put("idBilletera",idBilletera);
obj.put("idUsuario",idUsuario);
obj.put("debe",debe);
obj.put("haber",haber);
obj.put("idUsuarioResponsable",idUsuarioResponsable);
obj.put("fecha",fecha == null ? "" : f1.format(fecha));
return obj;
}


    /* ********************************************************************** */
    // Negocio


}
