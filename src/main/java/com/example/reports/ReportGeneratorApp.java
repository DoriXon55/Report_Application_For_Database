package com.example.reports;

import com.example.database.DatabaseConnector;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.SimpleFontExtensionHelper;
import net.sf.jasperreports.engine.fonts.SimpleFontFace;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReportGeneratorApp extends Application {
    private ComboBox<String> reportComboBox;
    private TextField minPriceField, maxPriceField, nipField, orderIdField;
    private Button generateButton;
    private Label statusLabel;
    private final String PDF_OUTPUT_PATH = "src/main/templates/";


    // Funkcja generowania raportu
    private String generateReportFile(String reportName, Map<String, Object> parameters) {
        System.out.println("Generowanie raportu: " + reportName);
        System.out.println("Podane parametry: " + parameters);

        try (Connection connection = DatabaseConnector.getConnection()) {

            String jrxmlPath = "src/main/templates/" + reportName.toLowerCase() + ".jrxml";

            JasperReport report = JasperCompileManager.compileReport(jrxmlPath);
            JasperPrint reportPrint = JasperFillManager.fillReport(report, parameters, connection);

            String outputPath = PDF_OUTPUT_PATH + reportName.toLowerCase() + "_report.pdf";
            JasperExportManager.exportReportToPdfFile(reportPrint, outputPath);

            return outputPath;
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Błąd podczas generowania raportu.");
            return null;
        }
    }

    // Funkcja otwierania pliku PDF
    private void openReportFile(String reportPath) {
        System.out.println("Otwieranie raportu: " + reportPath);
        try {
            File pdfFile = new File(reportPath);

            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
                System.out.println("Plik został otwarty.");
            } else {
                System.out.println("Plik nie istnieje.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Funkcja generowania wybranego raportu
    private void generateReport() {
        String selectedReport = reportComboBox.getValue();
        if (selectedReport == null) {
            statusLabel.setText("Proszę wybrać raport.");
            return;
        }

        Map<String, Object> parameters = new HashMap<>();
        try {
            if ("Wykres".equals(selectedReport)) {
                String minPrice = minPriceField.getText();
                String maxPrice = maxPriceField.getText();
                if (minPrice.isEmpty() || maxPrice.isEmpty()) {
                    statusLabel.setText("Proszę podać zakres cen.");
                    return;
                }
                parameters.put("min_price", Integer.parseInt(minPrice));
                parameters.put("max_price", Integer.parseInt(maxPrice));
            } else if ("Faktura".equals(selectedReport)) {
                String nip = nipField.getText();
                String orderId = orderIdField.getText();
                if (nip.isEmpty() || orderId.isEmpty()) {
                    statusLabel.setText("Proszę podać NIP i numer zamówienia.");
                    return;
                }
                parameters.put("nip", nip);
                parameters.put("order_id", Integer.parseInt(orderId));
            }

            String reportPath = generateReportFile(selectedReport, parameters);
            if (reportPath != null) {
                statusLabel.setText("Raport został wygenerowany: " + reportPath);
                openReportFile(reportPath);
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Proszę podać poprawne wartości liczbowe.");
        }
    }

    // Pokazywanie i ukrywanie pól wejściowych
    private void toggleInputFields(String selectedReport) {
        minPriceField.setVisible(false);
        maxPriceField.setVisible(false);
        nipField.setVisible(false);
        orderIdField.setVisible(false);

        if ("Wykres".equals(selectedReport)) {
            minPriceField.setVisible(true);
            maxPriceField.setVisible(true);
        } else if ("Faktura".equals(selectedReport)) {
            nipField.setVisible(true);
            orderIdField.setVisible(true);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aplikacja do generowania raportów");

        // Layout aplikacji
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Wybieranie raportu
        reportComboBox = new ComboBox<>();
        reportComboBox.getItems().addAll("Wykres", "Faktura", "Grupowanie");
        reportComboBox.setPromptText("Wybierz raport");

        // Pola wejściowe
        minPriceField = new TextField();
        minPriceField.setPromptText("Minimalna cena");
        maxPriceField = new TextField();
        maxPriceField.setPromptText("Maksymalna cena");
        nipField = new TextField();
        nipField.setPromptText("NIP");
        orderIdField = new TextField();
        orderIdField.setPromptText("Numer zamówienia");

        generateButton = new Button("Generuj raport");
        generateButton.setOnAction(e -> generateReport());

        statusLabel = new Label();

        // Dodanie elementów do layoutu
        grid.add(new Label("Wybierz raport:"), 0, 0);
        grid.add(reportComboBox, 1, 0);
        grid.add(new Label("Parametry:"), 0, 1);
        grid.add(minPriceField, 1, 1);
        grid.add(maxPriceField, 2, 1);
        grid.add(nipField, 1, 2);
        grid.add(orderIdField, 2, 2);
        grid.add(generateButton, 1, 3);
        grid.add(statusLabel, 1, 4, 2, 1);

        toggleInputFields(null);

        reportComboBox.setOnAction(e -> toggleInputFields(reportComboBox.getValue()));

        Scene scene = new Scene(grid, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}