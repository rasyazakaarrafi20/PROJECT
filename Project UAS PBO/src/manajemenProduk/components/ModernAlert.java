package manajemenProduk.components;

import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModernAlert {

    public static void show(

        String title,
        String message
    ) {

        Stage stage =
            new Stage();

        // TITLE
        Label lblTitle =
            new Label(title);

        lblTitle.getStyleClass().add(
            "modern-alert-title"
        );

        // MESSAGE
        Label lblMessage =
            new Label(message);

        lblMessage.getStyleClass().add(
            "modern-alert-message"
        );

        // BUTTON
        Button btnOK =
            new Button("OK");

        btnOK.getStyleClass().add(
            "modern-alert-button"
        );

        btnOK.setOnAction(e -> {

            stage.close();
        });

        // ROOT
        VBox root =
            new VBox(20);

        root.setAlignment(
            Pos.CENTER
        );

        root.getChildren().addAll(

            lblTitle,
            lblMessage,
            btnOK
        );

        root.getStyleClass().add(
            "modern-alert-root"
        );

        // SCENE
        Scene scene =
            new Scene(root, 380, 220);

        scene.getStylesheets().add(

            ModernAlert.class
                .getResource("/style/alert.css")
                .toExternalForm()
        );

        stage.initModality(
            Modality.APPLICATION_MODAL
        );

        stage.setResizable(false);

        stage.setScene(scene);

        stage.showAndWait();
    }
}