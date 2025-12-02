package gestores;

import model.Usuario;
import model.Voluntario;
import model.Coordinador;
import exceptions.PersistenciaException;
import persistence.GestorAlmacenamiento;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {
    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    private GestorAlmacenamiento gestor;
    private List<Usuario> usuarios;

    public GestorUsuarios() {
        this.gestor = new GestorAlmacenamiento();
        cargarUsuarios();
    }

    @SuppressWarnings("unchecked")
    private void cargarUsuarios() {
        try {
            Object datos = gestor.cargar(ARCHIVO_USUARIOS);
            if (datos != null) {
                usuarios = (List<Usuario>) datos;
            } else {
                usuarios = new ArrayList<>();
            }
        } catch (PersistenciaException e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
            usuarios = new ArrayList<>();
        }
    }

    private void guardarUsuarios() throws PersistenciaException {
        gestor.guardar(ARCHIVO_USUARIOS, usuarios);
    }

    // Métodos para trabajar con tus gestores existentes

    public List<Usuario> obtenerTodosUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public List<Voluntario> obtenerVoluntarios() {
        List<Voluntario> voluntarios = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Voluntario) {
                voluntarios.add((Voluntario) usuario);
            }
        }
        return voluntarios;
    }

    public List<Coordinador> obtenerCoordinadores() {
        List<Coordinador> coordinadores = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Coordinador) {
                coordinadores.add((Coordinador) usuario);
            }
        }
        return coordinadores;
    }

    public void guardarVoluntario(Voluntario voluntario) throws PersistenciaException {
        // Verificar si ya existe
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(voluntario.getId())) {
                usuarios.set(i, voluntario); // Actualizar
                guardarUsuarios();
                return;
            }
        }
        // Si no existe, agregar
        usuarios.add(voluntario);
        guardarUsuarios();
    }

    public void guardarVoluntarioList(List<Voluntario> nuevaListaVoluntarios) throws PersistenciaException {
        // Primero, eliminar todos los voluntarios actuales
        usuarios.removeIf(usuario -> usuario instanceof Voluntario);

        // Agregar la nueva lista de voluntarios
        usuarios.addAll(nuevaListaVoluntarios);

        // Guardar los cambios
        guardarUsuarios();
    }

    public void registrarVoluntario(Voluntario voluntario) throws PersistenciaException {
        // Verificar si ya existe
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(voluntario.getId())) {
                usuarios.set(i, voluntario); // Actualizar
                guardarUsuarios();
                return;
            }
        }
        // Si no existe, agregar
        usuarios.add(voluntario);
        guardarUsuarios();
    }

    public void guardarCoordinador(Coordinador coordinador) throws PersistenciaException {
        // Verificar si ya existe
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(coordinador.getId())) {
                usuarios.set(i, coordinador); // Actualizar
                guardarUsuarios();
                return;
            }
        }
        // Si no existe, agregar
        usuarios.add(coordinador);
        guardarUsuarios();
    }

    public Usuario autenticarUsuario(String email, String password) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email) &&
                    usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean existeEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // NUEVOS MÉTODOS PARA BUSCAR VOLUNTARIOS

    public Voluntario buscarVoluntarioPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Voluntario && usuario.getId().equals(id)) {
                return (Voluntario) usuario;
            }
        }
        return null;
    }

    public Voluntario buscarVoluntarioPorEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Voluntario &&
                    usuario.getEmail().equalsIgnoreCase(email)) {
                return (Voluntario) usuario;
            }
        }
        return null;
    }

    public Voluntario buscarVoluntarioPorNombre(String nombre) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Voluntario &&
                    usuario.getNombre().equalsIgnoreCase(nombre)) {
                return (Voluntario) usuario;
            }
        }
        return null;
    }

    public Coordinador buscarCoordinadorPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Coordinador && usuario.getId().equals(id)) {
                return (Coordinador) usuario;
            }
        }
        return null;
    }

    public Coordinador buscarCoordinadorPorEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Coordinador &&
                    usuario.getEmail().equalsIgnoreCase(email)) {
                return (Coordinador) usuario;
            }
        }
        return null;
    }

    public boolean existeVoluntarioConId(String id) {
        return buscarVoluntarioPorId(id) != null;
    }

    public boolean existeCoordinadorConId(String id) {
        return buscarCoordinadorPorId(id) != null;
    }

    // Método para generar ID único para nuevo voluntario
    public String generarNuevoIdVoluntario() {
        int maxId = 0;
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Voluntario) {
                try {
                    String idStr = usuario.getId().replace("VOL-", "");
                    int idNum = Integer.parseInt(idStr);
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar IDs que no sigan el formato
                }
            }
        }
        return String.format("VOL-%03d", maxId + 1);
    }

    // Método para generar ID único para nuevo coordinador
    public String generarNuevoIdCoordinador() {
        int maxId = 0;
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Coordinador) {
                try {
                    String idStr = usuario.getId().replace("COORD-", "");
                    int idNum = Integer.parseInt(idStr);
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar IDs que no sigan el formato
                }
            }
        }
        return String.format("COORD-%03d", maxId + 1);
    }
}