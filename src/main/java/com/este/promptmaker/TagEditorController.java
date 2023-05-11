package com.este.promptmaker;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TagEditorController {

    @FXML
    private BorderPane editTagsView;
    @FXML
    private ChoiceBox<String> tagsLanguage;
    @FXML
    private TextArea tagsArea;
    private TagReader tags;

    public void initialize() {
        tags = new TagReader();
        tagsLanguage.getItems().addAll("English", "French", "German", "Hungarian", "Spanish");
        tagsLanguage.getSelectionModel().selectFirst();
    }

    private void print(List<String> list) {
        tagsArea.setText(tags.printTags(list));
    }

    private void confirmSave() throws IOException {
        String tags = tagsArea.getText().trim();
        FileManipulator fm = new FileManipulator();
        String lang = tagsLanguage.getValue();
        switch (lang) {
            case "English" -> fm.saveTags("tags-en.txt", tags);
            case "French" -> fm.saveTags("tags-fr.txt", tags);
            case "German" -> fm.saveTags("tags-de.txt", tags);
            case "Hungarian" -> fm.saveTags("tags-hu.txt", tags);
            case "Spanish" -> fm.saveTags("tags-es.txt", tags);
        }
    }

    private void close(){
        Stage stage = (Stage) editTagsView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void language() {
        String lang = tagsLanguage.getValue();
        switch (lang) {
            case "English" -> print(tags.getEnglish());
            case "French" -> print(tags.getFrench());
            case "German" -> print(tags.getGerman());
            case "Hungarian" -> print(tags.getHungarian());
            case "Spanish" -> print(tags.getSpanish());
        }
    }

    @FXML
    private void save() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("Confirm change(s)?");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.orElse(null) == ButtonType.OK) {
            confirmSave();
            close();
        }
    }

    @FXML
    private void cancel() {
        close();
    }
}
