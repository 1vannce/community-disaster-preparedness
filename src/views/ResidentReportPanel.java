package views;

import models.DamageReport;
import models.User;
import controllers.ReportController;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class ResidentReportPanel extends JPanel {
    private User resident;
    private ReportController reportController;
    private JComboBox<String> categoryCombo;
    private JTextArea descriptionArea;
    private JTextField photoPathField;
    private String selectedPhotoPath;

    public ResidentReportPanel(User resident) {
        this.resident = resident;
        this.reportController = new ReportController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Submit Damage Report"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Category
        String[] categories = {"Infrastructure", "Property", "Utilities", "Environment", "Other"};
        categoryCombo = new JComboBox<>(categories);
        addFormField(formPanel, gbc, "Category:", categoryCombo, 0);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);

        // Photo upload
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Photo:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JPanel photoPanel = new JPanel(new BorderLayout(5, 0));
        photoPathField = new JTextField();
        photoPathField.setEditable(false);
        photoPanel.add(photoPathField, BorderLayout.CENTER);

        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> browsePhoto());
        photoPanel.add(browseButton, BorderLayout.EAST);
        formPanel.add(photoPanel, gbc);

        // Submit button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JButton submitButton = new JButton("Submit Report");
        submitButton.addActionListener(e -> submitReport());
        formPanel.add(submitButton, gbc);

        // Add form to panel
        add(formPanel, BorderLayout.NORTH);

        // Add instructions
        JTextArea instructionsArea = new JTextArea(
            "Instructions:\n" +
            "1. Select the category that best describes the damage\n" +
            "2. Provide a detailed description of the damage\n" +
            "3. Optionally attach a photo of the damage\n" +
            "4. Click Submit to send your report"
        );
        instructionsArea.setEditable(false);
        instructionsArea.setBackground(getBackground());
        instructionsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(instructionsArea, BorderLayout.CENTER);
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

    private void browsePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedPhotoPath = selectedFile.getAbsolutePath();
            photoPathField.setText(selectedFile.getName());
        }
    }

    private void submitReport() {
        String category = (String) categoryCombo.getSelectedItem();
        String description = descriptionArea.getText().trim();

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a description of the damage",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String photoPath = null;
            if (selectedPhotoPath != null && !selectedPhotoPath.isEmpty()) {
                // Copy photo to application's storage
                String fileName = Paths.get(selectedPhotoPath).getFileName().toString();
                Path targetPath = Paths.get("photos", fileName);
                Files.createDirectories(Paths.get("photos"));
                Files.copy(Paths.get(selectedPhotoPath), targetPath, StandardCopyOption.REPLACE_EXISTING);
                photoPath = targetPath.toString();
            }

            DamageReport report = new DamageReport(
                reportController.generateReportId(),
                resident.getUsername(),
                category,
                description,
                photoPath,
                LocalDateTime.now()
            );

            reportController.createReport(report);
            clearForm();
            JOptionPane.showMessageDialog(this,
                "Report submitted successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error submitting report: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        categoryCombo.setSelectedIndex(0);
        descriptionArea.setText("");
        photoPathField.setText("");
        selectedPhotoPath = null;
    }
} 