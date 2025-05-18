package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Alert {
    private String id;
    private String type;
    private String description;
    private String severity;
    private LocalDateTime timestamp;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Alert(String id, String type, String description, String severity, LocalDateTime timestamp) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.severity = severity;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // Method to convert alert data to string format for file storage
    public String toFileString() {
        return String.join("|", 
            id,
            type,
            description,
            severity,
            timestamp.format(DATE_FORMATTER)
        );
    }

    // Method to create Alert object from file string
    public static Alert fromFileString(String fileString) {
        String[] parts = fileString.split("\\|");
        return new Alert(
            parts[0], // id
            parts[1], // type
            parts[2], // description
            parts[3], // severity
            LocalDateTime.parse(parts[4], DATE_FORMATTER) // timestamp
        );
    }
} 