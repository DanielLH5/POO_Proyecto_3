import gestores.*;
import model.*;
import exceptions.*;

public class Main {
    public static void main(String[] args) {
        try {
            // =============================================
            // INICIALIZACIÓN DE GESTORES (esto va en la GUI)
            // =============================================
            GestorBrigadas gestorBrigadas = new GestorBrigadas();
            GestorVoluntarios gestorVoluntarios = new GestorVoluntarios();
            GestorActividades gestorActividades = new GestorActividades();
            GestorRecursos gestorRecursos = new GestorRecursos();
            GestorReportes gestorReportes = new GestorReportes();

            // Configurar relaciones entre gestores
            gestorBrigadas.setGestorVoluntarios(gestorVoluntarios);
            gestorActividades.setGestorVoluntarios(gestorVoluntarios);
            gestorActividades.setGestorRecursos(gestorRecursos);
            gestorReportes.setGestores(gestorBrigadas, gestorVoluntarios, gestorActividades, gestorRecursos);

            // =============================================
            // EJEMPLOS DE USO PARA LA GUI
            // =============================================

            // RF-01: Registrar Brigada - GUI: formulario de creación
            Coordinador coord = new Coordinador("coord001", "Ana García", "8888-8888", "ana@email.com", "Zona Norte");
            Brigada brigada = new Brigada("brig001", "Brigada Alimentos", "Alimentación", coord);
            gestorBrigadas.crearBrigada(brigada);

            // RF-02: Registrar Voluntario - GUI: formulario de registro
            Voluntario vol = new Voluntario("vol001", "Carlos Méndez", "7777-7777", "carlos@email.com");
            gestorVoluntarios.registrarVoluntario(vol);

            // RF-03: Asociar Voluntario a Brigada - GUI: selección de lista
            gestorBrigadas.agregarVoluntarioABrigada("brig001", vol);

            // RF-04: Planificar Actividad - GUI: formulario de actividad
            Actividad actividad = new Actividad("act001", "Distribución alimentos", "20/12/2024", "Comunidad X", "Entregar kits");
            gestorActividades.planificarActividad(actividad);

            // RF-05: Asignar Voluntarios a Actividad - GUI: checklist de voluntarios
            gestorActividades.asignarVoluntarioAActividad("act001", vol);

            // RF-06: Asignar Recursos - GUI: selección de recursos
            Recurso recurso = new Recurso("rec001", "Kits alimentos", "material", 100, "unidades");
            gestorRecursos.agregarRecurso(recurso);
            gestorActividades.asignarRecursoAActividad("act001", recurso, 50);

            // RF-07: Registrar Resultados - GUI: formulario de resultados
            gestorActividades.registrarResultadoActividad("act001", "Distribución exitosa", 50);

            // RF-08: Consultar Historial - GUI: tabla de actividades
            System.out.println("Historial:");
            for (Actividad a : gestorActividades.obtenerHistorialActividades()) {
                System.out.println("- " + a.getDescripcion());
            }

            // RF-09: Convocar Voluntarios - GUI: botón de convocatoria
            int notificados = gestorVoluntarios.convocarVoluntariosUrgentes("Mensaje urgente");
            System.out.println("Notificados: " + notificados);

            // RF-10: Consultar Recursos - GUI: inventario/stock
            System.out.println("Recursos disponibles:");
            for (Recurso r : gestorRecursos.consultarRecursosDisponibles()) {
                System.out.println("- " + r.getNombre() + ": " + r.getCantidadDisponible());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}