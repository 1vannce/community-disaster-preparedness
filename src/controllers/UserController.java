package controllers;

import models.User;
import utils.FileHandler;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserController {
    private static final String USERS_FILE = "users.txt";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public User login(String username, String password) throws IOException {
        // Check for admin login
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return new User(ADMIN_USERNAME, ADMIN_PASSWORD, "Administrator", "N/A", "N/A", true);
        }

        // Check regular user login
        List<String> users = FileHandler.readLines(USERS_FILE);
        Optional<String> userLine = users.stream()
            .filter(line -> {
                String[] parts = line.split("\\|");
                return parts[0].equals(username) && parts[1].equals(password);
            })
            .findFirst();

        return userLine.map(User::fromFileString).orElse(null);
    }

    public boolean register(String username, String password, String name, String contactNumber, String barangay) throws IOException {
        // Check if username already exists
        List<String> users = FileHandler.readLines(USERS_FILE);
        boolean usernameExists = users.stream()
            .map(line -> line.split("\\|")[0])
            .anyMatch(username::equals);

        if (usernameExists || username.equals(ADMIN_USERNAME)) {
            return false;
        }

        // Create new user
        User newUser = new User(username, password, name, contactNumber, barangay, false);
        FileHandler.appendLine(USERS_FILE, newUser.toFileString());
        return true;
    }

    public boolean updateUser(User user) throws IOException {
        List<String> users = FileHandler.readLines(USERS_FILE);
        for (int i = 0; i < users.size(); i++) {
            String[] parts = users.get(i).split("\\|");
            if (parts[0].equals(user.getUsername())) {
                users.set(i, user.toFileString());
                FileHandler.writeLines(USERS_FILE, users);
                return true;
            }
        }
        return false;
    }
} 