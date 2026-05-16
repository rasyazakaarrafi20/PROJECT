package manajemenProduk.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import manajemenProduk.services.DatabaseConfig;
import manajemenProduk.model.Produk;
import manajemenProduk.model.ProdukDiskon;
import manajemenProduk.services.GudangManager;

public class ProdukDiskonView {

    private GudangManager gm;
    private Produk produkDipilih;
    private int hargaDiskon;
    private int diskonValue;

    public ProdukDiskonView(GudangManager gm) {
        this.gm = gm;
    }

    public VBox getView() {
        VBox layoutWrapper = new VBox();
        layoutWrapper.getStyleClass().add("main-pane");
        HBox.setHgrow(layoutWrapper, Priority.ALWAYS);

        // === TOPBAR (HITAM) ===
        HBox topbar = new HBox();
        topbar.setPadding(new Insets(15, 40, 15, 40));
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.getStyleClass().add("topbar");

        Label topHint = new Label("Kami Siap Membantu anda");
        topHint.getStyleClass().add("topbar-text");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label userLabel = new Label("KELOMPOK   👤");
        userLabel.getStyleClass().add("topbar-text");
        topbar.getChildren().addAll(topHint, spacer, userLabel);

        // === BODY CONTENT ===
        VBox body = new VBox(25);
        body.setPadding(new Insets(30, 60, 30, 60));
        body.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Diskon Produk");
        title.getStyleClass().add("section-title");

        // Container Area Tabel/List
        VBox tableContainer = new VBox(15);
        tableContainer.setPadding(new Insets(20));
        tableContainer.getStyleClass().add("table-container");
        
        Label listTitle = new Label("Daftar Produk & Pilih");
        listTitle.getStyleClass().add("label-white");

        ComboBox<String> cbProduk = new ComboBox<>();
        cbProduk.setPromptText("Pilih Produk");
        cbProduk.setMaxWidth(Double.MAX_VALUE);
        cbProduk.getStyleClass().add("combo-produk");

        ObservableList<String> data =
        FXCollections.observableArrayList();

        try {

            Connection conn =
                DatabaseConfig.getConnection();

            String sql =
                "SELECT nama FROM produk";

            PreparedStatement ps =
                conn.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

            while (rs.next()) {

                data.add(
                    rs.getString("nama")
                );
            }

            cbProduk.setItems(data);

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        // NOTIFIKASI DETAIL (Akan muncul di bawah ComboBox)
        Label lblHasil = new Label("Pilih produk dan masukkan nominal diskon untuk melihat simulasi.");
        lblHasil.getStyleClass().add("label-hasil");
        lblHasil.setMaxWidth(Double.MAX_VALUE);
        lblHasil.setWrapText(true);

        tableContainer.getChildren().addAll(listTitle, cbProduk, lblHasil);

        // Input Section
        VBox inputSection = new VBox(10);
        Label diskonTitle = new Label("Masukkan Diskon (%)");
        diskonTitle.getStyleClass().add("label-white");

        TextField tfDiskon = new TextField();
        tfDiskon.setPromptText("Contoh: 20");
        tfDiskon.getStyleClass().add("diskon-input");

        HBox actionBox = new HBox(20);
        actionBox.setAlignment(Pos.CENTER);
        
        Button btnHitung = new Button("Hitung Diskon %");
        btnHitung.getStyleClass().add("btn-hitung");

        Button btnTetapkan = new Button("Tetapkan Diskon");
        btnTetapkan.getStyleClass().add("btn-tetapkan");

        // === LOGIKA HITUNG DENGAN NOTIFIKASI DETAIL ===
        btnHitung.setOnAction(e -> {
            String nama = cbProduk.getValue();
            if (nama == null) {
                lblHasil.setText("⚠️ Silahkan pilih produk terlebih dahulu!");
                lblHasil.setStyle("-fx-text-fill: #ff6b6b;"); // Merah jika error
                return;
            }
            try {
                int diskon = Integer.parseInt(tfDiskon.getText());
                Produk p = gm.cariNama(nama);
                if (p != null) {
                    produkDipilih = p;
                    diskonValue = diskon;
                    
                    // Menggunakan Logika ProdukDiskon
                    ProdukDiskon pd = new ProdukDiskon(p.getNama(), p.getHarga(), p.getStok(), diskon);
                    hargaDiskon = pd.hargaSetelahDiskon();
                    int hemat = p.getHarga() - hargaDiskon;

                    // Notifikasi Detail
                    lblHasil.setText(
                        "DETAIL PERHITUNGAN:\n" +
                        "• Nama Produk: " + p.getNama() + "\n" +
                        "• Harga Normal: Rp " + p.getHarga() + "\n" +
                        "• Potongan (" + diskon + "%): - Rp " + hemat + "\n" +
                        "---------------------------\n" +
                        "• HARGA AKHIR: Rp " + hargaDiskon
                    );
                    lblHasil.setStyle("-fx-text-fill: #34d399;"); // Hijau jika sukses
                }
            } catch (NumberFormatException ex) {
                lblHasil.setText("⚠️ Nominal diskon harus berupa angka (0-100)!");
                lblHasil.setStyle("-fx-text-fill: #ff6b6b;");
            }
        });

        btnTetapkan.setOnAction(e -> {

            if (produkDipilih != null) {

                try {

                    // =========================
                    // UPDATE OBJECT
                    // =========================
                    produkDipilih.setingHarga(
                        hargaDiskon
                    );

                    produkDipilih.setDiskon(
                        diskonValue
                    );

                    // =========================
                    // UPDATE DATABASE
                    // =========================
                    Connection conn =
                        DatabaseConfig.getConnection();

                    String sql =
                        "UPDATE produk SET harga=?, diskon=? WHERE nama=?";

                    PreparedStatement ps =
                        conn.prepareStatement(sql);

                    ps.setInt(
                        1,
                        hargaDiskon
                    );

                    ps.setInt(
                        2,
                        diskonValue
                    );

                    ps.setString(
                        3,
                        produkDipilih.getNama()
                    );

                    ps.executeUpdate();

                    conn.close();

                    // =========================
                    // NOTIFIKASI
                    // =========================
                    lblHasil.setText(
                        "✅ BERHASIL! Diskon telah diterapkan dan disimpan ke database."
                    );

                    lblHasil.setStyle(
                        "-fx-text-fill: #34d399;" +
                        "-fx-font-weight: bold;"
                    );

                } catch (Exception ex) {

                    lblHasil.setText(
                        "❌ Gagal menyimpan ke database!"
                    );

                    lblHasil.setStyle(
                        "-fx-text-fill: red;"
                    );

                    ex.printStackTrace();
                }
            }
        });

        actionBox.getChildren().addAll(btnHitung, btnTetapkan);
        inputSection.getChildren().addAll(diskonTitle, tfDiskon, actionBox);
        body.getChildren().addAll(title, tableContainer, inputSection);
        layoutWrapper.getChildren().addAll(topbar, body);

        return layoutWrapper;
    }
}