package com.este.promptmaker;

import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;

public class FolderChooser {

    private String path;

    public FolderChooser() throws IOException {
        path = readFile();
    }

    public String getPath() {
        return path;
    }

    private String readFile() throws IOException {
        try (Scanner scanner = new Scanner(Paths.get("settings.ini"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                return line;
            }
        } catch (Exception e) {
            File settings = new File("settings.ini");
            settings.createNewFile();
        }
        return "";
    }

    public void chooseDirectory() throws IOException {
        DirectoryChooser directory = new DirectoryChooser();
        directory.setInitialDirectory(new File("../"));
        File selectedDirectory = directory.showDialog(null);
        if (selectedDirectory != null) {
            path = selectedDirectory.getCanonicalPath();
        }
    }

    public void savePath() throws IOException {
        PrintWriter writer = new PrintWriter("settings.ini", StandardCharsets.UTF_8);
        writer.println(path);
        writer.close();
    }
}
