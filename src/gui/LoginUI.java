package gui;

import gestores.GestorGeneral;
import model.Coordinador;
import model.Usuario;
import model.Voluntario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        // Fondo blanco total con GridBagLayout para centrar el cardPanel
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(Color.WHITE);
        setContentPane(backgroundPanel);

        // Tarjeta central
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        cardPanel.setPreferredSize(new Dimension(420, 480));

        // Título centrado
        JLabel titleLabel = new JLabel("Iniciar Sesión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 118, 210));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Ingresa tus credenciales para acceder");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createVerticalStrut(20));

        // Tipo de Usuario
        cardPanel.add(createLabel("Tipo de Usuario"));
        String[] userTypes = {"Voluntario", "Coordinador"};
        JComboBox<String> userTypeCombo = new JComboBox<>(userTypes);
        userTypeCombo.setPreferredSize(new Dimension(300, 45)); // más alto
        userTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cardPanel.add(userTypeCombo);
        cardPanel.add(Box.createVerticalStrut(15));

        // Correo Electrónico con placeholder
        cardPanel.add(createLabel("Correo Electrónico"));
        JTextField emailField = new JTextField("correo@ejemplo.com");
        emailField.setForeground(Color.GRAY);
        emailField.setPreferredSize(new Dimension(300, 45)); // más alto
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("correo@ejemplo.com")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("correo@ejemplo.com");
                    emailField.setForeground(Color.GRAY);
                }
            }
        });
        cardPanel.add(emailField);
        cardPanel.add(Box.createVerticalStrut(25));

        // Contraseña con placeholder
        cardPanel.add(createLabel("Contraseña"));
        JPasswordField passwordField = new JPasswordField("••••••••••");
        passwordField.setForeground(Color.GRAY);
        passwordField.setPreferredSize(new Dimension(300, 45)); // más alto
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals("••••••••••")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("••••••••••");
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });
        cardPanel.add(passwordField);
        cardPanel.add(Box.createVerticalStrut(25));

        // Botón Iniciar Sesión
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setBackground(new Color(25, 118, 210));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(300, 45)); // más alto
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // En el action listener del botón de login:
        loginButton.addActionListener(e -> {
            String email = getFieldValue(emailField);
            String password = new String(passwordField.getPassword());
            String tipoSeleccionado = (String) userTypeCombo.getSelectedItem();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                GestorGeneral gestorGeneral = new GestorGeneral();
                Usuario usuario = gestorGeneral.autenticarUsuario(email, password);

                if (usuario != null) {
                    if (!usuario.getRol().equals(tipoSeleccionado)) {
                        JOptionPane.showMessageDialog(this,
                                "Tipo de usuario incorrecto. Selecciona " + usuario.getRol(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    JOptionPane.showMessageDialog(this,
                            "¡Bienvenido " + usuario.getNombre() + "!",
                            "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);

                    // Abrir dashboard con gestorGeneral para acceso a todos los gestores
                    if (usuario instanceof Coordinador) {
                        new DashboardCoordUI((Coordinador) usuario, gestorGeneral);
                    } else if (usuario instanceof Voluntario) {
                        new DashboardVoluntarioUI((Voluntario) usuario, gestorGeneral);
                    }
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Email o contraseña incorrectos",
                            "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al autenticar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cardPanel.add(loginButton);
        cardPanel.add(Box.createVerticalStrut(30));

        // Enlace de registro
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setOpaque(false);
        JLabel noAccountLabel = new JLabel("¿No tienes cuenta? ");
        registerPanel.add(noAccountLabel);
        JLabel registerLink = createLink("Regístrate aquí");
        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new RegisterUI();
                dispose();
            }
        });
        registerPanel.add(registerLink);
        cardPanel.add(registerPanel);

        // Agregar tarjeta al fondo
        backgroundPanel.add(cardPanel);

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // Agrega estos métodos en la clase LoginUI (después del constructor o antes del main)

    private String getFieldValue(JTextField field) {
        String value = field.getText().trim();
        // Verificar si es placeholder (texto gris)
        if (field.getForeground().equals(Color.GRAY)) {
            return ""; // Tratar como vacío si es placeholder
        }
        return value;
    }

    private String getFieldValue(JPasswordField field) {
        String value = new String(field.getPassword()).trim();
        // Verificar si es placeholder (texto gris)
        if (field.getForeground().equals(Color.GRAY)) {
            return ""; // Tratar como vacío si es placeholder
        }
        return value;
    }

    private JLabel createLink(String text) {
        JLabel link = new JLabel("<html><u>" + text + "</u></html>");
        link.setForeground(new Color(25, 118, 210));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return link;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
