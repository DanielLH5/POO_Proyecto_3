package gestores;

import model.Voluntario;
import exceptions.VoluntarioNoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
Gestor responsable de la administración de perfiles de voluntarios (RF-02).
*/
public class GestorVoluntarios {
    private List<Voluntario> voluntarios;

    public GestorVoluntarios() {
        this.voluntarios = new ArrayList<>();
    }

    /* Registra un nuevo voluntario en el sistema. */
    public void registrarVoluntario(Voluntario voluntario) {
        if (voluntario != null && !existeVoluntario(voluntario.getId())) {
            voluntarios.add(voluntario);
        }
    }

    /* Busca un voluntario por su identificador único. */
    public Voluntario buscarVoluntarioPorId(String id) {
        return voluntarios.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /* Verifica si un voluntario existe por su ID. */
    public boolean existeVoluntario(String id) {
        return buscarVoluntarioPorId(id) != null;
    }
}