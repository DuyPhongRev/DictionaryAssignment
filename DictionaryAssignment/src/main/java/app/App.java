package app;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Pos;
import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("container.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("logo.png")));
        primaryStage.setTitle("Dictionary");

        ImageView loadingImageView = new ImageView(new Image(App.class.getResourceAsStream("loadingData.gif")));
        StackPane loadingLayout = new StackPane(loadingImageView);
        loadingLayout.setAlignment(Pos.CENTER);
        Scene loadingScene = new Scene(loadingLayout, 498, 356);
        Stage loadingStage = new Stage(StageStyle.UNDECORATED);
        loadingStage.setScene(loadingScene);

        Task<Void> loadingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(2403);
                return null;
            }
        };

        loadingStage.show();

        loadingTask.setOnSucceeded(event -> {
            loadingStage.hide();
            primaryStage.show();
        });

        Thread loadingThread = new Thread(loadingTask);
        loadingThread.start();
    }
}
