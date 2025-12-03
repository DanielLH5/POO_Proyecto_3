package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
    public Map<String, String> getVoluntariosAsignados() {
        return voluntariosAsignados;
    }
    public Map<String, Integer> getRecursosAsignados() { return recursosAsignados; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public void setBrigadaAsociada(Brigada brigadaAsociada) {
        this.brigadaAsociada = brigadaAsociada;
    }
    public void setResultados(String resultados) { this.resultados = resultados; }

    // SETTER PARA voluntariosAsignados - AÑADIDO
    public void setVoluntariosAsignados(Map<String, String> voluntariosAsignados) {
        if (voluntariosAsignados == null) {
            this.voluntariosAsignados = new HashMap<>();
        } else {
            this.voluntariosAsignados = new HashMap<>(voluntariosAsignados);
        }
    }

    // SETTER PARA recursosAsignados - AÑADIDO
    public void setRecursosAsignados(Map<String, Integer> recursosAsignados) {
        if (recursosAsignados == null) {
            this.recursosAsignados = new HashMap<>();
        } else {
            this.recursosAsignados = new HashMap<>(recursosAsignados);
        }
    }

    // Métodos para manejar voluntarios
    public void asignarVoluntario(String idVoluntario, String rol) {
        if (this.voluntariosAsignados == null) {
            this.voluntariosAsignados = new HashMap<>();
        }
        this.voluntariosAsignados.put(idVoluntario, rol);
    }

    public void eliminarVoluntario(String idVoluntario) {
        if (this.voluntariosAsignados != null) {
            this.voluntariosAsignados.remove(idVoluntario);
        }
    }

    // Métodos para manejar recursos
    public void registrarUsoRecurso(String idRecurso, int cantidad) {
        if (this.recursosAsignados == null) {
            this.recursosAsignados = new HashMap<>();
        }
        this.recursosAsignados.put(idRecurso, cantidad);
    }

    public void eliminarRecurso(String idRecurso) {
        if (this.recursosAsignados != null) {
            this.recursosAsignados.remove(idRecurso);
        }
    }

    // Métodos de consulta
    public boolean tieneVoluntarioAsignado(String idVoluntario) {
        return this.voluntariosAsignados != null &&
                this.voluntariosAsignados.containsKey(idVoluntario);
    }

    public String getRolVoluntario(String idVoluntario) {
        if (this.voluntariosAsignados != null) {
            return this.voluntariosAsignados.get(idVoluntario);
        }
        return null;
    }

    public Map<String, String> getAsignacionesVoluntarios() {
        if (this.voluntariosAsignados == null) {
            return new HashMap<>();
        }
        return new HashMap<>(voluntariosAsignados); // Retorna copia para evitar modificación externa
    }

    public int getCantidadRecursoAsignado(String recursoId) {
        if (recursosAsignados != null && recursosAsignados.containsKey(recursoId)) {
            return recursosAsignados.get(recursoId);
        }
        return 0;
    }

    // Método para obtener lista de IDs de voluntarios
    public List<String> getVoluntariosIds() {
        if (this.voluntariosAsignados == null || this.voluntariosAsignados.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(this.voluntariosAsignados.keySet());
    }

    // Método para obtener cantidad de voluntarios
    public int getCantidadVoluntarios() {
        if (this.voluntariosAsignados == null) {
            return 0;
        }
        return this.voluntariosAsignados.size();
    }

    // Método para verificar si la actividad tiene voluntarios
    public boolean tieneVoluntarios() {
        return this.voluntariosAsignados != null &&
                !this.voluntariosAsignados.isEmpty();
    }

    // Método para limpiar todos los voluntarios
    public void limpiarVoluntarios() {
        if (this.voluntariosAsignados != null) {
            this.voluntariosAsignados.clear();
        }
    }

    // Método para limpiar todos los recursos
    public void limpiarRecursos() {
        if (this.recursosAsignados != null) {
            this.recursosAsignados.clear();
        }
    }

    // Método para obtener el rol por defecto
    public String getRolPorDefecto() {
        return "Participante";
    }

    @Override
    public String toString() {
        return nombre + " (" + lugar + ", " + fecha + ")";
    }

    // Método para clonar asignaciones de voluntarios (útil para copias)
    public Map<String, String> clonarVoluntariosAsignados() {
        if (this.voluntariosAsignados == null) {
            return new HashMap<>();
        }
        return new HashMap<>(this.voluntariosAsignados);
    }

    // Método para clonar recursos asignados
    public Map<String, Integer> clonarRecursosAsignados() {
        if (this.recursosAsignados == null) {
            return new HashMap<>();
        }
        return new HashMap<>(this.recursosAsignados);
    }
}