package orm;

import conexion.Conexion;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Escribir {

    public static void escribirClases(String nombreTabla, String tablespace, Conexion con) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {

            fichero = new FileWriter("c:/Modelo/" + nombreTabla + ".java");
            pw = new PrintWriter(fichero);

            ResultSet campos = DataBase.Campostabla(nombreTabla, con, tablespace);
            List<CamposTabla> lista = new ArrayList<>();

            while (campos.next()) {
                CamposTabla c = new CamposTabla(campos.getString("COLUMN_NAME"), campos.getString("DATA_TYPE"));
                lista.add(c);
            }
            con.Close();

            pw.println(Cimport());
            pw.println("\n");
            pw.println(Cinit(nombreTabla));
            pw.println("\n");
            pw.println(cinisializar(nombreTabla, lista));
            pw.println("\n");
            pw.println(CConstructorVacio(nombreTabla));
            pw.println("\n");
            pw.println(CConstructorLleno(nombreTabla, lista));
            pw.println("\n");
            pw.println(CInsertar(nombreTabla, lista, tablespace));
            pw.println("\n");
            pw.println(CUpdate(nombreTabla, lista, tablespace));
            pw.println("\n");
            pw.println(CEliminar(nombreTabla, lista, tablespace));
            pw.println("\n");
            pw.println(CTodos(nombreTabla, tablespace, lista));
            pw.println("\n");
            pw.println(CBuscar(nombreTabla, tablespace, lista));
            pw.println("\n");
            pw.println(CGetYSet(nombreTabla, lista));
            pw.println("\n");
            pw.println("}");
            con.Close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private static String CInsertar(String nombre_clase, List<CamposTabla> lista, String tablespace) throws SQLException {
        String ccampos = "public int Insertar() throws SQLException {\n";
        ccampos += "String consulta = \"insert into " + tablespace + "." + nombre_clase + "(";

        for (int i = 0; i < lista.size(); i++) {

            ccampos += lista.get(i).getNombre();
            if (i != lista.size() - 1) {
                ccampos += ", ";
            }
        }

        ccampos += ") VALUES (";

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getTipo().equals("NUMBER")) {
                ccampos += "\"+" + lista.get(i).getNombre() + "+\"";
            }
            if (lista.get(i).getTipo().equals("VARCHAR2")) {
                ccampos += "'\"+" + lista.get(i).getNombre() + "+\"'";
            }
            if (lista.get(i).getTipo().equals("FLOAT")) {
                ccampos += "\"+" + lista.get(i).getNombre() + "+\"";
            }
            if (lista.get(i).getTipo().equals("DATE")) {
                ccampos += "\"+get" + lista.get(i).getNombre() + "_INSERT()+\"";
            }
            if (i != lista.size() - 1) {
                ccampos += ",";
            }
        }

        ccampos += ")\";\n";

        ccampos += "con.EjecutarSentencia(consulta);\n";
        ccampos += "consulta = \"select " + tablespace + "." + nombre_clase + "_SEQ.currval as ID from dual\";\n";
        ccampos += "ResultSet rs = con.EjecutarConsulta(consulta);";
        ccampos += "rs.next();\n";
        ccampos += "return rs.getInt(\"ID\");\n";
        ccampos += "}";
        return ccampos;
    }

    private static String CUpdate(String nombre_clase, List<CamposTabla> lista, String tablespace) throws SQLException {
        String ccampos = "public boolean Update() throws SQLException {\n";
        ccampos += "String consulta = \"update " + tablespace + "." + nombre_clase + " set ";

        for (int i = 0; i < lista.size(); i++) {
            ccampos += lista.get(i).getNombre() + "=";
            if (lista.get(i).getTipo().equals("NUMBER")) {
                ccampos += "\"+" + lista.get(i).getNombre() + "+\"";
            }
            if (lista.get(i).getTipo().equals("VARCHAR2")) {
                ccampos += "'\"+" + lista.get(i).getNombre() + "+\"'";
            }
            if (lista.get(i).getTipo().equals("FLOAT")) {
                ccampos += "\"+" + lista.get(i).getNombre() + "+\"";
            }
            if (lista.get(i).getTipo().equals("DATE")) {
                ccampos += "\"+get" + lista.get(i).getNombre() + "_INSERT()+\"";
            }
            if (i != lista.size() - 1) {
                ccampos += ",";
            }
        }
        ccampos += " where ID = \"+getID();\n";

        ccampos += "con.EjecutarSentencia(consulta);\n";
        ccampos += "return true;\n";
        ccampos += "}";
        return ccampos;
    }

    private static String CGetYSet(String nombre_clase, List<CamposTabla> lista) throws SQLException {
        String ccampos = "";

        for (int i = 0; i < lista.size(); i++) {
            String tipo = lista.get(i).getTipo();
            String nombre = lista.get(i).getNombre();

            if (tipo.equals("NUMBER")) {
                ccampos += "public int get" + nombre + "(){\nreturn " + nombre + ";\n}\n";
                ccampos += "public void set" + nombre + "(int " + nombre + "){\n this." + nombre + " = " + nombre + ";\n}\n";
            }
            if (tipo.equals("VARCHAR2")) {
                ccampos += "public String get" + nombre + "(){\nreturn " + nombre + "==null?\"\":" + nombre + ";\n}\n";
                ccampos += "public void set" + nombre + "(String " + nombre + "){\n this." + nombre + " = " + nombre + ";\n}\n";
            }
            if (tipo.equals("FLOAT")) {
                ccampos += "public double get" + nombre + "(){\nreturn " + nombre + ";\n}\n";
                ccampos += "public void set" + nombre + "(Double " + nombre + "){\n this." + nombre + " = " + nombre + ";\n}\n";
            }
            if (tipo.equals("DATE")) {

                ccampos += "public String get" + nombre + "_FORMATO() {\nif(" + nombre + "!=null)\n{\nSimpleDateFormat sdf = new SimpleDateFormat(\"dd/MM/yyyy\");\nreturn sdf.format(" + nombre + ");\n}\nelse\nreturn \"\";\n}\n";
                ccampos += "public String get" + nombre + "_INSERT() {\nif(" + nombre + "!=null)\n{\nSimpleDateFormat sdf = new SimpleDateFormat(\"dd/MM/yyyy\");\nreturn \"to_date('\"+sdf.format(" + nombre + ")+\"','dd/MM/yyyy')\";\n}\nelse\nreturn \"null\";\n}";
                ccampos += "public Date get" + nombre + "() {\nreturn " + nombre + ";\n}";

                ccampos += "public void set" + nombre + "(Date " + nombre + "){\n this." + nombre + " = " + nombre + ";\n}\n";
            }
        }

        ccampos += "public Conexion getCon(){\nreturn this.con;\n}\n";
        ccampos += "public void setCon(Conexion con){\n this.con=con;\n}\n";
        return ccampos;
    }

    private static String CEliminar(String nombre_clase, List<CamposTabla> lista, String tablespace) {
        String ccampos = "public boolean EliminarXid()  throws SQLException {\n";
        ccampos += "String consulta = \"delete from " + tablespace + "." + nombre_clase + " where ID= \"+getID();\n";
        ccampos += "con.EjecutarSentencia(consulta);\n";
        ccampos += "return true;\n";
        ccampos += "}";
        ccampos += "\n\n";
        ccampos += "public boolean EliminarXid(int id)  throws SQLException {\n";
        ccampos += "String consulta = \"delete from " + tablespace + "." + nombre_clase + " where ID= \"+id;\n";
        ccampos += "con.EjecutarSentencia(consulta);\n";
        ccampos += "return true;\n";
        ccampos += "}";
        return ccampos;
    }

    private static String CTodos(String nombre_clase, String tablespace, List<CamposTabla> lista) throws SQLException {
        String ccampos = "public JSONArray Todos()  throws SQLException, JSONException{\n";
        ccampos += "String consulta = \"select * from " + tablespace + "." + nombre_clase + "\";\n";
        ccampos += "PreparedStatement ps=con.statamet(consulta);\n";
        ccampos += "ResultSet rs=ps.executeQuery();\n";
        ccampos += "JSONArray json = new JSONArray();\n";
        ccampos += "JSONObject obj;\n";
        ccampos += "while(rs.next()){\n";
        ccampos += "obj= new JSONObject();\n";
        for (int i = 0; i < lista.size(); i++) {

            if (lista.get(i).getTipo().equals("NUMBER")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getInt(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("VARCHAR2")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getString(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("FLOAT")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getDouble(\"" + lista.get(i).getNombre() + "\"));\"";
            }
            if (lista.get(i).getTipo().equals("DATE")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getDate(\"" + lista.get(i).getNombre() + "\"));\n";
            }
        }
        ccampos += "json.put(obj);\n";
        ccampos += "}\n";
        ccampos += "rs.close();\n";
        ccampos += "ps.close();\n";
        ccampos += "return json;\n";

        ccampos += "}";
        return ccampos;
    }

    private static String CBuscar(String nombre_clase, String tablespace, List<CamposTabla> lista) throws SQLException {

        String ccampos = "public JSONObject Buscar(int id)  throws SQLException, JSONException{\n";
        ccampos += "String consulta = \"select * from " + tablespace + "." + nombre_clase + " where ID=\"+id;\n";
        ccampos += "PreparedStatement ps=con.statamet(consulta);\n";
        ccampos += "ResultSet rs=ps.executeQuery();\n";
        ccampos += "JSONObject obj = new JSONObject();\n";
        ccampos += "if(rs.next()){\n";
        for (int i = 0; i < lista.size(); i++) {

            if (lista.get(i).getTipo().equals("NUMBER")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getInt(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("VARCHAR2")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getString(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("FLOAT")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getDouble(\"" + lista.get(i).getNombre() + "\"));\"";
            }
            if (lista.get(i).getTipo().equals("DATE")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getDate(\"" + lista.get(i).getNombre() + "\"));\n";
            }
        }
        ccampos += "}\n";
        ccampos += "rs.close();\n";
        ccampos += "ps.close();\n";
        ccampos += "return obj;\n";
        ccampos += "}";
        return ccampos;
    }

    private static String Cimport() {
        return "package  modelo;\n\nimport Conexion.Conexion;\n"
                + "import java.sql.PreparedStatement;\n"
                + "import java.sql.SQLException;\n"
                + "import java.text.SimpleDateFormat;\n"
                + "import java.util.Date;\n"
                + "import java.sql.ResultSet;\n"
                + "import org.json.JSONArray;\n"
                + "import org.json.JSONException;\n"
                + "import org.json.JSONObject;\n";
    }

    private static String Cinit(String nombreClase) {
        return "public class " + nombreClase + " {\n";
    }

    private static String CConstructorVacio(String nombreClase) {
        return "public " + nombreClase + "(Conexion con) {\nthis.con=con;\n}\n";
    }

    private static String CConstructorLleno(String nombre_clase, List<CamposTabla> lista) throws SQLException {

        String ccampos = "public " + nombre_clase + "( ";

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getTipo().equals("NUMBER")) {
                ccampos += "int ";
            }
            if (lista.get(i).getTipo().equals("VARCHAR2")) {
                ccampos += "String ";
            }
            if (lista.get(i).getTipo().equals("FLOAT")) {
                ccampos += "Double ";
            }
            if (lista.get(i).getTipo().equals("DATE")) {
                ccampos += "Date ";
            }
            ccampos += lista.get(i).getNombre();

            if (i != lista.size() - 1) {
                ccampos += ", ";
            }

        }
        ccampos += ")\n{\n";

        ccampos += CConstructorLlenoCampos(nombre_clase, lista);

        ccampos += "}\n";
        return ccampos;
    }

    private static String CConstructorLlenoCampos(String nombre_clase, List<CamposTabla> lista) throws SQLException {

        String ccampos = "";

        for (int i = 0; i < lista.size(); i++) {
            ccampos += "this." + lista.get(i).getNombre() + " = " + lista.get(i).getNombre() + ";\n";
        }
        return ccampos;
    }

    private static String cinisializar(String nombre_clase, List<CamposTabla> lista) throws SQLException {

        String ccampos = "";
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getTipo().equals("NUMBER")) {
                ccampos += "private int ";
            }
            if (lista.get(i).getTipo().equals("VARCHAR2")) {
                ccampos += "private String ";
            }
            if (lista.get(i).getTipo().equals("FLOAT")) {
                ccampos += "private Double ";
            }
            if (lista.get(i).getTipo().equals("DATE")) {
                ccampos += "private Date ";
            }

            ccampos += lista.get(i).getNombre() + ";\n";
        }
        ccampos += "private Conexion con = null;\n";
        return ccampos;
    }

    /*public static void main(String args[]) throws SQLException {
     String tablespace="MOVIL";        
     Conexion con = new Conexion(tablespace, "oracle123");
     List<String> tablas = DataBase.tablas(con,tablespace);

     for (int i = 0; i < tablas.size(); i++) {
     Escribir.escribirClases(tablas.get(i),tablespace,con);
     }
     con.Close();
     }*/
}
