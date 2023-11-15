package app.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageEncode {
    public static String imageToBase64(String ImagePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(ImagePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static void base64ToImage(String base64Image) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        String workingDir = System.getProperty("user.dir");
        // save the image to the working directory
        String destinationPath = workingDir + "/DictionaryAssignment/src/main/resources/app/result.png";
        Files.write(Paths.get(destinationPath), decodedBytes);
    }
}
