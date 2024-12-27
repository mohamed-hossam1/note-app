package com.sourcepackage.ViewPackage.NoteView;

import com.sourcepackage.Applicationpackage.*;
import com.sourcepackage.ViewPackage.HomeView.HomeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javafx.application.Platform;

import com.sourcepackage.Applicationpackage.Note;
import com.sourcepackage.Applicationpackage.User;

public class NoteController {
    @FXML private Label noteTitle;
    @FXML private HTMLEditor htmlEditor;
    @FXML private Canvas drawingCanvas;
    @FXML private ColorPicker colorPicker;
    @FXML private Slider brushSize;
    @FXML private FlowPane imageGallery;
    @FXML private ListView<String> audioList;
    @FXML private Button recordButton;
    @FXML private ToggleButton editModeToggle;
    @FXML private Button textView;
    @FXML private Button imageView;
    @FXML private Button drawView;
    @FXML private Button audioView;
    @FXML private VBox textEditorContainer;
    @FXML private VBox drawingContainer;
    @FXML private VBox imageContainer;
    @FXML private VBox audioContainer;
    @FXML private ImageView backButton;

    private User currentUser;
    private Note currentNote;
    private GraphicsContext gc;
    private AudioFormat format;
    private TargetDataLine line;
    private boolean isRecording = false;
    private ByteArrayOutputStream recordingStream;

    public void initialize(User user, Note note) {
        this.currentUser = user;
        this.currentNote = note;
        setupUI();
        setupDrawingCanvas();
        setupAudioRecorder();
        loadExistingContent();
    }

    private void setupUI() {
        noteTitle.setText(currentNote.getNoteName());
        colorPicker.setValue(Color.BLACK);
        editModeToggle.setSelected(true);
        showTextEditor();
    }

    private void setupDrawingCanvas() {
        gc = drawingCanvas.getGraphicsContext2D();
        drawingCanvas.setOnMouseDragged(e -> {
            if (editModeToggle.isSelected()) {
                double size = brushSize.getValue();
                Color color = colorPicker.getValue();
                gc.setFill(color);
                gc.fillOval(e.getX() - size/2, e.getY() - size/2, size, size);

                currentNote.addSketchPoint(new Sketch(
                        e.getX(), e.getY(), size,
                        color.getRed(), color.getGreen(),
                        color.getBlue(), color.getOpacity()
                ));
                saveNote();
            }
        });
    }

    private void setupAudioRecorder() {
        recordButton.setOnAction(e -> handleRecording());
        audioList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedAudio = audioList.getSelectionModel().getSelectedItem();
                if (selectedAudio != null) {
                    playAudio(currentUser.folder.getAudioPath() + selectedAudio);
                }
            }
        });
    }

    @FXML
    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String imageName = System.currentTimeMillis() + "_" + selectedFile.getName();
                String imagePath = currentUser.folder.getImagesPath() + imageName;

                Files.copy(selectedFile.toPath(), new File(imagePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                currentNote.addImagePath(imagePath);
                addImageToGallery(imagePath);
                saveNote();
            } catch (IOException e) {
                showAlert("Error", "Failed to add image");
            }
        }
    }

    private void addImageToGallery(String imagePath) {
        try {
            Image image = new Image(new File(imagePath).toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);

            // إضافة قائمة منسدلة للحذف
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(e -> {
                // حذف الصورة من الواجهة
                imageGallery.getChildren().remove(imageView);
                // حذف الصورة من النوت
                currentNote.removeImagePath(imagePath);
                // حذف الملف فعلياً
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    imageFile.delete();
                }
                // حفظ التغييرات
                saveNote();
            });
            contextMenu.getItems().add(deleteItem);

            imageView.setOnContextMenuRequested(event -> {
                contextMenu.show(imageView, event.getScreenX(), event.getScreenY());
            });

            imageGallery.getChildren().add(imageView);
        } catch (Exception e) {
            showAlert("Error", "Failed to load image");
        }
    }

    private void handleRecording() {
        if (!isRecording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        try {
            format = new AudioFormat(44100, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                showAlert("Error", "Recording not supported");
                return;
            }

            String audioName = "recording_" + System.currentTimeMillis() + ".wav";
            String audioPath = currentUser.folder.getAudioPath() + audioName;

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            File audioFile = new File(audioPath);
            Thread recordingThread = new Thread(() -> {
                try (AudioInputStream ais = new AudioInputStream(line)) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, audioFile);
                    currentNote.addAudioPath(audioPath);
                    Platform.runLater(() -> {
                        loadAudioList();
                        saveNote();
                    });
                } catch (IOException e) {
                    Platform.runLater(() ->
                            showAlert("Error", "Failed to save recording"));
                }
            });

            isRecording = true;
            recordButton.setText("Stop Recording");
            recordingThread.start();

        } catch (LineUnavailableException e) {
            showAlert("Error", "Could not start recording");
        }
    }

    private void stopRecording() {
        if (line != null) {
            isRecording = false;
            line.stop();
            line.close();
            recordButton.setText("Start Recording");
        }
    }

    private void playAudio(String audioPath) {
        try {
            File audioFile = new File(audioPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                line.open(format);
                line.start();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
                    line.write(buffer, 0, bytesRead);
                }

                line.drain();
                line.stop();
            }
        } catch (Exception e) {
            showAlert("Error", "Could not play audio");
        }
    }

    @FXML
    private void clearCanvas() {
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
        currentNote.clearSketchPoints();
        saveNote();
    }

    @FXML
    private void showTextEditor() {
        textEditorContainer.setVisible(true);
        drawingContainer.setVisible(false);
        imageContainer.setVisible(false);
        audioContainer.setVisible(false);

        textView.setStyle("-fx-background-color: #2575fc;");
        imageView.setStyle(null);
        drawView.setStyle(null);
        audioView.setStyle(null);
    }

    @FXML
    private void showDrawingCanvas() {
        textEditorContainer.setVisible(false);
        drawingContainer.setVisible(true);
        imageContainer.setVisible(false);
        audioContainer.setVisible(false);

        textView.setStyle(null);
        imageView.setStyle(null);
        drawView.setStyle("-fx-background-color: #2575fc;");
        audioView.setStyle(null);
    }

    @FXML
    private void showImageGallery() {
        textEditorContainer.setVisible(false);
        drawingContainer.setVisible(false);
        imageContainer.setVisible(true);
        audioContainer.setVisible(false);

        textView.setStyle(null);
        imageView.setStyle("-fx-background-color: #2575fc;");
        drawView.setStyle(null);
        audioView.setStyle(null);
    }

    @FXML
    private void showAudioRecorder() {
        textEditorContainer.setVisible(false);
        drawingContainer.setVisible(false);
        imageContainer.setVisible(false);
        audioContainer.setVisible(true);

        textView.setStyle(null);
        imageView.setStyle(null);
        drawView.setStyle(null);
        audioView.setStyle("-fx-background-color: #2575fc;");
    }

    private void loadExistingContent() {
        // Load text
        if (currentNote.getTextContent() != null) {
            htmlEditor.setHtmlText(currentNote.getTextContent());
        }

        // Load images
        imageGallery.getChildren().clear();
        for (NoteImage imagePath : currentNote.getImagePaths()) {
            addImageToGallery(imagePath.getImagePath());
        }

        // Load audio list
        loadAudioList();

        // Load sketch
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
        for (Sketch point : currentNote.getSketchPoints()) {
            gc.setFill(new Color(point.getRed(), point.getGreen(),
                    point.getBlue(), point.getOpacity()));
            gc.fillOval(point.getX() - point.getSize()/2,
                    point.getY() - point.getSize()/2,
                    point.getSize(), point.getSize());
        }
    }

    private void loadAudioList() {
        audioList.getItems().clear();
        for (NoteAudio audioPath : currentNote.getAudioPaths()) {
            File audioFile = new File(audioPath.getAudioPath());
            audioList.getItems().add(audioFile.getName());
        }
    }

    private void saveNote() {
        currentNote.setTextContent(htmlEditor.getHtmlText());
        String notePath = currentUser.folder.getFolderPath() + '/' +
                currentNote.getNoteName() + ".dat";
        FileManager.updateNote(currentNote, notePath);
        UsersManager.updateData();
    }

    @FXML
    private void handleBack() {
        try {
            saveNote();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/sourcepackage/ViewPackage/HomeView/home.fxml"));
            Parent root = loader.load();
            HomeController homeController = loader.getController();
            homeController.initialize(currentUser);
            Stage stage = (Stage) noteTitle.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Failed to return to home view");
        }
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}