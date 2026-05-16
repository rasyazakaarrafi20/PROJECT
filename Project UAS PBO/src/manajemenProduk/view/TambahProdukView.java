package manajemenProduk.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import manajemenProduk.services.GudangManager;
import manajemenProduk.services.DatabaseConfig;

import manajemenProduk.components.ModernAlert;
import manajemenProduk.components.ModernConfirm;

public class TambahProdukView {

    GudangManager gm;
    BorderPane root;

    public TambahProdukView(
        GudangManager gm,
        BorderPane root
    ) {

        this.gm = gm;
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

        topbar.getStyleClass().add(
            "topbar"
        );

        Label topText = new Label(
            "Kami Siap Membantu Anda"
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
            "kelompok-text"
        );

        topbar.getChildren().addAll(
            topText,
            spacer,
            kelompok
        );

        // =========================
        // FORM BOX
        // =========================
        VBox formBox = new VBox(25);

        formBox.setPadding(
            new Insets(40)
        );

        formBox.setMaxWidth(750);

        formBox.getStyleClass().add(
            "form-container"
        );

        // =========================
        // TITLE
        // =========================
        Label title = new Label(
            "Tambah Produk Baru"
        );

        title.getStyleClass().add(
            "form-title"
        );

        // =========================
        // INPUT
        // =========================
        TextField tfNama = new TextField();

        tfNama.setPromptText(
            "Masukkan nama produk"
        );

        tfNama.getStyleClass().add(
            "custom-field"
        );

        TextField tfHarga = new TextField();

        tfHarga.setPromptText(
            "Masukkan harga"
        );

        tfHarga.getStyleClass().add(
            "custom-field"
        );

        TextField tfStok = new TextField();

        tfStok.setPromptText(
            "Masukkan stok"
        );

        tfStok.getStyleClass().add(
            "custom-field"
        );

        VBox namaBox = createInputBox(
            "Nama Produk",
            tfNama
        );

        VBox hargaBox = createInputBox(
            "Harga",
            tfHarga
        );

        VBox stokBox = createInputBox(
            "Stok",
            tfStok
        );

        // =========================
        // BUTTON
        // =========================
        HBox buttonBox = new HBox(15);

        buttonBox.setAlignment(
            Pos.CENTER_RIGHT
        );

        Button btnBatal = new Button(
            "Batal"
        );

        btnBatal.getStyleClass().add(
            "btn-batal"
        );

        Button btnSimpan = new Button(
            "Simpan"
        );

        btnSimpan.getStyleClass().add(
            "btn-simpan"
        );

        // =========================
        // ACTION SIMPAN
        // =========================
        btnSimpan.setOnAction(e -> {

            String nama =
                tfNama.getText();

            String hargaText =
                tfHarga.getText();

            String stokText =
                tfStok.getText();

            // VALIDASI KOSONG
            if (
                nama.isEmpty() ||
                hargaText.isEmpty() ||
                stokText.isEmpty()
            ) {

                ModernAlert.show(

                    "Peringatan",
                    "Semua input wajib diisi!"
                );

                return;
            }

            try {

                int harga =
                    Integer.parseInt(
                        hargaText
                    );

                int stok =
                    Integer.parseInt(
                        stokText
                    );

                // VALIDASI ANGKA
                if (
                    harga <= 0 ||
                    stok <= 0
                ) {

                    ModernAlert.show(

                        "Error",
                        "Harga dan stok harus lebih dari 0!"
                    );

                    return;
                }

                // =========================
                // SIMPAN KE DATABASE
                // =========================
                try {

                    Connection conn =
                        DatabaseConfig.getConnection();

                    String sql =
                        "INSERT INTO produk(nama, harga, stok, diskon) VALUES (?, ?, ?, ?)";

                    PreparedStatement ps =
                        conn.prepareStatement(sql);

                    ps.setString(
                        1,
                        nama
                    );

                    ps.setInt(
                        2,
                        harga
                    );

                    ps.setInt(
                        3,
                        stok
                    );

                    ps.setInt(
                        4,
                        0
                    );

                    ps.executeUpdate();

                    conn.close();

                } catch (SQLException ex) {

                    ModernAlert.show(

                        "Database Error",
                        "Gagal menyimpan ke database!"
                    );

                    ex.printStackTrace();

                    return;
                }

                // ALERT BERHASIL
                ModernAlert.show(

                    "Berhasil",
                    "Produk berhasil ditambahkan!"
                );

                // CLEAR FORM
                tfNama.clear();
                tfHarga.clear();
                tfStok.clear();

            } catch (NumberFormatException ex) {

                ModernAlert.show(

                    "Error",
                    "Harga dan stok harus berupa angka!"
                );
            }
        });

        // =========================
        // ACTION BATAL
        // =========================
        btnBatal.setOnAction(e -> {

            // VALIDASI ADA ISI ATAU TIDAK
            if (
                !tfNama.getText().isEmpty() ||
                !tfHarga.getText().isEmpty() ||
                !tfStok.getText().isEmpty()
            ) {

                boolean confirm =
                    ModernConfirm.show(

                        "Konfirmasi",
                        "Yakin ingin menghapus input?"
                    );

                if (confirm) {

                    tfNama.clear();
                    tfHarga.clear();
                    tfStok.clear();
                }

            } else {

                ModernAlert.show(

                    "Informasi",
                    "Form sudah kosong!"
                );
            }
        });

        buttonBox.getChildren().addAll(
            btnBatal,
            btnSimpan
        );

        // =========================
        // ADD
        // =========================
        formBox.getChildren().addAll(
            title,
            namaBox,
            hargaBox,
            stokBox,
            buttonBox
        );

        // =========================
        // CONTENT
        // =========================
        VBox content = new VBox(30);

        content.setPadding(
            new Insets(30)
        );

        content.setAlignment(
            Pos.TOP_CENTER
        );

        content.getChildren().addAll(
            formBox
        );

        // =========================
        // LAYOUT
        // =========================
        BorderPane layout =
            new BorderPane();

        layout.setTop(topbar);

        layout.setCenter(content);

        return new VBox(layout);
    }

    // =========================
    // INPUT BOX
    // =========================
    private VBox createInputBox(
        String labelText,
        TextField field
    ) {

        VBox box = new VBox(10);

        Label label = new Label(
            labelText
        );

        label.getStyleClass().add(
            "input-label"
        );

        box.getChildren().addAll(
            label,
            field
        );

        return box;
    }
}