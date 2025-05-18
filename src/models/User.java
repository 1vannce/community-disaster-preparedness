package models;

public class User {
    private String username;
    private String password;
    private String name;
    private String contactNumber;
    private String barangay;
    private boolean isAdmin;

    public User(String username, String password, String name, String contactNumber, String barangay, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.contactNumber = contactNumber;
        this.barangay = barangay;
        this.isAdmin = isAdmin;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    
    public String getBarangay() { return barangay; }
    public void setBarangay(String barangay) { this.barangay = barangay; }
    
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    // Method to convert user data to string format for file storage
    public String toFileString() {
        return String.join("|", username, password, name, contactNumber, barangay, String.valueOf(isAdmin));
    }

    // Method to create User object from file string
    public static User fromFileString(String fileString) {
        String[] parts = fileString.split("\\|");
        return new User(
            parts[0], // username
            parts[1], // password
            parts[2], // name
            parts[3], // contactNumber
            parts[4], // barangay
            Boolean.parseBoolean(parts[5]) // isAdmin
        );
    }
} 