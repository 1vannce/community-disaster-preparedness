package models;

public class EmergencyContact {
    private String id;
    private String name;
    private String organization;
    private String contactNumber;
    private String role;
    private String address;

    public EmergencyContact(String id, String name, String organization, String contactNumber, String role, String address) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.contactNumber = contactNumber;
        this.role = role;
        this.address = address;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // Method to convert emergency contact data to string format for file storage
    public String toFileString() {
        return String.join("|", 
            id,
            name,
            organization,
            contactNumber,
            role,
            address
        );
    }

    // Method to create EmergencyContact object from file string
    public static EmergencyContact fromFileString(String fileString) {
        String[] parts = fileString.split("\\|");
        return new EmergencyContact(
            parts[0], // id
            parts[1], // name
            parts[2], // organization
            parts[3], // contactNumber
            parts[4], // role
            parts[5]  // address
        );
    }
} 