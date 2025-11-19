package exceptions;

/*
Excepción lanzada cuando un recurso solicitado no está disponible en el inventario o no tiene la cantidad suficiente para realizar una operación.
Previene operaciones inválidas sobre recursos insuficientes.
*/
public class RecursoNoDisponibleException extends BrigadaException {

    /*
    Crea una excepción indicando que un recurso específico no está disponible
    @param recursoId identificador del recurso no disponible
    */
    public RecursoNoDisponibleException(String recursoId) {
        super("Recurso no disponible en el inventario: " + recursoId);
    }

    /*
    Crea una excepción con un mensaje personalizado sobre la indisponibilidad
    @param mensaje descripción específica del problema de disponibilidad
    */
    public RecursoNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /*
    Crea una excepción indicando cantidad insuficiente de un recurso
    @param recursoId identificador del recurso
    @param cantidadSolicitada cantidad que se intentó utilizar
    @param cantidadDisponible cantidad actual en inventario
    */
    public RecursoNoDisponibleException(String recursoId, int cantidadSolicitada, int cantidadDisponible) {
        super("Cantidad insuficiente del recurso '" + recursoId + "'. " +
                "Solicitado: " + cantidadSolicitada + ", Disponible: " + cantidadDisponible);
    }
}