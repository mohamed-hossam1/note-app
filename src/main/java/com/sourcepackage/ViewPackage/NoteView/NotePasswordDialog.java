package com.sourcepackage.ViewPackage.NoteView;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NotePasswordDialog extends Dialog<String> {

    private final PasswordField passwordField;

    public NotePasswordDialog() {
        setTitle("Note Password");
        setHeaderText("Enter password to access this note");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        VBox content = new VBox(10);
        content.getChildren().add(passwordField);
        getDialogPane().setContent(content);

        ButtonType loginButtonType = new ButtonType("Enter", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return passwordField.getText();
            }
            return null;
        });
    }
}