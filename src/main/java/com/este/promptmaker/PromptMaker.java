package com.este.promptmaker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.CaseUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.Normalizer;
import java.util.List;

public class PromptMaker {
    private String prompt;
    private String text;
    private List<String> sources;

    private List<String> shorthands;

    private String detail;

    private String submitter;

    private List<String> tags;

    public PromptMaker(){

    }

    public PromptMaker(String prompt, String text, List<String> sources, List<String> shorthands, String detail, String submitter, List<String> tags){
        this(prompt,sources,shorthands,detail,submitter,tags);
        this.text = text;
    }

    public PromptMaker(String prompt, List<String> sources, List<String> shorthands, String detail, String submitter, List<String> tags){
        this.prompt = prompt;
        this.sources = sources;
        this.shorthands = shorthands;
        this.detail = detail;
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

    public String getDetail() {
        return detail;
    }

    public String getSubmitter() {
        return submitter;
    }

    public List<String> getTags() {
        return tags;
    }

    public String printText(){
        if (getText() == null){
            return null;
        }
        return "\"" + getText() + "\"";
    }
    public String printSources(){
        String string = "";
        int cnt = 1;
        for (String s : getSources()){
            string += "\"" + s +"\"";
            if (cnt < getSources().size()){
                string += ",\n    ";
            }
            cnt++;
        }
        return string;
    }

    public String printShorthands(){
        if (getShorthands().isEmpty()){
            return "[]";
        }
        String string = "[\n    ";
        int cnt = 1;
        for (String s : getShorthands()){
            string += "\"" + s +"\"";
            if (cnt < getShorthands().size()){
                string += ",\n    ";
            }
            cnt++;
        }
        string += "\n  ]";
        return string;
    }

    public String printTags(){
        String string = "";
        int cnt = 1;
        for (String s : getTags()){
            string += "\"" + s +"\"";
            if (cnt < getTags().size()){
                string += ",\n    ";
            }
            cnt++;
        }
        return string;
    }

    public String save(){
        return "{\n  \"prompt\": \"" + getPrompt()
                + "\",\n  \"text\": " + printText()
                + ",\n  \"source\": [\n    " + printSources()
                + "\n  ],\n  \"shorthand\": " + printShorthands()
                + ",\n  \"details\": \"" + getDetail()
                + "\",\n  \"submitter\": \""+ getSubmitter()
                + "\",\n  \"tags\": [\n    " + printTags()
                + "\n  ]\n}";
    }

    public void writeToFile(String fileName, String text) throws Exception {
        if (fileName.isBlank()){
            fileName = "sample";
        }
        String title = CaseUtils.toCamelCase(fileName, false, ' ', '_');

        String convertedString = Normalizer
                .normalize(title, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9]+","");

        int num = 1;
        String save = convertedString + ".json";
        File file = new File("../" + save);
        while(file.exists()) {
            save = convertedString + "_" + (num++) + ".json";
            file = new File("../" + save);
        }

        PrintWriter writer = new PrintWriter("../" + save, StandardCharsets.UTF_8);
        writer.println(text);
        writer.close();
    }

    public void copyFile(File selectedFile, BufferedImage image, String fileName, Boolean checked) throws Exception {
        if (fileName.isBlank()){
            fileName = "sample";
        }
        String title = CaseUtils.toCamelCase(fileName, false, ' ', '_');

        String convertedString = Normalizer
                .normalize(title, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9]+","");


        String ext = FilenameUtils.getExtension(selectedFile.getName());

        int num = 1;
        String save = convertedString + "." + ext;
        File file = new File("../" + save);
        while(file.exists()) {
            save = convertedString + "_" + (num++) + "." + ext;
            file = new File("../" + save);
        }

        BufferedImage f = simpleResizeImage(image, 1024);
        File outputfile = new File("../" + save);
        if (checked){
            ImageIO.write(f, ext, outputfile);
        } else{
            ImageIO.write(image, ext, outputfile);
        }
    }

    private BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {
        return Scalr.resize(originalImage, targetWidth);
    }
}
