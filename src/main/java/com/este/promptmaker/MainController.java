package com.este.promptmaker;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
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
    private Label selectedImage;
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
    private Setting settings;
    private TagReader tags;
    private ImageManipulator image;
    private List<CheckBox> checkboxes;

    public void initialize() throws IOException {
        settings = new Setting();
        tags = new TagReader();
        image = new ImageManipulator();
        checkboxes = new ArrayList<>();

        promptType.getItems().addAll("Image", "Text");
        promptType.getSelectionModel().selectFirst();

        getTags();
    }

    private PromptMaker promptContent() {
        PromptMaker promptContent = new PromptMaker();

        String question = formattedText(questionField.getText());
        String textContent = formattedText(textContentArea.getText()).replaceAll("\n", "\\\\n");
        String source = formattedText(answersArea.getText());
        List<String> answers = new ArrayList<>(splitText(source, "\n"));
        String shorthand = formattedText(shorthandsField.getText());
        List<String> shorthands = new ArrayList<>(splitText(shorthand, ","));
        String details = formattedText(detailsArea.getText()).replaceAll("\n", "\\\\n");
        String submitter = formattedText(submitterField.getText());
        List<String> tags = new ArrayList<>();
        if (manualTagsBox.isVisible()) {
            String tagsValue = formattedText(tagsInputArea.getText());
            tags.addAll(splitText(tagsValue, "\n"));
        } else {
            for (CheckBox checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    tags.add(checkbox.getText());
                }
            }
        }

        String type = promptType.getValue();

        if (type.equals("Image")) {
            return new PromptMaker(question, answers, shorthands, details, submitter, tags);
        } else if (type.equals("Text")) {
            return new PromptMaker(question, textContent, answers, shorthands, details, submitter, tags);
        }
        return promptContent;
    }

    private String formattedText(String string) {
        return string
                .replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\"", "\\\\\"");
    }

    private List<String> splitText(String text, String splitter) {
        List<String> list = new ArrayList<>();
        String[] parts = text.split(splitter);
        for (String p : parts) {
            if (!p.isBlank()) {
                list.add(p.trim());
            }
        }
        return list;
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
        stage.getIcons().add(new Image("sparklinlabs.png"));
        stage.setResizable(false);
        stage.initOwner(primaryStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        return stage;
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

    private void setTags(List<String> list, String lang) {
        removeCheckboxes();
        createCheckboxes(list);
        addCheckboxes();
        settings.set("tags", lang);
    }

    private void getTags() {
        String lang = settings.get("tags");
        switch (lang) {
            case "en" -> englishTags();
            case "fr" -> frenchTags();
            case "de" -> germanTags();
            case "hu" -> hungarianTags();
            case "es" -> spanishTags();
        }
    }

    @FXML
    private void englishTags() {
        setTags(tags.getEnglish(), "en");
    }

    @FXML
    private void frenchTags() {
        setTags(tags.getFrench(), "fr");
    }

    @FXML
    private void germanTags() {
        setTags(tags.getGerman(), "de");
    }

    @FXML
    private void hungarianTags() {
        setTags(tags.getHungarian(), "hu");
    }

    @FXML
    private void spanishTags() {
        setTags(tags.getSpanish(), "es");
    }

    @FXML
    private void editTagsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tags-view.fxml"));
        Stage stage = newStage(fxmlLoader);
        stage.setTitle("Tag Editor");
        stage.showAndWait();
        tags = new TagReader();
        getTags();
    }

    @FXML
    private void automatedInput() {
        automatedTagsBox.setVisible(true);
        manualTagsBox.setVisible(false);
        tagsInputArea.clear();
    }

    @FXML
    private void manualInput() {
        manualTagsBox.setVisible(true);
        automatedTagsBox.setVisible(false);
        clearCheckboxes();
    }

    @FXML
    private void settingsView() throws IOException {
        settings.save();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings-view.fxml"));
        Stage stage = newStage(fxmlLoader);
        stage.setTitle("Settings");
        stage.showAndWait();
        settings = new Setting();
    }

    @FXML
    private void projectPage() {
        HostServices hostServices = (HostServices) primaryStage().getProperties().get("hostServices");
        hostServices.showDocument("https://github.com/esteb4nned/promptmaker");
    }

    @FXML
    private void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage());
        alert.setTitle("About");
        alert.setHeaderText("PromptMaker");
        alert.setContentText("Version 0.4\n© este. All rights reserved.");
        alert.show();
    }

    @FXML
    private void promptType() {
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
    private void customFilename() {
        if (customFilename.isSelected()) {
            filenameField.setVisible(true);
        } else {
            filenameField.setVisible(false);
            filenameField.clear();
        }
    }

    @FXML
    private void selectImage() throws IOException {
        image.imageChooser();
        if (image.getSelectedFile() != null) {
            selectedImage.setText(image.getSelectedFile().getName());
            resizeBox.setVisible(true);
            unloadImageButton.setVisible(true);
        }
    }

    @FXML
    private void reset() {
        clearCheckboxes();
    }

    @FXML
    private void generateJson() throws IOException {
        if (settings.get("folder_output").isEmpty()) {
            settings.chooseDirectory();
            if (settings.get("folder_output").isEmpty()) {
                System.out.println("-----------------------------------------------\nCanceled.\n-----------------------------------------------\n");
                return;
            }
            settings.save();
            settings = new Setting();
        }
        if (!new File("settings.ini").exists() || settings.changed()) {
            settings.save();
        }

        PromptMaker prompt = promptContent();

        if (missingField(prompt)) {
            System.out.println("\n-----------------------------------------------\nCanceled.\n-----------------------------------------------\n");
            return;
        }

        String promptContent = prompt.save();
        System.out.println(promptContent);
        System.out.println();

        String fileName = prompt.getSources().get(0);
        if (customFilename.isSelected()) {
            fileName = filenameField.getText();
        }

        FileManipulator fm = new FileManipulator();
        fm.makeJson(fileName, promptContent);

        if (image.getSelectedFile() != null) {
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

        System.out.println("Output folder:\n\"" + settings.get("folder_output") + "\"\n\n-----------------------------------------------\n");
    }

    @FXML
    private void clear() {
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
    private void unloadImage() {
        image.unloadFile();
        selectedImage.setText("...");
        resizeBox.setVisible(false);
        resizeImage.setSelected(false);
        unloadImageButton.setVisible(false);
    }
}