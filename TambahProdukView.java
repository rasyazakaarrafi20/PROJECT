package manajemenProduk.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Optional;

import manajemenProduk.model.Produk;
import manajemenProduk.services.GudangManager;

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
            "KELOMPOK 3"
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

                Alert alert =
                    new Alert(
                        Alert.AlertType.WARNING
                    );

                alert.setTitle(
                    "Peringatan"
                );

                alert.setHeaderText(
                    null
                );

                alert.setContentText(
                    "Semua input wajib diisi!"
                );

                alert.showAndWait();

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

                    Alert alert =
                        new Alert(
                            Alert.AlertType.ERROR
                        );

                    alert.setTitle(
                        "Error"
                    );

                    alert.setHeaderText(
                        null
                    );

                    alert.setContentText(
                        "Harga dan stok harus lebih dari 0!"
                    );

                    alert.showAndWait();

                    return;
                }

                Produk produk =
                    new Produk(
                        nama,
                        harga,
                        stok
                    );

                gm.tambahProduk(
                    produk
                );

                // ALERT BERHASIL
                Alert success =
                    new Alert(
                        Alert.AlertType.INFORMATION
                    );

                success.setTitle(
                    "Berhasil"
                );

                success.setHeaderText(
                    null
                );

                success.setContentText(
                    "Produk berhasil ditambahkan!"
                );

                success.showAndWait();

                // CLEAR FORM
                tfNama.clear();
                tfHarga.clear();
                tfStok.clear();

            } catch (NumberFormatException ex) {

                Alert alert =
                    new Alert(
                        Alert.AlertType.ERROR
                    );

                alert.setTitle(
                    "Error"
                );

                alert.setHeaderText(
                    null
                );

                alert.setContentText(
                    "Harga dan stok harus berupa angka!"
                );

                alert.showAndWait();
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

                Alert confirm =
                    new Alert(
                        Alert.AlertType.CONFIRMATION
                    );

                confirm.setTitle(
                    "Konfirmasi"
                );

                confirm.setHeaderText(
                    null
                );

                confirm.setContentText(
                    "Yakin ingin menghapus input?"
                );

                Optional<ButtonType> result =
                    confirm.showAndWait();

                if (
                    result.isPresent() &&
                    result.get() == ButtonType.OK
                ) {

                    tfNama.clear();
                    tfHarga.clear();
                    tfStok.clear();
                }

            } else {

                Alert kosong =
                    new Alert(
                        Alert.AlertType.INFORMATION
                    );

                kosong.setTitle(
                    "Informasi"
                );

                kosong.setHeaderText(
                    null
                );

                kosong.setContentText(
                    "Form sudah kosong!"
                );

                kosong.showAndWait();
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