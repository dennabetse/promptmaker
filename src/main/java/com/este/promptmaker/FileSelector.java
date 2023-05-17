package com.este.promptmaker;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

public class FileSelector {

    private final Setting settings;
    private File selectedImage;

    public FileSelector() throws IOException {
        settings = new Setting();
    }

    public String chooseDirectory(Stage stage) {
        String path = settings.get("folder_output");
        if (path.isEmpty() || !new File(path).exists()) {
            path = System.getProperty("user.home") + "/Desktop";
        }

        DirectoryChooser directory = new DirectoryChooser();
        directory.setInitialDirectory(new File(path));

        File selectedDirectory = directory.showDialog(stage);
        if (selectedDirectory != null) {
            settings.set("folder_output", selectedDirectory.getAbsolutePath());
            return selectedDirectory.getAbsolutePath();
        }
        return settings.get("folder_output");
    }

    public File getSelectedImage() {
        return selectedImage;
    }

    public String getExt() {
        return FilenameUtils.getExtension(selectedImage.getName());
    }

    public String imageChooser(Stage stage) {
        String value = "Any image";
        if (settings.get("locale").equals("fr")) {
            value = "Images supportées";
        }
        String path = settings.get("last_folder");
        if (path.isEmpty() || !new File(path).exists()) {
            path = System.getProperty("user.home") + "/Desktop";
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(path));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(value, "*.png", "*.jpg", "*.svg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("SVG", "*.svg")
        );

        selectedImage = fileChooser.showOpenDialog(stage);
        if (selectedImage != null) {
            settings.set("last_folder", selectedImage.getParent());
            return selectedImage.getName();
        }
        return null;
    }

    public void unloadFile() {
        selectedImage = null;
    }
}
