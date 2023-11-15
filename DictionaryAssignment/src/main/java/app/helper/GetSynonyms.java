package app.helper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class GetSynonyms {

    public static List<String> getSynonyms(String word) throws IOException {
        if (word == null || word.isEmpty() || word.contains(" ") || word.contains("-")) {
            return new ArrayList<>();
        }
        String apiKey = "299e02427cmsh5a9a506e912b905p15d1b0jsn927f063c763f";
        String apiUrl = "https://synonyms-word-info.p.rapidapi.com/v1/word/synonyms?str=%5C%22" + word + "%5C%22";

        URI uri = URI.create(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-RapidAPI-Key", apiKey);
        connection.setRequestProperty("X-RapidAPI-Host", "synonyms-word-info.p.rapidapi.com");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            List<String> synonyms = parseSynonyms(response.toString());
            return synonyms;
        } else {
            System.out.println("Error: " + responseCode);
            return new ArrayList<>();
        }
    }

    private static List<String> parseSynonyms(String jsonResponse) {
        List<String> synonyms = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONArray synonymsArray = dataObject.getJSONArray("synonyms");

        for (int i = 0; i < synonymsArray.length(); i++) {
            JSONArray synonymData = synonymsArray.getJSONArray(i);
            String synonym = synonymData.getString(0); // Lấy từ đồng nghĩa từ phần tử đầu tiên của mảng
            synonyms.add(synonym);
        }

        return synonyms;
    }

    public static void main(String[] args) {
        try {
            String word = "medical";
            List<String> synonyms = getSynonyms(word);
            System.out.println("Synonyms for " + word + ": " + synonyms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
