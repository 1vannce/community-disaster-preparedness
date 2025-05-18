package controllers;

import models.Alert;
import utils.FileHandler;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertController {
    private static final String ALERTS_FILE = "alerts.txt";

    public String generateAlertId() {
        return FileHandler.generateId();
    }

    public void createAlert(Alert alert) throws IOException {
        FileHandler.appendLine(ALERTS_FILE, alert.toFileString());
    }

    public List<Alert> getAllAlerts() throws IOException {
        List<String> lines = FileHandler.readLines(ALERTS_FILE);
        List<Alert> alerts = new ArrayList<>();
        
        for (String line : lines) {
            alerts.add(Alert.fromFileString(line));
        }
        
        return alerts;
    }

    public void deleteAlert(String alertId) throws IOException {
        List<String> lines = FileHandler.readLines(ALERTS_FILE);
        List<String> updatedLines = new ArrayList<>();
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (!parts[0].equals(alertId)) {
                updatedLines.add(line);
            }
        }
        
        FileHandler.writeLines(ALERTS_FILE, updatedLines);
    }

    public Alert getAlert(String alertId) throws IOException {
        List<String> lines = FileHandler.readLines(ALERTS_FILE);
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(alertId)) {
                return Alert.fromFileString(line);
            }
        }
        
        return null;
    }
} 