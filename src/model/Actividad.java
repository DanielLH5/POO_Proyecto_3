package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Actividad implements Serializable {
    private String id;
    private String nombre;
    private Date fecha;
    private String lugar;
    private String objetivo;
    private Brigada brigadaAsociada;
    private String resultados;
    private Map<String, String> voluntariosAsignados;
    private Map<String, Integer> recursosAsignados;

    public Actividad() {
        this.voluntariosAsignados = new HashMap<>();
        this.recursosAsignados = new HashMap<>();
    }

    public Actividad(String id, String nombre, Date fecha, String lugar,
                     String objetivo, Brigada brigadaAsociada) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.objetivo = objetivo;
        this.brigadaAsociada = brigadaAsociada;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Date getFecha() { return fecha; }
    public String getLugar() { return lugar; }
    public String getObjetivo() { return objetivo; }
    public Brigada getBrigadaAsociada() { return brigadaAsociada; }
    public String getResultados() { return resultados; }
    public Map<String, String> getVoluntariosAsignados() { return voluntariosAsignados; }
    public Map<String, Integer> getRecursosAsignados() { return recursosAsignados; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public void setBrigadaAsociada(Brigada brigadaAsociada) { this.brigadaAsociada = brigadaAsociada; }
    public void setResultados(String resultados) { this.resultados = resultados; }

    public void asignarVoluntario(String idVoluntario, String rol) {
        this.voluntariosAsignados.put(idVoluntario, rol);
    }

    public void eliminarVoluntario(String idVoluntario) {
        this.voluntariosAsignados.remove(idVoluntario);
    }

    public void registrarUsoRecurso(String idRecurso, int cantidad) {
        this.recursosAsignados.put(idRecurso, cantidad);
    }

    public void eliminarRecurso(String idRecurso) {
        this.recursosAsignados.remove(idRecurso);
    }

    public boolean tieneVoluntarioAsignado(String idVoluntario) {
        return this.voluntariosAsignados.containsKey(idVoluntario);
    }

    public String getRolVoluntario(String idVoluntario) {
        return this.voluntariosAsignados.get(idVoluntario);
    }

    public Map<String, String> getAsignacionesVoluntarios() {
        return new HashMap<>(voluntariosAsignados); // Retorna copia para evitar modificación externa
    }

    public int getCantidadVoluntarios() {
        return this.voluntariosAsignados.size();
    }

    @Override
    public String toString() {
        return nombre + " (" + lugar + ", " + fecha + ")";
    }
}