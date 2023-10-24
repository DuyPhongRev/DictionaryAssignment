package app.dictionary;
import app.connections.databaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;


public class Dictionary {
    private final Connection connection;
    Dictionary() {
        databaseConnection.connectDatabase();
        this.connection = databaseConnection.connection;
    }

    public void insertWordDatabase() {
        PreparedStatement pre_state;
        try {
            pre_state = connection.prepareStatement("SELECT word, description FROM av group by word");
            ResultSet resultSet = pre_state.executeQuery();
            while (resultSet.next()) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> insertHistoryListFromDB() {
        ArrayList<String> history = new ArrayList<>();
        try {
            PreparedStatement pre_state = connection.prepareStatement("SELECT word FROM avHistory");
            ResultSet resultSet = pre_state.executeQuery();
            while (resultSet.next()) {
                history.add(0, resultSet.getString(1));
            }
        } catch (Exception e) {
            System.err.println("Cannot insert history list from database");
            e.printStackTrace();
        }
        return history;
    }

    public boolean addNewWord(String target, String explain) {
        target = target.toLowerCase();
        explain = explain.toLowerCase();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO av (word, description) VALUES (?, ?)");
            stmt.setString(1, target);
            stmt.setString(2, explain);
            stmt.executeUpdate();
            System.out.println("Added word successfully!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeWord(String key) {
        try{
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM av WHERE word = ?");
            stmt.setString(1, key);
            stmt.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot delete the word due to some errors");
        }
    }

    public void changeWord(String key, String explain) {
        try{
            PreparedStatement stmt = connection.prepareStatement("UPDATE av SET description = ? WHERE word = ?");
            stmt.setString(1, explain);
            stmt.setString(2, key);
            stmt.executeUpdate();

        } catch(SQLException e) {
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

    public String LookUpInHist(String word) {
        try{
            PreparedStatement stmt = connection.prepareStatement("SELECT html FROM avHistory WHERE word = ?");
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

    public TreeMap<String, String> findRelevantWord(String word) {
        word = word.toLowerCase();
        word = word.trim();
        TreeMap<String, String> word_list = new TreeMap<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT word, description FROM av WHERE word LIKE '" + word + "%' group by word");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                word_list.put(rs.getString("word"), rs.getString("description"));
            }
        } catch (Exception e) {
            System.err.println("Cannot find the word");
            e.printStackTrace();
        }
        return word_list;
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

    public ArrayList<String> getArrayRelevantWordInHist(String word) {
        word = word.toLowerCase();
        word = word.trim();
        ArrayList<String> word_list = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT word FROM avHistory WHERE word LIKE '" + word + "%' group by word");
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

    public void saveWordToHistoryDatabase(String foundWord) {
        foundWord = foundWord.trim();
        String sql = "INSERT INTO avHistory(word, html, description, pronounce) SELECT word, html, description, pronounce FROM av WHERE word = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, foundWord);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWordFromHistoryDatabase(String foundWord) {
        foundWord = foundWord.trim();
        String sql = "DELETE FROM avHistory WHERE word = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, foundWord);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
