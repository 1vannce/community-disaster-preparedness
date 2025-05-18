package views;

import models.DamageReport;
import models.User;
import controllers.ReportController;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ResidentReportHistoryPanel extends JPanel {
    private User resident;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private ReportController reportController;
    private JComboBox<String> filterCombo;

    public ResidentReportHistoryPanel(User resident) {
        this.resident = resident;
        this.reportController = new ReportController();
        initializeUI();
        loadReports();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Category:"));
        
        String[] categories = {"All", "Infrastructure", "Property", "Utilities", "Environment", "Other"};
        filterCombo = new JComboBox<>(categories);
        filterCombo.addActionListener(e -> filterReports());
        filterPanel.add(filterCombo);

        add(filterPanel, BorderLayout.NORTH);

        // Create table
        String[] columns = {"ID", "Category", "Description", "Photo", "Timestamp"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(tableModel);
        reportTable.getColumnModel().getColumn(2).setPreferredWidth(300);

        // Add double-click listener to view details
        reportTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewReportDetails();
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(reportTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadReports());
        buttonPanel.add(refreshButton);
        
        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> viewReportDetails());
        buttonPanel.add(viewButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadReports() {
        try {
            tableModel.setRowCount(0);
            List<DamageReport> reports = reportController.getReportsByUser(resident.getUsername());
            String selectedCategory = (String) filterCombo.getSelectedItem();
            
            for (DamageReport report : reports) {
                if (selectedCategory.equals("All") || selectedCategory.equals(report.getCategory())) {
                    tableModel.addRow(new Object[]{
                        report.getId(),
                        report.getCategory(),
                        report.getDescription(),
                        report.getPhotoPath() != null ? "Yes" : "No",
                        report.getTimestamp()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading reports: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterReports() {
        loadReports();
    }

    private void viewReportDetails() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a report to view",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String reportId = (String) tableModel.getValueAt(selectedRow, 0);
        try {
            DamageReport report = reportController.getReport(reportId);
            if (report != null) {
                showReportDetailsDialog(report);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading report details: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReportDetailsDialog(DamageReport report) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Report Details",
            true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add report details
        addDetailField(detailsPanel, gbc, "Report ID:", report.getId(), 0);
        addDetailField(detailsPanel, gbc, "Category:", report.getCategory(), 1);
        addDetailField(detailsPanel, gbc, "Timestamp:", report.getTimestamp().toString(), 2);

        // Description area
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        detailsPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JTextArea descArea = new JTextArea(report.getDescription());
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setPreferredSize(new Dimension(450, 100));
        detailsPanel.add(scrollPane, gbc);

        // Photo preview if available
        if (report.getPhotoPath() != null && !report.getPhotoPath().isEmpty()) {
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            try {
                ImageIcon imageIcon = new ImageIcon(report.getPhotoPath());
                // Scale image if too large
                if (imageIcon.getIconWidth() > 400) {
                    Image image = imageIcon.getImage();
                    Image scaledImage = image.getScaledInstance(400, -1, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(scaledImage);
                }
                JLabel photoLabel = new JLabel(imageIcon);
                photoLabel.setBorder(BorderFactory.createTitledBorder("Photo"));
                detailsPanel.add(photoLabel, gbc);
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("Error loading photo: " + report.getPhotoPath());
                errorLabel.setForeground(Color.RED);
                detailsPanel.add(errorLabel, gbc);
            }
        }

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
} 