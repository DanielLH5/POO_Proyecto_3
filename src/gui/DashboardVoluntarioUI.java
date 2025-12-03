package gui;

import model.*;
import gestores.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardVoluntarioUI extends JFrame {

    // ============================================
    // CONSTANTES DE COLORES
    // ============================================
    private static final Color PRIMARY_BLUE = new Color(25, 118, 210);
    private static final Color ACTIVE_MENU_BG = new Color(230, 240, 255);
    private static final Color BACKGROUND_GRAY = new Color(248, 248, 248);
    private static final Color CARD_GREEN = new Color(76, 175, 80);
    private static final Color CARD_PURPLE = new Color(156, 39, 176);
    private static final Color CARD_ORANGE = new Color(255, 152, 0);
    private static final Color CARD_RED = new Color(244, 67, 54);
    private static final Color TEXT_GRAY = new Color(128, 128, 128);
    private static final Color BORDER_GRAY = new Color(224, 224, 224);

    // ============================================
    // COMPONENTES PRINCIPALES
    // ============================================
    private Voluntario voluntario;
    private GestorGeneral gestorGeneral;
    private GestorActividades gestorActividades;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel brigadasPanel;
    private JComboBox<String> filtroBrigadasEstadoCombo;
    private JComboBox<String> filtroBrigadasZonaCombo;
    private JComboBox<String> filtroBrigadasTipoCombo;

    // Paneles
    private JPanel inicioPanel;
    private JPanel perfilPanel;
    private JPanel actividadesPanel;

    // Componentes de perfil
    private JTextField perfilNombreField;
    private JTextField perfilTelefonoField;
    private JTextArea perfilHabilidadesArea;
    private JCheckBox[] checkBoxDias;

    // Filtros de actividades
    private JComboBox<String> filtroTipoCombo;
    private JComboBox<String> filtroUbicacionCombo;
    private JComboBox<String> filtroEstadoCombo;

    // ============================================
    // CONSTRUCTOR Y CONFIGURACIÓN INICIAL
    // ============================================
    public DashboardVoluntarioUI(Voluntario voluntario, GestorGeneral gestorGeneral) {
        this.voluntario = voluntario;
        this.gestorGeneral = gestorGeneral;
        this.gestorActividades = gestorGeneral.getGestorActividades();
        configurarVentana();
        initUI();
    }

    private void configurarVentana() {
        setTitle("Brigadas Comunitarias - Voluntario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void initUI() {
        // 1. Sidebar (Oeste)
        add(crearSidebar(), BorderLayout.WEST);

        // 2. Header (Norte)
        add(crearHeader(), BorderLayout.NORTH);

        // 3. Sistema de paneles intercambiables (Centro)
        configurarSistemaPaneles();

        setVisible(true);
    }

    // ============================================
    // HEADER Y SIDEBAR
    // ============================================
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Logo/Título
        JLabel logo = new JLabel("Brigadas Comunitarias");
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(logo, BorderLayout.WEST);

        // Panel de usuario
        header.add(crearPanelUsuario(), BorderLayout.EAST);
        return header;
    }

    private JPanel crearPanelUsuario() {
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);

        // Avatar con nombre
        userPanel.add(crearAvatarUsuario());
        return userPanel;
    }

    private List<Actividad> obtenerActividadesParaMostrar() {
        // Obtener todas las actividades pero excluir las completadas
        List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();
        List<Actividad> actividadesFiltradas = new ArrayList<>();

        Date ahora = new Date();

        for (Actividad actividad : todasActividades) {
            String estado = actividad.determinarEstado();
            // Excluir actividades completadas de la lista principal
            if (!estado.equals("Completada")) {
                actividadesFiltradas.add(actividad);
            }
        }

        return actividadesFiltradas;
    }

    private JPanel crearAvatarUsuario() {
        JPanel avatarPanel = new JPanel(new BorderLayout(5, 0));
        avatarPanel.setOpaque(false);
        avatarPanel.setBorder(BorderFactory.createLineBorder(BORDER_GRAY, 1));
        avatarPanel.setBackground(new Color(240, 240, 240));
        avatarPanel.setPreferredSize(new Dimension(120, 30));
        avatarPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        avatarPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("perfil");
            }
        });

        JLabel avatarName = new JLabel(voluntario.getNombre(), SwingConstants.RIGHT);
        avatarName.setFont(new Font("Arial", Font.BOLD, 12));
        avatarPanel.add(avatarName, BorderLayout.CENTER);

        JLabel avatarRole = new JLabel("Voluntario", SwingConstants.RIGHT);
        avatarRole.setFont(new Font("Arial", Font.PLAIN, 10));
        avatarRole.setForeground(Color.DARK_GRAY);
        avatarPanel.add(avatarRole, BorderLayout.SOUTH);

        return avatarPanel;
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_GRAY));

        sidebar.add(Box.createVerticalStrut(20));

        // Items del menú
        JPanel inicioItem = crearItemMenu("Inicio", "🏠", true);
        JPanel perfilItem = crearItemMenu("Perfil", "👤", false);
        JPanel actividadesItem = crearItemMenu("Actividades", "📅", false);
        JPanel brigadasItem = crearItemMenu("Mis Brigadas", "🏢", false);
        JPanel historialItem = crearItemMenu("Historial", "📊", false);

        // Configurar listeners - pasar los 5 items
        configurarListenersMenu(inicioItem, perfilItem, actividadesItem, brigadasItem, historialItem);

        sidebar.add(inicioItem);
        sidebar.add(perfilItem);
        sidebar.add(actividadesItem);
        sidebar.add(brigadasItem);
        sidebar.add(historialItem);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(crearBotonCerrarSesion());
        sidebar.add(Box.createVerticalStrut(20));

        return sidebar;
    }

    private JPanel crearItemMenu(String texto, String icono, boolean activo) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (activo) {
            item.setBackground(ACTIVE_MENU_BG);
            item.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, PRIMARY_BLUE));
        } else {
            item.setBackground(Color.WHITE);
            item.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        }

        JLabel iconLabel = new JLabel(icono);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 16));
        iconLabel.setForeground(activo ? PRIMARY_BLUE : TEXT_GRAY);
        item.add(iconLabel);

        JLabel textLabel = new JLabel(texto);
        textLabel.setFont(new Font("Arial", activo ? Font.BOLD : Font.PLAIN, 14));
        textLabel.setForeground(activo ? PRIMARY_BLUE : Color.BLACK);
        item.add(textLabel);

        return item;
    }

    private void configurarListenersMenu(JPanel inicioItem, JPanel perfilItem, JPanel actividadesItem,
                                         JPanel brigadasItem, JPanel historialItem) {
        inicioItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("inicio");
                actualizarEstiloMenu(inicioItem, perfilItem, actividadesItem, brigadasItem, historialItem,
                        true, false, false, false, false);
            }
        });

        perfilItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("perfil");
                actualizarEstiloMenu(inicioItem, perfilItem, actividadesItem, brigadasItem, historialItem,
                        false, true, false, false, false);
            }
        });

        actividadesItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("actividades");
                actualizarEstiloMenu(inicioItem, perfilItem, actividadesItem, brigadasItem, historialItem,
                        false, false, true, false, false);
            }
        });

        brigadasItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("brigadas");
                actualizarEstiloMenu(inicioItem, perfilItem, actividadesItem, brigadasItem, historialItem,
                        false, false, false, true, false);
            }
        });

        historialItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(DashboardVoluntarioUI.this,
                        "Funcionalidad en desarrollo", "Próximamente", JOptionPane.INFORMATION_MESSAGE);
                // Nota: No cambiamos el estilo del menú para "historial" ya que muestra un mensaje
            }
        });
    }

    private void actualizarEstiloMenu(JPanel inicioItem, JPanel perfilItem, JPanel actividadesItem,
                                      JPanel brigadasItem, JPanel historialItem,
                                      boolean inicioActivo, boolean perfilActivo, boolean actividadesActivo,
                                      boolean brigadasActivo, boolean historialActivo) {
        actualizarEstiloItem(inicioItem, inicioActivo);
        actualizarEstiloItem(perfilItem, perfilActivo);
        actualizarEstiloItem(actividadesItem, actividadesActivo);
        actualizarEstiloItem(brigadasItem, brigadasActivo);
        actualizarEstiloItem(historialItem, historialActivo);
    }

    private void actualizarEstiloItem(JPanel item, boolean activo) {
        Component[] components = item.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().length() <= 2) { // Icono
                    label.setForeground(activo ? PRIMARY_BLUE : TEXT_GRAY);
                } else { // Texto
                    label.setFont(new Font("Arial", activo ? Font.BOLD : Font.PLAIN, 14));
                    label.setForeground(activo ? PRIMARY_BLUE : Color.BLACK);
                }
            }
        }
        item.setBackground(activo ? ACTIVE_MENU_BG : Color.WHITE);
        item.setBorder(activo ? BorderFactory.createMatteBorder(0, 4, 0, 0, PRIMARY_BLUE) :
                BorderFactory.createEmptyBorder(0, 4, 0, 0));
    }

    private JPanel crearBotonCerrarSesion() {
        JPanel logoutPanel = crearItemMenu("Cerrar Sesión", "🚪", false);
        logoutPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int confirm = JOptionPane.showConfirmDialog(
                        DashboardVoluntarioUI.this,
                        "¿Estás seguro que deseas cerrar sesión?",
                        "Cerrar Sesión",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    new gui.LoginUI();
                    dispose();
                }
            }
        });
        return logoutPanel;
    }

    // ============================================
    // SISTEMA DE PANELES INTERCAMBIABLES
    // ============================================
    private void configurarSistemaPaneles() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        inicioPanel = crearPanelInicio();
        perfilPanel = crearPanelPerfil();
        actividadesPanel = crearPanelActividades();

        cardPanel.add(inicioPanel, "inicio");
        cardPanel.add(perfilPanel, "perfil");
        cardPanel.add(actividadesPanel, "actividades");

        brigadasPanel = crearPanelBrigadas();
        cardPanel.add(brigadasPanel, "brigadas");

        cardLayout.show(cardPanel, "inicio");
        add(cardPanel, BorderLayout.CENTER);
    }

    private void cambiarPanel(String nombrePanel) {
        cardLayout.show(cardPanel, nombrePanel);

        // Actualizar datos específicos del panel
        switch (nombrePanel) {
            case "actividades":
                actualizarPanelActividades(); // Ya existe
                break;
            case "brigadas":
                actualizarPanelBrigadas(); // Ya existe
                break;
            case "inicio":
                // Actualizar panel de inicio para mostrar estadísticas actualizadas
                actualizarPanelInicio();
                break;
            case "perfil":
                // Refrescar datos del perfil
                cargarDatosPerfil();
                break;
        }
    }

    private void actualizarPanelInicio() {
        inicioPanel = crearPanelInicio();
        cardPanel.remove(0); // El índice 0 corresponde a "inicio"
        cardPanel.add(inicioPanel, "inicio");
        cardLayout.show(cardPanel, "inicio");
    }

    // ============================================
    // PANEL DE INICIO - MODIFICADO PARA TAMAÑO FIJO
    // ============================================
    private JPanel crearPanelInicio() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(BACKGROUND_GRAY);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_GRAY);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Usar un panel interno con tamaño preferido fijo
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBackground(BACKGROUND_GRAY);
        innerPanel.setPreferredSize(new Dimension(800, 600)); // Tamaño fijo

        // Encabezado
        innerPanel.add(crearEncabezadoInicio());
        innerPanel.add(Box.createVerticalStrut(30));

        // Tarjetas de estadísticas
        innerPanel.add(crearPanelEstadisticas());
        innerPanel.add(Box.createVerticalStrut(30));

        // Información personal
        innerPanel.add(crearResumenInfoPersonal());
        innerPanel.add(Box.createVerticalStrut(30));

        // Actividades próximas
        innerPanel.add(crearSeccionActividadesProximas());
        innerPanel.add(Box.createVerticalGlue());

        contentPanel.add(innerPanel);

        // ScrollPane con tamaño preferido fijo
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(900, 600)); // Tamaño fijo para el scrollpane

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private JPanel crearEncabezadoInicio() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 100)); // Tamaño máximo fijo

        JLabel welcomeTitle = new JLabel("¡Hola, " + voluntario.getNombre() + "!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(welcomeTitle);

        JLabel subtitle = new JLabel("Encuentra y únete a brigadas cerca de ti");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        return panel;
    }
    private JPanel crearPanelEstadisticas() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(800, 120));

        // 1. Calcular horas aportadas solo de actividades COMPLETADAS
        double horasAportadas = calcularHorasDeActividadesCompletadas();

        // 2. Calcular estadísticas de actividades NO completadas
        List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();
        List<Actividad> actividadesPendientes = new ArrayList<>();
        List<Actividad> actividadesNoCompletadas = new ArrayList<>();

        int misParticipaciones = 0;
        for (Actividad actividad : todasActividades) {
            String estado = actividad.determinarEstado();

            // Contar actividades pendientes (futuras)
            if (actividad.getFecha().after(new Date())) {
                actividadesPendientes.add(actividad);
            }

            // Contar mis participaciones en actividades NO completadas
            if (!estado.equals("Completada") &&
                    gestorActividades.estaVoluntarioEnActividad(actividad.getId(), voluntario.getId())) {
                misParticipaciones++;
            }

            // Para la lista de actividades no completadas
            if (!estado.equals("Completada")) {
                actividadesNoCompletadas.add(actividad);
            }
        }

        statsPanel.add(crearTarjetaEstadistica("Mis Participaciones",
                String.valueOf(misParticipaciones), "📅", PRIMARY_BLUE));

        // Usar las horas calculadas desde actividades COMPLETADAS
        statsPanel.add(crearTarjetaEstadistica("Horas Aportadas",
                String.format("%.1fh", horasAportadas), "🕒", CARD_GREEN));

        statsPanel.add(crearTarjetaEstadistica("Próximas Actividades",
                String.valueOf(actividadesPendientes.size()), "🗓️", CARD_PURPLE));

        return statsPanel;
    }

    /**
     * Método nuevo: Calcula horas solo de actividades COMPLETADAS
     */
    private double calcularHorasDeActividadesCompletadas() {
        double horasTotales = 0.0;

        try {
            List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();
            GestorResultados gestorResultados = gestorGeneral.getGestorResultados();

            for (Actividad actividad : todasActividades) {
                String estado = actividad.determinarEstado();

                // Solo considerar actividades COMPLETADAS
                if (estado.equals("Completada") &&
                        gestorActividades.estaVoluntarioEnActividad(actividad.getId(), voluntario.getId())) {

                    // Obtener resultados de la actividad
                    ResultadoActividad resultado = gestorResultados.obtenerResultadoPorActividad(actividad.getId());

                    if (resultado != null) {
                        List<String> voluntariosParticipantes = resultado.getVoluntariosParticipantes();
                        String voluntarioId = voluntario.getId();

                        // Opción 1: Si hay lista específica de participantes
                        if (voluntariosParticipantes != null && !voluntariosParticipantes.isEmpty()) {
                            if (voluntariosParticipantes.contains(voluntarioId)) {
                                horasTotales += resultado.getHorasTrabajadas();
                            }
                        }
                        // Opción 2: Si no hay lista específica, asumir participación
                        else {
                            horasTotales += resultado.getHorasTrabajadas();
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error al calcular horas de actividades completadas: " + e.getMessage());
            // En caso de error, usar método alternativo
            horasTotales = calcularHorasAlternativo();
        }

        return horasTotales;
    }

    /**
     * Método alternativo para cálculo de horas
     */
    private double calcularHorasAlternativo() {
        double horasTotales = 0.0;

        try {
            List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();

            for (Actividad actividad : todasActividades) {
                String estado = actividad.determinarEstado();

                if (estado.equals("Completada") &&
                        gestorActividades.estaVoluntarioEnActividad(actividad.getId(), voluntario.getId())) {

                    // Si no hay resultados, usar un cálculo aproximado basado en duración típica
                    // Suponer 4 horas por actividad completada si no hay información específica
                    horasTotales += 4.0; // Ajusta este valor según sea necesario
                }
            }

        } catch (Exception e) {
            System.err.println("Error en cálculo alternativo: " + e.getMessage());
            horasTotales = voluntario.getHorasAcumuladas();
        }

        return horasTotales;
    }

    /**
     * Nuevo método: Calcula las horas aportadas del voluntario a partir de resultados de actividades
     */
    private double calcularHorasAportadasDesdeResultados() {
        double horasTotales = 0.0;

        try {
            // Obtener todas las actividades
            List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();

            for (Actividad actividad : todasActividades) {
                // Verificar si el voluntario participó en esta actividad
                if (gestorActividades.estaVoluntarioEnActividad(actividad.getId(), voluntario.getId())) {

                    // Buscar si hay resultados registrados para esta actividad
                    ResultadoActividad resultado = gestorGeneral.getGestorResultados()
                            .obtenerResultadoPorActividad(actividad.getId());

                    if (resultado != null) {
                        // Si hay resultados, verificar si el voluntario está en la lista de participantes
                        List<String> voluntariosParticipantes = resultado.getVoluntariosParticipantes();
                        String voluntarioId = voluntario.getId();

                        // Opción 1: Si el resultado tiene lista específica de participantes
                        if (voluntariosParticipantes != null && !voluntariosParticipantes.isEmpty()) {
                            if (voluntariosParticipantes.contains(voluntarioId)) {
                                // Sumar horas del resultado
                                horasTotales += resultado.getHorasTrabajadas();
                            }
                        }
                        // Opción 2: Si no hay lista específica, pero el voluntario estaba inscrito
                        else {
                            // Sumar las horas totales de la actividad (asumiendo participación completa)
                            horasTotales += resultado.getHorasTrabajadas();
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error al calcular horas desde resultados: " + e.getMessage());
            // En caso de error, usar las horas acumuladas del voluntario
            horasTotales = voluntario.getHorasAcumuladas();
        }

        return horasTotales;
    }

    /**
     * Método alternativo más preciso: Calcula horas basándose en distribución proporcional
     */
    private double calcularHorasAportadasProporcionales() {
        double horasTotales = 0.0;

        try {
            List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();
            GestorResultados gestorResultados = gestorGeneral.getGestorResultados();

            for (Actividad actividad : todasActividades) {
                if (gestorActividades.estaVoluntarioEnActividad(actividad.getId(), voluntario.getId())) {

                    ResultadoActividad resultado = gestorResultados.obtenerResultadoPorActividad(actividad.getId());

                    if (resultado != null) {
                        double horasActividad = resultado.getHorasTrabajadas();
                        int voluntariosTotales = actividad.getVoluntariosAsignados().size();

                        if (voluntariosTotales > 0) {
                            // Distribuir horas proporcionalmente entre voluntarios
                            double horasPorVoluntario = horasActividad / voluntariosTotales;
                            horasTotales += horasPorVoluntario;
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error en cálculo proporcional: " + e.getMessage());
            horasTotales = voluntario.getHorasAcumuladas();
        }

        return horasTotales;
    }

    /**
     * Método mejorado: Usa la lista específica de voluntarios participantes del resultado
     */
    private double calcularHorasAportadasPrecisas() {
        double horasTotales = 0.0;

        try {
            // Obtener todos los resultados
            GestorResultados gestorResultados = gestorGeneral.getGestorResultados();
            List<ResultadoActividad> todosResultados = gestorResultados.obtenerTodosResultados();

            for (ResultadoActividad resultado : todosResultados) {
                List<String> voluntariosParticipantes = resultado.getVoluntariosParticipantes();

                if (voluntariosParticipantes != null && voluntariosParticipantes.contains(voluntario.getId())) {
                    // Sumar las horas de este resultado
                    horasTotales += resultado.getHorasTrabajadas();
                }
            }

        } catch (Exception e) {
            System.err.println("Error en cálculo preciso: " + e.getMessage());
            // Volver al método anterior si hay error
            horasTotales = calcularHorasAportadasDesdeResultados();
        }

        return horasTotales;
    }

    private JPanel crearTarjetaEstadistica(String titulo, String valor, String icono, Color colorIcono) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 100));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(TEXT_GRAY);
        content.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(valor);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.BLACK);
        content.add(valueLabel, BorderLayout.WEST);

        JLabel iconLabel = new JLabel(icono);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 28));
        iconLabel.setForeground(colorIcono);
        content.add(iconLabel, BorderLayout.EAST);

        card.add(content, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(DashboardVoluntarioUI.this,
                        "Ver detalles de: " + titulo + "\nValor: " + valor,
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return card;
    }

    private JPanel crearResumenInfoPersonal() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        infoPanel.setMaximumSize(new Dimension(800, 250)); // Tamaño máximo fijo

        // Título con botón de editar
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Mi Información");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(title, BorderLayout.WEST);

        JButton editButton = new JButton("Editar Perfil");
        editButton.setBackground(PRIMARY_BLUE);
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Arial", Font.BOLD, 12));
        editButton.addActionListener(e -> cambiarPanel("perfil"));
        titlePanel.add(editButton, BorderLayout.EAST);

        infoPanel.add(titlePanel);
        infoPanel.add(Box.createVerticalStrut(15));

        // Información
        agregarFilaInfo(infoPanel, "📧 Email:", voluntario.getEmail());
        agregarFilaInfo(infoPanel, "📞 Teléfono:", voluntario.getTelefono());
        agregarFilaInfo(infoPanel, "🎯 Habilidades:", voluntario.getHabilidadesTexto());
        agregarFilaInfo(infoPanel, "📅 Disponibilidad:", voluntario.getDiasDisponiblesTexto());

        return infoPanel;
    }

    private void agregarFilaInfo(JPanel panel, String etiqueta, String valor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(760, 30)); // Tamaño máximo fijo

        JLabel icon = new JLabel(etiqueta);
        icon.setFont(new Font("Arial", Font.PLAIN, 14));
        row.add(icon);

        JLabel value = new JLabel(valor);
        value.setFont(new Font("Arial", Font.BOLD, 14));
        row.add(value);

        panel.add(row);
    }

    private JPanel crearSeccionActividadesProximas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 400)); // Tamaño máximo fijo

        JLabel featuredTitle = new JLabel("Próximas Actividades");
        featuredTitle.setFont(new Font("Arial", Font.BOLD, 20));
        featuredTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(featuredTitle);

        panel.add(Box.createVerticalStrut(15));

        // Obtener actividades próximas
        List<Actividad> actividadesPendientes = gestorActividades.obtenerActividadesPendientes();

        if (actividadesPendientes.isEmpty()) {
            JLabel noActivitiesLabel = new JLabel("No hay actividades próximas disponibles");
            noActivitiesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            noActivitiesLabel.setForeground(TEXT_GRAY);
            noActivitiesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(noActivitiesLabel);
        } else {
            // Mostrar solo las primeras 3
            int count = Math.min(3, actividadesPendientes.size());
            for (int i = 0; i < count; i++) {
                Actividad actividad = actividadesPendientes.get(i);
                panel.add(crearTarjetaActividad(actividad));
                if (i < count - 1) {
                    panel.add(Box.createVerticalStrut(15));
                }
            }

            if (actividadesPendientes.size() > 3) {
                panel.add(Box.createVerticalStrut(15));
                JButton verMasButton = new JButton("Ver todas las actividades");
                verMasButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                verMasButton.addActionListener(e -> cambiarPanel("actividades"));
                panel.add(verMasButton);
            }
        }

        return panel;
    }

    // ============================================
    // PANEL DE PERFIL
    // ============================================
    private JPanel crearPanelPerfil() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Encabezado
        panel.add(crearEncabezadoPerfil());
        panel.add(Box.createVerticalStrut(30));

        // Formulario de perfil
        panel.add(crearFormularioPerfil());
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel crearEncabezadoPerfil() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Mi Perfil");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);

        JLabel subtitle = new JLabel("Actualiza tu información personal");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        return panel;
    }

    private JPanel crearFormularioPerfil() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // Campos del formulario
        agregarCampoFormulario(formPanel, "Nombre Completo");
        perfilNombreField = crearCampoTexto(voluntario.getNombre());
        formPanel.add(perfilNombreField);
        formPanel.add(Box.createVerticalStrut(15));

        agregarCampoFormulario(formPanel, "Email");
        JTextField emailField = crearCampoTexto(voluntario.getEmail());
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(15));

        agregarCampoFormulario(formPanel, "Teléfono");
        perfilTelefonoField = crearCampoTexto(voluntario.getTelefono());
        formPanel.add(perfilTelefonoField);
        formPanel.add(Box.createVerticalStrut(15));

        // CORRECCIÓN AQUÍ:
        agregarCampoFormulario(formPanel, "Habilidades (separadas por comas)");
        perfilHabilidadesArea = new JTextArea(voluntario.getHabilidadesTexto(), 3, 30);
        perfilHabilidadesArea.setLineWrap(true);
        perfilHabilidadesArea.setWrapStyleWord(true);
        perfilHabilidadesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane habilidadesScroll = new JScrollPane(perfilHabilidadesArea);
        habilidadesScroll.setMaximumSize(new Dimension(400, 80));
        formPanel.add(habilidadesScroll);
        formPanel.add(Box.createVerticalStrut(15));

        agregarCampoFormulario(formPanel, "Días de Disponibilidad");
        formPanel.add(crearPanelDiasDisponibilidad());
        formPanel.add(Box.createVerticalStrut(15));

        agregarCampoFormulario(formPanel, "Información del Sistema");
        formPanel.add(crearPanelInfoSistema());
        formPanel.add(Box.createVerticalStrut(30));

        // Botones
        formPanel.add(crearPanelBotonesPerfil());

        return formPanel;
    }

    private void agregarCampoFormulario(JPanel panel, String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
    }

    private JTextField crearCampoTexto(String texto) {
        JTextField campo = new JTextField(texto);
        campo.setMaximumSize(new Dimension(400, 35));
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        return campo;
    }

    private JScrollPane crearAreaTexto(String texto, int filas) {
        JTextArea area = new JTextArea(texto, filas, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setMaximumSize(new Dimension(400, 80));
        return scroll;
    }

    private JPanel crearPanelDiasDisponibilidad() {
        JPanel diasPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        diasPanel.setMaximumSize(new Dimension(400, 120));
        diasPanel.setBackground(Color.WHITE);

        checkBoxDias = new JCheckBox[7];
        String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        java.util.List<String> diasVoluntario = voluntario.getDiasDisponibles();

        for (int i = 0; i < diasSemana.length; i++) {
            checkBoxDias[i] = new JCheckBox(diasSemana[i]);
            checkBoxDias[i].setBackground(Color.WHITE);
            checkBoxDias[i].setFont(new Font("Arial", Font.PLAIN, 12));
            checkBoxDias[i].setSelected(diasVoluntario.contains(diasSemana[i]));
            diasPanel.add(checkBoxDias[i]);
        }

        // Panel de botones rápidos
        JPanel botonesRapidos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        botonesRapidos.setMaximumSize(new Dimension(400, 35));
        botonesRapidos.setBackground(Color.WHITE);

        String[] opciones = {"Lunes a Viernes", "Fines de Semana", "Toda la Semana", "Limpiar"};
        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.setFont(new Font("Arial", Font.PLAIN, 11));
            boton.setFocusPainted(false);
            boton.addActionListener(e -> aplicarSeleccionDias(opcion));
            botonesRapidos.add(boton);
        }

        JPanel panelCompleto = new JPanel();
        panelCompleto.setLayout(new BoxLayout(panelCompleto, BoxLayout.Y_AXIS));
        panelCompleto.setOpaque(false);
        panelCompleto.add(diasPanel);
        panelCompleto.add(Box.createVerticalStrut(5));
        panelCompleto.add(botonesRapidos);

        return panelCompleto;
    }

    private void aplicarSeleccionDias(String opcion) {
        switch (opcion) {
            case "Lunes a Viernes":
                for (JCheckBox cb : checkBoxDias) {
                    String dia = cb.getText();
                    cb.setSelected(dia.equals("Lunes") || dia.equals("Martes") ||
                            dia.equals("Miércoles") || dia.equals("Jueves") || dia.equals("Viernes"));
                }
                break;
            case "Fines de Semana":
                for (JCheckBox cb : checkBoxDias) {
                    String dia = cb.getText();
                    cb.setSelected(dia.equals("Sábado") || dia.equals("Domingo"));
                }
                break;
            case "Toda la Semana":
                for (JCheckBox cb : checkBoxDias) {
                    cb.setSelected(true);
                }
                break;
            case "Limpiar":
                for (JCheckBox cb : checkBoxDias) {
                    cb.setSelected(false);
                }
                break;
        }
    }

    private JPanel crearPanelInfoSistema() {
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        infoPanel.setOpaque(false);

        agregarInfoItem(infoPanel, "ID de Usuario:", voluntario.getId());
        agregarInfoItem(infoPanel, "Horas Acumuladas:", voluntario.getHorasAcumuladas() + " horas");

        infoPanel.setMaximumSize(new Dimension(400, 60));
        return infoPanel;
    }

    private void agregarInfoItem(JPanel panel, String etiqueta, String valor) {
        panel.add(new JLabel(etiqueta));
        panel.add(new JLabel(valor));
    }

    private JPanel crearPanelBotonesPerfil() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton guardarButton = new JButton("Guardar Cambios");
        guardarButton.setBackground(PRIMARY_BLUE);
        guardarButton.setForeground(Color.WHITE);
        guardarButton.setFocusPainted(false);
        guardarButton.setFont(new Font("Arial", Font.BOLD, 14));
        guardarButton.addActionListener(e -> guardarPerfil());

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setBackground(Color.LIGHT_GRAY);
        cancelarButton.setForeground(Color.BLACK);
        cancelarButton.setFocusPainted(false);
        cancelarButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelarButton.addActionListener(e -> cargarDatosPerfil());

        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelarButton);

        return buttonPanel;
    }

    private void cargarDatosPerfil() {
        perfilNombreField.setText(voluntario.getNombre());
        perfilTelefonoField.setText(voluntario.getTelefono());

        if (perfilHabilidadesArea != null) {
            perfilHabilidadesArea.setText(voluntario.getHabilidadesTexto());
        }

        if (checkBoxDias != null) {
            java.util.List<String> diasVoluntario = voluntario.getDiasDisponibles();
            for (JCheckBox cb : checkBoxDias) {
                cb.setSelected(diasVoluntario.contains(cb.getText()));
            }
        }
    }

    private void guardarPerfil() {
        // Validaciones
        if (perfilNombreField.getText().trim().isEmpty()) {
            mostrarError("El nombre no puede estar vacío", perfilNombreField);
            return;
        }

        if (perfilTelefonoField.getText().trim().isEmpty()) {
            mostrarError("El teléfono no puede estar vacío", perfilTelefonoField);
            return;
        }

        try {
            // Actualizar objeto voluntario
            voluntario.setNombre(perfilNombreField.getText().trim());
            voluntario.setTelefono(perfilTelefonoField.getText().trim());

            if (perfilHabilidadesArea != null) {
                voluntario.setHabilidadesTexto(perfilHabilidadesArea.getText().trim());
            }

            // Guardar días seleccionados
            if (checkBoxDias != null) {
                java.util.List<String> diasSeleccionados = new ArrayList<>();
                for (JCheckBox cb : checkBoxDias) {
                    if (cb.isSelected()) {
                        diasSeleccionados.add(cb.getText());
                    }
                }
                voluntario.setDiasDisponibles(diasSeleccionados);
            }

            // TODO: Guardar en persistencia
            // gestorGeneral.actualizarVoluntario(voluntario);

            JOptionPane.showMessageDialog(this,
                    "¡Perfil actualizado exitosamente!",
                    "Perfil Actualizado", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            mostrarError("Error al guardar el perfil: " + ex.getMessage(), null);
        }
    }

    private void mostrarError(String mensaje, Component componente) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        if (componente != null) {
            componente.requestFocus();
        }
    }

    // ============================================
    // PANEL DE ACTIVIDADES - MODIFICADO PARA TAMAÑO FIJO
    // ============================================
// PANEL DE ACTIVIDADES - MODIFICADO PARA TAMAÑO FIJO
// ============================================
    private JPanel crearPanelActividades() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(BACKGROUND_GRAY);

        // Panel con tabs para separar actividades disponibles y completadas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // Tab 1: Actividades Disponibles
        JPanel actividadesDisponiblesPanel = new JPanel(new BorderLayout());
        actividadesDisponiblesPanel.setBackground(BACKGROUND_GRAY);

        JPanel contentPanelDisponibles = new JPanel();
        contentPanelDisponibles.setLayout(new BoxLayout(contentPanelDisponibles, BoxLayout.Y_AXIS));
        contentPanelDisponibles.setBackground(BACKGROUND_GRAY);
        contentPanelDisponibles.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel innerPanelDisponibles = new JPanel();
        innerPanelDisponibles.setLayout(new BoxLayout(innerPanelDisponibles, BoxLayout.Y_AXIS));
        innerPanelDisponibles.setBackground(BACKGROUND_GRAY);
        innerPanelDisponibles.setPreferredSize(new Dimension(800, 700));

        // Encabezado
        innerPanelDisponibles.add(crearEncabezadoActividades());
        innerPanelDisponibles.add(Box.createVerticalStrut(30));

        // Filtros
        innerPanelDisponibles.add(crearPanelFiltros());
        innerPanelDisponibles.add(Box.createVerticalStrut(30));

        // Lista de actividades DISPONIBLES (excluyendo completadas)
        innerPanelDisponibles.add(crearListaActividadesDisponibles());
        innerPanelDisponibles.add(Box.createVerticalGlue());

        contentPanelDisponibles.add(innerPanelDisponibles);

        JScrollPane scrollPaneDisponibles = new JScrollPane(contentPanelDisponibles);
        scrollPaneDisponibles.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneDisponibles.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneDisponibles.getVerticalScrollBar().setUnitIncrement(16);
        scrollPaneDisponibles.setBorder(null);
        scrollPaneDisponibles.setPreferredSize(new Dimension(900, 700));

        actividadesDisponiblesPanel.add(scrollPaneDisponibles, BorderLayout.CENTER);

        // Tab 2: Actividades Completadas
        JPanel actividadesCompletadasPanel = new JPanel(new BorderLayout());
        actividadesCompletadasPanel.setBackground(BACKGROUND_GRAY);

        JPanel contentPanelCompletadas = new JPanel();
        contentPanelCompletadas.setLayout(new BoxLayout(contentPanelCompletadas, BoxLayout.Y_AXIS));
        contentPanelCompletadas.setBackground(BACKGROUND_GRAY);
        contentPanelCompletadas.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel innerPanelCompletadas = new JPanel();
        innerPanelCompletadas.setLayout(new BoxLayout(innerPanelCompletadas, BoxLayout.Y_AXIS));
        innerPanelCompletadas.setBackground(BACKGROUND_GRAY);
        innerPanelCompletadas.setPreferredSize(new Dimension(800, 700));

        // Encabezado específico para actividades completadas
        innerPanelCompletadas.add(crearEncabezadoActividadesCompletadas());
        innerPanelCompletadas.add(Box.createVerticalStrut(30));

        // Sección de actividades completadas
        innerPanelCompletadas.add(crearSeccionActividadesCompletadas());
        innerPanelCompletadas.add(Box.createVerticalGlue());

        contentPanelCompletadas.add(innerPanelCompletadas);

        JScrollPane scrollPaneCompletadas = new JScrollPane(contentPanelCompletadas);
        scrollPaneCompletadas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneCompletadas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneCompletadas.getVerticalScrollBar().setUnitIncrement(16);
        scrollPaneCompletadas.setBorder(null);
        scrollPaneCompletadas.setPreferredSize(new Dimension(900, 700));

        actividadesCompletadasPanel.add(scrollPaneCompletadas, BorderLayout.CENTER);

        // Agregar tabs
        tabbedPane.addTab("Actividades Disponibles", actividadesDisponiblesPanel);
        tabbedPane.addTab("Mis Actividades Completadas", actividadesCompletadasPanel);

        // Cambiar color de las tabs seleccionadas
        tabbedPane.setBackgroundAt(0, new Color(240, 248, 255));
        tabbedPane.setBackgroundAt(1, new Color(240, 248, 255));

        container.add(tabbedPane, BorderLayout.CENTER);
        return container;
    }

    private void actualizarPanelActividades() {
        actividadesPanel = crearPanelActividades();
        cardPanel.remove(2);
        cardPanel.add(actividadesPanel, "actividades");
        cardLayout.show(cardPanel, "actividades");
    }

    // Método para crear encabezado de actividades completadas
    private JPanel crearEncabezadoActividadesCompletadas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 100));

        JLabel title = new JLabel("Mis Actividades Completadas");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);

        JLabel subtitle = new JLabel("Actividades en las que has participado y están finalizadas");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        return panel;
    }

    // Método modificado: Lista solo actividades NO completadas
    private JPanel crearListaActividadesDisponibles() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 1000));

        // Obtener solo actividades NO completadas
        List<Actividad> actividades = obtenerActividadesNoCompletadas();

        if (actividades.isEmpty()) {
            panel.add(crearMensajeSinActividades());
        } else {
            List<Actividad> actividadesFiltradas = filtrarActividades(actividades);

            if (actividadesFiltradas.isEmpty()) {
                panel.add(crearMensajeSinResultados());
            } else {
                for (int i = 0; i < actividadesFiltradas.size(); i++) {
                    panel.add(crearTarjetaActividad(actividadesFiltradas.get(i)));
                    if (i < actividadesFiltradas.size() - 1) {
                        panel.add(Box.createVerticalStrut(20));
                    }
                }
            }
        }

        return panel;
    }

    // Método para obtener solo actividades NO completadas
    private List<Actividad> obtenerActividadesNoCompletadas() {
        List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();
        List<Actividad> actividadesNoCompletadas = new ArrayList<>();

        for (Actividad actividad : todasActividades) {
            String estado = actividad.determinarEstado();
            if (!estado.equals("Completada")) {
                actividadesNoCompletadas.add(actividad);
            }
        }

        return actividadesNoCompletadas;
    }

    // ============================================
// SECCIÓN DE ACTIVIDADES COMPLETADAS
// ============================================
    private JPanel crearSeccionActividadesCompletadas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 1000));

        // Obtener actividades completadas en las que participó
        List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();
        List<Actividad> misActividadesCompletadas = new ArrayList<>();

        for (Actividad actividad : todasActividades) {
            String estado = actividad.determinarEstado();
            if (estado.equals("Completada") &&
                    gestorActividades.estaVoluntarioEnActividad(actividad.getId(), voluntario.getId())) {
                misActividadesCompletadas.add(actividad);
            }
        }

        // Panel de estadísticas de actividades completadas
        panel.add(crearEstadisticasCompletadas(misActividadesCompletadas));
        panel.add(Box.createVerticalStrut(20));

        if (misActividadesCompletadas.isEmpty()) {
            panel.add(crearMensajeSinActividadesCompletadas());
        } else {
            // Ordenar por fecha (más recientes primero)
            misActividadesCompletadas.sort((a1, a2) -> a2.getFecha().compareTo(a1.getFecha()));

            for (int i = 0; i < misActividadesCompletadas.size(); i++) {
                panel.add(crearTarjetaActividadCompletada(misActividadesCompletadas.get(i)));
                if (i < misActividadesCompletadas.size() - 1) {
                    panel.add(Box.createVerticalStrut(15));
                }
            }
        }

        return panel;
    }

    // Panel de estadísticas de actividades completadas
    private JPanel crearEstadisticasCompletadas(List<Actividad> actividadesCompletadas) {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(800, 100));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
                new EmptyBorder(15, 20, 15, 20)
        ));

        int totalActividades = actividadesCompletadas.size();
        double totalHoras = calcularHorasDeActividadesCompletadas();

        JLabel actividadesLabel = new JLabel("Actividades Completadas: " + totalActividades);
        actividadesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        actividadesLabel.setForeground(new Color(76, 175, 80));
        statsPanel.add(actividadesLabel);

        statsPanel.add(Box.createHorizontalStrut(30));

        JLabel horasLabel = new JLabel("Horas Aportadas: " + String.format("%.1f", totalHoras) + "h");
        horasLabel.setFont(new Font("Arial", Font.BOLD, 14));
        horasLabel.setForeground(new Color(76, 175, 80));
        statsPanel.add(horasLabel);

        return statsPanel;
    }

    // Mensaje cuando no hay actividades completadas
    private JLabel crearMensajeSinActividadesCompletadas() {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setOpaque(false);
        messagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        messagePanel.setMaximumSize(new Dimension(800, 200));

        JLabel iconLabel = new JLabel("🎯");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagePanel.add(iconLabel);

        messagePanel.add(Box.createVerticalStrut(15));

        JLabel textLabel = new JLabel("Aún no has completado actividades");
        textLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        textLabel.setForeground(TEXT_GRAY);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagePanel.add(textLabel);

        messagePanel.add(Box.createVerticalStrut(10));

        JLabel subtextLabel = new JLabel("¡Participa en actividades y completa tus tareas!");
        subtextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtextLabel.setForeground(TEXT_GRAY);
        subtextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagePanel.add(subtextLabel);

        JLabel label = new JLabel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 200);
            }
        };
        label.setLayout(new BorderLayout());
        label.add(messagePanel, BorderLayout.CENTER);

        return label;
    }

    // Tarjeta especial para actividades completadas
    private JPanel crearTarjetaActividadCompletada(Actividad actividad) {
        JPanel card = crearTarjetaActividad(actividad);

        // Agregar encabezado especial para actividad completada
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(760, 40));
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel completadaLabel = new JLabel("✓ COMPLETADA");
        completadaLabel.setFont(new Font("Arial", Font.BOLD, 12));
        completadaLabel.setForeground(CARD_GREEN);
        completadaLabel.setBackground(new Color(76, 175, 80, 20));
        completadaLabel.setOpaque(true);
        completadaLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        completadaLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(completadaLabel, BorderLayout.WEST);

        // Mostrar resultados si existen
        if (actividad.getResultados() != null && !actividad.getResultados().isEmpty()) {
            JButton verResultadosBtn = new JButton("Ver Resultados");
            verResultadosBtn.setFont(new Font("Arial", Font.PLAIN, 11));
            verResultadosBtn.setBackground(new Color(76, 175, 80));
            verResultadosBtn.setForeground(Color.WHITE);
            verResultadosBtn.setFocusPainted(false);
            verResultadosBtn.addActionListener(e -> mostrarResultadosActividad(actividad));
            headerPanel.add(verResultadosBtn, BorderLayout.EAST);
        }

        // Deshabilitar botón "Participar" en actividades completadas
        Component[] components = card.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComponents = ((JPanel) comp).getComponents();
                for (Component subComp : subComponents) {
                    if (subComp instanceof JButton) {
                        JButton btn = (JButton) subComp;
                        if (btn.getText().equals("Participar") || btn.getText().equals("Ya Inscrito")) {
                            btn.setText("Completada");
                            btn.setBackground(CARD_GREEN);
                            btn.setEnabled(false);
                        }
                    }
                }
            }
        }

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(headerPanel);
        wrapper.add(card);

        return wrapper;
    }

    // Método para mostrar resultados de actividad completada
    private void mostrarResultadosActividad(Actividad actividad) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Resultados de la actividad:\n\n");
        detalles.append("• Actividad: ").append(actividad.getNombre()).append("\n");
        detalles.append("• Fecha: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(actividad.getFecha())).append("\n");
        detalles.append("• Lugar: ").append(actividad.getLugar()).append("\n\n");
        detalles.append("RESULTADOS:\n");
        detalles.append(actividad.getResultados());

        JTextArea textArea = new JTextArea(detalles.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Resultados: " + actividad.getNombre(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Modificar el método filtrarActividades para usar solo actividades no completadas
    private List<Actividad> filtrarActividades(List<Actividad> actividades) {
        List<Actividad> filtradas = new ArrayList<>();
        String tipo = (String) filtroTipoCombo.getSelectedItem();
        String ubicacion = (String) filtroUbicacionCombo.getSelectedItem();
        String estado = (String) filtroEstadoCombo.getSelectedItem();

        for (Actividad actividad : actividades) {
            // Filtrar primero por estado de completitud (ya viene filtrado)
            String estadoActividad = actividad.determinarEstado();
            if (estadoActividad.equals("Completada")) {
                continue; // No mostrar actividades completadas en esta sección
            }

            if (pasaFiltros(actividad, tipo, ubicacion, estado)) {
                filtradas.add(actividad);
            }
        }

        return filtradas;
    }


    private JPanel crearEncabezadoActividades() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 100)); // Tamaño máximo fijo

        JLabel title = new JLabel("Actividades Disponibles");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);

        JLabel subtitle = new JLabel("Encuentra y únete a brigadas cerca de ti");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        return panel;
    }

    private JPanel crearPanelFiltros() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        filterPanel.setMaximumSize(new Dimension(800, 120)); // Tamaño máximo fijo

        JLabel filterTitle = new JLabel("Filtrar Actividades");
        filterTitle.setFont(new Font("Arial", Font.BOLD, 14));
        filterTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(filterTitle);
        filterPanel.add(Box.createVerticalStrut(10));

        JPanel filtersContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filtersContainer.setOpaque(false);
        filtersContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        filtersContainer.setMaximumSize(new Dimension(780, 60)); // Tamaño máximo fijo

        JButton refreshButton = new JButton("🔄 Actualizar");
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setForeground(PRIMARY_BLUE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> {
            // Forzar refresco de datos
            gestorActividades.obtenerTodasActividades(); // Si existe
            actualizarPanelActividades();
            JOptionPane.showMessageDialog(this,
                    "Lista de actividades actualizada",
                    "Actualizado",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        filtersContainer.add(refreshButton);

        // Filtro por tipo
        filtersContainer.add(new JLabel("Tipo:"));
        String[] typeOptions = {"Todos", "Alimentos", "Salud", "Educación", "Medio Ambiente", "Construcción", "Limpieza", "Distribución"};
        filtroTipoCombo = new JComboBox<>(typeOptions);
        filtroTipoCombo.setPreferredSize(new Dimension(150, 30));
        filtersContainer.add(filtroTipoCombo);

        // Filtro por ubicación
        filtersContainer.add(new JLabel("Ubicación:"));
        String[] locationOptions = {"Todas", "Zona Norte", "Zona Sur", "Zona Este", "Zona Oeste", "Centro", "San José"};
        filtroUbicacionCombo = new JComboBox<>(locationOptions);
        filtroUbicacionCombo.setPreferredSize(new Dimension(150, 30));
        filtersContainer.add(filtroUbicacionCombo);

        // Filtro por estado
        filtersContainer.add(new JLabel("Estado:"));
        String[] stateOptions = {"Todos", "Pendientes", "Completadas", "Disponibles"};
        filtroEstadoCombo = new JComboBox<>(stateOptions);
        filtroEstadoCombo.setPreferredSize(new Dimension(150, 30));
        filtersContainer.add(filtroEstadoCombo);

        // Botones de filtro
        JButton applyButton = new JButton("Aplicar Filtros");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.addActionListener(e -> {
            actualizarPanelActividades();
            JOptionPane.showMessageDialog(this, "Filtros aplicados correctamente",
                    "Filtros Aplicados", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton clearButton = new JButton("Limpiar");
        clearButton.setBackground(Color.LIGHT_GRAY);
        clearButton.setForeground(Color.BLACK);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> {
            filtroTipoCombo.setSelectedItem("Todos");
            filtroUbicacionCombo.setSelectedItem("Todas");
            filtroEstadoCombo.setSelectedItem("Todos");
            actualizarPanelActividades();
        });

        filtersContainer.add(applyButton);
        filtersContainer.add(clearButton);
        filterPanel.add(filtersContainer);

        return filterPanel;
    }

    private JPanel crearListaActividades() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 1000));

        // Asegurar que obtenemos datos frescos del gestor
        List<Actividad> actividades = obtenerActividadesParaMostrar();

        if (actividades.isEmpty()) {
            panel.add(crearMensajeSinActividades());
        } else {
            List<Actividad> actividadesFiltradas = filtrarActividades(actividades);

            if (actividadesFiltradas.isEmpty()) {
                panel.add(crearMensajeSinResultados());
            } else {
                for (int i = 0; i < actividadesFiltradas.size(); i++) {
                    panel.add(crearTarjetaActividad(actividadesFiltradas.get(i)));
                    if (i < actividadesFiltradas.size() - 1) {
                        panel.add(Box.createVerticalStrut(20));
                    }
                }
            }
        }

        return panel;
    }

    private JLabel crearMensajeSinActividades() {
        JLabel label = new JLabel("No hay actividades disponibles en este momento");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT_GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel crearMensajeSinResultados() {
        JLabel label = new JLabel("No hay actividades que coincidan con los filtros seleccionados");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT_GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private boolean pasaFiltros(Actividad actividad, String tipo, String ubicacion, String estado) {
        // Filtro por tipo
        if (!"Todos".equals(tipo)) {
            String nombreLower = actividad.getNombre().toLowerCase();
            String objetivoLower = actividad.getObjetivo().toLowerCase();
            String tipoLower = tipo.toLowerCase();
            if (!nombreLower.contains(tipoLower) && !objetivoLower.contains(tipoLower)) {
                return false;
            }
        }

        // Filtro por ubicación
        if (!"Todas".equals(ubicacion)) {
            if (!actividad.getLugar().toLowerCase().contains(ubicacion.toLowerCase())) {
                return false;
            }
        }

        // Filtro por estado
        if (!"Todos".equals(estado)) {
            boolean esPendiente = actividad.getFecha().after(new java.util.Date());
            int maxVoluntarios = 20;
            int actualVoluntarios = actividad.getVoluntariosAsignados().size();

            switch (estado) {
                case "Pendientes":
                    if (!esPendiente) return false;
                    break;
                case "Completadas":
                    if (esPendiente) return false;
                    break;
                case "Disponibles":
                    if (actualVoluntarios >= maxVoluntarios) return false;
                    break;
            }
        }

        return true;
    }

    // ============================================
    // TARJETA DE ACTIVIDAD - MODIFICADA PARA TAMAÑO FIJO
    // ============================================
    private JPanel crearTarjetaActividad(Actividad actividad) {
        String icono = obtenerIconoActividad(actividad);
        String fechaFormateada = new SimpleDateFormat("dd MMM yyyy • HH:mm").format(actividad.getFecha());
        String brigadaNombre = actividad.getBrigadaAsociada() != null ?
                actividad.getBrigadaAsociada().getNombre() : "Sin brigada asignada";

        int maxVoluntarios = 20;

        // Obtener el mapa de voluntarios asignados
        java.util.Map<String, String> voluntariosMap = actividad.getVoluntariosAsignados();
        int voluntariosInscritos = voluntariosMap != null ? voluntariosMap.size() : 0;

        int cuposDisponibles = maxVoluntarios - voluntariosInscritos;
        String cuposTexto = cuposDisponibles > 0 ? cuposDisponibles + " cupos disponibles" : "Completado";

        // Verificar si el voluntario actual está en el mapa
        boolean yaInscrito = voluntariosMap != null &&
                voluntariosMap.containsKey(voluntario.getId());

        Color colorEstado = obtenerColorEstado(actividad, voluntariosInscritos, maxVoluntarios);

        return crearTarjetaActividadDetallada(
                actividad.getNombre(),
                icono,
                brigadaNombre,
                cuposTexto,
                " " + fechaFormateada,
                " " + actividad.getLugar(),
                actividad.getObjetivo(),
                voluntariosInscritos,
                maxVoluntarios,
                actividad.getId(),
                yaInscrito,
                colorEstado
        );
    }

    private String obtenerIconoActividad(Actividad actividad) {
        String nombre = actividad.getNombre().toLowerCase();
        String objetivo = actividad.getObjetivo().toLowerCase();

        if (nombre.contains("alimento") || nombre.contains("comida") || objetivo.contains("alimento")) {
            return "";
        } else if (nombre.contains("salud") || nombre.contains("médico") || objetivo.contains("salud")) {
            return "";
        } else if (nombre.contains("educación") || nombre.contains("enseñanza") || objetivo.contains("educar")) {
            return "";
        } else if (nombre.contains("limpieza") || nombre.contains("ecolog") || objetivo.contains("medio ambiente")) {
            return "";
        } else if (nombre.contains("construcción") || nombre.contains("reparación")) {
            return "";
        } else if (nombre.contains("distribución") || nombre.contains("entrega")) {
            return "";
        } else {
            return "";
        }
    }
    private Color obtenerColorEstado(Actividad actividad, int inscritos, int maximo) {
        Date ahora = new Date();
        boolean esPendiente = actividad.getFecha().after(ahora);
        String estado = actividad.determinarEstado();

        // PRIORIDAD 1: Si la actividad está COMPLETADA
        if (estado.equals("Completada")) {
            return CARD_GREEN; // Color verde para actividades completadas
        }

        // PRIORIDAD 2: Si la fecha ya pasó pero no está completada
        else if (!esPendiente) {
            return CARD_ORANGE; // Pasada (esperando resultados)
        }

        // PRIORIDAD 3: Verificar cupos
        else if (inscritos >= maximo) {
            return CARD_RED; // Sin cupos
        } else if (inscritos >= maximo * 0.8) {
            return CARD_ORANGE; // Casi llena
        } else {
            return new Color(33, 150, 243); // Azul para actividades disponibles
        }
    }

    private JPanel crearTarjetaActividadDetallada(String titulo, String icono, String subtitulo,
                                                  String cupos, String fechaHora, String ubicacion, String descripcion,
                                                  int voluntariosActuales, int voluntariosMaximos, String actividadId,
                                                  boolean yaInscrito, Color colorEstado) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(800, 350)); // Tamaño máximo fijo para tarjetas

        // 1. Encabezado (Título y Cupos)
        card.add(crearEncabezadoActividadCard(titulo, icono, subtitulo, cupos, colorEstado));
        card.add(Box.createVerticalStrut(15));

        // 2. Información de fecha y ubicación
        card.add(crearInfoFechaUbicacion(fechaHora, ubicacion));
        card.add(Box.createVerticalStrut(15));

        // 3. Descripción
        card.add(crearDescripcionActividad(descripcion));
        card.add(Box.createVerticalStrut(15));

        // 4. Barra de progreso
        card.add(crearBarraProgreso(voluntariosActuales, voluntariosMaximos, colorEstado));
        card.add(Box.createVerticalStrut(20));

        // 5. Botones de acción
        card.add(crearBotonesAccion(titulo, actividadId, yaInscrito, voluntariosActuales, voluntariosMaximos,
                fechaHora, ubicacion, subtitulo, descripcion, colorEstado));

        return card;
    }

    private JPanel crearEncabezadoActividadCard(String titulo, String icono, String subtitulo,
                                                String cupos, Color colorEstado) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(760, 60)); // Tamaño máximo fijo

        JPanel titleGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleGroup.setOpaque(false);

        JLabel activityIcon = new JLabel(icono);
        activityIcon.setFont(new Font("Arial", Font.BOLD, 24));
        titleGroup.add(activityIcon);

        JPanel textGroup = new JPanel();
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
        textGroup.setOpaque(false);
        textGroup.setMaximumSize(new Dimension(600, 60));

        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textGroup.add(titleLabel);

        JLabel subtitleLabel = new JLabel(subtitulo);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_GRAY);
        textGroup.add(subtitleLabel);

        titleGroup.add(textGroup);
        header.add(titleGroup, BorderLayout.WEST);

        JLabel slotsLabel = new JLabel(cupos);
        slotsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        slotsLabel.setForeground(colorEstado);
        slotsLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        slotsLabel.setBackground(new Color(colorEstado.getRed(), colorEstado.getGreen(), colorEstado.getBlue(), 20));
        slotsLabel.setOpaque(true);
        header.add(slotsLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel crearInfoFechaUbicacion(String fechaHora, String ubicacion) {
        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        metaPanel.setOpaque(false);
        metaPanel.setMaximumSize(new Dimension(760, 30)); // Tamaño máximo fijo

        JLabel dateTimeLabel = new JLabel(fechaHora);
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        metaPanel.add(dateTimeLabel);

        JLabel locationLabel = new JLabel(ubicacion);
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        metaPanel.add(locationLabel);

        return metaPanel;
    }

    private JScrollPane crearDescripcionActividad(String descripcion) {
        JTextArea descriptionArea = new JTextArea(descripcion);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setMaximumSize(new Dimension(760, 100)); // Tamaño máximo fijo
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    private JPanel crearBarraProgreso(int actuales, int maximos, Color color) {
        JPanel progressPanel = new JPanel(new BorderLayout(10, 0));
        progressPanel.setOpaque(false);
        progressPanel.setMaximumSize(new Dimension(760, 30)); // Tamaño máximo fijo

        JLabel volunteerCount = new JLabel(actuales + "/" + maximos + " voluntarios inscritos");
        volunteerCount.setFont(new Font("Arial", Font.PLAIN, 12));
        progressPanel.add(volunteerCount, BorderLayout.WEST);

        JProgressBar progressBar = new JProgressBar(0, maximos);
        progressBar.setValue(actuales);
        progressBar.setStringPainted(false);
        progressBar.setForeground(color);
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setPreferredSize(new Dimension(400, 20));
        progressPanel.add(progressBar, BorderLayout.CENTER);

        return progressPanel;
    }

    private JPanel crearBotonesAccion(String titulo, String actividadId, boolean yaInscrito,
                                      int actuales, int maximos, String fechaHora, String ubicacion,
                                      String subtitulo, String descripcion, Color colorEstado) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(760, 50)); // Tamaño máximo fijo

        // Botón Participar
        JButton participateButton = crearBotonParticipar(titulo, actividadId, yaInscrito, actuales, maximos, colorEstado);
        buttonPanel.add(participateButton);

        // Botón Ver Detalles
        JButton detailsButton = crearBotonDetalles(titulo, actividadId, fechaHora, ubicacion,
                subtitulo, descripcion, actuales, maximos, yaInscrito);
        buttonPanel.add(detailsButton);

        return buttonPanel;
    }

    private JButton crearBotonParticipar(String titulo, String actividadId, boolean yaInscrito,
                                         int actuales, int maximos, Color colorEstado) {
        JButton button;

        if (yaInscrito) {
            button = new JButton("Ya Inscrito");
            button.setBackground(CARD_GREEN);
            button.setEnabled(false);
        } else if (actuales >= maximos) {
            button = new JButton("Completado");
            button.setBackground(CARD_RED);
            button.setEnabled(false);
        } else {
            button = new JButton("Participar");
            button.setBackground(PRIMARY_BLUE);
            button.addActionListener(e -> manejarInscripcion(titulo, actividadId));
        }

        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 35));

        return button;
    }

    private void manejarInscripcion(String titulo, String actividadId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Confirmas tu participación en '" + titulo + "'?",
                "Confirmar Participación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gestorActividades.asignarVoluntarioAActividad(actividadId, voluntario.getId(), "Participante");
                JOptionPane.showMessageDialog(this,
                        "¡Te has inscrito exitosamente en '" + titulo + "'!\n" +
                                "Recibirás más detalles por correo electrónico.",
                        "Inscripción Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                actualizarPanelActividades();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al inscribirse: " + ex.getMessage(),
                        "Error de Inscripción",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton crearBotonDetalles(String titulo, String actividadId, String fechaHora,
                                       String ubicacion, String subtitulo, String descripcion,
                                       int actuales, int maximos, boolean yaInscrito) {
        JButton button = new JButton("Ver Detalles");
        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_BLUE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 1));
        button.setPreferredSize(new Dimension(120, 35));

        button.addActionListener(e -> mostrarDetallesActividad(titulo, actividadId, fechaHora,
                ubicacion, subtitulo, descripcion, actuales, maximos, yaInscrito));

        return button;
    }

    private void mostrarDetallesActividad(String titulo, String actividadId, String fechaHora,
                                          String ubicacion, String subtitulo, String descripcion,
                                          int actuales, int maximos, boolean yaInscrito) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Información detallada de la actividad:\n\n");
        detalles.append("• Actividad: ").append(titulo).append("\n");
        detalles.append("• ID: ").append(actividadId).append("\n");
        detalles.append("• Fecha: ").append(fechaHora.split(" • ")[0].replace("📅 ", "")).append("\n");
        detalles.append("• Horario: ").append(fechaHora.split(" • ")[1]).append("\n");
        detalles.append("• Ubicación: ").append(ubicacion.replace("📍 ", "")).append("\n");
        detalles.append("• Brigada: ").append(subtitulo).append("\n");
        detalles.append("• Objetivo: ").append(descripcion).append("\n");
        detalles.append("• Cupos: ").append(actuales).append("/").append(maximos).append("\n");

        if (yaInscrito) {
            detalles.append("\nYa estás inscrito en esta actividad");
        }

        JOptionPane.showMessageDialog(this,
                detalles.toString(),
                "Detalles: " + titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Métodos nuevos para el panel de brigadas:
    private JPanel crearPanelBrigadas() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(BACKGROUND_GRAY);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_GRAY);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBackground(BACKGROUND_GRAY);
        innerPanel.setPreferredSize(new Dimension(800, 700));

        innerPanel.add(crearEncabezadoBrigadas());
        innerPanel.add(Box.createVerticalStrut(30));
        innerPanel.add(crearPanelFiltrosBrigadas());
        innerPanel.add(Box.createVerticalStrut(30));
        innerPanel.add(crearListaBrigadas());
        innerPanel.add(Box.createVerticalGlue());

        contentPanel.add(innerPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(900, 700));

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private void actualizarPanelBrigadas() {
        brigadasPanel = crearPanelBrigadas();
        cardPanel.remove(3);
        cardPanel.add(brigadasPanel, "brigadas");
        cardLayout.show(cardPanel, "brigadas");
    }

    private JPanel crearEncabezadoBrigadas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 100));

        JLabel title = new JLabel("Mis Brigadas");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);

        JLabel subtitle = new JLabel("Brigadas a las que estás asignado como voluntario");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        return panel;
    }

    private JPanel crearPanelFiltrosBrigadas() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        filterPanel.setMaximumSize(new Dimension(800, 120));

        JLabel filterTitle = new JLabel("Filtrar Brigadas");
        filterTitle.setFont(new Font("Arial", Font.BOLD, 14));
        filterTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(filterTitle);
        filterPanel.add(Box.createVerticalStrut(10));

        JPanel filtersContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filtersContainer.setOpaque(false);
        filtersContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        filtersContainer.setMaximumSize(new Dimension(780, 60));

        filtersContainer.add(new JLabel("Estado:"));
        String[] estadoOptions = {"Todos", "Activa", "Inactiva", "En planificación"};
        filtroBrigadasEstadoCombo = new JComboBox<>(estadoOptions);
        filtroBrigadasEstadoCombo.setPreferredSize(new Dimension(150, 30));
        filtersContainer.add(filtroBrigadasEstadoCombo);

        filtersContainer.add(new JLabel("Zona:"));
        String[] zonaOptions = {"Todas", "Zona Norte", "Zona Sur", "Zona Este", "Zona Oeste", "Centro", "San José"};
        filtroBrigadasZonaCombo = new JComboBox<>(zonaOptions);
        filtroBrigadasZonaCombo.setPreferredSize(new Dimension(150, 30));
        filtersContainer.add(filtroBrigadasZonaCombo);

        filtersContainer.add(new JLabel("Tipo:"));
        String[] tipoOptions = {"Todos", "Alimentos", "Salud", "Educación", "Medio Ambiente",
                "Construcción", "Limpieza", "Distribución", "Emergencia"};
        filtroBrigadasTipoCombo = new JComboBox<>(tipoOptions);
        filtroBrigadasTipoCombo.setPreferredSize(new Dimension(150, 30));
        filtersContainer.add(filtroBrigadasTipoCombo);

        JButton applyButton = new JButton("Aplicar Filtros");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.addActionListener(e -> {
            actualizarPanelBrigadas();
            JOptionPane.showMessageDialog(this, "Filtros aplicados correctamente",
                    "Filtros Aplicados", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton clearButton = new JButton("Limpiar");
        clearButton.setBackground(Color.LIGHT_GRAY);
        clearButton.setForeground(Color.BLACK);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> {
            filtroBrigadasEstadoCombo.setSelectedItem("Todos");
            filtroBrigadasZonaCombo.setSelectedItem("Todas");
            filtroBrigadasTipoCombo.setSelectedItem("Todos");
            actualizarPanelBrigadas();
        });

        filtersContainer.add(applyButton);
        filtersContainer.add(clearButton);
        filterPanel.add(filtersContainer);

        return filterPanel;
    }

    private JPanel crearListaBrigadas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 1000));

        List<Brigada> misBrigadas = gestorGeneral.getGestorBrigadas().obtenerBrigadasDeVoluntario(voluntario.getId());

        if (misBrigadas.isEmpty()) {
            panel.add(crearMensajeSinBrigadas());
        } else {
            List<Brigada> brigadasFiltradas = filtrarBrigadas(misBrigadas);

            if (brigadasFiltradas.isEmpty()) {
                panel.add(crearMensajeSinResultadosBrigadas());
            } else {
                for (int i = 0; i < brigadasFiltradas.size(); i++) {
                    panel.add(crearTarjetaBrigada(brigadasFiltradas.get(i)));
                    if (i < brigadasFiltradas.size() - 1) {
                        panel.add(Box.createVerticalStrut(20));
                    }
                }
            }
        }

        return panel;
    }

    private JLabel crearMensajeSinBrigadas() {
        JLabel label = new JLabel("No estás asignado a ninguna brigada actualmente.");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT_GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel crearMensajeSinResultadosBrigadas() {
        JLabel label = new JLabel("No hay brigadas que coincidan con los filtros seleccionados");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT_GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private List<Brigada> filtrarBrigadas(List<Brigada> brigadas) {
        List<Brigada> filtradas = new ArrayList<>();
        String estado = (String) filtroBrigadasEstadoCombo.getSelectedItem();
        String zona = (String) filtroBrigadasZonaCombo.getSelectedItem();
        String tipo = (String) filtroBrigadasTipoCombo.getSelectedItem();

        for (Brigada brigada : brigadas) {
            if (pasaFiltrosBrigada(brigada, estado, zona, tipo)) {
                filtradas.add(brigada);
            }
        }

        return filtradas;
    }

    private boolean pasaFiltrosBrigada(Brigada brigada, String estado, String zona, String tipo) {
        if (!"Todos".equals(estado)) {
            if (!brigada.getEstado().equalsIgnoreCase(estado)) {
                return false;
            }
        }

        if (!"Todas".equals(zona)) {
            if (!brigada.getZona().toLowerCase().contains(zona.toLowerCase())) {
                return false;
            }
        }

        if (!"Todos".equals(tipo)) {
            if (!brigada.getTipo().equalsIgnoreCase(tipo)) {
                return false;
            }
        }

        return true;
    }

    private JPanel crearTarjetaBrigada(Brigada brigada) {
        String icono = obtenerIconoBrigada(brigada);
        String coordinadorNombre = brigada.getCoordinador() != null ?
                brigada.getCoordinador().getNombre() : "Sin coordinador asignado";
        int totalVoluntarios = brigada.getVoluntarios().size();
        Color colorEstado = obtenerColorEstadoBrigada(brigada.getEstado());

        return crearTarjetaBrigadaDetallada(
                brigada.getNombre(),
                icono,
                brigada.getTipo(),
                " " + brigada.getEstado(),
                " " + brigada.getZona(),
                coordinadorNombre,
                brigada.getDescripcion(),
                totalVoluntarios,
                brigada.getId(),
                colorEstado
        );
    }

    private String obtenerIconoBrigada(Brigada brigada) {
        String tipo = brigada.getTipo().toLowerCase();

        if (tipo.contains("alimento")) {
            return "🍎";
        } else if (tipo.contains("salud") || tipo.contains("médico")) {
            return "🏥";
        } else if (tipo.contains("educación") || tipo.contains("enseñanza")) {
            return "📚";
        } else if (tipo.contains("medio ambiente") || tipo.contains("ecolog")) {
            return "🌱";
        } else if (tipo.contains("limpieza")) {
            return "🧹";
        } else if (tipo.contains("construcción") || tipo.contains("reparación")) {
            return "🏗️";
        } else if (tipo.contains("distribución") || tipo.contains("entrega")) {
            return "🚚";
        } else if (tipo.contains("emergencia")) {
            return "🚨";
        } else {
            return "🏢";
        }
    }

    private Color obtenerColorEstadoBrigada(String estado) {
        switch (estado) {
            case "Activa":
                return CARD_GREEN;
            case "Inactiva":
                return CARD_RED;
            case "En planificación":
                return CARD_ORANGE;
            default:
                return CARD_PURPLE;
        }
    }

    private JPanel crearTarjetaBrigadaDetallada(String titulo, String icono, String tipo,
                                                String estado, String zona, String coordinador,
                                                String descripcion, int totalVoluntarios,
                                                String brigadaId, Color colorEstado) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(800, 300));

        card.add(crearEncabezadoBrigadaCard(titulo, icono, tipo, estado, colorEstado));
        card.add(Box.createVerticalStrut(15));
        card.add(crearInfoBasicaBrigada(zona, coordinador, totalVoluntarios));
        card.add(Box.createVerticalStrut(15));
        card.add(crearDescripcionBrigada(descripcion));
        card.add(Box.createVerticalStrut(15));
        card.add(crearBotonesAccionBrigada(brigadaId, titulo, descripcion, tipo, zona,
                coordinador, totalVoluntarios, estado));

        return card;
    }

    private JPanel crearEncabezadoBrigadaCard(String titulo, String icono, String tipo,
                                              String estado, Color colorEstado) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(760, 60));

        JPanel titleGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleGroup.setOpaque(false);

        JLabel brigadaIcon = new JLabel(icono);
        brigadaIcon.setFont(new Font("Arial", Font.BOLD, 24));
        titleGroup.add(brigadaIcon);

        JPanel textGroup = new JPanel();
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
        textGroup.setOpaque(false);
        textGroup.setMaximumSize(new Dimension(600, 60));

        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textGroup.add(titleLabel);

        JLabel typeLabel = new JLabel("Tipo: " + tipo);
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        typeLabel.setForeground(TEXT_GRAY);
        textGroup.add(typeLabel);

        titleGroup.add(textGroup);
        header.add(titleGroup, BorderLayout.WEST);

        JLabel statusLabel = new JLabel(estado.trim());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(colorEstado);
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusLabel.setBackground(new Color(colorEstado.getRed(), colorEstado.getGreen(),
                colorEstado.getBlue(), 20));
        statusLabel.setOpaque(true);
        header.add(statusLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel crearInfoBasicaBrigada(String zona, String coordinador, int totalVoluntarios) {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        infoPanel.setOpaque(false);
        infoPanel.setMaximumSize(new Dimension(760, 30));

        JLabel zonaLabel = new JLabel("📍 " + zona);
        zonaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(zonaLabel);

        JLabel coordinadorLabel = new JLabel("👤 Coordinador: " + coordinador);
        coordinadorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(coordinadorLabel);

        JLabel voluntariosLabel = new JLabel("👥 Voluntarios: " + totalVoluntarios);
        voluntariosLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(voluntariosLabel);

        return infoPanel;
    }

    private JScrollPane crearDescripcionBrigada(String descripcion) {
        JTextArea descriptionArea = new JTextArea(descripcion);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setMaximumSize(new Dimension(760, 80));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    private JPanel crearBotonesAccionBrigada(String brigadaId, String titulo, String descripcion,
                                             String tipo, String zona, String coordinador,
                                             int totalVoluntarios, String estado) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(760, 50));

        JButton detailsButton = new JButton("Ver Detalles");
        detailsButton.setBackground(Color.WHITE);
        detailsButton.setForeground(PRIMARY_BLUE);
        detailsButton.setFocusPainted(false);
        detailsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsButton.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 1));
        detailsButton.setPreferredSize(new Dimension(120, 35));
        detailsButton.addActionListener(e -> mostrarDetallesBrigada(titulo, brigadaId, tipo,
                zona, coordinador, descripcion, totalVoluntarios, estado));

        JButton volunteersButton = new JButton("Ver Voluntarios");
        volunteersButton.setBackground(new Color(0, 150, 136));
        volunteersButton.setForeground(Color.WHITE);
        volunteersButton.setFocusPainted(false);
        volunteersButton.setFont(new Font("Arial", Font.BOLD, 14));
        volunteersButton.setPreferredSize(new Dimension(140, 35));
        volunteersButton.addActionListener(e -> mostrarVoluntariosBrigada(brigadaId, titulo));

        buttonPanel.add(detailsButton);
        buttonPanel.add(volunteersButton);

        return buttonPanel;
    }

    private void mostrarDetallesBrigada(String titulo, String brigadaId, String tipo,
                                        String zona, String coordinador, String descripcion,
                                        int totalVoluntarios, String estado) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Información detallada de la brigada:\n\n");
        detalles.append("• Brigada: ").append(titulo).append("\n");
        detalles.append("• ID: ").append(brigadaId).append("\n");
        detalles.append("• Tipo: ").append(tipo).append("\n");
        detalles.append("• Zona: ").append(zona).append("\n");
        detalles.append("• Estado: ").append(estado).append("\n");
        detalles.append("• Coordinador: ").append(coordinador).append("\n");
        detalles.append("• Total de voluntarios: ").append(totalVoluntarios).append("\n\n");
        detalles.append("• Descripción:\n").append(descripcion);

        JOptionPane.showMessageDialog(this,
                detalles.toString(),
                "Detalles: " + titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarVoluntariosBrigada(String brigadaId, String nombreBrigada) {
        try {
            List<Voluntario> voluntarios = gestorGeneral.getGestorBrigadas().obtenerVoluntariosDeBrigada(brigadaId);

            if (voluntarios.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay voluntarios asignados a esta brigada.",
                        "Voluntarios de " + nombreBrigada,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder listaVoluntarios = new StringBuilder();
            listaVoluntarios.append("Voluntarios de la brigada '").append(nombreBrigada).append("':\n\n");

            int contador = 1;
            for (Voluntario voluntario : voluntarios) {
                listaVoluntarios.append(contador).append(". ").append(voluntario.getNombre()).append("\n");
                listaVoluntarios.append("   ").append(voluntario.getEmail()).append("\n");
                listaVoluntarios.append("   ").append(voluntario.getTelefono()).append("\n");
                listaVoluntarios.append("   Horas acumuladas: ").append(voluntario.getHorasAcumuladas()).append("h\n");

                if (!voluntario.getHabilidadesTexto().isEmpty()) {
                    listaVoluntarios.append("   Habilidades: ").append(voluntario.getHabilidadesTexto()).append("\n");
                }

                listaVoluntarios.append("\n");
                contador++;
            }

            listaVoluntarios.append("Total de voluntarios: ").append(voluntarios.size());

            JOptionPane.showMessageDialog(this,
                    listaVoluntarios.toString(),
                    "Voluntarios de " + nombreBrigada,
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener los voluntarios: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}