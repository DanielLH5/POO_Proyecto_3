package model;

/*
Representa a una persona, familia o comunidad que recibe ayuda de las brigadas.
Almacena información sobre las necesidades específicas y ubicación para facilitar la planificación y seguimiento de la ayuda brindada.
*/
public class Beneficiario {
    private String id;
    private String nombre;
    private String direccion;
    private String necesidades; // Descripción de necesidades: "alimentos, medicinas, ropa"

    public Beneficiario() {}

    public Beneficiario(String id, String nombre, String direccion, String necesidades) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.necesidades = necesidades;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public Beneficiario setId(String id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Beneficiario setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getDireccion() {
        return direccion;
    }

    public Beneficiario setDireccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public String getNecesidades() {
        return necesidades;
    }

    public Beneficiario setNecesidades(String necesidades) {
        this.necesidades = necesidades;
        return this;
    }

    @Override
    public String toString() {
        return "Beneficiario{" + "id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + ", necesidades=" + necesidades + '}';
    }
}