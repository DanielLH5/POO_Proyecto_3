package gestores;

import model.Voluntario;
import exceptions.VoluntarioNoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
Gestor especializado en la administración de voluntarios del sistema.
Maneja el registro, búsqueda, actualización de disponibilidad y filtrado de voluntarios según diferentes criterios.
*/
public class GestorVoluntarios {
    private List<Voluntario> voluntarios;

    public GestorVoluntarios() {
        this.voluntarios = new ArrayList<>();
    }

    /*
    Registra un nuevo voluntario en el sistema
    @param voluntario el voluntario a registrar
    */
    public void registrarVoluntario(Voluntario voluntario) {
        if (voluntario != null && !existeVoluntario(voluntario.getId())) {
            voluntarios.add(voluntario);
        }
    }

    /*
    Obtiene todos los voluntarios registrados en el sistema
    @return lista de voluntarios
    */
    public List<Voluntario> obtenerVoluntarios() {
        return new ArrayList<>(voluntarios);
    }

    /*
    Busca un voluntario por su identificador único
    @param id identificador del voluntario
    @return el voluntario encontrado o null si no existe
    */
    public Voluntario buscarVoluntarioPorId(String id) {
        return voluntarios.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /*
    Obtiene la lista de voluntarios disponibles para actividades
    @return lista de voluntarios con disponibilidad activa
    */
    public List<Voluntario> buscarVoluntariosDisponibles() {
        return voluntarios.stream()
                .filter(Voluntario::isDisponible)
                .collect(Collectors.toList());
    }

    /*
    Actualiza la información de disponibilidad de un voluntario
    @param voluntarioId identificador del voluntario
    @param disponible nuevo estado de disponibilidad
    @param diasDisponibles días en que está disponible
    @throws VoluntarioNoEncontradoException si el voluntario no existe
    */
    public void actualizarDisponibilidad(String voluntarioId, boolean disponible, String diasDisponibles)
            throws VoluntarioNoEncontradoException {

        Voluntario voluntario = buscarVoluntarioPorId(voluntarioId);
        if (voluntario == null) {
            throw new VoluntarioNoEncontradoException(voluntarioId);
        }

        voluntario.setDisponible(disponible);
        voluntario.setDiasDisponibles(diasDisponibles);
    }

    /*
    Busca voluntarios que tengan una habilidad específica
    @param habilidad habilidad a buscar
    @return lista de voluntarios con la habilidad especificada
    */
    public List<Voluntario> buscarVoluntariosPorHabilidad(String habilidad) {
        return voluntarios.stream()
                .filter(v -> v.getHabilidades().contains(habilidad))
                .collect(Collectors.toList());
    }

    /*
    Verifica si existe un voluntario con el identificador especificado
    @param id identificador a verificar
    @return true si existe, false en caso contrario
    */
    public boolean existeVoluntario(String id) {
        return voluntarios.stream().anyMatch(v -> v.getId().equals(id));
    }

    /*
    Obtiene el número total de voluntarios registrados
    @return cantidad de voluntarios
    */
    public int getTotalVoluntarios() {
        return voluntarios.size();
    }

    /*
    Obtiene el número de voluntarios disponibles
    @return cantidad de voluntarios disponibles
    */
    public int getTotalVoluntariosDisponibles() {
        return (int) voluntarios.stream()
                .filter(Voluntario::isDisponible)
                .count();
    }

    /*
    Convocar voluntarios urgentes - RF-09
    @param mensaje el mensaje de la convocatoria urgente
    @return número de voluntarios notificados
    */
    public int convocarVoluntariosUrgentes(String mensaje) {
        int voluntariosNotificados = 0;

        for (Voluntario voluntario : voluntarios) {
            if (voluntario.isDisponible()) {
                voluntariosNotificados++;
            }
        }

        return voluntariosNotificados;
    }
}