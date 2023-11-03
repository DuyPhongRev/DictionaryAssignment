package app.dictionary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Dictionary {
    private Connection connection;
    private ArrayList<String> default_dictionary = new ArrayList<>();
    public Dictionary() {
    }

    public void insertWordListFromDB() {
        try {
            PreparedStatement pre_state = connection.prepareStatement("SELECT word FROM av");
            ResultSet resultSet = pre_state.executeQuery();
            while (resultSet.next()) {
                default_dictionary.add(resultSet.getString(1));
            }
        } catch (Exception e) {
            System.err.println("Cannot insert word list from database");
            e.printStackTrace();
        }
    }


    public void changeWord(String key, String explain) {
        try{
            PreparedStatement stmt = connection.prepareStatement("UPDATE av SET html = ? WHERE word = ?");
            stmt.setString(1, explain);
            stmt.setString(2, key);
            stmt.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWordFromDictionaryDatabase(String foundWord) {
        foundWord = foundWord.trim();
        default_dictionary.remove(foundWord);
        String sql = "DELETE FROM av WHERE word = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, foundWord);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String LookUp(String word) {
        try{
            PreparedStatement stmt = connection.prepareStatement("SELECT html FROM av WHERE word = ?");
            stmt.setString(1, word);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("html");
            } else {
                return "Cannot find \"" + word + "\" in the database system!!.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Word not found!";
    }

    public ArrayList<String> getArrayRelevantWord(String word) {
        word = word.toLowerCase();
        word = word.trim();
        ArrayList<String> word_list = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT word FROM av WHERE word LIKE '" + word + "%' group by word");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                word_list.add(rs.getString("word"));
            }
        } catch (Exception e) {
            System.err.println("Cannot find the word");
            e.printStackTrace();
        }
        return word_list;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public ArrayList<String> getDefault_dictionary() {
        return default_dictionary;
    }
}
