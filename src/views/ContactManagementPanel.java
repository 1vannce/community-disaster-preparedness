package views;

import models.EmergencyContact;
import controllers.ContactController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContactManagementPanel extends JPanel {
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private ContactController contactController;
    private JTextField nameField;
    private JTextField organizationField;
    private JTextField contactNumberField;
    private JTextField roleField;
    private JTextField addressField;

    public ContactManagementPanel() {
        contactController = new ContactController();
        initializeUI();
        loadContacts();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Emergency Contact"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name field
        nameField = new JTextField(20);
        addFormField(formPanel, gbc, "Name:", nameField, 0);

        // Organization field
        organizationField = new JTextField(20);
        addFormField(formPanel, gbc, "Organization:", organizationField, 1);

        // Contact Number field
        contactNumberField = new JTextField(20);
        addFormField(formPanel, gbc, "Contact Number:", contactNumberField, 2);

        // Role field
        roleField = new JTextField(20);
        addFormField(formPanel, gbc, "Role:", roleField, 3);

        // Address field
        addressField = new JTextField(20);
        addFormField(formPanel, gbc, "Address:", addressField, 4);

        // Add button
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(e -> addContact());
        formPanel.add(addButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Create table
        String[] columns = {"ID", "Name", "Organization", "Contact Number", "Role", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        contactTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(contactTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedContact());
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
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

    private void addContact() {
        String name = nameField.getText().trim();
        String organization = organizationField.getText().trim();
        String contactNumber = contactNumberField.getText().trim();
        String role = roleField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || organization.isEmpty() || contactNumber.isEmpty() || role.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            EmergencyContact contact = new EmergencyContact(
                contactController.generateContactId(),
                name,
                organization,
                contactNumber,
                role,
                address
            );
            contactController.createContact(contact);
            loadContacts();
            clearForm();
            JOptionPane.showMessageDialog(this,
                "Contact added successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error adding contact: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a contact to delete",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String contactId = (String) tableModel.getValueAt(selectedRow, 0);
        try {
            contactController.deleteContact(contactId);
            loadContacts();
            JOptionPane.showMessageDialog(this,
                "Contact deleted successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error deleting contact: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadContacts() {
        try {
            tableModel.setRowCount(0);
            List<EmergencyContact> contacts = contactController.getAllContacts();
            for (EmergencyContact contact : contacts) {
                tableModel.addRow(new Object[]{
                    contact.getId(),
                    contact.getName(),
                    contact.getOrganization(),
                    contact.getContactNumber(),
                    contact.getRole(),
                    contact.getAddress()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading contacts: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        organizationField.setText("");
        contactNumberField.setText("");
        roleField.setText("");
        addressField.setText("");
    }
} 