package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException{
        System.out.println("test");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("container.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 760, 480);
        stage.setScene(scene);
        stage.show();
    }
}
