package model;

import gestores.Gestionable;

/*
Representa un recurso material disponible para las actividades de las brigadas.
Implementa la interfaz Gestionable para permitir el control de disponibilidad y cantidad del recurso en el inventario del sistema.
*/
public class Recurso implements Gestionable {
    private String id;
    private String nombre;
    private String tipo; // "material", "herramienta", "equipo", "transporte"
    private int cantidadDisponible;
    private String unidad; // "kg", "litros", "unidades", "cajas"

    public Recurso() {}

    public Recurso(String id, String nombre, String tipo, int cantidadDisponible, String unidad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidadDisponible = cantidadDisponible;
        this.unidad = unidad;
    }

    // Implementación de la interfaz Gestionable
    @Override
    public boolean estaDisponible() {
        return cantidadDisponible > 0;
    }

    @Override
    public void actualizarCantidad(int cantidad) {
        this.cantidadDisponible = cantidad;
    }

    /*
    Reduce la cantidad disponible del recurso
    */
    public void consumir(int cantidad) {
        if (cantidad <= cantidadDisponible) {
            cantidadDisponible -= cantidad;
        }
    }

    /*
    Aumenta la cantidad disponible del recurso
    */
    public void reponer(int cantidad) {
        cantidadDisponible += cantidad;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public Recurso setId(String id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Recurso setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getTipo() {
        return tipo;
    }

    public Recurso setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public Recurso setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
        return this;
    }

    public String getUnidad() {
        return unidad;
    }

    public Recurso setUnidad(String unidad) {
        this.unidad = unidad;
        return this;
    }

    @Override
    public String toString() {
        return "Recurso{" + "id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", cantidad=" + cantidadDisponible + " " + unidad + '}';
    }
}