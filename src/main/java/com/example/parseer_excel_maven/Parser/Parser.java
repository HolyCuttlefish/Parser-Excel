package com.example.parseer_excel_maven.Parser;

import com.example.parseer_excel_maven.Errors.Errors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;

public class Parser extends Errors {

    private String fileName = "";
    private InputStream inputStream = null;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private File file = null;
    private Iterator<Row> rowIterator;
    private Iterator<Cell> cellIterator;
    private int row = 0;
    private int cell = 0;
    private int listNumber = 0;
    private String otherException;
    private final Logger log;

    public Parser(String fileName) throws IOException {

        this.fileName = fileName;
        file = new File(fileName);

        log = LogManager.getLogger(getClass().getName());

        if(!file.exists()){
            this.errnoNumber = -2;
        }
        else {
            this.errnoNumber = 0;
        }

        if(!file.canRead()){
            this.errnoNumber = -6;
        }
        else {
            this.errnoNumber = 0;
        }

        System.out.println(getFileExtension(fileName));

        if(!getFileExtension(fileName).equals("xlsx")){
            this.errnoNumber = -7;
        }
        else {
            this.errnoNumber = 0;
        }

        try {
            inputStream = new FileInputStream(file);
            this.workbook = new XSSFWorkbook(inputStream);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            this.errnoNumber = -6;
        }
        finally {
            if(inputStream != null){
                this.errnoNumber = 0;
            }
        }

        //ClosFile();
    }

    private String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int doIndex = fileName.lastIndexOf('.');
        return (doIndex == -1) ? "" : fileName.substring(doIndex + 1);
    }

    public void closeFile() {

        if(file == null){ this.errnoNumber = -6; }
        else { this.errnoNumber = 0; }

        if(inputStream == null){ this.errnoNumber = -6; }
        else { this.errnoNumber = 0; }

        try {
            inputStream.close();
            this.errnoNumber = 0;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            this.errnoNumber = -6;
        }
        inputStream = null;
        file = null;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getRow(){
        return this.row;
    }

    public int getCell(){
        return this.cell;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getListNumber() {
        return this.listNumber;
    }

    public void setListNumber(int listNumber) {
        this.listNumber = listNumber;
    }

    public void focusingOnSheet() {
        if(this.workbook == null) {
            this.errnoNumber = -6;
        } else {
            this.sheet = this.workbook.getSheetAt(this.listNumber);
        }
    }

    public  ArrayList<ArrayList<String>> getStringRowsCells(int rowFinal, int cellFinal) {
        XSSFCell resultCell = null;
        XSSFRow resultRow = null;
        ArrayList<ArrayList<String>> listRowsCells = new ArrayList<>();
        ArrayList<String> listRowsCellsBuf = new ArrayList<String>();

        if(this.workbook == null){
            this.errnoNumber = -6;
            return listRowsCells;
        } else if (this.sheet == null) {
            this.errnoNumber = -3;
            return listRowsCells;
        } else {
            for(int counter = 0; counter <= rowFinal; ++counter){
                listRowsCellsBuf.clear();

                resultRow = this.sheet.getRow(counter);

                for(int counter_two = 0; counter_two <= cellFinal; ++counter_two) {
                    if(resultRow == null){
                        listRowsCellsBuf.add("");
                    } else {
                        resultCell = resultRow.getCell(counter_two);
                        listRowsCellsBuf.add(resultCell != null ? resultCell.toString() : "");
                    }
                }

                listRowsCells.add((ArrayList)listRowsCellsBuf.clone());
            }

            return  listRowsCells;
        }
    }

    public ArrayList<String> getStringRowCells(int row, int cellFinal) {
        XSSFCell resultCell = null;
        XSSFRow resultRow = null;
        ArrayList<String> listRowCell = new ArrayList<String>();

        if(this.workbook == null){
            this.errnoNumber = -6;
            return listRowCell;
        } else if (this.sheet == null) {
            this.errnoNumber = -3;
            return listRowCell;
        } else {
            resultRow = this.sheet.getRow(row);
            for(int counter = 0; counter <= cellFinal; ++counter){
                if(resultRow == null){
                    listRowCell.add("");
                } else {
                    resultCell = resultRow.getCell(counter);
                    listRowCell.add(resultCell != null ? resultCell.toString() : "");
                }
            }

            return  listRowCell;
        }
    }

    public String getStringCell(int row, int cell) {
        XSSFCell resultCell = null;
        XSSFRow resultRow = null;

        if(this.workbook == null) {
            this.errnoNumber = -6;
            return "";
        } else if (this.sheet == null) {
            this.errnoNumber = -3;
            return "";
        } else {
            resultRow = this.sheet.getRow(row);
            if(resultRow == null){
                return "";
            } else {
                resultCell = resultRow.getCell(cell);
                return  resultCell != null ? resultCell.toString() : "";
            }
        }
    }

    public static void createExcelFile(String fileName, String sheetName, String headers, ArrayList<ArrayList<String>> info){

        int rowCounter = 0;

        File file = new File(fileName);

        final Logger log;
        log = LogManager.getLogger(Parser.class);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow row = sheet.createRow(rowCounter);

        FileOutputStream fileOutputStream = null;

        for(int counter = 0; counter < headers.split(":").length; ++counter){
            row.createCell(counter).setCellValue(headers.split(":")[counter]);
        }

        ++rowCounter;

        for (int counter = 0; counter < info.size(); ++counter){
            row = sheet.createRow(rowCounter);
            for(int counter2 = 0; counter2 < info.get(counter).size(); ++counter2){
                row.createCell(counter2).setCellValue(info.get(counter).get(counter2));
            }
            ++rowCounter;
        }

        try{
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            return;
        }
        finally {
            log.info("Файл Excel успешно создан");
        }
    }

    public ArrayList<ArrayList<String>> getLinesStringValues(int rowFinish, int cellFinish, int rowStart, int cellStart){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbook == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheet == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = rowStart; counter <= rowFinish; ++counter)
        {
            listBuf.clear();

            for(int counter2 = cellStart; counter2 <= cellFinish; ++counter2){

                resultRow = sheet.getRow(counter);

                if (resultRow == null){
                    listBuf.add("");
                    continue;
                }

                cell = resultRow.getCell(counter2);

                listBuf.add((cell != null)? cell.toString() : "");
            }

            list.add((ArrayList<String>) listBuf.clone());
        }

        log.info("Получить строковые значения строк из рабочей книги списка");
        return list;
    }

    public ArrayList<ArrayList<String>> getLinesStringValues(int rowFinish, int cellFinish){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listBuf = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbook == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheet == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= rowFinish; ++counter)
        {
            listBuf.clear();

            for(int counter2 = 0; counter2 <= cellFinish; ++counter2){

                resultRow = sheet.getRow(counter);

                if (resultRow == null){
                    listBuf.add("");
                    continue;
                }

                cell = resultRow.getCell(counter2);

                listBuf.add((cell != null)? cell.toString() : "");
            }

            list.add((ArrayList<String>) listBuf.clone());
        }

        log.info("Получить строковые значения строк из рабочей книги списка");
        return list;
    }

    public ArrayList<ArrayList<String>> getSelectedDataString(ArrayList<Integer> numbersColumn, ArrayList<ArrayList<String>> listData){
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String>  listElement =  new ArrayList<String>();

        for(int counter = 0; counter < listData.size();  ++counter){
            listElement.clear();
            for(int counter2 = 0; counter2 < numbersColumn.size(); ++counter2){
                listElement.add(listData.get(counter).get(numbersColumn.get(counter2)));
            }
            list.add((ArrayList<String>) listElement.clone());
        }

        return list;
    }

    public ArrayList<Integer> getNumbersCells(String template, ArrayList<String> headers){

        ArrayList<Integer> list = new ArrayList<Integer>();

        for(int counter = 0; counter < template.split(":").length; ++counter){
            for(int counter2 = 0; counter2 < headers.size(); ++counter2) {
                if (template.split(":")[counter].equals(headers.get(counter2))) {
                    list.add(counter2);
                }
            }
        }

        return list;
    }

    public ArrayList<String> getLineStringValues(int cellFinal, int row){
        ArrayList<String> list = new ArrayList<String>();
        XSSFCell cell = null;
        XSSFRow resultRow = null;

        if(this.workbook == null){
            this.setState(this.STATE_IS_NOT_OPEN_WORKBOOK, "");
            log.error(this.getErrnoStr());
            return list;
        }

        if(this.sheet == null){
            this.setState(this.STATE_IS_NOT_SELECTED_SHEET, "");
            log.error(this.getErrnoStr());
            return list;
        }

        for(int counter = 0; counter <= cellFinal; ++counter){

            resultRow = sheet.getRow(row);

            if (resultRow == null){
                list.add("");
                continue;
            }

            cell = resultRow.getCell(counter);

            list.add((cell != null)? cell.toString() : "");
        }

        log.info("Получить строковые значения строк из рабочей книги списка");
        return list;
    }
}
