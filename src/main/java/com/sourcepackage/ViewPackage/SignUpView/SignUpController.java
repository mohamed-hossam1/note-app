package com.sourcepackage.ViewPackage.SignUpView;

import com.sourcepackage.Applicationpackage.PasswordUtils;
import com.sourcepackage.Applicationpackage.UsersManager;
import com.sourcepackage.Applicationpackage.User;
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

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label slogan;

    @FXML
    private Label sloganNote;

    @FXML
    private ImageView exitImage;


    public void initialize() {
        setupUI();

    }

    private void setupUI() {
        DropShadow shadow = new DropShadow(40, Color.valueOf("#509dea"));
        shadow.setRadius(50);
        slogan.setEffect(shadow);
        sloganNote.setEffect(shadow);
        exitImage.setOnMouseClicked(event -> exit());

    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username or Password cannot be empty!");
            return;
        }

        if (!PasswordUtils.isValidPassword(password)) {
            showError("Password must contain at least 8 characters,\nincluding uppercase,lowercase,\nand number!");
            return;
        }

        if (password.equals(username)) {
            showError("Password cannot be the same as username!");
            return;
        }

        User existingUser = UsersManager.getUserByUsername(username);
        if (existingUser != null) {
            showError("Username already exists!");
            return;
        }
        String hashedPassword = PasswordUtils.hashPassword(password);
        User newUser = new User(username, hashedPassword);
        UsersManager.addUser(newUser);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sourcepackage/ViewPackage/LoginView/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (IOException e) {
            showError("Error loading login page");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }



    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sourcepackage/ViewPackage/LoginView/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
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
