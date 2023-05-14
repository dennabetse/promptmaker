package com.este.promptmaker;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsController {

    @FXML
    private BorderPane settingsView;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextField folderOutputField;
    private Setting settings;
    private ResourceBundle bundle;
    private FileSelector fs;
    private String lang;

    public void initialize() throws IOException {
        settings = new Setting();
        fs = new FileSelector();
        lang = settings.get("locale");
        bundle = ResourceBundle.getBundle("com.este.promptmaker.locale", new Locale(lang));
        setChoiceBox();
        folderOutputField.setText(settings.get("folder_output"));
    }

    private String value(String string) {
        return bundle.getString(string);
    }

    private void setChoiceBox() {
        choiceBox.getItems().addAll(value("key2"), value("key3"));
        if (lang.equals("en")) {
            choiceBox.getSelectionModel().selectFirst();
        } else if (lang.equals("fr")) {
            choiceBox.getSelectionModel().select(value("key3"));
        }
    }

    private void close() {
        Stage stage = (Stage) settingsView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setLanguage() {
        String selected = choiceBox.getValue();
        if (selected.equals(value("key2"))) {
            lang = "en";
        } else if (selected.equals(value("key3"))) {
            lang = "fr";
        }
    }

    @FXML
    private void folder() {
        String selectedFolder = fs.chooseDirectory();
        folderOutputField.setText(selectedFolder);
    }

    @FXML
    private void confirm() throws IOException {
        settings.set("locale", lang);
        settings.set("folder_output", folderOutputField.getText());
        settings.save();
        close();
    }

    @FXML
    private void cancel() {
        close();
    }
}
