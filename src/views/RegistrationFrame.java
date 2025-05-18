package views;

import controllers.UserController;
import javax.swing.*;
import java.awt.*;

public class RegistrationFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField nameField;
    private JTextField contactField;
    private JTextField barangayField;
    private UserController userController;

    public RegistrationFrame() {
        userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Register New Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title label
        JLabel titleLabel = new JLabel("New Resident Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        addFormField(formPanel, gbc, "Username:", usernameField = new JTextField(20), 0);

        // Password
        addFormField(formPanel, gbc, "Password:", passwordField = new JPasswordField(20), 1);

        // Confirm Password
        addFormField(formPanel, gbc, "Confirm Password:", confirmPasswordField = new JPasswordField(20), 2);

        // Full Name
        addFormField(formPanel, gbc, "Full Name:", nameField = new JTextField(20), 3);

        // Contact Number
        addFormField(formPanel, gbc, "Contact Number:", contactField = new JTextField(20), 4);

        // Barangay
        addFormField(formPanel, gbc, "Barangay:", barangayField = new JTextField(20), 5);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegistration());
        
        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> backToLogin());

        buttonsPanel.add(registerButton);
        buttonsPanel.add(backButton);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField field, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.weightx = 0.0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String name = nameField.getText();
        String contact = contactField.getText();
        String barangay = barangayField.getText();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || 
            contact.isEmpty() || barangay.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean success = userController.register(username, password, name, contact, barangay);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Registration successful! Please login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                backToLogin();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Username already exists",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error during registration: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToLogin() {
        new LoginFrame().setVisible(true);
        this.dispose();
    }
} 