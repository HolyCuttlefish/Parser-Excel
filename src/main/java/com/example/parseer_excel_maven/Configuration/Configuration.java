package com.example.parseer_excel_maven.Configuration;

import com.example.parseer_excel_maven.Errors.Errors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.*;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;


public class Configuration extends Errors {

    private String nameFile;

    Properties prop;

    private File file;
    private FileReader fileReader;
    private final Logger log;

    public Configuration(String nameFile) {
        log = LogManager.getLogger(getClass().getName());
        this.nameFile = nameFile;
    }

    public void openFile() {

        if(this.file != null) {
            this.setState(this.STATE_FILE_ALREADY_OPEN, "");
            log.error(this.getErrnoStr());
            return;
        }

        if(!checkFormat()) {
            this.setState(this.STATE_FORMAT_FILE_IS_NOT_SUPPORTED, "");
            log.error(this.getErrnoStr());
            return;
        }

        this.file = new File(this.nameFile);

        if(!this.file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception ex) {
                setState(STATE_OTHER_EXCEPTION, ex.getMessage());
                log.error(this.getErrnoStr());
            }
            finally {
                if(!file.exists()) {
                    return;
                }
                else {
                    setState(STATE_OK, "");
                    log.info("Файл конфигурации открыт");
                }
            }
        }

        if(!this.file.canRead()) {
            this.setState(this.STATE_ERROR_FILE_CANNOT_BE_OPEN, "");
            log.error(this.getErrnoStr());
            return;
        }

        try {
            fileReader = new FileReader(file);
        }
        catch (Exception ex) {
            this.setState(this.STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
        }

        finally {
            if(fileReader != null) {
                prop = new Properties();

                try {
                    prop.load(fileReader);
                }
                catch (Exception ex) {
                    setState(STATE_OTHER_EXCEPTION, ex.getMessage());
                    log.error(this.getErrnoStr());
                    return;
                }
            }
        }
    }

    public void closeFile() {
        if(file == null) {
            this.setState(this.STATE_FILE_IS_NOT_OPEN, "");
            log.error(this.getErrnoStr());
            return;
        }

        try {
            fileReader.close();
        }
        catch (Exception ex) {
            setState(STATE_OTHER_EXCEPTION, ex.getMessage());
            log.error(this.getErrnoStr());
            return;
        }
        finally {
            file = null;
            fileReader = null;
            log.info("Файл конфигурации закрыт");
        }
    }
    public String getTable() {
        if(file == null) {
            setState(STATE_FILE_IS_NOT_OPEN, "");
            log.error(this.getErrnoStr());
            return "";
        }
        log.info("Получите имя таблицы из файла конфигурации");
        return prop.getProperty("Table.Name");
    }

    public String getTemplateOne() {
        if(file == null) {
            setState(STATE_FILE_IS_NOT_OPEN, "");
            log.error(this.getErrnoStr());
            return "";
        }
        log.info("Получите первый шаблон из файла конфигурации");
        return prop.getProperty("Pattern.One");
    }

    public String getTemplateTwo() {
        if(file == null) {
            setState(STATE_FILE_IS_NOT_OPEN, "");
            log.error(this.getErrnoStr());
            return "";
        }
        log.info("Получите второй шаблон из файла конфигурации");
        return prop.getProperty("Pattern.Two");
    }

    public String getTemplateThree() {
        if(file == null) {
            setState(STATE_FILE_IS_NOT_OPEN, "");
            log.error(this.getErrnoStr());
            return "";
        }
        log.info("Получите третий шаблон из файла конфигурации");
        return prop.getProperty("Pattern.Three");
    }

    public String getSleep() {
        if(file == null) {
            setState(STATE_FILE_IS_NOT_OPEN, "");
            log.error(this.getErrnoStr());
            return "";
        }
        log.info("Получите милисекунды сна из конфигурационного файла");
        return prop.getProperty("Sleep");
    }

    public String getNameFile(){ return this.nameFile; }

    public void setNameFile(String nameFile){ this.nameFile = nameFile; }

    private boolean checkFormat(){ return ((this.nameFile.contains(".")) && (this.nameFile.substring(this.nameFile.lastIndexOf('.') + 1).equals("ini"))); }
}