package com.example.main;

import com.example.database.DatabaseConnector;
import net.sf.jasperreports.engine.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SQLException, JRException {
        // Ścieżki do raportów
        String fakturaPath = "src/main/templates/faktura.jrxml";
        String gurpowaniePath = "src/main/templates/report_grupowanie.jrxml";


        try (Connection connection = DatabaseConnector.getConnection()) {

            JasperReport fakturaReport = JasperCompileManager.compileReport(fakturaPath);
            JasperReport grupowanieReport = JasperCompileManager.compileReport(gurpowaniePath);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("nip", "1234567890");
            parameters.put("order_id", 8);

            JasperPrint fakturaPrint = JasperFillManager.fillReport(fakturaReport, parameters, connection);
            JasperPrint grupowaniePrint = JasperFillManager.fillReport(grupowanieReport, null, connection);
            String fakturaPDF = "src/main/resources/faktura.pdf";
            String grupowaniePDF = "src/main/resources/grupowanie.pdf";


            JasperExportManager.exportReportToPdfFile(fakturaPrint, fakturaPDF);
            JasperExportManager.exportReportToPdfFile(grupowaniePrint, grupowaniePDF);
            System.out.println("Wygenerowano raport! " + fakturaPDF);
            System.out.println("Wygenerowano raport! " + grupowaniePDF);


        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
