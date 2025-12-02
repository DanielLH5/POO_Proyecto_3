package gui;

import model.Voluntario;
import gestores.GestorGeneral;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class DashboardVoluntarioUI extends JFrame {

    // --- Colores Personalizados ---
    private final Color PRIMARY_BLUE = new Color(25, 118, 210);
    private final Color ACTIVE_MENU_BG = new Color(230, 240, 255);
    private final Color BACKGROUND_GRAY = new Color(248, 248, 248);
    private final Color CARD_GREEN = new Color(76, 175, 80);
    private final Color CARD_PURPLE = new Color(156, 39, 176);

    // Componentes principales
    private Voluntario voluntario;
    private GestorGeneral gestorGeneral;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JCheckBox[] checkBoxDias;

    // Paneles del sistema de cards
    private JPanel inicioPanel;
    private JPanel perfilPanel;
    private JPanel actividadesPanel;

    // Elementos del perfil
    private JTextField perfilNombreField;
    private JTextField perfilTelefonoField;
    private JTextArea perfilHabilidadesArea;
    private JTextField perfilDiasField;

    public DashboardVoluntarioUI(Voluntario voluntario, GestorGeneral gestorGeneral) {
        this.voluntario = voluntario;
        this.gestorGeneral = gestorGeneral;
        initUI();
    }

    private void initUI() {
        setTitle("Brigadas Comunitarias - Voluntario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Sidebar (Oeste)
        add(createSidebar(), BorderLayout.WEST);

        // 2. Header (Norte)
        add(createHeader(), BorderLayout.NORTH);

        // 3. Sistema de paneles intercambiables (Centro)
        setupCardSystem();

        setVisible(true);
    }

    /**
     * Configura el sistema de paneles intercambiables
     */
    private void setupCardSystem() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Crear los diferentes paneles
        inicioPanel = createInicioPanel();
        perfilPanel = createPerfilPanel();
        actividadesPanel = createActividadesPanel();

        // Agregar paneles al cardPanel
        cardPanel.add(inicioPanel, "inicio");
        cardPanel.add(perfilPanel, "perfil");
        cardPanel.add(actividadesPanel, "actividades");

        // Mostrar el panel de inicio por defecto
        cardLayout.show(cardPanel, "inicio");

        // Agregar al frame
        add(cardPanel, BorderLayout.CENTER);
    }

    /**
     * Cambia el panel visible
     */
    private void cambiarPanel(String nombrePanel) {
        cardLayout.show(cardPanel, nombrePanel);
    }

    /**
     * Crea el panel superior (Header).
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Título/Logo
        JLabel logo = new JLabel("Brigadas Comunitarias");
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(logo, BorderLayout.WEST);

        // Información de Usuario y Notificaciones
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);

        // Icono de Notificación (Simulación)
        JLabel notifIcon = new JLabel("🔔");
        notifIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        notifIcon.setForeground(Color.GRAY);
        notifIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        notifIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(DashboardVoluntarioUI.this,
                        "No tienes notificaciones nuevas",
                        "Notificaciones", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        userPanel.add(notifIcon);

        // Avatar/Nombre de Usuario
        JPanel avatarPanel = new JPanel(new BorderLayout(5, 0));
        avatarPanel.setOpaque(false);
        avatarPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        avatarPanel.setBackground(new Color(240, 240, 240));
        avatarPanel.setPreferredSize(new Dimension(120, 30));
        avatarPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        userPanel.add(avatarPanel);
        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    /**
     * Crea el panel de navegación lateral (Sidebar).
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        sidebar.add(Box.createVerticalStrut(20)); // Espacio superior

        // ITEMS DEL MENÚ
        JPanel inicioItem = createMenuItem("Inicio", "🏠", true);
        JPanel perfilItem = createMenuItem("Perfil", "👤", false);
        JPanel actividadesItem = createMenuItem("Actividades", "📅", false);
        JPanel brigadasItem = createMenuItem("Mis Brigadas", "🏢", false);
        JPanel historialItem = createMenuItem("Historial", "📊", false);

        // Action listeners para los items del menú
        inicioItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("inicio");
                updateMenuItemStyle(inicioItem, true);
                updateMenuItemStyle(perfilItem, false);
                updateMenuItemStyle(actividadesItem, false);
            }
        });

        perfilItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("perfil");
                updateMenuItemStyle(inicioItem, false);
                updateMenuItemStyle(perfilItem, true);
                updateMenuItemStyle(actividadesItem, false);
            }
        });

        actividadesItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("actividades");
                updateMenuItemStyle(inicioItem, false);
                updateMenuItemStyle(perfilItem, false);
                updateMenuItemStyle(actividadesItem, true);
            }
        });

        // Items sin funcionalidad aún
        brigadasItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(DashboardVoluntarioUI.this,
                        "Funcionalidad en desarrollo",
                        "Próximamente", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        historialItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(DashboardVoluntarioUI.this,
                        "Funcionalidad en desarrollo",
                        "Próximamente", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        sidebar.add(inicioItem);
        sidebar.add(perfilItem);
        sidebar.add(actividadesItem);
        sidebar.add(brigadasItem);
        sidebar.add(historialItem);

        sidebar.add(Box.createVerticalGlue()); // Empuja los elementos hacia arriba

        // Botón de cerrar sesión
        JPanel logoutPanel = createMenuItem("Cerrar Sesión", "🚪", false);
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
        sidebar.add(logoutPanel);
        sidebar.add(Box.createVerticalStrut(20));

        return sidebar;
    }

    /**
     * Actualiza el estilo de un item del menú
     */
    private void updateMenuItemStyle(JPanel item, boolean isActive) {
        Component[] components = item.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().length() <= 2) { // Es un emoji/icono
                    label.setForeground(isActive ? PRIMARY_BLUE : Color.GRAY);
                } else { // Es texto
                    label.setFont(new Font("Arial", isActive ? Font.BOLD : Font.PLAIN, 14));
                    label.setForeground(isActive ? PRIMARY_BLUE : Color.BLACK);
                }
            }
        }
        item.setBackground(isActive ? ACTIVE_MENU_BG : Color.WHITE);
        item.setBorder(isActive ?
                BorderFactory.createMatteBorder(0, 4, 0, 0, PRIMARY_BLUE) :
                BorderFactory.createEmptyBorder(0, 4, 0, 0));
    }

    /**
     * Crea un elemento de menú con estilo.
     */
    private JPanel createMenuItem(String text, String icon, boolean isActive) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isActive) {
            item.setBackground(ACTIVE_MENU_BG);
            item.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, PRIMARY_BLUE));
        } else {
            item.setBackground(Color.WHITE);
            item.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        }

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 16));
        iconLabel.setForeground(isActive ? PRIMARY_BLUE : Color.GRAY);
        item.add(iconLabel);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", isActive ? Font.BOLD : Font.PLAIN, 14));
        textLabel.setForeground(isActive ? PRIMARY_BLUE : Color.BLACK);
        item.add(textLabel);

        return item;
    }

    /**
     * Crea el panel de inicio (dashboard principal)
     */
    private JPanel createInicioPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        // Título de bienvenida
        JLabel welcomeTitle = new JLabel("¡Hola, " + voluntario.getNombre() + "!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(welcomeTitle);

        JLabel subtitle = new JLabel("Encuentra y únete a brigadas cerca de ti");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        panel.add(Box.createVerticalStrut(30));

        // Panel de Tarjetas de Estadísticas
        panel.add(createStatsPanel());

        panel.add(Box.createVerticalStrut(30));

        // Panel de Información Personal (resumen)
        panel.add(createPersonalInfoResumen());

        panel.add(Box.createVerticalStrut(30));

        // Tarjeta de Actividad destacada
        JLabel featuredTitle = new JLabel("Actividad Destacada");
        featuredTitle.setFont(new Font("Arial", Font.BOLD, 20));
        featuredTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(featuredTitle);

        panel.add(Box.createVerticalStrut(15));

        panel.add(createActivityCard(
                "Distribución de Alimentos",
                "🍽️",
                "Ayuda Comunitaria Zona Norte - Brigada Alimentaria",
                "6 cupos disponibles",
                "📅 25 Nov 2025 • 9:00 AM - 12:00 PM",
                "📍 Zona Norte, Barrio Las Flores, San José",
                "Participa en la distribución de alimentos a familias necesitadas en la zona norte de la ciudad.",
                14,
                20
        ));

        panel.add(Box.createVerticalGlue());

        return (JPanel) scrollPane.getViewport().getView();
    }

    /**
     * Crea el panel de edición de perfil
     */
    /**
     * Crea el panel de edición de perfil
     */
    private JPanel createPerfilPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título
        JLabel title = new JLabel("Mi Perfil");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);

        JLabel subtitle = new JLabel("Actualiza tu información personal");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        panel.add(Box.createVerticalStrut(30));

        // Formulario de edición de perfil
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // Nombre
        formPanel.add(createFormLabel("Nombre Completo"));
        perfilNombreField = new JTextField(voluntario.getNombre());
        perfilNombreField.setMaximumSize(new Dimension(400, 35));
        perfilNombreField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(perfilNombreField);
        formPanel.add(Box.createVerticalStrut(15));

        // Email (solo lectura)
        formPanel.add(createFormLabel("Email"));
        JTextField emailField = new JTextField(voluntario.getEmail());
        emailField.setMaximumSize(new Dimension(400, 35));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(15));

        // Teléfono
        formPanel.add(createFormLabel("Teléfono"));
        perfilTelefonoField = new JTextField(voluntario.getTelefono());
        perfilTelefonoField.setMaximumSize(new Dimension(400, 35));
        perfilTelefonoField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(perfilTelefonoField);
        formPanel.add(Box.createVerticalStrut(15));

        // Habilidades
        formPanel.add(createFormLabel("Habilidades (separadas por comas)"));
        perfilHabilidadesArea = new JTextArea(voluntario.getHabilidadesTexto(), 3, 30);
        perfilHabilidadesArea.setLineWrap(true);
        perfilHabilidadesArea.setWrapStyleWord(true);
        perfilHabilidadesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane habilidadesScroll = new JScrollPane(perfilHabilidadesArea);
        habilidadesScroll.setMaximumSize(new Dimension(400, 80));
        formPanel.add(habilidadesScroll);
        formPanel.add(Box.createVerticalStrut(15));

        // Días de disponibilidad - Checkboxes
        formPanel.add(createFormLabel("Días de Disponibilidad"));

        // Panel principal para días
        JPanel diasPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        diasPanel.setMaximumSize(new Dimension(400, 120));
        diasPanel.setBackground(Color.WHITE);

        // Crear checkboxes para cada día
        checkBoxDias = new JCheckBox[7];
        String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        // Obtener días del voluntario como List<String>
        java.util.List<String> diasVoluntario = voluntario.getDiasDisponibles();

        for (int i = 0; i < diasSemana.length; i++) {
            checkBoxDias[i] = new JCheckBox(diasSemana[i]);
            checkBoxDias[i].setBackground(Color.WHITE);
            checkBoxDias[i].setFont(new Font("Arial", Font.PLAIN, 12));

            // Verificar si el voluntario tiene este día seleccionado
            checkBoxDias[i].setSelected(diasVoluntario.contains(diasSemana[i]));

            diasPanel.add(checkBoxDias[i]);
        }

        formPanel.add(diasPanel);
        formPanel.add(Box.createVerticalStrut(5));

        // Panel para botones rápidos
        JPanel botonesRapidosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        botonesRapidosPanel.setMaximumSize(new Dimension(400, 35));
        botonesRapidosPanel.setBackground(Color.WHITE);

        JButton btnLaborables = new JButton("Lunes a Viernes");
        btnLaborables.setFont(new Font("Arial", Font.PLAIN, 11));
        btnLaborables.setFocusPainted(false);
        btnLaborables.addActionListener(e -> {
            for (JCheckBox cb : checkBoxDias) {
                String dia = cb.getText();
                cb.setSelected(dia.equals("Lunes") || dia.equals("Martes") ||
                        dia.equals("Miércoles") || dia.equals("Jueves") ||
                        dia.equals("Viernes"));
            }
        });

        JButton btnFinesSemana = new JButton("Fines de Semana");
        btnFinesSemana.setFont(new Font("Arial", Font.PLAIN, 11));
        btnFinesSemana.setFocusPainted(false);
        btnFinesSemana.addActionListener(e -> {
            for (JCheckBox cb : checkBoxDias) {
                String dia = cb.getText();
                cb.setSelected(dia.equals("Sábado") || dia.equals("Domingo"));
            }
        });

        JButton btnTodaSemana = new JButton("Toda la Semana");
        btnTodaSemana.setFont(new Font("Arial", Font.PLAIN, 11));
        btnTodaSemana.setFocusPainted(false);
        btnTodaSemana.addActionListener(e -> {
            for (JCheckBox cb : checkBoxDias) {
                cb.setSelected(true);
            }
        });

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Arial", Font.PLAIN, 11));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.addActionListener(e -> {
            for (JCheckBox cb : checkBoxDias) {
                cb.setSelected(false);
            }
        });

        botonesRapidosPanel.add(btnLaborables);
        botonesRapidosPanel.add(btnFinesSemana);
        botonesRapidosPanel.add(btnTodaSemana);
        botonesRapidosPanel.add(btnLimpiar);

        formPanel.add(botonesRapidosPanel);
        formPanel.add(Box.createVerticalStrut(15));

        // Información de solo lectura
        formPanel.add(createFormLabel("Información del Sistema"));
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        infoPanel.setOpaque(false);
        infoPanel.add(new JLabel("ID de Usuario:"));
        infoPanel.add(new JLabel(voluntario.getId()));
        infoPanel.add(new JLabel("Horas Acumuladas:"));
        infoPanel.add(new JLabel(voluntario.getHorasAcumuladas() + " horas"));
        infoPanel.setMaximumSize(new Dimension(400, 60));
        formPanel.add(infoPanel);
        formPanel.add(Box.createVerticalStrut(30));

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton guardarButton = new JButton("Guardar Cambios");
        guardarButton.setBackground(PRIMARY_BLUE);
        guardarButton.setForeground(Color.WHITE);
        guardarButton.setFocusPainted(false);
        guardarButton.setFont(new Font("Arial", Font.BOLD, 14));
        guardarButton.addActionListener(e -> guardarPerfil());
        buttonPanel.add(guardarButton);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setBackground(Color.LIGHT_GRAY);
        cancelarButton.setForeground(Color.BLACK);
        cancelarButton.setFocusPainted(false);
        cancelarButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelarButton.addActionListener(e -> cargarDatosPerfil());
        buttonPanel.add(cancelarButton);

        formPanel.add(buttonPanel);
        panel.add(formPanel);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Crea el panel de actividades
     */
    private JPanel createActividadesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        // Título
        JLabel title = new JLabel("Actividades Disponibles");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);

        JLabel subtitle = new JLabel("Encuentra y únete a brigadas cerca de ti");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        panel.add(Box.createVerticalStrut(30));

        // Panel de Filtro
        panel.add(createFilterPanel());

        panel.add(Box.createVerticalStrut(30));

        // Tarjetas de actividades
        panel.add(createActivityCard(
                "Distribución de Alimentos",
                "🍽️",
                "Ayuda Comunitaria Zona Norte - Brigada Alimentaria",
                "6 cupos disponibles",
                "📅 25 Nov 2025 • 9:00 AM - 12:00 PM",
                "📍 Zona Norte, Barrio Las Flores, San José",
                "Participa en la distribución de alimentos a familias necesitadas en la zona norte de la ciudad. Se requiere ayuda para organizar, empaquetar y distribuir los víveres.",
                14,
                20
        ));

        panel.add(Box.createVerticalStrut(20));

        panel.add(createActivityCard(
                "Limpieza Comunitaria",
                "🧹",
                "Mantenimiento de Espacios Públicos - Brigada Ecológica",
                "8 cupos disponibles",
                "📅 28 Nov 2025 • 8:00 AM - 11:00 AM",
                "📍 Parque Central, Centro de la Ciudad",
                "Únete a la jornada de limpieza del Parque Central. Actividad apta para todas las edades. Se proveerán guantes, bolsas y herramientas necesarias.",
                12,
                20
        ));

        panel.add(Box.createVerticalStrut(20));

        panel.add(createActivityCard(
                "Taller de Primeros Auxilios",
                "🩹",
                "Capacitación Comunitaria - Brigada de Salud",
                "15 cupos disponibles",
                "📅 30 Nov 2025 • 2:00 PM - 5:00 PM",
                "📍 Centro de Salud Comunitario, Barrio Sur",
                "Aprende técnicas básicas de primeros auxilios con instructores certificados. Taller teórico-práctico abierto a toda la comunidad. Certificado de participación incluido.",
                5,
                20
        ));

        panel.add(Box.createVerticalGlue());

        return (JPanel) scrollPane.getViewport().getView();
    }

    /**
     * Crea una etiqueta para formulario
     */
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // Agrega estas variables de instancia junto con las otras


    // Actualiza el método cargarDatosPerfil()
    private void cargarDatosPerfil() {
        perfilNombreField.setText(voluntario.getNombre());
        perfilTelefonoField.setText(voluntario.getTelefono());
        perfilHabilidadesArea.setText(voluntario.getHabilidadesTexto());

        // Cargar días en checkboxes
        if (checkBoxDias != null) {
            java.util.List<String> diasVoluntario = voluntario.getDiasDisponibles();

            for (JCheckBox cb : checkBoxDias) {
                cb.setSelected(diasVoluntario.contains(cb.getText()));
            }
        }
    }

    private void guardarPerfil() {
        // Validaciones básicas
        if (perfilNombreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre no puede estar vacío",
                    "Error", JOptionPane.ERROR_MESSAGE);
            perfilNombreField.requestFocus();
            return;
        }

        if (perfilTelefonoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El teléfono no puede estar vacío",
                    "Error", JOptionPane.ERROR_MESSAGE);
            perfilTelefonoField.requestFocus();
            return;
        }

        try {
            // Actualizar objeto voluntario
            voluntario.setNombre(perfilNombreField.getText().trim());
            voluntario.setTelefono(perfilTelefonoField.getText().trim());
            voluntario.setHabilidadesTexto(perfilHabilidadesArea.getText().trim());

            // Guardar días de checkboxes como List<String>
            java.util.List<String> diasSeleccionados = new ArrayList<>();
            if (checkBoxDias != null) {
                for (JCheckBox cb : checkBoxDias) {
                    if (cb.isSelected()) {
                        diasSeleccionados.add(cb.getText());
                    }
                }
            }
            voluntario.setDiasDisponibles(diasSeleccionados);

            // TODO: Aquí deberías llamar al gestor para actualizar en persistencia
            // gestorGeneral.actualizarVoluntario(voluntario);

            JOptionPane.showMessageDialog(this,
                    "¡Perfil actualizado exitosamente!",
                    "Perfil Actualizado", JOptionPane.INFORMATION_MESSAGE);

            // Actualizar header si es necesario
            // ...

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar el perfil: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea el panel con las tres tarjetas de estadísticas.
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 1. Mis Participaciones
        statsPanel.add(createStatsCard("Mis Participaciones", "12", "📅", PRIMARY_BLUE));

        // 2. Horas Aportadas
        statsPanel.add(createStatsCard("Horas Aportadas", voluntario.getHorasAcumuladas() + "h", "🕒", CARD_GREEN));

        // 3. Próximas
        statsPanel.add(createStatsCard("Próximas", "3", "🗓️", CARD_PURPLE));

        return statsPanel;
    }

    /**
     * Crea una tarjeta de estadística individual.
     */
    private JPanel createStatsCard(String title, String value, String icon, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 100));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Contenido de la tarjeta
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);
        content.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.BLACK);
        content.add(valueLabel, BorderLayout.WEST);

        // Icono
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 28));
        iconLabel.setForeground(iconColor);
        content.add(iconLabel, BorderLayout.EAST);

        card.add(content, BorderLayout.CENTER);

        // Action listener para la tarjeta
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(DashboardVoluntarioUI.this,
                        "Ver detalles de: " + title + "\nValor: " + value,
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return card;
    }

    /**
     * Crea el panel de información personal (resumen para inicio)
     */
    private JPanel createPersonalInfoResumen() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

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

        // Información en filas
        String[][] infoData = {
                {"📧 Email:", voluntario.getEmail()},
                {"📞 Teléfono:", voluntario.getTelefono()},
                {"🎯 Habilidades:", voluntario.getHabilidadesTexto()},
                {"📅 Disponibilidad:", voluntario.getDiasDisponiblesTexto()}
        };

        for (String[] data : infoData) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row.setOpaque(false);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel icon = new JLabel(data[0]);
            icon.setFont(new Font("Arial", Font.PLAIN, 14));
            row.add(icon);

            JLabel value = new JLabel(data[1]);
            value.setFont(new Font("Arial", Font.BOLD, 14));
            row.add(value);

            infoPanel.add(row);
        }

        return infoPanel;
    }

    /**
     * Crea el panel de filtro "Tipo de Actividad".
     */
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Título del filtro
        JLabel filterTitle = new JLabel("Filtrar Actividades");
        filterTitle.setFont(new Font("Arial", Font.BOLD, 14));
        filterTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(filterTitle);
        filterPanel.add(Box.createVerticalStrut(10));

        // Contenedor para filtros
        JPanel filtersContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filtersContainer.setOpaque(false);
        filtersContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Filtro por tipo
        JLabel typeLabel = new JLabel("Tipo:");
        filtersContainer.add(typeLabel);

        String[] typeOptions = {"Todas", "Alimentos", "Salud", "Educación", "Medio Ambiente", "Construcción"};
        JComboBox<String> typeCombo = new JComboBox<>(typeOptions);
        typeCombo.setSelectedItem("Todas");
        typeCombo.setPreferredSize(new Dimension(150, 30));
        filtersContainer.add(typeCombo);

        // Filtro por ubicación
        JLabel locationLabel = new JLabel("Ubicación:");
        filtersContainer.add(locationLabel);

        String[] locationOptions = {"Cualquiera", "Zona Norte", "Zona Sur", "Zona Este", "Zona Oeste", "Centro"};
        JComboBox<String> locationCombo = new JComboBox<>(locationOptions);
        locationCombo.setSelectedItem("Cualquiera");
        locationCombo.setPreferredSize(new Dimension(150, 30));
        filtersContainer.add(locationCombo);

        // Botón aplicar filtros
        JButton applyButton = new JButton("Aplicar Filtros");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Filtros aplicados:\n" +
                            "• Tipo: " + typeCombo.getSelectedItem() +
                            "\n• Ubicación: " + locationCombo.getSelectedItem(),
                    "Filtros Aplicados", JOptionPane.INFORMATION_MESSAGE);
        });
        filtersContainer.add(applyButton);

        // Botón limpiar filtros
        JButton clearButton = new JButton("Limpiar");
        clearButton.setBackground(Color.LIGHT_GRAY);
        clearButton.setForeground(Color.BLACK);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> {
            typeCombo.setSelectedItem("Todas");
            locationCombo.setSelectedItem("Cualquiera");
            JOptionPane.showMessageDialog(this,
                    "Filtros limpiados",
                    "Filtros Limpiados", JOptionPane.INFORMATION_MESSAGE);
        });
        filtersContainer.add(clearButton);

        filterPanel.add(filtersContainer);

        return filterPanel;
    }

    /**
     * Crea la tarjeta de listado de actividad detallada.
     */
    private JPanel createActivityCard(String title, String icon, String subtitle,
                                      String slots, String dateTime, String location,
                                      String description, int currentVolunteers, int maxVolunteers) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // 1. Título y Cupos
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titleGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleGroup.setOpaque(false);

        JLabel activityIcon = new JLabel(icon);
        activityIcon.setFont(new Font("Arial", Font.BOLD, 24));
        titleGroup.add(activityIcon);

        JPanel textGroup = new JPanel();
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
        textGroup.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textGroup.add(titleLabel);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        textGroup.add(subtitleLabel);

        titleGroup.add(textGroup);
        header.add(titleGroup, BorderLayout.WEST);

        // Etiqueta de cupos
        JLabel slotsLabel = new JLabel(slots);
        slotsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        slotsLabel.setForeground(CARD_GREEN);
        slotsLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        slotsLabel.setBackground(new Color(232, 245, 233));
        slotsLabel.setOpaque(true);
        header.add(slotsLabel, BorderLayout.EAST);

        card.add(header);
        card.add(Box.createVerticalStrut(15));

        // 2. Fecha, Hora y Ubicación
        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        metaPanel.setOpaque(false);

        JLabel dateTimeLabel = new JLabel(dateTime);
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        metaPanel.add(dateTimeLabel);

        JLabel locationLabel = new JLabel(location);
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        metaPanel.add(locationLabel);

        card.add(metaPanel);
        card.add(Box.createVerticalStrut(15));

        // 3. Descripción
        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        card.add(descriptionArea);
        card.add(Box.createVerticalStrut(15));

        // 4. Barra de Progreso
        JPanel progressPanel = new JPanel(new BorderLayout(10, 0));
        progressPanel.setOpaque(false);

        // Contador de voluntarios
        JLabel volunteerCount = new JLabel(currentVolunteers + "/" + maxVolunteers + " voluntarios inscritos");
        volunteerCount.setFont(new Font("Arial", Font.PLAIN, 12));
        progressPanel.add(volunteerCount, BorderLayout.WEST);

        // Barra de progreso
        JProgressBar progressBar = new JProgressBar(0, maxVolunteers);
        progressBar.setValue(currentVolunteers);
        progressBar.setStringPainted(false);
        progressBar.setForeground(CARD_GREEN);
        progressBar.setBackground(new Color(220, 220, 220));
        progressPanel.add(progressBar, BorderLayout.CENTER);

        card.add(progressPanel);
        card.add(Box.createVerticalStrut(20));

        // 5. Botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton participateButton = new JButton("Participar");
        participateButton.setBackground(PRIMARY_BLUE);
        participateButton.setForeground(Color.WHITE);
        participateButton.setFocusPainted(false);
        participateButton.setFont(new Font("Arial", Font.BOLD, 14));
        participateButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Confirmas tu participación en '" + title + "'?",
                    "Confirmar Participación",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                        "¡Te has inscrito exitosamente en '" + title + "'!\n" +
                                "Recibirás más detalles por correo electrónico.",
                        "Inscripción Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(participateButton);

        JButton detailsButton = new JButton("Ver Detalles");
        detailsButton.setBackground(Color.WHITE);
        detailsButton.setForeground(PRIMARY_BLUE);
        detailsButton.setFocusPainted(false);
        detailsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsButton.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 1));
        detailsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Información detallada de la actividad:\n\n" +
                            "• Actividad: " + title + "\n" +
                            "• Fecha: " + dateTime.split(" • ")[0].replace("📅 ", "") + "\n" +
                            "• Horario: " + dateTime.split(" • ")[1] + "\n" +
                            "• Ubicación: " + location.replace("📍 ", "") + "\n" +
                            "• Descripción: " + description + "\n" +
                            "• Cupos: " + slots + "\n" +
                            "• Inscritos: " + currentVolunteers + "/" + maxVolunteers,
                    "Detalles: " + title,
                    JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(detailsButton);

        card.add(buttonPanel);

        return card;
    }
}