package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

public class SisEventos {

    public SisEventos() {
    }

    public static RoundingMode tipo_redondeo = RoundingMode.HALF_EVEN;

    public static String decodeUTF8(String text) throws UnsupportedEncodingException {
        return new String(text.getBytes("ISO-8859-15"), "UTF-8");
    }

    public boolean guardarImagenEnElSistemaDeFicheros(InputStream input, String fileName) {
        FileOutputStream output = null;
        boolean ok = false;
        try {
            output = new FileOutputStream(fileName);
            int leido = 0;
            leido = input.read();
            while (leido != -1) {
                output.write(leido);
                leido = input.read();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SisEventos.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(SisEventos.class.getName()).log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                output.flush();
                output.close();
                input.close();
                ok = true;
            } catch (IOException ex) {
                Logger.getLogger(SisEventos.class.getName()).log(Level.SEVERE, "Error cerrando flujo de salida", ex);
            }
        }
        return ok;
    }

    public String getRealPath(ServletContext servletContext, String resourcePath) {
        String result = servletContext.getRealPath(resourcePath);
        if (result == null) {
//            resources en un.war(JBoss, WebLogic)
            java.net.URL url;
            try {
                url = servletContext.getResource(resourcePath);
                result = url.getPath();
            } catch (MalformedURLException ex) {
                Logger.getLogger(SisEventos.class.getName()).log(Level.SEVERE, "No se ha podido recuperar el path real de: " + resourcePath, ex);
            }
        }
        return result;
    }

    public boolean eliminarImagenEnElSistemaDeFicheros(String archivo) {
        try {
            File file = new File(archivo);
            boolean estatus = file.delete();
            if (!estatus) {
                String msj = "Error no se ha podido eliminar el  archivo " + archivo;
                Logger.getLogger(SisEventos.class.getName()).log(Level.SEVERE, msj);
                return false;
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(SisEventos.class.getName()).log(Level.SEVERE, "Error no se ha podido eliminar el  archivo " + archivo, ex);
            return false;
        }
    }

    public double arreglarErrorSuma2D(double monto) throws ParseException {
        RoundingMode aux = tipo_redondeo;
        tipo_redondeo = RoundingMode.HALF_EVEN;
        double res = acomodarDosDecimalesD(monto);
        tipo_redondeo = aux;
        return res;
    }

    public static double acomodarDosDecimalesD(double monto) throws ParseException  {
        return acomodarDecimalesD(monto, 2);
    }

    public static double acomodarDecimalesD(double monto, int cantidad_decimales) throws ParseException  {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat();
        formatter.setDecimalFormatSymbols(dfs);
        formatter.setGroupingUsed(false);
        formatter.setMaximumFractionDigits(cantidad_decimales);
        formatter.setMinimumFractionDigits(cantidad_decimales);
        formatter.setRoundingMode(tipo_redondeo);
        String res = formatter.format(monto);
        return formatter.parse(res).doubleValue();
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        String nombre = "ñoño hóla";
        System.out.println(nombre);
        System.out.println(SisEventos.decodeUTF8("ñoño hóla"));
        System.out.println(SisEventos.decodeUTF8(SisEventos.decodeUTF8(SisEventos.decodeUTF8("ñoño hóla"))));
        System.out.println(SisEventos.decodeUTF8(SisEventos.decodeUTF8(SisEventos.decodeUTF8(SisEventos.decodeUTF8("ñoño hóla")))));
    }
}
