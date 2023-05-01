package com.este.promptmaker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.CaseUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;

public class PromptMaker {
    private String prompt;
    private String text;
    private List<String> sources;
    private List<String> shorthands;
    private String details;
    private String submitter;
    private List<String> tags;

    public PromptMaker() {

    }

    public PromptMaker(String prompt, String text, List<String> sources, List<String> shorthands, String detail, String submitter, List<String> tags) {
        this(prompt, sources, shorthands, detail, submitter, tags);
        this.text = text;
    }

    public PromptMaker(String prompt, List<String> sources, List<String> shorthands, String detail, String submitter, List<String> tags) {
        this.prompt = prompt;
        this.sources = sources;
        this.shorthands = shorthands;
        this.details = detail;
        this.submitter = submitter;
        this.tags = tags;
        this.text = null;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getText() {
        return text;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getShorthands() {
        return shorthands;
    }

    public String getDetails() {
        return details;
    }

    public String getSubmitter() {
        return submitter;
    }

    public List<String> getTags() {
        return tags;
    }

    public String printText() {
        if (getText() == null) {
            return null;
        }
        return "\"" + getText() + "\"";
    }

    public String printSource() {
        String string = "";
        int cnt = 1;
        for (String s : getSources()) {
            string += "\"" + s + "\"";
            if (cnt < getSources().size()) {
                string += ",\n    ";
            }
            cnt++;
        }
        return string;
    }

    public String printShorthand() {
        if (getShorthands().isEmpty()) {
            return "[]";
        }
        String string = "[\n    ";
        int cnt = 1;
        for (String s : getShorthands()) {
            string += "\"" + s + "\"";
            if (cnt < getShorthands().size()) {
                string += ",\n    ";
            }
            cnt++;
        }
        string += "\n  ]";
        return string;
    }

    public String printTags() {
        String string = "";
        int cnt = 1;
        for (String s : getTags()) {
            string += "\"" + s + "\"";
            if (cnt < getTags().size()) {
                string += ",\n    ";
            }
            cnt++;
        }
        return string;
    }

    public String save() {
        return "{\n  \"prompt\": \"" + getPrompt()
                + "\",\n  \"text\": " + printText()
                + ",\n  \"source\": [\n    " + printSource()
                + "\n  ],\n  \"shorthand\": " + printShorthand()
                + ",\n  \"details\": \"" + getDetails()
                + "\",\n  \"submitter\": \"" + getSubmitter()
                + "\",\n  \"tags\": [\n    " + printTags()
                + "\n  ]\n}";
    }

    public void createFile(String name, String content) throws IOException {
        String convertedName = toCamelCase(name);

        int num = 1;
        String filename = convertedName + ".json";
        File file = new File("../" + filename);
        while (file.exists()) {
            filename = convertedName + "_" + (num++) + ".json";
            file = new File("../" + filename);
        }

        writeToFile(filename, content);

        System.out.println("\nJSON located at: " + file.getCanonicalPath());
        System.out.println();
    }

    public void createFile(String name, String content, File image, String ext, Boolean checked) throws IOException {
        String convertedName = toCamelCase(name);

        int num = 1;
        String filename = convertedName + ".json";
        String imageName = convertedName + "." + ext;
        File file = new File("../" + filename);
        File imageFile = new File("../" + imageName);
        while (file.exists()) {
            filename = convertedName + "_" + num + ".json";
            imageName = convertedName + "_" + num + "." + ext;
            num++;

            file = new File("../" + filename);
            imageFile = new File("../" + imageName);
        }

        writeToFile(filename, content);

        System.out.println();

        if (checked) {
            BufferedImage bi = ImageIO.read(new File(image.getPath()));
            if (!(bi == null)) {
                BufferedImage f = simpleResizeImage(bi, 1024);
                ImageIO.write(f, ext, imageFile);
                System.out.println("Image has been resized.");
            } else {
                FileUtils.copyFile(image, imageFile);
                System.out.println("Resize failed. Image has been copied without any change.");
            }
        } else {
            FileUtils.copyFile(image, imageFile);
            System.out.println("Image copied.");
        }

        System.out.println("\nFiles located at:\n" + file.getCanonicalPath());
        System.out.println(imageFile.getCanonicalPath());
        System.out.println();
    }

    private String toCamelCase(String name) {
        if (name.isBlank()) {
            name = "untitled";
        }
        String title = CaseUtils.toCamelCase(name, false, ' ', '_');

        return Normalizer
                .normalize(title, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9]+", "");
    }

    private void writeToFile(String fileName, String text) throws IOException {
        PrintWriter writer = new PrintWriter("../" + fileName, StandardCharsets.UTF_8);
        writer.println(text);
        writer.close();
    }

    private BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) {
        return Scalr.resize(originalImage, targetWidth);
    }
}
