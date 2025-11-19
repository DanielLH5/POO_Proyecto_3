package exceptions;

/*
Excepción lanzada cuando se intenta realizar una operación sobre una actividad que no existe en el sistema o no ha sido planificada correctamente.
Garantiza que solo se operen actividades válidas y registradas.
*/
public class ActividadNoPlanificadaException extends BrigadaException {

    /*
    Crea una excepción indicando que una actividad específica no fue encontrada
    @param actividadId identificador de la actividad que no pudo ser localizada
    */
    public ActividadNoPlanificadaException(String actividadId) {
        super("Actividad no planificada o no encontrada en el sistema: " + actividadId);
    }

    /*
    Crea una excepción con un mensaje personalizado
    @param mensaje descripción específica del error
    */
    public ActividadNoPlanificadaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /*
    Crea una excepción indicando que la actividad no está en estado válido para la operación
    @param actividadId identificador de la actividad
    @param estadoActual estado actual de la actividad
    @param estadoRequerido estado requerido para la operación
    */
    public ActividadNoPlanificadaException(String actividadId, String estadoActual, String estadoRequerido) {
        super("Actividad '" + actividadId + "' no está en estado válido. " + "Estado actual: " + estadoActual + ", Estado requerido: " + estadoRequerido);
    }
}