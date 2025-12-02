package persistence;

import exceptions.PersistenciaException;
import java.io.*;

public class GestorAlmacenamiento {
    public void guardar(String archivo, Object datos) throws PersistenciaException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(datos);
        } catch (IOException e) {
            throw new PersistenciaException("Error guardando " + archivo, e);
        }
    }

    public Object cargar(String archivo) throws PersistenciaException {
        if (!new File(archivo).exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Error cargando " + archivo, e);
        }
    }

    public boolean existeArchivo(String archivo) {
        return new File(archivo).exists();
    }
}