package model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa los resultados registrados de una actividad (RF-07).
 */
public class ResultadoActividad {
    private String id;
    private String actividadId;
    private String nombreActividad;
    private Date fechaActividad;
    private Date fechaRegistro;
    private String coordinadorId;
    private String coordinadorNombre;
    private int personasBeneficiadas;
    private double horasTrabajadas;
    private List<String> materialesUtilizados;
    private String resultadosAlcanzados;
    private String observaciones;
    private List<String> voluntariosParticipantes;
    private List<String> evidencias; // URLs o nombres de archivos de fotos/documentos

    public ResultadoActividad(String id, String actividadId, String nombreActividad,
                              Date fechaActividad, String coordinadorId, String coordinadorNombre) {
        this.id = id;
        this.actividadId = actividadId;
        this.nombreActividad = nombreActividad;
        this.fechaActividad = fechaActividad;
        this.fechaRegistro = new Date();
        this.coordinadorId = coordinadorId;
        this.coordinadorNombre = coordinadorNombre;
        this.personasBeneficiadas = 0;
        this.horasTrabajadas = 0.0;
        this.materialesUtilizados = new ArrayList<>();
        this.resultadosAlcanzados = "";
        this.observaciones = "";
        this.voluntariosParticipantes = new ArrayList<>();
        this.evidencias = new ArrayList<>();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getActividadId() { return actividadId; }
    public void setActividadId(String actividadId) { this.actividadId = actividadId; }

    public String getNombreActividad() { return nombreActividad; }
    public void setNombreActividad(String nombreActividad) { this.nombreActividad = nombreActividad; }

    public Date getFechaActividad() { return fechaActividad; }
    public void setFechaActividad(Date fechaActividad) { this.fechaActividad = fechaActividad; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getCoordinadorId() { return coordinadorId; }
    public void setCoordinadorId(String coordinadorId) { this.coordinadorId = coordinadorId; }

    public String getCoordinadorNombre() { return coordinadorNombre; }
    public void setCoordinadorNombre(String coordinadorNombre) { this.coordinadorNombre = coordinadorNombre; }

    public int getPersonasBeneficiadas() { return personasBeneficiadas; }
    public void setPersonasBeneficiadas(int personasBeneficiadas) { this.personasBeneficiadas = personasBeneficiadas; }

    public double getHorasTrabajadas() { return horasTrabajadas; }
    public void setHorasTrabajadas(double horasTrabajadas) { this.horasTrabajadas = horasTrabajadas; }

    public List<String> getMaterialesUtilizados() { return materialesUtilizados; }
    public void setMaterialesUtilizados(List<String> materialesUtilizados) { this.materialesUtilizados = materialesUtilizados; }
    public void addMaterialUtilizado(String material) { this.materialesUtilizados.add(material); }

    public String getResultadosAlcanzados() { return resultadosAlcanzados; }
    public void setResultadosAlcanzados(String resultadosAlcanzados) { this.resultadosAlcanzados = resultadosAlcanzados; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<String> getVoluntariosParticipantes() { return voluntariosParticipantes; }
    public void setVoluntariosParticipantes(List<String> voluntariosParticipantes) { this.voluntariosParticipantes = voluntariosParticipantes; }
    public void addVoluntarioParticipante(String voluntarioId) { this.voluntariosParticipantes.add(voluntarioId); }

    public List<String> getEvidencias() { return evidencias; }
    public void setEvidencias(List<String> evidencias) { this.evidencias = evidencias; }
    public void addEvidencia(String evidencia) { this.evidencias.add(evidencia); }

    /**
     * Calcula el impacto total basado en beneficiarios y horas trabajadas.
     */
    public double calcularImpacto() {
        return personasBeneficiadas * horasTrabajadas;
    }

    /**
     * Genera un resumen de los resultados.
     */
    public String generarResumen() {
        return String.format(
                "Resultados de '%s':\n" +
                        "- Personas beneficiadas: %d\n" +
                        "- Horas trabajadas: %.1f\n" +
                        "- Materiales utilizados: %d\n" +
                        "- Voluntarios participantes: %d\n" +
                        "- Impacto total: %.1f",
                nombreActividad,
                personasBeneficiadas,
                horasTrabajadas,
                materialesUtilizados.size(),
                voluntariosParticipantes.size(),
                calcularImpacto()
        );
    }

    /**
     * Genera un reporte detallado.
     */
    public String generarReporteDetallado() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=".repeat(50)).append("\n");
        reporte.append("REPORTE DE RESULTADOS - RF-07\n");
        reporte.append("=".repeat(50)).append("\n\n");

        reporte.append("ACTIVIDAD: ").append(nombreActividad).append("\n");
        reporte.append("ID Actividad: ").append(actividadId).append("\n");
        reporte.append("Fecha de la actividad: ").append(fechaActividad).append("\n");
        reporte.append("Fecha de registro: ").append(fechaRegistro).append("\n");
        reporte.append("Coordinador: ").append(coordinadorNombre).append(" (ID: ").append(coordinadorId).append(")\n\n");

        reporte.append("MÉTRICAS:\n");
        reporte.append("• Personas beneficiadas: ").append(personasBeneficiadas).append("\n");
        reporte.append("• Horas trabajadas: ").append(horasTrabajadas).append("\n");
        reporte.append("• Impacto total: ").append(String.format("%.1f", calcularImpacto())).append("\n\n");

        reporte.append("RESULTADOS ALCANZADOS:\n");
        reporte.append(resultadosAlcanzados).append("\n\n");

        if (!materialesUtilizados.isEmpty()) {
            reporte.append("MATERIALES UTILIZADOS (").append(materialesUtilizados.size()).append("):\n");
            for (String material : materialesUtilizados) {
                reporte.append("• ").append(material).append("\n");
            }
            reporte.append("\n");
        }

        if (!voluntariosParticipantes.isEmpty()) {
            reporte.append("VOLUNTARIOS PARTICIPANTES (").append(voluntariosParticipantes.size()).append(")\n");
            // Aquí se podrían agregar los nombres de los voluntarios
            reporte.append("\n");
        }

        if (!observaciones.isEmpty()) {
            reporte.append("OBSERVACIONES:\n");
            reporte.append(observaciones).append("\n");
        }

        if (!evidencias.isEmpty()) {
            reporte.append("EVIDENCIAS (").append(evidencias.size()).append(" archivos adjuntos)\n");
        }

        return reporte.toString();
    }

    @Override
    public String toString() {
        return String.format("%s - %s (Beneficiarios: %d, Horas: %.1f)",
                nombreActividad,
                new java.text.SimpleDateFormat("dd/MM/yyyy").format(fechaActividad),
                personasBeneficiadas,
                horasTrabajadas
        );
    }
}