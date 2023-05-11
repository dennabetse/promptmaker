package com.este.promptmaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = loadResource();
        Scene scene = new Scene(fxmlLoader.load());
        stage.getProperties().put("hostServices", getHostServices());
        stage.getIcons().add(new Image("sparklinlabs.png"));
        stage.setTitle("PromptMaker");
        stage.setScene(scene);
        stage.setMinHeight(735);
        stage.setMinWidth(528);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static FXMLLoader loadResource() {
        String os = System.getProperty("os.name");
        if (os.contains("Linux")) {
            return new FXMLLoader(MainApplication.class.getResource("linux-layout.fxml"));
        } else if (os.contains("Mac")) {
            return new FXMLLoader(MainApplication.class.getResource("mac-layout.fxml"));
        }
        return new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
    }
}