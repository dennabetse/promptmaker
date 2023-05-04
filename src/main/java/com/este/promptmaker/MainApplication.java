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
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image("sparklinlabs.png"));
        stage.setTitle("PromptMaker v0.2");
        stage.setScene(scene);
        stage.setMinHeight(728);
        stage.setMinWidth(512);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}