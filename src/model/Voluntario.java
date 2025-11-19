package model;

import java.util.ArrayList;
import java.util.List;

/*
Representa a un voluntario que participa en las brigadas comunitarias.
Extiende de Persona y agrega información específica sobre habilidades, disponibilidad y días en los que puede colaborar.
*/
public class Voluntario extends Persona {
    private List<String> habilidades;
    private boolean disponible;
    private String diasDisponibles; // Formato: "Lunes,Martes,Viernes"

    public Voluntario() {
        super();
        this.habilidades = new ArrayList<>();
        this.disponible = true;
    }

    public Voluntario(String id, String nombre, String telefono, String email) {
        super(id, nombre, telefono, email);
        this.habilidades = new ArrayList<>();
        this.disponible = true;
    }

    @Override
    public String getTipo() {
        return "VOLUNTARIO";
    }

    // Getters y setters específicos de Voluntario
    public List<String> getHabilidades() {
        return habilidades;
    }

    public Voluntario setHabilidades(List<String> habilidades) {
        this.habilidades = habilidades;
        return this;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public Voluntario setDisponible(boolean disponible) {
        this.disponible = disponible;
        return this;
    }

    public String getDiasDisponibles() {
        return diasDisponibles;
    }

    public Voluntario setDiasDisponibles(String diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
        return this;
    }

    @Override
    public String toString() {
        return "Voluntario{" + "id=" + id + ", nombre=" + nombre + ", disponible=" + disponible + '}';
    }
}