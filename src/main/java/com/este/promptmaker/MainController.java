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
import java.util.*;

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
    @FXML
    private Label missingPromptLabel;
    @FXML
    private Label missingTextLabel;
    @FXML
    private Label missingAnswerLabel;
    @FXML
    private Label missingTagsLabel;
    @FXML
    private Label missingTagsLabel2;
    @FXML
    private TextArea resultArea;
    @FXML
    private TextArea resultFilenameArea;
    @FXML
    private TextField resultField;
    private Setting settings;
    private ResourceBundle bundle;
    private TagReader tags;
    private FileSelector file;
    private List<CheckBox> checkboxes;

    public void initialize() throws IOException {
        settings = new Setting();
        bundle = ResourceBundle.getBundle("com.este.promptmaker.locale", new Locale(settings.get("locale")));
        tags = new TagReader();
        file = new FileSelector();
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
            resultArea.appendText(value("key41") + "\n");
            missingPromptLabel.setVisible(true);
            empty = true;
        }
        if (promptType.getValue().equals(value("key40")) && promptContent.getText().isEmpty()) {
            resultArea.appendText(value("key42") + "\n");
            missingTextLabel.setVisible(true);
            empty = true;
        }
        if (promptContent.getSources().isEmpty()) {
            resultArea.appendText(value("key43") + "\n");
            missingAnswerLabel.setVisible(true);
            empty = true;
        }
        if (promptContent.getTags().isEmpty()) {
            resultArea.appendText(value("key44") + "\n");
            missingTagsLabel.setVisible(true);
            missingTagsLabel2.setVisible(true);
            empty = true;
        }

        return empty;
    }

    private void hideRequiredLabels() {
        missingPromptLabel.setVisible(false);
        missingTextLabel.setVisible(false);
        missingAnswerLabel.setVisible(false);
        missingTagsLabel.setVisible(false);
        missingTagsLabel2.setVisible(false);
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
        if (settings.changed()) {
            settings.load();
        }
        if (!bundle.getLocale().toString().equals(settings.get("locale"))) {
            bundle = ResourceBundle.getBundle("com.este.promptmaker.locale", new Locale(settings.get("locale")));
            String os = System.getProperty("os.name");
            primaryStage().getScene().setRoot(loadResource(os).load());
        }
    }

    private FXMLLoader loadResource(String os) {
        if (os.contains("Win")) {
            return new FXMLLoader(MainController.class.getResource("main-view.fxml"), bundle);
        } else if (os.contains("Mac")) {
            return new FXMLLoader(MainController.class.getResource("mac-layout.fxml"), bundle);
        }
        return new FXMLLoader(MainController.class.getResource("linux-layout.fxml"), bundle);
    }

    @FXML
    private void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage());
        alert.setTitle(value("key14"));
        alert.setHeaderText("PromptMaker");
        alert.setContentText("Version 0.5.1\n" + value("key45"));
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
            missingTextLabel.setVisible(false);
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
        String selectedFile = file.imageChooser(primaryStage());
        if (selectedFile != null) {
            settings.set("last_folder", file.getSelectedImage().getParent());
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
        resultArea.clear();
        hideRequiredLabels();

        PromptMaker prompt = promptContent();

        if (missingField(prompt)) {
            resultField.setText(value("key46"));
            resultFilenameArea.setVisible(false);
            return;
        }

        if (settings.get("folder_output").isEmpty()) {
            String selectedFolder = file.chooseDirectory(primaryStage());
            if (selectedFolder.isEmpty()) {
                resultField.setText(value("key46"));
                return;
            }
            settings.set("folder_output", selectedFolder);
        }

        String promptContent = prompt.save();

        String fileName = prompt.getSources().get(0);
        if (customFilename.isSelected()) {
            fileName = filenameField.getText();
        }

        if (settings.changed()) {
            settings.save();
            settings.load();
        }

        FileManipulator fm = new FileManipulator(settings.get("folder_output"));
        fm.makeJson(fileName, promptContent);

        resultArea.setText(promptContent);

        String ext = "";
        if (file.getSelectedImage() != null) {
            ext = file.getExt();
            if (resizeImage.isSelected()) {
                ImageManipulator im = new ImageManipulator();
                BufferedImage bi = im.resizeImage(file.getSelectedImage());
                if (bi != null) {
                    fm.copyResizedImage(bi);
                    ext = "png";
                    resultField.setText(value("key47") + " " + value("key21") + " " + bi.getHeight() + ", " + value("key22") + " " + bi.getWidth());
                } else {
                    fm.copyImage(file.getSelectedImage(), file.getExt());
                    resultField.setText(value("key48"));
                }
            } else {
                fm.copyImage(file.getSelectedImage(), file.getExt());
                resultField.setText(value("key49"));
            }
        }

        if (selectedImage.getText().equals("...")) {
            resultField.setText(value("key13"));
            resultFilenameArea.setText(fm.getFilename() + ".json");
            resultFilenameArea.setVisible(true);
        } else {
            resultFilenameArea.setText(fm.getFilename() + ".json\n" + fm.getFilename() + "." + ext);
            resultFilenameArea.setVisible(true);
        }
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
        hideRequiredLabels();
    }

    @FXML
    private void unloadImage() {
        file.unloadFile();
        selectedImage.setText("...");
        resizeBox.setVisible(false);
        resizeImage.setSelected(false);
        unloadImageButton.setVisible(false);
    }
}