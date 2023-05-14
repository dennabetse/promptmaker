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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
    private ResourceBundle bundle;
    private TagReader tags;
    private FileSelector fs;
    private List<CheckBox> checkboxes;

    public void initialize() throws IOException {
        settings = new Setting();
        bundle = ResourceBundle.getBundle("com.este.promptmaker.locale", new Locale(settings.get("locale")));
        tags = new TagReader();
        fs = new FileSelector();
        checkboxes = new ArrayList<>();

        promptType.getItems().addAll("Image", value("key40"));
        promptType.getSelectionModel().selectFirst();

        getTags();
    }

    private String value(String string) {
        return bundle.getString(string);
    }

    private PromptMaker promptContent() {
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

        if (promptType.getValue().equals("Image")) {
            return new PromptMaker(question, answers, shorthands, details, submitter, tags);
        }
        return new PromptMaker(question, textContent, answers, shorthands, details, submitter, tags);
    }

    private String formattedText(String string) {
        return string.replaceAll("\\\\", "\\\\\\\\")
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
            System.out.println(value("key41"));
            empty = true;
        }
        if (promptType.getValue().equals(value("key40")) && promptContent.getText().isEmpty()) {
            System.out.println(value("key42"));
            empty = true;
        }
        if (promptContent.getSources().isEmpty()) {
            System.out.println(value("key43"));
            empty = true;
        }
        if (promptContent.getTags().isEmpty()) {
            System.out.println(value("key44"));
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

    private void setTags(List<String> list, String language) {
        removeCheckboxes();
        createCheckboxes(list);
        addCheckboxes();
        settings.set("tags_language", language);
    }

    private void getTags() {
        String lang = settings.get("tags_language");
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tags-view.fxml"), bundle);
        Stage stage = newStage(fxmlLoader);
        stage.setTitle(bundle.getString("key10"));
        stage.showAndWait();
        tags.load();
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings-view.fxml"), bundle);
        Stage stage = newStage(fxmlLoader);
        stage.setTitle(bundle.getString("key11"));
        stage.showAndWait();
        settings.load();
    }

    @FXML
    private void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage());
        alert.setTitle(value("key14"));
        alert.setHeaderText("PromptMaker");
        alert.setContentText("Version 0.4.2\n" + value("key45"));
        Hyperlink link = new Hyperlink("GitHub");
        alert.setGraphic(link);
        link.setOnAction((event) -> {
            HostServices hostServices = (HostServices) primaryStage().getProperties().get("hostServices");
            hostServices.showDocument("https://github.com/esteb4nned/PromptMaker");
        });
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
    private void selectImage() {
        String selectedFile = fs.imageChooser();
        if (selectedFile != null) {
            settings.set("last_folder", fs.getSelectedImage().getParent());
            selectedImage.setText(selectedFile);
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
            String selectedFolder = fs.chooseDirectory();
            if (selectedFolder.isEmpty()) {
                System.out.println("-----------------------------------------------\n" + value("key46") + "\n-----------------------------------------------\n");
                return;
            }
            settings.set("folder_output", selectedFolder);
        }

        PromptMaker prompt = promptContent();
        if (missingField(prompt)) {
            System.out.println("\n-----------------------------------------------\n" + value("key46") + "\n-----------------------------------------------\n");
            return;
        }
        if (settings.changed()) {
            settings.save();
            settings.load();
        }

        String promptContent = prompt.save();
        System.out.println(promptContent);
        System.out.println();
        String fileName = prompt.getSources().get(0);
        if (customFilename.isSelected()) {
            fileName = filenameField.getText();
        }

        FileManipulator fm = new FileManipulator(settings.get("folder_output"));
        fm.makeJson(fileName, promptContent);

        if (fs.getSelectedImage() != null) {
            if (resizeImage.isSelected()) {
                ImageManipulator im = new ImageManipulator();
                BufferedImage bi = im.resizeImage(fs.getSelectedImage());
                if (bi != null) {
                    fm.copyResizedImage(bi, fs.getExt());
                    System.out.println(value("key47") + "\n");
                } else {
                    fm.copyImage(fs.getSelectedImage(), fs.getExt());
                    System.out.println(value("key48") + "\n");
                }
            } else {
                fm.copyImage(fs.getSelectedImage(), fs.getExt());
                System.out.println(value("key49") + "\n");
            }
        }

        System.out.println(value("key38") + ":\n\"" + settings.get("folder_output") + "\"\n\n-----------------------------------------------\n");
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
        fs.unloadFile();
        selectedImage.setText("...");
        resizeBox.setVisible(false);
        resizeImage.setSelected(false);
        unloadImageButton.setVisible(false);
    }
}