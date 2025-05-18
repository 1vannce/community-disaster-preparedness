package views;

import models.EmergencyContact;
import controllers.ContactController;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ResidentContactPanel extends JPanel {
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private ContactController contactController;
    private JComboBox<String> filterCombo;

    public ResidentContactPanel() {
        contactController = new ContactController();
        initializeUI();
        loadContacts();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Organization:"));
        
        filterCombo = new JComboBox<>(new String[]{"All", "Police", "Fire Department", "Hospital", "Barangay", "Other"});
        filterCombo.addActionListener(e -> filterContacts());
        filterPanel.add(filterCombo);

        add(filterPanel, BorderLayout.NORTH);

        // Create table
        String[] columns = {"Name", "Organization", "Contact Number", "Role", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        contactTable = new JTable(tableModel);
        contactTable.getColumnModel().getColumn(4).setPreferredWidth(200);

        // Add double-click listener to view details
        contactTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewContactDetails();
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(contactTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadContacts());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadContacts() {
        try {
            tableModel.setRowCount(0);
            List<EmergencyContact> contacts = contactController.getAllContacts();
            String selectedOrg = (String) filterCombo.getSelectedItem();
            
            for (EmergencyContact contact : contacts) {
                if (selectedOrg.equals("All") || selectedOrg.equals(contact.getOrganization())) {
                    tableModel.addRow(new Object[]{
                        contact.getName(),
                        contact.getOrganization(),
                        contact.getContactNumber(),
                        contact.getRole(),
                        contact.getAddress()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading contacts: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterContacts() {
        loadContacts();
    }

    private void viewContactDetails() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) return;

        String name = (String) tableModel.getValueAt(selectedRow, 0);
        String organization = (String) tableModel.getValueAt(selectedRow, 1);
        String contactNumber = (String) tableModel.getValueAt(selectedRow, 2);
        String role = (String) tableModel.getValueAt(selectedRow, 3);
        String address = (String) tableModel.getValueAt(selectedRow, 4);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Contact Details",
            true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add contact details
        addDetailField(detailsPanel, gbc, "Name:", name, 0);
        addDetailField(detailsPanel, gbc, "Organization:", organization, 1);
        addDetailField(detailsPanel, gbc, "Contact Number:", contactNumber, 2);
        addDetailField(detailsPanel, gbc, "Role:", role, 3);

        // Address area
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        detailsPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JTextArea addressArea = new JTextArea(address);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(addressArea);
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