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
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class TagEditorController {

    @FXML
    private BorderPane editTagsView;
    @FXML
    private ChoiceBox<String> tagsLanguage;
    @FXML
    private TextArea tagsArea;
    private ResourceBundle bundle;
    private TagReader tags;

    public void initialize() throws IOException {
        Setting settings = new Setting();
        bundle = ResourceBundle.getBundle("com.este.promptmaker.locale", new Locale(settings.get("locale")));
        tags = new TagReader();
        tagsLanguage.getItems().addAll(value("key2"), value("key3"), value("key4"), value("key5"), value("key6"));
        tagsLanguage.getSelectionModel().selectFirst();
    }

    private String value(String string) {
        return bundle.getString(string);
    }

    private void print(List<String> list) {
        tagsArea.setText(tags.printTags(list));
    }

    private void confirmSave() throws IOException {
        String tags = tagsArea.getText().trim();
        FileManipulator fm = new FileManipulator();
        String lang = tagsLanguage.getValue();
        if (lang.equals(value("key2"))) {
            fm.saveTags("tags-en.txt", tags);
        } else if (lang.equals(value("key3"))) {
            fm.saveTags("tags-fr.txt", tags);
        } else if (lang.equals(value("key4"))) {
            fm.saveTags("tags-de.txt", tags);
        } else if (lang.equals(value("key5"))) {
            fm.saveTags("tags-hu.txt", tags);
        } else if (lang.equals(value("key6"))) {
            fm.saveTags("tags-es.txt", tags);
        }
    }

    private void close() {
        Stage stage = (Stage) editTagsView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void language() {
        String lang = tagsLanguage.getValue();
        if (lang.equals(value("key2"))) {
            print(tags.getEnglish());
        } else if (lang.equals(value("key3"))) {
            print(tags.getFrench());
        } else if (lang.equals(value("key4"))) {
            print(tags.getGerman());
        } else if (lang.equals(value("key5"))) {
            print(tags.getHungarian());
        } else if (lang.equals(value("key6"))) {
            print(tags.getSpanish());
        }
    }

    @FXML
    private void save() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) editTagsView.getScene().getWindow();
        alert.initOwner(stage);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(value("key36"));

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
