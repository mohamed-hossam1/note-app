package com.sourcepackage.ViewPackage.LoginView;
import com.sourcepackage.Applicationpackage.PasswordUtils;


import com.sourcepackage.Applicationpackage.UsersManager;
import com.sourcepackage.Applicationpackage.User;
import com.sourcepackage.ViewPackage.HomeView.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label slogan;

    @FXML
    private Label sloganNote;

    @FXML
    private ImageView exitImage;

    @FXML
    private Label errorLabel;


    public void initialize() {
        DropShadow shadow = new DropShadow(40, Color.valueOf("#509dea"));
        shadow.setRadius(50);
        slogan.setEffect(shadow);
        sloganNote.setEffect(shadow);


        exitImage.setOnMouseClicked(event -> {
            exit();
        });

    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username or Password cannot be empty!");
            return;
        }

        User user = UsersManager.getUserByUsername(username);
        password = PasswordUtils.hashPassword(password);
        if (user != null && password.equals(user.getPassword())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sourcepackage/ViewPackage/HomeView/home.fxml"));
                Parent root = loader.load();

                HomeController homeController = loader.getController();
                homeController.initialize(user);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Home");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showError("Username or Password is wrong");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void handleGoToSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sourcepackage/ViewPackage/SignUpView/signup.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void setOnMouseEntered(javafx.scene.input.MouseEvent mouseEvent) {
        DropShadow shadow = new DropShadow(40, Color.valueOf("#509dea"));
        shadow.setRadius(50);
        if (mouseEvent.getSource() == slogan) {
            slogan.setEffect(shadow);
            slogan.setStyle("-fx-text-fill:#509dea");
        }
        else if(mouseEvent.getSource() == sloganNote){
            sloganNote.setEffect(shadow);
            sloganNote.setStyle("-fx-text-fill:#509dea");
        }
    }

    @FXML
    private void setOnMouseExited(javafx.scene.input.MouseEvent mouseEvent) {
        DropShadow shadow = new DropShadow(40, Color.valueOf("#509dea"));
        shadow.setRadius(50);
        if (mouseEvent.getSource() == slogan) {
            slogan.setEffect(shadow);
            slogan.setStyle("-fx-text-fill:#141E30");
        }else if(mouseEvent.getSource() == sloganNote){
            sloganNote.setEffect(shadow);
            sloganNote.setStyle("-fx-text-fill:#141E30");
        }
    }

    public void exit(){
        System.exit(0);
    }






}
