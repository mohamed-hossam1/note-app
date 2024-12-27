package com.sourcepackage.ViewPackage.AddNodeView;

import com.sourcepackage.Applicationpackage.*;
import com.sourcepackage.ViewPackage.HomeView.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class AddNodeController {

    @FXML
    private TextField notenameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label Linke;

    @FXML
    private Label Note;

    @FXML
    private ImageView exitImage;

    private User user;

    private UserFolder userFolder;

    private Note newNote;

    @FXML
    private Label errorLabel;


    public void initialize(User user) {
        this.user = user;
        this.userFolder = user.folder;

        DropShadow shadow = new DropShadow(40, Color.valueOf("#509dea"));
        shadow.setRadius(50);
        Linke.setEffect(shadow);
        Note.setEffect(shadow);

        exitImage.setOnMouseClicked(event -> {
            exit();
        });
    }

    @FXML
    private void loginToNote(ActionEvent event) {
        String noteName = notenameField.getText().trim();
        String password = passwordField.getText().trim();

        if (noteName.isEmpty()) {
            showError("Note name cannot be empty!");
            return;
        }
        if (!password.isEmpty()) {
            password = PasswordUtils.hashPassword(password);
            if (password.equals(user.getPassword())) {
                showError("The password cannot be the user's password.");
                return;
            }
        }

        if (userFolder.ValidName(noteName)) {
            newNote = password.isEmpty() ?
                    userFolder.createNote(noteName) :
                    userFolder.createSecureNote(noteName, password);

            handleBack(null);
        } else {
            showError("Note name already exists!");
        }
    }
    @FXML
    private void handleBack(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sourcepackage/ViewPackage/HomeView/home.fxml"));
            Parent root = loader.load();

            HomeController homeController = loader.getController();
            homeController.initialize(user);
            Stage stage = (Stage) notenameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void setOnMouseEntered(javafx.scene.input.MouseEvent mouseEvent) {
        DropShadow shadow = new DropShadow(40, Color.valueOf("#509dea"));
        shadow.setRadius(50);
        if (mouseEvent.getSource() == Linke) {
            Linke.setEffect(shadow);
            Linke.setStyle("-fx-text-fill:#509dea");
        }
        else if(mouseEvent.getSource() == Note){
            Note.setEffect(shadow);
            Note.setStyle("-fx-text-fill:#509dea");
        }
    }

    @FXML
    private void setOnMouseExited(javafx.scene.input.MouseEvent mouseEvent) {
        DropShadow shadow = new DropShadow(40, Color.valueOf("#509dea"));
        shadow.setRadius(50);
        if (mouseEvent.getSource() == Linke) {
            Linke.setEffect(shadow);
            Linke.setStyle("-fx-text-fill:#141E30");
        }else if(mouseEvent.getSource() == Note){
            Note.setEffect(shadow);
            Note.setStyle("-fx-text-fill:#141E30");
        }
    }

    public void exit(){
        System.exit(0);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }


    public Note getNote() {
        return newNote;
    }


}
