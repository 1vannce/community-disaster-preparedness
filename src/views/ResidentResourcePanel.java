package views;

import models.EmergencyResource;
import controllers.ResourceController;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ResidentResourcePanel extends JPanel {
    private JTable resourceTable;
    private DefaultTableModel tableModel;
    private ResourceController resourceController;
    private JComboBox<String> filterCombo;

    public ResidentResourcePanel() {
        resourceController = new ResourceController();
        initializeUI();
        loadResources();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Category:"));
        
        String[] categories = {"All", "Food", "Water", "Medicine", "First Aid", "Tools", "Communication", "Other"};
        filterCombo = new JComboBox<>(categories);
        filterCombo.addActionListener(e -> filterResources());
        filterPanel.add(filterCombo);

        add(filterPanel, BorderLayout.NORTH);

        // Create table
        String[] columns = {"Name", "Category", "Quantity", "Location", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Integer.class : String.class;
            }
        };
        resourceTable = new JTable(tableModel);
        resourceTable.getColumnModel().getColumn(4).setPreferredWidth(200);

        // Add custom cell renderer for quantity
        resourceTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int quantity = (Integer) value;
                if (quantity == 0) {
                    c.setForeground(Color.RED);
                } else if (quantity < 10) {
                    c.setForeground(new Color(255, 140, 0)); // Dark Orange
                } else {
                    c.setForeground(new Color(0, 128, 0)); // Dark Green
                }
                return c;
            }
        });

        // Add double-click listener to view details
        resourceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewResourceDetails();
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(resourceTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadResources());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadResources() {
        try {
            tableModel.setRowCount(0);
            List<EmergencyResource> resources = resourceController.getAllResources();
            String selectedCategory = (String) filterCombo.getSelectedItem();
            
            for (EmergencyResource resource : resources) {
                if (selectedCategory.equals("All") || selectedCategory.equals(resource.getCategory())) {
                    tableModel.addRow(new Object[]{
                        resource.getName(),
                        resource.getCategory(),
                        resource.getQuantity(),
                        resource.getLocation(),
                        resource.getDescription()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading resources: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterResources() {
        loadResources();
    }

    private void viewResourceDetails() {
        int selectedRow = resourceTable.getSelectedRow();
        if (selectedRow == -1) return;

        String name = (String) tableModel.getValueAt(selectedRow, 0);
        String category = (String) tableModel.getValueAt(selectedRow, 1);
        Integer quantity = (Integer) tableModel.getValueAt(selectedRow, 2);
        String location = (String) tableModel.getValueAt(selectedRow, 3);
        String description = (String) tableModel.getValueAt(selectedRow, 4);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Resource Details",
            true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add resource details
        addDetailField(detailsPanel, gbc, "Name:", name, 0);
        addDetailField(detailsPanel, gbc, "Category:", category, 1);
        addDetailField(detailsPanel, gbc, "Quantity:", quantity.toString(), 2);
        addDetailField(detailsPanel, gbc, "Location:", location, 3);

        // Description area
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        detailsPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JTextArea descArea = new JTextArea(description);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setPreferredSize(new Dimension(350, 80));
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
} 