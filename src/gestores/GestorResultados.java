package gestores;

import model.ResultadoActividad;
import model.Actividad;
import model.Coordinador;
import exceptions.PersistenciaException;
import persistence.GestorAlmacenamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Gestor para manejar los resultados de actividades (RF-07).
 */
public class GestorResultados {
    private static final String ARCHIVO_RESULTADOS = "resultados.dat";
    private List<ResultadoActividad> resultados;
    private GestorAlmacenamiento gestorAlmacenamiento;
    private GestorActividades gestorActividades;

    public GestorResultados(GestorActividades gestorActividades) {
        this.resultados = new ArrayList<>();
        this.gestorAlmacenamiento = new GestorAlmacenamiento();
        this.gestorActividades = gestorActividades;
        cargarResultados();
    }

    /**
     * Carga los resultados desde persistencia.
     */
    @SuppressWarnings("unchecked")
    private void cargarResultados() {
        try {
            Object datos = gestorAlmacenamiento.cargar(ARCHIVO_RESULTADOS);
            if (datos != null) {
                resultados = (List<ResultadoActividad>) datos;
                System.out.println("[INFO] Cargados " + resultados.size() + " resultados desde persistencia.");
            } else {
                resultados = new ArrayList<>();
                System.out.println("[INFO] No se encontraron resultados guardados, iniciando lista vacía.");
            }
        } catch (PersistenciaException e) {
            System.err.println("Error cargando resultados: " + e.getMessage());
            resultados = new ArrayList<>();
        }
    }

    /**
     * Guarda los resultados en persistencia.
     */
    private void guardarResultados() throws PersistenciaException {
        gestorAlmacenamiento.guardar(ARCHIVO_RESULTADOS, resultados);
    }

    /**
     * Registra resultados para una actividad (RF-07).
     */
    public ResultadoActividad registrarResultados(
            String actividadId,
            String coordinadorId,
            String coordinadorNombre,
            int personasBeneficiadas,
            double horasTrabajadas,
            List<String> materialesUtilizados,
            String resultadosAlcanzados,
            String observaciones,
            List<String> voluntariosParticipantes,
            List<String> evidencias) throws Exception {

        // Validaciones
        if (actividadId == null || actividadId.trim().isEmpty()) {
            throw new Exception("El ID de la actividad no puede estar vacío.");
        }

        if (resultadosAlcanzados == null || resultadosAlcanzados.trim().isEmpty()) {
            throw new Exception("Los resultados alcanzados no pueden estar vacíos.");
        }

        // Verificar que la actividad existe
        Actividad actividad = gestorActividades.buscarActividadPorId(actividadId);
        if (actividad == null) {
            throw new Exception("Actividad no encontrada: " + actividadId);
        }

        // Verificar que no exista ya un registro para esta actividad
        if (existeResultadoParaActividad(actividadId)) {
            throw new Exception("Ya existe un registro de resultados para esta actividad.");
        }

        // Crear nuevo resultado
        String resultadoId = generarIdResultado();
        ResultadoActividad nuevoResultado = new ResultadoActividad(
                resultadoId,
                actividadId,
                actividad.getNombre(),
                actividad.getFecha(),
                coordinadorId,
                coordinadorNombre
        );

        // Configurar datos
        nuevoResultado.setPersonasBeneficiadas(personasBeneficiadas);
        nuevoResultado.setHorasTrabajadas(horasTrabajadas);
        nuevoResultado.setMaterialesUtilizados(materialesUtilizados);
        nuevoResultado.setResultadosAlcanzados(resultadosAlcanzados);
        nuevoResultado.setObservaciones(observaciones);
        nuevoResultado.setVoluntariosParticipantes(voluntariosParticipantes);
        nuevoResultado.setEvidencias(evidencias);

        try {
            // Registrar también en la actividad (para compatibilidad)
            gestorActividades.registrarResultados(
                    actividadId,
                    personasBeneficiadas,
                    horasTrabajadas,
                    resultadosAlcanzados,
                    observaciones
            );

            // Guardar resultado específico
            resultados.add(nuevoResultado);
            guardarResultados();

            System.out.println("[INFO] Resultados registrados exitosamente para la actividad '" +
                    actividad.getNombre() + "' (RF-07).");

            return nuevoResultado;

        } catch (Exception e) {
            resultados.remove(nuevoResultado);
            throw new Exception("Error al registrar resultados: " + e.getMessage());
        }
    }

    /**
     * Obtiene resultados por actividad.
     */
    public ResultadoActividad obtenerResultadoPorActividad(String actividadId) {
        return resultados.stream()
                .filter(r -> r.getActividadId().equals(actividadId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene resultados por coordinador.
     */
    public List<ResultadoActividad> obtenerResultadosPorCoordinador(String coordinadorId) {
        return resultados.stream()
                .filter(r -> r.getCoordinadorId().equals(coordinadorId))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene resultados por rango de fechas.
     */
    public List<ResultadoActividad> obtenerResultadosPorRangoFechas(Date fechaInicio, Date fechaFin) {
        return resultados.stream()
                .filter(r -> !r.getFechaActividad().before(fechaInicio) &&
                        !r.getFechaActividad().after(fechaFin))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los resultados (para historial - RF-08).
     */
    public List<ResultadoActividad> obtenerTodosResultados() {
        return new ArrayList<>(resultados);
    }

    /**
     * Obtiene resultados ordenados por fecha (más recientes primero).
     */
    public List<ResultadoActividad> obtenerHistorialOrdenado() {
        return resultados.stream()
                .sorted((r1, r2) -> r2.getFechaActividad().compareTo(r1.getFechaActividad()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene estadísticas de resultados.
     */
    public String obtenerEstadisticasResultados() {
        int totalResultados = resultados.size();
        int totalBeneficiados = resultados.stream().mapToInt(ResultadoActividad::getPersonasBeneficiadas).sum();
        double totalHoras = resultados.stream().mapToDouble(ResultadoActividad::getHorasTrabajadas).sum();
        double impactoTotal = resultados.stream().mapToDouble(ResultadoActividad::calcularImpacto).sum();

        return String.format("""
            ========== ESTADÍSTICAS DE RESULTADOS ==========
            Total de actividades con resultados: %d
            Personas beneficiadas totales: %d
            Horas trabajadas totales: %.1f
            Impacto total calculado: %.1f
            Promedio de beneficiarios por actividad: %.1f
            Promedio de horas por actividad: %.1f
            ================================================
            """,
                totalResultados,
                totalBeneficiados,
                totalHoras,
                impactoTotal,
                totalResultados > 0 ? (double) totalBeneficiados / totalResultados : 0,
                totalResultados > 0 ? totalHoras / totalResultados : 0
        );
    }

    /**
     * Genera reporte de impacto por mes.
     */
    public List<String[]> generarReporteImpactoMensual() {
        List<String[]> reporte = new ArrayList<>();

        // Agrupar por mes-año
        java.util.Map<String, Integer> beneficiadosPorMes = new java.util.HashMap<>();
        java.util.Map<String, Double> horasPorMes = new java.util.HashMap<>();

        for (ResultadoActividad resultado : resultados) {
            String mesKey = new java.text.SimpleDateFormat("yyyy-MM").format(resultado.getFechaActividad());

            beneficiadosPorMes.put(mesKey,
                    beneficiadosPorMes.getOrDefault(mesKey, 0) + resultado.getPersonasBeneficiadas());

            horasPorMes.put(mesKey,
                    horasPorMes.getOrDefault(mesKey, 0.0) + resultado.getHorasTrabajadas());
        }

        // Convertir a lista
        for (String mesKey : beneficiadosPorMes.keySet()) {
            int beneficiados = beneficiadosPorMes.get(mesKey);
            double horas = horasPorMes.get(mesKey);
            double impacto = beneficiados * horas;

            String mesFormateado = formatearMes(mesKey);

            reporte.add(new String[]{
                    mesFormateado,
                    String.valueOf(beneficiados),
                    String.format("%.1f", horas),
                    String.format("%.1f", impacto)
            });
        }

        // Ordenar por mes (más reciente primero)
        reporte.sort((a, b) -> b[0].compareTo(a[0]));

        return reporte;
    }

    /**
     * Verifica si existe resultado para una actividad.
     */
    private boolean existeResultadoParaActividad(String actividadId) {
        return resultados.stream()
                .anyMatch(r -> r.getActividadId().equals(actividadId));
    }

    /**
     * Genera un ID único para resultado.
     */
    private String generarIdResultado() {
        return String.format("RES-%03d", resultados.size() + 1);
    }

    /**
     * Formatea la clave de mes para mostrar.
     */
    private String formatearMes(String mesKey) {
        try {
            String[] parts = mesKey.split("-");
            int año = Integer.parseInt(parts[0]);
            int mes = Integer.parseInt(parts[1]);

            String[] nombresMeses = {
                    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            };

            return nombresMeses[mes - 1] + " " + año;
        } catch (Exception e) {
            return mesKey;
        }
    }

    /**
     * Obtiene el top de actividades con mayor impacto.
     */
    public List<ResultadoActividad> obtenerTopActividadesImpacto(int cantidad) {
        return resultados.stream()
                .sorted((r1, r2) -> Double.compare(r2.calcularImpacto(), r1.calcularImpacto()))
                .limit(cantidad)
                .collect(Collectors.toList());
    }

    /**
     * Busca resultados por término.
     */
    public List<ResultadoActividad> buscarResultados(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return new ArrayList<>(resultados);
        }

        String terminoLower = termino.toLowerCase();
        return resultados.stream()
                .filter(r -> r.getNombreActividad().toLowerCase().contains(terminoLower) ||
                        r.getResultadosAlcanzados().toLowerCase().contains(terminoLower) ||
                        r.getObservaciones().toLowerCase().contains(terminoLower))
                .collect(Collectors.toList());
    }

    /**
     * Exporta resultados a formato CSV.
     */
    public String exportarResultadosCSV() {
        StringBuilder csv = new StringBuilder();

        // Encabezados
        csv.append("ID Resultado,ID Actividad,Nombre Actividad,Fecha Actividad,Coordinador,")
                .append("Personas Beneficiadas,Horas Trabajadas,Impacto Total,Resultados\n");

        // Datos
        for (ResultadoActividad resultado : resultados) {
            csv.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,%.1f,%.1f,\"%s\"\n",
                    resultado.getId(),
                    resultado.getActividadId(),
                    resultado.getNombreActividad(),
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(resultado.getFechaActividad()),
                    resultado.getCoordinadorNombre(),
                    resultado.getPersonasBeneficiadas(),
                    resultado.getHorasTrabajadas(),
                    resultado.calcularImpacto(),
                    resultado.getResultadosAlcanzados().replace("\"", "\"\"") // Escapar comillas
            ));
        }

        return csv.toString();
    }
}