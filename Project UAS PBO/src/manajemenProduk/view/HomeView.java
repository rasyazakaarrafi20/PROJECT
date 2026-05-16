package manajemenProduk.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

import manajemenProduk.model.Produk;
import manajemenProduk.services.DatabaseConfig;
import manajemenProduk.services.GudangManager;

public class HomeView {

    GudangManager gm;
    BorderPane root;

    // CONSTRUCTOR
    public HomeView(
        GudangManager gm,
        BorderPane root
    ) {

        this.gm = gm;
        this.root = root;
    }

    // VIEW
    public VBox getView() {

        // ROOT STYLE
        root.getStyleClass().add(
            "main-background"
        );

        // TOPBAR
        HBox topbar = new HBox();
        
        topbar.setPadding(new Insets(15, 30, 15, 30));
        topbar.setAlignment(Pos.CENTER_LEFT);topbar.getStyleClass().add("topbar");
        
        // KIRI
        Label topText = new Label("Kami Siap Membantu Anda");
        topText.getStyleClass().add("topbar-text");
        
        // SPACER
        Region spacer = new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);
        
        // KANAN
        Label kelompok = new Label("KELOMPOK 3 👤");
        
        kelompok.getStyleClass().add("kelompok-text");
        
        // ADD
        topbar.getChildren().addAll(
            topText,
            spacer,
            kelompok
        );

        // CONTENT
        VBox content = new VBox(30);

        content.setPadding(
            new Insets(30)
        );

        // CARD DATA
        HBox cards = new HBox(50);

        cards.setAlignment(
            Pos.CENTER
        );

        // TOTAL PRODUK
        int totalProduk = 0;

        try {

            Connection conn =
                DatabaseConfig.getConnection();

            String sql =
                "SELECT COUNT(*) FROM produk";

            PreparedStatement ps =
                conn.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

            if (rs.next()) {

                totalProduk =
                    rs.getInt(1);
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        VBox cardProduk = createCard(
            "Total Produk",
            String.valueOf(totalProduk)
        );

        // TOTAL STOK
        int totalStok = 0;

        try {

            Connection conn =
                DatabaseConfig.getConnection();

            String sql =
                "SELECT SUM(stok) FROM produk";

            PreparedStatement ps =
                conn.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

            if (rs.next()) {

                totalStok =
                    rs.getInt(1);
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        VBox cardStok = createCard(
            "Total Stok",
            String.valueOf(totalStok)
        );

        // PRODUK BARU
        String produkBaru = "-";

        try {

            Connection conn =
                DatabaseConfig.getConnection();

            String sql =
                "SELECT nama FROM produk ORDER BY id DESC LIMIT 1";

            PreparedStatement ps =
                conn.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

            if (rs.next()) {

                produkBaru =
                    rs.getString("nama");
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        VBox cardBaru = createCard(
            "Produk Baru",
            produkBaru
        );

        cards.getChildren().addAll(
            cardProduk,
            cardStok,
            cardBaru
        );

        // SEARCH
        TextField searchField =
            new TextField();

        searchField.setPromptText(
            "Cari produk..."
        );

        searchField.getStyleClass().add(
            "search-field"
        );
        searchField.setMaxWidth(
            Double.MAX_VALUE
        );

        // HASIL SEARCH
        VBox hasilSearchBox =
            new VBox(10);

        hasilSearchBox.getStyleClass().add(
            "diskon-box"
        );

        // SEARCH ACTION
        searchField.textProperty().addListener(
            (observable, oldValue, newValue) -> {

                hasilSearchBox.getChildren().clear();

                if (!newValue.isEmpty()) {

                    try {

                        Connection conn =
                            DatabaseConfig.getConnection();

                        String sql =
                            "SELECT * FROM produk WHERE LOWER(nama) LIKE ?";

                        PreparedStatement ps =
                            conn.prepareStatement(sql);

                        ps.setString(
                            1,
                            "%" + newValue.toLowerCase() + "%"
                        );

                        ResultSet rs =
                            ps.executeQuery();

                        while (rs.next()) {

                            Label hasil =
                                new Label(

                                    rs.getString("nama") +

                                    " | Rp." +

                                    rs.getInt("harga")
                                );

                            hasil.getStyleClass().add(
                                "diskon-item"
                            );

                            hasilSearchBox
                                .getChildren()
                                .add(hasil);
                        }

                        conn.close();

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
            }
        );

        // SEARCH + BUTTON SECTION
        HBox topAction = new HBox(15);
        topAction.setPrefWidth(1000);
        
        topAction.setAlignment(Pos.CENTER_LEFT);
        
        // BUTTON
        Button btnLihat = new Button("Daftar Produk");
        Button btnTambah = new Button("Tambah Produk");
        
        btnLihat.getStyleClass().add("action-button");
        btnTambah.getStyleClass().add("action-button");
        
        // WRAPPER SEARCH
        VBox searchSection = new VBox(10);
        HBox.setHgrow(searchSection,Priority.ALWAYS);
        
        searchSection.setMaxWidth(Double.MAX_VALUE);searchSection.setAlignment(Pos.CENTER_LEFT);

        searchSection.getChildren().addAll(
            searchField,
            hasilSearchBox
        );

        // MASUKKAN KE TOPACTION
        topAction.getChildren().addAll(
            searchSection,
            btnLihat,
            btnTambah
        );
        
        // BUTTON ACTION
        // PINDAH KE DAFTAR PRODUK
        btnLihat.setOnAction(e -> {

            DaftarProdukView daftar =
                new DaftarProdukView(gm, root);

            root.setCenter(
                daftar.getView()
            );
        });

        // PINDAH KE TAMBAH PRODUK
        btnTambah.setOnAction(e -> {

            TambahProdukView tambah =
                new TambahProdukView(gm, root);

            root.setCenter(
                tambah.getView()
            );
        });

        // PRODUK DISKON TITLE
        Label diskonTitle =
            new Label(
                "Produk Diskon"
            );

        diskonTitle.getStyleClass().add(
            "section-title"
        );


        // BOX PRODUK DISKON
        // =========================
        VBox diskonBox = new VBox(10);

        diskonBox.setPrefHeight(220);

        diskonBox.getStyleClass().add(
            "diskon-box"
        );

        // TAMPILKAN PRODUK DISKON
        try {
            Connection conn =
                DatabaseConfig.getConnection();

            String sql =
                "SELECT * FROM produk WHERE diskon > 0";

            PreparedStatement ps =
                conn.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

            while (rs.next()) {

                Label produkDiskon =
                    new Label(

                        rs.getString("nama") +

                        " | Harga: Rp." +

                        rs.getInt("harga") +

                        " | Diskon: " +

                        rs.getInt("diskon") + "%"
                    );

                produkDiskon.getStyleClass().add(
                    "diskon-item"
                );

                diskonBox.getChildren().add(
                    produkDiskon
                );
            }

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        // =========================
        // ADD CONTENT
        // =========================
        content.getChildren().addAll(
    cards,
    topAction,
    diskonTitle,
    diskonBox
);
        BorderPane layout = new BorderPane();
        layout.setTop(topbar);
        layout.setCenter(content);
        return new VBox(layout);
    }

    // =========================
    // CARD METHOD
    // =========================
    private VBox createCard(
        String title,
        String value
    ) {

        VBox card = new VBox(15);

        card.setAlignment(
            Pos.CENTER
        );

        card.setPrefSize(
            200,
            150
        );

        card.getStyleClass().add(
            "dashboard-card"
        );

        // TITLE
        Label lblTitle =
            new Label(title);

        lblTitle.getStyleClass().add(
            "card-title"
        );

        // VALUE
        Label lblValue =
            new Label(value);

        lblValue.getStyleClass().add(
            "card-value"
        );

        card.getChildren().addAll(
            lblTitle,
            lblValue
        );

        return card;
    }
}