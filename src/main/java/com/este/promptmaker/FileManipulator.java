package com.este.promptmaker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.CaseUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

public class FileManipulator {

    private FolderChooser fc;
    private String filename;

    public FileManipulator() throws IOException {
        fc = new FolderChooser();
    }

    private String toCamelCase(String name) {
        if (name.isEmpty()) {
            name = "untitled";
        }
        String title = CaseUtils.toCamelCase(name, false, ' ', '_');

        return Normalizer
                .normalize(title, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9]+", "");
    }

    private void setFilename(String name) {
        String convertedName = toCamelCase(name);

        int num = 1;
        filename = convertedName + "_" + num;
        File file = new File(fc.getPath() + "/" + filename + ".json");
        file.getParentFile().mkdirs();
        while (file.exists()) {
            filename = convertedName + "_" + (num++);
            file = new File(fc.getPath() + "/" + filename + ".json");
        }
    }

    private void writeToFile(String pathname, String text) throws IOException {
        PrintWriter writer = new PrintWriter(pathname, StandardCharsets.UTF_8);
        writer.println(text);
        writer.close();
    }

    public void makeJson(String name, String text) throws IOException {
        setFilename(name);
        writeToFile(fc.getPath() + "/" + filename + ".json", text);
    }

    public void saveTags(String languageFile, String text) throws IOException {
        String pathname = "config/" + languageFile;
        File file = new File(pathname);
        file.getParentFile().mkdirs();
        writeToFile(pathname, text);
    }

    private File imageFile(String ext) {
        return new File(fc.getPath() + "/" + filename + "." + ext);
    }

    public void copyImage(File file, String ext) throws IOException {
        FileUtils.copyFile(file, imageFile(ext));
    }

    public void copyResizedImage(BufferedImage f, String ext) throws IOException {
        ImageIO.write(f, ext, imageFile(ext));
    }
}
