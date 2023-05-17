package com.este.promptmaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = loadResource();
        Scene scene = new Scene(fxmlLoader.load());
        stage.getProperties().put("hostServices", getHostServices());
        stage.getIcons().add(new Image("sparklinlabs.png"));
        stage.setTitle("PromptMaker");
        stage.setScene(scene);

        String os = System.getProperty("os.name");
        if (os.contains("Win")) {
            stage.setMinHeight(735);
            stage.setMinWidth(944);
        } else if (os.contains("Mac")) {
            stage.setMinHeight(696);
            stage.setMinWidth(962);
        } else {
            stage.setMinHeight(696);
            stage.setMinWidth(998);
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static FXMLLoader loadResource() throws IOException {
        Setting settings = new Setting();
        ResourceBundle bundle = ResourceBundle.getBundle("com.este.promptmaker.locale", new Locale(settings.get("locale")));
        String os = System.getProperty("os.name");
        if (os.contains("Linux")) {
            return new FXMLLoader(MainApplication.class.getResource("linux-layout.fxml"), bundle);
        } else if (os.contains("Mac")) {
            return new FXMLLoader(MainApplication.class.getResource("mac-layout.fxml"), bundle);
        }
        return new FXMLLoader(MainApplication.class.getResource("main-view.fxml"), bundle);
    }
}