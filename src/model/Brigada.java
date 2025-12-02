package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Brigada implements Serializable {
    private String id;
    private String nombre;
    private String tipo;
    private String zona;           // Cambié de "ubicacion" a "zona" para coincidir con el dashboard
    private String descripcion;    // Nuevo campo para el dashboard
    private String estado;         // Nuevo campo para manejar estados (Activa, Inactiva, etc.)
    private Coordinador coordinador;
    private List<Voluntario> voluntarios;
    private int capacidadMaxima;   // Para controlar límite de voluntarios

    public Brigada() {
        this.voluntarios = new ArrayList<>();
        this.estado = "Activa";    // Estado por defecto
        this.capacidadMaxima = 50; // Capacidad por defecto
    }

    // Constructor completo para usar en el dashboard
    public Brigada(String id, String nombre, String tipo, String zona, String descripcion, Coordinador coordinador) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.zona = zona;
        this.descripcion = descripcion;
        this.coordinador = coordinador;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getZona() { return zona; }  // Cambiado de getUbicacion
    public void setZona(String zona) { this.zona = zona; }  // Cambiado de setUbicacion

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Coordinador getCoordinador() { return coordinador; }
    public void setCoordinador(Coordinador coordinador) { this.coordinador = coordinador; }

    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public int getCantidadVoluntarios() { return voluntarios.size(); }

    public boolean tieneCupoDisponible() {
        return voluntarios.size() < capacidadMaxima;
    }

    // Método para agregar voluntario con validación de capacidad
    public void agregarVoluntario(Voluntario voluntario) throws IllegalStateException {
        if (!voluntarios.contains(voluntario)) {
            if (tieneCupoDisponible()) {
                voluntarios.add(voluntario);
            } else {
                throw new IllegalStateException("La brigada ha alcanzado su capacidad máxima de " + capacidadMaxima + " voluntarios.");
            }
        }
    }

    // Método para remover voluntario
    public boolean removerVoluntario(String voluntarioId) {
        return voluntarios.removeIf(v -> v.getId().equals(voluntarioId));
    }

    // Método mejorado para convocar urgentes
    public void convocarUrgente(String mensaje) {
        System.out.println("\n--- ALERTA URGENTE - Brigada " + this.nombre + " (ID: " + this.id + ") ---");
        System.out.println("Mensaje: " + mensaje);
        System.out.println("Estado: " + this.estado);
        System.out.println("Zona: " + this.zona);
        System.out.println("Coordinador: " + (coordinador != null ? coordinador.getNombre() : "No asignado"));

        if (voluntarios.isEmpty()) {
            System.out.println("No hay voluntarios asignados a esta brigada.");
        } else {
            System.out.println("Contactando " + voluntarios.size() + " voluntario(s):");
            for (Voluntario v : voluntarios) {
                System.out.println("  • " + v.getNombre() +
                        " | Tel: " + v.getTelefono() +
                        " | Email: " + v.getEmail());
                // Aquí se podría agregar lógica real de envío de notificaciones
            }
        }
        System.out.println("Convocatoria completada.");
    }

    // Método para obtener información resumida
    public String getResumen() {
        return String.format("%s (%s) - Zona: %s - Voluntarios: %d/%d - Estado: %s",
                nombre, tipo, zona, voluntarios.size(), capacidadMaxima, estado);
    }

    @Override
    public String toString() {
        return getResumen();
    }

    // Método para generar datos para tablas en el dashboard
    public Object[] toTableRow() {
        return new Object[]{
                id,
                nombre,
                tipo,
                zona,
                estado,
                voluntarios.size() + "/" + capacidadMaxima,
                coordinador != null ? coordinador.getNombre() : "Sin asignar",
                descripcion
        };
    }

    // Método para validar si la brigada está activa
    public boolean isActiva() {
        return "Activa".equalsIgnoreCase(estado);
    }

    // Método para cambiar estado
    public void activar() {
        this.estado = "Activa";
    }

    public void desactivar() {
        this.estado = "Inactiva";
    }

    // Método para obtener porcentaje de ocupación
    public int getPorcentajeOcupacion() {
        if (capacidadMaxima == 0) return 0;
        return (voluntarios.size() * 100) / capacidadMaxima;
    }

    // En la clase Brigada, asegúrate de tener estos métodos:
    public void eliminarVoluntario(Voluntario voluntario) {
        this.voluntarios.remove(voluntario);
    }

    public List<Voluntario> getVoluntarios() {
        return new ArrayList<>(voluntarios); // Retorna copia para proteger la lista interna
    }

    // Y en los setters, asegúrate de que hagan copias defensivas:
    public void setVoluntarios(List<Voluntario> voluntarios) {
        if (voluntarios != null) {
            this.voluntarios = new ArrayList<>(voluntarios);
        } else {
            this.voluntarios = new ArrayList<>();
        }
    }
}