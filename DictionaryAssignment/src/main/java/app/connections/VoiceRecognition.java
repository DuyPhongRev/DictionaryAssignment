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
        String filePath = "C:\\DictionaryAssignment\\DictionaryAssignment\\src\\main\\java\\app\\connections\\test_out.m4a";
        String apiKey = "AIzaSyALFroO5S4PF2NwHM26bSO2MqDc823PQhM";

        AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }

        try {
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            System.out.println("Recording...");

            AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);

            Thread.sleep(5000);

            System.out.println("Recording finished.");

            targetDataLine.stop();
            targetDataLine.close();
            
            File audioFile = new File(filePath);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
            audioInputStream.close();

            System.out.println("Audio saved to: " + filePath);
        } catch (LineUnavailableException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

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