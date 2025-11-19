package gestores;

import model.Brigada;
import model.Voluntario;
import model.Coordinador;
import exceptions.BrigadaException;
import exceptions.VoluntarioNoEncontradoException;
import java.util.ArrayList;
import java.util.List;

/*
Gestor responsable de la administración de brigadas en el sistema.
Controla la creación, modificación, asignación de coordinadores y voluntarios a las diferentes brigadas comunitarias.
*/
public class GestorBrigadas {
    private List<Brigada> brigadas;
    private GestorVoluntarios gestorVoluntarios;

    public GestorBrigadas() {
        this.brigadas = new ArrayList<>();
    }

    /*
    Establece la referencia al gestor de voluntarios para coordinación entre módulos del sistema.
    */
    public void setGestorVoluntarios(GestorVoluntarios gestorVoluntarios) {
        this.gestorVoluntarios = gestorVoluntarios;
    }

    /*
    Crea una nueva brigada en el sistema
    @param brigada la brigada a crear
    */
    public void crearBrigada(Brigada brigada) {
        if (brigada != null && !existeBrigada(brigada.getId())) {
            brigadas.add(brigada);
        }
    }

    /*
    Obtiene todas las brigadas registradas en el sistema
    @return lista de brigadas
    */
    public List<Brigada> obtenerBrigadas() {
        return new ArrayList<>(brigadas);
    }

    /*
    Busca una brigada por su identificador único
    @param id identificador de la brigada
    @return la brigada encontrada o null si no existe
    */
    public Brigada buscarBrigadaPorId(String id) {
        return brigadas.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /*
    Agrega un voluntario a una brigada específica
    @param brigadaId identificador de la brigada
    @param voluntario voluntario a agregar
    @throws BrigadaException si la brigada no existe
    @throws VoluntarioNoEncontradoException si el voluntario no está registrado
    */
    public void agregarVoluntarioABrigada(String brigadaId, Voluntario voluntario)
            throws BrigadaException, VoluntarioNoEncontradoException {

        Brigada brigada = buscarBrigadaPorId(brigadaId);
        if (brigada == null) {
            throw new BrigadaException("Brigada no encontrada: " + brigadaId);
        }

        // Verificar que el voluntario existe en el sistema
        if (gestorVoluntarios != null && !gestorVoluntarios.existeVoluntario(voluntario.getId())) {
            throw new VoluntarioNoEncontradoException(voluntario.getId());
        }

        brigada.agregarVoluntario(voluntario);
    }

    /*
    Asigna un coordinador a una brigada
    @param brigadaId identificador de la brigada
    @param coordinador coordinador a asignar
    @throws BrigadaException si la brigada no existe
    */
    public void asignarCoordinadorABrigada(String brigadaId, Coordinador coordinador)
            throws BrigadaException {

        Brigada brigada = buscarBrigadaPorId(brigadaId);
        if (brigada == null) {
            throw new BrigadaException("Brigada no encontrada: " + brigadaId);
        }

        brigada.setCoordinador(coordinador);
    }

    /*
    Obtiene todas las brigadas de un tipo específico
    @param tipo tipo de brigada a filtrar
    @return lista de brigadas del tipo especificado
    */
    public List<Brigada> obtenerBrigadasPorTipo(String tipo) {
        // Crear una lista nueva para guardar las brigadas del tipo buscado
        List<Brigada> brigadasDelTipo = new ArrayList<>();

        // Recorrer todas las brigadas
        for (Brigada brigada : brigadas) {
            // Comparar el tipo de la brigada (ignorando mayúsculas/minúsculas)
            if (brigada.getTipo().equalsIgnoreCase(tipo)) {
                // Si coincide, agregar a la lista
                brigadasDelTipo.add(brigada);
            }
        }

        // Devolver la lista con las brigadas del tipo especificado
        return brigadasDelTipo;
    }

    /*
    Verifica si existe una brigada con el identificador especificado
    @param id identificador a verificar
    @return true si existe, false en caso contrario
    */
    private boolean existeBrigada(String id) {
        // Recorrer todas las brigadas
        for (Brigada brigada : brigadas) {
            // Si encontramos una brigada con el ID buscado
            if (brigada.getId().equals(id)) {
                return true; // La brigada existe
            }
        }
        // Si llegamos aquí, no encontramos la brigada
        return false;
    }
    /*
    Obtiene el número total de brigadas registradas
    @return cantidad de brigadas
    */
    public int getTotalBrigadas() {
        return brigadas.size();
    }
}