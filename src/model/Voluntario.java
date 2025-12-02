package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Voluntario extends Usuario implements Serializable {
    private List<String> habilidades;
    private boolean disponible;
    private List<String> diasDisponibles; // Cambiado a List<String>
    private int horasAcumuladas;

    // Días de la semana constantes para validación y consistencia
    public static final List<String> DIAS_SEMANA = Arrays.asList(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    );

    public Voluntario() {
        super();
        this.habilidades = new ArrayList<>();
        this.disponible = true;
        this.diasDisponibles = new ArrayList<>();
        this.horasAcumuladas = 0;
    }

    public Voluntario(String id, String nombre, String telefono, String email,
                      String password, List<String> habilidades, List<String> diasDisponibles) {
        super(id, nombre, telefono, email, password, "Voluntario");
        this.habilidades = habilidades != null ? habilidades : new ArrayList<>();
        this.diasDisponibles = diasDisponibles != null ? diasDisponibles : new ArrayList<>();
        this.disponible = true;
        this.horasAcumuladas = 0;
    }

    // Constructor que acepta texto para compatibilidad
    public Voluntario(String nombre, String telefono, String email,
                      String password, String habilidadesTexto, String diasTexto) {
        super(nombre, telefono, email, password, "Voluntario");

        // Procesar habilidades
        this.habilidades = new ArrayList<>();
        if (habilidadesTexto != null && !habilidadesTexto.isEmpty()) {
            String[] habilidadesArray = habilidadesTexto.split(",");
            for (String habilidad : habilidadesArray) {
                this.habilidades.add(habilidad.trim());
            }
        }

        // Procesar días (compatibilidad con versión anterior)
        this.diasDisponibles = new ArrayList<>();
        if (diasTexto != null && !diasTexto.isEmpty()) {
            // Si viene en formato texto separado por comas
            String[] diasArray = diasTexto.split(",");
            for (String dia : diasArray) {
                String diaTrim = dia.trim();
                if (DIAS_SEMANA.contains(diaTrim)) {
                    this.diasDisponibles.add(diaTrim);
                }
            }
        }

        this.disponible = true;
        this.horasAcumuladas = 0;
    }

    // Getters y Setters
    public List<String> getHabilidades() { return habilidades; }
    public void setHabilidades(List<String> habilidades) { this.habilidades = habilidades; }

    public String getHabilidadesTexto() {
        return String.join(", ", habilidades);
    }

    public void setHabilidadesTexto(String habilidadesTexto) {
        this.habilidades = new ArrayList<>();
        if (habilidadesTexto != null && !habilidadesTexto.isEmpty()) {
            String[] habilidadesArray = habilidadesTexto.split(",");
            for (String habilidad : habilidadesArray) {
                this.habilidades.add(habilidad.trim());
            }
        }
    }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    // Nuevos métodos para días como lista
    public List<String> getDiasDisponibles() { return diasDisponibles; }
    public void setDiasDisponibles(List<String> diasDisponibles) {
        this.diasDisponibles = diasDisponibles != null ? diasDisponibles : new ArrayList<>();
    }

    // Para compatibilidad con código existente que espera un String
    public String getDiasDisponiblesTexto() {
        return String.join(", ", diasDisponibles);
    }

    // Para compatibilidad con código existente que envía un String
    public void setDiasDisponiblesTexto(String diasTexto) {
        this.diasDisponibles = new ArrayList<>();
        if (diasTexto != null && !diasTexto.isEmpty()) {
            String[] diasArray = diasTexto.split(",");
            for (String dia : diasArray) {
                String diaTrim = dia.trim();
                if (DIAS_SEMANA.contains(diaTrim)) {
                    this.diasDisponibles.add(diaTrim);
                }
            }
        }
    }

    // Métodos para manipular días individualmente
    public void agregarDia(String dia) {
        if (dia != null && !diasDisponibles.contains(dia) && DIAS_SEMANA.contains(dia)) {
            diasDisponibles.add(dia);
        }
    }

    public void eliminarDia(String dia) {
        diasDisponibles.remove(dia);
    }

    public boolean tieneDia(String dia) {
        return diasDisponibles.contains(dia);
    }

    // Métodos para configuraciones rápidas
    public void setDiasLaborables() {
        diasDisponibles.clear();
        diasDisponibles.addAll(Arrays.asList("Lunes", "Martes", "Miércoles", "Jueves", "Viernes"));
    }

    public void setFinesDeSemana() {
        diasDisponibles.clear();
        diasDisponibles.addAll(Arrays.asList("Sábado", "Domingo"));
    }

    public void setTodaLaSemana() {
        diasDisponibles.clear();
        diasDisponibles.addAll(DIAS_SEMANA);
    }

    public void limpiarDias() {
        diasDisponibles.clear();
    }

    public int getHorasAcumuladas() { return horasAcumuladas; }
    public void setHorasAcumuladas(int horasAcumuladas) { this.horasAcumuladas = horasAcumuladas; }

    public void agregarHoras(int horas) { this.horasAcumuladas += horas; }

    public void inscribirseAActividad(Actividad actividad) {
        System.out.println(this.getNombre() + " se ha inscrito en la actividad: " + actividad.getNombre());
    }

    public void consultarHistorial() {
        System.out.println("Consultando historial de actividades de " + this.getNombre());
    }
}