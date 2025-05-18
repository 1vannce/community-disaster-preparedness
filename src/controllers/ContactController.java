package controllers;

import models.EmergencyContact;
import utils.FileHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactController {
    private static final String CONTACTS_FILE = "contacts.txt";

    public String generateContactId() {
        return FileHandler.generateId();
    }

    public void createContact(EmergencyContact contact) throws IOException {
        FileHandler.appendLine(CONTACTS_FILE, contact.toFileString());
    }

    public List<EmergencyContact> getAllContacts() throws IOException {
        List<String> lines = FileHandler.readLines(CONTACTS_FILE);
        List<EmergencyContact> contacts = new ArrayList<>();
        
        for (String line : lines) {
            contacts.add(EmergencyContact.fromFileString(line));
        }
        
        return contacts;
    }

    public void deleteContact(String contactId) throws IOException {
        List<String> lines = FileHandler.readLines(CONTACTS_FILE);
        List<String> updatedLines = new ArrayList<>();
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (!parts[0].equals(contactId)) {
                updatedLines.add(line);
            }
        }
        
        FileHandler.writeLines(CONTACTS_FILE, updatedLines);
    }

    public EmergencyContact getContact(String contactId) throws IOException {
        List<String> lines = FileHandler.readLines(CONTACTS_FILE);
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(contactId)) {
                return EmergencyContact.fromFileString(line);
            }
        }
        
        return null;
    }
} 