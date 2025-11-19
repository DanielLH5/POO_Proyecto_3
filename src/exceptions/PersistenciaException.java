package exceptions;

/*
Excepción lanzada cuando ocurren errores durante las operaciones de persistencia de datos (lectura/escritura en archivos, base de datos, etc.).
Encapsula errores de E/S y problemas de acceso a almacenamiento.
*/
public class PersistenciaException extends BrigadaException {

    /*
    Crea una excepción para errores de persistencia con operación y recurso específicos
    @param operacion tipo de operación que falló (guardar, cargar, etc.)
    @param recurso nombre del recurso afectado (archivo, tabla, etc.)
    */
    public PersistenciaException(String operacion, String recurso) {
        super("Error durante " + operacion + " del recurso: " + recurso);
    }

    /*
    Crea una excepción con mensaje personalizado
    @param mensaje descripción específica del error de persistencia
    */
    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    /*
    Crea una excepción para errores de archivo específicos
    @param archivo nombre del archivo que causó el error
    @param operacion operación que se intentaba realizar
    @param causa excepción original de E/S
    */
    public PersistenciaException(String archivo, String operacion, Throwable causa) {
        super("Error al " + operacion + " el archivo: " + archivo, causa);
    }

    /*
    Crea una excepción para errores de serialización/deserialización
    @param recurso recurso que no pudo ser serializado/deserializado
    @param causa excepción original de serialización
    */
    public PersistenciaException(String recurso, Throwable causa) {
        super("Error de serialización/deserialización para: " + recurso, causa);
    }
}