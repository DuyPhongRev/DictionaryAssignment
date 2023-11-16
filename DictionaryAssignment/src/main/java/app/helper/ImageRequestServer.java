package app.helper;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import static app.helper.ImageEncode.imageToBase64;
import static app.helper.ImageEncode.base64ToImage;

public class ImageRequestServer {
    public static final URL SERVER_URL;

    static {
        try {
            SERVER_URL = new URL("http://127.0.0.1:5000");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendImageBase64ToServer(String ImagePath) throws IOException {
        String base64Data = imageToBase64(ImagePath);
        URL ImageServerUrl = new URL(SERVER_URL + "/image");
        HttpURLConnection conn = (HttpURLConnection) ImageServerUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        String jsonData = "{\"image_data\":\"" + base64Data + "\"}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            responseLine = response.toString();
            System.out.println("Response from server: " + responseLine);
            responseLine = responseLine.substring(15, responseLine.length() - 2);
            try {
                base64ToImage(responseLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

    }

    public static void sendToServer() throws IOException {
        String workingDir = System.getProperty("user.dir");
        // save the image to the working directory
        String destinationPath = workingDir + "/DictionaryAssignment/src/main/resources/app/input.png";
        sendImageBase64ToServer(destinationPath);
    }

}