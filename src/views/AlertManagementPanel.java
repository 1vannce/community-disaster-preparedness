package views;

import models.Alert;
import controllers.AlertController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class AlertManagementPanel extends JPanel {
    private JTable alertTable;
    private DefaultTableModel tableModel;
    private AlertController alertController;
    private JComboBox<String> alertTypeCombo;
    private JComboBox<String> severityCombo;
    private JTextArea descriptionArea;

    public AlertManagementPanel() {
        alertController = new AlertController();
        initializeUI();
        loadAlerts();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Create New Alert"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Alert Type
        String[] alertTypes = {"Typhoon", "Flood", "Earthquake", "Fire", "Landslide"};
        alertTypeCombo = new JComboBox<>(alertTypes);
        addFormField(formPanel, gbc, "Alert Type:", alertTypeCombo, 0);

        // Severity
        String[] severityLevels = {"Low", "Medium", "High", "Critical"};
        severityCombo = new JComboBox<>(severityLevels);
        addFormField(formPanel, gbc, "Severity:", severityCombo, 1);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);

        // Create button
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton createButton = new JButton("Create Alert");
        createButton.addActionListener(e -> createAlert());
        formPanel.add(createButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Create table
        String[] columns = {"ID", "Type", "Severity", "Description", "Timestamp"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        alertTable = new JTable(tableModel);
        alertTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        JScrollPane tableScrollPane = new JScrollPane(alertTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedAlert());
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.weightx = 0.0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void createAlert() {
        String type = (String) alertTypeCombo.getSelectedItem();
        String severity = (String) severityCombo.getSelectedItem();
        String description = descriptionArea.getText().trim();

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a description",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Alert alert = new Alert(
                alertController.generateAlertId(),
                type,
                description,
                severity,
                LocalDateTime.now()
            );
            alertController.createAlert(alert);
            loadAlerts();
            clearForm();
            JOptionPane.showMessageDialog(this,
                "Alert created successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error creating alert: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedAlert() {
        int selectedRow = alertTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an alert to delete",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String alertId = (String) tableModel.getValueAt(selectedRow, 0);
        try {
            alertController.deleteAlert(alertId);
            loadAlerts();
            JOptionPane.showMessageDialog(this,
                "Alert deleted successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error deleting alert: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAlerts() {
        try {
            tableModel.setRowCount(0);
            List<Alert> alerts = alertController.getAllAlerts();
            for (Alert alert : alerts) {
                tableModel.addRow(new Object[]{
                    alert.getId(),
                    alert.getType(),
                    alert.getSeverity(),
                    alert.getDescription(),
                    alert.getTimestamp()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading alerts: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        alertTypeCombo.setSelectedIndex(0);
        severityCombo.setSelectedIndex(0);
        descriptionArea.setText("");
    }
} 