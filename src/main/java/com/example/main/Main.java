package com.example.main;

import com.example.reports.ReportGenerator;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Ścieżki do raportów
        String report1Path = "src/main/resources/reports/report1_grouping.jrxml";
        String report2Path = "src/main/resources/reports/report2_chart.jrxml";
        String report3Path = "src/main/resources/reports/report3_form.jrxml";

        // Generowanie raportu 1: Grupy zamówień
        Map<String, Object> report1Params = new HashMap<>();
        report1Params.put("start_date", "2024-01-01");
        report1Params.put("end_date", "2024-12-31");
        report1Params.put("order_status", "Completed");
        ReportGenerator.generateReport(report1Path, report1Params);

        // Generowanie raportu 2: Wykres
        Map<String, Object> report2Params = new HashMap<>();
        report2Params.put("start_date", "2024-01-01");
        report2Params.put("end_date", "2024-12-31");
        ReportGenerator.generateReport(report2Path, report2Params);

        // Generowanie raportu 3: Formularz zamówienia
        Map<String, Object> report3Params = new HashMap<>();
        report3Params.put("order_id", 12345);
        ReportGenerator.generateReport(report3Path, report3Params);
    }
}
