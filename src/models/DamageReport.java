package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DamageReport {
    private String id;
    private String username;
    private String category;
    private String description;
    private String photoPath;
    private LocalDateTime timestamp;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DamageReport(String id, String username, String category, String description, String photoPath, LocalDateTime timestamp) {
        this.id = id;
        this.username = username;
        this.category = category;
        this.description = description;
        this.photoPath = photoPath;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // Method to convert damage report data to string format for file storage
    public String toFileString() {
        return String.join("|", 
            id,
            username,
            category,
            description,
            photoPath == null ? "" : photoPath,
            timestamp.format(DATE_FORMATTER)
        );
    }

    // Method to create DamageReport object from file string
    public static DamageReport fromFileString(String fileString) {
        String[] parts = fileString.split("\\|");
        return new DamageReport(
            parts[0], // id
            parts[1], // username
            parts[2], // category
            parts[3], // description
            parts[4].isEmpty() ? null : parts[4], // photoPath
            LocalDateTime.parse(parts[5], DATE_FORMATTER) // timestamp
        );
    }
} 