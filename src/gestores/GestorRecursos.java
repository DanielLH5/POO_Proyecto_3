package gestores;

import model.Recurso;
import model.Actividad;
import exceptions.BrigadaException;
import exceptions.PersistenciaException;
import persistence.GestorAlmacenamiento;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor responsable de la administración de recursos e inventario (RF-06, RF-10).
 * Incluye persistencia completa de datos.
 */
public class GestorRecursos {
    private static final String ARCHIVO_RECURSOS = "recursos.dat";
    private List<Recurso> inventario;
    private GestorAlmacenamiento gestorAlmacenamiento;

    public GestorRecursos() {
        this.gestorAlmacenamiento = new GestorAlmacenamiento();
        this.inventario = new ArrayList<>();
        cargarRecursos();
    }

    /**
     * Carga los recursos desde el archivo de persistencia.
     */
    @SuppressWarnings("unchecked")
    private void cargarRecursos() {
        try {
            Object datos = gestorAlmacenamiento.cargar(ARCHIVO_RECURSOS);
            if (datos != null) {
                inventario = (List<Recurso>) datos;
                System.out.println("[INFO] Cargados " + inventario.size() + " recursos desde persistencia.");

                // Verificar alertas de stock al cargar
                verificarAlertasStock();
            } else {
                inventario = new ArrayList<>();
                System.out.println("[INFO] No se encontraron recursos guardados, iniciando inventario vacío.");
            }
        } catch (PersistenciaException e) {
            System.err.println("Error cargando recursos: " + e.getMessage());
            inventario = new ArrayList<>();
        }
    }

    /**
     * Guarda los recursos en el archivo de persistencia.
     */
    private void guardarRecursos() throws PersistenciaException {
        gestorAlmacenamiento.guardar(ARCHIVO_RECURSOS, inventario);
    }

    /**
     * Verifica alertas de stock para todos los recursos.
     */
    private void verificarAlertasStock() {
        for (Recurso recurso : inventario) {
            recurso.verificarAlerta();
        }
    }

    /**
     * Agrega un recurso al inventario con persistencia.
     */
    public void agregarRecurso(Recurso recurso) throws BrigadaException {
        if (recurso == null) {
            throw new BrigadaException("El recurso no puede ser nulo.");
        }

        // Verificar si ya existe un recurso con el mismo ID
        Recurso existente = buscarRecursoPorId(recurso.getId());
        if (existente != null) {
            throw new BrigadaException("Ya existe un recurso con ID: " + recurso.getId());
        }

        try {
            inventario.add(recurso);
            guardarRecursos();
            System.out.println("[INFO] Recurso '" + recurso.getNombre() + "' agregado al inventario.");
            recurso.verificarAlerta(); // Verifica el umbral inicial
        } catch (PersistenciaException e) {
            // Revertir si falla el guardado
            inventario.remove(recurso);
            throw new BrigadaException("Error al guardar el recurso: " + e.getMessage());
        }
    }

    /**
     * Consulta la disponibilidad de un recurso (RF-10).
     */
    public boolean consultarDisponibilidad(String recursoId, int cantidadRequerida) {
        Recurso recurso = buscarRecursoPorId(recursoId);
        if (recurso != null) {
            return recurso.consultarDisponibilidad(cantidadRequerida);
        }
        return false;
    }

    /**
     * Asigna recursos a una actividad y deduce el stock (RF-06).
     */
    public void asignarRecursosAActividad(Actividad actividad, String recursoId, int cantidad)
            throws BrigadaException {

        Recurso recurso = buscarRecursoPorId(recursoId);

        if (recurso == null) {
            throw new BrigadaException("Recurso no encontrado: " + recursoId);
        }

        if (!consultarDisponibilidad(recursoId, cantidad)) {
            throw new BrigadaException("Stock insuficiente para " + recurso.getNombre() +
                    ". Se requieren " + cantidad + ", disponible: " + recurso.getStockActual());
        }

        try {
            // 1. Deducir del stock
            recurso.deducirStock(cantidad);

            // 2. Registrar el uso en la Actividad
            actividad.registrarUsoRecurso(recursoId, cantidad);

            // 3. Guardar cambios
            guardarRecursos();

            System.out.println("[INFO] Recurso " + recurso.getNombre() + " asignado a " +
                    actividad.getNombre() + " (Cantidad: " + cantidad + "). (RF-06)");
            recurso.verificarAlerta();

        } catch (PersistenciaException e) {
            // Revertir la deducción si falla el guardado
            recurso.reponerStock(cantidad);
            throw new BrigadaException("Error al asignar recursos: " + e.getMessage());
        }
    }

    /**
     * Busca un recurso por ID.
     */
    public Recurso buscarRecursoPorId(String id) {
        return inventario.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene todos los recursos del inventario.
     */
    public List<Recurso> obtenerTodosRecursos() {
        return new ArrayList<>(inventario);
    }

    /**
     * Actualiza un recurso existente con persistencia.
     */
    public void actualizarRecurso(Recurso recursoActualizado) throws BrigadaException {
        if (recursoActualizado == null) {
            throw new BrigadaException("El recurso actualizado no puede ser nulo.");
        }

        Recurso recursoExistente = buscarRecursoPorId(recursoActualizado.getId());
        if (recursoExistente == null) {
            throw new BrigadaException("Recurso no encontrado con ID: " + recursoActualizado.getId());
        }

        // Guardar estado anterior para revertir si falla
        String nombreAnterior = recursoExistente.getNombre();
        String categoriaAnterior = recursoExistente.getCategoria();
        int stockAnterior = recursoExistente.getStockActual();
        int capacidadAnterior = recursoExistente.getCapacidadMaxima();
        int umbralAnterior = recursoExistente.getUmbralAlerta();

        try {
            // Actualizar propiedades
            recursoExistente.setNombre(recursoActualizado.getNombre());
            recursoExistente.setCategoria(recursoActualizado.getCategoria());
            recursoExistente.setStockActual(recursoActualizado.getStockActual());
            recursoExistente.setCapacidadMaxima(recursoActualizado.getCapacidadMaxima());
            recursoExistente.setUmbralAlerta(recursoActualizado.getUmbralAlerta());

            guardarRecursos();
            System.out.println("[INFO] Recurso '" + recursoExistente.getNombre() + "' actualizado.");
            recursoExistente.verificarAlerta();

        } catch (PersistenciaException e) {
            // Revertir cambios si falla el guardado
            recursoExistente.setNombre(nombreAnterior);
            recursoExistente.setCategoria(categoriaAnterior);
            recursoExistente.setStockActual(stockAnterior);
            recursoExistente.setCapacidadMaxima(capacidadAnterior);
            recursoExistente.setUmbralAlerta(umbralAnterior);
            throw new BrigadaException("Error al actualizar el recurso: " + e.getMessage());
        }
    }

    /**
     * Elimina un recurso del inventario con persistencia.
     */
    public void eliminarRecurso(String id) throws BrigadaException {
        Recurso recurso = buscarRecursoPorId(id);
        if (recurso == null) {
            throw new BrigadaException("Recurso no encontrado con ID: " + id);
        }

        // Verificar si el recurso tiene stock asignado
        if (recurso.getStockActual() > 0) {
            throw new BrigadaException("No se puede eliminar el recurso '" + recurso.getNombre() +
                    "' porque aún tiene stock disponible: " + recurso.getStockActual());
        }

        try {
            inventario.remove(recurso);
            guardarRecursos();
            System.out.println("[INFO] Recurso '" + recurso.getNombre() + "' eliminado del inventario.");
        } catch (PersistenciaException e) {
            // Revertir si falla el guardado
            inventario.add(recurso);
            throw new BrigadaException("Error al eliminar el recurso: " + e.getMessage());
        }
    }

    /**
     * Busca recursos por término de búsqueda.
     */
    public List<Recurso> buscarRecursos(String termino) {
        List<Recurso> resultados = new ArrayList<>();
        if (termino == null || termino.trim().isEmpty()) {
            return obtenerTodosRecursos();
        }

        String terminoLower = termino.toLowerCase();
        for (Recurso recurso : inventario) {
            if (recurso.getNombre().toLowerCase().contains(terminoLower) ||
                    recurso.getCategoria().toLowerCase().contains(terminoLower) ||
                    recurso.getId().toLowerCase().contains(terminoLower)) {
                resultados.add(recurso);
            }
        }
        return resultados;
    }

    /**
     * Obtiene recursos con stock bajo (por debajo del umbral).
     */
    public List<Recurso> obtenerRecursosBajoStock() {
        List<Recurso> recursosBajoStock = new ArrayList<>();
        for (Recurso recurso : inventario) {
            if (recurso.getStockActual() <= recurso.getUmbralAlerta()) {
                recursosBajoStock.add(recurso);
            }
        }
        return recursosBajoStock;
    }

    public List<Recurso> obtenerRecursosDeActividad(Actividad actividad) {
        List<Recurso> recursos = new ArrayList<>();

        if (actividad.getRecursosAsignados() != null) {
            for (String recursoId : actividad.getRecursosAsignados().keySet()) {
                Recurso recurso = buscarRecursoPorId(recursoId);
                if (recurso != null) {
                    recursos.add(recurso);
                }
            }
        }

        return recursos;
    }

    /**
     * Repone stock de un recurso con persistencia.
     */
    public void reponerStock(String recursoId, int cantidad) throws BrigadaException {
        Recurso recurso = buscarRecursoPorId(recursoId);
        if (recurso == null) {
            throw new BrigadaException("Recurso no encontrado: " + recursoId);
        }

        int stockAnterior = recurso.getStockActual();
        try {
            recurso.reponerStock(cantidad);
            guardarRecursos();
            System.out.println("[INFO] Stock repuesto para " + recurso.getNombre() +
                    ". Nueva cantidad: " + recurso.getStockActual());
        } catch (PersistenciaException e) {
            // Revertir si falla el guardado
            recurso.setStockActual(stockAnterior);
            throw new BrigadaException("Error al reponer stock: " + e.getMessage());
        }
    }

    /**
     * Obtiene el porcentaje total de recursos disponibles.
     */
    public double obtenerPorcentajeRecursosDisponibles() {
        if (inventario.isEmpty()) return 0.0;

        double totalStock = 0;
        double totalCapacidad = 0;

        for (Recurso recurso : inventario) {
            totalStock += recurso.getStockActual();
            totalCapacidad += recurso.getCapacidadMaxima();
        }

        if (totalCapacidad == 0) return 0.0;
        return (totalStock / totalCapacidad) * 100;
    }

    /**
     * Obtiene estadísticas del inventario.
     */
    public String obtenerEstadisticasInventario() {
        int totalRecursos = inventario.size();
        int recursosBajoStock = obtenerRecursosBajoStock().size();
        double porcentajeDisponible = obtenerPorcentajeRecursosDisponibles();

        StringBuilder estadisticas = new StringBuilder();
        estadisticas.append("Estadísticas del Inventario:\n");
        estadisticas.append("=".repeat(40)).append("\n");
        estadisticas.append(String.format("Total de recursos: %d\n", totalRecursos));
        estadisticas.append(String.format("Recursos con stock bajo: %d\n", recursosBajoStock));
        estadisticas.append(String.format("Porcentaje total disponible: %.1f%%\n", porcentajeDisponible));

        if (recursosBajoStock > 0) {
            estadisticas.append("\nRecursos que requieren atención:\n");
            for (Recurso recurso : obtenerRecursosBajoStock()) {
                estadisticas.append(String.format("  • %s (ID: %s) - Stock: %d/%d\n",
                        recurso.getNombre(), recurso.getId(),
                        recurso.getStockActual(), recurso.getCapacidadMaxima()));
            }
        }

        return estadisticas.toString();
    }

    /**
     * Guarda el estado actual del inventario.
     */
    public void guardarInventario() throws BrigadaException {
        try {
            guardarRecursos();
            System.out.println("[INFO] Inventario guardado exitosamente.");
        } catch (PersistenciaException e) {
            throw new BrigadaException("Error al guardar el inventario: " + e.getMessage());
        }
    }

    /**
     * Carga el inventario desde el archivo.
     */
    public void cargarInventario() throws BrigadaException {
        try {
            cargarRecursos();
            System.out.println("[INFO] Inventario cargado exitosamente.");
        } catch (Exception e) {
            throw new BrigadaException("Error al cargar el inventario: " + e.getMessage());
        }
    }

    /**
     * Obtiene el total de recursos en inventario.
     */
    public int obtenerTotalRecursos() {
        return inventario.size();
    }

    /**
     * Obtiene el total de stock disponible.
     */
    public int obtenerTotalStockDisponible() {
        return inventario.stream()
                .mapToInt(Recurso::getStockActual)
                .sum();
    }
}