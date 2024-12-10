package com.example.main;

import com.example.database.DatabaseConnector;
import com.example.reports.ReportGenerator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SQLException, JRException {
        // Ścieżki do raportów
        String report1Path = "src/main/resources/reports/Coffe_1.jrxml";
        String report2Path = "src/main/resources/reports/grupowanie_report.jrxml";
        String report3Path = "src/main/resources/reports/wykres_report.jrxml";




    }
}
