package exceptions;

/*
Excepción base para todas las excepciones específicas del sistema de brigadas.
Proporciona una jerarquía común para el manejo de errores y facilita
el tratamiento uniforme de excepciones en toda la aplicación.
*/
public class BrigadaException extends Exception {

    /*
    Crea una nueva excepción con un mensaje descriptivo
    @param mensaje descripción del error ocurrido
    */
    public BrigadaException(String mensaje) {
        super(mensaje);
    }

    /*
    Crea una nueva excepción con un mensaje y una causa subyacente
    @param mensaje descripción del error ocurrido
    @param causa excepción que originó este error
    */
    public BrigadaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /*
    Crea una nueva excepción con una causa subyacente
    @param causa excepción que originó este error
    */
    public BrigadaException(Throwable causa) {
        super(causa);
    }
}