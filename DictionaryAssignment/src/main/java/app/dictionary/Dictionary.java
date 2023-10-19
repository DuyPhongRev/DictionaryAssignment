package app.dictionary;
import app.connections.databaseConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class Dictionary {
    //    TreeMap<String, String> WordList = new TreeMap<>();
    private final Connection connection;
    Dictionary() {
        databaseConnection.connectDatabase();
        this.connection = databaseConnection.connection;
    }
//    public TreeMap<String, String> getWordList() {
//        return WordList;
//    }

//    public int SizeOfList() {
//        return WordList.size();
//    }

    public void insertWordDatabase() {
        PreparedStatement pre_state;
        try {
            pre_state = connection.prepareStatement("SELECT word, description FROM av group by word");
            ResultSet resultSet = pre_state.executeQuery();

            while (resultSet.next()) {
//                Word newVocab = new Word(resultSet.getString(1), resultSet.getString(2));
//                this.addNewWord(newVocab.getWord_target(), newVocab.getWord_expain());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void initDictionary() {
        String fileName = "./b.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String word = parts[0].trim();
                    String meaning = parts[1].trim();
                    this.addNewWord(word, meaning);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Cannot read the file " + e.getMessage());
        }
    }

    public void printAllWords() {
        TreeMap<String, String> word_list = new TreeMap<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT word, description FROM av");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                word_list.put(rs.getString("word"), rs.getString("description"));
            }
            for (Map.Entry<String, String> entry : word_list.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (Exception e) {

            System.err.println("Cannot print the list of words");
            e.printStackTrace();
        }
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
//        boolean ret = true;
//        if (target != "") {
//            target = target.toLowerCase();
//        } else {
//            System.err.println("Could not find the word!");
//            return false;
//        }
//        if (explain != "") {
//            explain = explain.toLowerCase();
//        } else {
//            System.err.println("The word you have added has no meaning!");
//            return false;
//        }
//
//        WordList.put(target, explain);
//        return true;

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

//        if (WordList.containsKey(key)) {
//            WordList.remove(key);
//        } else {
//            System.err.println("Cannot find the word to be deleted");
//        }
    }

    public void changeWord(String key, String explain) {
//        key = key.toLowerCase();
//        explain = explain.toLowerCase();
//        if (WordList.containsKey(key)) {
//            WordList.replace(WordList.get(key), explain);
//        } else {
//            System.err.println("cannot find the word to be replaced");
//        }
        try{
            PreparedStatement stmt = connection.prepareStatement("UPDATE av SET description = ? WHERE word = ?");
            stmt.setString(1, explain);
            stmt.setString(2, key);
            stmt.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

//    public ArrayList<String> FindAdjacency(String key) {
//        int cnt = 5; // find the top 10 nearest words
//        key = key.toLowerCase();
//        ArrayList<String> list = new ArrayList<String>();
//        list.add(key);
//        String tmp = key;
//        while(cnt > 0) {
//            try {
//                tmp = WordList.lowerKey(tmp);
//            } catch (NullPointerException e)  {
//                break;
//            }
//            if (tmp != null) {
//                list.add(tmp);
//            }
//            cnt--;
//        }
//        cnt = 5;
//        while(cnt > 0) {
//            try {
//                tmp = WordList.higherKey(tmp);
//            } catch (NullPointerException e)  {
//                break;
//            }
//            if (tmp != null) {
//                list.add(tmp);
//            }
//            cnt--;
//        }
//
//        return list;
//    }

    public String LookUp(String word) {
        word = word.toLowerCase();

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

//        if (WordList.containsKey(word)) {
//            return "here is the result: " + word + ": " + WordList.get(word);
//        } else {
//            return "Cannot find \"" + word + "\" in the database system!!.";
//        }
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
}
