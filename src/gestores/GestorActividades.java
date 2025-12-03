package gestores;

import model.*;
import exceptions.BrigadaException;
import exceptions.VoluntarioNoEncontradoException;
import exceptions.PersistenciaException;
import persistence.GestorAlmacenamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestor responsable de la planificación de actividades (RF-04, RF-05, RF-07, RF-08).
 * Incluye persistencia completa de datos.
 */
public class GestorActividades {
    private static final String ARCHIVO_ACTIVIDADES = "actividades.dat";
    private List<Actividad> actividades;
    // Dependencias
    private GestorBrigadas gestorBrigadas;
    private GestorVoluntarios gestorVoluntarios;
    private GestorAlmacenamiento gestorAlmacenamiento;
    private GestorRecursos gestorRecursos;

    // Mapa para roles de voluntarios en actividades
    private Map<String, Map<String, String>> asignacionesVoluntarios; // actividadId -> (voluntarioId -> rol)

    public GestorActividades(GestorBrigadas gestorBrigadas, GestorVoluntarios gestorVoluntarios, GestorRecursos gestorRecursos) {
        this.actividades = new ArrayList<>();
        this.gestorBrigadas = gestorBrigadas;
        this.gestorVoluntarios = gestorVoluntarios;
        this.gestorAlmacenamiento = new GestorAlmacenamiento();
        this.gestorRecursos = gestorRecursos;
        this.asignacionesVoluntarios = new HashMap<>();
        cargarActividades();
    }

    /**
     * Carga las actividades desde el archivo de persistencia.
     */
    @SuppressWarnings("unchecked")
    private void cargarActividades() {
        try {
            Object datos = gestorAlmacenamiento.cargar(ARCHIVO_ACTIVIDADES);
            if (datos != null) {
                actividades = (List<Actividad>) datos;
                System.out.println("[INFO] Cargadas " + actividades.size() + " actividades desde persistencia.");

                // Inicializar asignaciones de voluntarios
                inicializarAsignacionesVoluntarios();
            } else {
                actividades = new ArrayList<>();
                System.out.println("[INFO] No se encontraron actividades guardadas, iniciando lista vacía.");
            }
        } catch (PersistenciaException e) {
            System.err.println("Error cargando actividades: " + e.getMessage());
            actividades = new ArrayList<>();
        }
    }

    /**
     * Inicializa las asignaciones de voluntarios desde las actividades cargadas.
     */
    private void inicializarAsignacionesVoluntarios() {
        for (Actividad actividad : actividades) {
            Map<String, String> asignacion = actividad.getAsignacionesVoluntarios();
            if (asignacion != null && !asignacion.isEmpty()) {
                asignacionesVoluntarios.put(actividad.getId(), new HashMap<>(asignacion));
            }
        }
    }

    /**
     * Obtiene los recursos de una actividad
     */
    public Map<String, Integer> obtenerRecursosDeActividad(String actividadId) {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            return new HashMap<>();
        }

        return new HashMap<>(actividad.getRecursosAsignados());
    }

    /**
     * Elimina un recurso de una actividad
     */
    public void eliminarRecursoDeActividad(String actividadId, String recursoId)
            throws BrigadaException {

        Actividad actividad = obtenerActividadPorId(actividadId);

        try {
            actividad.eliminarRecurso(recursoId);
            guardarActividades();
            System.out.println("[INFO] Recurso eliminado de la actividad '" + actividad.getNombre() + "'");

        } catch (Exception e) {
            throw new BrigadaException("Error al eliminar recurso: " + e.getMessage());
        }
    }

    /**
     * Guarda las actividades en el archivo de persistencia.
     */
    private void guardarActividades() throws PersistenciaException {
        gestorAlmacenamiento.guardar(ARCHIVO_ACTIVIDADES, actividades);
    }

    public String generarIdActividadGestor() {
        try {
            // Buscar el número más alto en los IDs existentes
            int maxNum = 0;

            for (Actividad actividad : actividades) {
                String id = actividad.getId();
                if (id != null && id.startsWith("ACT-")) {
                    try {
                        String numeroStr = id.substring(4); // Quitar "ACT-"
                        int num = Integer.parseInt(numeroStr);
                        if (num > maxNum) {
                            maxNum = num;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar IDs que no siguen el formato
                    }
                }
            }

            // Incrementar para el nuevo ID
            int nuevoNumero = maxNum + 1;

            // Formatear con 3 dígitos: 001, 002, etc.
            return String.format("ACT-%03d", nuevoNumero);

        } catch (Exception e) {
            // Fallback en caso de error
            return "ACT-001";
        }
    }

    /**
     * Obtiene todos los voluntarios asignados a una actividad específica
     * @param actividadId ID de la actividad
     * @return Lista de objetos Voluntario asignados a la actividad
     */
    public List<Voluntario> obtenerVoluntariosDeActividad(String actividadId) {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            return new ArrayList<>();
        }

        return obtenerVoluntariosDeActividad(actividad);
    }

    /**
     * Obtiene todos los voluntarios asignados a una actividad específica
     * @param actividad Objeto Actividad
     * @return Lista de objetos Voluntario asignados a la actividad
     */
    public List<Voluntario> obtenerVoluntariosDeActividad(Actividad actividad) {
        if (actividad == null) {
            return new ArrayList<>();
        }

        Map<String, String> asignaciones = actividad.getVoluntariosAsignados();
        List<Voluntario> voluntarios = new ArrayList<>();

        for (String voluntarioId : asignaciones.keySet()) {
            Voluntario voluntario = gestorVoluntarios.buscarVoluntarioPorId(voluntarioId);
            if (voluntario != null) {
                voluntarios.add(voluntario);
            }
        }

        return voluntarios;
    }

    public List<Recurso> obtenerRecursosDeActividad(Actividad actividad) {
        if (actividad == null) {
            return new ArrayList<>();
        }

        Map<String, Integer> asignaciones = actividad.getRecursosAsignados();
        List<Recurso> recursos = new ArrayList<>();

        for (String recursoId : asignaciones.keySet()) {
            Recurso recurso = gestorRecursos.buscarRecursoPorId(recursoId);
            if (recurso != null) {
                recursos.add(recurso);
            }
        }

        return recursos;
    }

    /**
     * Obtiene voluntarios de una actividad con su rol incluido
     * @param actividadId ID de la actividad
     * @return Mapa de Voluntario -> Rol en la actividad
     */
    public Map<Voluntario, String> obtenerVoluntariosConRolesDeActividad(String actividadId) {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            return new HashMap<>();
        }

        Map<String, String> asignaciones = actividad.getVoluntariosAsignados();
        Map<Voluntario, String> voluntariosConRoles = new HashMap<>();

        for (Map.Entry<String, String> entry : asignaciones.entrySet()) {
            Voluntario voluntario = gestorVoluntarios.buscarVoluntarioPorId(entry.getKey());
            if (voluntario != null) {
                voluntariosConRoles.put(voluntario, entry.getValue());
            }
        }

        return voluntariosConRoles;
    }

    /**
     * Obtiene información detallada de los voluntarios de una actividad
     * @param actividadId ID de la actividad
     * @return Lista de strings con información formateada de cada voluntario
     */
    public List<String> obtenerInfoVoluntariosDeActividad(String actividadId) {
        Map<Voluntario, String> voluntariosConRoles = obtenerVoluntariosConRolesDeActividad(actividadId);
        List<String> infoVoluntarios = new ArrayList<>();

        for (Map.Entry<Voluntario, String> entry : voluntariosConRoles.entrySet()) {
            Voluntario v = entry.getKey();
            String rol = entry.getValue();

            String info = String.format("%s %s (ID: %s) - Rol: %s | Email: %s | Tel: %s",
                    v.getNombre(),
                    v.getId(),
                    rol,
                    v.getEmail(),
                    v.getTelefono());

            infoVoluntarios.add(info);
        }

        return infoVoluntarios;
    }

    /**
     * Obtiene voluntarios de una actividad filtrados por rol
     * @param actividadId ID de la actividad
     * @param rol Rol a filtrar
     * @return Lista de voluntarios con el rol especificado
     */
    public List<Voluntario> obtenerVoluntariosDeActividadPorRol(String actividadId, String rol) {
        Map<Voluntario, String> voluntariosConRoles = obtenerVoluntariosConRolesDeActividad(actividadId);
        List<Voluntario> voluntariosFiltrados = new ArrayList<>();

        for (Map.Entry<Voluntario, String> entry : voluntariosConRoles.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(rol)) {
                voluntariosFiltrados.add(entry.getKey());
            }
        }

        return voluntariosFiltrados;
    }

    /**
     * Obtiene el número de voluntarios asignados a una actividad
     * @param actividadId ID de la actividad
     * @return Cantidad de voluntarios asignados
     */
    public int obtenerCantidadVoluntariosEnActividad(String actividadId) {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            return 0;
        }

        return actividad.getVoluntariosAsignados().size();
    }

    /**
     * Verifica si un voluntario está asignado a una actividad
     * @param actividadId ID de la actividad
     * @param voluntarioId ID del voluntario
     * @return true si el voluntario está asignado, false en caso contrario
     */
    public boolean estaVoluntarioEnActividad(String actividadId, String voluntarioId) {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            return false;
        }

        return actividad.getVoluntariosAsignados().containsKey(voluntarioId);
    }

    /**
     * Planifica y crea una nueva actividad (RF-04).
     */
    public Actividad planificarActividad(String id, String nombre, Date fecha, String lugar, String objetivo, String brigadaId)
            throws BrigadaException {

        // Validaciones
        if (id == null || id.trim().isEmpty()) {
            throw new BrigadaException("Error: El ID de la actividad no puede estar vacío.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BrigadaException("Error: El nombre de la actividad no puede estar vacío.");
        }

        if (fecha == null) {
            throw new BrigadaException("Error: La fecha de la actividad no puede ser nula.");
        }

        // Verificar si la actividad ya existe
        if (existeActividad(id)) {
            throw new BrigadaException("Error: La actividad con ID " + id + " ya existe.");
        }

        // Buscar brigada
        Brigada brigada = gestorBrigadas.buscarBrigadaPorId(brigadaId);
        if (brigada == null) {
            throw new BrigadaException("No se puede planificar la actividad. Brigada no encontrada: " + brigadaId);
        }

        // Crear la actividad
        Actividad nuevaActividad = new Actividad(id, nombre, fecha, lugar, objetivo, brigada);

        // Guardar estado anterior para poder revertir si falla
        try {
            actividades.add(nuevaActividad);
            guardarActividades();
            System.out.println("[INFO] Actividad '" + nombre + "' planificada con éxito (RF-04).");
            return nuevaActividad;
        } catch (PersistenciaException e) {
            // Revertir si falla el guardado
            actividades.remove(nuevaActividad);
            throw new BrigadaException("Error al guardar la actividad: " + e.getMessage());
        }
    }

    /**
     * Crea y guarda una nueva actividad directamente desde objeto Actividad.
     */
    public void crearActividad(Actividad actividad) throws BrigadaException {
        if (actividad == null) {
            throw new BrigadaException("Error: La actividad no puede ser nula.");
        }

        if (existeActividad(actividad.getId())) {
            throw new BrigadaException("Error: La actividad con ID " + actividad.getId() + " ya existe.");
        }

        try {
            actividades.add(actividad);
            guardarActividades();
            System.out.println("[INFO] Actividad '" + actividad.getNombre() + "' creada exitosamente.");
        } catch (PersistenciaException e) {
            actividades.remove(actividad);
            throw new BrigadaException("Error al crear la actividad: " + e.getMessage());
        }
    }

    /**
     * Asigna un voluntario a una actividad con un rol específico (RF-05).
     */
    public void asignarVoluntarioAActividad(String actividadId, String voluntarioId, String rolAsignado)
            throws VoluntarioNoEncontradoException, BrigadaException {

        Actividad actividad = obtenerActividadPorId(actividadId);
        Voluntario voluntario = gestorVoluntarios.buscarVoluntarioPorId(voluntarioId);

        if (actividad == null) throw new BrigadaException("Actividad no encontrada: " + actividadId);
        if (voluntario == null) throw new VoluntarioNoEncontradoException(voluntarioId);

        try {
            // Registrar la asignación en la actividad
            actividad.asignarVoluntario(voluntarioId, rolAsignado);

            // Actualizar mapa de asignaciones
            asignacionesVoluntarios.computeIfAbsent(actividadId, k -> new HashMap<>())
                    .put(voluntarioId, rolAsignado);

            guardarActividades();
            System.out.println("[INFO] Voluntario " + voluntario.getNombre() + " asignado a '" + actividad.getNombre() + "' como " + rolAsignado + " (RF-05).");
        } catch (PersistenciaException e) {
            // Revertir la asignación si falla el guardado
            actividad.eliminarVoluntario(voluntarioId);
            asignacionesVoluntarios.getOrDefault(actividadId, new HashMap<>()).remove(voluntarioId);
            throw new BrigadaException("Error al guardar la asignación: " + e.getMessage());
        }
    }

    /**
     * Asigna múltiples voluntarios a una actividad (NUEVO MÉTODO para RF-05).
     */
    public void asignarVoluntariosAActividad(String actividadId, List<String> voluntariosIds)
            throws BrigadaException {

        Actividad actividad = obtenerActividadPorId(actividadId);
        if (actividad == null) {
            throw new BrigadaException("Actividad no encontrada: " + actividadId);
        }

        try {
            for (String voluntarioId : voluntariosIds) {
                Voluntario voluntario = gestorVoluntarios.buscarVoluntarioPorId(voluntarioId);
                if (voluntario != null) {
                    // Asignar con rol "Participante" por defecto
                    actividad.asignarVoluntario(voluntarioId, "Participante");

                    // Actualizar mapa de asignaciones
                    asignacionesVoluntarios.computeIfAbsent(actividadId, k -> new HashMap<>())
                            .put(voluntarioId, "Participante");
                }
            }

            guardarActividades();
            System.out.println("[INFO] " + voluntariosIds.size() + " voluntarios asignados a la actividad '" + actividad.getNombre() + "'.");

        } catch (Exception e) {
            throw new BrigadaException("Error al asignar voluntarios: " + e.getMessage());
        }
    }

    /**
     * Obtiene voluntarios asignados a una actividad (NUEVO MÉTODO).
     */
    public List<Voluntario> obtenerVoluntariosAsignados(String actividadId) {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            return new ArrayList<>();
        }

        List<Voluntario> voluntarios = new ArrayList<>();
        Map<String, String> asignaciones = asignacionesVoluntarios.get(actividadId);

        if (asignaciones != null) {
            for (String voluntarioId : asignaciones.keySet()) {
                Voluntario voluntario = gestorVoluntarios.buscarVoluntarioPorId(voluntarioId);
                if (voluntario != null) {
                    voluntarios.add(voluntario);
                }
            }
        }

        return voluntarios;
    }

    /**
     * Registra los resultados de una actividad (RF-07).
     */
    public void registrarResultados(String actividadId, String resultados, Coordinador coordinador) throws BrigadaException {
        Actividad actividad = obtenerActividadPorId(actividadId);
        if (actividad == null) {
            throw new BrigadaException("Actividad no encontrada para registrar resultados: " + actividadId);
        }

        try {
            actividad.setResultados(resultados);
            guardarActividades();
            System.out.println("[INFO] Resultados registrados para la actividad '" + actividad.getNombre() + "' por " + coordinador.getNombre() + " (RF-07).");
        } catch (PersistenciaException e) {
            actividad.setResultados(null); // Revertir si falla
            throw new BrigadaException("Error al guardar resultados: " + e.getMessage());
        }
    }

    /**
     * Registra resultados completos de actividad (NUEVO MÉTODO para RF-07).
     */
    public void registrarResultados(String actividadId, int personasBeneficiadas, double horasTrabajadas, String resultados, String observaciones)
            throws BrigadaException {

        Actividad actividad = obtenerActividadPorId(actividadId);
        if (actividad == null) {
            throw new BrigadaException("Actividad no encontrada: " + actividadId);
        }

        try {
            // Crear string con todos los resultados
            String resultadosCompletos = String.format(
                    "Resultados de la actividad '%s':\n" +
                            "• Personas beneficiadas: %d\n" +
                            "• Horas trabajadas: %.1f\n" +
                            "• Resultados alcanzados: %s\n" +
                            "• Observaciones: %s",
                    actividad.getNombre(),
                    personasBeneficiadas,
                    horasTrabajadas,
                    resultados,
                    observaciones
            );

            actividad.setResultados(resultadosCompletos);
            guardarActividades();
            System.out.println("[INFO] Resultados completos registrados para la actividad '" + actividad.getNombre() + "'.");

        } catch (Exception e) {
            throw new BrigadaException("Error al registrar resultados: " + e.getMessage());
        }
    }

    /**
     * Consulta el historial de actividades (RF-08).
     */
    public List<Actividad> consultarHistorial() {
        System.out.println("\n--- Consultando Historial de Actividades (RF-08) ---");
        return new ArrayList<>(actividades);
    }

    /**
     * Busca una actividad por ID.
     */
    public Actividad buscarActividadPorId(String id) {
        return actividades.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene una actividad por ID o lanza excepción si no existe.
     */
    public Actividad obtenerActividadPorId(String id) throws BrigadaException {
        Actividad actividad = buscarActividadPorId(id);
        if (actividad == null) {
            throw new BrigadaException("Actividad no encontrada con ID: " + id);
        }
        return actividad;
    }

    /**
     * Elimina una actividad del sistema.
     */
    public void eliminarActividad(String id) throws BrigadaException {
        Actividad actividad = obtenerActividadPorId(id);

        // Verificar si la actividad tiene voluntarios asignados
        if (!actividad.getVoluntariosAsignados().isEmpty()) {
            throw new BrigadaException("No se puede eliminar la actividad '" + actividad.getNombre() +
                    "' porque tiene " + actividad.getVoluntariosAsignados().size() + " voluntarios asignados.");
        }

        try {
            actividades.remove(actividad);
            asignacionesVoluntarios.remove(id); // Eliminar asignaciones
            guardarActividades();
            System.out.println("[INFO] Actividad '" + actividad.getNombre() + "' eliminada exitosamente.");
        } catch (PersistenciaException e) {
            actividades.add(actividad); // Revertir si falla
            throw new BrigadaException("Error al eliminar la actividad: " + e.getMessage());
        }
    }

    /**
     * Elimina una actividad forzadamente (sin verificar voluntarios).
     */
    public void eliminarActividadForzadamente(String id) throws BrigadaException {
        Actividad actividad = obtenerActividadPorId(id);

        try {
            actividades.remove(actividad);
            asignacionesVoluntarios.remove(id); // Eliminar asignaciones
            guardarActividades();
            System.out.println("[WARNING] Actividad '" + actividad.getNombre() + "' eliminada forzadamente.");
        } catch (PersistenciaException e) {
            actividades.add(actividad); // Revertir si falla
            throw new BrigadaException("Error al eliminar la actividad: " + e.getMessage());
        }
    }

    /**
     * Actualiza una actividad existente.
     */
    public void actualizarActividad(Actividad actividadActualizada) throws BrigadaException {
        if (actividadActualizada == null) {
            throw new BrigadaException("La actividad actualizada no puede ser nula.");
        }

        Actividad actividadExistente = obtenerActividadPorId(actividadActualizada.getId());

        // Guardar estado anterior para revertir si falla
        String nombreAnterior = actividadExistente.getNombre();
        Date fechaAnterior = actividadExistente.getFecha();
        String lugarAnterior = actividadExistente.getLugar();
        String objetivoAnterior = actividadExistente.getObjetivo();
        String resultadosAnterior = actividadExistente.getResultados();
        Brigada brigadaAnterior = actividadExistente.getBrigadaAsociada();

        try {
            // Actualizar propiedades
            actividadExistente.setNombre(actividadActualizada.getNombre());
            actividadExistente.setFecha(actividadActualizada.getFecha());
            actividadExistente.setLugar(actividadActualizada.getLugar());
            actividadExistente.setObjetivo(actividadActualizada.getObjetivo());
            actividadExistente.setResultados(actividadActualizada.getResultados());

            // Actualizar brigada si es diferente
            if (actividadActualizada.getBrigadaAsociada() != null) {
                actividadExistente.setBrigadaAsociada(actividadActualizada.getBrigadaAsociada());
            }

            guardarActividades();
            System.out.println("[INFO] Actividad '" + actividadExistente.getNombre() + "' actualizada exitosamente.");
        } catch (PersistenciaException e) {
            // Revertir cambios si falla el guardado
            actividadExistente.setNombre(nombreAnterior);
            actividadExistente.setFecha(fechaAnterior);
            actividadExistente.setLugar(lugarAnterior);
            actividadExistente.setObjetivo(objetivoAnterior);
            actividadExistente.setResultados(resultadosAnterior);
            actividadExistente.setBrigadaAsociada(brigadaAnterior);
            throw new BrigadaException("Error al actualizar la actividad: " + e.getMessage());
        }
    }

    /**
     * Guarda una actividad (crea o actualiza).
     */
    public void guardarActividad(Actividad actividad) throws BrigadaException {
        if (actividad == null) {
            throw new BrigadaException("La actividad no puede ser nula.");
        }

        // Verificar si ya existe
        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).getId().equals(actividad.getId())) {
                actividades.set(i, actividad); // Actualizar
                try {
                    guardarActividades();
                    System.out.println("[INFO] Actividad '" + actividad.getNombre() + "' actualizada.");
                    return;
                } catch (PersistenciaException e) {
                    throw new BrigadaException("Error al actualizar actividad: " + e.getMessage());
                }
            }
        }

        // Si no existe, agregar
        actividades.add(actividad);
        try {
            guardarActividades();
            System.out.println("[INFO] Actividad '" + actividad.getNombre() + "' creada.");
        } catch (PersistenciaException e) {
            actividades.remove(actividad); // Revertir si falla
            throw new BrigadaException("Error al crear actividad: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las actividades.
     */
    public List<Actividad> obtenerTodasActividades() {
        return new ArrayList<>(actividades);
    }

    /**
     * Obtiene actividades por brigada.
     */
    public List<Actividad> obtenerActividadesPorBrigada(String brigadaId) {
        return actividades.stream()
                .filter(a -> a.getBrigadaAsociada() != null &&
                        brigadaId.equals(a.getBrigadaAsociada().getId()))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Obtiene actividades por fecha.
     */
    public List<Actividad> obtenerActividadesPorFecha(Date fecha) {
        return actividades.stream()
                .filter(a -> a.getFecha().equals(fecha))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Obtiene actividades pendientes (fecha futura).
     */
    public List<Actividad> obtenerActividadesPendientes() {
        Date ahora = new Date();
        return actividades.stream()
                .filter(a -> a.getFecha().after(ahora))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Obtiene actividades completadas (fecha pasada).
     */
    public List<Actividad> obtenerActividadesCompletadas() {
        Date ahora = new Date();
        return actividades.stream()
                .filter(a -> a.getFecha().before(ahora))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Busca actividades por término (nombre, lugar, objetivo).
     */
    public List<Actividad> buscarActividades(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return obtenerTodasActividades();
        }

        String terminoLower = termino.toLowerCase();
        return actividades.stream()
                .filter(a -> a.getNombre().toLowerCase().contains(terminoLower) ||
                        a.getLugar().toLowerCase().contains(terminoLower) ||
                        a.getObjetivo().toLowerCase().contains(terminoLower) ||
                        (a.getBrigadaAsociada() != null &&
                                a.getBrigadaAsociada().getNombre().toLowerCase().contains(terminoLower)))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Verifica si una actividad existe.
     */
    private boolean existeActividad(String id) {
        return buscarActividadPorId(id) != null;
    }

    /**
     * Obtiene el total de actividades.
     */
    public int obtenerTotalActividades() {
        return actividades.size();
    }

    /**
     * Obtiene el total de voluntarios asignados a actividades.
     */
    public int obtenerTotalVoluntariosEnActividades() {
        return actividades.stream()
                .mapToInt(a -> a.getVoluntariosAsignados().size())
                .sum();
    }

    /**
     * Obtiene actividades con voluntarios asignados.
     */
    public List<Actividad> obtenerActividadesConVoluntarios() {
        return actividades.stream()
                .filter(a -> !a.getVoluntariosAsignados().isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Obtiene actividades sin voluntarios asignados.
     */
    public List<Actividad> obtenerActividadesSinVoluntarios() {
        return actividades.stream()
                .filter(a -> a.getVoluntariosAsignados().isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Obtiene estadísticas de actividades.
     */
    public String obtenerEstadisticasActividades() {
        int total = obtenerTotalActividades();
        int pendientes = obtenerActividadesPendientes().size();
        int completadas = obtenerActividadesCompletadas().size();
        int conVoluntarios = obtenerActividadesConVoluntarios().size();
        int sinVoluntarios = obtenerActividadesSinVoluntarios().size();

        return String.format("""
            Estadísticas de Actividades:
            ----------------------------
            Total actividades: %d
            • Pendientes: %d
            • Completadas: %d
            • Con voluntarios asignados: %d
            • Sin voluntarios asignados: %d
            Total voluntarios asignados: %d
            """,
                total, pendientes, completadas, conVoluntarios, sinVoluntarios,
                obtenerTotalVoluntariosEnActividades());
    }

    /**
     * Obtiene actividades por estado.
     */
    public List<Actividad> obtenerActividadesPorEstado(String estado) {
        Date ahora = new Date();
        switch (estado.toLowerCase()) {
            case "pendiente":
                return obtenerActividadesPendientes();
            case "completada":
                return obtenerActividadesCompletadas();
            case "en proceso":
                // Actividades cuya fecha ya pasó pero no tienen resultados registrados
                return actividades.stream()
                        .filter(a -> a.getFecha().before(ahora) && a.getResultados() == null)
                        .collect(java.util.stream.Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Desasigna un voluntario de una actividad.
     */
    public void desasignarVoluntario(String actividadId, String voluntarioId) throws BrigadaException {
        Actividad actividad = obtenerActividadPorId(actividadId);
        if (actividad == null) {
            throw new BrigadaException("Actividad no encontrada: " + actividadId);
        }

        try {
            actividad.eliminarVoluntario(voluntarioId);

            // Actualizar mapa de asignaciones
            if (asignacionesVoluntarios.containsKey(actividadId)) {
                asignacionesVoluntarios.get(actividadId).remove(voluntarioId);
            }

            guardarActividades();
            System.out.println("[INFO] Voluntario desasignado de la actividad '" + actividad.getNombre() + "'.");

        } catch (Exception e) {
            throw new BrigadaException("Error al desasignar voluntario: " + e.getMessage());
        }
    }

    /**
     * Verifica si un voluntario está asignado a una actividad.
     */
    public boolean estaVoluntarioAsignado(String actividadId, String voluntarioId) {
        Map<String, String> asignaciones = asignacionesVoluntarios.get(actividadId);
        return asignaciones != null && asignaciones.containsKey(voluntarioId);
    }

    /**
     * Obtiene el rol de un voluntario en una actividad.
     */
    public String obtenerRolVoluntario(String actividadId, String voluntarioId) {
        Map<String, String> asignaciones = asignacionesVoluntarios.get(actividadId);
        return asignaciones != null ? asignaciones.get(voluntarioId) : null;
    }

    /**
     * Genera un reporte de actividades por brigada.
     */
    public String generarReportePorBrigada(String brigadaId) {
        List<Actividad> actividadesBrigada = obtenerActividadesPorBrigada(brigadaId);

        StringBuilder reporte = new StringBuilder();
        reporte.append("Reporte de Actividades - Brigada ID: ").append(brigadaId).append("\n");
        reporte.append("=".repeat(50)).append("\n");

        for (Actividad actividad : actividadesBrigada) {
            reporte.append(String.format("• %s (ID: %s)\n", actividad.getNombre(), actividad.getId()));
            reporte.append(String.format("  Fecha: %s | Lugar: %s\n",
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(actividad.getFecha()),
                    actividad.getLugar()));
            reporte.append(String.format("  Voluntarios asignados: %d\n",
                    actividad.getVoluntariosAsignados().size()));
            reporte.append(String.format("  Estado: %s\n\n",
                    actividad.getFecha().after(new Date()) ? "Pendiente" : "Completada"));
        }

        return reporte.toString();
    }
}