package com.example.parseer_excel_maven;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public VBox vBox;

    @FXML
    protected void onClickImportBut(ActionEvent event) throws IOException {
        loadFormImport  ();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onClickExportBut(ActionEvent event) throws IOException {
        loadFormExport();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    private void loadFormExport() throws IOException {
        ExportExcelFileApplication exportExcelFileApplication = new ExportExcelFileApplication();
        exportExcelFileApplication.showWindow();
    }

    private void loadFormImport() throws IOException {
        ImportExcelFileApplication importExcelFileApplication = new ImportExcelFileApplication();
        importExcelFileApplication.showWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String style = getClass().getResource("/com/example/parseer_excel_maven/style.css").toExternalForm();
        vBox.getStylesheets().add(style);
    }
}