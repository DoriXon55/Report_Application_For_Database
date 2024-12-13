package com.example.main;

import com.example.database.DatabaseConnector;
import com.example.reports.ReportGeneratorApp;
import javafx.application.Application;
import net.sf.jasperreports.engine.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SQLException, JRException {
        Application.launch(ReportGeneratorApp.class, args);
    }
}
