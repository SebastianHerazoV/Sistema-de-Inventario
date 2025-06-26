public class Proveedor {
    private String nombre;
    private String infoContacto;

    public Proveedor(String nombre, String infoContacto) {
        this.nombre = nombre;
        this.infoContacto = infoContacto;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getinfoContacto() { return infoContacto; }
    public void setDescripcion(String infoContacto) { this.infoContacto = infoContacto; }

    @Override
    public String toString() {
        return nombre + " - " + infoContacto;
    }
}
