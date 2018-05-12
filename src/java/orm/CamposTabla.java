package orm;

public class CamposTabla {

    String nombre;
    String tipo;

    public CamposTabla() {
    }

    public CamposTabla(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreUpperCase() {
        return (nombre.charAt(0) + "").toUpperCase() + nombre.substring(1);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
