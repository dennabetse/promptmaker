package com.este.promptmaker;

import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Setting {

    private final HashMap<String, String> settings;

    public Setting() throws IOException {
        settings = readFile();
    }

    private HashMap<String, String> readFile() throws IOException {
        HashMap<String, String> settings = new HashMap<>();
        List<String> lines = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get("settings.ini"))) {
            stream.forEach(lines::add);
        } catch (IOException e) {
            File file = new File("settings.ini");
            if (file.createNewFile()){
                System.out.println("Configuration file created.");
            }
        }

        for (String l : lines) {
            String[] parts = l.split("=");
            if (parts.length == 2) {
                settings.put(parts[0], parts[1]);
            }
        }
        return settings;
    }

    public String get(String key) {
        String defaultValue = "";
        switch (key) {
            case "locale" -> defaultValue = "English";
            case "tags" -> defaultValue = "en";
        }
        return settings.getOrDefault(key, defaultValue);
    }

    public void set(String key, String value){
        settings.put(key, value);
    }

    public void chooseDirectory() throws IOException {
        DirectoryChooser directory = new DirectoryChooser();
        directory.setInitialDirectory(new File("../"));
        File selectedDirectory = directory.showDialog(null);
        if (selectedDirectory != null) {
            settings.put("folder_output", selectedDirectory.getCanonicalPath());
        }
    }

    private String getSettings() {
        List<String> save = new ArrayList<>();
        save.add("locale=" + get("locale"));
        save.add("tags=" + get("tags"));
        save.add("folder_output=" + get("folder_output"));

        StringBuilder sb = new StringBuilder();
        for (String s : save){
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void save() throws IOException {
        FileManipulator fm = new FileManipulator();
        fm.writeToFile(new File("settings.ini"), getSettings());
    }
}
