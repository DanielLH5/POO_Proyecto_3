package persistence;

import exceptions.PersistenciaException;
import java.io.*;

/*
Gestor simple para guardar y cargar datos en archivos binarios.
Proporciona operaciones básicas de persistencia para el sistema de brigadas.
*/
public class GestorAlmacenamiento {

    /*
    Guarda un objeto en un archivo binario
    @param archivo nombre del archivo donde guardar
    @param datos objeto a serializar
    @throws PersistenciaException si hay error al guardar
    */
    public void guardar(String archivo, Object datos) throws PersistenciaException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(datos);
            System.out.println("Guardado en: " + archivo);
        } catch (IOException e) {
            throw new PersistenciaException("Error guardando " + archivo, e);
        }
    }

    /*
    Carga un objeto desde un archivo binario
    @param archivo nombre del archivo a cargar
    @return objeto cargado desde el archivo
    @throws PersistenciaException si hay error al cargar
    */
    public Object cargar(String archivo) throws PersistenciaException {
        // Si el archivo no existe, retorna null
        if (!new File(archivo).exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Object datos = ois.readObject();
            System.out.println("Cargado desde: " + archivo);
            return datos;
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Error cargando " + archivo, e);
        }
    }

    /*
    Verifica si un archivo existe
    @param archivo nombre del archivo
    @return true si existe, false si no
    */
    public boolean existeArchivo(String archivo) {
        return new File(archivo).exists();
    }
}