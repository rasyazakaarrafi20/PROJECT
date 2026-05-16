package manajemenProduk.components;

import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModernConfirm {

    private static boolean confirmed = false;

    public static boolean show(

        String title,
        String message
    ) {

        confirmed = false;

        Stage stage =
            new Stage();

        // TITLE
        Label lblTitle =
            new Label(title);

        lblTitle.getStyleClass().add(
            "confirm-title"
        );

        // MESSAGE
        Label lblMessage =
            new Label(message);

        lblMessage.getStyleClass().add(
            "confirm-message"
        );

        // BUTTON CANCEL
        Button btnCancel =
            new Button("Batal");

        btnCancel.getStyleClass().add(
            "confirm-cancel"
        );

        // BUTTON OK
        Button btnOK =
            new Button("Ya");

        btnOK.getStyleClass().add(
            "confirm-delete"
        );

        // ACTION
        btnCancel.setOnAction(e -> {

            confirmed = false;

            stage.close();
        });

        btnOK.setOnAction(e -> {

            confirmed = true;

            stage.close();
        });

        // BUTTON BOX
        HBox buttonBox =
            new HBox(15);

        buttonBox.setAlignment(
            Pos.CENTER
        );

        buttonBox.getChildren().addAll(

            btnCancel,
            btnOK
        );

        // ROOT
        VBox root =
            new VBox(25);

        root.setAlignment(
            Pos.CENTER
        );

        root.getChildren().addAll(

            lblTitle,
            lblMessage,
            buttonBox
        );

        root.getStyleClass().add(
            "confirm-root"
        );

        // SCENE
        Scene scene =
            new Scene(root, 400, 230);

        scene.getStylesheets().add(

            ModernConfirm.class
                .getResource("/style/confirm.css")
                .toExternalForm()
        );

        stage.setScene(scene);

        stage.initModality(
            Modality.APPLICATION_MODAL
        );

        stage.showAndWait();

        return confirmed;
    }
}