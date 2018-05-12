package orm;

import conexion.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class DataBasePostgres {

    public static List<String> tablas(Conexion con, String tablespace) throws SQLException {
        List<String> lista = new ArrayList<>();
        String consulta = "SELECT tablename FROM pg_tables WHERE schemaname = 'public'";
        ResultSet rs = con.ejecutarConsulta(consulta);
        while (rs.next()) {
            lista.add(rs.getString("tablename"));
        }
        return lista;
    }

    public static ResultSet Campostabla(String nombreTabla, Conexion con, String tablespace) {
        try {
//            String consulta = "SELECT DBA_TABLES.TABLE_NAME, USER_TAB_COLS.COLUMN_NAME, USER_TAB_COLS.DATA_TYPE FROM SYS.DBA_TABLES, SYS.USER_TAB_COLS WHERE (USER_TAB_COLS.TABLE_NAME = DBA_TABLES.TABLE_NAME) AND (DBA_TABLES.TABLE_NAME ='"+nombreTabla+"') AND (DBA_TABLES.OWNER = 'BROKER')";
// TABLE_NAME,COLUMN_NAME,DATA_TYPE
            String consulta = "SELECT DISTINCT \n"
                    + "    a.attnum as no,\n"
                    + "    a.attname as nombre_columna,\n"
                    + "    format_type(a.atttypid, a.atttypmod) as tipo,\n"
                    + "    a.attnotnull as notnull, \n"
                    + "    com.description as descripcion,\n"
                    + "    coalesce(i.indisprimary,false) as llave_primaria,\n"
                    + "    def.adsrc as default\n"
                    + "FROM pg_attribute a \n"
                    + "JOIN pg_class pgc ON pgc.oid = a.attrelid\n"
                    + "LEFT JOIN pg_index i ON \n"
                    + "    (pgc.oid = i.indrelid AND i.indkey[0] = a.attnum)\n"
                    + "LEFT JOIN pg_description com on \n"
                    + "    (pgc.oid = com.objoid AND a.attnum = com.objsubid)\n"
                    + "LEFT JOIN pg_attrdef def ON \n"
                    + "    (a.attrelid = def.adrelid AND a.attnum = def.adnum)\n"
                    + "WHERE a.attnum > 0 AND pgc.oid = a.attrelid\n"
                    + "AND pg_table_is_visible(pgc.oid)\n"
                    + "AND NOT a.attisdropped\n"
                    + " AND pgc.relname = '" + nombreTabla + "'  -- Nombre de la tabla\n"
                    + "ORDER BY a.attnum;";
            ResultSet rs = con.ejecutarConsulta(consulta);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws SQLException, JSONException {
        Conexion con = new Conexion();
        List<String> tablas = tablas(con, "");
        for (int i = 0; i < tablas.size(); i++) {
            System.out.println("************************************");
            String get = tablas.get(i);
            System.out.println(get);
            ResultSet rs = Campostabla(get, con, "");
            while (rs.next()) {
                System.out.println("\t" + rs.getString("nombre_columna") + "\t" + rs.getString("tipo"));
            }
        }
    }

}
