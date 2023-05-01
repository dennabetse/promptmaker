package com.este.promptmaker;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private ChoiceBox promptType;
    @FXML
    private CheckBox customFilename;
    @FXML
    private TextField filenameField;
    @FXML
    private TextField questionField;
    @FXML
    private HBox imageTypeBox;
    @FXML
    private Label selectedImagePath;
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
    private List<String> englishTags;
    private List<String> frenchTags;
    private List<String> germanTags;
    private List<String> hungarianTags;
    private List<String> spanishTags;
    private List<CheckBox> checkboxes;
    private File selectedFile;

    public void initialize() {
        englishTags = readFile("tags-en.txt");
        frenchTags = readFile("tags-fr.txt");
        germanTags = readFile("tags-de.txt");
        hungarianTags = readFile("tags-hu.txt");
        spanishTags = readFile("tags-es.txt");

        checkboxes = new ArrayList<>();

        promptType.getItems().addAll("image", "text");
        promptType.setValue("image");

        englishTags();
    }

    private List<String> readFile(String file) {
        List<String> rows = new ArrayList<>();
        try {
            Files.lines(Paths.get("config/" + file)).forEach(rows::add);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return rows;
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
        for (CheckBox c : checkboxes) {
            if (c.isSelected()) {
                c.setSelected(false);
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
        removeCheckboxes();
        createCheckboxes(englishTags);
        addCheckboxes();
    }

    @FXML
    protected void frenchTags() {
        removeCheckboxes();
        createCheckboxes(frenchTags);
        addCheckboxes();
    }

    @FXML
    protected void germanTags() {
        removeCheckboxes();
        createCheckboxes(germanTags);
        addCheckboxes();
    }

    @FXML
    protected void hungarianTags() {
        removeCheckboxes();
        createCheckboxes(hungarianTags);
        addCheckboxes();
    }

    @FXML
    protected void spanishTags() {
        removeCheckboxes();
        createCheckboxes(spanishTags);
        addCheckboxes();
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
    protected void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("PromptMaker version 0.1");
        alert.setContentText("add king#4718 on discord if you have any question");
        alert.show();
    }

    @FXML
    private void promptType() {
        if (promptType.getValue().equals("image")) {
            imageTypeBox.setVisible(true);
            textTypeBox.setVisible(false);
            textContentArea.clear();
        } else {
            textTypeBox.setVisible(true);
            imageTypeBox.setVisible(false);
            selectedImagePath.setText("...");
            resizeImage.setSelected(false);
        }
    }

    @FXML
    private void customFilename() {
        if (customFilename.isSelected())
            filenameField.setVisible(true);
        else {
            filenameField.setVisible(false);
            filenameField.clear();
        }
    }

    @FXML
    protected void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("../"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Any image", "*.png", "*.jpg", "*.svg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("SVG", "*.svg")
        );
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImagePath.setText(selectedFile.getName());
        }
    }

    @FXML
    private void reset() {
        clearCheckboxes();
    }

    @FXML
    protected void generateJson() {
        PromptMaker promptContent = new PromptMaker();

        List<String> answers = new ArrayList<>();
        List<String> shorthands = new ArrayList<>();
        List<String> tags = new ArrayList<>();

        String question = questionField.getText();
        String textContent = textContentArea.getText();
        String source = answersArea.getText();
        answers.addAll(splitText(source));
        String shorthand = shorthandsField.getText();
        if (!shorthand.isEmpty()) {
            String[] sSplit = shorthand.split(",");
            for (String shorts : sSplit) {
                if (!shorts.isBlank()) {
                    shorthands.add(shorts.trim());
                }
            }
        }
        String details = detailsArea.getText();
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

        if (promptType.getValue().equals("image")) {
            promptContent = new PromptMaker(question, answers, shorthands, details, submitter, tags);
        } else if (promptType.getValue().equals("text")) {
            promptContent = new PromptMaker(question, textContent, answers, shorthands, details, submitter, tags);
        }

        String fileContent = promptContent.save();
        System.out.println(fileContent);

        String fileName = "";

        if (!answers.isEmpty()) {
            fileName = answers.get(0);
        }

        if (customFilename.isSelected()) {
            fileName = filenameField.getText();
        }

        try {
            if (!selectedImagePath.getText().equals("...")) {
                String ext = FilenameUtils.getExtension(selectedFile.getName());
                Boolean checked = resizeImage.isSelected();

                promptContent.createFile(fileName, fileContent, selectedFile, ext, checked);
            } else {
                promptContent.createFile(fileName, fileContent);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void clear() {
        customFilename.setSelected(false);
        filenameField.setVisible(false);
        filenameField.clear();
        questionField.clear();
        selectedImagePath.setText("...");
        resizeImage.setSelected(false);
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
        selectedImagePath.setText("...");
        resizeImage.setSelected(false);
    }
}