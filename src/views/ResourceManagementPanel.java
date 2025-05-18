package views;

import models.EmergencyResource;
import controllers.ResourceController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResourceManagementPanel extends JPanel {
    private JTable resourceTable;
    private DefaultTableModel tableModel;
    private ResourceController resourceController;
    private JTextField nameField;
    private JComboBox<String> categoryCombo;
    private JSpinner quantitySpinner;
    private JTextField locationField;
    private JTextArea descriptionArea;

    public ResourceManagementPanel() {
        resourceController = new ResourceController();
        initializeUI();
        loadResources();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Emergency Resource"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name field
        nameField = new JTextField(20);
        addFormField(formPanel, gbc, "Name:", nameField, 0);

        // Category combo
        String[] categories = {"Food", "Water", "Medicine", "First Aid", "Tools", "Communication", "Other"};
        categoryCombo = new JComboBox<>(categories);
        addFormField(formPanel, gbc, "Category:", categoryCombo, 1);

        // Quantity spinner
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 0, 10000, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        addFormField(formPanel, gbc, "Quantity:", quantitySpinner, 2);

        // Location field
        locationField = new JTextField(20);
        addFormField(formPanel, gbc, "Location:", locationField, 3);

        // Description area
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);

        // Add button
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton addButton = new JButton("Add Resource");
        addButton.addActionListener(e -> addResource());
        formPanel.add(addButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Create table
        String[] columns = {"ID", "Name", "Category", "Quantity", "Location", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resourceTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(resourceTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton updateButton = new JButton("Update Quantity");
        updateButton.addActionListener(e -> updateSelectedResource());
        buttonPanel.add(updateButton);
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedResource());
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

    private void addResource() {
        String name = nameField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();
        String location = locationField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (name.isEmpty() || location.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            EmergencyResource resource = new EmergencyResource(
                resourceController.generateResourceId(),
                name,
                category,
                quantity,
                location,
                description
            );
            resourceController.createResource(resource);
            loadResources();
            clearForm();
            JOptionPane.showMessageDialog(this,
                "Resource added successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error adding resource: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelectedResource() {
        int selectedRow = resourceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a resource to update",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String resourceId = (String) tableModel.getValueAt(selectedRow, 0);
        String input = JOptionPane.showInputDialog(this,
            "Enter new quantity:",
            "Update Quantity",
            JOptionPane.PLAIN_MESSAGE);

        if (input != null && !input.isEmpty()) {
            try {
                int newQuantity = Integer.parseInt(input);
                if (newQuantity < 0) {
                    throw new IllegalArgumentException("Quantity cannot be negative");
                }
                
                resourceController.updateResourceQuantity(resourceId, newQuantity);
                loadResources();
                JOptionPane.showMessageDialog(this,
                    "Quantity updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid number",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error updating quantity: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedResource() {
        int selectedRow = resourceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a resource to delete",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String resourceId = (String) tableModel.getValueAt(selectedRow, 0);
        try {
            resourceController.deleteResource(resourceId);
            loadResources();
            JOptionPane.showMessageDialog(this,
                "Resource deleted successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error deleting resource: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadResources() {
        try {
            tableModel.setRowCount(0);
            List<EmergencyResource> resources = resourceController.getAllResources();
            for (EmergencyResource resource : resources) {
                tableModel.addRow(new Object[]{
                    resource.getId(),
                    resource.getName(),
                    resource.getCategory(),
                    resource.getQuantity(),
                    resource.getLocation(),
                    resource.getDescription()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading resources: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        categoryCombo.setSelectedIndex(0);
        quantitySpinner.setValue(1);
        locationField.setText("");
        descriptionArea.setText("");
    }
} 