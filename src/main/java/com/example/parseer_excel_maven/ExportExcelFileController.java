package com.example.parseer_excel_maven;

import com.example.parseer_excel_maven.Configuration.Configuration;
import com.example.parseer_excel_maven.bd.Database;
import com.example.parseer_excel_maven.Parser.Parser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExportExcelFileController implements Initializable {

    @FXML
    public ComboBox<String> templateChooser;

    @FXML
    public VBox vBox;

    @FXML
    protected void onClickExportExcelFileBut(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        File file = null;
        Database database = new Database("HolyCuttlefish", "Kippe154125", "localhost", 3306, "Parser_database");
        Configuration conf = new Configuration("config.ini");

        fileChooser.setTitle("Сохранение файла");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx"));

        file = fileChooser.showSaveDialog((Stage)((Node)event.getSource()).getScene().getWindow());

        if(file == null){
            return;
        }

        //database.openConnection();
        conf.openFile();

        if(templateChooser.getValue().equals(conf.getTemplateOne())){
            System.out.println("DEBUG: " + templateChooser.getValue());
            System.out.println("DEBUG:" + database.getDataTableOne(templateChooser.getValue()).size());
            Parser.createExcelFile(file.getAbsolutePath() + ".xlsx", "0", templateChooser.getValue(), database.getDataTableOne(templateChooser.getValue()));
        }
        else if(templateChooser.getValue().equals(conf.getTemplateTwo())) {
            Parser.createExcelFile(file.getAbsolutePath() + ".xlsx", "0", templateChooser.getValue(), database.getDataTableTwo(templateChooser.getValue()));
        }
        else {
            Parser.createExcelFile(file.getAbsolutePath() +".xlsx", "0", templateChooser.getValue(), database.getDataTableThree(templateChooser.getValue()));
        }

        database.closeConnection();
        conf.closeFile();
    }

    @FXML
    protected void onClickBackBut(ActionEvent event) throws IOException {
        Node node = (Node)event.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        loadFormMainWindow();
        stage.close();
    }

    private void loadFormMainWindow() throws IOException {
        MainApplication mainApplication = new MainApplication();
        mainApplication.showWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String style = getClass().getResource("/com/example/parseer_excel_maven/style.css").toExternalForm();
        vBox.getStylesheets().add(style);

        Configuration conf = new Configuration("config.ini");

        conf.openFile();

        ObservableList<String> list = FXCollections.observableArrayList();

        list.add(conf.getTemplateOne());
        list.add(conf.getTemplateTwo());
        list.add(conf.getTemplateThree());

        conf.closeFile();

        templateChooser.setItems(list);
        templateChooser.setValue(list.get(0));
    }
}
