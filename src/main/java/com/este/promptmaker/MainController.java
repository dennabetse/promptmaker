package com.este.promptmaker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private BorderPane mainView;
    @FXML
    private ChoiceBox<String> promptType;
    @FXML
    private CheckBox customFilename;
    @FXML
    private TextField filenameField;
    @FXML
    private TextField questionField;
    @FXML
    private VBox imageTypeBox;
    @FXML
    private Label selectedImagePath;
    @FXML
    private HBox resizeBox;
    @FXML
    private CheckBox resizeImage;
    @FXML
    private VBox textTypeBox;
    @FXML
    private TextArea textContentArea;
    @FXML
    private TextArea answersArea;
    @FXML
    private TextField shorthandsField;
    @FXML
    private TextArea detailsArea;
    @FXML
    private TextField submitterField;
    @FXML
    private VBox manualTagsBox;
    @FXML
    private TextArea tagsInputArea;
    @FXML
    private VBox automatedTagsBox;
    @FXML
    private VBox tagsListBox;
    @FXML
    private Button unloadImageButton;
    private TagReader tags;
    private List<CheckBox> checkboxes;
    private ImageManipulator image;

    public void initialize() {
        tags = new TagReader();
        checkboxes = new ArrayList<>();
        promptType.getItems().addAll("Image", "Text");
        promptType.getSelectionModel().selectFirst();
        image = new ImageManipulator();
        englishTags();
    }

    private PromptMaker promptContent() {
        PromptMaker promptContent = new PromptMaker();

        List<String> shorthands = new ArrayList<>();
        List<String> tags = new ArrayList<>();

        String question = questionField.getText();
        String textContent = textContentArea.getText().replaceAll("\n", "\\\\n");
        String source = answersArea.getText();
        List<String> answers = new ArrayList<>(splitText(source));
        String shorthand = shorthandsField.getText();
        if (!shorthand.isEmpty()) {
            String[] sSplit = shorthand.split(",");
            for (String shorts : sSplit) {
                if (!shorts.isBlank()) {
                    shorthands.add(shorts.trim());
                }
            }
        }
        String details = detailsArea.getText().replaceAll("\n", "\\\\n");
        String submitter = submitterField.getText();
        if (manualTagsBox.isVisible()) {
            String tagsValue = tagsInputArea.getText();
            tags.addAll(splitText(tagsValue));
        } else {
            for (CheckBox checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    tags.add(checkbox.getText());
                }
            }
        }

        if (promptType.getValue().equals("Image")) {
            return new PromptMaker(question, answers, shorthands, details, submitter, tags);
        } else if (promptType.getValue().equals("Text")) {
            return new PromptMaker(question, textContent, answers, shorthands, details, submitter, tags);
        }
        return promptContent;
    }

    private boolean missingField(PromptMaker promptContent) {
        boolean empty = false;

        if (promptContent.getPrompt().isEmpty()) {
            System.out.println("Missing question.");
            empty = true;
        }
        if (promptType.getValue().equals("Text") && promptContent.getText().isEmpty()) {
            System.out.println("Missing text.");
            empty = true;
        }
        if (promptContent.getSources().isEmpty()) {
            System.out.println("Missing answer(s).");
            empty = true;
        }
        if (promptContent.getTags().isEmpty()) {
            System.out.println("Missing tags.");
            empty = true;
        }

        return empty;
    }

    private Stage primaryStage() {
        return (Stage) mainView.getScene().getWindow();
    }

    private Stage newStage(FXMLLoader fxmlLoader) throws IOException {
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initOwner(primaryStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        return stage;
    }

    private void setTags(List<String> list) {
        removeCheckboxes();
        createCheckboxes(list);
        addCheckboxes();
    }

    private void createCheckboxes(List<String> tags) {
        for (String tag : tags) {
            checkboxes.add(new CheckBox(tag));
        }
    }

    private void addCheckboxes() {
        for (CheckBox checkbox : checkboxes) {
            tagsListBox.getChildren().add(checkbox);
        }
    }

    private void clearCheckboxes() {
        for (CheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                checkbox.setSelected(false);
            }
        }
    }

    private void removeCheckboxes() {
        for (CheckBox checkbox : checkboxes) {
            tagsListBox.getChildren().remove(checkbox);
        }
        checkboxes.clear();
    }

    private List<String> splitText(String text) {
        List<String> list = new ArrayList<>();
        String[] parts = text.split("\n");
        for (String p : parts) {
            if (!p.isBlank()) {
                list.add(p.trim());
            }
        }
        return list;
    }

    @FXML
    protected void englishTags() {
        setTags(tags.getEnglish());
    }

    @FXML
    protected void frenchTags() {
        setTags(tags.getFrench());
    }

    @FXML
    protected void germanTags() {
        setTags(tags.getGerman());
    }

    @FXML
    protected void hungarianTags() {
        setTags(tags.getHungarian());
    }

    @FXML
    protected void spanishTags() {
        setTags(tags.getSpanish());
    }

    @FXML
    protected void editTagsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tags-view.fxml"));
        Stage stage = newStage(fxmlLoader);
        stage.showAndWait();
        tags = new TagReader();
        englishTags();
    }

    @FXML
    protected void automatedInput() {
        automatedTagsBox.setVisible(true);
        manualTagsBox.setVisible(false);
        tagsInputArea.clear();
    }

    @FXML
    protected void manualInput() {
        manualTagsBox.setVisible(true);
        automatedTagsBox.setVisible(false);
        clearCheckboxes();
    }

    @FXML
    protected void outputFolderView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("folder-view.fxml"));
        Stage stage = newStage(fxmlLoader);
        stage.show();
    }

    @FXML
    protected void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage());
        alert.setTitle("About");
        alert.setHeaderText("PromptMaker");
        alert.setContentText("Version 0.3.2\n© este. All rights reserved.");
        alert.show();
    }

    @FXML
    protected void promptType() {
        if (promptType.getValue().equals("Image")) {
            imageTypeBox.setVisible(true);
            textTypeBox.setVisible(false);
            textContentArea.clear();
        } else {
            textTypeBox.setVisible(true);
            imageTypeBox.setVisible(false);
            unloadImage();
        }
    }

    @FXML
    protected void customFilename() {
        if (customFilename.isSelected())
            filenameField.setVisible(true);
        else {
            filenameField.setVisible(false);
            filenameField.clear();
        }
    }

    @FXML
    protected void selectImage() throws IOException {
        image.imageChooser();
        if (image.getPath() != null) {
            selectedImagePath.setText(image.getPath());
            resizeBox.setVisible(true);
            unloadImageButton.setVisible(true);
        }
    }

    @FXML
    protected void reset() {
        clearCheckboxes();
    }

    @FXML
    protected void generateJson() throws IOException {
        FolderChooser fc = new FolderChooser();
        if (fc.getPath().isEmpty()) {
            fc.chooseDirectory();
            if (fc.getPath().isEmpty()) {
                System.out.println("-----------------------------------------------\nCanceled.\n-----------------------------------------------\n");
                return;
            }
            fc.savePath();
        }

        PromptMaker promptContent = promptContent();
        if (missingField(promptContent)) {
            System.out.println("\n-----------------------------------------------\nCanceled.\n-----------------------------------------------\n");
            return;
        }

        String fileContent = promptContent.save();
        System.out.println(fileContent);
        System.out.println();

        String fileName = promptContent.getSources().get(0);
        if (customFilename.isSelected()) {
            fileName = filenameField.getText();
        }

        FileManipulator fm = new FileManipulator();
        fm.makeJson(fileName, fileContent);
        if (!selectedImagePath.getText().equals("...")) {
            if (resizeImage.isSelected()) {
                BufferedImage bi = image.resizeImage();
                if (bi != null) {
                    fm.copyResizedImage(bi, image.getExt());
                    System.out.println("Image has been resized.\n");
                } else {
                    fm.copyImage(image.getSelectedFile(), image.getExt());
                    System.out.println("Resize failed. Image has been copied without any change.\n");
                }
            } else {
                fm.copyImage(image.getSelectedFile(), image.getExt());
                System.out.println("Image copied.\n");
            }
        }

        System.out.println("Output folder:\n\"" + fc.getPath() + "\"\n\n-----------------------------------------------\n");
    }

    @FXML
    protected void clear() {
        customFilename.setSelected(false);
        filenameField.setVisible(false);
        filenameField.clear();
        questionField.clear();
        unloadImage();
        textContentArea.clear();
        answersArea.clear();
        shorthandsField.clear();
        detailsArea.clear();
        submitterField.clear();
        tagsInputArea.clear();
        clearCheckboxes();
    }

    @FXML
    protected void unloadImage() {
        selectedImagePath.setText("...");
        resizeBox.setVisible(false);
        resizeImage.setSelected(false);
        unloadImageButton.setVisible(false);
    }
}