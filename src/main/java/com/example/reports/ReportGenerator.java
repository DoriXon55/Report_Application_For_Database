package com.example.reports;

import com.example.database.DatabaseConnector;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {

    public static void generateReport(String reportPath, Map<String, Object> parameters) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            // Ładowanie pliku raportu (jrxml)
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            // Generowanie raportu z parametrami i połączeniem
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Wyświetlenie raportu
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
