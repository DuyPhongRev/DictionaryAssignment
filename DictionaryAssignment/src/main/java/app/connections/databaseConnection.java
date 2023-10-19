package app.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class databaseConnection {
    public static Connection connection;

    public databaseConnection() {connectDatabase();}

    public static void connectDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Cannot connect the forName class");
        }

        //Connect database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:dictionary_database.db");
            if (connection != null) {
                System.out.println(" Database is connected!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        databaseConnection myDb = new databaseConnection();

    }
}
