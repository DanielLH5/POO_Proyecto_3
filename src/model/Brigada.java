package model;

import java.util.ArrayList;
import java.util.List;

/*
Representa una brigada comunitaria, que es un grupo organizado de voluntarios bajo la dirección de un coordinador, enfocado en un tipo específico de ayuda.
Gestiona las relaciones entre coordinador, voluntarios y actividades.
*/
public class Brigada {
    private String id;
    private String nombre;
    private String tipo; // "alimentaria", "vivienda", "salud", "educación"
    private Coordinador coordinador;
    private List<Voluntario> voluntarios;
    private List<Actividad> actividades;

    public Brigada() {
        this.voluntarios = new ArrayList<>();
        this.actividades = new ArrayList<>();
    }

    public Brigada(String id, String nombre, String tipo, Coordinador coordinador) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.coordinador = coordinador;
    }

    /*
    Agrega un voluntario a la brigada
    */
    public void agregarVoluntario(Voluntario voluntario) {
        if (!voluntarios.contains(voluntario)) {
            voluntarios.add(voluntario);
        }
    }

    /*
    Agrega una actividad a la brigada
    */
    public void agregarActividad(Actividad actividad) {
        if (!actividades.contains(actividad)) {
            actividades.add(actividad);
        }
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public Brigada setId(String id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Brigada setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getTipo() {
        return tipo;
    }

    public Brigada setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public Coordinador getCoordinador() {
        return coordinador;
    }

    public Brigada setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
        return this;
    }

    public List<Voluntario> getVoluntarios() {
        return voluntarios;
    }

    public Brigada setVoluntarios(List<Voluntario> voluntarios) {
        this.voluntarios = voluntarios;
        return this;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public Brigada setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
        return this;
    }

    @Override
    public String toString() {
        return "Brigada{" + "id=" + id + ", nombre=" + nombre + ", tipo=" + tipo +
                ", voluntarios=" + voluntarios.size() + ", actividades=" + actividades.size() + '}';
    }
}