package gestores;

import model.Actividad;
import model.Voluntario;
import model.Recurso;
import model.Beneficiario;
import exceptions.ActividadNoPlanificadaException;
import exceptions.BrigadaException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
Gestor encargado de la planificación, ejecución y seguimiento de actividades.
Coordina la asignación de voluntarios y recursos, así como el registro de resultados de las actividades realizadas.
*/
public class GestorActividades {
    private List<Actividad> actividades;
    private GestorVoluntarios gestorVoluntarios;
    private GestorRecursos gestorRecursos;

    public GestorActividades() {
        this.actividades = new ArrayList<>();
    }

    /*
    Establece la referencia al gestor de voluntarios
    */
    public void setGestorVoluntarios(GestorVoluntarios gestorVoluntarios) {
        this.gestorVoluntarios = gestorVoluntarios;
    }

    /*
    Establece la referencia al gestor de recursos
    */
    public void setGestorRecursos(GestorRecursos gestorRecursos) {
        this.gestorRecursos = gestorRecursos;
    }

    /*
    Planifica una nueva actividad en el sistema
    @param actividad la actividad a planificar
    */
    public void planificarActividad(Actividad actividad) {
        if (actividad != null && !existeActividad(actividad.getId())) {
            actividades.add(actividad);
        }
    }

    /*
    Obtiene todas las actividades planificadas en el sistema
    @return lista de actividades
    */
    public List<Actividad> obtenerActividades() {
        return new ArrayList<>(actividades);
    }

    /*
    Busca una actividad por su identificador único
    @param id identificador de la actividad
    @return la actividad encontrada o null si no existe
    */
    public Actividad buscarActividadPorId(String id) {
        return actividades.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /*
    Asigna un voluntario a una actividad específica
    @param actividadId identificador de la actividad
    @param voluntario voluntario a asignar
    @throws ActividadNoPlanificadaException si la actividad no existe
    */
    public void asignarVoluntarioAActividad(String actividadId, Voluntario voluntario) throws ActividadNoPlanificadaException {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            throw new ActividadNoPlanificadaException(actividadId);
        }

        actividad.asignarVoluntario(voluntario);
    }

    /*
    Asigna un recurso a una actividad específica
    @param actividadId identificador de la actividad
    @param recurso recurso a asignar
    @param cantidad cantidad del recurso a utilizar
    @throws ActividadNoPlanificadaException si la actividad no existe
    @throws BrigadaException si el recurso no está disponible
    */
    public void asignarRecursoAActividad(String actividadId, Recurso recurso, int cantidad) throws ActividadNoPlanificadaException, BrigadaException {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            throw new ActividadNoPlanificadaException(actividadId);
        }

        if (!recurso.estaDisponible() || recurso.getCantidadDisponible() < cantidad) {
            throw new BrigadaException("Recurso no disponible en cantidad suficiente: " + recurso.getNombre());
        }

        actividad.asignarRecurso(recurso);
        recurso.consumir(cantidad);
    }

    /*
    Registra los resultados de una actividad realizada
    @param actividadId identificador de la actividad
    @param resultado descripción textual de los resultados
    @param personasBeneficiadas número de personas beneficiadas
    @throws ActividadNoPlanificadaException si la actividad no existe
    */
    public void registrarResultadoActividad(String actividadId, String resultado, int personasBeneficiadas) throws ActividadNoPlanificadaException {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            throw new ActividadNoPlanificadaException(actividadId);
        }

        actividad.setResultado(resultado);
        actividad.setPersonasBeneficiadas(personasBeneficiadas);
    }

    /*
    Agrega un beneficiario a una actividad
    @param actividadId identificador de la actividad
    @param beneficiario beneficiario a agregar
    @throws ActividadNoPlanificadaException si la actividad no existe
    */
    public void agregarBeneficiarioAActividad(String actividadId, Beneficiario beneficiario) throws ActividadNoPlanificadaException {
        Actividad actividad = buscarActividadPorId(actividadId);
        if (actividad == null) {
            throw new ActividadNoPlanificadaException(actividadId);
        }

        actividad.agregarBeneficiario(beneficiario);
    }

    /*
    Obtiene el historial de actividades realizadas (con resultados)
    @return lista de actividades con resultados registrados
    */
    public List<Actividad> obtenerHistorialActividades() {
        // Crear una lista nueva para guardar las actividades con resultados
        List<Actividad> actividadesConResultado = new ArrayList<>();

        // Recorrer todas las actividades una por una
        for (Actividad actividad : actividades) {
            // Verificar si la actividad tiene resultado
            boolean tieneResultado = actividad.getResultado() != null;
            boolean resultadoNoVacio = tieneResultado && !actividad.getResultado().isEmpty();

            // Si tiene resultado, agregarla a la lista
            if (resultadoNoVacio) {
                actividadesConResultado.add(actividad);
            }
        }

        // Devolver la lista con actividades que tienen resultados
        return actividadesConResultado;
    }

    /*
    Verifica si existe una actividad con el identificador especificado
    @param id identificador a verificar
    @return true si existe, false en caso contrario
    */
    private boolean existeActividad(String id) {
        // Recorrer todas las actividades
        for (Actividad actividad : actividades) {
            // Comparar el ID de cada actividad con el ID buscado
            if (actividad.getId().equals(id)) {
                // Si encontramos una coincidencia, retornar true inmediatamente
                return true;
            }
        }
        // Si llegamos aquí, no encontramos ninguna actividad con ese ID
        return false;
    }

    /*
    Obtiene el número total de actividades planificadas
    @return cantidad de actividades
    */
    public int getTotalActividades() {
        return actividades.size();
    }
}