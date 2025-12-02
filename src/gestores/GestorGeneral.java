package gestores;

import exceptions.PersistenciaException;
import gestores.*;
import model.*;
import java.util.Date;
import java.util.List;

public class GestorGeneral {
    private GestorUsuarios gestorUsuarios;
    private GestorVoluntarios gestorVoluntarios;
    private GestorBrigadas gestorBrigadas;
    private GestorActividades gestorActividades;
    private GestorRecursos gestorRecursos;

    public GestorGeneral() {
        // 1. Inicializar persistencia
        gestorUsuarios = new GestorUsuarios();

        // 2. Inicializar gestores de negocio
        gestorVoluntarios = new GestorVoluntarios();
        gestorBrigadas = new GestorBrigadas(gestorVoluntarios);
        gestorActividades = new GestorActividades(gestorBrigadas, gestorVoluntarios);
        gestorRecursos = new GestorRecursos();

        // 3. Sincronizar datos de persistencia con gestores
        sincronizarDatos();
    }

    private void sincronizarDatos() {
        // Cargar voluntarios desde persistencia al gestor de voluntarios
        for (Voluntario voluntario : gestorUsuarios.obtenerVoluntarios()) {
            gestorVoluntarios.registrarVoluntario(voluntario);
        }

        // Aquí podrías cargar brigadas, actividades, etc. de archivos similares
    }

    // En la clase GestorGeneral, añade este método:

    public boolean actualizarVoluntario(Voluntario voluntarioActualizado) {
        try {
            // Buscar el voluntario en la persistencia y actualizarlo
            List<Voluntario> voluntarios = gestorUsuarios.obtenerVoluntarios();

            for (int i = 0; i < voluntarios.size(); i++) {
                if (voluntarios.get(i).getId().equals(voluntarioActualizado.getId())) {
                    // Actualizar el voluntario
                    voluntarios.set(i, voluntarioActualizado);

                    // También actualizar en la lista general de usuarios
                    List<Usuario> usuarios = gestorUsuarios.obtenerTodosUsuarios();
                    for (int j = 0; j < usuarios.size(); j++) {
                        if (usuarios.get(j).getId().equals(voluntarioActualizado.getId())) {
                            usuarios.set(j, voluntarioActualizado);
                            break;
                        }
                    }

                    // Guardar cambios
                    // Necesitarías un método en GestorUsuariosPersistence para guardar toda la lista
                    // gestorUsuarios.guardarTodosUsuarios(usuarios);

                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            System.err.println("Error al actualizar voluntario: " + e.getMessage());
            return false;
        }
    }

    public GestorUsuarios getGestorUsuarios() {
        return gestorUsuarios;
    }

    // Métodos para el sistema de registro/login
    public boolean registrarVoluntario(String nombre, String telefono, String email,
                                       String password, String habilidades, String diasDisponibles)
            throws PersistenciaException {

        if (gestorUsuarios.existeEmail(email)) {
            throw new PersistenciaException("El email ya está registrado");
        }

        Voluntario voluntario = new Voluntario(nombre, telefono, email,
                password, habilidades, diasDisponibles);

        // Guardar en persistencia
        gestorUsuarios.guardarVoluntario(voluntario);

        // Registrar en gestor de negocio
        gestorVoluntarios.registrarVoluntario(voluntario);

        return true;
    }

    public boolean registrarCoordinador(String nombre, String telefono, String email,
                                        String password, String areaResponsabilidad)
            throws PersistenciaException {

        if (gestorUsuarios.existeEmail(email)) {
            throw new PersistenciaException("El email ya está registrado");
        }

        Coordinador coordinador = new Coordinador(nombre, telefono, email,
                password, areaResponsabilidad);

        // Guardar en persistencia
        gestorUsuarios.guardarCoordinador(coordinador);

        return true;
    }

    public Usuario autenticarUsuario(String email, String password) {
        return gestorUsuarios.autenticarUsuario(email, password);
    }

    // Getters para los gestores de negocio
    public GestorVoluntarios getGestorVoluntarios() { return gestorVoluntarios; }
    public GestorBrigadas getGestorBrigadas() { return gestorBrigadas; }
    public GestorActividades getGestorActividades() { return gestorActividades; }
    public GestorRecursos getGestorRecursos() { return gestorRecursos; }
}