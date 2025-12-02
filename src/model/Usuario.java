package model;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private String id;
    private String nombre;
    private String telefono;
    private String email;
    private String password;
    private String rol;

    public Usuario() {}

    public Usuario(String id, String nombre, String telefono, String email, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(String nombre, String telefono, String email, String password, String rol) {
        this.id = generarId();
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    private String generarId() {
        return "USR" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean login(String passIngresada) {
        return this.password.equals(passIngresada);
    }

    public void actualizarPerfil(String nuevoNombre, String nuevoEmail) {
        this.nombre = nuevoNombre;
        this.email = nuevoEmail;
    }

    @Override
    public String toString() {
        return nombre + " (" + email + ")";
    }
}