package app.dictionary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.dictionary.HelperAlgorithm.convertToHTML;

public class DictionaryFavourite {
    private Connection connection;
    private ArrayList<String> default_favourite = new ArrayList<>();
    DictionaryFavourite() {

    }

    public void insertFavouriteListFromDB() {
        try {
            if (!default_favourite.isEmpty()) {
                default_favourite.clear();
            }
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT word, html FROM avFavourite");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                default_favourite.add(0, resultSet.getString(1));
            }
        } catch(Exception e) {
            System.err.println("Cannot insert favourite list from database");
            e.printStackTrace();
        }
    }

    public String LookUpInFavourite(String word) {
        try{
            PreparedStatement stmt = connection.prepareStatement("SELECT html FROM avFavourite WHERE word = ?");
            stmt.setString(1, word);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("html");
            } else {
                return "Cannot find \"" + word + "\" in the database system!!.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Word not found!";
    }

    public ArrayList<String> getArrayRelevantWordInFavourite(String word) {
        word = word.toLowerCase();
        word = word.trim();
        ArrayList<String> word_list = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT word FROM avFavourite WHERE word LIKE '" + word + "%' group by word");
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

    public void saveWordToFavouriteDB(String foundWord) {
        foundWord = foundWord.trim();
        if (default_favourite.contains(foundWord)) {
//            do nothing
        } else {
            default_favourite.add(0, foundWord);
            String sql = "INSERT INTO avFavourite(word, html, description, pronounce) SELECT word, html, description, pronounce FROM av WHERE word = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, foundWord);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteWordFromFavouriteDatabase(String foundWord) {
        foundWord = foundWord.trim();
        default_favourite.remove(foundWord);
        String sql = "DELETE FROM avFavourite WHERE word = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, foundWord);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeWord(String wordOld, String wordNew, String wordType, String pronunciation,
                           String description) {
        String html = convertToHTML(wordNew, pronunciation, description, wordType);
        String sql = "UPDATE avFavourite SET description = ?, html = ?, pronounce = ?, word = ? WHERE word = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, description);
            preparedStatement.setString(2, html);
            preparedStatement.setString(3, pronunciation);
            preparedStatement.setString(4, wordNew);
            preparedStatement.setString(5, wordOld);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        insertFavouriteListFromDB();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<String> getDefault_favourite() {
        insertFavouriteListFromDB();
        return default_favourite;
    }

    public ArrayList<String> getFavouriteList() {
        return default_favourite;
    }
    public Connection getConnection() {
        return connection;
    }
}
