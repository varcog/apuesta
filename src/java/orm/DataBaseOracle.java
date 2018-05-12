package orm;

import conexion.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseOracle {

    public static List<String> tablas(Conexion con, String tablespace) throws SQLException {
        List<String> lista = new ArrayList<>();
        String consulta = "SELECT * FROM \n"
                + "(SELECT DBA_TABLES.TABLE_NAME FROM SYS.DBA_TABLES WHERE (DBA_TABLES.OWNER = '" + tablespace + "')\n"
                + "UNION \n"
                + "select DBA_VIEWS.VIEW_NAME from SYS.DBA_VIEWS WHERE (DBA_VIEWS.OWNER = '" + tablespace + "')) TABLAS  ORDER BY 1";
        ResultSet rs = con.ejecutarConsulta(consulta);
        while (rs.next()) {
            lista.add(rs.getString("TABLE_NAME"));
        }
        return lista;
    }

    public static ResultSet Campostabla(String nombreTabla, Conexion con, String tablespace) {
        try {
//            String consulta = "SELECT DBA_TABLES.TABLE_NAME, USER_TAB_COLS.COLUMN_NAME, USER_TAB_COLS.DATA_TYPE FROM SYS.DBA_TABLES, SYS.USER_TAB_COLS WHERE (USER_TAB_COLS.TABLE_NAME = DBA_TABLES.TABLE_NAME) AND (DBA_TABLES.TABLE_NAME ='"+nombreTabla+"') AND (DBA_TABLES.OWNER = 'BROKER')";
            String consulta = "SELECT DISTINCT TABLE_NAME,COLUMN_NAME,DATA_TYPE FROM\n"
                    + "(\n"
                    + "SELECT DBA_TABLES.TABLE_NAME, DBA_TAB_COLS.COLUMN_NAME, DBA_TAB_COLS.DATA_TYPE FROM SYS.DBA_TABLES, SYS.DBA_TAB_COLS WHERE (DBA_TAB_COLS.TABLE_NAME = DBA_TABLES.TABLE_NAME)   AND (DBA_TABLES.OWNER = '" + tablespace + "')\n"
                    + "UNION ALL\n"
                    + "SELECT  DBA_VIEWS.VIEW_NAME , DBA_TAB_COLUMNS.COLUMN_NAME, DBA_TAB_COLUMNS.DATA_TYPE   FROM SYS.DBA_VIEWS, SYS.DBA_TAB_COLUMNS  WHERE  (DBA_TAB_COLUMNS.OWNER = DBA_VIEWS.OWNER)  AND (DBA_TAB_COLUMNS.TABLE_NAME = DBA_VIEWS.VIEW_NAME)   AND (DBA_VIEWS.OWNER = '" + tablespace + "')\n"
                    + ") TABLAS\n"
                    + "WHERE (TABLAS.TABLE_NAME = '" + nombreTabla + "')";
            ResultSet rs = con.ejecutarConsulta(consulta);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
