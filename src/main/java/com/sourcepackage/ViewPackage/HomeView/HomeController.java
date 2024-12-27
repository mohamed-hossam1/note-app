package com.sourcepackage.ViewPackage.HomeView;

import com.sourcepackage.Applicationpackage.Note;
import com.sourcepackage.Applicationpackage.PasswordUtils;
import com.sourcepackage.Applicationpackage.SecureNote;
import com.sourcepackage.Applicationpackage.User;
import com.sourcepackage.ViewPackage.AddNodeView.AddNodeController;
import com.sourcepackage.ViewPackage.NoteView.NoteController;
import com.sourcepackage.ViewPackage.NoteView.NotePasswordDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class HomeController {

    @FXML
    private Label Linke;

    @FXML
    private Label userNameLabel;

    @FXML
    private Button addNode;

    @FXML
    private Button deleteNode;

    @FXML
    private ListView<Note> notesListView;

    private ObservableList<Note> notesList;


    @FXML
    private ImageView exitImage;

    private User user;


    public void initialize(User user) {
        this.user = user;

        userNameLabel.setText(user.getUserName());

        notesList = FXCollections.observableArrayList(user.folder.getNotes());
        notesListView.setItems(notesList);

        notesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Note note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null || note.getNoteName() == null) {
                    setText(null);
                } else {
                    setText(note.getNoteName());
                }
            }
        });

        exitImage.setOnMouseClicked(event -> exit());
    }


    @FXML
    private void addNode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sourcepackage/ViewPackage/AddNodeView/addNode.fxml"));

            Parent root = loader.load();

            AddNodeController addNodeController = loader.getController();

            addNodeController.initialize(user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading the addNode page.");
        }
    }

    @FXML
    private void deleteNode() {
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            notesList.remove(selectedNote);
            user.folder.deleteNote(selectedNote);
        }

    }

    @FXML
    private void openNode() {
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            if (selectedNote instanceof SecureNote) {
                NotePasswordDialog dialog = new NotePasswordDialog();
                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    String hashedInput = PasswordUtils.hashPassword(result.get());
                    hashedInput = PasswordUtils.hashPassword(hashedInput);
                    if (!hashedInput.equals(((SecureNote) selectedNote).getNotePassword())) {
                        return;
                    }
                } else {
                    return;
                }
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sourcepackage/ViewPackage/NoteView/note.fxml"));
                Parent root = loader.load();
                NoteController controller = loader.getController();
                controller.initialize(user, selectedNote);
                Stage stage = (Stage) notesListView.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void exit() {
        System.exit(0);
    }

    @FXML
    private void signOut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sourcepackage/ViewPackage/LoginView/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading the login page.");
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
    }

    @FXML
    private void setOnMouseExited(javafx.scene.input.MouseEvent mouseEvent) {
        DropShadow shadow = new DropShadow(40, Color.valueOf("#509dea"));
        shadow.setRadius(50);
        if (mouseEvent.getSource() == Linke) {
            Linke.setEffect(shadow);
            Linke.setStyle("-fx-text-fill:#141E30");
        }
    }
}
