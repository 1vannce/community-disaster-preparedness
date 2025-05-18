package views;

import models.User;
import controllers.UserController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserController userController;

    public LoginFrame() {
        userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Community Disaster Preparedness System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title label
        JLabel titleLabel = new JLabel("Login to System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> openRegistration());

        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = userController.login(username, password);
            if (user != null) {
                if (user.isAdmin()) {
                    new AdminDashboard(user).setVisible(true);
                } else {
                    new ResidentDashboard(user).setVisible(true);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error during login: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistration() {
        new RegistrationFrame().setVisible(true);
        this.dispose();
    }
} 