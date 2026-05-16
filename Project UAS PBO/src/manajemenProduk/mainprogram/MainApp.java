package manajemenProduk.mainprogram;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import manajemenProduk.services.GudangManager;

import manajemenProduk.view.SplashScreen;
import manajemenProduk.view.HomeView;
import manajemenProduk.view.TambahProdukView;
import manajemenProduk.view.DaftarProdukView;
import manajemenProduk.view.ProdukDiskonView;
import manajemenProduk.view.AboutView;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        SplashScreen splash =
            new SplashScreen(this);

        splash.show(stage);
    }

    // =========================
    // MAIN APP
    // =========================
    public void showMainApp(Stage stage) {

        GudangManager gm =
            new GudangManager();

        BorderPane root =
            new BorderPane();

        // =========================
        // SIDEBAR
        // =========================
        VBox sidebar =
            new VBox(25);

        sidebar.setPadding(
            new Insets(30)
        );

        sidebar.setAlignment(
            Pos.TOP_CENTER
        );

        sidebar.setPrefWidth(220);

        sidebar.getStyleClass().add(
            "sidebar"
        );

        // =========================
        // LOGO
        // =========================
        Image logo =
            new Image(
                "file:src/resources/images/SIMAMAT.png"
            );

        ImageView logoView =
            new ImageView(logo);

        logoView.setFitWidth(180);
        logoView.setFitHeight(180);
        logoView.setPreserveRatio(true);

        Label title =
            new Label("SI MAMAT");

        title.setStyle(
            "-fx-font-size: 30px;"
            + "-fx-text-fill: white;"
             + "-fx-font-weight: bold;"
        );

        // =========================
        // BUTTON MENU
        // =========================
        Button btnHome =
            new Button("Home");

        Button btnTambah =
            new Button("Tambah Produk");

        Button btnDaftar =
            new Button("Daftar Produk");

        Button btnDiskon =
            new Button("Produk Diskon");

        Button btnAbout =
            new Button("About");

        Button[] buttons = {

            btnHome,
            btnTambah,
            btnDaftar,
            btnDiskon,
            btnAbout
        };

        // =========================
        // STYLE BUTTON
        // =========================
        for (Button btn : buttons) {

            btn.setPrefWidth(160);
            btn.setPrefHeight(45);

            btn.getStyleClass().add(
                "sidebar-button"
            );
        }

        // =========================
        // RESET ACTIVE BUTTON
        // =========================
        Runnable resetButton = () -> {

            for (Button btn : buttons) {

                btn.getStyleClass().remove(
                    "sidebar-button-active"
                );
            }
        };

        // =========================
        // SIDEBAR COMPONENT
        // =========================
        sidebar.getChildren().addAll(

            title,
            logoView,

            btnHome,
            btnTambah,
            btnDaftar,
            btnDiskon,
            btnAbout
        );

        // =========================
        // DEFAULT PAGE
        // =========================
        HomeView home =
            new HomeView(gm, root);

        root.setCenter(
            home.getView()
        );

        btnHome.getStyleClass().add(
            "sidebar-button-active"
        );

        // =========================
        // BUTTON ACTION
        // =========================

        // HOME
        btnHome.setOnAction(e -> {

            resetButton.run();

            btnHome.getStyleClass().add(
                "sidebar-button-active"
            );

            HomeView hv =
                new HomeView(gm, root);

            root.setCenter(
                hv.getView()
            );
        });

        // TAMBAH PRODUK
        btnTambah.setOnAction(e -> {

            resetButton.run();

            btnTambah.getStyleClass().add(
                "sidebar-button-active"
            );

            TambahProdukView tambah =
                new TambahProdukView(gm, root);

            root.setCenter(
                tambah.getView()
            );
        });

        // DAFTAR PRODUK
        btnDaftar.setOnAction(e -> {

            resetButton.run();

            btnDaftar.getStyleClass().add(
                "sidebar-button-active"
            );

            DaftarProdukView daftar =
                new DaftarProdukView(gm, root);

            root.setCenter(
                daftar.getView()
            );
        });

        // PRODUK DISKON
        btnDiskon.setOnAction(e -> {

            resetButton.run();

            btnDiskon.getStyleClass().add(
                "sidebar-button-active"
            );

            ProdukDiskonView diskon =
                new ProdukDiskonView(gm);

            root.setCenter(
                diskon.getView()
            );
        });

        // ABOUT
        btnAbout.setOnAction(e -> {

            resetButton.run();

            btnAbout.getStyleClass().add(
                "sidebar-button-active"
            );

            AboutView about =
                new AboutView(root);

            root.setCenter(
                about.getView()
            );
        });

        // =========================
        // ROOT
        // =========================
        root.setLeft(sidebar);

        // =========================
        // SCENE
        // =========================
        Scene scene =
            new Scene(
                root,
                1280,
                720
            );

        scene.getStylesheets().add(

            getClass()
                .getResource("/style/style.css")
                .toExternalForm()
        );

        // =========================
        // STAGE
        // =========================
        stage.setTitle(
            "Manajemen Produk"
        );

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}