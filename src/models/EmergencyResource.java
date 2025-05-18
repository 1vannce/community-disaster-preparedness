package models;

public class EmergencyResource {
    private String id;
    private String name;
    private String category;
    private int quantity;
    private String location;
    private String description;

    public EmergencyResource(String id, String name, String category, int quantity, String location, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.location = location;
        this.description = description;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Method to convert emergency resource data to string format for file storage
    public String toFileString() {
        return String.join("|", 
            id,
            name,
            category,
            String.valueOf(quantity),
            location,
            description
        );
    }

    // Method to create EmergencyResource object from file string
    public static EmergencyResource fromFileString(String fileString) {
        String[] parts = fileString.split("\\|");
        return new EmergencyResource(
            parts[0], // id
            parts[1], // name
            parts[2], // category
            Integer.parseInt(parts[3]), // quantity
            parts[4], // location
            parts[5]  // description
        );
    }
} 