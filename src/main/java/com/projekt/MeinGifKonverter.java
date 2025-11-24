package com.projekt;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

public class MeinGifKonverter extends Application {
    @Override
    public void start(Stage stage) throws Exception {


        Button btnOpen = new Button("Video auswählen & Konvertieren");


        btnOpen.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wähle ein Video");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Video Dateien", "*.mp4", "*.mov")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                System.out.println("Datei gewählt: " + selectedFile.getAbsolutePath());

                String inputPath = selectedFile.getAbsolutePath();
                String outputPath = inputPath.substring(0, inputPath.lastIndexOf('.')) + ".gif";
                File outputFile = new File(outputPath);

                startKonverter(selectedFile, outputFile);

            }
        });
        StackPane root = new StackPane();
        root.getChildren().add(btnOpen);

        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("GIF Konverter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startKonverter(File quellVideo, File zielGif) {

        Task<Void> task = new Task<Void>() {
           @Override
           protected Void call() throws Exception {
               Mp4ToGif.convertMp4ToGif(quellVideo, zielGif);
               return null;
           }

           @Override
            protected void succeeded() {
               System.out.println(quellVideo + "wurde erfolgreich erstellt");
           }

           @Override
           protected void failed() {
               System.out.println(quellVideo + "wurde nicht erstellt");
                getException().printStackTrace();
           }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();



    }

}
