package com.este.promptmaker;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private BorderPane settingsView;
    @FXML
    private ChoiceBox<String> language;
    @FXML
    private TextField folderOutputField;
    private Setting settings;

    public void initialize() throws IOException {
        settings = new Setting();
        language.getItems().addAll("English", "French");
        language();
        folderOutputField.setText(settings.get("folder_output"));
    }

    private void language() {
        String lang = settings.get("locale");
        switch (lang) {
            case "English" -> language.getSelectionModel().select("English");
            case "French" -> language.getSelectionModel().select("French");
        }
    }

    private void close() {
        Stage stage = (Stage) settingsView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void folder() throws IOException {
        settings.chooseDirectory();
        folderOutputField.setText(settings.get("folder_output"));
    }

    @FXML
    private void confirm() throws IOException {
        settings.set("locale", language.getValue());
        settings.save();
        close();
    }

    @FXML
    private void cancel() {
        close();
    }
}
