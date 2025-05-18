package utils;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileHandler {
    private static final String DATA_DIR = "data";
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    static {
        // Create data directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readLines(String filename) throws IOException {
        lock.readLock().lock();
        try {
            Path filePath = Paths.get(DATA_DIR, filename);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                return new ArrayList<>();
            }
            return Files.readAllLines(filePath);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void writeLines(String filename, List<String> lines) throws IOException {
        lock.writeLock().lock();
        try {
            Path filePath = Paths.get(DATA_DIR, filename);
            Files.write(filePath, lines);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void appendLine(String filename, String line) throws IOException {
        lock.writeLock().lock();
        try {
            Path filePath = Paths.get(DATA_DIR, filename);
            Files.write(filePath, (line + System.lineSeparator()).getBytes(), 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void createDataFiles() {
        String[] files = {
            "users.txt",
            "alerts.txt",
            "contacts.txt",
            "resources.txt",
            "reports.txt"
        };

        for (String file : files) {
            try {
                Path filePath = Paths.get(DATA_DIR, file);
                if (!Files.exists(filePath)) {
                    Files.createFile(filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String generateId() {
        return String.valueOf(System.currentTimeMillis());
    }
} 