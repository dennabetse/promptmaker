package com.este.promptmaker;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class OutputFolderController {

    @FXML
    private BorderPane folderView;
    @FXML
    private TextField folderOutputField;
    private FolderChooser fc;

    public void initialize() throws IOException {
        fc = new FolderChooser();
        folderOutputField.setText(fc.getPath());
    }

    private void close(){
        Stage stage = (Stage) folderView.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void folder() throws IOException {
        fc.chooseDirectory();
        folderOutputField.setText(fc.getPath());
    }

    @FXML
    protected void confirm() throws IOException {
        fc.savePath();
        close();
    }

    @FXML
    protected void cancel() {
        close();
    }
}
