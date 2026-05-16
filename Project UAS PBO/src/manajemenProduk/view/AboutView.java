package manajemenProduk.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AboutView {

    BorderPane root;

    public AboutView(BorderPane root) {
        this.root = root;
    }

    public VBox getView() {

        // =========================
        // TOPBAR
        // =========================
        HBox topbar = new HBox();

        topbar.setPadding(
            new Insets(15, 30, 15, 30)
        );

        topbar.setAlignment(
            Pos.CENTER_LEFT
        );

        topbar.getStyleClass().add(
            "topbar"
        );

        Label topText = new Label(
            "Kami Siap Membantu anda"
        );

        topText.getStyleClass().add(
            "topbar-text"
        );

        Region spacer = new Region();

        HBox.setHgrow(
            spacer,
            Priority.ALWAYS
        );

        Label kelompok = new Label(
            "KELOMPOK 3 👤"
        );

        kelompok.getStyleClass().add(
            "topbar-text"
        );

        topbar.getChildren().addAll(
            topText,
            spacer,
            kelompok
        );

        // =========================
        // CONTENT
        // =========================
        VBox content = new VBox(35);

        content.setPadding(
            new Insets(40)
        );

        content.setAlignment(
            Pos.TOP_CENTER
        );

        content.getStyleClass().add(
            "about-bg"
        );

        // =========================
        // TITLE
        // =========================
        Label title = new Label(
            "About Aplikasi"
        );

        title.getStyleClass().add(
            "about-title"
        );

        // =========================
        // DESKRIPSI
        // =========================
        Label desc = new Label(
            "Aplikasi Manajemen Produk adalah sistem yang dirancang "
            + "untuk membantu pengguna dalam mengelola data produk "
            + "secara lebih mudah, cepat, dan terorganisir. "
            + "Aplikasi ini memiliki fitur seperti menambah, "
            + "mengedit, menghapus, dan mencari data produk "
            + "sehingga proses pengelolaan menjadi lebih efisien."
        );

        desc.setWrapText(true);

        desc.setMaxWidth(700);

        desc.setMinHeight(Region.USE_PREF_SIZE);

        desc.getStyleClass().add(
            "about-desc"
        );

        // =========================
        // TITLE TEAM
        // =========================
        Label teamTitle = new Label(
            "Kelompok Kami"
        );

        teamTitle.getStyleClass().add(
            "team-title"
        );

        // =========================
        // TEAM CONTAINER
        // =========================
        FlowPane teamPane = new FlowPane();

        teamPane.setHgap(40);

        teamPane.setVgap(30);

        teamPane.setAlignment(
            Pos.CENTER
        );

        // =========================
        // FOTO MEMBER
        // =========================

        teamPane.getChildren().addAll(

            createMemberCard(
                "file:src/resources/images/Andhika.jpeg",
                "Andhika Annas Satria",
                "25051204429"
            ),

            createMemberCard(
                "file:src/resources/images/Rafi.jpeg",
                "Rasya Zaka Arrafi",
                "25051204419"
            ),

            createMemberCard(
                "file:src/resources/images/Anggito.jpeg",
                "Anggito Satrio Wicaksono",
                "25051204428"
            ),

            createMemberCard(
                "file:src/resources/images/Rafi.jpeg",
                "Budi",
                "12345678911"
            ),

            createMemberCard(
                "file:src/resources/images/Rafi.jpeg",
                "Nanda",
                "12345678912"
            ),

            createMemberCard(
                "file:src/resources/images/Rafi.jpeg",
                "Fajar",
                "12345678913"
            ),

            createMemberCard(
                "file:src/resources/images/Rafi.jpeg",
                "Putri",
                "12345678914"
            )
        );

        // =========================
        // ADD CONTENT
        // =========================
        content.getChildren().addAll(
            title,
            desc,
            teamTitle,
            teamPane
        );

        // =========================
        // LAYOUT
        // =========================
        BorderPane layout = new BorderPane();

        layout.setTop(topbar);

        layout.setCenter(content);

        return new VBox(layout);
    }

    // =========================
    // MEMBER CARD
    // =========================
    private VBox createMemberCard(
        String fotoAnggota,
        String nama,
        String nim
    ) {

        VBox card = new VBox(10);

        card.setAlignment(Pos.CENTER);

        // FOTO
        ImageView foto = new ImageView(
            new Image(fotoAnggota)
        );

        foto.setFitWidth(90);

        foto.setFitHeight(90);

        Rectangle clip = new Rectangle(
            90,
            90
        );

        clip.setArcWidth(25);
        clip.setArcHeight(25);

        foto.setClip(clip);
        foto.setPreserveRatio(false);

        // NAMA
        Label namaLabel = new Label(nama);

        namaLabel.setWrapText(true);

        namaLabel.setMaxWidth(150);

        namaLabel.setAlignment(Pos.CENTER);

        namaLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-alignment: center;"
        );

        // NIM
        Label namanim = new Label(nim);

        namanim.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-alignment: center;"
        );

        namanim.setAlignment(Pos.CENTER);

        namanim.setMaxWidth(150);

        // ADD
        card.getChildren().addAll(
            foto,
            namaLabel,
            namanim
        );

        return card;
    }
}