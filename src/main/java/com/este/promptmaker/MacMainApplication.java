package com.este.promptmaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MacMainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MacMainApplication.class.getResource("mac-layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
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
}