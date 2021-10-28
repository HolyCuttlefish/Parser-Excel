package com.example.parseer_excel_maven.Errors;

public abstract class Errors {
    protected String otherException;

    protected int errnoNumber = 0;

    public final int STATE_OK = 0;
    public final int STATE_ERROR_FILE_IS_NOT_FOUND = -1;
    public final int STATE_ERROR_FILE_CANNOT_BE_OPEN = -2;
    public final int STATE_SHEET_IS_NOT_FOUND = -3;
    public final int STATE_IS_NOT_SELECTED_SHEET = -4;
    public final int STATE_IS_NOT_OPEN_WORKBOOK = -5;
    public final int STATE_OTHER_EXCEPTION = -6;
    public final int STATE_FORMAT_FILE_IS_NOT_SUPPORTED = -7;
    public final int STATE_FILE_ALREADY_OPEN = -8;
    public final int STATE_FILE_IS_NOT_OPEN = -9;
    public final int STATE_CONNECTION_ALREADY_OPEN = -10;
    public final int STATE_CONNECTION_IS_NOT_OPEN = -11;

    public int getErrorNumber(){ return this.errnoNumber; }

    public String getErrnoStr() {
        return getErrnoStr(this.errnoNumber);
    }

    public String getErrnoStr(int errnoNumber) {
        switch (errnoNumber) {
            case 0: {
                return "Хорошо";
            }

            case -1: {
                return "Файл не найден";
            }

            case -2: {
                return "Файл не может быть открыт";
            }

            case -3: {
                return "Лист не найден";
            }

            case -4: {
                return "Лист не выбран";
            }

            case -5: {
                return "Рабочая книга не открыта";
            }

            case -6: {
                return otherException;
            }

            case -7: {
                return "Формат файла не поддерживается";
            }

            case -8:{
                return "Файл уже открыт";
            }

            case -9:{
                return "Файл не открыт";
            }

            case -10:{
                return "Соединение уже открыто";
            }

            case -11:{
                return "Соединение не открыто";
            }

            default: {
                return "Неизвестная ошибка";
            }
        }
    }
    protected void setState(int state, String errorMsg){
        this.errnoNumber = state;
        this.otherException = errorMsg;
    }
}
