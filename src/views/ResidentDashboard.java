package views;

import models.User;
import javax.swing.*;
import java.awt.*;

public class ResidentDashboard extends JFrame {
    private User resident;
    private JTabbedPane tabbedPane;

    public ResidentDashboard(User resident) {
        this.resident = resident;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Resident Dashboard - Community Disaster Preparedness System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + resident.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Add tabs
        tabbedPane.addTab("View Alerts", new ResidentAlertPanel());
        tabbedPane.addTab("Emergency Contacts", new ResidentContactPanel());
        tabbedPane.addTab("Emergency Resources", new ResidentResourcePanel());
        tabbedPane.addTab("Submit Report", new ResidentReportPanel(resident));
        tabbedPane.addTab("My Reports", new ResidentReportHistoryPanel(resident));

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel statusLabel = new JLabel("System Status: Online");
        statusBar.add(statusLabel);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
} 