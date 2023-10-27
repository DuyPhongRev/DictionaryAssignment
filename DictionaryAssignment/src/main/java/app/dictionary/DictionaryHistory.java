package app.dictionary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DictionaryHistory {
    private Connection connection;
    private ArrayList<String> default_history = new ArrayList<>();
    DictionaryHistory() {

    }

    public void insertHistoryListFromDB() {
        try {
            if (!default_history.isEmpty()) {
                default_history.clear();
            }
            PreparedStatement pre_state = connection.prepareStatement("SELECT word FROM avHistory");
            ResultSet resultSet = pre_state.executeQuery();
            while (resultSet.next()) {
                default_history.add(0, resultSet.getString(1));
            }
        } catch (Exception e) {
            System.err.println("Cannot insert history list from database");
            e.printStackTrace();
        }
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
        if (default_history.contains(foundWord)) {
            default_history.remove(foundWord);
            System.out.println("hehe");
            default_history.add(0, foundWord);
            deleteWordFromHistoryDatabase(foundWord);

        } else {
            default_history.add(0, foundWord);
        }
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

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public ArrayList<String> getDefault_history() {
        insertHistoryListFromDB();
        return default_history;
    }
}
