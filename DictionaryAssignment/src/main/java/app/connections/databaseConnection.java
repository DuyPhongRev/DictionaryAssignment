package app.connections;

import java.sql.*;

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
    public String gameQueryWord() {
        try {
            PreparedStatement pre_state = connection.prepareStatement("SELECT word FROM av WHERE LENGTH(word) = 5 ORDER BY RANDOM() LIMIT 1");
            ResultSet resultSet = pre_state.executeQuery();
            return resultSet.getString("word");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
