package com.este.promptmaker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

public class FileManipulator {

    private final Setting settings;
    private String filename;

    public FileManipulator() throws IOException {
        settings = new Setting();
    }

    private String toCamelCase(String name) {
        if (name.isEmpty()) {
            name = "untitled";
        }

        return Normalizer
                .normalize(CaseUtils.toCamelCase(name, false, ' ', '_'), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9]+", "");
    }

    private void setFilename(String name) {
        String convertedName = toCamelCase(StringUtils.left(name, 123));

        int num = 1;
        filename = convertedName + "_" + num;
        File file = makeFile("json");
        if (file.getParentFile().mkdirs()) {
            System.out.println();
        }
        while (file.exists() && num < 1001) {
            filename = convertedName + "_" + (num++);
            file = makeFile("json");
        }
    }

    private File makeFile(String ext) {
        return new File(settings.get("folder_output") + "/" + filename + "." + ext);
    }

    public void writeToFile(File file, String text) throws IOException {
        PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
        writer.println(text);
        writer.close();
    }

    public void saveTags(String languageFile, String text) throws IOException {
        File file = new File("config/" + languageFile);
        if (file.exists() || file.getParentFile().mkdirs()) {
            writeToFile(file, text);
        }
    }

    public void makeJson(String name, String text) throws IOException {
        setFilename(name);
        writeToFile(makeFile("json"), text);
    }

    public void copyImage(File file, String ext) throws IOException {
        FileUtils.copyFile(file, makeFile(ext));
    }

    public void copyResizedImage(BufferedImage bufferedImage, String ext) throws IOException {
        ImageIO.write(bufferedImage, ext, makeFile(ext));
    }
}
