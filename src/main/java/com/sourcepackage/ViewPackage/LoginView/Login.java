package com.sourcepackage.ViewPackage.LoginView;

import com.sourcepackage.Applicationpackage.UsersManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.io.IOException;


public class Login extends Application {

    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws IOException {

        new UsersManager();

        Scene scene = new Scene(new FXMLLoader(getClass().getResource("login.fxml")).load(), 1000, 600);

        stage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        stage.setResizable(false);

        scene.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        stage.setTitle("NoteView Taking");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
