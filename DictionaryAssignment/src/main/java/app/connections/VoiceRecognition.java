package app.connections;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class VoiceRecognition {
    public static void main(String[] args) throws Exception {
        String filePath = "test.mp4a";
        String apiKey = "AIzaSyALFroO5S4PF2NwHM26bSO2MqDc823PQhM";

        Path path = Paths.get(filePath);
        byte[] audioData = Files.readAllBytes(path);

        String endpoint = "https://speech.googleapis.com/v1/speech:recognize?key=" + apiKey;

        String jsonPayload = "{"
                + "\"config\": {"
                + "\"encoding\":\"LINEAR16\","
                + "\"sampleRateHertz\":16000,"
                + "\"languageCode\":\"en\""
                + "},"
                + "\"audio\": {"
                + "\"content\":\"" + java.util.Base64.getEncoder().encodeToString(audioData) + "\""
                + "}"
                + "}";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

    }
}