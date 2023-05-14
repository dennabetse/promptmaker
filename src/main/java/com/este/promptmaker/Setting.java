package com.este.promptmaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Setting {

    private HashMap<String, String> settings;

    public Setting() throws IOException {
        load();
    }

    public void load() throws IOException {
        settings = readFile();
    }

    public String settingsFile() {
        return "settings.ini";
    }

    private HashMap<String, String> readFile() throws IOException {
        HashMap<String, String> settings = new HashMap<>();
        List<String> lines = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(settingsFile()))) {
            stream.forEach(lines::add);
        } catch (IOException e) {
            File file = new File(settingsFile());
            if (file.createNewFile()) {
                System.out.println("-----------------------------------------------\nConfiguration file created.\n-----------------------------------------------\n");
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
        if (key.equals("locale") || key.equals("tags_language")) {
            defaultValue = "en";
        }
        return settings.getOrDefault(key, defaultValue);
    }

    public void set(String key, String value) {
        settings.put(key, value);
    }

    private String getSettings() {
        List<String> save = new ArrayList<>();
        save.add("locale=" + get("locale") + "\n");
        save.add("tags_language=" + get("tags_language") + "\n");
        save.add("folder_output=" + get("folder_output") + "\n");
        save.add("last_folder=" + get("last_folder"));

        StringBuilder sb = new StringBuilder();
        for (String s : save) {
            sb.append(s);
        }
        return sb.toString();
    }

    public boolean changed() throws IOException {
        return !readFile().equals(settings);
    }

    public void save() throws IOException {
        FileManipulator fm = new FileManipulator();
        if (changed()) {
            fm.writeToFile(new File(settingsFile()), getSettings());
            load();
        }
    }
}
