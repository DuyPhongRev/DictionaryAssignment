package app.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class  GeneratePhonetics {
    private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    public static String getPhonetics(String word) throws IOException {
        String urlString = API_URL + word;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        // Đóng kết nối
        in.close();
        conn.disconnect();

        // Phân tích cú pháp kết quả JSON
        JSONArray jsonArray = new JSONArray(content.toString());
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONArray phoneticsArray = jsonObject.getJSONArray("phonetics");

        for (int i = 0; i < phoneticsArray.length(); i++) {
            JSONObject phoneticObject = phoneticsArray.getJSONObject(i);
            if (phoneticObject.has("text")) {
                return phoneticObject.getString("text");

            }
        }
        return "Pronunciation not found!";
    }
}
