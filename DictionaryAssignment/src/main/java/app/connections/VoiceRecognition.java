package app.connections;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class VoiceRecognition {
    public static String APIVoiceRecognitionRequest() throws Exception {
        String filePath = "DictionaryAssignment/src/main/java/app/connections/recordedAudio.wav";
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
        return processJsonResponse(response.body());
    }

    private static String processJsonResponse(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray resultsArray = jsonObject.getAsJsonArray("results");
        if (resultsArray != null && resultsArray.size() > 0) {
            JsonObject firstResult = resultsArray.get(0).getAsJsonObject();
            JsonArray alternativesArray = firstResult.getAsJsonArray("alternatives");
            if (alternativesArray != null && alternativesArray.size() > 0) {
                JsonObject firstAlternative = alternativesArray.get(0).getAsJsonObject();
                String transcript = firstAlternative.get("transcript").getAsString();
                return transcript;
            }
        }
        return "";
    }
}