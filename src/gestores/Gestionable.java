package gestores;

/*
Interfaz que define las operaciones básicas para la gestión de recursos en el sistema.
Permite un manejo polimórfico de diferentes tipos de elementos que necesitan control de disponibilidad y cantidad.
*/
public interface Gestionable {

    /*
    Verifica si el elemento está disponible para su uso
    @return true si está disponible, false en caso contrario
    */
    boolean estaDisponible();

    /*
    Actualiza la cantidad disponible del elemento
    @param cantidad nueva cantidad a establecer
    */
    void actualizarCantidad(int cantidad);
}