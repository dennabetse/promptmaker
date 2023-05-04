package com.este.promptmaker;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class FolderController {

    @FXML
    private BorderPane folderView;
    @FXML
    private TextField folderOutputField;
    private FolderChooser fc;

    public void initialize() throws IOException {
        fc = new FolderChooser();
        folderOutputField.setText(fc.getPath());
    }

    @FXML
    protected void folderButton() throws IOException {
        fc.chooseDirectory();
        folderOutputField.setText(fc.getPath());
    }

    @FXML
    protected void confirmButton() throws IOException {
        fc.savePath();
        Stage stage = (Stage) folderView.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void cancelButton() {
        Stage stage = (Stage) folderView.getScene().getWindow();
        stage.close();
    }
}
