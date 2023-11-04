package app.dictionary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.dictionary.HelperAlgorithm.BinarySearch;


public class Dictionary {
    private Connection connection;
    private ArrayList<String> default_dictionary = new ArrayList<>();
    public Dictionary() {
    }

    public void insertWordListFromDB() {
        try {
            default_dictionary.clear();
            PreparedStatement pre_state = connection.prepareStatement("SELECT word FROM av group by word");
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

    public boolean addWordToDictionaryDatabase(String word, String pron, String des, String type) {
        word = word.toLowerCase();
        word = word.trim();
        try {
            if (checkContains(word)) {
                return false;
            }
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO av (word, html, description, pronounce) VALUES (?, ?, ?, ?)");
            stmt.setString(1, word);
            stmt.setString(2, convertToHTML(word, pron, des, type));
            stmt.setString(3, des);
            stmt.setString(4, pron);
            stmt.executeUpdate();
            insertWordListFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String convertToHTML(String addWord, String addPron, String addDescription, String type) {
        if (addPron.isEmpty()) {
            addPron = "/No pronunciation/";
        }
        if (addDescription.isEmpty()) {
            addDescription = "/No description/";
        }
        if (type.isEmpty()) {
            type = "/No type/";
        }
        if (addWord.isEmpty()) {
            addWord = "no word";
        }
        StringBuilder convertHTML = new StringBuilder();
        convertHTML.append("<h1>").append(addWord).append("</h1>");
        convertHTML.append("<h3><i>/").append(addPron).append("/</i></h3>");
        convertHTML.append("<h2>").append(type).append("</h2>");
        convertHTML.append("<ul><li>").append(addDescription).append("</li></ul>");
        return convertHTML.toString();
    }

    public boolean checkContains(String word) {
        word = word.toLowerCase();
        word = word.trim();
        return BinarySearch(default_dictionary, word);
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
