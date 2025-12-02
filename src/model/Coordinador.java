package model;

import java.io.Serializable;

public class Coordinador extends Usuario implements Serializable {
    private String areaResponsabilidad;

    public Coordinador() {
        super();
        this.areaResponsabilidad = "";
    }

    public Coordinador(String id, String nombre, String telefono, String email,
                       String password, String areaResponsabilidad) {
        super(id, nombre, telefono, email, password, "Coordinador");
        this.areaResponsabilidad = areaResponsabilidad;
    }

    public Coordinador(String nombre, String telefono, String email,
                       String password, String areaResponsabilidad) {
        super(nombre, telefono, email, password, "Coordinador");
        this.areaResponsabilidad = areaResponsabilidad;
    }

    public String getAreaResponsabilidad() { return areaResponsabilidad; }
    public void setAreaResponsabilidad(String areaResponsabilidad) {
        this.areaResponsabilidad = areaResponsabilidad;
    }
}