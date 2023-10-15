package app.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslateTextAPIs {
    public static final String GOOGLE_TRANSLATE_URL = "https://translate.google.com/translate_a/t?";

    public static String translate(String text, LANGUAGE srcLang, LANGUAGE destLang) throws IOException {
        URL url = new URL(generateURL(srcLang.toString(), destLang.toString(), text));
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString().trim().replace("\"", "");
    }

    private static String generateURL(String sourceLanguage, String targetLanguage, String text) {
        return GOOGLE_TRANSLATE_URL + "client=gtrans"
                + "&sl=" + sourceLanguage
                + "&tl=" + targetLanguage
                + "&q=" + URLEncoder.encode(text, StandardCharsets.UTF_8);
    }

    public enum LANGUAGE {
        ENGLISH("en"), VIETNAMESE("vi"), AUTO("auto");

        private final String lang;

        LANGUAGE(String lang) {
            this.lang = lang;
        }

        @Override
        public String toString() {
            return this.lang;
        }
    }
}