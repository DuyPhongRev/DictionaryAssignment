package app.connections;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslateVoiceAPIs {
    public static final String GOOGLE_TRANSLATE_AUDIO = "https://translate.google.com/translate_tts?";

    private static String generateURL(String text, String language) {
        return GOOGLE_TRANSLATE_AUDIO + "?ie=UTF-8"
                + "&q=" + URLEncoder.encode(text, StandardCharsets.UTF_8)
                + "&tl=" + language
                + "&client=tw-ob";
    }

    public static void getAudio(String text, String destLang)
            throws IOException, JavaLayerException {

        String urlString = generateURL(text, destLang);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        InputStream audioSrc = connection.getInputStream();
        new Player(new BufferedInputStream(audioSrc)).play();
    }
}
