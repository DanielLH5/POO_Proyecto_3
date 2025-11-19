package gestores;

import model.Actividad;
import model.Brigada;
import model.Voluntario;
import model.Recurso;
import java.util.List;

/*
Gestor especializado en la generación de reportes y estadísticas del sistema.
Proporciona métodos para obtener información consolidada sobre brigadas, actividades, voluntarios y recursos para análisis y toma de decisiones.
*/
public class GestorReportes {
    private GestorBrigadas gestorBrigadas;
    private GestorVoluntarios gestorVoluntarios;
    private GestorActividades gestorActividades;
    private GestorRecursos gestorRecursos;

    /*
    Establece las referencias a los gestores necesarios para generar reportes
    */
    public void setGestores(GestorBrigadas gestorBrigadas, GestorVoluntarios gestorVoluntarios,
                            GestorActividades gestorActividades, GestorRecursos gestorRecursos) {
        this.gestorBrigadas = gestorBrigadas;
        this.gestorVoluntarios = gestorVoluntarios;
        this.gestorActividades = gestorActividades;
        this.gestorRecursos = gestorRecursos;
    }

    /*
    Genera un reporte general del sistema con estadísticas clave
    @return texto con el reporte general
    */
    public String generarReporteGeneral() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE GENERAL DEL SISTEMA ===\n\n");

        // Estadísticas de brigadas
        if (gestorBrigadas != null) {
            reporte.append("BRIGADAS:\n");
            reporte.append("- Total registradas: ").append(gestorBrigadas.getTotalBrigadas()).append("\n");

            List<Brigada> brigadas = gestorBrigadas.obtenerBrigadas();
            for (Brigada brigada : brigadas) {
                reporte.append("  * ").append(brigada.getNombre())
                        .append(" (").append(brigada.getTipo()).append(") - ")
                        .append(brigada.getVoluntarios().size()).append(" voluntarios\n");
            }
            reporte.append("\n");
        }

        // Estadísticas de voluntarios
        if (gestorVoluntarios != null) {
            reporte.append("VOLUNTARIOS:\n");
            reporte.append("- Total registrados: ").append(gestorVoluntarios.getTotalVoluntarios()).append("\n");
            reporte.append("- Disponibles: ").append(gestorVoluntarios.getTotalVoluntariosDisponibles()).append("\n\n");
        }

        // Estadísticas de actividades
        if (gestorActividades != null) {
            reporte.append("ACTIVIDADES:\n");
            reporte.append("- Total planificadas: ").append(gestorActividades.getTotalActividades()).append("\n");

            List<Actividad> actividadesConResultado = gestorActividades.obtenerHistorialActividades();
            reporte.append("- Con resultados registrados: ").append(actividadesConResultado.size()).append("\n\n");
        }

        // Estadísticas de recursos
        if (gestorRecursos != null) {
            reporte.append("RECURSOS:\n");
            reporte.append("- Total en inventario: ").append(gestorRecursos.getTotalRecursos()).append("\n");
            reporte.append("- Disponibles: ").append(gestorRecursos.getTotalRecursosDisponibles()).append("\n");
        }

        return reporte.toString();
    }

    /*
    Genera un reporte detallado de las actividades realizadas
    @return texto con el reporte de actividades
    */
    public String generarReporteActividades() {
        if (gestorActividades == null) return "Gestor de actividades no disponible";

        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE ACTIVIDADES ===\n\n");

        List<Actividad> actividades = gestorActividades.obtenerActividades();

        if (actividades.isEmpty()) {
            reporte.append("No hay actividades planificadas.\n");
        } else {
            for (Actividad actividad : actividades) {
                reporte.append("ACTIVIDAD: ").append(actividad.getDescripcion()).append("\n");
                reporte.append("Fecha: ").append(actividad.getFecha()).append(" | Lugar: ").append(actividad.getLugar()).append("\n");
                reporte.append("Objetivo: ").append(actividad.getObjetivo()).append("\n");
                reporte.append("Voluntarios asignados: ").append(actividad.getVoluntariosAsignados().size()).append("\n");
                reporte.append("Beneficiarios: ").append(actividad.getBeneficiarios().size()).append("\n");

                if (actividad.getResultado() != null && !actividad.getResultado().isEmpty()) {
                    reporte.append("RESULTADO: ").append(actividad.getResultado()).append("\n");
                    reporte.append("Personas beneficiadas: ").append(actividad.getPersonasBeneficiadas()).append("\n");
                } else {
                    reporte.append("Estado: Planificada\n");
                }
                reporte.append("---\n");
            }
        }

        return reporte.toString();
    }

    /*
    Genera un reporte del inventario de recursos
    @return texto con el reporte de recursos
    */
    public String generarReporteRecursos() {
        if (gestorRecursos == null) return "Gestor de recursos no disponible";

        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE INVENTARIO ===\n\n");

        List<Recurso> recursos = gestorRecursos.obtenerRecursos();

        if (recursos.isEmpty()) {
            reporte.append("No hay recursos en el inventario.\n");
        } else {
            reporte.append(String.format("%-5s %-20s %-15s %-10s %-10s\n",
                    "ID", "NOMBRE", "TIPO", "CANTIDAD", "DISPONIBLE"));
            reporte.append("----------------------------------------------------------------\n");

            for (Recurso recurso : recursos) {
                String disponible = recurso.estaDisponible() ? "SÍ" : "NO";
                reporte.append(String.format("%-5s %-20s %-15s %-10d %-10s\n",
                        recurso.getId(), recurso.getNombre(), recurso.getTipo(),
                        recurso.getCantidadDisponible(), disponible));
            }
        }

        return reporte.toString();
    }

    /*
    Genera un reporte de efectividad basado en actividades realizadas
    @return texto con el reporte de efectividad
    */
    public String generarReporteEfectividad() {
        if (gestorActividades == null) return "Gestor de actividades no disponible";

        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE EFECTIVIDAD ===\n\n");

        List<Actividad> actividadesConResultado = gestorActividades.obtenerHistorialActividades();

        if (actividadesConResultado.isEmpty()) {
            reporte.append("No hay actividades con resultados registrados.\n");
        } else {
            int totalBeneficiados = 0;

            for (Actividad actividad : actividadesConResultado) {
                totalBeneficiados += actividad.getPersonasBeneficiadas();
                reporte.append("- ").append(actividad.getDescripcion())
                        .append(": ").append(actividad.getPersonasBeneficiadas())
                        .append(" personas beneficiadas\n");
            }

            reporte.append("\nTOTAL GENERAL: ").append(totalBeneficiados).append(" personas beneficiadas\n");
            reporte.append("PROMEDIO: ").append(totalBeneficiados / actividadesConResultado.size())
                    .append(" personas por actividad\n");
        }

        return reporte.toString();
    }
}