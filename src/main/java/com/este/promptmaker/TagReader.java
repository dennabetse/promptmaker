package com.este.promptmaker;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TagReader {

    private final List<String> english;
    private final List<String> french;
    private final List<String> german;
    private final List<String> hungarian;
    private final List<String> spanish;

    public TagReader() {
        english = readFile("tags-en.txt");
        french = readFile("tags-fr.txt");
        german = readFile("tags-de.txt");
        hungarian = readFile("tags-hu.txt");
        spanish = readFile("tags-es.txt");
    }

    private static List<String> readFile(String file) {
        List<String> rows = new ArrayList<>();
        try (Stream<String> line = Files.lines(Paths.get("config/" + file))) {
            line.filter(row -> !row.isEmpty())
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
        StringBuilder tags = new StringBuilder();
        for (String tag : list) {
            if (!tag.isEmpty()) {
                tags.append(tag);
                tags.append("\n");
            }
        }
        return tags.toString();
    }
}
