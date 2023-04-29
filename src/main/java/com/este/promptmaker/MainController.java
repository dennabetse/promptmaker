package com.este.promptmaker;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private VBox automaticTagsBox;
    @FXML
    private VBox manualTagsBox;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private CheckBox checkedCustomFilename;
    @FXML
    private TextField filenameBox;
    @FXML
    private TextField questionBox;
    @FXML
    private HBox imageTypeBox;
    @FXML
    private Label imagePathLabel;
    @FXML
    private CheckBox resizeImage;
    @FXML
    private VBox textTypeBox;
    @FXML
    private TextArea textContentBox;
    @FXML
    private TextArea answersBox;
    @FXML
    private TextField shorthandsBox;
    @FXML
    private TextArea detailsBox;
    @FXML
    private TextField submitterBox;
    @FXML
    private TextArea tagsInputBox;
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
        choiceBox.getItems().addAll("image", "text");
        choiceBox.getSelectionModel().select("image");

        this.englishTags = readFile("config/tags-en.txt");
        this.frenchTags = readFile("config/tags-fr.txt");
        this.germanTags = readFile("config/tags-de.txt");
        this.hungarianTags = readFile("config/tags-hu.txt");
        this.spanishTags = readFile("config/tags-es.txt");

        this.checkboxes = new ArrayList<>();

        onEnglishTagsClick();

        choiceBox.setOnAction((event) -> {
            if (choiceBox.getValue().equals("image")) {
                imageTypeBox.setVisible(true);
                textTypeBox.setVisible(false);
                textContentBox.clear();
            } else {
                textTypeBox.setVisible(true);
                imageTypeBox.setVisible(false);
                imagePathLabel.setText("...");
                resizeImage.setSelected(false);
            }
        });

        checkedCustomFilename.setOnAction((event) -> {
            if (checkedCustomFilename.isSelected())
                filenameBox.setVisible(true);
            else {
                filenameBox.setVisible(false);
                filenameBox.clear();
            }
        });
    }

    private static List<String> readFile(String file) {
        List<String> rows = new ArrayList<>();
        try {
            Files.lines(Paths.get(file)).forEach(rows::add);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return rows;
    }

    private void clearCheckBoxes() {
        for (CheckBox c : checkboxes) {
            if (c.isSelected()) {
                c.setSelected(false);
            }
        }
    }

    private void removeCheckBoxes() {
        for (CheckBox checkbox : checkboxes) {
            tagsListBox.getChildren().remove(checkbox);
        }
        checkboxes.clear();
    }

    private void createCheckBoxes(List<String> tags) {
        for (String tag : tags) {
            checkboxes.add(new CheckBox(tag));
        }
    }

    private void addCheckBoxes() {
        for (CheckBox checkbox : checkboxes) {
            tagsListBox.getChildren().add(checkbox);
        }
    }

    @FXML
    protected void onEnglishTagsClick() {
        removeCheckBoxes();
        createCheckBoxes(englishTags);
        addCheckBoxes();
    }

    @FXML
    protected void onFrenchTagsClick() {
        removeCheckBoxes();
        createCheckBoxes(frenchTags);
        addCheckBoxes();
    }

    @FXML
    protected void onGermanTagsClick() {
        removeCheckBoxes();
        createCheckBoxes(germanTags);
        addCheckBoxes();
    }

    @FXML
    protected void onHungarianTagsClick() {
        removeCheckBoxes();
        createCheckBoxes(hungarianTags);
        addCheckBoxes();
    }

    @FXML
    protected void onSpanishTagsClick() {
        removeCheckBoxes();
        createCheckBoxes(spanishTags);
        addCheckBoxes();
    }

    @FXML
    protected void onAutomaticInputClick() {
        automaticTagsBox.setVisible(true);
        manualTagsBox.setVisible(false);
        tagsInputBox.clear();
    }

    @FXML
    protected void onManualInputClick() {
        manualTagsBox.setVisible(true);
        automaticTagsBox.setVisible(false);
        clearCheckBoxes();
    }

    @FXML
    protected void onAboutClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("PromptMaker version 0.1");
        alert.setContentText("add king#4718 on discord if you have any question");
        alert.show();
    }

    @FXML
    protected void onSelectImageClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("../"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Any", "*.png", "*.jpg", "*.svg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("SVG", "*.svg")
        );
        this.selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePathLabel.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    protected void onClearButtonClick() {
        filenameBox.clear();
        questionBox.clear();
        textContentBox.clear();
        answersBox.clear();
        shorthandsBox.clear();
        detailsBox.clear();
        submitterBox.clear();
        tagsInputBox.clear();
        clearCheckBoxes();
        imagePathLabel.setText("...");
        resizeImage.setSelected(false);
    }

    @FXML
    protected void onGenerateJsonButtonClick() {
        PromptMaker prompt = new PromptMaker();
        List<String> answers = new ArrayList<>();
        List<String> shorthands = new ArrayList<>();
        List<String> tags = new ArrayList<>();

        String promptValue = questionBox.getText();
        String textContentValue = textContentBox.getText();
        String sourceValue = answersBox.getText();
        answers.addAll(splitTextArea(sourceValue));
        String shorthandSource = shorthandsBox.getText();
        if (!shorthandSource.isEmpty()) {
            String[] sSplit = shorthandSource.split(",");
            for (String shorts : sSplit) {
                shorthands.add(shorts.trim());
            }
        }
        String detailValue = detailsBox.getText();
        String submitterValue = submitterBox.getText();
        if (manualTagsBox.isVisible()) {
            String tagValue = tagsInputBox.getText();
            tags.addAll(splitTextArea(tagValue));
        } else {
            for (CheckBox checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    tags.add(checkbox.getText());
                }
            }
        }

        if (choiceBox.getValue().equals("image")) {
            prompt = new PromptMaker(promptValue, answers, shorthands, detailValue, submitterValue, tags);
        } else if (choiceBox.getValue().equals("text")) {
            prompt = new PromptMaker(promptValue, textContentValue, answers, shorthands, detailValue, submitterValue, tags);
        }

        String getJSON = prompt.save();
        System.out.println(getJSON);

        String fileName = answers.get(0);

        if (checkedCustomFilename.isSelected()) {
            fileName = filenameBox.getText();
        }

        try {
            prompt.writeToFile(fileName, getJSON);
            if (!imagePathLabel.getText().equals("...")) {
                BufferedImage myPicture = ImageIO.read(new File(selectedFile.getPath()));
                Boolean checked = resizeImage.isSelected();
                prompt.copyFile(selectedFile, myPicture, fileName, checked);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> splitTextArea(String text) {
        List<String> list = new ArrayList<>();
        String[] parts = text.split("\n");
        for (String p : parts) {
            list.add(p.trim());
        }
        return list;
    }
}