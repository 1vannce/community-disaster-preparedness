package controllers;

import models.DamageReport;
import utils.FileHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportController {
    private static final String REPORTS_FILE = "reports.txt";

    public String generateReportId() {
        return FileHandler.generateId();
    }

    public void createReport(DamageReport report) throws IOException {
        FileHandler.appendLine(REPORTS_FILE, report.toFileString());
    }

    public List<DamageReport> getAllReports() throws IOException {
        List<String> lines = FileHandler.readLines(REPORTS_FILE);
        List<DamageReport> reports = new ArrayList<>();
        
        for (String line : lines) {
            reports.add(DamageReport.fromFileString(line));
        }
        
        return reports;
    }

    public List<DamageReport> getReportsByUser(String username) throws IOException {
        List<String> lines = FileHandler.readLines(REPORTS_FILE);
        List<DamageReport> reports = new ArrayList<>();
        
        for (String line : lines) {
            DamageReport report = DamageReport.fromFileString(line);
            if (report.getUsername().equals(username)) {
                reports.add(report);
            }
        }
        
        return reports;
    }

    public DamageReport getReport(String reportId) throws IOException {
        List<String> lines = FileHandler.readLines(REPORTS_FILE);
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(reportId)) {
                return DamageReport.fromFileString(line);
            }
        }
        
        return null;
    }

    public void deleteReport(String reportId) throws IOException {
        List<String> lines = FileHandler.readLines(REPORTS_FILE);
        List<String> updatedLines = new ArrayList<>();
        
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (!parts[0].equals(reportId)) {
                updatedLines.add(line);
            }
        }
        
        FileHandler.writeLines(REPORTS_FILE, updatedLines);
    }
} 