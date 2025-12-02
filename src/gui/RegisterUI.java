package gui;

import exceptions.PersistenciaException;
import gestores.GestorGeneral;
import gestores.GestorUsuarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Pattern;

public class RegisterUI extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> userTypeCombo;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextArea skillsArea;
    private JCheckBox termsCheckbox;

    public RegisterUI() {
        setTitle("Registro de Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);

        // Fondo blanco total con GridBagLayout para centrar el cardPanel
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(Color.WHITE);
        setContentPane(backgroundPanel);

        // Tarjeta central con scroll
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        cardPanel.setPreferredSize(new Dimension(420, 650));

        // Título centrado
        JLabel titleLabel = new JLabel("Crear Cuenta");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 118, 210));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Completa el formulario para registrarte");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createVerticalStrut(20));

        // Nombre Completo
        cardPanel.add(createLabel("Nombre Completo *"));
        nameField = new JTextField();
        nameField.setForeground(Color.GRAY);
        nameField.setPreferredSize(new Dimension(300, 45));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        addPlaceholderFocusListener(nameField, "Ingresa tu nombre completo");
        cardPanel.add(nameField);
        cardPanel.add(Box.createVerticalStrut(15));

        // Correo Electrónico
        cardPanel.add(createLabel("Correo Electrónico *"));
        emailField = new JTextField();
        emailField.setForeground(Color.GRAY);
        emailField.setPreferredSize(new Dimension(300, 45));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        addPlaceholderFocusListener(emailField, "correo@ejemplo.com");
        cardPanel.add(emailField);
        cardPanel.add(Box.createVerticalStrut(15));

        // Teléfono
        cardPanel.add(createLabel("Teléfono *"));
        phoneField = new JTextField();
        phoneField.setForeground(Color.GRAY);
        phoneField.setPreferredSize(new Dimension(300, 45));
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        addPlaceholderFocusListener(phoneField, "88888888");
        cardPanel.add(phoneField);
        cardPanel.add(Box.createVerticalStrut(15));

        // Tipo de Usuario
        cardPanel.add(createLabel("Tipo de Usuario *"));
        String[] userTypes = {"Voluntario", "Coordinador"};
        userTypeCombo = new JComboBox<>(userTypes);
        userTypeCombo.setPreferredSize(new Dimension(300, 45));
        userTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cardPanel.add(userTypeCombo);
        cardPanel.add(Box.createVerticalStrut(15));

        // Contraseña
        cardPanel.add(createLabel("Contraseña *"));
        passwordField = new JPasswordField();
        passwordField.setForeground(Color.GRAY);
        passwordField.setPreferredSize(new Dimension(300, 45));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        addPasswordPlaceholderFocusListener(passwordField, "••••••••••");
        cardPanel.add(passwordField);
        cardPanel.add(Box.createVerticalStrut(15));

        // Confirmar Contraseña
        cardPanel.add(createLabel("Confirmar Contraseña *"));
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setForeground(Color.GRAY);
        confirmPasswordField.setPreferredSize(new Dimension(300, 45));
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        addPasswordPlaceholderFocusListener(confirmPasswordField, "••••••••••");
        cardPanel.add(confirmPasswordField);
        cardPanel.add(Box.createVerticalStrut(20));

        // Habilidades (solo para voluntarios)
        cardPanel.add(createLabel("Habilidades (Opcional)"));
        skillsArea = new JTextArea(3, 20);
        skillsArea.setLineWrap(true);
        skillsArea.setWrapStyleWord(true);
        skillsArea.setForeground(Color.GRAY);
        skillsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        addTextAreaPlaceholderFocusListener(skillsArea, "Describe tus habilidades relevantes...");

        JScrollPane skillsScroll = new JScrollPane(skillsArea);
        skillsScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        cardPanel.add(skillsScroll);
        cardPanel.add(Box.createVerticalStrut(20));

        // Términos y condiciones
        JPanel termsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        termsPanel.setOpaque(false);
        termsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        termsCheckbox = new JCheckBox();
        JLabel termsLabel = new JLabel("Acepto los ");
        JLabel termsLink = createLink("términos y condiciones");

        termsPanel.add(termsCheckbox);
        termsPanel.add(termsLabel);
        termsPanel.add(termsLink);

        cardPanel.add(termsPanel);
        cardPanel.add(Box.createVerticalStrut(20));

        // Botón Registrar
        JButton registerButton = new JButton("Registrarse");
        registerButton.setBackground(new Color(25, 118, 210));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setPreferredSize(new Dimension(300, 45));
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cardPanel.add(registerButton);
        cardPanel.add(Box.createVerticalStrut(15));

        // Enlace para volver al login
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setOpaque(false);
        JLabel haveAccountLabel = new JLabel("¿Ya tienes cuenta? ");
        JLabel loginLink = createLink("Inicia sesión aquí");

        loginPanel.add(haveAccountLabel);
        loginPanel.add(loginLink);
        cardPanel.add(loginPanel);

        // Configurar scroll pane
        scrollPane.setViewportView(cardPanel);
        scrollPane.getViewport().setBackground(Color.WHITE);
        backgroundPanel.add(scrollPane);

// Luego en el action listener del botón de registro:
        registerButton.addActionListener(e -> {
            if (!validateForm()) {
                return;
            }

            try {
                String nombre = getFieldValue(nameField);
                String email = getFieldValue(emailField);
                String telefono = getFieldValue(phoneField);
                String tipoUsuario = (String) userTypeCombo.getSelectedItem();
                String password = new String(passwordField.getPassword());
                String habilidades = getTextAreaValue(skillsArea);

                // Usar el GestorGeneral en lugar de GestorUsuarios directamente
                GestorGeneral gestorGeneral = new GestorGeneral();

                if (tipoUsuario.equals("Voluntario")) {
                    String diasDisponibles = "Lunes a Viernes";
                    gestorGeneral.registrarVoluntario(nombre, telefono, email,
                            password, habilidades, diasDisponibles);
                } else { // Coordinador
                    String areaResponsabilidad = "General";
                    gestorGeneral.registrarCoordinador(nombre, telefono, email,
                            password, areaResponsabilidad);
                }

                JOptionPane.showMessageDialog(this,
                        "¡Registro exitoso! Bienvenido a la plataforma.",
                        "Registro Completado", JOptionPane.INFORMATION_MESSAGE);

                new LoginUI();
                dispose();

            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al registrar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error inesperado: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Mouse listener para el enlace de login
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new LoginUI();
                dispose();
            }
        });

        // Mouse listener para el enlace de términos
        termsLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(RegisterUI.this,
                        "Términos y condiciones de la plataforma...\n\n" +
                                "1. Uso responsable de la plataforma\n" +
                                "2. Protección de datos personales\n" +
                                "3. Conducta apropiada en actividades\n" +
                                "4. Cumplimiento de horarios asignados\n" +
                                "5. Respeto a la comunidad beneficiada",
                        "Términos y Condiciones", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        setVisible(true);
    }

    // Action Listeners
    private String getFieldValue(JTextField field) {
        String value = field.getText().trim();
        // Verificar si es placeholder
        if (field.getForeground().equals(Color.GRAY)) {
            return ""; // Tratar como vacío si es placeholder
        }
        return value;
    }

    private String getTextAreaValue(JTextArea textArea) {
        String value = textArea.getText().trim();
        // Verificar si es placeholder
        if (textArea.getForeground().equals(Color.GRAY)) {
            return ""; // Tratar como vacío si es placeholder
        }
        return value;
    }

    private boolean validateForm() {
        // Validar nombre
        String nombre = getFieldValue(nameField);
        if (nombre.isEmpty() || nombre.equals("Ingresa tu nombre completo")) {
            showError("El nombre completo es obligatorio");
            nameField.requestFocus();
            return false;
        }

        if (nombre.length() < 3) {
            showError("El nombre debe tener al menos 3 caracteres");
            nameField.requestFocus();
            return false;
        }

        // Validar email
        String email = getFieldValue(emailField);
        if (email.isEmpty() || email.equals("correo@ejemplo.com")) {
            showError("El correo electrónico es obligatorio");
            emailField.requestFocus();
            return false;
        }

        if (!isValidEmail(email)) {
            showError("Ingresa un correo electrónico válido (ejemplo: usuario@dominio.com)");
            emailField.requestFocus();
            return false;
        }

        // Validar teléfono
        String telefono = getFieldValue(phoneField);
        if (telefono.isEmpty() || telefono.equals("8888-8888")) {
            showError("El teléfono es obligatorio");
            phoneField.requestFocus();
            return false;
        }

        if (!telefono.matches("\\d{4}\\d{4}")) {
            showError("El teléfono debe tener el formato 88888888");
            phoneField.requestFocus();
            return false;
        }

        // Validar contraseña
        String password = new String(passwordField.getPassword());
        if (password.isEmpty() || password.equals("••••••••••")) {
            showError("La contraseña es obligatoria");
            passwordField.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            showError("La contraseña debe tener al menos 8 caracteres");
            passwordField.requestFocus();
            return false;
        }

        // Validar confirmación de contraseña
        String confirmPassword = new String(confirmPasswordField.getPassword());
        if (confirmPassword.isEmpty() || confirmPassword.equals("••••••••••")) {
            showError("Debes confirmar tu contraseña");
            confirmPasswordField.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden");
            confirmPasswordField.requestFocus();
            return false;
        }

        // Validar términos y condiciones
        if (!termsCheckbox.isSelected()) {
            showError("Debes aceptar los términos y condiciones");
            termsCheckbox.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }

    private void addPlaceholderFocusListener(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void addPasswordPlaceholderFocusListener(JPasswordField field, String placeholder) {
        field.setText(placeholder);
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void addTextAreaPlaceholderFocusListener(JTextArea textArea, String placeholder) {
        textArea.setText(placeholder);
        textArea.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText(placeholder);
                    textArea.setForeground(Color.GRAY);
                }
            }
        });
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createLink(String text) {
        JLabel link = new JLabel("<html><u>" + text + "</u></html>");
        link.setForeground(new Color(25, 118, 210));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return link;
    }
}