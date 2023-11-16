package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Pos;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class App extends Application {
    public static Stage AppStage;
    private TrayIcon trayIcon;
    private SystemTray tray;
    public static final double WIDTH = 1000;
    public static final double HEIGHT = 750;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        tray = SystemTray.getSystemTray();
        try {
            InputStream stream = App.class.getResourceAsStream("logo.png");
            assert stream != null;
            BufferedImage bufferedImage = ImageIO.read(stream);

            java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
            awtImage = awtImage.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
            trayIcon = new TrayIcon(awtImage);
            SystemTray.getSystemTray().add(trayIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        trayIcon.setToolTip("Dictionary");

        // Xử lý sự kiện click vào tray icon
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
//                    System.out.println("Double click");
                    Platform.runLater(() -> {
                        primaryStage.setIconified(false);
                        primaryStage.toFront();
                        primaryStage.setResizable(false);
                        primaryStage.show();

                    });
                }
            }

        });

        // Tạo menu chuột phải cho tray icon
        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.exit();
                tray.remove(trayIcon);
            }
        });
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);
        // when click X, hide the window
        Platform.setImplicitExit(false);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("container.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("logo.png")));
        primaryStage.setTitle("Dictionary");
        primaryStage.setMaxWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);
        primaryStage.setResizable(false);

        ImageView loadingImageView = new ImageView(new Image(App.class.getResourceAsStream("loadingData.gif")));
        StackPane loadingLayout = new StackPane(loadingImageView);
        loadingLayout.setAlignment(Pos.CENTER);
        Scene loadingScene = new Scene(loadingLayout, WIDTH, HEIGHT);
        Stage loadingStage = new Stage(StageStyle.UNDECORATED);
        loadingStage.setScene(loadingScene);

        AppStage = primaryStage;

        Task<Void> loadingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(2000);
                return null;
            }
        };

        loadingStage.show();

        loadingTask.setOnSucceeded(event -> {
            primaryStage.show();
            loadingStage.hide();
        });

        Thread loadingThread = new Thread(loadingTask);
        loadingThread.start();
    }
}
