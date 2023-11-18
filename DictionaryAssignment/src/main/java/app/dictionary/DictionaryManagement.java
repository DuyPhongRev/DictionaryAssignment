package app.dictionary;

import app.connections.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DictionaryManagement {
    private Dictionary dict_main;
    private DictionaryHistory dic_history;
    private DictionaryFavourite dict_favourite;
    private final Connection connection;
    public DictionaryManagement() {
        dict_main = new Dictionary();
        dic_history = new DictionaryHistory();
        dict_favourite = new DictionaryFavourite();

        DatabaseConnection.connectDatabase();
        this.connection = DatabaseConnection.connection;

        dict_main.setConnection(this.connection);
        dic_history.setConnection(this.connection);
        dict_favourite.setConnection(this.connection);
        dict_main.insertWordListFromDB();
        dic_history.insertHistoryListFromDB();
        dict_favourite.insertFavouriteListFromDB();
    }

    public String searchAct(String word) throws SQLException {
        String ans = "";
        try {
            connection.setAutoCommit(false);
            ans = dict_main.LookUp(word);
            dic_history.saveWordToHistoryDatabase(word);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();

        } finally {
            connection.setAutoCommit(true);
//            connection.close();
        }
        return ans;
    }

    public void deleteAct(String word) throws SQLException {
        try {
            connection.setAutoCommit(false);
            dict_main.deleteWordFromDictionaryDatabase(word);
            dic_history.deleteWordFromHistoryDatabase(word);
            dict_favourite.deleteWordFromFavouriteDatabase(word);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }
    public void addToFavourite(String word) {
        dict_favourite.saveWordToFavouriteDB(word);
    }

    public boolean addNewWord(String target, String explain) {
        target = target.toLowerCase();
        explain = explain.toLowerCase();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO av (word, html) VALUES (?, ?)");
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

    public Dictionary getDictMain() {
        return dict_main;
    }

    public DictionaryHistory getDicHistory() {
        return dic_history;
    }

    public DictionaryFavourite getDictFavourite() {
        return dict_favourite;
    }
}
