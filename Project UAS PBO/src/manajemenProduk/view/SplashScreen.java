package manajemenProduk.view;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

import javafx.application.Platform;

import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.util.Duration;

import manajemenProduk.mainprogram.MainApp;

public class SplashScreen {

    private MainApp app;

    // CONSTRUCTOR
    public SplashScreen(MainApp app) {

        this.app = app;
    }

    // SHOW SPLASH
    public void show(Stage stage) {

        // =========================
        // LOGO
        // =========================
        Image logoImage = new Image(
            "file:src/resources/images/MAMAT.png"
        );

        ImageView logo =
            new ImageView(logoImage);

        logo.setFitWidth(220);
        logo.setFitHeight(220);

        logo.setPreserveRatio(true);

        // EFFECT
        Glow glow = new Glow();

        glow.setLevel(0.8);

        DropShadow shadow =
            new DropShadow();

        shadow.setRadius(30);

        shadow.setColor(Color.CYAN);

        logo.setEffect(shadow);

        // =========================
        // TITLE
        // =========================
        Label title =
            new Label("SI MAMAT");

        title.getStyleClass().add(
            "splash-title"
        );

        // =========================
        // SUBTITLE
        // =========================
        Label subtitle =
            new Label(
                "Sistem Manajemen Produk"
            );

        subtitle.getStyleClass().add(
            "splash-subtitle"
        );

        // =========================
        // LOADING TEXT
        // =========================
        Label loadingText =
            new Label(
                "Loading System..."
            );

        loadingText.getStyleClass().add(
            "loading-text"
        );

        // =========================
        // LOADING BAR
        // =========================
        ProgressBar loadingBar =
            new ProgressBar();

        loadingBar.setPrefWidth(350);

        loadingBar.setPrefHeight(12);

        loadingBar.getStyleClass().add(
            "loading-bar"
        );

        // =========================
        // CONTENT
        // =========================
        VBox content =
            new VBox(18);

        content.setAlignment(
            Pos.CENTER
        );

        content.getChildren().addAll(

            logo,
            title,
            subtitle,
            loadingBar,
            loadingText
        );

        // =========================
        // ROOT
        // =========================
        StackPane root =
            new StackPane(content);

        root.getStyleClass().add(
            "splash-root"
        );

        // =========================
        // SCENE
        // =========================
        Scene scene =
            new Scene(
                root,
                1280,
                720
            );

        // LOAD CSS
        scene.getStylesheets().add(

            getClass()
                .getResource("/style/splash.css")
                .toExternalForm()
        );

        stage.setScene(scene);

        stage.show();

        // =========================
        // FADE ANIMATION
        // =========================
        FadeTransition fade =
            new FadeTransition(

                Duration.seconds(2),
                content
            );

        fade.setFromValue(0);

        fade.setToValue(1);

        fade.play();

        // =========================
        // SCALE ANIMATION
        // =========================
        ScaleTransition scale =
            new ScaleTransition(

                Duration.seconds(2),
                logo
            );

        scale.setFromX(0.5);

        scale.setFromY(0.5);

        scale.setToX(1);

        scale.setToY(1);

        scale.play();

        // =========================
        // FLOAT ANIMATION
        // =========================
        TranslateTransition floatAnim =
            new TranslateTransition(

                Duration.seconds(2),
                logo
            );

        floatAnim.setFromY(0);

        floatAnim.setToY(-10);

        floatAnim.setCycleCount(
            TranslateTransition.INDEFINITE
        );

        floatAnim.setAutoReverse(true);

        floatAnim.play();

        // =========================
        // ROTATE ANIMATION
        // =========================
        RotateTransition rotate =
            new RotateTransition(

                Duration.seconds(10),
                logo
            );

        rotate.setByAngle(360);

        rotate.setCycleCount(
            RotateTransition.INDEFINITE
        );

        rotate.play();

        // =========================
        // LOADING EFFECT
        // =========================
        new Thread(() -> {

            for (int i = 0; i <= 100; i++) {

                final double progress =
                    i / 100.0;

                final int percent =
                    i;

                try {

                    Thread.sleep(30);

                } catch (Exception e) {

                    e.printStackTrace();
                }

                Platform.runLater(() -> {

                    loadingBar.setProgress(
                        progress
                    );

                    loadingText.setText(

                        "Loading System... " +

                        percent + "%"
                    );
                });
            }
        }).start();

        // =========================
        // DELAY
        // =========================
        PauseTransition delay =
            new PauseTransition(

                Duration.seconds(4)
            );

        delay.setOnFinished(e -> {

            app.showMainApp(stage);
        });

        delay.play();
    }
}