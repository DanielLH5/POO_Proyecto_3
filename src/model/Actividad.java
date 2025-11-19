package model;

import java.util.ArrayList;
import java.util.List;

/*
Representa una actividad planificada realizada por una brigada.
Contiene información sobre la planificación, ejecución y resultados de una acción específica de ayuda comunitaria.
*/
public class Actividad {
    private String id;
    private String descripcion;
    private String fecha; // Formato: "dd/MM/yyyy"
    private String lugar;
    private String objetivo;
    private List<Beneficiario> beneficiarios;
    private List<Voluntario> voluntariosAsignados;
    private List<Recurso> recursosAsignados;
    private String resultado; // Descripción textual de los resultados
    private int personasBeneficiadas;

    public Actividad() {
        this.beneficiarios = new ArrayList<>();
        this.voluntariosAsignados = new ArrayList<>();
        this.recursosAsignados = new ArrayList<>();
    }

    public Actividad(String id, String descripcion, String fecha, String lugar, String objetivo) {
        this();
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.lugar = lugar;
        this.objetivo = objetivo;
    }

    /*
    Agrega un beneficiario a la actividad
    */
    public void agregarBeneficiario(Beneficiario beneficiario) {
        if (!beneficiarios.contains(beneficiario)) {
            beneficiarios.add(beneficiario);
        }
    }

    /*
    Asigna un voluntario a la actividad
    */
    public void asignarVoluntario(Voluntario voluntario) {
        if (!voluntariosAsignados.contains(voluntario)) {
            voluntariosAsignados.add(voluntario);
        }
    }

    /*
    Asigna un recurso a la actividad
    */
    public void asignarRecurso(Recurso recurso) {
        if (!recursosAsignados.contains(recurso)) {
            recursosAsignados.add(recurso);
        }
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public Actividad setId(String id) {
        this.id = id;
        return this;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Actividad setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public String getFecha() {
        return fecha;
    }

    public Actividad setFecha(String fecha) {
        this.fecha = fecha;
        return this;
    }

    public String getLugar() {
        return lugar;
    }

    public Actividad setLugar(String lugar) {
        this.lugar = lugar;
        return this;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public Actividad setObjetivo(String objetivo) {
        this.objetivo = objetivo;
        return this;
    }

    public List<Beneficiario> getBeneficiarios() {
        return beneficiarios;
    }

    public Actividad setBeneficiarios(List<Beneficiario> beneficiarios) {
        this.beneficiarios = beneficiarios;
        return this;
    }

    public List<Voluntario> getVoluntariosAsignados() {
        return voluntariosAsignados;
    }

    public Actividad setVoluntariosAsignados(List<Voluntario> voluntariosAsignados) {
        this.voluntariosAsignados = voluntariosAsignados;
        return this;
    }

    public List<Recurso> getRecursosAsignados() {
        return recursosAsignados;
    }

    public Actividad setRecursosAsignados(List<Recurso> recursosAsignados) {
        this.recursosAsignados = recursosAsignados;
        return this;
    }

    public String getResultado() {
        return resultado;
    }

    public Actividad setResultado(String resultado) {
        this.resultado = resultado;
        return this;
    }

    public int getPersonasBeneficiadas() {
        return personasBeneficiadas;
    }

    public Actividad setPersonasBeneficiadas(int personasBeneficiadas) {
        this.personasBeneficiadas = personasBeneficiadas;
        return this;
    }

    @Override
    public String toString() {
        return "Actividad{" + "id=" + id + ", descripcion=" + descripcion + ", fecha=" + fecha + ", lugar=" + lugar +
                ", beneficiarios=" + beneficiarios.size() + '}';
    }
}