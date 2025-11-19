package model;

/*
Representa a un coordinador responsable de organizar y supervisar las brigadas.
Extiende de Persona y agrega información sobre el área geográfica o temática de su responsabilidad dentro del sistema.
*/
public class Coordinador extends Persona {
    private String areaResponsabilidad;

    public Coordinador() {
        super();
    }

    public Coordinador(String id, String nombre, String telefono, String email, String areaResponsabilidad) {
        super(id, nombre, telefono, email);
        this.areaResponsabilidad = areaResponsabilidad;
    }

    // Getters y setters específicos de Coordinador
    public String getAreaResponsabilidad() {
        return areaResponsabilidad;
    }

    public Coordinador setAreaResponsabilidad(String areaResponsabilidad) {
        this.areaResponsabilidad = areaResponsabilidad;
        return this;
    }

    @Override
    public String getTipo() {
        return "COORDINADOR";
    }

    @Override
    public String toString() {
        return "Coordinador{" + "id=" + id + ", nombre=" + nombre + ", area=" + areaResponsabilidad + '}';
    }
}