package gestores;

import model.Recurso;
import exceptions.RecursoNoDisponibleException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
Gestor responsable del inventario y gestión de recursos del sistema.
Implementa la interfaz Gestionable para proveer funcionalidades de control de disponibilidad y cantidad de recursos.
*/
public class GestorRecursos implements Gestionable {
    private List<Recurso> recursos;

    public GestorRecursos() {
        this.recursos = new ArrayList<>();
    }

    /*
    Agrega un nuevo recurso al inventario del sistema
    @param recurso el recurso a agregar
    */
    public void agregarRecurso(Recurso recurso) {
        if (recurso != null && !existeRecurso(recurso.getId())) {
            recursos.add(recurso);
        }
    }

    /*
    Obtiene todos los recursos del inventario
    @return lista de recursos
    */
    public List<Recurso> obtenerRecursos() {
        return new ArrayList<>(recursos);
    }

    /*
    Busca un recurso por su identificador único
    @param id identificador del recurso
    @return el recurso encontrado o null si no existe
    */
    public Recurso buscarRecursoPorId(String id) {
        return recursos.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /*
    Obtiene la lista de recursos disponibles (con cantidad > 0)
    @return lista de recursos disponibles
    */
    public List<Recurso> consultarRecursosDisponibles() {
        return recursos.stream()
                .filter(Recurso::estaDisponible)
                .collect(Collectors.toList());
    }

    /*
    Actualiza la cantidad disponible de un recurso específico
    @param recursoId identificador del recurso
    @param nuevaCantidad nueva cantidad a establecer
    @throws RecursoNoDisponibleException si el recurso no existe
    */
    public void actualizarCantidadRecurso(String recursoId, int nuevaCantidad)
            throws RecursoNoDisponibleException {

        Recurso recurso = buscarRecursoPorId(recursoId);
        if (recurso == null) {
            throw new RecursoNoDisponibleException(recursoId);
        }

        recurso.actualizarCantidad(nuevaCantidad);
    }

    /*
    Consume una cantidad específica de un recurso
    @param recursoId identificador del recurso
    @param cantidad cantidad a consumir
    @throws RecursoNoDisponibleException si no hay suficiente cantidad
    */
    public void consumirRecurso(String recursoId, int cantidad)
            throws RecursoNoDisponibleException {

        Recurso recurso = buscarRecursoPorId(recursoId);
        if (recurso == null) {
            throw new RecursoNoDisponibleException(recursoId);
        }

        if (recurso.getCantidadDisponible() < cantidad) {
            throw new RecursoNoDisponibleException("Cantidad insuficiente de: " + recurso.getNombre());
        }

        recurso.consumir(cantidad);
    }

    /*
    Repone cantidad a un recurso existente
    @param recursoId identificador del recurso
    @param cantidad cantidad a reponer
    @throws RecursoNoDisponibleException si el recurso no existe
    */
    public void reponerRecurso(String recursoId, int cantidad)
            throws RecursoNoDisponibleException {

        Recurso recurso = buscarRecursoPorId(recursoId);
        if (recurso == null) {
            throw new RecursoNoDisponibleException(recursoId);
        }

        recurso.reponer(cantidad);
    }

    /*
    Obtiene recursos de un tipo específico
    @param tipo tipo de recurso a filtrar
    @return lista de recursos del tipo especificado
    */
    public List<Recurso> obtenerRecursosPorTipo(String tipo) {
        return recursos.stream()
                .filter(r -> r.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    // Implementación de la interfaz Gestionable
    @Override
    public boolean estaDisponible() {
        return recursos.stream().anyMatch(Recurso::estaDisponible);
    }

    @Override
    public void actualizarCantidad(int cantidad) {
        // Implementación para el gestor en general
        // Podría usarse para establecer un límite global de recursos
    }

    /*
    Verifica si existe un recurso con el identificador especificado
    @param id identificador a verificar
    @return true si existe, false en caso contrario
    */
    private boolean existeRecurso(String id) {
        return recursos.stream().anyMatch(r -> r.getId().equals(id));
    }

    /*
    Obtiene el número total de recursos en inventario
    @return cantidad de recursos
    */
    public int getTotalRecursos() {
        return recursos.size();
    }

    /*
    Obtiene el número de recursos disponibles
    @return cantidad de recursos disponibles
    */
    public int getTotalRecursosDisponibles() {
        return (int) recursos.stream()
                .filter(Recurso::estaDisponible)
                .count();
    }
}