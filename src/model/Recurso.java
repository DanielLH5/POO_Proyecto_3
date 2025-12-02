package model;

import java.io.Serializable;

public class Recurso implements Serializable {
    private String id;
    private String nombre;
    private String categoria;  // Cambié "tipo" por "categoria" para coincidir con GestorRecursos
    private int stockActual;
    private int umbralAlerta;
    private int capacidadMaxima;  // Falta
    private String unidadMedida;  // Falta

    public Recurso() {
        this.umbralAlerta = 10;
        this.capacidadMaxima = 100;
        this.unidadMedida = "Unidad";
    }

    // Constructor completo
    public Recurso(String id, String nombre, String categoria, int capacidadMaxima, int umbralAlerta, String unidadMedida) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.capacidadMaxima = capacidadMaxima;
        this.umbralAlerta = umbralAlerta;
        this.unidadMedida = unidadMedida;
        this.stockActual = 0; // Inicialmente sin stock
    }

    // Constructor con stock inicial (para compatibilidad)
    public Recurso(String id, String nombre, String categoria, int stockInicial, int umbralAlerta, int capacidadMaxima, String unidadMedida) {
        this(id, nombre, categoria, capacidadMaxima, umbralAlerta, unidadMedida);
        this.stockActual = stockInicial;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getTipo() { return categoria; } // Para compatibilidad con código existente
    public int getStockActual() { return stockActual; }
    public int getUmbralAlerta() { return umbralAlerta; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public String getUnidadMedida() { return unidadMedida; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setTipo(String tipo) { this.categoria = tipo; } // Para compatibilidad
    public void setStockActual(int stockActual) {
        if (stockActual >= 0 && stockActual <= capacidadMaxima) {
            this.stockActual = stockActual;
        } else {
            throw new IllegalArgumentException("Stock debe estar entre 0 y " + capacidadMaxima);
        }
    }
    public void setUmbralAlerta(int umbralAlerta) {
        if (umbralAlerta >= 0 && umbralAlerta <= capacidadMaxima) {
            this.umbralAlerta = umbralAlerta;
        }
    }
    public void setCapacidadMaxima(int capacidadMaxima) {
        if (capacidadMaxima > 0) {
            this.capacidadMaxima = capacidadMaxima;
        }
    }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public boolean consultarDisponibilidad(int cantidadRequerida) {
        return this.stockActual >= cantidadRequerida;
    }

    public void deducirStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a deducir debe ser positiva");
        }
        if (this.stockActual >= cantidad) {
            this.stockActual -= cantidad;
            verificarAlerta();
        } else {
            throw new IllegalArgumentException("Error de stock: No se puede deducir " + cantidad + " de " + this.stockActual + ".");
        }
    }

    public void reponerStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reponer debe ser positiva");
        }
        if (this.stockActual + cantidad <= this.capacidadMaxima) {
            this.stockActual += cantidad;
        } else {
            throw new IllegalArgumentException("Excede la capacidad máxima. Capacidad: " +
                    capacidadMaxima + ", Stock actual: " + stockActual);
        }
    }

    public void verificarAlerta() {
        if (this.stockActual <= this.umbralAlerta) {
            System.out.println("[ALERTA] Stock Bajo para: " + this.nombre +
                    " (Stock actual: " + this.stockActual + ", Umbral: " + this.umbralAlerta + ")");
        }
    }

    public boolean tieneStockBajo() {
        return this.stockActual <= this.umbralAlerta;
    }

    public boolean estaAgotado() {
        return this.stockActual <= 0;
    }

    public int getStockDisponible() {
        return this.stockActual;
    }

    public int getEspacioDisponible() {
        return this.capacidadMaxima - this.stockActual;
    }

    public double getPorcentajeDisponible() {
        if (capacidadMaxima == 0) return 0.0;
        return (double) stockActual / capacidadMaxima * 100;
    }

    @Override
    public String toString() {
        return nombre + " (" + categoria + ", Stock: " + stockActual + "/" + capacidadMaxima + " " + unidadMedida + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recurso recurso = (Recurso) obj;
        return id.equals(recurso.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}