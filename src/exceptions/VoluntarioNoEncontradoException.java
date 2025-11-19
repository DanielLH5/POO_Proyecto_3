package exceptions;

/*
Excepción lanzada cuando se intenta realizar una operación sobre un voluntario que no existe en el sistema o no puede ser localizado.
Indica problemas de integridad referencial en las operaciones con voluntarios.
*/
public class VoluntarioNoEncontradoException extends BrigadaException {

    /*
    Crea una excepción indicando que un voluntario específico no fue encontrado
    @param voluntarioId identificador del voluntario que no pudo ser localizado
    */
    public VoluntarioNoEncontradoException(String voluntarioId) {
        super("Voluntario no encontrado en el sistema: " + voluntarioId);
    }

    /*
    Crea una excepción con un mensaje personalizado
    @param mensaje descripción específica del error
    */
    public VoluntarioNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}