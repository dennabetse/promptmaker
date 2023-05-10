package com.este.promptmaker;

import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManipulator {

    private File selectedFile;
    private String pathname;

    public File getSelectedFile() {
        return selectedFile;
    }

    public void unloadFile() {
        selectedFile = null;
    }

    public String getExt() {
        return FilenameUtils.getExtension(selectedFile.getName());
    }

    public void imageChooser() throws IOException {
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
            pathname = selectedFile.getCanonicalPath();
        }
    }

    // still a work in progress
    public BufferedImage resizeImage() throws IOException {
        BufferedImage bi = ImageIO.read(new File(pathname));
        if (!(bi == null)) {
            bi = simpleResizeImage(bi, 1024);
            return bi;
        }
        return null;
    }

    private BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) {
        return Scalr.resize(originalImage, targetWidth);
    }
}
