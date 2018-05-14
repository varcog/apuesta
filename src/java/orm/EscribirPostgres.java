package orm;

import conexion.Conexion;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EscribirPostgres {

    public static String rutaF = System.getProperty("os.name").toLowerCase().contains("windows") ? "c:/Modelo/" : System.getProperty("user.home") + "/Modelo";

    public static void escribirClases(String nombreTabla, String tablespace, Conexion con) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            File f = new File(rutaF);
            if (!f.exists()) {
                f.mkdirs();
            }
            fichero = new FileWriter(rutaF + nombreTabla + ".java");
            pw = new PrintWriter(fichero);

            ResultSet campos = DataBasePostgres.Campostabla(nombreTabla, con, tablespace);
            List<CamposTabla> lista = new ArrayList<>();

            while (campos.next()) {
                CamposTabla c = new CamposTabla(campos.getString("nombre_columna"), campos.getString("tipo"));
                lista.add(c);
            }
//            con.close();

            pw.println(Cimport());
            pw.println("\n");
            pw.println(Cinit(nombreTabla));
            pw.println("\n");
            pw.println(cinisializar(nombreTabla, lista));
            pw.println("\n");
            pw.println(CConstructorVacio(nombreTabla));
            pw.println("\n");
            pw.println(CConstructorLlenoConexion(nombreTabla, lista));
            pw.println("\n");
            pw.println(CConstructorLleno(nombreTabla, lista));
            pw.println("\n");
            pw.println(CGetYSet(nombreTabla, lista));
            pw.println("\n");
            pw.println("    ////////////////////////////////////////////////////////////////////////////\n");
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
            pw.println(CBuscarObject(nombreTabla, tablespace, lista));
            pw.println("\n");
            pw.println(CToJSON(nombreTabla, tablespace, lista));
            pw.println("\n");
            pw.println("    /* ********************************************************************** */");
            pw.println("    // Negocio");
            pw.println("\n");
            pw.println("}");
//            con.close();
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
        String nombres = "";
        String values = "";
        String insert = "";
        for (int i = 0; i < lista.size(); i++) {
            if (!lista.get(i).getNombre().equals("id")) {
                nombres += "\\\"" + lista.get(i).getNombre() + "\\\"";
                values += "?";

                if (lista.get(i).getTipo().equals("date")) {
                    insert += lista.get(i).getNombre() + " == null ? null : new java.sql.Timestamp(" + lista.get(i).getNombre() + ".getTime())";
                }
                if (lista.get(i).getTipo().contains("timestamp")) {
                    insert += lista.get(i).getNombre() + " == null ? null : new java.sql.Timestamp(" + lista.get(i).getNombre() + ".getTime())";
                } else {
                    if ("id".equals(lista.get(i).getNombre().substring(0, 2)) && lista.get(i).getTipo().equals("integer")) {
                        insert += lista.get(i).getNombre() + " > 0 ? " + lista.get(i).getNombre() + " : null";
                    } else {
                        insert += lista.get(i).getNombre();
                    }
                }
                if (i != lista.size() - 1) {
                    nombres += ", ";
                    values += ", ";
                    insert += ", ";
                }
            }
        }
        String ccampos = "public int insert() throws SQLException {\n";
        ccampos += "String consulta = \"INSERT INTO " + tablespace + ".\\\"" + nombre_clase + "\\\"(\\n\"\n";
        ccampos += "+ \"    " + nombres + ")\\n\"\n";
        ccampos += "+ \"    VALUES (" + values + ")\\n\";\n";
        ccampos += "this.id  = con.ejecutarInsert(consulta, \"id\", " + insert + ");\n";
        ccampos += "return this.id;\n";
        ccampos += "}";
        return ccampos;
    }

    private static String CUpdate(String nombre_clase, List<CamposTabla> lista, String tablespace) throws SQLException {
        String nombres = "";
        String insert = "";
        for (int i = 0; i < lista.size(); i++) {
            if (!lista.get(i).getNombre().equals("id")) {
                nombres += "\\\"" + lista.get(i).getNombre() + "\\\" = ?";
                if (lista.get(i).getTipo().equals("date")) {
                    insert += lista.get(i).getNombre() + " == null ? null : new java.sql.Date(" + lista.get(i).getNombre() + ".getTime())";
                }
                if (lista.get(i).getTipo().contains("timestamp")) {
                    insert += lista.get(i).getNombre() + " == null ? null : new java.sql.Timestamp(" + lista.get(i).getNombre() + ".getTime())";
                } else {
                    if ("id".equals(lista.get(i).getNombre().substring(0, 2)) && lista.get(i).getTipo().equals("integer")) {
                        insert += lista.get(i).getNombre() + " > 0 ? " + lista.get(i).getNombre() + " : null";
                    } else {
                        insert += lista.get(i).getNombre();
                    }
                }
                if (i != lista.size() - 1) {
                    nombres += ", ";
                    insert += ", ";
                }
            }
        }
        String ccampos = "public void update() throws SQLException {\n";
        ccampos += "String consulta = \"UPDATE " + tablespace + ".\\\"" + nombre_clase + "\\\"\\n\"\n";
        ccampos += "+ \"    SET " + nombres + "\\n\"\n";
        ccampos += "+ \"    WHERE \\\"id\\\"=?\";\n";
        ccampos += "con.ejecutarSentencia(consulta, " + insert + ", id);\n";
        ccampos += "}";
        return ccampos;
    }

    private static String CGetYSet(String nombre_clase, List<CamposTabla> lista) throws SQLException {
        String ccampos = "";

        for (int i = 0; i < lista.size(); i++) {
            String tipo = lista.get(i).getTipo();
            String nombre = lista.get(i).getNombre();
            String nombreU = lista.get(i).getNombreUpperCase();

            if (tipo.equals("integer")) {
                ccampos += "public int get" + nombreU + "(){\nreturn " + nombre + ";\n}\n";
                ccampos += "public void set" + nombreU + "(int " + nombre + "){\n this." + nombre + " = " + nombre + ";\n}\n";
            }
            if (tipo.equals("boolean")) {
                ccampos += "public boolean is" + nombreU + "(){\nreturn " + nombre + ";\n}\n";
                ccampos += "public void set" + nombreU + "(boolean " + nombre + "){\n this." + nombre + " = " + nombre + ";\n}\n";
            }
            if (tipo.contains("character")) {
                ccampos += "public String get" + nombreU + "(){\nreturn " + nombre + "==null?\"\":" + nombre + ";\n}\n";
                ccampos += "public void set" + nombreU + "(String " + nombre + "){\n this." + nombre + " = " + nombre + ";\n}\n";
            }
            if (tipo.contains("numeric")) {
                ccampos += "public double get" + nombreU + "(){\nreturn " + nombre + ";\n}\n";
                ccampos += "public void set" + nombreU + "(Double " + nombre + "){\n this." + nombre + " = " + nombre + ";\n}\n";
            }
            if (tipo.equals("date") || tipo.contains("timestamp")) {

//                ccampos += "public String get" + nombreU + "Formato() {\nif(" + nombre + "!=null)\n{\nSimpleDateFormat sdf = new SimpleDateFormat(\"dd/MM/yyyy\");\nreturn sdf.format(" + nombre + ");\n}\nelse\nreturn \"\";\n}\n";
                ccampos += "public Date get" + nombreU + "() {\nreturn " + nombre + ";\n}";
                ccampos += "public void set" + nombreU + "(Date " + nombre + "){\n this." + nombre + " = " + nombre + ";\n}\n";
            }
        }
        ccampos += "public Conexion getCon(){\nreturn this.con;\n}\n";
        ccampos += "public void setCon(Conexion con){\n this.con=con;\n}\n";
        return ccampos;
    }

    private static String CEliminar(String nombre_clase, List<CamposTabla> lista, String tablespace) {
        String ccampos = "public void delete()  throws SQLException {\n";
        ccampos += "String consulta = \"delete from " + tablespace + ".\\\"" + nombre_clase + "\\\" where \\\"id\\\"= ?;\";\n";
        ccampos += "con.ejecutarSentencia(consulta);\n";
        ccampos += "}";
//        ccampos += "\n\n";
//        ccampos += "public boolean EliminarXid(int id)  throws SQLException {\n";
//        ccampos += "String consulta = \"delete from " + tablespace + "." + nombre_clase + " where ID= \"+id;\n";
//        ccampos += "con.EjecutarSentencia(consulta);\n";
//        ccampos += "return true;\n";
//        ccampos += "}";
        return ccampos;
    }

    private static String CTodos(String nombre_clase, String tablespace, List<CamposTabla> lista) throws SQLException {
        String ccampos = "public JSONArray todos()  throws SQLException, JSONException{\n";
        ccampos += "String consulta = \"SELECT\\n\"\n";
        for (int i = 0; i < lista.size(); i++) {
            String tipo = lista.get(i).getTipo();
            String nombre = lista.get(i).getNombre();
            if (tipo.equals("date")) {
                ccampos += "+ \"    to_char(\\\"" + nombre_clase + "\\\".\\\"" + nombre + "\\\", 'DD/MM/YYYY') AS " + nombre;
            }
            if (tipo.contains("timestamp")) {
                ccampos += "+ \"    to_char(\\\"" + nombre_clase + "\\\".\\\"" + nombre + "\\\", 'DD/MM/YYYY HH24:MI:SS') AS " + nombre;
            } else {
                ccampos += "+ \"    \\\"" + nombre_clase + "\\\".\\\"" + nombre + "\\\"";
            }
            if (i != lista.size() - 1) {
                ccampos += ",\\n\"\n";
            } else {
                ccampos += "\\n\"\n";
            }
        }
        ccampos += "+ \"    FROM " + tablespace + ".\\\"" + nombre_clase + "\\\";\";\n";
        ccampos += "PreparedStatement ps=con.statamet(consulta);\n";
        ccampos += "ResultSet rs=ps.executeQuery();\n";
        ccampos += "JSONArray json = new JSONArray();\n";
        ccampos += "JSONObject obj;\n";
        ccampos += "while(rs.next()){\n";
        ccampos += "obj= new JSONObject();\n";
        for (int i = 0; i < lista.size(); i++) {

            if (lista.get(i).getTipo().equals("boolean")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getBoolean(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("integer")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getInt(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().contains("character")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getString(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().contains("numeric")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getDouble(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("date")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getString(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().contains("timestamp")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getString(\"" + lista.get(i).getNombre() + "\"));\n";
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
        String ccampos = "public JSONObject buscarJSONObject(int id)  throws SQLException, JSONException{\n";
        ccampos += "String consulta = \"SELECT\\n\"\n";
        for (int i = 0; i < lista.size(); i++) {
            String tipo = lista.get(i).getTipo();
            String nombre = lista.get(i).getNombre();
            if (tipo.equals("date")) {
                ccampos += "+ \"    to_char(\\\"" + nombre_clase + "\\\".\\\"" + nombre + "\\\", 'DD/MM/YYYY') AS " + nombre;
            }
            if (tipo.contains("timestamp")) {
                ccampos += "+ \"    to_char(\\\"" + nombre_clase + "\\\".\\\"" + nombre + "\\\", 'DD/MM/YYYY HH24:MI:SS') AS " + nombre;
            } else {
                ccampos += "+ \"    \\\"" + nombre_clase + "\\\".\\\"" + nombre + "\\\"";
            }
            if (i != lista.size() - 1) {
                ccampos += ",\\n\"\n";
            } else {
                ccampos += "\\n\"\n";
            }
        }
        ccampos += "+ \"    FROM " + tablespace + ".\\\"" + nombre_clase + "\\\"\\n\"\n";
        ccampos += "+ \"    WHERE \\\"id\\\" = ?;\";\n";
        ccampos += "PreparedStatement ps = con.statametObject(consulta, id);\n";
        ccampos += "ResultSet rs=ps.executeQuery();\n";
        ccampos += "JSONObject obj = new JSONObject();\n";
        ccampos += "if(rs.next()){\n";
        for (int i = 0; i < lista.size(); i++) {

            if (lista.get(i).getTipo().equals("boolean")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getBoolean(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("integer")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getInt(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().contains("character")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getString(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().contains("numeric")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getDouble(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("date")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getString(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().contains("timestamp")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\",rs.getString(\"" + lista.get(i).getNombre() + "\"));\n";
            }
        }
        ccampos += "}\n";
        ccampos += "rs.close();\n";
        ccampos += "ps.close();\n";
        ccampos += "return obj;\n";
        ccampos += "}";
        return ccampos;
    }

    private static String CBuscarObject(String nombre_clase, String tablespace, List<CamposTabla> lista) throws SQLException {
        String ccampos = "public " + nombre_clase + " buscar(int id)  throws SQLException, JSONException{\n";
        ccampos += "String consulta = \"SELECT *\\n\"\n";
        ccampos += "+ \"    FROM " + tablespace + ".\\\"" + nombre_clase + "\\\"\\n\"\n";
        ccampos += "+ \"    WHERE \\\"id\\\" = ?;\";\n";
        ccampos += "PreparedStatement ps = con.statametObject(consulta, id);\n";
        ccampos += "ResultSet rs=ps.executeQuery();\n";
        ccampos += nombre_clase + " obj = null;\n";
        ccampos += "if(rs.next()){\n";
        ccampos += "obj = new " + nombre_clase + "(con);\n";
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getTipo().equals("integer")) {
                ccampos += "obj.set" + lista.get(i).getNombreUpperCase() + "(rs.getInt(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("boolean")) {
                ccampos += "obj.set" + lista.get(i).getNombreUpperCase() + "(rs.getBoolean(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().contains("character")) {
                ccampos += "obj.set" + lista.get(i).getNombreUpperCase() + "(rs.getString(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().contains("numeric")) {
                ccampos += "obj.set" + lista.get(i).getNombreUpperCase() + "(rs.getDouble(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().equals("date")) {
                ccampos += "obj.set" + lista.get(i).getNombreUpperCase() + "(rs.getDate(\"" + lista.get(i).getNombre() + "\"));\n";
            }
            if (lista.get(i).getTipo().contains("timestamp")) {
                ccampos += "obj.set" + lista.get(i).getNombreUpperCase() + "(rs.getTimestamp(\"" + lista.get(i).getNombre() + "\"));\n";
            }
        }
        ccampos += "}\n";
        ccampos += "rs.close();\n";
        ccampos += "ps.close();\n";
        ccampos += "return obj;\n";
        ccampos += "}";
        return ccampos;
    }

    private static String CToJSON(String nombre_clase, String tablespace, List<CamposTabla> lista) throws SQLException {
        String ccampos = "public JSONObject toJSONObject()  throws JSONException{\n";
        ccampos += "SimpleDateFormat f = new SimpleDateFormat(\"dd/MM/yyyy\");\n";
        ccampos += "SimpleDateFormat f1 = new SimpleDateFormat(\"dd/MM/yyyy HH:mm:ss\");\n";
        ccampos += "JSONObject obj = new JSONObject();\n";
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getTipo().equals("date")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\"," + lista.get(i).getNombre() + " == null ? \"\" : f.format(" + lista.get(i).getNombre() + "));\n";
            } else if (lista.get(i).getTipo().contains("timestamp")) {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\"," + lista.get(i).getNombre() + " == null ? \"\" : f1.format(" + lista.get(i).getNombre() + "));\n";
            } else {
                ccampos += "obj.put(\"" + lista.get(i).getNombre() + "\"," + lista.get(i).getNombre() + ");\n";
            }
        }
        ccampos += "return obj;\n";
        ccampos += "}";
        return ccampos;
    }

    private static String Cimport() {
        return "package  modelo;\n\n"
                + "import conexion.Conexion;\n"
                + "import java.sql.PreparedStatement;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.sql.SQLException;\n"
                + "import java.text.SimpleDateFormat;\n"
                + "import java.util.Date;\n"
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
            if (lista.get(i).getTipo().equals("integer")) {
                ccampos += "int ";
            }
            if (lista.get(i).getTipo().equals("boolean")) {
                ccampos += "boolean ";
            }
            if (lista.get(i).getTipo().contains("character")) {
                ccampos += "String ";
            }
            if (lista.get(i).getTipo().contains("numeric")) {
                ccampos += "Double ";
            }
            if (lista.get(i).getTipo().equals("date")) {
                ccampos += "Date ";
            }
            if (lista.get(i).getTipo().contains("timestamp")) {
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

    private static String CConstructorLlenoConexion(String nombre_clase, List<CamposTabla> lista) throws SQLException {

        String ccampos = "public " + nombre_clase + "( ";

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getTipo().equals("integer")) {
                ccampos += "int ";
            }
            if (lista.get(i).getTipo().equals("boolean")) {
                ccampos += "boolean ";
            }
            if (lista.get(i).getTipo().contains("character")) {
                ccampos += "String ";
            }
            if (lista.get(i).getTipo().contains("numeric")) {
                ccampos += "Double ";
            }
            if (lista.get(i).getTipo().equals("date")) {
                ccampos += "Date ";
            }
            if (lista.get(i).getTipo().contains("timestamp")) {
                ccampos += "Date ";
            }
            ccampos += lista.get(i).getNombre();

            if (i != lista.size() - 1) {
                ccampos += ", ";
            }

        }
        ccampos += ", Conexion con)\n{\n";
        for (int i = 0; i < lista.size(); i++) {
            ccampos += "this." + lista.get(i).getNombre() + " = " + lista.get(i).getNombre() + ";\n";
        }
        ccampos += "this.con=con;\n";
        ccampos += "}\n";
        return ccampos;
    }

    private static String cinisializar(String nombre_clase, List<CamposTabla> lista) throws SQLException {
        String ccampos = "";
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getTipo().equals("integer")) {
                ccampos += "\tprivate int ";
            }
            if (lista.get(i).getTipo().equals("boolean")) {
                ccampos += "\tprivate boolean ";
            }
            if (lista.get(i).getTipo().contains("character")) {
                ccampos += "\tprivate String ";
            }
            if (lista.get(i).getTipo().contains("numeric")) {
                ccampos += "\tprivate Double ";
            }
            if (lista.get(i).getTipo().equals("date")) {
                ccampos += "\tprivate Date ";
            }
            if (lista.get(i).getTipo().contains("timestamp")) {
                ccampos += "\tprivate Date ";
            }

            ccampos += lista.get(i).getNombre() + ";\n";
        }
        ccampos += "\tprivate Conexion con = null;\n";
        return ccampos;
    }

    /*public static void main(String args[]) throws SQLException {
     String tablespace="MOVIL";        
     Conexion con = new Conexion(tablespace, "oracle123");
     List<String> tablas = DataBaseOracle.tablas(con,tablespace);

     for (int i = 0; i < tablas.size(); i++) {
     EscribirOracle.escribirClases(tablas.get(i),tablespace,con);
     }
     con.Close();
     }*/
}
