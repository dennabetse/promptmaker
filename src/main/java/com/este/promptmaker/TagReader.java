package com.este.promptmaker;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TagReader {

    private List<String> english;
    private List<String> french;
    private List<String> german;
    private List<String> hungarian;
    private List<String> spanish;

    public TagReader() {
        english = readFile("tags-en.txt");
        french = readFile("tags-fr.txt");
        german = readFile("tags-de.txt");
        hungarian = readFile("tags-hu.txt");
        spanish = readFile("tags-es.txt");
    }

    private static List<String> readFile(String file) {
        List<String> rows = new ArrayList<>();
        try {
            Files.lines(Paths.get("config/" + file))
                    .filter(row -> !row.isEmpty())
                    .forEach(row -> rows.add(row.trim()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return rows;
    }

    public List<String> getEnglish() {
        return english;
    }

    public List<String> getFrench() {
        return french;
    }

    public List<String> getGerman() {
        return german;
    }

    public List<String> getHungarian() {
        return hungarian;
    }

    public List<String> getSpanish() {
        return spanish;
    }

    public String printTags(List<String> list) {
        String tags = "";
        for (String tag : list) {
            if (!tag.isEmpty()) {
                tags += tag + "\n";
            }
        }
        return tags;
    }
}
