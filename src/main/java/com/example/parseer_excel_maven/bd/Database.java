package com.example.parseer_excel_maven.bd;

import com.example.parseer_excel_maven.Configuration.Configuration;
import com.example.parseer_excel_maven.Errors.Errors;
import com.example.parseer_excel_maven.Models.Pattern_One;
import com.example.parseer_excel_maven.Models.Pattern_Two;
import com.example.parseer_excel_maven.Models.Pattern_Three;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class Database extends Errors {

    private String user;
    private String password;
    private String host;
    private String databaseName;

    private String url  = "jdbc:mysql://HOST:PORT/DBNAME";

    private int port = 0;

    private Connection conn;

    private final Logger log;

    public Database(String user, String password, String host, int port, String databaseName) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.databaseName = databaseName;
        this.port = port;

        log = LogManager.getLogger(getClass().getName());

        if(conn != null) {
            setState(STATE_CONNECTION_ALREADY_OPEN, "");
            log.error(this.getErrnoStr());
            return;
        }

        this.url = this.url.replace("HOST", this.host).replace("PORT", Integer.toString(port)).replace("DBNAME", databaseName);

        try {
            this.conn = DriverManager.getConnection(this.url, this.user, this.password);
        }
        catch (Exception ex){
            this.errnoNumber = -6;
            return;
        }
        finally {
            this.errnoNumber = 0;
            return;
        }
    }

    public void insertDataTableOne(ArrayList<ArrayList<String>> data) {
        String query = "insert into SimpleTableForTemplateOne values(null, ?, ?)";
        PreparedStatement stmt = null;

        for(int counter = 0; counter < data.size(); ++counter) {

            try {
                stmt = conn.prepareStatement(query);

                stmt.setString(1, data.get(counter).get(0));
                stmt.setString(2, data.get(counter).get(1));

                stmt.execute();
            } catch (Exception ex) {
                this.errnoNumber = -6;
                return;
            }
            log.info("Вставлена строка из таблицы SimpleTableForPatternOne");
        }
    }

    public void insertDataTableOne(String name, String description) {
        String query = "insert into SimpleTableForTemplateOne values(null, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(query);

            stmt.setString(1, name);
            stmt.setString(2, description);

            stmt.execute();
        } catch (Exception ex) {
            this.errnoNumber = -6;
            return;
        }
        finally {
            log.info("Вставлена строка из таблицы SimpleTableForPatternOne");
        }
    }

    public void insertDataTableTwo(ArrayList<ArrayList<String>> data) {
        String query = "insert into SimpleTableForTemplateTwo values(null, ?, ?, ?)";
        PreparedStatement stmt = null;
        Double num = 0.0;

        for(int counter = 0; counter < data.size(); ++counter) {

            try {
                num = Double.parseDouble(data.get(counter).get(2));

                stmt = conn.prepareStatement(query);

                stmt.setString(1, data.get(counter).get(0));
                stmt.setString(2, data.get(counter).get(1));
                stmt.setInt(3, num.intValue());

                stmt.execute();
            } catch (Exception ex) {
                this.errnoNumber = -6;
                return;
            }
            finally {
                log.info("Вставлена строка из таблицы SimpleTableForPatternTwo");
            }
        }
    }

    public void insertDataTableTwo(String name, String description, long value) {
        String query = "insert into SimpleTableForTemplateTwo values(null, ?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(query);

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setLong(3, value);

            stmt.execute();
        } catch (Exception ex) {
            this.errnoNumber = -6;
            return;
        }
        finally {
            log.info("Вставлена строка из таблицы SimpleTableForPatternTwo");
        }
    }

    public void insertDataTableThree(ArrayList<ArrayList<String>> data) {
        String query = "insert into SimpleTableForTemplateThree values(null, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        Double num = 0.0;
        Double num2 = 0.0;

        for(int counter = 0; counter < data.size(); ++counter) {

            try {
                num = Double.parseDouble(data.get(counter).get(2));
                num2 = Double.parseDouble(data.get(counter).get(3));

                stmt = conn.prepareStatement(query);

                stmt.setString(1, data.get(counter).get(0));
                stmt.setString(2, data.get(counter).get(1));
                stmt.setInt(3, num.intValue());
                stmt.setBoolean(4, (num2 != 0.0)? true : false);

                stmt.execute();
            } catch (Exception ex) {
                this.errnoNumber = -6;
                return;
            }
            finally {
                log.info("Вставлена строка из таблицы SimpleTableForPatternThree");
            }
        }
    }

    public void insertDataTableThree(String name, String description, long value, boolean availability) {
        String query = "insert into SimpleTableForTemplateThree values(null, ?, ?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(query);

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setLong(3, value);
            stmt.setBoolean(4, availability);

            stmt.execute();
        } catch (Exception ex) {
            this.errnoNumber = -6;
            return;
        }
        finally {
            log.info("Вставлена строка из таблицы SimpleTableForPatternThree");
        }
    }

    public ArrayList<ArrayList<String>> getDataTableOne(String template) {
        String query = "select * from SimpleTableForTemplateOne";
        PreparedStatement stmt = null;
        Pattern_One pattern_one = null;
        ResultSet resultSet = null;

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listElement = new ArrayList<String>();

        try {
            stmt = conn.prepareStatement(query);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                log.info("Получил строку из таблицы SimpleTableForPatternOne");
                listElement.clear();

                pattern_one = new Pattern_One(0, resultSet.getString(template.split(":")[0]), resultSet.getString(template.split(":")[1]));

                listElement.add(pattern_one.getName());
                listElement.add(pattern_one.getDescription());

                list.add((ArrayList<String>) listElement.clone());
            }
        }
        catch (Exception ex) {
            this.errnoNumber = -6;
            return list;
        }

        return list;
    }

    public ArrayList<ArrayList<String>> getDataTableTwo(String template) {
        String query = "select * from SimpleTableForPatternTwo";
        PreparedStatement stmt = null;
        Pattern_Two pattern_two = null;
        ResultSet resultSet = null;

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listElement = new ArrayList<String>();

        try {
            stmt = conn.prepareStatement(query);

            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                log.info("Получил строку из таблицы SimpleTableForPatternTwo");
                listElement.clear();

                pattern_two = new Pattern_Two(0, resultSet.getString(template.split(":")[0]), resultSet.getString(template.split(":")[1]), resultSet.getLong(template.split(":")[2]));

                //listElement.add(Integer.toString(templateTwo.getId()));
                listElement.add(pattern_two.getName());
                listElement.add(pattern_two.getDescription());
                listElement.add(Long.toString(pattern_two.getValue()));

                list.add((ArrayList<String>) listElement.clone());
            }
        }
        catch (Exception ex) {
            this.errnoNumber = -6;
            return list;
        }

        return list;
    }

    public ArrayList<ArrayList<String>> getDataTableThree(String template) {
        String query = "select * from SimpleTableForPatternThree";
        PreparedStatement stmt = null;
        Pattern_Three pattern_three = null;
        ResultSet resultSet = null;

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> listElement = new ArrayList<String>();

        try {
            stmt = conn.prepareStatement(query);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                log.info("Получил строку из таблицы SimpleTableForPatternThree");
                listElement.clear();

                pattern_three = new Pattern_Three(0, resultSet.getString(template.split(":")[0]), resultSet.getString(template.split(":")[1]), resultSet.getLong(template.split(":")[2]), resultSet.getBoolean(template.split(":")[3]));

                listElement.add(pattern_three.getName());
                listElement.add(pattern_three.getDescription());
                listElement.add(Long.toString(pattern_three.getValue()));
                listElement.add(Boolean.toString(pattern_three.getAvailability()));

                list.add((ArrayList<String>) listElement.clone());
            }
        }
        catch (Exception ex) {
            this.errnoNumber = -6;
            return list;
        }

        return list;
    }

    public void closeConnection() {
        if(conn == null) {
            this.errnoNumber = -11;
            return;
        }

        try {
            this.conn.close();
        }
        catch (Exception ex) {
            this.errnoNumber = -6;
            return;
        }
        finally {
            log.info("Соединение с базой данных закрыто");
        }
    }

    public int getPort(){
        return this.port;
    }

    public String getHost() {
        return this.host;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setPort(int port) {
        this.port = port;
    }
}