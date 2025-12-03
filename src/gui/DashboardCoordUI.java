package gui;

import gestores.GestorActividades;
import gestores.GestorBrigadas;
import gestores.GestorGeneral;
import gestores.GestorRecursos;
import model.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.Toolkit;
import java.util.List;

public class DashboardCoordUI extends JFrame {

    // --- Colores Personalizados ---
    private final Color PRIMARY_BLUE = new Color(25, 118, 210);
    private final Color ACTIVE_MENU_BG = new Color(230, 240, 255);
    private final Color BACKGROUND_GRAY = new Color(248, 248, 248);
    private final Color CARD_GREEN = new Color(76, 175, 80);
    private final Color CARD_PURPLE = new Color(156, 39, 176);
    private final Color CARD_ORANGE = new Color(255, 152, 0);
    private final Color CARD_RED = new Color(244, 67, 54);

    // Componentes principales
    private Coordinador coordinador;
    private GestorGeneral gestorGeneral;
    private GestorActividades gestorActividades;
    private GestorBrigadas gestorBrigadas;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Paneles del sistema de cards
    private JPanel inicioPanel;
    private JPanel brigadasPanel;
    private JPanel voluntariosPanel;
    private JPanel actividadesPanel;
    private JPanel recursosPanel;
    private JPanel reportesPanel;

    public DashboardCoordUI(Coordinador coordinador, GestorGeneral gestorGeneral) {
        this.coordinador = coordinador;
        this.gestorGeneral = gestorGeneral;
        this.gestorActividades = gestorGeneral.getGestorActividades();
        this.gestorBrigadas = gestorGeneral.getGestorBrigadas();
        initUI();
    }

    private void initUI() {
        setTitle("Brigadas Comunitarias - Coordinador");
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
        brigadasPanel = createBrigadasPanel();
        voluntariosPanel = createVoluntariosPanel();
        actividadesPanel = createActividadesPanel();
        recursosPanel = createRecursosPanel();
        reportesPanel = createReportesPanel();

        // Agregar paneles al cardPanel
        cardPanel.add(inicioPanel, "inicio");
        cardPanel.add(brigadasPanel, "brigadas");
        cardPanel.add(voluntariosPanel, "voluntarios");
        cardPanel.add(actividadesPanel, "actividades");
        cardPanel.add(recursosPanel, "recursos");
        cardPanel.add(reportesPanel, "reportes");

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
        JLabel logo = new JLabel("Brigadas Comunitarias - Coordinación");
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(logo, BorderLayout.WEST);

        // Información de Usuario y Notificaciones
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);

        // Avatar/Nombre de Usuario
        JPanel avatarPanel = new JPanel(new BorderLayout(5, 0));
        avatarPanel.setOpaque(false);
        avatarPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        avatarPanel.setBackground(new Color(240, 240, 240));
        avatarPanel.setPreferredSize(new Dimension(180, 30));
        avatarPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel avatarName = new JLabel(coordinador.getNombre(), SwingConstants.RIGHT);
        avatarName.setFont(new Font("Arial", Font.BOLD, 12));
        avatarPanel.add(avatarName, BorderLayout.CENTER);

        JLabel avatarRole = new JLabel("Coordinador - " + coordinador.getAreaResponsabilidad(), SwingConstants.RIGHT);
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

        // ITEMS DEL MENÚ - Funcionalidades de Coordinador
        JPanel inicioItem = createMenuItem("Inicio", "🏠", true);
        JPanel brigadasItem = createMenuItem("Brigadas", "🏢", false);
        JPanel voluntariosItem = createMenuItem("Voluntarios", "👥", false);
        JPanel actividadesItem = createMenuItem("Actividades", "📅", false);
        JPanel recursosItem = createMenuItem("Recursos", "📦", false);
        JPanel reportesItem = createMenuItem("Reportes", "📊", false);

        // Action listeners para los items del menú
        inicioItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("inicio");
                updateMenuItemStyle(inicioItem, true);
                updateMenuItemStyle(brigadasItem, false);
                updateMenuItemStyle(voluntariosItem, false);
                updateMenuItemStyle(actividadesItem, false);
                updateMenuItemStyle(recursosItem, false);
                updateMenuItemStyle(reportesItem, false);
            }
        });

        brigadasItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("brigadas");
                updateMenuItemStyle(inicioItem, false);
                updateMenuItemStyle(brigadasItem, true);
                updateMenuItemStyle(voluntariosItem, false);
                updateMenuItemStyle(actividadesItem, false);
                updateMenuItemStyle(recursosItem, false);
                updateMenuItemStyle(reportesItem, false);
            }
        });

        voluntariosItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("voluntarios");
                updateMenuItemStyle(inicioItem, false);
                updateMenuItemStyle(brigadasItem, false);
                updateMenuItemStyle(voluntariosItem, true);
                updateMenuItemStyle(actividadesItem, false);
                updateMenuItemStyle(recursosItem, false);
                updateMenuItemStyle(reportesItem, false);
            }
        });

        actividadesItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("actividades");
                updateMenuItemStyle(inicioItem, false);
                updateMenuItemStyle(brigadasItem, false);
                updateMenuItemStyle(voluntariosItem, false);
                updateMenuItemStyle(actividadesItem, true);
                updateMenuItemStyle(recursosItem, false);
                updateMenuItemStyle(reportesItem, false);
            }
        });

        recursosItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("recursos");
                updateMenuItemStyle(inicioItem, false);
                updateMenuItemStyle(brigadasItem, false);
                updateMenuItemStyle(voluntariosItem, false);
                updateMenuItemStyle(actividadesItem, false);
                updateMenuItemStyle(recursosItem, true);
                updateMenuItemStyle(reportesItem, false);
            }
        });

        reportesItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cambiarPanel("reportes");
                updateMenuItemStyle(inicioItem, false);
                updateMenuItemStyle(brigadasItem, false);
                updateMenuItemStyle(voluntariosItem, false);
                updateMenuItemStyle(actividadesItem, false);
                updateMenuItemStyle(recursosItem, false);
                updateMenuItemStyle(reportesItem, true);
            }
        });

        sidebar.add(inicioItem);
        sidebar.add(brigadasItem);
        sidebar.add(voluntariosItem);
        sidebar.add(actividadesItem);
        sidebar.add(recursosItem);
        sidebar.add(reportesItem);

        sidebar.add(Box.createVerticalGlue()); // Empuja los elementos hacia arriba

        // Botón de cerrar sesión
        JPanel logoutPanel = createMenuItem("Cerrar Sesión", "🚪", false);
        logoutPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int confirm = JOptionPane.showConfirmDialog(
                        DashboardCoordUI.this,
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
     * Crea el panel de gestión de voluntarios (CRUD completo + asociar/desasociar)
     */
    private JPanel createVoluntariosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_GRAY);

        JLabel title = new JLabel("Gestión de Voluntarios");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("RF-02: Registrar voluntarios | RF-03: Asociar a brigadas");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Barra de herramientas
        panel.add(createToolbarVoluntarios(), BorderLayout.NORTH);

        // Tabla de voluntarios
        String[] columnNames = {"ID", "Nombre", "Email", "Teléfono", "Habilidades", "Brigadas Asociadas", "Acciones"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de acciones es editable
            }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(40);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));

        // Configurar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150); // Email
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Teléfono
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150); // Habilidades
        tabla.getColumnModel().getColumn(5).setPreferredWidth(150); // Brigadas Asociadas
        tabla.getColumnModel().getColumn(6).setPreferredWidth(180); // Acciones

        // **** AQUÍ DEBEN ESTAR ESTAS LÍNEAS ****
        tabla.getColumnModel().getColumn(6).setCellRenderer(new VoluntariosButtonRenderer());
        tabla.getColumnModel().getColumn(6).setCellEditor(new VoluntariosButtonEditor(new JCheckBox(), tabla));

        // Cargar datos
        cargarVoluntariosEnTabla(model);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(1100, 500));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea la barra de herramientas para voluntarios
     */
    private JPanel createToolbarVoluntarios() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Botón Nuevo Voluntario
        JButton nuevoBtn = createToolbarButton("Nuevo Voluntario", "Registrar nuevo voluntario",
                CARD_GREEN, e -> mostrarFormularioNuevoVoluntario());
        toolbar.add(nuevoBtn);

        // Campo de búsqueda
        JTextField searchField = new JTextField(25);
        searchField.setToolTipText("Buscar voluntario por nombre, email o habilidades");
        toolbar.add(searchField);

        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.addActionListener(e -> buscarVoluntarios(searchField.getText()));
        toolbar.add(buscarBtn);

        // Botón Actualizar
        JButton actualizarBtn = createToolbarButton("Actualizar", "Actualizar lista de voluntarios",
                PRIMARY_BLUE, e -> actualizarTablaVoluntarios());
        toolbar.add(actualizarBtn);

        return toolbar;
    }

    /**
     * Crea un botón de barra de herramientas con estilo
     */
    private JButton createToolbarButton(String text, String tooltip, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setToolTipText(tooltip);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    /**
     * Renderizador para botones en la tabla de brigadas - VERSIÓN CORREGIDA
     */
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton editarBtn;
        private JButton eliminarBtn;
        private JButton verBtn;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 3, 5)); // Reducir espacio horizontal
            setOpaque(true);
            setPreferredSize(new Dimension(180, 40)); // Tamaño ajustado

            // Botón Editar - mejor diseño
            editarBtn = new JButton("Editar");
            editarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editarBtn.setBackground(new Color(33, 150, 243)); // Azul
            editarBtn.setForeground(Color.WHITE);
            editarBtn.setFocusPainted(false);
            editarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            editarBtn.setPreferredSize(new Dimension(55, 25));
            editarBtn.setOpaque(true);
            editarBtn.setContentAreaFilled(true);
            editarBtn.setBorderPainted(true);

            // Botón Eliminar
            eliminarBtn = new JButton("Eliminar");
            eliminarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            eliminarBtn.setBackground(new Color(244, 67, 54)); // Rojo
            eliminarBtn.setForeground(Color.WHITE);
            eliminarBtn.setFocusPainted(false);
            eliminarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            eliminarBtn.setPreferredSize(new Dimension(60, 25));
            eliminarBtn.setOpaque(true);
            eliminarBtn.setContentAreaFilled(true);
            eliminarBtn.setBorderPainted(true);

            // Botón Ver
            verBtn = new JButton("Ver");
            verBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            verBtn.setBackground(new Color(76, 175, 80)); // Verde
            verBtn.setForeground(Color.WHITE);
            verBtn.setFocusPainted(false);
            verBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            verBtn.setPreferredSize(new Dimension(45, 25));
            verBtn.setOpaque(true);
            verBtn.setContentAreaFilled(true);
            verBtn.setBorderPainted(true);

            // Agregar botones al panel
            add(editarBtn);
            add(eliminarBtn);
            add(verBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * Editor para botones en la tabla de brigadas - VERSIÓN CORREGIDA
     */
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton editarBtn;
        private JButton eliminarBtn;
        private JButton verBtn;
        private JTable tabla;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox, JTable tabla) {
            this.tabla = tabla;

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.setOpaque(true);
            panel.setPreferredSize(new Dimension(200, 40));

            // Botón Editar
            editarBtn = new JButton("Editar");
            editarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editarBtn.setBackground(new Color(33, 150, 243)); // Azul
            editarBtn.setForeground(Color.WHITE);
            editarBtn.setFocusPainted(false);
            editarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            editarBtn.setPreferredSize(new Dimension(55, 25));
            editarBtn.addActionListener(e -> {
                editarBrigadaDesdeTabla();
                fireEditingStopped();
            });

            // Botón Eliminar
            eliminarBtn = new JButton("Eliminar");
            eliminarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            eliminarBtn.setBackground(new Color(244, 67, 54)); // Rojo
            eliminarBtn.setForeground(Color.WHITE);
            eliminarBtn.setFocusPainted(false);
            eliminarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            eliminarBtn.setPreferredSize(new Dimension(60, 25));
            eliminarBtn.addActionListener(e -> {
                eliminarBrigadaDesdeTabla();
                fireEditingStopped();
            });

            // Botón Ver
            verBtn = new JButton("Ver");
            verBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            verBtn.setBackground(new Color(76, 175, 80)); // Verde
            verBtn.setForeground(Color.WHITE);
            verBtn.setFocusPainted(false);
            verBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            verBtn.setPreferredSize(new Dimension(45, 25));
            verBtn.addActionListener(e -> {
                verDetallesBrigada();
                fireEditingStopped();
            });

            // Agregar botones al panel
            panel.add(editarBtn);
            panel.add(eliminarBtn);
            panel.add(verBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Editar|Eliminar|Ver";
        }

        private void editarBrigadaDesdeTabla() {
            String id = (String) tabla.getValueAt(currentRow, 0);
            Brigada brigada = obtenerBrigadaPorId(id);
            if (brigada != null) {
                mostrarFormularioEditarBrigada(brigada);
            }
        }

        private Brigada obtenerBrigadaPorId(String id) {
            try {
                return gestorGeneral.getGestorBrigadas().buscarBrigadaPorId(id);
            } catch (Exception e) {
                System.err.println("Error obteniendo brigada por ID: " + e.getMessage());
                return null;
            }
        }

        private void eliminarBrigadaDesdeTabla() {
            String id = (String) tabla.getValueAt(currentRow, 0);
            String nombre = (String) tabla.getValueAt(currentRow, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    tabla,
                    "¿Está seguro que desea eliminar la brigada:\n" +
                            nombre + " (ID: " + id + ")?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Detener la edición ANTES de eliminar
                fireEditingStopped(); // <-- Añadir esta línea aquí

                // Luego eliminar
                eliminarBrigada(id, nombre);
            } else {
                // Si cancelan, también detener la edición
                fireEditingStopped();
            }
        }

        private void verDetallesBrigada() {
            String id = (String) tabla.getValueAt(currentRow, 0);
            Brigada brigada = obtenerBrigadaPorId(id);
            if (brigada != null) {
                StringBuilder detalles = new StringBuilder();
                detalles.append("Detalles de la brigada:\n");
                detalles.append("ID: ").append(brigada.getId()).append("\n");
                detalles.append("Nombre: ").append(brigada.getNombre()).append("\n");
                detalles.append("Tipo: ").append(brigada.getTipo()).append("\n");
                detalles.append("Zona: ").append(brigada.getZona()).append("\n");
                detalles.append("Estado: ").append(brigada.getEstado()).append("\n");
                detalles.append("Descripción: ").append(brigada.getDescripcion()).append("\n");
                detalles.append("Coordinador: ").append(brigada.getCoordinador().getNombre()).append("\n");
                detalles.append("Voluntarios: ").append(brigada.getCantidadVoluntarios());

                JOptionPane.showMessageDialog(tabla,
                        detalles.toString(),
                        "Detalles de Brigada",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void eliminarBrigada(String id, String nombre) {
            try {
                // Verificar si la brigada tiene voluntarios asignados
                List<Voluntario> voluntarios = gestorGeneral.getGestorBrigadas().obtenerVoluntariosDeBrigada(id);

                if (!voluntarios.isEmpty()) {
                    int confirm = JOptionPane.showConfirmDialog(
                            tabla,
                            "La brigada tiene " + voluntarios.size() + " voluntarios asignados.\n" +
                                    "¿Desea eliminar la brigada de todos modos?\n" +
                                    "(Los voluntarios serán desasociados de esta brigada)",
                            "Confirmar Eliminación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                // Eliminar la brigada usando el gestor
                gestorGeneral.getGestorBrigadas().eliminarBrigada(id);

                JOptionPane.showMessageDialog(tabla,
                        "Brigada '" + nombre + "' eliminada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar tabla usando SwingUtilities.invokeLater
                SwingUtilities.invokeLater(() -> {
                    actualizarTablaBrigadas();
                });

            } catch (Exception e) {
                JOptionPane.showMessageDialog(tabla,
                        "Error al eliminar brigada: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para mostrar formulario de edición de brigada
     */
    private void mostrarFormularioEditarBrigada(Brigada brigada) {
        JDialog dialog = new JDialog(this, "Editar Brigada: " + brigada.getNombre(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario con datos actuales
        JLabel idLabel = new JLabel(brigada.getId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JTextField nombreField = new JTextField(brigada.getNombre(), 20);

        // Combo box para tipo
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{
                "Alimentos", "Medio Ambiente", "Salud", "Educación", "Construcción", "Otros"
        });
        tipoCombo.setSelectedItem(brigada.getTipo());

        // Combo box para zona
        JComboBox<String> zonaCombo = new JComboBox<>(new String[]{
                "Zona Norte", "Centro", "Zona Sur", "Zona Este", "Zona Oeste", "Todas las zonas"
        });
        zonaCombo.setSelectedItem(brigada.getZona());

        // Combo box para estado
        JComboBox<String> estadoCombo = new JComboBox<>(new String[]{
                "En planificación", "Activa", "Inactiva"
        });
        estadoCombo.setSelectedItem(brigada.getEstado());

        JTextArea descripcionArea = new JTextArea(brigada.getDescripcion(), 4, 20);
        descripcionArea.setLineWrap(true);

        // Configurar GridBagConstraints
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("ID de Brigada:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nombre*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tipoCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Zona:"), gbc);
        gbc.gridx = 1;
        formPanel.add(zonaCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        formPanel.add(estadoCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(descripcionArea), gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Guardar Cambios");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "El nombre es requerido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Actualizar datos de la brigada
                brigada.setNombre(nombre);
                brigada.setTipo((String) tipoCombo.getSelectedItem());
                brigada.setZona((String) zonaCombo.getSelectedItem());
                brigada.setEstado((String) estadoCombo.getSelectedItem());
                brigada.setDescripcion(descripcionArea.getText().trim());

                // Guardar cambios usando el gestor
                gestorGeneral.getGestorBrigadas().actualizarBrigada(brigada);

                JOptionPane.showMessageDialog(dialog,
                        "Brigada '" + nombre + "' actualizada exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaBrigadas();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al actualizar brigada: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(PRIMARY_BLUE);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }


    /**
     * Renderizador para botones de voluntarios - VERSIÓN CORREGIDA Y MEJORADA
     */
    class VoluntariosButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton editarBtn;
        private JButton asociarBtn;
        private JButton eliminarBtn;

        public VoluntariosButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 3, 5));
            setOpaque(true);
            setPreferredSize(new Dimension(200, 40)); // Tamaño ajustado

            // Botón Editar - MEJOR VISIBILIDAD
            editarBtn = new JButton("Editar");
            editarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editarBtn.setBackground(new Color(33, 150, 243)); // Azul
            editarBtn.setForeground(Color.WHITE);
            editarBtn.setFocusPainted(false);
            editarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            editarBtn.setPreferredSize(new Dimension(55, 25));
            editarBtn.setOpaque(true);
            editarBtn.setContentAreaFilled(true);
            editarBtn.setBorderPainted(true);
            editarBtn.setToolTipText("Editar información del voluntario");

            // Botón Asociar - MEJOR VISIBILIDAD
            asociarBtn = new JButton("Asociar");
            asociarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            asociarBtn.setBackground(new Color(156, 39, 176)); // Morado
            asociarBtn.setForeground(Color.WHITE);
            asociarBtn.setFocusPainted(false);
            asociarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            asociarBtn.setPreferredSize(new Dimension(60, 25));
            asociarBtn.setOpaque(true);
            asociarBtn.setContentAreaFilled(true);
            asociarBtn.setBorderPainted(true);
            asociarBtn.setToolTipText("Asociar/desasociar a brigadas");

            // Botón Eliminar - MEJOR VISIBILIDAD
            eliminarBtn = new JButton("Eliminar");
            eliminarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            eliminarBtn.setBackground(new Color(244, 67, 54)); // Rojo
            eliminarBtn.setForeground(Color.WHITE);
            eliminarBtn.setFocusPainted(false);
            eliminarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            eliminarBtn.setPreferredSize(new Dimension(60, 25));
            eliminarBtn.setOpaque(true);
            eliminarBtn.setContentAreaFilled(true);
            eliminarBtn.setBorderPainted(true);
            eliminarBtn.setToolTipText("Eliminar voluntario del sistema");

            // Agregar botones al panel
            add(editarBtn);
            add(asociarBtn);
            add(eliminarBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * Editor para botones de voluntarios - VERSIÓN CORREGIDA
     */
    class VoluntariosButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton editarBtn;
        private JButton asociarBtn;
        private JButton eliminarBtn;
        private JTable tabla;
        private int currentRow;

        public VoluntariosButtonEditor(JCheckBox checkBox, JTable tabla) {
            this.tabla = tabla;

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 5));
            panel.setOpaque(true);
            panel.setPreferredSize(new Dimension(200, 40));

            // Botón Editar - MEJOR VISIBILIDAD
            editarBtn = new JButton("Editar");
            editarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editarBtn.setBackground(new Color(33, 150, 243));
            editarBtn.setForeground(Color.WHITE);
            editarBtn.setFocusPainted(false);
            editarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            editarBtn.setPreferredSize(new Dimension(55, 25));
            editarBtn.setOpaque(true);
            editarBtn.setContentAreaFilled(true);
            editarBtn.setBorderPainted(true);
            editarBtn.addActionListener(e -> {
                editarVoluntarioDesdeTabla();
                fireEditingStopped();
            });

            // Botón Asociar - MEJOR VISIBILIDAD
            asociarBtn = new JButton("Asociar");
            asociarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            asociarBtn.setBackground(new Color(156, 39, 176));
            asociarBtn.setForeground(Color.WHITE);
            asociarBtn.setFocusPainted(false);
            asociarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            asociarBtn.setPreferredSize(new Dimension(60, 25));
            asociarBtn.setOpaque(true);
            asociarBtn.setContentAreaFilled(true);
            asociarBtn.setBorderPainted(true);
            asociarBtn.addActionListener(e -> {
                gestionarAsociacionVoluntario();
                fireEditingStopped();
            });

            // Botón Eliminar - MEJOR VISIBILIDAD
            eliminarBtn = new JButton("Eliminar");
            eliminarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            eliminarBtn.setBackground(new Color(244, 67, 54));
            eliminarBtn.setForeground(Color.WHITE);
            eliminarBtn.setFocusPainted(false);
            eliminarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            eliminarBtn.setPreferredSize(new Dimension(60, 25));
            eliminarBtn.setOpaque(true);
            eliminarBtn.setContentAreaFilled(true);
            eliminarBtn.setBorderPainted(true);
            eliminarBtn.addActionListener(e -> {
                eliminarVoluntarioDesdeTabla();
                fireEditingStopped();
            });

            // Agregar botones al panel
            panel.add(editarBtn);
            panel.add(asociarBtn);
            panel.add(eliminarBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Editar|Asociar|Eliminar";
        }

        private void editarVoluntarioDesdeTabla() {
            String id = (String) tabla.getValueAt(currentRow, 0);
            Voluntario voluntario = obtenerVoluntarioPorId(id);
            if (voluntario != null) {
                mostrarFormularioEditarVoluntario(voluntario);
            }
        }

        private void eliminarVoluntarioDesdeTabla() {
            String id = (String) tabla.getValueAt(currentRow, 0);
            String nombre = (String) tabla.getValueAt(currentRow, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    tabla,
                    "¿Está seguro que desea eliminar al voluntario:\n" +
                            nombre + " (ID: " + id + ")?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Detener la edición ANTES de eliminar
                fireEditingStopped(); // <-- Añadir esta línea aquí

                // Luego eliminar
                eliminarVoluntario(id);
            } else {
                // Si cancelan, también detener la edición
                fireEditingStopped();
            }
        }
    }

    /**
     * Carga los voluntarios en la tabla
     */
    private void cargarVoluntariosEnTabla(DefaultTableModel model) {
        model.setRowCount(0); // Limpiar tabla

        try {
            List<Voluntario> voluntarios = gestorGeneral.getGestorUsuarios().obtenerVoluntarios();

            for (Voluntario vol : voluntarios) {
                // Convertir habilidades de List<String> a String
                String habilidadesStr = "";
                if (vol.getHabilidades() != null && !vol.getHabilidades().isEmpty()) {
                    habilidadesStr = String.join(", ", vol.getHabilidades());
                }

                // Obtener brigadas a las que pertenece
                List<Brigada> brigadasDelVoluntario = gestorGeneral.getGestorBrigadas().obtenerBrigadasDeVoluntario(vol.getId());
                String brigadasStr = formatBrigadasList(brigadasDelVoluntario);

                model.addRow(new Object[]{
                        vol.getId(),
                        vol.getNombre(),
                        vol.getEmail(),
                        vol.getTelefono(),
                        habilidadesStr,
                        brigadasStr,
                        "Editar|Asociar|Eliminar"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando voluntarios: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Formatea la lista de brigadas para mostrar en tabla
     */
    private String formatBrigadasList(List<Brigada> brigadas) {
        if (brigadas == null || brigadas.isEmpty()) {
            return "Sin asignar";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(brigadas.size(), 2); i++) {
            sb.append(brigadas.get(i).getNombre());
            if (i < Math.min(brigadas.size(), 2) - 1) {
                sb.append(", ");
            }
        }

        if (brigadas.size() > 2) {
            sb.append(" +").append(brigadas.size() - 2).append(" más");
        }

        return sb.toString();
    }

    /**
     * Muestra formulario para nuevo voluntario (RF-02)
     */
    private void mostrarFormularioNuevoVoluntario() {
        JDialog dialog = new JDialog(this, "Registrar Nuevo Voluntario (RF-02)", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        JTextField idField = new JTextField(generarIdVoluntario(), 20);
        idField.setEditable(false);
        JTextField nombreField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField telefonoField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextArea habilidadesArea = new JTextArea(4, 20);
        habilidadesArea.setLineWrap(true);

        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{
                "Alimentos", "Medio Ambiente", "Salud", "Educación", "Construcción", "Otros"
        });

        // Configurar GridBagConstraints
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("ID (automático):"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nombre completo*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Email*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Contraseña*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        formPanel.add(telefonoField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Habilidades:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(habilidadesArea), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Áreas de interés:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tipoCombo, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Registrar Voluntario");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            // Validar campos
            String nombre = nombreField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Nombre, Email y Contraseña son campos requeridos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de email
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor ingrese un email válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Verificar si el email ya existe
                if (gestorGeneral.getGestorUsuarios().existeEmail(email)) {
                    JOptionPane.showMessageDialog(dialog,
                            "El email ya está registrado en el sistema",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Convertir habilidades de texto a lista
                List<String> habilidadesList = new ArrayList<>();
                String habilidadesTexto = habilidadesArea.getText().trim();
                if (!habilidadesTexto.isEmpty()) {
                    // Separar por comas y limpiar espacios
                    String[] habilidadesArray = habilidadesTexto.split(",\\s*");
                    for (String habilidad : habilidadesArray) {
                        if (!habilidad.trim().isEmpty()) {
                            habilidadesList.add(habilidad.trim());
                        }
                    }
                }

                String diasDisponibles = "Lunes a Viernes";
                gestorGeneral.registrarVoluntario(nombre, telefonoField.getText().trim(), email,
                        password, String.valueOf(habilidadesList), diasDisponibles);

                JOptionPane.showMessageDialog(dialog,
                        "Voluntario '" + nombre + "' registrado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaVoluntarios();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al registrar voluntario: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(CARD_GREEN);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Muestra formulario para editar voluntario
     */
    private void mostrarFormularioEditarVoluntario(Voluntario voluntario) {
        JDialog dialog = new JDialog(this, "Editar Voluntario: " + voluntario.getNombre(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario con datos actuales
        JLabel idLabel = new JLabel(voluntario.getId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JTextField nombreField = new JTextField(voluntario.getNombre(), 20);
        JLabel emailLabel = new JLabel(voluntario.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JTextField telefonoField = new JTextField(voluntario.getTelefono(), 20);

        // Convertir List<String> a String para el JTextArea
        String habilidadesStr = "";
        if (voluntario.getHabilidades() != null && !voluntario.getHabilidades().isEmpty()) {
            habilidadesStr = String.join(", ", voluntario.getHabilidades());
        }
        JTextArea habilidadesArea = new JTextArea(habilidadesStr, 4, 20);
        habilidadesArea.setLineWrap(true);

        // Configurar GridBagConstraints
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nombre*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        formPanel.add(telefonoField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Habilidades:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(habilidadesArea), gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Guardar Cambios");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "El nombre es requerido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Actualizar datos del voluntario
                voluntario.setNombre(nombre);
                voluntario.setTelefono(telefonoField.getText().trim());

                // Convertir String a List<String> para habilidades
                String habilidadesTexto = habilidadesArea.getText().trim();
                List<String> habilidadesList = new ArrayList<>();
                if (!habilidadesTexto.isEmpty()) {
                    String[] habilidadesArray = habilidadesTexto.split(",\\s*");
                    for (String habilidad : habilidadesArray) {
                        if (!habilidad.trim().isEmpty()) {
                            habilidadesList.add(habilidad.trim());
                        }
                    }
                }
                voluntario.setHabilidades(habilidadesList);

                // Guardar cambios
                gestorGeneral.getGestorUsuarios().guardarVoluntario(voluntario);

                JOptionPane.showMessageDialog(dialog,
                        "Voluntario '" + nombre + "' actualizado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaVoluntarios();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al actualizar voluntario: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(PRIMARY_BLUE);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Gestiona la asociación/desasociación de voluntario a brigadas (RF-03)
     */
    private void gestionarAsociacionVoluntario() {
        // Encontrar la tabla en el panel de voluntarios
        Component[] components = voluntariosPanel.getComponents();
        JTable tabla = null;

        for (Component comp : components) {
            if (comp instanceof JScrollPane) {
                JViewport viewport = ((JScrollPane) comp).getViewport();
                if (viewport.getView() instanceof JTable) {
                    tabla = (JTable) viewport.getView();
                    break;
                }
            }
        }

        if (tabla == null) return;

        int selectedRow = tabla.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un voluntario de la tabla",
                    "Seleccionar Voluntario",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idVoluntario = (String) tabla.getValueAt(selectedRow, 0);
        Voluntario voluntario = obtenerVoluntarioPorId(idVoluntario);

        if (voluntario == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el voluntario seleccionado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Asociar/Desasociar Voluntario: " + voluntario.getNombre(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel superior: Información del voluntario
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información del Voluntario"));

        infoPanel.add(new JLabel("Nombre:"));
        infoPanel.add(new JLabel(voluntario.getNombre()));
        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(new JLabel(voluntario.getEmail()));

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Panel central: Lista de brigadas disponibles y asociadas
        JPanel brigadasPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Lista de brigadas disponibles
        JPanel disponiblesPanel = new JPanel(new BorderLayout());
        disponiblesPanel.setBorder(BorderFactory.createTitledBorder("Brigadas Disponibles"));

        List<Brigada> todasBrigadas = gestorGeneral.getGestorBrigadas().obtenerTodasBrigadas();
        List<Brigada> brigadasDelVoluntario = gestorGeneral.getGestorBrigadas().obtenerBrigadasDeVoluntario(voluntario.getId());

        DefaultListModel<String> disponiblesModel = new DefaultListModel<>();
        DefaultListModel<String> asociadasModel = new DefaultListModel<>();

        for (Brigada brigada : todasBrigadas) {
            String item = brigada.getNombre() + " (" + brigada.getTipo() + ") - " + brigada.getEstado();
            if (!brigadasDelVoluntario.contains(brigada)) {
                disponiblesModel.addElement(item);
            } else {
                asociadasModel.addElement(item);
            }
        }

        JList<String> disponiblesList = new JList<>(disponiblesModel);
        JList<String> asociadasList = new JList<>(asociadasModel);

        disponiblesPanel.add(new JScrollPane(disponiblesList), BorderLayout.CENTER);

        // Lista de brigadas asociadas
        JPanel asociadasPanel = new JPanel(new BorderLayout());
        asociadasPanel.setBorder(BorderFactory.createTitledBorder("Brigadas Asociadas"));
        asociadasPanel.add(new JScrollPane(asociadasList), BorderLayout.CENTER);

        brigadasPanel.add(disponiblesPanel);
        brigadasPanel.add(asociadasPanel);

        mainPanel.add(brigadasPanel, BorderLayout.CENTER);

        // Panel inferior: Botones de acción
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton asociarBtn = new JButton("Asociar →");
        asociarBtn.addActionListener(e -> {
            int selectedIndex = disponiblesList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selected = disponiblesModel.getElementAt(selectedIndex);
                // Encontrar la brigada correspondiente
                for (Brigada brigada : todasBrigadas) {
                    String item = brigada.getNombre() + " (" + brigada.getTipo() + ") - " + brigada.getEstado();
                    if (item.equals(selected)) {
                        try {
                            gestorGeneral.getGestorBrigadas().agregarVoluntarioABrigada(
                                    brigada.getId(), voluntario.getId());
                            disponiblesModel.remove(selectedIndex);
                            asociadasModel.addElement(selected);
                            JOptionPane.showMessageDialog(dialog,
                                    "Voluntario asociado a " + brigada.getNombre(),
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(dialog,
                                    "Error al asociar: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                }
            }
        });

        JButton desasociarBtn = new JButton("← Desasociar");
        desasociarBtn.addActionListener(e -> {
            int selectedIndex = asociadasList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selected = asociadasModel.getElementAt(selectedIndex);
                // Encontrar la brigada correspondiente
                for (Brigada brigada : todasBrigadas) {
                    String item = brigada.getNombre() + " (" + brigada.getTipo() + ") - " + brigada.getEstado();
                    if (item.equals(selected)) {
                        try {
                            gestorGeneral.getGestorBrigadas().eliminarVoluntarioDeBrigada(
                                    brigada.getId(), voluntario.getId());
                            asociadasModel.remove(selectedIndex);
                            disponiblesModel.addElement(selected);
                            JOptionPane.showMessageDialog(dialog,
                                    "Voluntario desasociado de " + brigada.getNombre(),
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(dialog,
                                    "Error al desasociar: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                }
            }
        });

        JButton cerrarBtn = new JButton("Cerrar");
        cerrarBtn.addActionListener(e -> {
            dialog.dispose();
            actualizarTablaVoluntarios();
        });

        actionPanel.add(asociarBtn);
        actionPanel.add(desasociarBtn);
        actionPanel.add(Box.createHorizontalStrut(50));
        actionPanel.add(cerrarBtn);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    /**
     * Elimina un voluntario del sistema
     */
    private void eliminarVoluntario(String id) {
        try {
            // Resto del código de eliminación...
            Voluntario voluntario = obtenerVoluntarioPorId(id);
            if (voluntario == null) {
                JOptionPane.showMessageDialog(DashboardCoordUI.this,
                        "No se encontró el voluntario con ID: " + id,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nombreVoluntario = voluntario.getNombre();
            boolean estaEnBrigadas = gestorGeneral.getGestorBrigadas().voluntarioEstaEnAlgunaBrigada(id);

            if (estaEnBrigadas) {
                List<Brigada> brigadasDelVoluntario = gestorGeneral.getGestorBrigadas()
                        .obtenerBrigadasDeVoluntario(id);

                int confirm = JOptionPane.showConfirmDialog(DashboardCoordUI.this,
                        "El voluntario " + nombreVoluntario + " está asignado a " +
                                brigadasDelVoluntario.size() + " brigada(s).\n" +
                                "¿Desea eliminar el voluntario del sistema? (Se desasociará de todas las brigadas)",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    for (Brigada brigada : brigadasDelVoluntario) {
                        try {
                            gestorGeneral.getGestorBrigadas().eliminarVoluntarioDeBrigada(
                                    brigada.getId(), id);
                        } catch (Exception e) {
                            System.err.println("Error eliminando de brigada " + brigada.getNombre() +
                                    ": " + e.getMessage());
                        }
                    }
                    eliminarVoluntarioDelSistema(id, nombreVoluntario);
                }

            } else {
                int confirm = JOptionPane.showConfirmDialog(DashboardCoordUI.this,
                        "¿Está seguro que desea eliminar al voluntario:\n" +
                                nombreVoluntario + " (ID: " + id + ")?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    eliminarVoluntarioDelSistema(id, nombreVoluntario);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(DashboardCoordUI.this,
                    "Error al eliminar voluntario: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Elimina el voluntario del sistema de usuarios
     */
    private void eliminarVoluntarioDelSistema(String id, String nombreVoluntario) {
        try {
            // Obtener todos los voluntarios
            List<Voluntario> voluntarios = gestorGeneral.getGestorUsuarios().obtenerVoluntarios();

            // Buscar y eliminar el voluntario
            Voluntario voluntarioAEliminar = null;
            for (Voluntario vol : voluntarios) {
                if (vol.getId().equals(id)) {
                    voluntarioAEliminar = vol;
                    break;
                }
            }

            if (voluntarioAEliminar != null) {
                // Eliminar de la lista de voluntarios
                voluntarios.remove(voluntarioAEliminar);

                // Guardar los cambios en el gestor
                gestorGeneral.getGestorUsuarios().guardarVoluntarioList(voluntarios);

                JOptionPane.showMessageDialog(this,
                        "Voluntario '" + nombreVoluntario + "' eliminado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Actualizar la tabla
                actualizarTablaVoluntarios();

            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el voluntario a eliminar",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar voluntario del sistema: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Busca voluntarios por término
     */
    private void buscarVoluntarios(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            actualizarTablaVoluntarios();
            return;
        }

        try {
            Component[] components = voluntariosPanel.getComponents();
            JTable tabla = null;

            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    JViewport viewport = ((JScrollPane) comp).getViewport();
                    if (viewport.getView() instanceof JTable) {
                        tabla = (JTable) viewport.getView();
                        break;
                    }
                }
            }

            if (tabla == null) return;

            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            List<Voluntario> todosVoluntarios = gestorGeneral.getGestorUsuarios().obtenerVoluntarios();

            // Limpiar la tabla
            model.setRowCount(0);

            String terminoLower = termino.toLowerCase();
            for (Voluntario vol : todosVoluntarios) {
                // Convertir habilidades a String para búsqueda
                String habilidadesStr = "";
                if (vol.getHabilidades() != null && !vol.getHabilidades().isEmpty()) {
                    habilidadesStr = String.join(", ", vol.getHabilidades()).toLowerCase();
                }

                // Buscar en nombre, email, teléfono o habilidades
                if (vol.getNombre().toLowerCase().contains(terminoLower) ||
                        vol.getEmail().toLowerCase().contains(terminoLower) ||
                        (vol.getTelefono() != null && vol.getTelefono().toLowerCase().contains(terminoLower)) ||
                        habilidadesStr.contains(terminoLower)) {

                    // Obtener brigadas a las que pertenece
                    List<Brigada> brigadasDelVoluntario = gestorGeneral.getGestorBrigadas().obtenerBrigadasDeVoluntario(vol.getId());
                    String brigadasStr = formatBrigadasList(brigadasDelVoluntario);

                    model.addRow(new Object[]{
                            vol.getId(),
                            vol.getNombre(),
                            vol.getEmail(),
                            vol.getTelefono(),
                            habilidadesStr.isEmpty() ? "" : String.join(", ", vol.getHabilidades()),
                            brigadasStr,
                            "Editar|Asociar|Eliminar"
                    });
                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron voluntarios que coincidan con: '" + termino + "'",
                        "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error en la búsqueda: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void actualizarTablaVoluntarios() {
        SwingUtilities.invokeLater(() -> {
            // Buscar el JScrollPane en el panel de voluntarios
            JScrollPane scrollPane = findScrollPaneInPanel(voluntariosPanel);

            if (scrollPane != null) {
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    JTable tabla = (JTable) view;
                    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                    cargarVoluntariosEnTabla(model);
                    tabla.repaint();
                    tabla.revalidate();
                }
            }
        });
    }

    /**
     * Obtiene un voluntario por ID
     */
    private Voluntario obtenerVoluntarioPorId(String id) {
        try {
            List<Voluntario> voluntarios = gestorGeneral.getGestorUsuarios().obtenerVoluntarios();
            for (Voluntario vol : voluntarios) {
                if (vol.getId().equals(id)) {
                    return vol;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Genera un ID automático para voluntario
     */
    private String generarIdVoluntario() {
        try {
            List<Voluntario> voluntarios = gestorGeneral.getGestorUsuarios().obtenerVoluntarios();
            int nextId = voluntarios.size() + 1;
            return String.format("VOL-%03d", nextId);
        } catch (Exception e) {
            return "VOL-001";
        }
    }

    // ===== MÉTODOS SIMPLIFICADOS PARA LAS OTRAS SECCIONES =====

    /**
     * Crea el panel de inicio (dashboard principal)
     */
    private JPanel createInicioPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40)); // Reducido un poco el margen superior

        // Panel superior con título y botón de actualización
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Título de bienvenida
        JLabel welcomeTitle = new JLabel("¡Hola, Coordinador " + coordinador.getNombre() + "!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(welcomeTitle, BorderLayout.WEST);

        // Botón de actualización
        JButton actualizarBtn = new JButton("Actualizar Datos");
        actualizarBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        actualizarBtn.setBackground(PRIMARY_BLUE);
        actualizarBtn.setForeground(Color.WHITE);
        actualizarBtn.setFocusPainted(false);
        actualizarBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        actualizarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actualizarBtn.setToolTipText("Actualizar todos los datos del dashboard");

        // Acción para actualizar los datos
        actualizarBtn.addActionListener(e -> actualizarDashboard());

        // Panel para el botón (para mantenerlo a la derecha)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(actualizarBtn);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(headerPanel);
        panel.add(Box.createVerticalStrut(10)); // Espacio reducido

        // Subtítulo
        JLabel subtitle = new JLabel("Área de Responsabilidad: " + coordinador.getAreaResponsabilidad());
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        panel.add(Box.createVerticalStrut(20)); // Espacio reducido

        // Panel de Tarjetas de Estadísticas
        panel.add(createStatsPanel());

        panel.add(Box.createVerticalStrut(20)); // Espacio reducido

        // Panel de Acciones Rápidas
        panel.add(createAccionesRapidasPanel());

        panel.add(Box.createVerticalStrut(20)); // Espacio reducido

        // Panel de Actividades Pendientes
        panel.add(createActividadesPendientesPanel());

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Método para actualizar todos los datos del dashboard
     */
    private void actualizarDashboard() {
        // Mostrar indicador de carga
        JDialog loadingDialog = new JDialog(this, "Actualizando...", false);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.setSize(200, 100);
        loadingDialog.setLocationRelativeTo(this);

        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel loadingLabel = new JLabel("Actualizando datos del dashboard...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingPanel.add(loadingLabel, BorderLayout.CENTER);

        loadingDialog.add(loadingPanel);
        loadingDialog.setVisible(true);

        new Thread(() -> {
            try {
                // Actualizar todas las tablas
                actualizarTodasLasTablas();

                // Actualizar estadísticas
                SwingUtilities.invokeLater(() -> {
                    // Actualizar el panel de estadísticas
                    actualizarEstadisticas();
                });

                Thread.sleep(500); // Dar tiempo para que se completen

                SwingUtilities.invokeLater(() -> {
                    loadingDialog.dispose();
                    JOptionPane.showMessageDialog(this,
                            "Dashboard actualizado exitosamente",
                            "Actualización Completada",
                            JOptionPane.INFORMATION_MESSAGE);
                });

            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    loadingDialog.dispose();
                    JOptionPane.showMessageDialog(this,
                            "Error al actualizar: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    /**
     * Actualiza todas las tablas del sistema (para uso general)
     */
    public void actualizarTodasLasTablas() {
        SwingUtilities.invokeLater(() -> {
            actualizarTablaActividades();
            actualizarTablaBrigadas();
            actualizarTablaVoluntarios();
            actualizarTablaRecursos();
            cargarHistorialCompleto(); // Para reportes
        });
    }

    /**
     * Actualiza las estadísticas del dashboard (tarjetas de estadísticas)
     */
    private void actualizarEstadisticas() {
        // Obtener los datos actualizados
        int brigadasActivas = obtenerCantidadBrigadasActivas();
        int totalVoluntarios = obtenerTotalVoluntarios();
        int actividadesProgramadas = obtenerCantidadActividadesProgramadas();
        int recursosDisponibles = obtenerPorcentajeRecursosDisponibles();

        // Encontrar y actualizar las tarjetas en el panel de inicio
        SwingUtilities.invokeLater(() -> {
            Component[] components = inicioPanel.getComponents();

            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    JPanel panel = (JPanel) comp;

                    // Buscar el panel de estadísticas (que tiene FlowLayout)
                    if (panel.getLayout() instanceof FlowLayout) {
                        Component[] cards = panel.getComponents();

                        // Actualizar cada tarjeta
                        for (int i = 0; i < cards.length && i < 4; i++) {
                            if (cards[i] instanceof JPanel) {
                                JPanel card = (JPanel) cards[i];

                                // Actualizar según el índice
                                switch (i) {
                                    case 0: // Brigadas Activas
                                        actualizarTarjetaEstadistica(card, "Brigadas Activas",
                                                String.valueOf(brigadasActivas), "🏢", PRIMARY_BLUE);
                                        break;
                                    case 1: // Total Voluntarios
                                        actualizarTarjetaEstadistica(card, "Total Voluntarios",
                                                String.valueOf(totalVoluntarios), "👥", CARD_GREEN);
                                        break;
                                    case 2: // Actividades Programadas
                                        actualizarTarjetaEstadistica(card, "Actividades Programadas",
                                                String.valueOf(actividadesProgramadas), "📅", CARD_PURPLE);
                                        break;
                                    case 3: // Recursos Disponibles
                                        actualizarTarjetaEstadistica(card, "Recursos Disponibles",
                                                recursosDisponibles + "%", "📦", CARD_ORANGE);
                                        break;
                                }
                            }
                        }
                        break; // Salir del bucle una vez encontrado
                    }
                }
            }

            // También actualizar el panel de actividades pendientes
            actualizarActividadesPendientesPanel();
        });
    }

    /**
     * Método auxiliar para actualizar una tarjeta de estadística individual
     */
    private void actualizarTarjetaEstadistica(JPanel card, String titulo, String valor, String icono, Color color) {
        // Limpiar la tarjeta
        card.removeAll();
        card.setLayout(new BorderLayout());

        // Contenido de la tarjeta
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);
        content.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(valor);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.BLACK);
        content.add(valueLabel, BorderLayout.WEST);

        JLabel iconLabel = new JLabel(icono);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 28));
        iconLabel.setForeground(color);
        content.add(iconLabel, BorderLayout.EAST);

        card.add(content, BorderLayout.CENTER);

        // Agregar listener para clic
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(240, 240, 240));
                Timer timer = new Timer(200, e -> {
                    card.setBackground(Color.WHITE);
                });
                timer.setRepeats(false);
                timer.start();

                JOptionPane.showMessageDialog(card,
                        "Estadística: " + titulo + "\n" +
                                "Valor actual: " + valor + "\n" +
                                "Última actualización: " +
                                new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()),
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        card.revalidate();
        card.repaint();
    }

    /**
     * Actualiza el panel de actividades pendientes
     */
    private void actualizarActividadesPendientesPanel() {
        // Buscar el panel de actividades pendientes
        Component[] components = inicioPanel.getComponents();

        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;

                // Buscar por el título "Actividades Pendientes y En Proceso"
                Component[] childComponents = panel.getComponents();
                for (int i = 0; i < childComponents.length; i++) {
                    if (childComponents[i] instanceof JLabel) {
                        JLabel label = (JLabel) childComponents[i];
                        if (label.getText().contains("Actividades Pendientes y En Proceso")) {
                            // Este es el panel correcto
                            // Recrear el contenido del panel
                            panel.removeAll();

                            // Reconstruir el panel con datos actualizados
                            JPanel nuevoPanelActividades = createActividadesPendientesPanel();

                            // Reemplazar el panel existente
                            inicioPanel.remove(panel);
                            inicioPanel.add(nuevoPanelActividades, getComponentIndex(inicioPanel, panel));

                            inicioPanel.revalidate();
                            inicioPanel.repaint();
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Obtiene el índice de un componente dentro de un contenedor
     */
    private int getComponentIndex(Container container, Component component) {
        Component[] components = container.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] == component) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Crea el panel con las tarjetas de estadísticas
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Obtener datos reales
        int brigadasActivas = obtenerCantidadBrigadasActivas();
        int totalVoluntarios = obtenerTotalVoluntarios();
        int actividadesProgramadas = obtenerCantidadActividadesProgramadas();
        int recursosDisponibles = obtenerPorcentajeRecursosDisponibles();

        // 1. Brigadas Activas
        statsPanel.add(createStatsCard("Brigadas Activas",
                String.valueOf(brigadasActivas), "🏢", PRIMARY_BLUE));

        // 2. Total Voluntarios
        statsPanel.add(createStatsCard("Total Voluntarios",
                String.valueOf(totalVoluntarios), "👥", CARD_GREEN));

        // 3. Actividades Programadas
        statsPanel.add(createStatsCard("Actividades Programadas",
                String.valueOf(actividadesProgramadas), "📅", CARD_PURPLE));

        // 4. Recursos Disponibles
        statsPanel.add(createStatsCard("Recursos Disponibles",
                recursosDisponibles + "%", "📦", CARD_ORANGE));

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

        // Agregar listener para actualizar al hacer clic
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Efecto visual de clic
                card.setBackground(new Color(240, 240, 240));
                Timer timer = new Timer(200, e -> {
                    card.setBackground(Color.WHITE);
                });
                timer.setRepeats(false);
                timer.start();

                // Mostrar mensaje informativo
                JOptionPane.showMessageDialog(card,
                        "Estadística: " + title + "\n" +
                                "Valor actual: " + value + "\n" +
                                "Última actualización: " +
                                new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()),
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

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

        return card;
    }

    // Métodos auxiliares para obtener datos
    private int obtenerCantidadBrigadasActivas() {
        try {
            return gestorGeneral.getGestorBrigadas().obtenerBrigadasActivas().size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int obtenerTotalVoluntarios() {
        try {
            return gestorGeneral.getGestorUsuarios().obtenerVoluntarios().size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int obtenerCantidadActividadesProgramadas() {
        try {
            List<Actividad> todas = gestorActividades.obtenerTodasActividades();
            int contador = 0;

            for (Actividad actividad : todas) {
                String estado = determinarEstado(actividad);
                if ("Pendiente".equals(estado) || "En proceso".equals(estado)) {
                    contador++;
                }
            }
            return contador;
        } catch (Exception e) {
            return 0;
        }
    }

    private int obtenerPorcentajeRecursosDisponibles() {
        try {
            double porcentaje = gestorGeneral.getGestorRecursos().obtenerPorcentajeRecursosDisponibles();
            return (int) Math.round(porcentaje);
        } catch (Exception e) {
            return 85;
        }
    }

    /**
     * Crea el panel de acciones rápidas
     */
    private JPanel createAccionesRapidasPanel() {
        JPanel accionesPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        accionesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        accionesPanel.setBackground(Color.WHITE);
        accionesPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Acción 1: Registrar Nueva Brigada (RF-01)
        accionesPanel.add(createAccionRapida(
                "Nueva Brigada",
                "🏢",
                "Crear nueva brigada comunitaria",
                PRIMARY_BLUE,
                e -> cambiarPanel("brigadas")
        ));

        // Acción 3: Planificar Actividad (RF-04)
        accionesPanel.add(createAccionRapida(
                "Planificar Actividad",
                "📝",
                "Crear nueva actividad",
                CARD_GREEN,
                e -> planificarNuevaActividad()
        ));

        // Acción 4: Consultar Recursos (RF-10)
        accionesPanel.add(createAccionRapida(
                "Consultar Recursos",
                "📦",
                "Revisar inventario",
                CARD_ORANGE,
                e -> consultarRecursos()
        ));

        return accionesPanel;
    }

    /**
     * Crea un botón de acción rápida
     */
    private JPanel createAccionRapida(String title, String icon, String tooltip, Color color, java.awt.event.ActionListener action) {
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        actionPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionPanel.setToolTipText(tooltip);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 32));
        iconLabel.setForeground(color);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        actionPanel.add(iconLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        actionPanel.add(titleLabel, BorderLayout.SOUTH);

        actionPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.actionPerformed(new java.awt.event.ActionEvent(this, 0, ""));
            }
        });

        return actionPanel;
    }

    /**
     * Crea el panel de actividades pendientes y en proceso con datos reales del sistema
     */
    private JPanel createActividadesPendientesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel("Actividades Pendientes y En Proceso");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title);

        JLabel subtitle = new JLabel("Actividades por completar o en ejecución");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);

        panel.add(Box.createVerticalStrut(15));

        // Obtener actividades pendientes y en proceso reales del sistema
        List<Actividad> actividades = obtenerActividadesPendientesYEnProceso();

        if (actividades.isEmpty()) {
            JLabel noActividadesLabel = new JLabel("No hay actividades pendientes o en proceso en este momento.");
            noActividadesLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            noActividadesLabel.setForeground(Color.GRAY);
            noActividadesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(noActividadesLabel);
        } else {
            // Agrupar por estado
            List<Actividad> pendientes = new ArrayList<>();
            List<Actividad> enProceso = new ArrayList<>();

            for (Actividad actividad : actividades) {
                String estado = determinarEstado(actividad);
                if ("Pendiente".equals(estado)) {
                    pendientes.add(actividad);
                } else if ("En proceso".equals(estado)) {
                    enProceso.add(actividad);
                }
            }

            // Mostrar primero "En proceso", luego "Pendientes"
            if (!enProceso.isEmpty()) {
                panel.add(createSubtitleLabel("En Proceso (" + enProceso.size() + ")"));
                for (Actividad actividad : enProceso) {
                    panel.add(createItemActividadConEstado(actividad, "En proceso"));
                    panel.add(Box.createVerticalStrut(8));
                }
            }

            if (!pendientes.isEmpty()) {
                if (!enProceso.isEmpty()) {
                    panel.add(Box.createVerticalStrut(10));
                }
                panel.add(createSubtitleLabel("Pendientes (" + pendientes.size() + ")"));
                for (Actividad actividad : pendientes) {
                    panel.add(createItemActividadConEstado(actividad, "Pendiente"));
                    panel.add(Box.createVerticalStrut(8));
                }
            }
        }

        // Botón para ir a la sección de actividades
        JButton verTodasBtn = new JButton("Ver todas las actividades");
        verTodasBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        verTodasBtn.setBackground(new Color(240, 240, 240));
        verTodasBtn.setFocusPainted(false);
        verTodasBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        verTodasBtn.addActionListener(e -> cambiarPanel("actividades"));
        verTodasBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        panel.add(Box.createVerticalStrut(15));
        panel.add(verTodasBtn);

        return panel;
    }

    /**
     * Determina el estado de una actividad usando el algoritmo
     */
    private String determinarEstado(Actividad actividad) {
        Date ahora = new Date();

        if (actividad.getFecha().after(ahora)) {
            return "Pendiente";
        } else if (actividad.getFecha().before(ahora) &&
                (actividad.getResultados() == null || actividad.getResultados().isEmpty())) {
            return "En proceso";
        } else if (actividad.getResultados() != null && !actividad.getResultados().isEmpty()) {
            return "Completada";
        } else {
            return "Planificada";
        }
    }

    /**
     * Obtiene las actividades pendientes y en proceso del sistema
     */
    private List<Actividad> obtenerActividadesPendientesYEnProceso() {
        List<Actividad> actividadesFiltradas = new ArrayList<>();

        try {
            List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();

            for (Actividad actividad : todasActividades) {
                String estado = determinarEstado(actividad);

                // Solo incluir Pendientes y En proceso
                if ("Pendiente".equals(estado) || "En proceso".equals(estado)) {
                    actividadesFiltradas.add(actividad);
                }
            }

            // Ordenar: primero por estado (En proceso primero), luego por fecha
            actividadesFiltradas.sort((a1, a2) -> {
                String estado1 = determinarEstado(a1);
                String estado2 = determinarEstado(a2);

                // Prioridad: En proceso > Pendiente
                if ("En proceso".equals(estado1) && "Pendiente".equals(estado2)) {
                    return -1;
                }
                if ("Pendiente".equals(estado1) && "En proceso".equals(estado2)) {
                    return 1;
                }

                // Mismo estado: ordenar por fecha (más cercana primero)
                return a1.getFecha().compareTo(a2.getFecha());
            });

            // Limitar a un máximo razonable
            if (actividadesFiltradas.size() > 8) {
                actividadesFiltradas = actividadesFiltradas.subList(0, 8);
            }

        } catch (Exception e) {
            System.err.println("Error obteniendo actividades: " + e.getMessage());
        }

        return actividadesFiltradas;
    }

    /**
     * Crea un subtítulo para agrupar actividades por estado
     */
    private JLabel createSubtitleLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(66, 66, 66));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(5, 0, 5, 0));
        return label;
    }

    /**
     * Crea un item de actividad con diseño específico según estado - VERSIÓN CON BOTÓN ASIGNAR
     */
    private JPanel createItemActividadConEstado(Actividad actividad, String estado) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Color de fondo según estado
        if ("En proceso".equals(estado)) {
            item.setBackground(new Color(255, 248, 225)); // Amarillo muy claro
            item.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 193, 7), 1),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        } else { // Pendiente
            item.setBackground(new Color(240, 248, 255)); // Azul muy claro
        }

        // Panel izquierdo con información
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Nombre y estado
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nombrePanel.setOpaque(false);

        JLabel nombreLabel = new JLabel(actividad.getNombre());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nombrePanel.add(nombreLabel);

        JLabel estadoLabel = new JLabel(" [" + estado + "]");
        estadoLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        if ("En proceso".equals(estado)) {
            estadoLabel.setForeground(new Color(218, 165, 32)); // Dorado
            estadoLabel.setFont(new Font("Arial", Font.BOLD, 10));
        } else {
            estadoLabel.setForeground(new Color(100, 100, 100));
        }
        nombrePanel.add(estadoLabel);

        infoPanel.add(nombrePanel);

        // Detalles
        String fechaStr = new java.text.SimpleDateFormat("dd/MM/yyyy").format(actividad.getFecha());
        String lugarStr = actividad.getLugar();
        String brigadaStr = actividad.getBrigadaAsociada() != null ?
                actividad.getBrigadaAsociada().getNombre() : "Sin brigada";

        JLabel detallesLabel = new JLabel(String.format("%s | %s | %s", fechaStr, lugarStr, brigadaStr));
        detallesLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        detallesLabel.setForeground(Color.DARK_GRAY);
        infoPanel.add(detallesLabel);

        // Días restantes o transcurridos
        Date ahora = new Date();
        long diasDiferencia = (actividad.getFecha().getTime() - ahora.getTime()) / (1000 * 60 * 60 * 24);

        if ("Pendiente".equals(estado)) {
            String diasInfo = diasDiferencia == 1 ? "1 día" : Math.abs(diasDiferencia) + " días";
            JLabel diasLabel = new JLabel("Faltan " + diasInfo);
            diasLabel.setFont(new Font("Arial", Font.PLAIN, 9));
            diasLabel.setForeground(new Color(0, 100, 0));
            infoPanel.add(diasLabel);
        } else if ("En proceso".equals(estado)) {
            String diasInfo = Math.abs(diasDiferencia) == 1 ? "1 día" : Math.abs(diasDiferencia) + " días";
            JLabel diasLabel = new JLabel("En curso desde hace " + diasInfo);
            diasLabel.setFont(new Font("Arial", Font.PLAIN, 9));
            diasLabel.setForeground(new Color(139, 0, 0));
            infoPanel.add(diasLabel);
        }

        item.add(infoPanel, BorderLayout.CENTER);

        // Botón de acción - AHORA SIEMPRE ES "ASIGNAR"
        JButton accionBtn = new JButton("Asignar");
        accionBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        accionBtn.setFocusPainted(false);
        accionBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        // Color según estado
        if ("En proceso".equals(estado)) {
            accionBtn.setBackground(new Color(156, 39, 176)); // Morado
            accionBtn.setToolTipText("Asignar voluntarios y brigadas a esta actividad");
        } else { // Pendiente
            accionBtn.setBackground(PRIMARY_BLUE); // Azul
            accionBtn.setToolTipText("Asignar voluntarios y brigadas a esta actividad");
        }

        accionBtn.setForeground(Color.WHITE);
        accionBtn.addActionListener(e -> {
            // Usar el nuevo diálogo mejorado para ambas situaciones
            mostrarDialogoAsignarVoluntarios(actividad);
        });

        item.add(accionBtn, BorderLayout.EAST);

        return item;
    }

    // En la clase DashboardCoordUI, en el método createBrigadasPanel():
    private JPanel createBrigadasPanel() {
        JPanel panel = new JPanel(new BorderLayout()); // Cambiar a BorderLayout
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_GRAY);

        JLabel title = new JLabel("Gestión de Brigadas - CRUD");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("RF-01: Crear brigadas | RF-08: Consultar información");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Barra de herramientas
        panel.add(createToolbarBrigadas(), BorderLayout.NORTH);

        // Tabla de brigadas
        String[] columnNames = {"ID", "Nombre", "Tipo", "Zona", "Estado", "Voluntarios", "Acciones"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de acciones es editable
            }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(40);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));

        // Configurar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120); // Tipo
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Zona
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100); // Estado
        tabla.getColumnModel().getColumn(5).setPreferredWidth(100); // Voluntarios
        tabla.getColumnModel().getColumn(6).setPreferredWidth(180); // Acciones

        // **** IMPORTANTE: Configurar renderizador y editor para la columna de acciones ****
        tabla.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), tabla));

        // Cargar datos
        cargarBrigadasEnTabla(model);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(1100, 500));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea la barra de herramientas para CRUD de brigadas
     */
    private JPanel createToolbarBrigadas() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botón Crear
        JButton crearBtn = createToolbarButton("Crear Brigada", "Crear nueva brigada", PRIMARY_BLUE, e -> mostrarFormularioNuevaBrigada());
        toolbar.add(crearBtn);

        // Botón Actualizar
        JButton actualizarBtn = createToolbarButton("Actualizar", "Actualizar lista de brigadas", CARD_GREEN,
                e -> actualizarTablaBrigadas());
        toolbar.add(actualizarBtn);

        return toolbar;
    }

    /**
     * Carga las brigadas en la tabla
     */
    private void cargarBrigadasEnTabla(DefaultTableModel model) {
        model.setRowCount(0); // Limpiar tabla

        try {
            List<Brigada> brigadas = gestorGeneral.getGestorBrigadas().obtenerTodasBrigadas();

            for (Brigada brigada : brigadas) {
                model.addRow(new Object[]{
                        brigada.getId(),
                        brigada.getNombre(),
                        brigada.getTipo(),
                        brigada.getZona(),
                        brigada.getEstado(),
                        brigada.getCantidadVoluntarios(),
                        "Editar|Eliminar|Ver"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando brigadas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaBrigadas() {
        SwingUtilities.invokeLater(() -> {
            // Buscar el JScrollPane en el panel de brigadas
            JScrollPane scrollPane = findScrollPaneInPanel(brigadasPanel);

            if (scrollPane != null) {
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    JTable tabla = (JTable) view;
                    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                    cargarBrigadasEnTabla(model);
                    tabla.repaint();
                    tabla.revalidate();
                }
            }
        });
    }

    private JScrollPane findScrollPaneInPanel(JPanel panel) {
        // Primero buscar directamente en los componentes
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JScrollPane) {
                return (JScrollPane) comp;
            }
        }

        // Si no se encuentra, buscar recursivamente en subpaneles
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JScrollPane found = findScrollPaneInPanel((JPanel) comp);
                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    // ===== MÉTODOS SIMPLIFICADOS PARA LAS DEMÁS SECCIONES =====
// En DashboardCoordUI.java, reemplazar el método createActividadesPanel():

    private JPanel createActividadesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_GRAY);

        JLabel title = new JLabel("Gestión de Actividades");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("RF-04: Planificar | RF-05: Asignar | RF-07: Registrar resultados");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Barra de herramientas
        panel.add(createToolbarActividades(), BorderLayout.NORTH);

        // Tabla de actividades
        String[] columnNames = {"ID", "Nombre", "Fecha", "Lugar", "Objetivo", "Brigada", "Estado", "Acciones"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo la columna de acciones es editable
            }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(65);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));

        // Configurar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100); // Fecha
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120); // Lugar
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150); // Objetivo
        tabla.getColumnModel().getColumn(5).setPreferredWidth(120); // Brigada
        tabla.getColumnModel().getColumn(6).setPreferredWidth(100); // Estado
        tabla.getColumnModel().getColumn(7).setPreferredWidth(200); // Acciones

        // Configurar renderizador y editor para botones de acciones
        tabla.getColumnModel().getColumn(7).setCellRenderer(new ActividadesButtonRenderer());
        tabla.getColumnModel().getColumn(7).setCellEditor(new ActividadesButtonEditor(new JCheckBox(), tabla));

        // Cargar datos
        cargarActividadesEnTabla(model);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(1100, 500));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea la barra de herramientas para actividades
     */
    private JPanel createToolbarActividades() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Botón Nueva Actividad (RF-04)
        JButton nuevoBtn = createToolbarButton("Nueva Actividad", "Planificar nueva actividad (RF-04)",
                CARD_GREEN, e -> mostrarFormularioNuevaActividad());
        toolbar.add(nuevoBtn);

        // Campo de búsqueda
        JTextField searchField = new JTextField(25);
        searchField.setToolTipText("Buscar actividad por nombre, lugar o brigada");
        toolbar.add(searchField);

        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.addActionListener(e -> buscarActividades(searchField.getText()));
        toolbar.add(buscarBtn);

        // Botón Actualizar
        JButton actualizarBtn = createToolbarButton("Actualizar", "Actualizar lista de actividades",
                PRIMARY_BLUE, e -> actualizarTablaActividades());
        toolbar.add(actualizarBtn);

        return toolbar;
    }

    /**
     * Renderizador para botones en la tabla de actividades - VERSIÓN CON BOTÓN ASIGNAR
     */
    class ActividadesButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton asignarBtn; // Nuevo botón
        private JButton resultadosBtn;
        private JButton editarBtn;
        private JButton eliminarBtn;

        public ActividadesButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 3, 5));
            setOpaque(true);
            setPreferredSize(new Dimension(220, 40)); // Aumentado para 4 botones

            // Botón Asignar (RF-05) - NUEVO
            asignarBtn = new JButton("Asignar");
            asignarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            asignarBtn.setBackground(new Color(25, 118, 210)); // Azul primario
            asignarBtn.setForeground(Color.WHITE);
            asignarBtn.setFocusPainted(false);
            asignarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            asignarBtn.setPreferredSize(new Dimension(65, 25));
            asignarBtn.setOpaque(true);
            asignarBtn.setContentAreaFilled(true);
            asignarBtn.setBorderPainted(true);
            asignarBtn.setToolTipText("Asignar voluntarios y brigadas (RF-05)");

            // Botón Resultados (RF-07)
            resultadosBtn = new JButton("Resultados");
            resultadosBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            resultadosBtn.setBackground(new Color(156, 39, 176)); // Morado
            resultadosBtn.setForeground(Color.WHITE);
            resultadosBtn.setFocusPainted(false);
            resultadosBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            resultadosBtn.setPreferredSize(new Dimension(70, 25));
            resultadosBtn.setOpaque(true);
            resultadosBtn.setContentAreaFilled(true);
            resultadosBtn.setBorderPainted(true);
            resultadosBtn.setToolTipText("Registrar resultados (RF-07)");

            // Botón Editar
            editarBtn = new JButton("Editar");
            editarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editarBtn.setBackground(new Color(255, 193, 7)); // Amarillo
            editarBtn.setForeground(Color.BLACK);
            editarBtn.setFocusPainted(false);
            editarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            editarBtn.setPreferredSize(new Dimension(55, 25));
            editarBtn.setOpaque(true);
            editarBtn.setContentAreaFilled(true);
            editarBtn.setBorderPainted(true);
            editarBtn.setToolTipText("Editar actividad");

            // Botón Eliminar
            eliminarBtn = new JButton("Eliminar");
            eliminarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            eliminarBtn.setBackground(new Color(244, 67, 54)); // Rojo
            eliminarBtn.setForeground(Color.WHITE);
            eliminarBtn.setFocusPainted(false);
            eliminarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            eliminarBtn.setPreferredSize(new Dimension(60, 25));
            eliminarBtn.setOpaque(true);
            eliminarBtn.setContentAreaFilled(true);
            eliminarBtn.setBorderPainted(true);
            eliminarBtn.setToolTipText("Eliminar actividad");

            // Agregar 4 botones (incluyendo Asignar)
            add(asignarBtn);
            add(resultadosBtn);
            add(editarBtn);
            add(eliminarBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }

            // Determinar estado de la actividad
            String estado = (String) table.getValueAt(row, 6);

            // Configurar estado de botones según el estado de la actividad
            asignarBtn.setEnabled(!"Completada".equals(estado));
            resultadosBtn.setEnabled("En proceso".equals(estado) || "Completada".equals(estado));
            editarBtn.setEnabled(!"Completada".equals(estado));
            eliminarBtn.setEnabled(!estado.contains("Completada"));

            // Cambiar colores según estado
            asignarBtn.setBackground(asignarBtn.isEnabled() ?
                    new Color(25, 118, 210) : Color.LIGHT_GRAY);
            resultadosBtn.setBackground(resultadosBtn.isEnabled() ?
                    new Color(156, 39, 176) : Color.LIGHT_GRAY);
            editarBtn.setBackground(editarBtn.isEnabled() ?
                    new Color(255, 193, 7) : Color.LIGHT_GRAY);
            eliminarBtn.setBackground(eliminarBtn.isEnabled() ?
                    new Color(244, 67, 54) : Color.LIGHT_GRAY);

            return this;
        }
    }

    /**
     * Editor para botones en la tabla de actividades - VERSIÓN CON BOTÓN ASIGNAR
     */
    class ActividadesButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton asignarBtn; // Nuevo botón
        private JButton resultadosBtn;
        private JButton editarBtn;
        private JButton eliminarBtn;
        private JTable tabla;
        private int currentRow;
        private Actividad actividadActual;

        public ActividadesButtonEditor(JCheckBox checkBox, JTable tabla) {
            this.tabla = tabla;

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 5));
            panel.setOpaque(true);
            panel.setPreferredSize(new Dimension(220, 40)); // Aumentado

            // Botón Asignar (RF-05) - NUEVO
            asignarBtn = new JButton("Asignar");
            asignarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            asignarBtn.setBackground(new Color(25, 118, 210));
            asignarBtn.setForeground(Color.WHITE);
            asignarBtn.setFocusPainted(false);
            asignarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            asignarBtn.setPreferredSize(new Dimension(65, 25));
            asignarBtn.addActionListener(e -> {
                asignarVoluntariosYBrigadasDesdeTabla();
                fireEditingStopped();
            });

            // Botón Resultados (RF-07)
            resultadosBtn = new JButton("Resultados");
            resultadosBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            resultadosBtn.setBackground(new Color(156, 39, 176));
            resultadosBtn.setForeground(Color.WHITE);
            resultadosBtn.setFocusPainted(false);
            resultadosBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            resultadosBtn.setPreferredSize(new Dimension(70, 25));
            resultadosBtn.addActionListener(e -> {
                registrarResultadosActividad();
                fireEditingStopped();
            });

            // Botón Editar
            editarBtn = new JButton("Editar");
            editarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editarBtn.setBackground(new Color(255, 193, 7));
            editarBtn.setForeground(Color.BLACK);
            editarBtn.setFocusPainted(false);
            editarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            editarBtn.setPreferredSize(new Dimension(55, 25));
            editarBtn.addActionListener(e -> {
                editarActividadDesdeTabla();
                fireEditingStopped();
            });

            // Botón Eliminar
            eliminarBtn = new JButton("Eliminar");
            eliminarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            eliminarBtn.setBackground(new Color(244, 67, 54));
            eliminarBtn.setForeground(Color.WHITE);
            eliminarBtn.setFocusPainted(false);
            eliminarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            eliminarBtn.setPreferredSize(new Dimension(60, 25));
            eliminarBtn.addActionListener(e -> {
                eliminarActividadDesdeTabla();
                fireEditingStopped();
            });

            // Agregar 4 botones (incluyendo Asignar)
            panel.add(asignarBtn);
            panel.add(resultadosBtn);
            panel.add(editarBtn);
            panel.add(eliminarBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;

            // Obtener la actividad actual de la fila
            String idActividad = (String) tabla.getValueAt(currentRow, 0);
            actividadActual = obtenerActividadPorId(idActividad);

            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }

            // Habilitar/deshabilitar botones según el estado
            if (actividadActual != null) {
                String estado = (String) tabla.getValueAt(currentRow, 6);

                asignarBtn.setEnabled(!"Completada".equals(estado));
                resultadosBtn.setEnabled("En proceso".equals(estado) || "Completada".equals(estado));
                editarBtn.setEnabled(!"Completada".equals(estado));
                eliminarBtn.setEnabled(!estado.contains("Completada"));
            }

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Asignar|Resultados|Editar|Eliminar";
        }

        private void asignarVoluntariosYBrigadasDesdeTabla() {
            if (actividadActual == null) {
                JOptionPane.showMessageDialog(tabla,
                        "No se pudo obtener la información de la actividad",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Usar el nuevo método mejorado
            mostrarDialogoAsignarVoluntarios(actividadActual);
        }

        // Mantener los otros métodos existentes...
        private void registrarResultadosActividad() {
            if (actividadActual == null) {
                JOptionPane.showMessageDialog(tabla,
                        "No se pudo obtener la información de la actividad",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            mostrarDialogoRegistrarResultados(actividadActual);
        }

        private void editarActividadDesdeTabla() {
            if (actividadActual == null) {
                JOptionPane.showMessageDialog(tabla,
                        "No se pudo obtener la información de la actividad",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            mostrarFormularioEditarActividad(actividadActual);
        }

        private void eliminarActividadDesdeTabla() {
            String id = (String) tabla.getValueAt(currentRow, 0);
            String nombre = (String) tabla.getValueAt(currentRow, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    tabla,
                    "¿Está seguro que desea eliminar la actividad:\n" +
                            nombre + " (ID: " + id + ")?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                fireEditingStopped();
                eliminarActividad(id, nombre);
            } else {
                fireEditingStopped();
            }
        }

        private Actividad obtenerActividadPorId(String id) {
            try {
                return gestorActividades.buscarActividadPorId(id);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    /**
     * RF-05: Asignar voluntarios y brigadas a actividad - VERSIÓN CON ESTRUCTURA REAL
     */
    private void mostrarDialogoAsignarVoluntarios(Actividad actividad) {
        JDialog dialog = new JDialog(this, "Gestionar Asignaciones: " + actividad.getNombre(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(700, 600);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Información de la actividad
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información de la Actividad"));

        infoPanel.add(new JLabel("Actividad:"));
        infoPanel.add(new JLabel(actividad.getNombre()));
        infoPanel.add(new JLabel("Fecha:"));
        infoPanel.add(new JLabel(new java.text.SimpleDateFormat("dd/MM/yyyy").format(actividad.getFecha())));
        infoPanel.add(new JLabel("Lugar:"));
        infoPanel.add(new JLabel(actividad.getLugar()));
        infoPanel.add(new JLabel("Brigada asignada:"));
        infoPanel.add(new JLabel(actividad.getBrigadaAsociada() != null ?
                actividad.getBrigadaAsociada().getNombre() : "Ninguna"));

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Panel con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña 1: Voluntarios
        DefaultListModel<String> voluntariosModel = new DefaultListModel<>();
        JList<String> voluntariosList = new JList<>(voluntariosModel);

        // Pestaña 2: Brigadas
        DefaultListModel<String> brigadasModel = new DefaultListModel<>();
        JList<String> brigadasList = new JList<>(brigadasModel);

        // Cargar datos iniciales
        cargarVoluntariosEnLista(voluntariosModel, actividad);
        cargarBrigadasEnLista(brigadasModel, actividad);

        // Crear las pestañas
        JPanel voluntariosTab = crearPanelLista(voluntariosList, "Voluntarios disponibles");
        JPanel brigadasTab = crearPanelLista(brigadasList, "Brigadas disponibles");

        tabbedPane.addTab("👥 Voluntarios", voluntariosTab);
        tabbedPane.addTab("🏢 Brigadas", brigadasTab);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Panel de botones de acción
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton agregarBtn = new JButton("➕ Agregar seleccionados");
        agregarBtn.setBackground(new Color(76, 175, 80));
        agregarBtn.setForeground(Color.WHITE);
        agregarBtn.addActionListener(e -> {
            procesarAgregar(dialog, actividad, tabbedPane, voluntariosList, brigadasList,
                    voluntariosModel, brigadasModel);
        });

        JButton quitarBtn = new JButton("➖ Quitar seleccionados");
        quitarBtn.setBackground(new Color(244, 67, 54));
        quitarBtn.setForeground(Color.WHITE);
        quitarBtn.addActionListener(e -> {
            procesarQuitar(dialog, actividad, tabbedPane, voluntariosList, brigadasList,
                    voluntariosModel, brigadasModel);
        });

        actionPanel.add(agregarBtn);
        actionPanel.add(Box.createHorizontalStrut(20));
        actionPanel.add(quitarBtn);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cerrarBtn = new JButton("Cerrar");
        cerrarBtn.addActionListener(e -> dialog.dispose());

        bottomPanel.add(cerrarBtn);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Procesa la adición de elementos seleccionados
     */
    private void procesarAgregar(JDialog dialog, Actividad actividad, JTabbedPane tabbedPane,
                                 JList<String> voluntariosList, JList<String> brigadasList,
                                 DefaultListModel<String> voluntariosModel,
                                 DefaultListModel<String> brigadasModel) {

        int tabIndex = tabbedPane.getSelectedIndex();

        if (tabIndex == 0) { // Voluntarios
            int[] indices = voluntariosList.getSelectedIndices();
            if (indices.length == 0) {
                JOptionPane.showMessageDialog(dialog, "Seleccione voluntarios", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String> nuevosVoluntariosIds = new ArrayList<>();
            for (int index : indices) {
                String item = voluntariosModel.getElementAt(index);
                if (!item.contains("(Ya asignado)")) {
                    // Extraer email del voluntario
                    String email = item.split(" - ")[1];
                    if (item.contains("(Ya asignado)")) {
                        email = email.replace(" (Ya asignado)", "");
                    }

                    Voluntario vol = gestorGeneral.getGestorUsuarios().buscarVoluntarioPorEmail(email.trim());
                    if (vol != null) {
                        nuevosVoluntariosIds.add(vol.getId());
                    }
                }
            }

            if (!nuevosVoluntariosIds.isEmpty()) {
                // Obtener voluntarios actuales
                List<String> voluntariosActuales = obtenerVoluntariosAsignadosIds(actividad.getId());

                // Agregar nuevos sin duplicar
                List<String> todosVoluntarios = new ArrayList<>(voluntariosActuales);
                for (String nuevoId : nuevosVoluntariosIds) {
                    if (!todosVoluntarios.contains(nuevoId)) {
                        todosVoluntarios.add(nuevoId);
                    }
                }

                // Actualizar en la actividad
                if (asignarVoluntariosActividad(actividad.getId(), todosVoluntarios)) {
                    cargarVoluntariosEnLista(voluntariosModel, actividad);
                    JOptionPane.showMessageDialog(dialog,
                            "Se agregaron " + nuevosVoluntariosIds.size() + " voluntarios",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } else if (tabIndex == 1) { // Brigadas
            int[] indices = brigadasList.getSelectedIndices();
            if (indices.length == 0) {
                JOptionPane.showMessageDialog(dialog, "Seleccione una brigada", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (indices.length > 1) {
                JOptionPane.showMessageDialog(dialog, "Solo puede seleccionar una brigada", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String item = brigadasModel.getElementAt(indices[0]);
            if (!item.contains("(Ya asignada)")) {
                // Extraer nombre de la brigada
                String brigadaNombre = item.split(" - ")[0];

                try {
                    // Buscar la brigada
                    Brigada brigada;
                    brigada = gestorGeneral.getGestorBrigadas().buscarBrigadaPorNombre(brigadaNombre);
                    if (brigada != null) {
                        // Confirmar
                        int confirm = JOptionPane.showConfirmDialog(dialog,
                                "¿Asignar brigada '" + brigada.getNombre() + "'?\n" +
                                        "Se asignarán automáticamente sus " + brigada.getCantidadVoluntarios() + " voluntarios.",
                                "Confirmar", JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            // Asignar brigada a la actividad
                            actividad.setBrigadaAsociada(brigada);
                            gestorActividades.actualizarActividad(actividad);

                            // Obtener voluntarios de la brigada
                            List<Voluntario> voluntariosBrigada = gestorGeneral.getGestorBrigadas()
                                    .obtenerVoluntariosDeBrigada(brigada.getId());

                            List<String> voluntariosBrigadaIds = new ArrayList<>();
                            for (Voluntario vol : voluntariosBrigada) {
                                voluntariosBrigadaIds.add(vol.getId());
                            }

                            // Asignar voluntarios de la brigada
                            asignarVoluntariosActividad(actividad.getId(), voluntariosBrigadaIds);

                            // Actualizar listas
                            cargarVoluntariosEnLista(voluntariosModel, actividad);
                            cargarBrigadasEnLista(brigadasModel, actividad);

                            JOptionPane.showMessageDialog(dialog,
                                    "Brigada asignada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Procesa la eliminación de elementos seleccionados
     */
    private void procesarQuitar(JDialog dialog, Actividad actividad, JTabbedPane tabbedPane,
                                JList<String> voluntariosList, JList<String> brigadasList,
                                DefaultListModel<String> voluntariosModel,
                                DefaultListModel<String> brigadasModel) {

        int tabIndex = tabbedPane.getSelectedIndex();

        if (tabIndex == 0) { // Voluntarios
            int[] indices = voluntariosList.getSelectedIndices();
            if (indices.length == 0) {
                JOptionPane.showMessageDialog(dialog, "Seleccione voluntarios", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String> voluntariosAQuitar = new ArrayList<>();
            for (int index : indices) {
                String item = voluntariosModel.getElementAt(index);
                if (item.contains("(Ya asignado)")) {
                    // Extraer email del voluntario
                    String email = item.split(" - ")[1].replace(" (Ya asignado)", "").trim();
                    Voluntario vol = gestorGeneral.getGestorUsuarios().buscarVoluntarioPorEmail(email);
                    if (vol != null) {
                        voluntariosAQuitar.add(vol.getId());
                    }
                }
            }

            if (!voluntariosAQuitar.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "¿Quitar " + voluntariosAQuitar.size() + " voluntario(s)?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Obtener voluntarios actuales
                    List<String> voluntariosActuales = obtenerVoluntariosAsignadosIds(actividad.getId());

                    // Remover los seleccionados
                    voluntariosActuales.removeAll(voluntariosAQuitar);

                    // Actualizar en la actividad
                    if (asignarVoluntariosActividad(actividad.getId(), voluntariosActuales)) {
                        cargarVoluntariosEnLista(voluntariosModel, actividad);
                        JOptionPane.showMessageDialog(dialog,
                                "Voluntarios quitados exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }

        } else if (tabIndex == 1) { // Brigadas
            int[] indices = brigadasList.getSelectedIndices();
            if (indices.length == 0) {
                JOptionPane.showMessageDialog(dialog, "Seleccione una brigada", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String item = brigadasModel.getElementAt(indices[0]);
            if (item.contains("(Ya asignada)")) {
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "¿Quitar la brigada asignada?\n" +
                                "Esto también quitará a todos sus voluntarios de la actividad.",
                        "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Quitar brigada
                        actividad.setBrigadaAsociada(null);
                        gestorActividades.actualizarActividad(actividad);

                        // Actualizar listas
                        cargarBrigadasEnLista(brigadasModel, actividad);

                        JOptionPane.showMessageDialog(dialog,
                                "Brigada quitada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(dialog, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * Crea un panel con una lista
     */
    private JPanel crearPanelLista(JList<String> lista, String titulo) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(tituloLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(lista);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Carga voluntarios en la lista
     */
    private void cargarVoluntariosEnLista(DefaultListModel<String> modelo, Actividad actividad) {
        modelo.clear();
        try {
            List<Voluntario> todosVoluntarios = gestorGeneral.getGestorUsuarios().obtenerVoluntarios();
            List<String> voluntariosAsignadosIds = obtenerVoluntariosAsignadosIds(actividad.getId());

            for (Voluntario vol : todosVoluntarios) {
                boolean yaAsignado = voluntariosAsignadosIds.contains(vol.getId());
                String estado = yaAsignado ? " (Ya asignado)" : "";
                modelo.addElement(vol.getNombre() + " - " + vol.getEmail() + estado);
            }
        } catch (Exception e) {
            modelo.addElement("Error: " + e.getMessage());
        }
    }

    /**
     * Carga brigadas en la lista
     */
    private void cargarBrigadasEnLista(DefaultListModel<String> modelo, Actividad actividad) {
        modelo.clear();
        try {
            List<Brigada> todasBrigadas = gestorGeneral.getGestorBrigadas().obtenerTodasBrigadas();
            Brigada brigadaActual = actividad.getBrigadaAsociada();

            for (Brigada brigada : todasBrigadas) {
                boolean yaAsignada = (brigadaActual != null && brigadaActual.getId().equals(brigada.getId()));
                String estado = yaAsignada ? " (Ya asignada)" : "";
                modelo.addElement(brigada.getNombre() + " - " + brigada.getTipo() +
                        " (" + brigada.getCantidadVoluntarios() + " voluntarios)" + estado);
            }
        } catch (Exception e) {
            modelo.addElement("Error: " + e.getMessage());
        }
    }

    /**
     * Procesa la adición de elementos seleccionados
     */
    private void procesarAgregarSeleccionados(JDialog dialog, Actividad actividad, JTabbedPane tabbedPane,
                                              JList<String> voluntariosList, JList<String> brigadasList) {

        int selectedTab = tabbedPane.getSelectedIndex();

        if (selectedTab == 0) { // Pestaña de Voluntarios
            int[] selectedIndices = voluntariosList.getSelectedIndices();
            if (selectedIndices.length == 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor seleccione al menos un voluntario para agregar",
                        "Selección requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                List<String> voluntariosIds = new ArrayList<>();

                for (int index : selectedIndices) {
                    String selected = voluntariosList.getModel().getElementAt(index);

                    // No permitir agregar voluntarios ya asignados
                    if (selected.contains("(Ya asignado)")) {
                        String nombreVoluntario = selected.split(" - ")[0];
                        JOptionPane.showMessageDialog(dialog,
                                "El voluntario " + nombreVoluntario + " ya está asignado",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                        continue;
                    }

                    // Extraer email y buscar voluntario
                    String email = selected.split(" - ")[1];
                    Voluntario vol = gestorGeneral.getGestorUsuarios().buscarVoluntarioPorEmail(email);
                    if (vol != null) {
                        voluntariosIds.add(vol.getId());
                    }
                }

                if (!voluntariosIds.isEmpty()) {
                    // Agregar voluntarios a la actividad
                    List<Voluntario> voluntariosActuales = gestorActividades.obtenerVoluntariosAsignados(actividad.getId());
                    List<String> nuevosIds = new ArrayList<>();

                    for (Voluntario vol : voluntariosActuales) {
                        nuevosIds.add(vol.getId());
                    }

                    // Agregar los nuevos IDs sin duplicados
                    for (String id : voluntariosIds) {
                        if (!nuevosIds.contains(id)) {
                            nuevosIds.add(id);
                        }
                    }

                    gestorActividades.asignarVoluntariosAActividad(actividad.getId(), nuevosIds);

                    JOptionPane.showMessageDialog(dialog,
                            "Se agregaron " + voluntariosIds.size() + " voluntario(s) a la actividad",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al agregar voluntarios: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (selectedTab == 1) { // Pestaña de Brigadas
            int[] selectedIndices = brigadasList.getSelectedIndices();
            if (selectedIndices.length == 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor seleccione una brigada para agregar",
                        "Selección requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedIndices.length > 1) {
                JOptionPane.showMessageDialog(dialog,
                        "Solo puede seleccionar una brigada a la vez",
                        "Selección múltiple", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String selected = brigadasList.getModel().getElementAt(selectedIndices[0]);

                // Verificar si ya está asignada
                if (selected.contains("(Ya asignada)")) {
                    JOptionPane.showMessageDialog(dialog,
                            "Esta brigada ya está asignada a la actividad",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Extraer nombre de brigada
                String brigadaNombre = selected.split(" \\(")[0];

                // Buscar brigada por nombre
                List<Brigada> brigadas = gestorGeneral.getGestorBrigadas().obtenerTodasBrigadas();
                Brigada brigadaSeleccionada = null;

                for (Brigada brigada : brigadas) {
                    if (brigada.getNombre().equals(brigadaNombre)) {
                        brigadaSeleccionada = brigada;
                        break;
                    }
                }

                if (brigadaSeleccionada != null) {
                    // Confirmar asignación de brigada
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                            "¿Desea asignar la brigada '" + brigadaSeleccionada.getNombre() + "'?\n" +
                                    "Esto agregará automáticamente a sus " +
                                    brigadaSeleccionada.getCantidadVoluntarios() + " voluntarios.",
                            "Confirmar asignación de brigada",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Asignar brigada
                        actividad.setBrigadaAsociada(brigadaSeleccionada);

                        // Agregar todos los voluntarios de la brigada
                        List<Voluntario> voluntariosBrigada = gestorGeneral.getGestorBrigadas()
                                .obtenerVoluntariosDeBrigada(brigadaSeleccionada.getId());

                        List<String> voluntariosIds = new ArrayList<>();
                        for (Voluntario vol : voluntariosBrigada) {
                            voluntariosIds.add(vol.getId());
                        }

                        gestorActividades.asignarVoluntariosAActividad(actividad.getId(), voluntariosIds);

                        JOptionPane.showMessageDialog(dialog,
                                "Brigada '" + brigadaSeleccionada.getNombre() + "' asignada exitosamente\n" +
                                        "Se agregaron " + voluntariosIds.size() + " voluntarios de la brigada",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al agregar brigada: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Procesa la eliminación de elementos seleccionados
     */
    private void procesarQuitarSeleccionados(JDialog dialog, Actividad actividad, JTabbedPane tabbedPane,
                                             JList<String> voluntariosList, JList<String> brigadasList) {

        int selectedTab = tabbedPane.getSelectedIndex();

        if (selectedTab == 0) { // Pestaña de Voluntarios
            int[] selectedIndices = voluntariosList.getSelectedIndices();
            if (selectedIndices.length == 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor seleccione al menos un voluntario para quitar",
                        "Selección requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                List<String> voluntariosAQuitar = new ArrayList<>();
                List<String> nombresVoluntarios = new ArrayList<>();

                for (int index : selectedIndices) {
                    String selected = voluntariosList.getModel().getElementAt(index);

                    // Solo se pueden quitar voluntarios que ya están asignados
                    if (!selected.contains("(Ya asignado)")) {
                        String nombreVoluntario = selected.split(" - ")[0];
                        JOptionPane.showMessageDialog(dialog,
                                "El voluntario " + nombreVoluntario + " no está asignado",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                        continue;
                    }

                    // Extraer email y buscar voluntario
                    String email = selected.split(" - ")[1].replace(" (Ya asignado)", "").trim();
                    Voluntario vol = gestorGeneral.getGestorUsuarios().buscarVoluntarioPorEmail(email);
                    if (vol != null) {
                        voluntariosAQuitar.add(vol.getId());
                        nombresVoluntarios.add(vol.getNombre());
                    }
                }

                if (!voluntariosAQuitar.isEmpty()) {
                    // Confirmar eliminación
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                            "¿Está seguro de quitar " + voluntariosAQuitar.size() +
                                    " voluntario(s) de la actividad?\n" +
                                    String.join(", ", nombresVoluntarios),
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Obtener voluntarios actuales
                        List<Voluntario> voluntariosActuales = gestorActividades.obtenerVoluntariosAsignados(actividad.getId());
                        List<String> nuevosIds = new ArrayList<>();

                        for (Voluntario vol : voluntariosActuales) {
                            if (!voluntariosAQuitar.contains(vol.getId())) {
                                nuevosIds.add(vol.getId());
                            }
                        }

                        gestorActividades.asignarVoluntariosAActividad(actividad.getId(), nuevosIds);

                        JOptionPane.showMessageDialog(dialog,
                                "Se quitaron " + voluntariosAQuitar.size() + " voluntario(s) de la actividad",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al quitar voluntarios: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (selectedTab == 1) { // Pestaña de Brigadas
            int[] selectedIndices = brigadasList.getSelectedIndices();
            if (selectedIndices.length == 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor seleccione una brigada para quitar",
                        "Selección requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String selected = brigadasList.getModel().getElementAt(selectedIndices[0]);

                // Solo se puede quitar una brigada que ya está asignada
                if (!selected.contains("(Ya asignada)")) {
                    JOptionPane.showMessageDialog(dialog,
                            "Esta brigada no está asignada a la actividad",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Extraer nombre de brigada
                String brigadaNombre = selected.split(" \\(")[0];

                // Buscar brigada actual
                Brigada brigadaActual = actividad.getBrigadaAsociada();

                if (brigadaActual != null && brigadaActual.getNombre().equals(brigadaNombre)) {
                    // Confirmar quitar brigada
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                            "¿Está seguro de quitar la brigada '" + brigadaActual.getNombre() + "'?\n" +
                                    "Esto quitará a todos los voluntarios que participan por esta brigada.",
                            "Confirmar quitar brigada",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Quitar brigada
                        actividad.setBrigadaAsociada(null);

                        JOptionPane.showMessageDialog(dialog,
                                "Brigada '" + brigadaNombre + "' quitada exitosamente",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al quitar brigada: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Guarda todos los cambios realizados
     */
    private void guardarCambios(JDialog dialog, Actividad actividad) {
        try {
            // Actualizar la actividad en el sistema
            gestorActividades.actualizarActividad(actividad);

            JOptionPane.showMessageDialog(dialog,
                    "Todos los cambios han sido guardados exitosamente",
                    "Cambios guardados", JOptionPane.INFORMATION_MESSAGE);

            dialog.dispose();
            actualizarTablaActividades();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog,
                    "Error al guardar cambios: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene voluntarios asignados a una actividad
     */
    private List<Voluntario> obtenerVoluntariosAsignadosAActividad(String actividadId) {
        try {
            return gestorActividades.obtenerVoluntariosAsignados(actividadId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * RF-07: Registrar resultados de actividad
     */
    private void mostrarDialogoRegistrarResultados(Actividad actividad) {
        JDialog dialog = new JDialog(this, "Registrar Resultados: " + actividad.getNombre(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Información de la actividad
        JLabel infoLabel = new JLabel("<html><b>Actividad:</b> " + actividad.getNombre() +
                "<br><b>Fecha:</b> " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(actividad.getFecha()) +
                "<br><b>Lugar:</b> " + actividad.getLugar() + "</html>");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(infoLabel, gbc);

        // Campos del formulario
        gbc.gridwidth = 1;

        int row = 1;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Personas beneficiadas:"), gbc);
        gbc.gridx = 1;
        JSpinner beneficiadosSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        formPanel.add(beneficiadosSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Horas trabajadas:"), gbc);
        gbc.gridx = 1;
        JSpinner horasSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.5, 24.0, 0.5));
        formPanel.add(horasSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Resultados alcanzados:"), gbc);
        gbc.gridx = 1;
        JTextArea resultadosArea = new JTextArea(4, 20);
        resultadosArea.setLineWrap(true);
        formPanel.add(new JScrollPane(resultadosArea), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        JTextArea observacionesArea = new JTextArea(3, 20);
        observacionesArea.setLineWrap(true);
        formPanel.add(new JScrollPane(observacionesArea), gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Registrar Resultados");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            try {
                int personasBeneficiadas = (int) beneficiadosSpinner.getValue();
                double horasTrabajadas = (double) horasSpinner.getValue();
                String resultados = resultadosArea.getText().trim();
                String observaciones = observacionesArea.getText().trim();

                if (resultados.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "El campo 'Resultados alcanzados' es requerido",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Registrar resultados usando el gestor
                gestorActividades.registrarResultados(
                        actividad.getId(),
                        personasBeneficiadas,
                        horasTrabajadas,
                        resultados,
                        observaciones
                );

                JOptionPane.showMessageDialog(dialog,
                        "Resultados registrados exitosamente para la actividad '" +
                                actividad.getNombre() + "'",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaActividades();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al registrar resultados: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(CARD_PURPLE);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Mostrar formulario para editar actividad
     */
    private void mostrarFormularioEditarActividad(Actividad actividad) {
        JDialog dialog = new JDialog(this, "Editar Actividad: " + actividad.getNombre(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 550);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario con datos actuales
        JLabel idLabel = new JLabel(actividad.getId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JTextField nombreField = new JTextField(actividad.getNombre(), 20);

        // Campo de fecha
        JPanel fechaPanel = new JPanel(new BorderLayout());
        JTextField fechaField = new JTextField(
                new java.text.SimpleDateFormat("dd/MM/yyyy").format(actividad.getFecha()), 15);
        JButton fechaBtn = new JButton("📅");
        fechaBtn.addActionListener(e -> {
            fechaField.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
        });
        fechaPanel.add(fechaField, BorderLayout.CENTER);
        fechaPanel.add(fechaBtn, BorderLayout.EAST);

        JTextField lugarField = new JTextField(actividad.getLugar(), 20);
        JTextArea objetivoArea = new JTextArea(actividad.getObjetivo(), 4, 20);
        objetivoArea.setLineWrap(true);

        // Combo box para seleccionar brigada
        JComboBox<String> brigadaCombo = new JComboBox<>();
        try {
            List<Brigada> brigadas = gestorGeneral.getGestorBrigadas().obtenerTodasBrigadas();
            for (Brigada brigada : brigadas) {
                String item = brigada.getNombre() + " (" + brigada.getId() + ")";
                brigadaCombo.addItem(item);

                // Seleccionar la brigada actual si existe
                if (actividad.getBrigadaAsociada() != null &&
                        actividad.getBrigadaAsociada().getId().equals(brigada.getId())) {
                    brigadaCombo.setSelectedItem(item);
                }
            }
        } catch (Exception e) {
            brigadaCombo.addItem("No hay brigadas disponibles");
        }

        // Configurar GridBagConstraints
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nombre*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Fecha*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fechaPanel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Lugar*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lugarField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Objetivo*:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(objetivoArea), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Brigada Responsable:"), gbc);
        gbc.gridx = 1;
        formPanel.add(brigadaCombo, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Guardar Cambios");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String fecha = fechaField.getText().trim();
            String lugar = lugarField.getText().trim();
            String objetivo = objetivoArea.getText().trim();

            if (nombre.isEmpty() || fecha.isEmpty() || lugar.isEmpty() || objetivo.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Nombre, Fecha, Lugar y Objetivo son campos requeridos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Actualizar actividad
                actividad.setNombre(nombre);
                actividad.setLugar(lugar);
                actividad.setObjetivo(objetivo);

                // Actualizar fecha
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                actividad.setFecha(sdf.parse(fecha));

                // Actualizar brigada si cambió
                String brigadaSeleccionada = (String) brigadaCombo.getSelectedItem();
                if (brigadaSeleccionada != null && brigadaSeleccionada.contains("(")) {
                    String brigadaId = brigadaSeleccionada.substring(
                            brigadaSeleccionada.indexOf("(") + 1,
                            brigadaSeleccionada.indexOf(")"));

                    Brigada nuevaBrigada = gestorGeneral.getGestorBrigadas().buscarBrigadaPorId(brigadaId);
                    if (nuevaBrigada != null) {
                        actividad.setBrigadaAsociada(nuevaBrigada);
                    }
                }

                // Guardar cambios
                gestorActividades.actualizarActividad(actividad);

                JOptionPane.showMessageDialog(dialog,
                        "Actividad '" + nombre + "' actualizada exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaActividades();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al actualizar actividad: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(PRIMARY_BLUE);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Muestra formulario para nueva actividad (RF-04)
     */
    private void mostrarFormularioNuevaActividad() {
        JDialog dialog = new JDialog(this, "Planificar Nueva Actividad (RF-04)", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        JTextField idField = new JTextField(generarIdActividad(), 20);
        idField.setEditable(false);
        JTextField nombreField = new JTextField(20);

        // Campo de fecha con DatePicker simplificado
        JPanel fechaPanel = new JPanel(new BorderLayout());
        JTextField fechaField = new JTextField(15);
        fechaField.setToolTipText("Formato: DD/MM/AAAA");
        JButton fechaBtn = new JButton("📅");
        fechaBtn.addActionListener(e -> {
            // En una implementación real, aquí iría un DatePicker
            fechaField.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
        });
        fechaPanel.add(fechaField, BorderLayout.CENTER);
        fechaPanel.add(fechaBtn, BorderLayout.EAST);

        JTextField lugarField = new JTextField(20);
        JTextArea objetivoArea = new JTextArea(4, 20);
        objetivoArea.setLineWrap(true);

        JTextArea beneficiariosArea = new JTextArea(3, 20);
        beneficiariosArea.setLineWrap(true);
        beneficiariosArea.setToolTipText("Ingrese los beneficiarios separados por comas");

        // Combo box para seleccionar brigada
        JComboBox<String> brigadaCombo = new JComboBox<>();
        try {
            List<Brigada> brigadas = gestorGeneral.getGestorBrigadas().obtenerTodasBrigadas();
            for (Brigada brigada : brigadas) {
                brigadaCombo.addItem(brigada.getNombre() + " (" + brigada.getId() + ")");
            }
        } catch (Exception e) {
            brigadaCombo.addItem("No hay brigadas disponibles");
        }

        // Configurar GridBagConstraints
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("ID (automático):"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nombre*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Fecha*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fechaPanel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Lugar*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lugarField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Objetivo*:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(objetivoArea), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Beneficiarios:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(beneficiariosArea), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Brigada Responsable*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(brigadaCombo, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Planificar Actividad");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            // Validar campos
            String nombre = nombreField.getText().trim();
            String fecha = fechaField.getText().trim();
            String lugar = lugarField.getText().trim();
            String objetivo = objetivoArea.getText().trim();
            String beneficiarios = beneficiariosArea.getText().trim();
            String brigadaSeleccionada = (String) brigadaCombo.getSelectedItem();

            if (nombre.isEmpty() || fecha.isEmpty() || lugar.isEmpty() || objetivo.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Nombre, Fecha, Lugar y Objetivo son campos requeridos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de fecha básico
            if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor ingrese la fecha en formato DD/MM/AAAA",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Obtener ID de la brigada seleccionada
                String brigadaId = "";
                if (brigadaSeleccionada != null && brigadaSeleccionada.contains("(")) {
                    brigadaId = brigadaSeleccionada.substring(brigadaSeleccionada.indexOf("(") + 1,
                            brigadaSeleccionada.indexOf(")"));
                }

                // Convertir fecha de String a Date
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                java.util.Date fechaDate = sdf.parse(fecha);

                // CAMBIO: Usar el gestor de actividades real
                Actividad nuevaActividad = gestorActividades.planificarActividad(
                        idField.getText(),
                        nombre,
                        fechaDate,
                        lugar,
                        objetivo,
                        brigadaId
                );

                JOptionPane.showMessageDialog(dialog,
                        "Actividad '" + nombre + "' planificada exitosamente\n" +
                                "Fecha: " + fecha + "\n" +
                                "Lugar: " + lugar + "\n" +
                                "Beneficiarios: " + (beneficiarios.isEmpty() ? "No especificados" : beneficiarios),
                        "Actividad Planificada (RF-04)", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaActividades();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al planificar actividad: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(CARD_GREEN);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Carga las actividades en la tabla
     */
    private void cargarActividadesEnTabla(DefaultTableModel model) {
        model.setRowCount(0); // Limpiar tabla

        try {
            // CAMBIO: Usar el gestor real en lugar de simulado
            List<Actividad> actividades = gestorActividades.obtenerTodasActividades();

            for (Actividad actividad : actividades) {
                String fechaStr = new java.text.SimpleDateFormat("dd/MM/yyyy").format(actividad.getFecha());
                String brigadaNombre = actividad.getBrigadaAsociada() != null ?
                        actividad.getBrigadaAsociada().getNombre() : "Sin asignar";

                // Determinar estado basado en fecha
                String estado;
                Date ahora = new Date();
                if (actividad.getFecha().after(ahora)) {
                    estado = "Pendiente";
                } else if (actividad.getFecha().before(ahora) && actividad.getResultados() == null) {
                    estado = "En proceso";
                } else if (actividad.getResultados() != null) {
                    estado = "Completada";
                } else {
                    estado = "Planificada";
                }

                model.addRow(new Object[]{
                        actividad.getId(),
                        actividad.getNombre(),
                        fechaStr,
                        actividad.getLugar(),
                        actividad.getObjetivo(),
                        brigadaNombre,
                        estado,
                        "Asignar|Resultados|Editar|Eliminar"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando actividades: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la tabla de actividades - VERSIÓN CORREGIDA
     */
    private void actualizarTablaActividades() {
        SwingUtilities.invokeLater(() -> {
            // Buscar el JScrollPane en el panel de actividades
            JScrollPane scrollPane = findScrollPaneInPanel(actividadesPanel);

            if (scrollPane != null) {
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    JTable tabla = (JTable) view;
                    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                    cargarActividadesEnTabla(model);
                    tabla.repaint();
                    tabla.revalidate();
                }
            }
        });
    }

    /**
     * Genera un ID automático para actividad
     */
    private String generarIdActividad() {
        try {
            return gestorActividades.generarIdActividadGestor();
        } catch (Exception e) {
            return "ACT-001";
        }
    }

    /**
     * Busca actividades por término
     */
    private void buscarActividades(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            actualizarTablaActividades();
            return;
        }

        try {
            Component[] components = actividadesPanel.getComponents();
            JTable tabla = null;

            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    JViewport viewport = ((JScrollPane) comp).getViewport();
                    if (viewport.getView() instanceof JTable) {
                        tabla = (JTable) viewport.getView();
                        break;
                    }
                }
            }

            if (tabla == null) return;

            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            List<Actividad> actividadesEncontradas = gestorActividades.buscarActividades(termino);

            // Limpiar la tabla
            model.setRowCount(0);

            String terminoLower = termino.toLowerCase();
            for (Actividad actividad : actividadesEncontradas) {
                // Buscar en nombre, lugar, objetivo o brigada
                if (actividad.getNombre().toLowerCase().contains(terminoLower) ||
                        actividad.getLugar().toLowerCase().contains(terminoLower) ||
                        actividad.getObjetivo().toLowerCase().contains(terminoLower) ||
                        (actividad.getBrigadaAsociada() != null &&
                                actividad.getBrigadaAsociada().getNombre().toLowerCase().contains(terminoLower))) {

                    String fechaStr = new java.text.SimpleDateFormat("dd/MM/yyyy").format(actividad.getFecha());
                    String brigadaNombre = actividad.getBrigadaAsociada() != null ?
                            actividad.getBrigadaAsociada().getNombre() : "Sin asignar";

                    model.addRow(new Object[]{
                            actividad.getId(),
                            actividad.getNombre(),
                            fechaStr,
                            actividad.getLugar(),
                            actividad.getObjetivo(),
                            brigadaNombre,
                            "Planificada",
                            "Asignar|Resultados|Editar|Eliminar"
                    });
                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron actividades que coincidan con: '" + termino + "'",
                        "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error en la búsqueda: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Elimina una actividad
     */
    /**
     * Elimina una actividad - VERSIÓN CORREGIDA PARA LA ESTRUCTURA REAL
     */
    private void eliminarActividad(String id, String nombre) {
        try {
            // Obtener la actividad
            Actividad actividad = gestorActividades.buscarActividadPorId(id);
            if (actividad == null) {
                JOptionPane.showMessageDialog(this, "Actividad no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si hay voluntarios asignados
            boolean tieneVoluntarios = actividad.getVoluntariosAsignados() != null &&
                    !actividad.getVoluntariosAsignados().isEmpty();

            if (tieneVoluntarios) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "La actividad '" + nombre + "' tiene " +
                                actividad.getVoluntariosAsignados().size() + " voluntarios asignados.\n" +
                                "¿Desea eliminar la actividad de todos modos?\n" +
                                "(Los voluntarios serán desasignados automáticamente)",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Eliminar la actividad (el gestor debería manejar la limpieza)
            gestorActividades.eliminarActividad(id);

            JOptionPane.showMessageDialog(this,
                    "Actividad '" + nombre + "' eliminada exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            actualizarTablaActividades();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar actividad: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }



    private JPanel createRecursosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_GRAY);

        JLabel title = new JLabel("Gestión de Recursos e Inventario");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("RF-06: Asignar recursos | RF-10: Consultar disponibilidad");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Barra de herramientas
        panel.add(createToolbarRecursos(), BorderLayout.NORTH);

        // Tabla de recursos
        String[] columnNames = {"ID", "Nombre", "Categoría", "Stock Actual", "Umbral Alerta", "Capacidad Máx", "Unidad", "Estado", "Acciones"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Solo la columna de acciones es editable
            }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(65);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));

        // Configurar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);   // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);  // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);  // Categoría
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);   // Stock Actual
        tabla.getColumnModel().getColumn(4).setPreferredWidth(80);   // Umbral Alerta
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80);   // Capacidad Máx
        tabla.getColumnModel().getColumn(6).setPreferredWidth(70);   // Unidad
        tabla.getColumnModel().getColumn(7).setPreferredWidth(100);  // Estado
        tabla.getColumnModel().getColumn(8).setPreferredWidth(200);  // Acciones

        // Configurar renderizador y editor para botones de acciones
        tabla.getColumnModel().getColumn(8).setCellRenderer(new RecursosButtonRenderer());
        tabla.getColumnModel().getColumn(8).setCellEditor(new RecursosButtonEditor(new JCheckBox(), tabla));

        // Cargar datos
        cargarRecursosEnTabla(model);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(1200, 500));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createToolbarRecursos() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Botón Nuevo Recurso
        JButton nuevoBtn = createToolbarButton("Nuevo Recurso", "Agregar nuevo recurso al inventario",
                CARD_GREEN, e -> mostrarFormularioNuevoRecurso());
        toolbar.add(nuevoBtn);

        // Botón Reponer Stock
        JButton reponerBtn = createToolbarButton("Reponer Stock", "Reponer stock de recursos",
                CARD_ORANGE, e -> mostrarDialogoReponerStock());
        toolbar.add(reponerBtn);

        // Campo de búsqueda
        JTextField searchField = new JTextField(25);
        searchField.setToolTipText("Buscar recurso por nombre, categoría o ID");
        toolbar.add(searchField);

        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.addActionListener(e -> buscarRecursos(searchField.getText()));
        toolbar.add(buscarBtn);

        // Botón Actualizar
        JButton actualizarBtn = createToolbarButton("Actualizar", "Actualizar inventario de recursos",
                PRIMARY_BLUE, e -> actualizarTablaRecursos());
        toolbar.add(actualizarBtn);

        // Botón Alertas de Stock
        JButton alertasBtn = createToolbarButton("Ver Alertas", "Ver recursos con stock bajo",
                CARD_RED, e -> mostrarAlertasStock());
        toolbar.add(alertasBtn);

        return toolbar;
    }

    /**
     * Carga los recursos en la tabla
     */
    private void cargarRecursosEnTabla(DefaultTableModel model) {
        model.setRowCount(0); // Limpiar tabla

        try {
            List<Recurso> recursos = gestorGeneral.getGestorRecursos().obtenerTodosRecursos();

            for (Recurso recurso : recursos) {
                String estado;
                Color estadoColor;

                if (recurso.getStockActual() <= 0) {
                    estado = "AGOTADO";
                    estadoColor = Color.RED;
                } else if (recurso.getStockActual() <= recurso.getUmbralAlerta()) {
                    estado = "BAJO STOCK";
                    estadoColor = Color.ORANGE;
                } else {
                    estado = "DISPONIBLE";
                    estadoColor = new Color(0, 150, 0); // Verde
                }

                model.addRow(new Object[]{
                        recurso.getId(),
                        recurso.getNombre(),
                        recurso.getCategoria(),
                        recurso.getStockActual(),
                        recurso.getUmbralAlerta(),
                        recurso.getCapacidadMaxima(),
                        recurso.getUnidadMedida(),
                        estado,
                        "Editar|Reponer|Eliminar|Asignar"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando recursos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la tabla de recursos - VERSIÓN CORREGIDA
     */
    private void actualizarTablaRecursos() {
        SwingUtilities.invokeLater(() -> {
            // Buscar el JScrollPane en el panel de recursos
            JScrollPane scrollPane = findScrollPaneInPanel(recursosPanel);

            if (scrollPane != null) {
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    JTable tabla = (JTable) view;
                    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                    cargarRecursosEnTabla(model);
                    tabla.repaint();
                    tabla.revalidate();
                }
            }
        });
    }

    private void mostrarFormularioNuevoRecurso() {
        JDialog dialog = new JDialog(this, "Agregar Nuevo Recurso", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        JTextField idField = new JTextField(generarIdRecurso(), 20);
        idField.setEditable(false);
        JTextField nombreField = new JTextField(20);
        JTextField categoriaField = new JTextField(20);

        JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        JSpinner umbralSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 1000, 1));
        JSpinner capacidadSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));

        JTextField unidadField = new JTextField(20);
        unidadField.setText("Unidad");

        // Configurar GridBagConstraints
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("ID (automático):"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nombre*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Categoría*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(categoriaField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Stock Inicial:"), gbc);
        gbc.gridx = 1;
        formPanel.add(stockSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Umbral de Alerta:"), gbc);
        gbc.gridx = 1;
        formPanel.add(umbralSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Capacidad Máxima:"), gbc);
        gbc.gridx = 1;
        formPanel.add(capacidadSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Unidad de Medida:"), gbc);
        gbc.gridx = 1;
        formPanel.add(unidadField, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Agregar Recurso");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String categoria = categoriaField.getText().trim();
            String unidad = unidadField.getText().trim();

            if (nombre.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Nombre y Categoría son campos requeridos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int stockInicial = (int) stockSpinner.getValue();
                int umbral = (int) umbralSpinner.getValue();
                int capacidadMax = (int) capacidadSpinner.getValue();

                // Crear nuevo recurso
                Recurso nuevoRecurso = new Recurso(
                        idField.getText(),
                        nombre,
                        categoria,
                        capacidadMax,
                        umbral,
                        unidad
                );

                // Si hay stock inicial, reponerlo
                if (stockInicial > 0) {
                    nuevoRecurso.reponerStock(stockInicial);
                }

                // Agregar al gestor
                gestorGeneral.getGestorRecursos().agregarRecurso(nuevoRecurso);

                JOptionPane.showMessageDialog(dialog,
                        "Recurso '" + nombre + "' agregado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaRecursos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al agregar recurso: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(CARD_GREEN);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Genera un ID automático para recurso
     */
    private String generarIdRecurso() {
        try {
            List<Recurso> recursos = gestorGeneral.getGestorRecursos().obtenerTodosRecursos();
            int nextId = recursos.size() + 1;
            return String.format("REC-%03d", nextId);
        } catch (Exception e) {
            return "REC-001";
        }
    }

    /**
     * Busca recursos por término
     */
    private void buscarRecursos(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            actualizarTablaRecursos();
            return;
        }

        try {
            Component[] components = recursosPanel.getComponents();
            JTable tabla = null;

            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    JViewport viewport = ((JScrollPane) comp).getViewport();
                    if (viewport.getView() instanceof JTable) {
                        tabla = (JTable) viewport.getView();
                        break;
                    }
                }
            }

            if (tabla == null) return;

            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            List<Recurso> recursosEncontrados = gestorGeneral.getGestorRecursos().buscarRecursos(termino);

            // Limpiar la tabla
            model.setRowCount(0);

            for (Recurso recurso : recursosEncontrados) {
                String estado = recurso.getStockActual() <= recurso.getUmbralAlerta() ?
                        "BAJO STOCK" : "DISPONIBLE";

                model.addRow(new Object[]{
                        recurso.getId(),
                        recurso.getNombre(),
                        recurso.getCategoria(),
                        recurso.getStockActual(),
                        recurso.getUmbralAlerta(),
                        recurso.getCapacidadMaxima(),
                        recurso.getUnidadMedida(),
                        estado,
                        "Editar|Reponer|Eliminar|Asignar"
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron recursos que coincidan con: '" + termino + "'",
                        "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error en la búsqueda: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Muestra diálogo para reponer stock
     */
    private void mostrarDialogoReponerStock() {
        JDialog dialog = new JDialog(this, "Reponer Stock de Recursos", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Lista de recursos
        JLabel recursoLabel = new JLabel("Recurso:");
        JComboBox<String> recursoCombo = new JComboBox<>();

        try {
            List<Recurso> recursos = gestorGeneral.getGestorRecursos().obtenerTodosRecursos();
            for (Recurso recurso : recursos) {
                recursoCombo.addItem(recurso.getNombre() + " (ID: " + recurso.getId() + ")");
            }
        } catch (Exception e) {
            recursoCombo.addItem("No hay recursos disponibles");
        }

        JLabel cantidadLabel = new JLabel("Cantidad a reponer:");
        JSpinner cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(recursoLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(recursoCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(cantidadLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(cantidadSpinner, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Reponer Stock");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            try {
                String recursoSeleccionado = (String) recursoCombo.getSelectedItem();
                if (recursoSeleccionado == null || recursoSeleccionado.contains("No hay")) {
                    JOptionPane.showMessageDialog(dialog,
                            "No hay recursos disponibles para reponer",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Extraer ID del recurso
                String recursoId = recursoSeleccionado.substring(
                        recursoSeleccionado.indexOf("(ID: ") + 5,
                        recursoSeleccionado.indexOf(")")
                );

                int cantidad = (int) cantidadSpinner.getValue();

                // Reponer stock
                gestorGeneral.getGestorRecursos().reponerStock(recursoId, cantidad);

                JOptionPane.showMessageDialog(dialog,
                        "Stock repuesto exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaRecursos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al reponer stock: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(CARD_ORANGE);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Muestra alertas de stock bajo
     */
    private void mostrarAlertasStock() {
        try {
            List<Recurso> recursosBajoStock = gestorGeneral.getGestorRecursos().obtenerRecursosBajoStock();

            if (recursosBajoStock.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay recursos con stock bajo en este momento.",
                        "Sin Alertas", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Recursos con stock bajo:\n\n");

            for (Recurso recurso : recursosBajoStock) {
                mensaje.append("• ").append(recurso.getNombre())
                        .append(" (ID: ").append(recurso.getId()).append(")\n")
                        .append("  Stock actual: ").append(recurso.getStockActual())
                        .append(" | Umbral: ").append(recurso.getUmbralAlerta())
                        .append("\n\n");
            }

            JOptionPane.showMessageDialog(this,
                    mensaje.toString(),
                    "Alertas de Stock", JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener alertas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Renderizador para botones en la tabla de recursos
     */
    class RecursosButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton editarBtn;
        private JButton reponerBtn;
        private JButton eliminarBtn;
        private JButton asignarBtn;

        public RecursosButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 3, 5));
            setOpaque(true);
            setPreferredSize(new Dimension(200, 40));

            // Botón Editar
            editarBtn = new JButton("Editar");
            editarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editarBtn.setBackground(new Color(33, 150, 243));
            editarBtn.setForeground(Color.WHITE);
            editarBtn.setFocusPainted(false);
            editarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            editarBtn.setPreferredSize(new Dimension(55, 25));
            editarBtn.setToolTipText("Editar información del recurso");

            // Botón Reponer
            reponerBtn = new JButton("Reponer");
            reponerBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            reponerBtn.setBackground(new Color(255, 152, 0));
            reponerBtn.setForeground(Color.WHITE);
            reponerBtn.setFocusPainted(false);
            reponerBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            reponerBtn.setPreferredSize(new Dimension(65, 25));
            reponerBtn.setToolTipText("Reponer stock");

            // Botón Eliminar
            eliminarBtn = new JButton("Eliminar");
            eliminarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            eliminarBtn.setBackground(new Color(244, 67, 54));
            eliminarBtn.setForeground(Color.WHITE);
            eliminarBtn.setFocusPainted(false);
            eliminarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            eliminarBtn.setPreferredSize(new Dimension(65, 25));
            eliminarBtn.setToolTipText("Eliminar recurso");

            // Botón Asignar (RF-06)
            asignarBtn = new JButton("Asignar");
            asignarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            asignarBtn.setBackground(new Color(76, 175, 80));
            asignarBtn.setForeground(Color.WHITE);
            asignarBtn.setFocusPainted(false);
            asignarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            asignarBtn.setPreferredSize(new Dimension(65, 25));
            asignarBtn.setToolTipText("Asignar a actividad (RF-06)");

            // Agregar botones al panel
            add(editarBtn);
            add(reponerBtn);
            add(eliminarBtn);
            add(asignarBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * Muestra formulario para editar recurso
     */
    private void mostrarFormularioEditarRecurso(Recurso recurso) {
        JDialog dialog = new JDialog(this, "Editar Recurso: " + recurso.getNombre(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario con datos actuales
        JLabel idLabel = new JLabel(recurso.getId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JTextField nombreField = new JTextField(recurso.getNombre(), 20);
        JTextField categoriaField = new JTextField(recurso.getCategoria(), 20);

        JSpinner umbralSpinner = new JSpinner(new SpinnerNumberModel(
                recurso.getUmbralAlerta(), 0, recurso.getCapacidadMaxima(), 1));

        JSpinner capacidadSpinner = new JSpinner(new SpinnerNumberModel(
                recurso.getCapacidadMaxima(), recurso.getStockActual(), 10000, 1));

        JTextField unidadField = new JTextField(recurso.getUnidadMedida(), 20);

        // Mostrar stock actual (solo lectura)
        JLabel stockLabel = new JLabel(String.valueOf(recurso.getStockActual()));
        stockLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // Configurar GridBagConstraints
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nombre*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Categoría*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(categoriaField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Stock Actual:"), gbc);
        gbc.gridx = 1;
        formPanel.add(stockLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Umbral de Alerta:"), gbc);
        gbc.gridx = 1;
        formPanel.add(umbralSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Capacidad Máxima:"), gbc);
        gbc.gridx = 1;
        formPanel.add(capacidadSpinner, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Unidad de Medida:"), gbc);
        gbc.gridx = 1;
        formPanel.add(unidadField, gbc);

        // Panel de botones adicionales
        JPanel extraButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton reponerBtn = new JButton("Reponer Stock");
        reponerBtn.setBackground(CARD_ORANGE);
        reponerBtn.setForeground(Color.WHITE);
        reponerBtn.addActionListener(e -> {
            String cantidadStr = JOptionPane.showInputDialog(dialog,
                    "Ingrese la cantidad a reponer:",
                    "Reponer Stock",
                    JOptionPane.QUESTION_MESSAGE);

            if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                try {
                    int cantidad = Integer.parseInt(cantidadStr);
                    if (cantidad <= 0) {
                        JOptionPane.showMessageDialog(dialog,
                                "La cantidad debe ser mayor que cero",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    recurso.reponerStock(cantidad);
                    stockLabel.setText(String.valueOf(recurso.getStockActual()));
                    JOptionPane.showMessageDialog(dialog,
                            "Stock repuesto exitosamente. Nuevo stock: " + recurso.getStockActual(),
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Por favor ingrese un número válido",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        extraButtonsPanel.add(reponerBtn);

        gbc.gridx = 0; gbc.gridy = ++row;
        gbc.gridwidth = 2;
        formPanel.add(extraButtonsPanel, gbc);

        // Panel de botones principal
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Guardar Cambios");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String categoria = categoriaField.getText().trim();
            String unidad = unidadField.getText().trim();

            if (nombre.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Nombre y Categoría son campos requeridos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int umbral = (int) umbralSpinner.getValue();
                int capacidadMax = (int) capacidadSpinner.getValue();

                // Verificar que la capacidad no sea menor que el stock actual
                if (capacidadMax < recurso.getStockActual()) {
                    JOptionPane.showMessageDialog(dialog,
                            "La capacidad máxima no puede ser menor que el stock actual (" +
                                    recurso.getStockActual() + ")",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Actualizar datos del recurso
                recurso.setNombre(nombre);
                recurso.setCategoria(categoria);
                recurso.setUmbralAlerta(umbral);
                recurso.setCapacidadMaxima(capacidadMax);
                recurso.setUnidadMedida(unidad);

                // Guardar cambios usando el gestor
                gestorGeneral.getGestorRecursos().actualizarRecurso(recurso);

                JOptionPane.showMessageDialog(dialog,
                        "Recurso '" + nombre + "' actualizado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaRecursos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al actualizar recurso: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(PRIMARY_BLUE);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Editor para botones en la tabla de recursos
     */
    /**
     * Editor para botones en la tabla de recursos - VERSIÓN CORREGIDA
     */
    class RecursosButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton editarBtn;
        private JButton reponerBtn;
        private JButton eliminarBtn;
        private JButton asignarBtn;
        private JTable tabla;
        private int currentRow;

        public RecursosButtonEditor(JCheckBox checkBox, JTable tabla) {
            this.tabla = tabla;

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 5));
            panel.setOpaque(true);
            panel.setPreferredSize(new Dimension(200, 40));

            // Botón Editar
            editarBtn = new JButton("Editar");
            editarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editarBtn.setBackground(new Color(33, 150, 243));
            editarBtn.setForeground(Color.WHITE);
            editarBtn.setFocusPainted(false);
            editarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            editarBtn.setPreferredSize(new Dimension(55, 25));
            editarBtn.addActionListener(e -> {
                editarRecursoDesdeTabla();
                fireEditingStopped();
            });

            // Botón Reponer
            reponerBtn = new JButton("Reponer");
            reponerBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            reponerBtn.setBackground(new Color(255, 152, 0));
            reponerBtn.setForeground(Color.WHITE);
            reponerBtn.setFocusPainted(false);
            reponerBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            reponerBtn.setPreferredSize(new Dimension(65, 25));
            reponerBtn.addActionListener(e -> {
                // CORRECCIÓN: Usar 'RecursosButtonEditor.this' para referirse a la instancia actual
                reponerRecursoDesdeTabla();
                fireEditingStopped();
            });

            // Botón Eliminar
            eliminarBtn = new JButton("Eliminar");
            eliminarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            eliminarBtn.setBackground(new Color(244, 67, 54));
            eliminarBtn.setForeground(Color.WHITE);
            eliminarBtn.setFocusPainted(false);
            eliminarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            eliminarBtn.setPreferredSize(new Dimension(65, 25));
            eliminarBtn.addActionListener(e -> {
                eliminarRecursoDesdeTabla();
                fireEditingStopped();
            });

            // Botón Asignar (RF-06)
            asignarBtn = new JButton("Asignar");
            asignarBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            asignarBtn.setBackground(new Color(76, 175, 80));
            asignarBtn.setForeground(Color.WHITE);
            asignarBtn.setFocusPainted(false);
            asignarBtn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            asignarBtn.setPreferredSize(new Dimension(65, 25));
            asignarBtn.addActionListener(e -> {
                asignarRecursoAActividad();
                fireEditingStopped();
            });

            // Agregar botones al panel
            panel.add(editarBtn);
            panel.add(reponerBtn);
            panel.add(eliminarBtn);
            panel.add(asignarBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Editar|Reponer|Eliminar|Asignar";
        }

        private void editarRecursoDesdeTabla() {
            String id = (String) tabla.getValueAt(currentRow, 0);
            Recurso recurso = obtenerRecursoPorId(id);
            if (recurso != null) {
                // CORRECCIÓN: Usar la referencia correcta al Dashboard
                DashboardCoordUI.this.mostrarFormularioEditarRecurso(recurso);
            }
        }

        private void reponerRecursoDesdeTabla() {
            String id = (String) tabla.getValueAt(currentRow, 0);
            String nombre = (String) tabla.getValueAt(currentRow, 1);

            // CORRECCIÓN: Usar 'DashboardCoordUI.this' para referirse al JFrame padre
            String cantidadStr = JOptionPane.showInputDialog(DashboardCoordUI.this,
                    "Ingrese la cantidad a reponer para " + nombre + ":",
                    "Reponer Stock",
                    JOptionPane.QUESTION_MESSAGE);

            if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                try {
                    int cantidad = Integer.parseInt(cantidadStr);
                    if (cantidad <= 0) {
                        JOptionPane.showMessageDialog(DashboardCoordUI.this,
                                "La cantidad debe ser mayor que cero",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    gestorGeneral.getGestorRecursos().reponerStock(id, cantidad);
                    actualizarTablaRecursos();

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(DashboardCoordUI.this,
                            "Por favor ingrese un número válido",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DashboardCoordUI.this,
                            "Error al reponer stock: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void eliminarRecursoDesdeTabla() {
            String id = (String) tabla.getValueAt(currentRow, 0);
            String nombre = (String) tabla.getValueAt(currentRow, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    DashboardCoordUI.this,  // CORRECCIÓN: Usar la referencia correcta
                    "¿Está seguro que desea eliminar el recurso:\n" +
                            nombre + " (ID: " + id + ")?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                fireEditingStopped();
                eliminarRecurso(id, nombre);
            } else {
                fireEditingStopped();
            }
        }

        private void asignarRecursoAActividad() {
            String idRecurso = (String) tabla.getValueAt(currentRow, 0);
            String nombreRecurso = (String) tabla.getValueAt(currentRow, 1);

            // CORRECCIÓN: Usar la referencia correcta
            DashboardCoordUI.this.mostrarDialogoAsignarRecurso(idRecurso, nombreRecurso);
        }
    }

    /**
     * Obtiene un recurso por ID
     */
    private Recurso obtenerRecursoPorId(String id) {
        try {
            return gestorGeneral.getGestorRecursos().buscarRecursoPorId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void eliminarRecurso(String id, String nombre) {
        try {
            gestorGeneral.getGestorRecursos().eliminarRecurso(id);
            JOptionPane.showMessageDialog(DashboardCoordUI.this,
                    "Recurso '" + nombre + "' eliminado exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaRecursos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(DashboardCoordUI.this,
                    "Error al eliminar recurso: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra diálogo para asignar recurso a actividad (RF-06)
     */
    private void mostrarDialogoAsignarRecurso(String recursoId, String nombreRecurso) {
        JDialog dialog = new JDialog(this, "Asignar Recurso a Actividad (RF-06)", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Información del recurso
        JLabel recursoInfo = new JLabel("<html><b>Recurso:</b> " + nombreRecurso +
                " (ID: " + recursoId + ")</html>");
        recursoInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(recursoInfo, gbc);

        // Seleccionar actividad
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Actividad*:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> actividadCombo = new JComboBox<>();
        try {
            List<Actividad> actividades = gestorActividades.obtenerTodasActividades();
            for (Actividad actividad : actividades) {
                actividadCombo.addItem(actividad.getNombre() + " (ID: " + actividad.getId() + ")");
            }
        } catch (Exception e) {
            actividadCombo.addItem("No hay actividades disponibles");
        }
        formPanel.add(actividadCombo, gbc);

        // Cantidad a asignar
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Cantidad*:"), gbc);

        gbc.gridx = 1;
        JSpinner cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        formPanel.add(cantidadSpinner, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton asignarBtn = new JButton("Asignar Recurso");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        asignarBtn.addActionListener(e -> {
            try {
                String actividadSeleccionada = (String) actividadCombo.getSelectedItem();
                if (actividadSeleccionada == null || actividadSeleccionada.contains("No hay")) {
                    JOptionPane.showMessageDialog(dialog,
                            "No hay actividades disponibles",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Extraer ID de la actividad
                String actividadId = actividadSeleccionada.substring(
                        actividadSeleccionada.indexOf("(ID: ") + 5,
                        actividadSeleccionada.indexOf(")")
                );

                int cantidad = (int) cantidadSpinner.getValue();

                // Obtener la actividad
                Actividad actividad = gestorActividades.buscarActividadPorId(actividadId);
                if (actividad == null) {
                    throw new Exception("Actividad no encontrada");
                }

                // Asignar recurso usando el gestor
                gestorGeneral.getGestorRecursos().asignarRecursosAActividad(
                        actividad, recursoId, cantidad
                );

                JOptionPane.showMessageDialog(dialog,
                        "Recurso asignado exitosamente a la actividad",
                        "Éxito (RF-06)", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                actualizarTablaRecursos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al asignar recurso: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        asignarBtn.setBackground(PRIMARY_BLUE);
        asignarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(asignarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Obtiene IDs de voluntarios asignados a una actividad (usando la estructura real)
     */
    private List<String> obtenerVoluntariosAsignadosIds(String actividadId) {
        try {
            Actividad actividad = gestorActividades.buscarActividadPorId(actividadId);
            if (actividad != null && actividad.getVoluntariosAsignados() != null) {
                return new ArrayList<>(actividad.getVoluntariosAsignados().keySet());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Obtiene voluntarios asignados a una actividad (usando la estructura real)
     */
    private List<Voluntario> obtenerVoluntariosAsignados(String actividadId) {
        List<Voluntario> voluntarios = new ArrayList<>();
        try {
            List<String> voluntariosIds = obtenerVoluntariosAsignadosIds(actividadId);
            for (String volId : voluntariosIds) {
                Voluntario vol = obtenerVoluntarioPorId(volId);
                if (vol != null) {
                    voluntarios.add(vol);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voluntarios;
    }

    /**
     * Asigna voluntarios a una actividad (usando la estructura real)
     */
    private boolean asignarVoluntariosActividad(String actividadId, List<String> voluntariosIds) {
        try {
            Actividad actividad = gestorActividades.buscarActividadPorId(actividadId);
            if (actividad != null) {
                // Crear nuevo mapa con los voluntarios
                Map<String, String> nuevosVoluntarios = new HashMap<>();
                for (String volId : voluntariosIds) {
                    Voluntario vol = obtenerVoluntarioPorId(volId);
                    if (vol != null) {
                        nuevosVoluntarios.put(volId, vol.getNombre()); // Guardamos ID -> Nombre
                    }
                }
                actividad.setVoluntariosAsignados(nuevosVoluntarios);
                gestorActividades.actualizarActividad(actividad);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Crea el panel de reportes simplificado - Historial (RF-08) con detalles (RF-07)
     */
    private JPanel createReportesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_GRAY);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_GRAY);

        JLabel title = new JLabel("Historial de Actividades");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("RF-08: Consultar historial | RF-07: Ver resultados registrados");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Barra de herramientas con filtros
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Filtros
        toolbar.add(new JLabel("Filtrar por:"));

        JComboBox<String> filtroCombo = new JComboBox<>(new String[]{
                "Todas las actividades", "Solo completadas", "Solo pendientes",
                "Con resultados", "Sin resultados"
        });
        toolbar.add(filtroCombo);

        toolbar.add(Box.createHorizontalStrut(20));

        toolbar.add(new JLabel("Brigada:"));
        JComboBox<String> brigadaCombo = new JComboBox<>();
        brigadaCombo.addItem("Todas");
        try {
            List<Brigada> brigadas = gestorGeneral.getGestorBrigadas().obtenerTodasBrigadas();
            for (Brigada brigada : brigadas) {
                brigadaCombo.addItem(brigada.getNombre());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.add(brigadaCombo);

        toolbar.add(Box.createHorizontalStrut(20));

        JTextField buscarField = new JTextField(20);
        buscarField.setToolTipText("Buscar por nombre o lugar");
        toolbar.add(buscarField);

        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.addActionListener(e -> {
            filtrarHistorialSimplicado(
                    filtroCombo.getSelectedIndex(),
                    brigadaCombo.getSelectedIndex(),
                    buscarField.getText()
            );
        });
        toolbar.add(buscarBtn);

        JButton limpiarBtn = new JButton("Limpiar filtros");
        limpiarBtn.addActionListener(e -> {
            filtroCombo.setSelectedIndex(0);
            brigadaCombo.setSelectedIndex(0);
            buscarField.setText("");
            cargarHistorialCompleto();
        });
        toolbar.add(limpiarBtn);

        JButton actualizarBtn = new JButton("Actualizar");
        actualizarBtn.addActionListener(e -> cargarHistorialCompleto());
        toolbar.add(actualizarBtn);

        panel.add(toolbar, BorderLayout.NORTH);

        // Tabla de historial
        String[] columnNames = {"ID", "Nombre", "Fecha", "Lugar", "Brigada", "Estado", "Resultados", "Acción"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo la columna de acción es editable
            }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(40);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));

        // Configurar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);   // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);  // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(80);   // Fecha
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120);  // Lugar
        tabla.getColumnModel().getColumn(4).setPreferredWidth(120);  // Brigada
        tabla.getColumnModel().getColumn(5).setPreferredWidth(100);  // Estado
        tabla.getColumnModel().getColumn(6).setPreferredWidth(200);  // Resultados
        tabla.getColumnModel().getColumn(7).setPreferredWidth(100);  // Acción

        // Configurar renderizador y editor para botón
        tabla.getColumnModel().getColumn(7).setCellRenderer(new HistorialButtonRenderer());
        tabla.getColumnModel().getColumn(7).setCellEditor(new HistorialButtonEditor(new JCheckBox(), tabla));

        // Cargar historial completo inicialmente
        cargarHistorialCompletoEnTabla(model);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(1100, 450));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de información
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));

        // Botón para ver estadísticas rápidas
        JButton statsBtn = new JButton("Ver estadísticas");
        statsBtn.addActionListener(e -> mostrarEstadisticasRapidas());
        infoPanel.add(statsBtn, BorderLayout.EAST);

        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Carga el historial completo en la tabla
     */
    private void cargarHistorialCompletoEnTabla(DefaultTableModel model) {
        model.setRowCount(0); // Limpiar tabla

        try {
            List<Actividad> actividades = gestorActividades.obtenerTodasActividades();
            cargarActividadesEnTabla(model, actividades);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando historial: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Carga actividades específicas en la tabla
     */
    private void cargarActividadesEnTabla(DefaultTableModel model, List<Actividad> actividades) {
        model.setRowCount(0);

        if (actividades.isEmpty()) {
            model.addRow(new Object[]{"", "No se encontraron actividades", "", "", "", "", "", ""});
            return;
        }

        Date ahora = new Date();

        for (Actividad actividad : actividades) {
            String fechaStr = new java.text.SimpleDateFormat("dd/MM/yyyy").format(actividad.getFecha());
            String brigadaNombre = actividad.getBrigadaAsociada() != null ?
                    actividad.getBrigadaAsociada().getNombre() : "Sin asignar";

            // Estado
            String estado = actividad.getFecha().after(ahora) ? "Pendiente" : "Completada";

            // Resultados
            String resultadosInfo = "Sin resultados";
            if (actividad.getResultados() != null && !actividad.getResultados().isEmpty()) {
                resultadosInfo = "Resultados registrados";
            }

            model.addRow(new Object[]{
                    actividad.getId(),
                    actividad.getNombre(),
                    fechaStr,
                    actividad.getLugar(),
                    brigadaNombre,
                    estado,
                    resultadosInfo,
                    "Ver detalles"
            });
        }
    }

    /**
     * Método para cargar historial completo (llamado desde botón)
     */
    private void cargarHistorialCompleto() {
        // Encontrar la tabla en el panel de reportes
        Component[] components = reportesPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JScrollPane) {
                JViewport viewport = ((JScrollPane) comp).getViewport();
                if (viewport.getView() instanceof JTable) {
                    JTable tabla = (JTable) viewport.getView();
                    DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                    cargarHistorialCompletoEnTabla(model);
                    tabla.repaint();
                    return;
                }
            }
        }
    }

    /**
     * Filtra el historial de manera simplificada
     */
    private void filtrarHistorialSimplicado(int filtroIndex, int brigadaIndex, String busqueda) {
        try {
            List<Actividad> todasActividades = gestorActividades.obtenerTodasActividades();
            List<Actividad> actividadesFiltradas = new ArrayList<>();
            Date ahora = new Date();

            String busquedaLower = busqueda.toLowerCase();
            String brigadaNombre = "";

            // Obtener nombre de brigada si se seleccionó una específica
            if (brigadaIndex > 0) {
                try {
                    List<Brigada> brigadas = gestorGeneral.getGestorBrigadas().obtenerTodasBrigadas();
                    brigadaNombre = brigadas.get(brigadaIndex - 1).getNombre();
                } catch (Exception e) {
                    // Si hay error, ignorar filtro de brigada
                }
            }

            for (Actividad actividad : todasActividades) {
                boolean pasaFiltro = true;

                // Aplicar filtro de estado
                switch (filtroIndex) {
                    case 1: // Solo completadas
                        if (actividad.getFecha().after(ahora)) pasaFiltro = false;
                        break;
                    case 2: // Solo pendientes
                        if (!actividad.getFecha().after(ahora)) pasaFiltro = false;
                        break;
                    case 3: // Con resultados
                        if (actividad.getResultados() == null || actividad.getResultados().isEmpty())
                            pasaFiltro = false;
                        break;
                    case 4: // Sin resultados
                        if (actividad.getResultados() != null && !actividad.getResultados().isEmpty())
                            pasaFiltro = false;
                        break;
                }

                // Aplicar filtro de brigada
                if (pasaFiltro && brigadaIndex > 0) {
                    if (actividad.getBrigadaAsociada() == null ||
                            !actividad.getBrigadaAsociada().getNombre().equals(brigadaNombre)) {
                        pasaFiltro = false;
                    }
                }

                // Aplicar búsqueda
                if (pasaFiltro && !busqueda.trim().isEmpty()) {
                    boolean coincide =
                            actividad.getNombre().toLowerCase().contains(busquedaLower) ||
                                    actividad.getLugar().toLowerCase().contains(busquedaLower) ||
                                    (actividad.getBrigadaAsociada() != null &&
                                            actividad.getBrigadaAsociada().getNombre().toLowerCase().contains(busquedaLower));

                    if (!coincide) pasaFiltro = false;
                }

                if (pasaFiltro) {
                    actividadesFiltradas.add(actividad);
                }
            }

            // Actualizar tabla
            Component[] components = reportesPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    JViewport viewport = ((JScrollPane) comp).getViewport();
                    if (viewport.getView() instanceof JTable) {
                        JTable tabla = (JTable) viewport.getView();
                        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                        cargarActividadesEnTabla(model, actividadesFiltradas);
                        tabla.repaint();

                        // Mostrar mensaje
                        if (actividadesFiltradas.isEmpty()) {
                            JOptionPane.showMessageDialog(this,
                                    "No se encontraron actividades con los filtros aplicados",
                                    "Sin resultados",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        return;
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al filtrar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Renderizador para botón "Ver detalles"
     */
    class HistorialButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton detallesBtn;

        public HistorialButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            setOpaque(true);

            detallesBtn = new JButton("Ver detalles");
            detallesBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            detallesBtn.setBackground(new Color(33, 150, 243));
            detallesBtn.setForeground(Color.WHITE);
            detallesBtn.setFocusPainted(false);
            detallesBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            detallesBtn.setPreferredSize(new Dimension(85, 25));

            add(detallesBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * Editor para botón "Ver detalles"
     */
    class HistorialButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton detallesBtn;
        private JTable tabla;
        private int currentRow;

        public HistorialButtonEditor(JCheckBox checkBox, JTable tabla) {
            this.tabla = tabla;

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.setOpaque(true);

            detallesBtn = new JButton("Ver detalles");
            detallesBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            detallesBtn.setBackground(new Color(33, 150, 243));
            detallesBtn.setForeground(Color.WHITE);
            detallesBtn.setFocusPainted(false);
            detallesBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            detallesBtn.setPreferredSize(new Dimension(85, 25));
            detallesBtn.addActionListener(e -> {
                verDetallesActividad();
                fireEditingStopped();
            });

            panel.add(detallesBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Ver detalles";
        }

        private void verDetallesActividad() {
            String actividadId = (String) tabla.getValueAt(currentRow, 0);
            String actividadNombre = (String) tabla.getValueAt(currentRow, 1);

            if (actividadId == null || actividadId.isEmpty()) {
                return; // Fila vacía
            }

            mostrarDetallesActividadDialogo(actividadId, actividadNombre);
        }
    }

    /**
     * Muestra diálogo con detalles completos de una actividad (incluye resultados RF-07 y recursos)
     */
    private void mostrarDetallesActividadDialogo(String actividadId, String actividadNombre) {
        try {
            Actividad actividad = gestorActividades.buscarActividadPorId(actividadId);
            if (actividad == null) {
                JOptionPane.showMessageDialog(this,
                        "Actividad no encontrada: " + actividadId,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog dialog = new JDialog(this, "Detalles: " + actividadNombre, true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(800, 700);
            dialog.setLocationRelativeTo(this);

            JTabbedPane tabbedPane = new JTabbedPane();

            // Tab 1: Información general
            tabbedPane.addTab("📋 Información General", crearPanelInfoGeneral(actividad));

            // Tab 2: Resultados registrados (RF-07)
            tabbedPane.addTab("📊 Resultados (RF-07)", crearPanelResultadosRegistrados(actividad));

            // Tab 3: Voluntarios asignados
            tabbedPane.addTab("👥 Participantes", crearPanelVoluntariosAsignados(actividad));

            // Tab 4: Recursos asignados (SOLO VISUALIZACIÓN)
            tabbedPane.addTab("📦 Recursos", crearPanelRecursosAsignados(actividad));

            dialog.add(tabbedPane, BorderLayout.CENTER);

            // Panel de botones (mantén solo lo que tenías antes)
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton cerrarBtn = new JButton("Cerrar");
            cerrarBtn.addActionListener(e -> dialog.dispose());

            buttonPanel.add(cerrarBtn);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al mostrar detalles: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Crea panel con información general de la actividad
     */
    private JPanel crearPanelInfoGeneral(Actividad actividad) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        StringBuilder info = new StringBuilder();
        info.append("INFORMACIÓN GENERAL\n");
        info.append("=".repeat(50)).append("\n\n");

        info.append("ID: ").append(actividad.getId()).append("\n");
        info.append("Nombre: ").append(actividad.getNombre()).append("\n");
        info.append("Fecha: ").append(
                new java.text.SimpleDateFormat("dd/MM/yyyy").format(actividad.getFecha())).append("\n");
        info.append("Lugar: ").append(actividad.getLugar()).append("\n\n");

        info.append("OBJETIVO:\n");
        info.append("-".repeat(30)).append("\n");
        info.append(actividad.getObjetivo()).append("\n\n");

        info.append("BRIGADA RESPONSABLE:\n");
        info.append("-".repeat(30)).append("\n");
        if (actividad.getBrigadaAsociada() != null) {
            Brigada brigada = actividad.getBrigadaAsociada();
            info.append("• Nombre: ").append(brigada.getNombre()).append("\n");
            info.append("• ID: ").append(brigada.getId()).append("\n");
            info.append("• Tipo: ").append(brigada.getTipo()).append("\n");
            info.append("• Zona: ").append(brigada.getZona()).append("\n");
            info.append("• Estado: ").append(brigada.getEstado()).append("\n");
        } else {
            info.append("No asignada\n");
        }
        info.append("\n");

        info.append("ESTADO:\n");
        info.append("-".repeat(30)).append("\n");
        Date ahora = new Date();
        if (actividad.getFecha().after(ahora)) {
            info.append("• Estado: Pendiente\n");
            info.append("• Faltan ").append(
                    (actividad.getFecha().getTime() - ahora.getTime()) / (1000 * 60 * 60 * 24)).append(" días\n");
        } else {
            info.append("• Estado: Completada\n");
            long diasTranscurridos = (ahora.getTime() - actividad.getFecha().getTime()) / (1000 * 60 * 60 * 24);
            info.append("• Completada hace ").append(diasTranscurridos).append(" días\n");
        }

        JTextArea textArea = new JTextArea(info.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea panel con resultados registrados (RF-07)
     */
    private JPanel crearPanelResultadosRegistrados(Actividad actividad) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        if (actividad.getResultados() == null || actividad.getResultados().isEmpty()) {
            JLabel noResultados = new JLabel("<html><center><h3>No hay resultados registrados</h3>" +
                    "<p>Esta actividad aún no tiene resultados documentados.</p>" +
                    "<p>Para registrar resultados, diríjase a la sección de actividades.</p></center></html>");
            noResultados.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noResultados, BorderLayout.CENTER);

            return panel;
        }

        // Si hay resultados, mostrarlos formateados
        JTextArea textArea = new JTextArea(actividad.getResultados());
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Agregar título
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel titulo = new JLabel("RESULTADOS REGISTRADOS (RF-07)");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setForeground(new Color(0, 100, 0));
        headerPanel.add(titulo, BorderLayout.WEST);

        JLabel fechaLabel = new JLabel("Registrado el: " +
                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        fechaLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        fechaLabel.setForeground(Color.GRAY);
        headerPanel.add(fechaLabel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea panel con voluntarios asignados - VERSIÓN CORREGIDA
     */
    private JPanel crearPanelVoluntariosAsignados(Actividad actividad) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        StringBuilder info = new StringBuilder();
        info.append("VOLUNTARIOS ASIGNADOS A LA ACTIVIDAD\n");
        info.append("=".repeat(50)).append("\n\n");

        try {
            // Obtener voluntarios directamente asignados a la actividad
            List<Voluntario> voluntariosDirectos = obtenerVoluntariosAsignadosAActividad(actividad.getId());

            // Obtener voluntarios a través de la brigada (si existe)
            List<Voluntario> voluntariosPorBrigada = new ArrayList<>();
            if (actividad.getBrigadaAsociada() != null) {
                voluntariosPorBrigada = gestorGeneral.getGestorBrigadas()
                        .obtenerVoluntariosDeBrigada(actividad.getBrigadaAsociada().getId());
            }

            // Voluntarios únicos (evitar duplicados)
            Set<String> voluntariosUnicos = new HashSet<>();
            List<Voluntario> todosVoluntarios = new ArrayList<>();

            // Agregar voluntarios directos primero
            for (Voluntario vol : voluntariosDirectos) {
                if (voluntariosUnicos.add(vol.getId())) {
                    todosVoluntarios.add(vol);
                    info.append("• ").append(vol.getNombre())
                            .append(" (Asignación directa)\n")
                            .append("  Email: ").append(vol.getEmail()).append("\n");

                    if (vol.getHabilidades() != null && !vol.getHabilidades().isEmpty()) {
                        info.append("  Habilidades: ").append(String.join(", ", vol.getHabilidades())).append("\n");
                    }
                    info.append("\n");
                }
            }

            // Agregar voluntarios de brigada (solo si no están ya en la lista)
            for (Voluntario vol : voluntariosPorBrigada) {
                if (voluntariosUnicos.add(vol.getId())) {
                    todosVoluntarios.add(vol);
                    info.append("• ").append(vol.getNombre())
                            .append(" (Por brigada: ").append(actividad.getBrigadaAsociada().getNombre()).append(")\n")
                            .append("  Email: ").append(vol.getEmail()).append("\n");

                    if (vol.getHabilidades() != null && !vol.getHabilidades().isEmpty()) {
                        info.append("  Habilidades: ").append(String.join(", ", vol.getHabilidades())).append("\n");
                    }
                    info.append("\n");
                }
            }

            if (todosVoluntarios.isEmpty()) {
                info.append("No hay voluntarios asignados a esta actividad.\n\n");
            } else {
                info.append("\nRESUMEN:\n");
                info.append("-".repeat(30)).append("\n");
                info.append("• Total de voluntarios: ").append(todosVoluntarios.size()).append("\n");
                info.append("• Por asignación directa: ").append(voluntariosDirectos.size()).append("\n");
                if (actividad.getBrigadaAsociada() != null) {
                    info.append("• Por brigada '").append(actividad.getBrigadaAsociada().getNombre())
                            .append("': ").append(voluntariosPorBrigada.size()).append("\n");
                }
            }

        } catch (Exception e) {
            info.append("Error al cargar voluntarios: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }

        textArea.setText(info.toString());

        // Agregar título
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel titulo = new JLabel("PARTICIPANTES DE LA ACTIVIDAD");
        titulo.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(titulo, BorderLayout.WEST);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTablaVoluntarios(String titulo, List<Voluntario> voluntarios) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (voluntarios.isEmpty()) {
            JLabel vacio = new JLabel("<html><center><h3>No hay voluntarios</h3></center></html>");
            vacio.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(vacio, BorderLayout.CENTER);
            return panel;
        }

        // Título
        JLabel lblTitulo = new JLabel("<html><h3>" + titulo + " (" + voluntarios.size() + ")</h3></html>");
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"Nombre", "Email", "Teléfono", "Habilidades"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Voluntario voluntario : voluntarios) {
            String habilidades = voluntario.getHabilidades() != null ?
                    String.join(", ", voluntario.getHabilidades()) : "";

            model.addRow(new Object[]{
                    voluntario.getNombre(),
                    voluntario.getEmail(),
                    voluntario.getTelefono(),
                    habilidades
            });
        }

        JTable tabla = new JTable(model);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tabla.setFont(new Font("Arial", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelError(Exception e) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel error = new JLabel("<html><center><h3>Error</h3><p>" + e.getMessage() + "</p></center></html>");
        error.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(error, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Muestra estadísticas rápidas
     */
    private void mostrarEstadisticasRapidas() {
        try {
            List<Actividad> actividades = gestorActividades.obtenerTodasActividades();
            Date ahora = new Date();

            int totalActividades = actividades.size();
            int actividadesPendientes = 0;
            int actividadesCompletadas = 0;
            int actividadesConResultados = 0;
            int actividadesSinResultados = 0;

            for (Actividad actividad : actividades) {
                if (actividad.getFecha().after(ahora)) {
                    actividadesPendientes++;
                } else {
                    actividadesCompletadas++;
                }

                if (actividad.getResultados() != null && !actividad.getResultados().isEmpty()) {
                    actividadesConResultados++;
                } else {
                    actividadesSinResultados++;
                }
            }

            String estadisticas = String.format("""
            ========== ESTADÍSTICAS RÁPIDAS ==========
            
            ACTIVIDADES:
            • Total: %d
            • Pendientes: %d (%.1f%%)
            • Completadas: %d (%.1f%%)
            
            RESULTADOS REGISTRADOS:
            • Con resultados: %d (%.1f%%)
            • Sin resultados: %d (%.1f%%)
            
            ==========================================
            """,
                    totalActividades,
                    actividadesPendientes,
                    totalActividades > 0 ? (actividadesPendientes * 100.0 / totalActividades) : 0,
                    actividadesCompletadas,
                    totalActividades > 0 ? (actividadesCompletadas * 100.0 / totalActividades) : 0,
                    actividadesConResultados,
                    actividadesCompletadas > 0 ? (actividadesConResultados * 100.0 / actividadesCompletadas) : 0,
                    actividadesSinResultados,
                    actividadesCompletadas > 0 ? (actividadesSinResultados * 100.0 / actividadesCompletadas) : 0
            );

            JTextArea textArea = new JTextArea(estadisticas);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 250));

            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Estadísticas de Actividades",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al calcular estadísticas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JPanel crearPanelRecursosAsignados(Actividad actividad) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        try {
            // Obtener el mapa de recursos asignados de la actividad
            Map<String, Integer> recursosAsignados = actividad.getRecursosAsignados();

            if (recursosAsignados == null || recursosAsignados.isEmpty()) {
                JLabel vacio = new JLabel("<html><center><h3>No hay recursos asignados</h3>" +
                        "<p>Esta actividad no tiene recursos asignados aún.</p></center></html>");
                vacio.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(vacio, BorderLayout.CENTER);
                return panel;
            }

            // Título
            JLabel lblTitulo = new JLabel("<html><h3>Recursos Asignados (" + recursosAsignados.size() + ")</h3></html>");
            lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
            panel.add(lblTitulo, BorderLayout.NORTH);

            // Tabla simple con 3 columnas
            String[] columnNames = {"Nombre", "Cantidad", "Categoría"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            int totalUnidades = 0;

            // Para cada recurso en el mapa, obtener información básica
            for (Map.Entry<String, Integer> entry : recursosAsignados.entrySet()) {
                String recursoId = entry.getKey();
                int cantidad = entry.getValue();

                try {
                    // Buscar información del recurso
                    Recurso recurso = gestorGeneral.getGestorRecursos().buscarRecursoPorId(recursoId);

                    if (recurso != null) {
                        model.addRow(new Object[]{
                                recurso.getNombre(),
                                cantidad,
                                recurso.getCategoria()
                        });
                    } else {
                        // Si no se encuentra el recurso
                        model.addRow(new Object[]{
                                "[ID: " + recursoId + "]",
                                cantidad,
                                "Desconocida"
                        });
                    }
                    totalUnidades += cantidad;

                } catch (Exception e) {
                    // En caso de error
                    model.addRow(new Object[]{
                            "[Error: " + recursoId + "]",
                            cantidad,
                            "Error"
                    });
                    totalUnidades += cantidad;
                }
            }

            JTable tabla = new JTable(model);
            tabla.setRowHeight(30);
            tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
            tabla.setFont(new Font("Arial", Font.PLAIN, 11));

            // Configurar anchos de columnas
            tabla.getColumnModel().getColumn(0).setPreferredWidth(200); // Nombre
            tabla.getColumnModel().getColumn(1).setPreferredWidth(80);  // Cantidad
            tabla.getColumnModel().getColumn(2).setPreferredWidth(120); // Categoría

            JScrollPane scrollPane = new JScrollPane(tabla);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Panel inferior con resumen simple
            JPanel resumenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            resumenPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    new EmptyBorder(5, 10, 5, 10)
            ));
            resumenPanel.setBackground(new Color(240, 240, 240));

            JLabel resumenLabel = new JLabel(String.format(
                    "Total: %d recursos diferentes | %d unidades",
                    recursosAsignados.size(),
                    totalUnidades
            ));
            resumenPanel.add(resumenLabel);

            panel.add(resumenPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            JLabel error = new JLabel("<html><center><h3>Error al cargar recursos</h3></center></html>");
            error.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(error, BorderLayout.CENTER);
        }

        return panel;
    }

    private void mostrarFormularioNuevaBrigada() {
        JDialog dialog = new JDialog(this, "Registrar Nueva Brigada", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        JTextField idField = new JTextField(20);
        JTextField nombreField = new JTextField(20);

        // Combo box para tipo
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{
                "Alimentos", "Medio Ambiente", "Salud", "Educación", "Construcción", "Otros"
        });

        // Combo box para zona
        JComboBox<String> zonaCombo = new JComboBox<>(new String[]{
                "Zona Norte", "Centro", "Zona Sur", "Zona Este", "Zona Oeste", "Todas las zonas"
        });

        // Combo box para estado
        JComboBox<String> estadoCombo = new JComboBox<>(new String[]{
                "En planificación", "Activa", "Inactiva"
        });

        JTextArea descripcionArea = new JTextArea(4, 20);
        descripcionArea.setLineWrap(true);

        // Configurar GridBagConstraints
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("ID de Brigada*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Nombre*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tipoCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Zona:"), gbc);
        gbc.gridx = 1;
        formPanel.add(zonaCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        formPanel.add(estadoCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(descripcionArea), gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelarBtn = new JButton("Cancelar");
        JButton guardarBtn = new JButton("Guardar Brigada");

        cancelarBtn.addActionListener(e -> dialog.dispose());

        guardarBtn.addActionListener(e -> {
            // Validar campos
            String id = idField.getText().trim();
            String nombre = nombreField.getText().trim();

            if (id.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "ID y Nombre son campos requeridos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Crear objeto Brigada
                Brigada nuevaBrigada = new Brigada(
                        id,
                        nombre,
                        (String) tipoCombo.getSelectedItem(),
                        (String) zonaCombo.getSelectedItem(),
                        descripcionArea.getText().trim(),
                        coordinador
                );

                // Establecer estado
                String estadoSeleccionado = (String) estadoCombo.getSelectedItem();
                nuevaBrigada.setEstado(estadoSeleccionado);

                // Guardar usando el gestor
                gestorGeneral.getGestorBrigadas().crearBrigada(nuevaBrigada);

                JOptionPane.showMessageDialog(dialog,
                        "Brigada '" + nombre + "' creada exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();

                actualizarTablaBrigadas();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error al crear brigada: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        guardarBtn.setBackground(PRIMARY_BLUE);
        guardarBtn.setForeground(Color.WHITE);

        buttonPanel.add(cancelarBtn);
        buttonPanel.add(guardarBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void planificarNuevaActividad() {
        // Cambiar al panel de actividades y abrir el formulario
        cambiarPanel("actividades");
        // Dar un pequeño delay para que se cargue el panel
        SwingUtilities.invokeLater(() -> {
            mostrarFormularioNuevaActividad();
        });
    }

    private void consultarRecursos() {
        cambiarPanel("recursos");
    }
}