package com.example.parseer_excel_maven;

import com.example.parseer_excel_maven.Configuration.Configuration;
import com.example.parseer_excel_maven.bd.Database;
import com.example.parseer_excel_maven.Models.Pattern_One;
import com.example.parseer_excel_maven.Models.Pattern_Two;
import com.example.parseer_excel_maven.Models.Pattern_Three;
import com.example.parseer_excel_maven.Parser.Parser;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ImportExcelFileController implements Initializable {

    @FXML
    public TableView<ObservableList<String>> table;
    @FXML
    public ProgressBar progress;
    @FXML
    public ComboBox<String> templateChooser;
    @FXML
    public Label nameTable;
    @FXML
    public VBox vBox;

    private File file;

    @FXML
    protected void onClickExportBut(ActionEvent event) throws InterruptedException, IOException {

        if(file == null){
            return;
        }

        Parser parser = new Parser(file.getAbsolutePath());
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        Configuration conf = new Configuration("config.ini");
        Database db = new Database("HolyCuttlefish", "Kippe154125", "localhost", 3306, "Parser_database");
        ArrayList<ArrayList<String>> data = null;
        ArrayList<Pattern_One> listPatternOne = new ArrayList<Pattern_One>();
        ArrayList<Pattern_Two> listPatternTwo = new ArrayList<Pattern_Two>();
        ArrayList<Pattern_Three> listPatternThree = new ArrayList<Pattern_Three>();
        long sleep = 0;

        progress.setOpacity(1);

        //parser.openFile();
        conf.openFile();
        //db.openConnection();

        parser.focusingOnSheet();

        sleep = Long.parseLong(conf.getSleep());

        list = parser.getLinesStringValues(4,9, 1, 0);
        data = parser.getSelectedDataString(parser.getNumbersCells(templateChooser.getValue(), parser.getLineStringValues(9, 0)), parser.getLinesStringValues(4,9, 1, 0));

        if(templateChooser.getValue().equals(conf.getTemplateOne()))
        {
            for(int counter = 0; counter < data.size(); ++counter){
                listPatternOne.add(new Pattern_One(0, data.get(counter).get(0), data.get(counter).get(1)));
            }
        }
        else if(templateChooser.getValue().equals(conf.getTemplateTwo())){
            for(int counter = 0; counter < data.size(); ++counter){
                listPatternTwo.add(new Pattern_Two(0, data.get(counter).get(0), data.get(counter).get(1), (long)Double.parseDouble(data.get(counter).get(2))));
            }
        }
        else {
            for (int counter = 0; counter < data.size(); ++counter) {
                listPatternThree.add(new Pattern_Three(0, data.get(counter).get(0), data.get(counter).get(1), (long)Double.parseDouble(data.get(counter).get(2)), Boolean.getBoolean(data.get(counter).get(3))));
            }
        }

        long  finalSleep = sleep;

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                System.out.println(finalSleep);

                if(templateChooser.getValue().equals(conf.getTemplateOne()))
                {
                    for (int counter = 0; counter < listPatternOne.size(); ++counter){
                        db.insertDataTableOne(listPatternOne.get(counter).getName(), listPatternOne.get(counter).getDescription());
                        System.out.println(progress.getProgress());
                        System.out.println();

                        double finalStage = (1.0 / listPatternOne.size());;
                        Platform.runLater(() -> progress.setProgress(finalStage + progress.getProgress()));

                        try {
                            Thread.sleep(finalSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if(templateChooser.getValue().equals(conf.getTemplateTwo())){
                    for (int counter = 0; counter < listPatternTwo.size(); ++counter){
                        db.insertDataTableTwo(listPatternTwo.get(counter).getName(), listPatternTwo.get(counter).getDescription(), listPatternTwo.get(counter).getValue());
                        System.out.println(progress.getProgress());
                        System.out.println();

                        double finalStage_one = (1.0 / listPatternTwo.size());
                        System.out.println(finalStage_one);
                        Platform.runLater(() -> progress.setProgress(progress.getProgress() + finalStage_one));

                        try {
                            Thread.sleep(finalSleep);
                        } catch (InterruptedException e) {
                            System.out.println("f");
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    for (int counter = 0; counter < listPatternThree.size(); ++counter){
                        db.insertDataTableThree(listPatternThree.get(counter).getName(), listPatternThree.get(counter).getDescription(), listPatternThree.get(counter).getValue(), listPatternThree.get(counter).getAvailability());
                        System.out.println(progress.getProgress());
                        System.out.println();

                        double finalStage_two = (1.0 / listPatternThree.size());
                        Platform.runLater(() -> progress.setProgress(progress.getProgress() + finalStage_two));

                        try {
                            Thread.sleep(finalSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                this.succeeded();
                return null;
            }
        };

        task.setOnSucceeded((WorkerStateEvent eventTask) -> {
            progress.setProgress(0.0);
            progress.setOpacity(0);

            try {
                parser.closeFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            conf.closeFile();

            db.closeConnection();
        });

        Thread thread = new Thread(task);

        thread.start();
    }

    @FXML
    protected void onClickImportBut(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parser parser;
        Database db;
        ObservableList<ObservableList<String>> list;
        ArrayList<ArrayList<String>> listData;

        table.getColumns().clear();

        fileChooser.setTitle("Установите файл Excel");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel файл (*.xlsx)", "*.xlsx"));

        file = fileChooser.showOpenDialog(stage);

        if(file == null){
            return;
        }

        parser = new Parser(file.getAbsolutePath());

        parser.focusingOnSheet();

        list = FXCollections.observableArrayList();

        listData = parser.getLinesStringValues(4,9);

        for(int counter = 1; counter < listData.size(); ++counter){
            list.add(FXCollections.observableArrayList(listData.get(counter)));
        }

        table.setItems(list);

        for (int counter = 0; counter < listData.get(0).size(); ++counter){
            final int index = counter;

            TableColumn<ObservableList<String>, String> column = new TableColumn<ObservableList<String>, String>(listData.get(0).get(index));
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index)));
            column.setEditable(false);
            column.setOnEditCommit((TableColumn.CellEditEvent<ObservableList<String>, String> t) -> {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).set(t.getTablePosition().getColumn(), t.getNewValue());
            } );
            table.getColumns().add(column);
        }

        parser.closeFile();
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

        table.getStylesheets().add(style);

        templateChooser.getScene();

        Configuration conf = new Configuration("config.ini");

        conf.openFile();

        ObservableList<String> list = FXCollections.observableArrayList();

        list.add(conf.getTemplateOne());
        System.out.println(conf.getTemplateOne());
        list.add(conf.getTemplateTwo());
        list.add(conf.getTemplateThree());

        nameTable.setText(conf.getTable());

        conf.closeFile();

        templateChooser.setItems(list);
        templateChooser.setValue(list.get(0));
    }
}
