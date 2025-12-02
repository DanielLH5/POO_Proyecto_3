package gestores;

import model.Brigada;
import model.Voluntario;
import model.Coordinador;
import exceptions.BrigadaException;
import exceptions.PersistenciaException;
import exceptions.VoluntarioNoEncontradoException;
import persistence.GestorAlmacenamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
Gestor responsable de la administración de brigadas en el sistema (RF-01, RF-03).
*/
public class GestorBrigadas {
    private static final String ARCHIVO_BRIGADAS = "brigadas.dat";
    private List<Brigada> brigadas;
    private GestorAlmacenamiento gestor;
    // Referencia cruzada para validar voluntarios
    private GestorVoluntarios gestorVoluntarios;

    public GestorBrigadas(GestorVoluntarios gestorVoluntarios) {
        this.gestor = new GestorAlmacenamiento();
        this.gestorVoluntarios = gestorVoluntarios;
        cargarBrigadas();
    }

    @SuppressWarnings("unchecked")
    private void cargarBrigadas() {
        try {
            Object datos = gestor.cargar(ARCHIVO_BRIGADAS);
            if (datos != null) {
                brigadas = (List<Brigada>) datos;
            } else {
                brigadas = new ArrayList<>();
            }
        } catch (PersistenciaException e) {
            System.err.println("Error cargando brigadas: " + e.getMessage());
            brigadas = new ArrayList<>();
        }
    }

    private void guardarBrigadas() throws PersistenciaException {
        gestor.guardar(ARCHIVO_BRIGADAS, brigadas);
    }

    /* Crea una nueva brigada en el sistema (RF-01). */
    public void crearBrigada(Brigada brigada) throws BrigadaException {
        if (brigada == null) {
            throw new BrigadaException("Error: La brigada no puede ser nula.");
        }

        if (existeBrigada(brigada.getId())) {
            throw new BrigadaException("Error: La brigada con ID " + brigada.getId() + " ya existe.");
        }

        if (brigada.getId() == null || brigada.getId().trim().isEmpty()) {
            throw new BrigadaException("Error: El ID de la brigada no puede estar vacío.");
        }

        if (brigada.getNombre() == null || brigada.getNombre().trim().isEmpty()) {
            throw new BrigadaException("Error: El nombre de la brigada no puede estar vacío.");
        }

        brigadas.add(brigada);
        try {
            guardarBrigadas();
            System.out.println("[INFO] Brigada " + brigada.getNombre() + " creada exitosamente.");
        } catch (PersistenciaException e) {
            brigadas.remove(brigada); // Revertir si falla el guardado
            throw new BrigadaException("Error al guardar la brigada: " + e.getMessage());
        }
    }

    /* Busca una brigada por su identificador único. */
    public Brigada buscarBrigadaPorId(String id) {
        return brigadas.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /* Obtiene una brigada por su ID o lanza excepción si no existe */
    public Brigada obtenerBrigadaPorId(String id) throws BrigadaException {
        Brigada brigada = buscarBrigadaPorId(id);
        if (brigada == null) {
            throw new BrigadaException("Brigada no encontrada con ID: " + id);
        }
        return brigada;
    }

    /* Agrega un voluntario a una brigada específica (RF-03). */
    public void agregarVoluntarioABrigada(String brigadaId, String voluntarioId)
            throws BrigadaException, VoluntarioNoEncontradoException {

        Brigada brigada = obtenerBrigadaPorId(brigadaId);

        Voluntario voluntario = gestorVoluntarios.buscarVoluntarioPorId(voluntarioId);
        if (voluntario == null) {
            throw new VoluntarioNoEncontradoException(voluntarioId);
        }

        // Verificar si el voluntario ya está en la brigada
        if (brigada.getVoluntarios().contains(voluntario)) {
            throw new BrigadaException("El voluntario ya está asignado a esta brigada.");
        }

        brigada.agregarVoluntario(voluntario);
        try {
            guardarBrigadas();
            System.out.println("[INFO] Voluntario " + voluntario.getNombre() + " agregado a brigada " + brigada.getNombre());
        } catch (PersistenciaException e) {
            brigada.eliminarVoluntario(voluntario); // Revertir si falla
            throw new BrigadaException("Error al guardar cambios: " + e.getMessage());
        }
    }

    /* Elimina un voluntario de una brigada */
    public void eliminarVoluntarioDeBrigada(String brigadaId, String voluntarioId)
            throws BrigadaException, VoluntarioNoEncontradoException {

        Brigada brigada = obtenerBrigadaPorId(brigadaId);

        Voluntario voluntario = gestorVoluntarios.buscarVoluntarioPorId(voluntarioId);
        if (voluntario == null) {
            throw new VoluntarioNoEncontradoException(voluntarioId);
        }

        if (!brigada.getVoluntarios().contains(voluntario)) {
            throw new BrigadaException("El voluntario no está asignado a esta brigada.");
        }

        brigada.eliminarVoluntario(voluntario);
        try {
            guardarBrigadas();
            System.out.println("[INFO] Voluntario " + voluntario.getNombre() + " eliminado de brigada " + brigada.getNombre());
        } catch (PersistenciaException e) {
            brigada.agregarVoluntario(voluntario); // Revertir si falla
            throw new BrigadaException("Error al guardar cambios: " + e.getMessage());
        }
    }

    /* NUEVO MÉTODO: Obtiene voluntarios de una brigada específica */
    public List<Voluntario> obtenerVoluntariosDeBrigada(String brigadaId) {
        Brigada brigada = buscarBrigadaPorId(brigadaId);
        if (brigada == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(brigada.getVoluntarios());
    }

    /* Asigna un coordinador a una brigada. */
    public void asignarCoordinadorABrigada(String brigadaId, Coordinador coordinador)
            throws BrigadaException {

        Brigada brigada = obtenerBrigadaPorId(brigadaId);

        if (coordinador == null) {
            throw new BrigadaException("El coordinador no puede ser nulo.");
        }

        brigada.setCoordinador(coordinador);
        try {
            guardarBrigadas();
            System.out.println("[INFO] Coordinador " + coordinador.getNombre() + " asignado a brigada " + brigada.getNombre());
        } catch (PersistenciaException e) {
            throw new BrigadaException("Error al guardar cambios: " + e.getMessage());
        }
    }

    /* Elimina una brigada del sistema */
    public void eliminarBrigada(String id) throws BrigadaException {
        Brigada brigada = obtenerBrigadaPorId(id);

        // Verificar si la brigada tiene voluntarios asignados
        if (!brigada.getVoluntarios().isEmpty()) {
            throw new BrigadaException("No se puede eliminar la brigada " + brigada.getNombre() +
                    " porque tiene " + brigada.getVoluntarios().size() + " voluntarios asignados." +
                    "\nReasigna o elimina los voluntarios primero.");
        }

        brigadas.remove(brigada);
        try {
            guardarBrigadas();
            System.out.println("[INFO] Brigada " + brigada.getNombre() + " eliminada exitosamente.");
        } catch (PersistenciaException e) {
            brigadas.add(brigada); // Revertir si falla
            throw new BrigadaException("Error al eliminar la brigada: " + e.getMessage());
        }
    }

    /* Elimina una brigada forzadamente (sin verificar voluntarios) */
    public void eliminarBrigadaForzadamente(String id) throws BrigadaException {
        Brigada brigada = obtenerBrigadaPorId(id);

        if (brigada == null) {
            throw new BrigadaException("Brigada no encontrada: " + id);
        }

        brigadas.remove(brigada);
        try {
            guardarBrigadas();
            System.out.println("[WARNING] Brigada " + brigada.getNombre() + " eliminada forzadamente.");
        } catch (PersistenciaException e) {
            brigadas.add(brigada); // Revertir si falla
            throw new BrigadaException("Error al eliminar la brigada: " + e.getMessage());
        }
    }

    /* Actualiza una brigada existente */
    public void actualizarBrigada(Brigada brigadaActualizada) throws BrigadaException {
        if (brigadaActualizada == null) {
            throw new BrigadaException("La brigada actualizada no puede ser nula.");
        }

        Brigada brigadaExistente = obtenerBrigadaPorId(brigadaActualizada.getId());

        // Guardar el estado anterior para poder revertir si es necesario
        String nombreAnterior = brigadaExistente.getNombre();
        String tipoAnterior = brigadaExistente.getTipo();
        String zonaAnterior = brigadaExistente.getZona();
        String descripcionAnterior = brigadaExistente.getDescripcion();
        String estadoAnterior = brigadaExistente.getEstado();
        Coordinador coordinadorAnterior = brigadaExistente.getCoordinador();

        try {
            // Actualizar propiedades
            brigadaExistente.setNombre(brigadaActualizada.getNombre());
            brigadaExistente.setTipo(brigadaActualizada.getTipo());
            brigadaExistente.setZona(brigadaActualizada.getZona());
            brigadaExistente.setDescripcion(brigadaActualizada.getDescripcion());
            brigadaExistente.setEstado(brigadaActualizada.getEstado());

            // Actualizar coordinador si es diferente
            if (brigadaActualizada.getCoordinador() != null) {
                brigadaExistente.setCoordinador(brigadaActualizada.getCoordinador());
            }

            guardarBrigadas();
            System.out.println("[INFO] Brigada " + brigadaExistente.getNombre() + " actualizada exitosamente.");
        } catch (PersistenciaException e) {
            // Revertir cambios si falla el guardado
            brigadaExistente.setNombre(nombreAnterior);
            brigadaExistente.setTipo(tipoAnterior);
            brigadaExistente.setZona(zonaAnterior);
            brigadaExistente.setDescripcion(descripcionAnterior);
            brigadaExistente.setEstado(estadoAnterior);
            brigadaExistente.setCoordinador(coordinadorAnterior);
            throw new BrigadaException("Error al actualizar la brigada: " + e.getMessage());
        }
    }

    /* Guarda una brigada (crea o actualiza) */
    public void guardarBrigada(Brigada brigada) throws BrigadaException {
        if (brigada == null) {
            throw new BrigadaException("La brigada no puede ser nula.");
        }

        // Verificar si ya existe
        for (int i = 0; i < brigadas.size(); i++) {
            if (brigadas.get(i).getId().equals(brigada.getId())) {
                brigadas.set(i, brigada); // Actualizar
                try {
                    guardarBrigadas();
                    System.out.println("[INFO] Brigada " + brigada.getNombre() + " actualizada.");
                    return;
                } catch (PersistenciaException e) {
                    throw new BrigadaException("Error al actualizar brigada: " + e.getMessage());
                }
            }
        }

        // Si no existe, agregar
        brigadas.add(brigada);
        try {
            guardarBrigadas();
            System.out.println("[INFO] Brigada " + brigada.getNombre() + " creada.");
        } catch (PersistenciaException e) {
            brigadas.remove(brigada); // Revertir si falla
            throw new BrigadaException("Error al crear brigada: " + e.getMessage());
        }
    }

    /* Obtiene todas las brigadas */
    public List<Brigada> obtenerTodasBrigadas() {
        return new ArrayList<>(brigadas);
    }

    /* Obtiene brigadas activas */
    public List<Brigada> obtenerBrigadasActivas() {
        return brigadas.stream()
                .filter(b -> "Activa".equalsIgnoreCase(b.getEstado()))
                .collect(Collectors.toList());
    }

    /* Obtiene brigadas inactivas */
    public List<Brigada> obtenerBrigadasInactivas() {
        return brigadas.stream()
                .filter(b -> "Inactiva".equalsIgnoreCase(b.getEstado()))
                .collect(Collectors.toList());
    }

    /* Obtiene brigadas en planificación */
    public List<Brigada> obtenerBrigadasEnPlanificacion() {
        return brigadas.stream()
                .filter(b -> "En planificación".equalsIgnoreCase(b.getEstado()))
                .collect(Collectors.toList());
    }

    /* Obtiene brigadas por tipo */
    public List<Brigada> obtenerBrigadasPorTipo(String tipo) {
        return brigadas.stream()
                .filter(b -> tipo.equalsIgnoreCase(b.getTipo()))
                .collect(Collectors.toList());
    }

    /* Obtiene brigadas por zona */
    public List<Brigada> obtenerBrigadasPorZona(String zona) {
        return brigadas.stream()
                .filter(b -> zona.equalsIgnoreCase(b.getZona()))
                .collect(Collectors.toList());
    }

    /* Obtiene brigadas coordinadas por un coordinador específico */
    public List<Brigada> obtenerBrigadasPorCoordinador(String coordinadorId) {
        return brigadas.stream()
                .filter(b -> b.getCoordinador() != null &&
                        coordinadorId.equals(b.getCoordinador().getId()))
                .collect(Collectors.toList());
    }

    /* Busca brigadas por nombre (búsqueda parcial) */
    public List<Brigada> buscarBrigadasPorNombre(String nombre) {
        String nombreLower = nombre.toLowerCase();
        return brigadas.stream()
                .filter(b -> b.getNombre().toLowerCase().contains(nombreLower))
                .collect(Collectors.toList());
    }

    /* Busca brigadas por término (nombre, tipo o zona) */
    public List<Brigada> buscarBrigadas(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return obtenerTodasBrigadas();
        }

        String terminoLower = termino.toLowerCase();
        return brigadas.stream()
                .filter(b -> b.getNombre().toLowerCase().contains(terminoLower) ||
                        b.getTipo().toLowerCase().contains(terminoLower) ||
                        b.getZona().toLowerCase().contains(terminoLower) ||
                        b.getDescripcion().toLowerCase().contains(terminoLower))
                .collect(Collectors.toList());
    }

    /* Obtiene el total de brigadas */
    public int obtenerTotalBrigadas() {
        return brigadas.size();
    }

    /* Obtiene el total de voluntarios en todas las brigadas */
    public int obtenerTotalVoluntariosEnBrigadas() {
        return brigadas.stream()
                .mapToInt(b -> b.getVoluntarios().size())
                .sum();
    }

    /* Verifica si una brigada existe */
    private boolean existeBrigada(String id) {
        return buscarBrigadaPorId(id) != null;
    }

    /* Verifica si un voluntario está en alguna brigada */
    public boolean voluntarioEstaEnAlgunaBrigada(String voluntarioId) {
        return brigadas.stream()
                .anyMatch(b -> b.getVoluntarios().stream()
                        .anyMatch(v -> v.getId().equals(voluntarioId)));
    }

    /* Obtiene las brigadas a las que pertenece un voluntario */
    public List<Brigada> obtenerBrigadasDeVoluntario(String voluntarioId) {
        return brigadas.stream()
                .filter(b -> b.getVoluntarios().stream()
                        .anyMatch(v -> v.getId().equals(voluntarioId)))
                .collect(Collectors.toList());
    }

    /* Convoca voluntarios urgentes (RF-09) */
    public void convocarUrgente(String brigadaId, String mensaje) throws BrigadaException {
        Brigada brigada = obtenerBrigadaPorId(brigadaId);

        if (brigada.getVoluntarios().isEmpty()) {
            throw new BrigadaException("La brigada no tiene voluntarios asignados.");
        }

        System.out.println("\n--- ALERTA URGENTE - Brigada " + brigada.getNombre() + " (RF-09) ---");
        System.out.println("Mensaje: " + mensaje);
        System.out.println("--- Enviando avisos a voluntarios ---");

        for (Voluntario v : brigada.getVoluntarios()) {
            // Simulación del envío de aviso (RNF-03)
            System.out.println("✓ Aviso enviado a: " + v.getNombre() +
                    " (" + v.getEmail() + " | " + v.getTelefono() + ")");
        }
        System.out.println("✓ Total de avisos urgentes enviados: " + brigada.getVoluntarios().size());
    }

    /* Cambia el estado de una brigada */
    public void cambiarEstadoBrigada(String brigadaId, String nuevoEstado) throws BrigadaException {
        Brigada brigada = obtenerBrigadaPorId(brigadaId);

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new BrigadaException("El nuevo estado no puede estar vacío.");
        }

        String estadoAnterior = brigada.getEstado();
        brigada.setEstado(nuevoEstado);

        try {
            guardarBrigadas();
            System.out.println("[INFO] Brigada " + brigada.getNombre() +
                    " cambió de estado: " + estadoAnterior + " → " + nuevoEstado);
        } catch (PersistenciaException e) {
            brigada.setEstado(estadoAnterior); // Revertir si falla
            throw new BrigadaException("Error al cambiar estado: " + e.getMessage());
        }
    }

    /* Activa una brigada */
    public void activarBrigada(String brigadaId) throws BrigadaException {
        cambiarEstadoBrigada(brigadaId, "Activa");
    }

    /* Desactiva una brigada */
    public void desactivarBrigada(String brigadaId) throws BrigadaException {
        cambiarEstadoBrigada(brigadaId, "Inactiva");
    }

    /* Obtiene estadísticas de brigadas */
    public String obtenerEstadisticasBrigadas() {
        int total = obtenerTotalBrigadas();
        int activas = obtenerBrigadasActivas().size();
        int inactivas = obtenerBrigadasInactivas().size();
        int planificacion = obtenerBrigadasEnPlanificacion().size();
        int totalVoluntarios = obtenerTotalVoluntariosEnBrigadas();

        return String.format("""
            Estadísticas de Brigadas:
            -------------------------
            Total brigadas: %d
            • Activas: %d
            • Inactivas: %d
            • En planificación: %d
            Total voluntarios asignados: %d
            Promedio voluntarios/brigada: %.1f
            """,
                total, activas, inactivas, planificacion, totalVoluntarios,
                total > 0 ? (double) totalVoluntarios / total : 0.0);
    }
}