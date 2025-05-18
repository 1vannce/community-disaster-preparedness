package views;

import models.Alert;
import controllers.AlertController;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ResidentAlertPanel extends JPanel {
    private JTable alertTable;
    private DefaultTableModel tableModel;
    private AlertController alertController;
    private Timer refreshTimer;

    public ResidentAlertPanel() {
        alertController = new AlertController();
        initializeUI();
        loadAlerts();
        startAutoRefresh();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        String[] columns = {"Type", "Severity", "Description", "Timestamp"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        alertTable = new JTable(tableModel);
        alertTable.getColumnModel().getColumn(2).setPreferredWidth(300);

        // Add double-click listener to view details
        alertTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewAlertDetails();
                }
            }
        });

        // Add custom cell renderer for severity
        alertTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String severity = (String) value;
                if ("Critical".equals(severity)) {
                    c.setForeground(Color.RED);
                } else if ("High".equals(severity)) {
                    c.setForeground(new Color(255, 140, 0)); // Dark Orange
                } else if ("Medium".equals(severity)) {
                    c.setForeground(new Color(255, 165, 0)); // Orange
                } else {
                    c.setForeground(new Color(0, 128, 0)); // Dark Green
                }
                return c;
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(alertTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAlerts());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAlerts() {
        try {
            tableModel.setRowCount(0);
            List<Alert> alerts = alertController.getAllAlerts();
            for (Alert alert : alerts) {
                tableModel.addRow(new Object[]{
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

    private void viewAlertDetails() {
        int selectedRow = alertTable.getSelectedRow();
        if (selectedRow == -1) return;

        String type = (String) tableModel.getValueAt(selectedRow, 0);
        String severity = (String) tableModel.getValueAt(selectedRow, 1);
        String description = (String) tableModel.getValueAt(selectedRow, 2);
        String timestamp = tableModel.getValueAt(selectedRow, 3).toString();

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Alert Details",
            true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add alert details
        addDetailField(detailsPanel, gbc, "Type:", type, 0);
        addDetailField(detailsPanel, gbc, "Severity:", severity, 1);
        addDetailField(detailsPanel, gbc, "Time:", timestamp, 2);

        // Description area
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        detailsPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JTextArea descArea = new JTextArea(description);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setPreferredSize(new Dimension(350, 100));
        detailsPanel.add(scrollPane, gbc);

        dialog.add(detailsPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void addDetailField(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(new JLabel(value), gbc);
    }

    private void startAutoRefresh() {
        // Refresh alerts every 30 seconds
        refreshTimer = new Timer(30000, e -> loadAlerts());
        refreshTimer.start();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
} 