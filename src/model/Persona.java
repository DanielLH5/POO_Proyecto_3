package model;

/*
Clase abstracta que representa a una persona en el sistema de brigadas.
Sirve como base para Voluntario y Coordinador, encapsulando los atributos comunes de identificación y contacto.
*/
public abstract class Persona {
    protected String id;
    protected String nombre;
    protected String telefono;
    protected String email;

    // Constructores
    public Persona() {}

    public Persona(String id, String nombre, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    /*
    Metodo abstracto que debe ser implementado por las subclases para identificar el tipo de persona en el sistema.
    */
    public abstract String getTipo();

    // Getters y setters
    public String getId() {
        return id;
    }

    public Persona setId(String id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Persona setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getTelefono() {
        return telefono;
    }

    public Persona setTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Persona setEmail(String email) {
        this.email = email;
        return this;
    }
}