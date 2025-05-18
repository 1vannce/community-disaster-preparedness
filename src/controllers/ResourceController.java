package controllers;

import models.EmergencyResource;
import utils.FileHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceController {
    private static final String RESOURCES_FILE = "resources.txt";

    public String generateResourceId() {
        return FileHandler.generateId();
    }

    public void createResource(EmergencyResource resource) throws IOException {
        FileHandler.appendLine(RESOURCES_FILE, resource.toFileString());
    }

    public List<EmergencyResource> getAllResources() throws IOException {
        List<String> lines = FileHandler.readLines(RESOURCES_FILE);
        List<EmergencyResource> resources = new ArrayList<>();
        
        for (String line : lines) {
            resources.add(EmergencyResource.fromFileString(line));
        }
        
        return resources;
    }

    public void updateResourceQuantity(String resourceId, int newQuantity) throws IOException {
        List<String> lines = FileHandler.readLines(RESOURCES_FILE);
        List<String> updatedLines = new ArrayList<>();
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(resourceId)) {
                EmergencyResource resource = EmergencyResource.fromFileString(line);
                resource.setQuantity(newQuantity);
                updatedLines.add(resource.toFileString());
            } else {
                updatedLines.add(line);
            }
        }
        
        FileHandler.writeLines(RESOURCES_FILE, updatedLines);
    }

    public void deleteResource(String resourceId) throws IOException {
        List<String> lines = FileHandler.readLines(RESOURCES_FILE);
        List<String> updatedLines = new ArrayList<>();
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (!parts[0].equals(resourceId)) {
                updatedLines.add(line);
            }
        }
        
        FileHandler.writeLines(RESOURCES_FILE, updatedLines);
    }

    public EmergencyResource getResource(String resourceId) throws IOException {
        List<String> lines = FileHandler.readLines(RESOURCES_FILE);
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(resourceId)) {
                return EmergencyResource.fromFileString(line);
            }
        }
        
        return null;
    }
} 