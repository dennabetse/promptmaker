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

public class TagsViewController {

    @FXML
    private BorderPane editTagsView;
    @FXML
    private ChoiceBox<String> tagsLanguage;
    @FXML
    private TextArea tagsArea;
    private TagsReader tags;

    public void initialize() {
        tags = new TagsReader();
        tagsLanguage.getItems().addAll("English", "French", "German", "Hungarian", "Spanish");
        tagsLanguage.getSelectionModel().selectFirst();
    }

    private void print(List<String> list) {
        tagsArea.setText(tags.printTags(list));
    }

    private void confirmSave() throws IOException {
        String tags = tagsArea.getText().trim();
        FileManipulator fm = new FileManipulator();
        if (tagsLanguage.getValue().equals("English")) {
            fm.saveTags("tags-en.txt", tags);
        } else if (tagsLanguage.getValue().equals("French")) {
            fm.saveTags("tags-fr.txt", tags);
        } else if (tagsLanguage.getValue().equals("German")) {
            fm.saveTags("tags-de.txt", tags);
        } else if (tagsLanguage.getValue().equals("Hungarian")) {
            fm.saveTags("tags-hu.txt", tags);
        } else if (tagsLanguage.getValue().equals("Spanish")) {
            fm.saveTags("tags-es.txt", tags);
        }
    }

    private void close(){
        Stage stage = (Stage) editTagsView.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void language() {
        if (tagsLanguage.getValue().equals("English")) {
            print(tags.getEnglish());
        } else if (tagsLanguage.getValue().equals("French")) {
            print(tags.getFrench());
        } else if (tagsLanguage.getValue().equals("German")) {
            print(tags.getGerman());
        } else if (tagsLanguage.getValue().equals("Hungarian")) {
            print(tags.getHungarian());
        } else if (tagsLanguage.getValue().equals("Spanish")) {
            print(tags.getSpanish());
        }
    }

    @FXML
    protected void save() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("Confirm change(s)?");

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            confirmSave();
            close();
        }
    }

    @FXML
    protected void cancel() {
        close();
    }
}
