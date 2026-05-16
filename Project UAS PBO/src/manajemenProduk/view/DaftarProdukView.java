package manajemenProduk.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.sql.*;
import java.util.Comparator;

import manajemenProduk.components.ModernAlert;
import manajemenProduk.components.ModernConfirm;
import manajemenProduk.model.Produk;
import manajemenProduk.services.DatabaseConfig;
import manajemenProduk.services.GudangManager;

public class DaftarProdukView {

    private GudangManager gm;
    private BorderPane root;
    private ObservableList<Produk> masterData = FXCollections.observableArrayList();

    public DaftarProdukView(GudangManager gm, BorderPane root) {
        this.gm = gm;
        this.root = root;
    }

    public VBox getView() {
        // --- TOPBAR (HEADER HITAM) ---
        HBox topbar = new HBox();
        topbar.getStyleClass().add("topbar");

        Label topText = new Label("Kami Siap Membantu anda");
        topText.getStyleClass().add("topbar-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label kelompok = new Label("KELOMPOK 3 👤");
        kelompok.getStyleClass().add("kelompok-text");
        topbar.getChildren().addAll(topText, spacer, kelompok);

        // --- PAGE TITLE ---
        Label pageTitle = new Label("Daftar Produk Lengkap");
        pageTitle.getStyleClass().add("page-title");

        // --- TABLE CONTAINER ---
        VBox tableContainer = new VBox(20);
        tableContainer.getStyleClass().add("table-container");

        // --- TABLE HEADER ---
        HBox tableHeader = new HBox(15);
        tableHeader.setAlignment(Pos.CENTER_LEFT);

        Label daftarText = new Label("Daftar Produk");
        daftarText.getStyleClass().add("table-title");

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        TextField searchField = new TextField();
        searchField.setPromptText("Cari nama produk...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(300); 

        ComboBox<String> urutkanBox = new ComboBox<>(FXCollections.observableArrayList(
            "Urutkan", "Nama (A-Z)", "Harga Terendah", "Stok Terbanyak"
        ));
        urutkanBox.getStyleClass().add("custom-combo");
        urutkanBox.setValue("Urutkan");

        tableHeader.getChildren().addAll(daftarText, spacer2, searchField, urutkanBox);

        // --- TABLE VIEW ---
        TableView<Produk> table = new TableView<>();
        table.getStyleClass().add("custom-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Kolom Nomor Otomatis
        TableColumn<Produk, Void> colNo = new TableColumn<>("No");
        colNo.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNo.setPrefWidth(50);

        TableColumn<Produk, String> colNama = new TableColumn<>("Nama Produk");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Produk, Integer> colHarga = new TableColumn<>("Harga (Rp)");
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        // Format Harga dengan pemisah ribuan (Opsional)
        colHarga.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("Rp %,d", item));
            }
        });

        TableColumn<Produk, Integer> colStok = new TableColumn<>("Stok");
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        // KOLOM DISKON DENGAN PERSEN (%)
        TableColumn<Produk, Integer> colDiskon = new TableColumn<>("Diskon");
        colDiskon.setCellValueFactory(new PropertyValueFactory<>("diskon"));
        colDiskon.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + "%"); // Menambahkan simbol persen
                }
            }
        });

        TableColumn<Produk, Void> colAksi = new TableColumn<>("Aksi");
        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("✎");
            private final Button btnHapus = new Button("■");
            private final HBox pane = new HBox(12, btnEdit, btnHapus);
            {
                btnEdit.getStyleClass().add("btn-edit");
                btnHapus.getStyleClass().add("btn-hapus");
                pane.setAlignment(Pos.CENTER);
                
                btnEdit.setFocusTraversable(false);
                btnHapus.setFocusTraversable(false);

                btnEdit.setOnAction(e -> handleEdit(getTableView().getItems().get(getIndex())));
                btnHapus.setOnAction(e -> handleHapus(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        table.getColumns().addAll(colNo, colNama, colHarga, colStok, colDiskon, colAksi);

        // --- LOGIKA FILTER & SEARCH ---
        loadDataFromDatabase(); 

        FilteredList<Produk> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(produk -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return produk.getNama().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // SortedList membungkus FilteredList agar sinkron saat sorting & searching
        SortedList<Produk> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // --- LOGIKA COMBOBOX URUTKAN ---
        urutkanBox.setOnAction(e -> {
            String selected = urutkanBox.getValue();
            if (selected.equals("Nama (A-Z)")) {
                masterData.sort(Comparator.comparing(Produk::getNama));
            } else if (selected.equals("Harga Terendah")) {
                masterData.sort(Comparator.comparingInt(Produk::getHarga));
            } else if (selected.equals("Stok Terbanyak")) {
                masterData.sort(Comparator.comparingInt(Produk::getStok).reversed());
            }
        });

        tableContainer.getChildren().addAll(tableHeader, table);
        
        VBox mainContent = new VBox(30, pageTitle, tableContainer);
        mainContent.getStyleClass().add("main-content-bg");

        BorderPane layout = new BorderPane();
        layout.setTop(topbar);
        layout.setCenter(mainContent);

        return new VBox(layout);
    }

    private void handleEdit(Produk p) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        VBox container = new VBox(20);
        container.getStyleClass().add("edit-modal");
        container.setPrefWidth(450);

        Label title = new Label("Edit Produk");
        title.getStyleClass().add("modal-title");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        VBox inputs = new VBox(12);
        
        Label lblNama = new Label("Nama Produk");
        lblNama.getStyleClass().add("modal-label");
        TextField txtNama = new TextField(p.getNama());
        txtNama.getStyleClass().add("modal-input");

        Label lblHarga = new Label("Harga");
        lblHarga.getStyleClass().add("modal-label");
        TextField txtHarga = new TextField(String.valueOf(p.getHarga()));
        txtHarga.getStyleClass().add("modal-input");

        Label lblStok = new Label("Stok");
        lblStok.getStyleClass().add("modal-label");
        TextField txtStok = new TextField(String.valueOf(p.getStok()));
        txtStok.getStyleClass().add("modal-input");

        inputs.getChildren().addAll(lblNama, txtNama, lblHarga, txtHarga, lblStok, txtStok);

        HBox btns = new HBox(15);
        btns.setAlignment(Pos.CENTER_RIGHT);
        btns.setPadding(new Insets(15, 0, 0, 0));

        Button btnBatal = new Button("Batal");
        btnBatal.getStyleClass().add("btn-batal-modal");
        btnBatal.setOnAction(e -> dialog.close());

        Button btnSimpan = new Button("Simpan");
        btnSimpan.getStyleClass().add("btn-simpan-modal");
        btnSimpan.setOnAction(e -> {
            try {
                updateDB(p.getNama(), txtNama.getText(), txtHarga.getText(), txtStok.getText());
                loadDataFromDatabase();
                dialog.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Harga dan Stok harus angka!");
                alert.show();
            }
        });

        btns.getChildren().addAll(btnBatal, btnSimpan);
        container.getChildren().addAll(title, inputs, btns);

        Scene scene = new Scene(container, Color.TRANSPARENT);
        try {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } catch (Exception e) { System.err.println("CSS tidak ditemukan!"); }
        
        dialog.setScene(scene);
        dialog.show();
    }

    private void updateDB(String oldName, String newName, String harga, String stok) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "UPDATE produk SET nama=?, harga=?, stok=? WHERE nama=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newName);
            ps.setInt(2, Integer.parseInt(harga));
            ps.setInt(3, Integer.parseInt(stok));
            ps.setString(4, oldName);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void handleHapus(Produk p) {
        boolean confirm =
            ModernConfirm.show(

                "Hapus Produk",
                "Yakin ingin menghapus " + p.getNama() + " ?"
            );

        if (confirm) {

            try (
                Connection conn =
                    DatabaseConfig.getConnection()
            ) {

                PreparedStatement ps =
                    conn.prepareStatement(

                        "DELETE FROM produk WHERE nama=?"
                    );

                ps.setString(
                    1,
                    p.getNama()
                );

                if (ps.executeUpdate() > 0) {

                    loadDataFromDatabase();

                    ModernAlert.show(

                        "Berhasil",
                        "Produk berhasil dihapus!"
                    );
                }

            } catch (Exception e) {

                ModernAlert.show(

                    "Error",
                    "Gagal menghapus produk!"
                );

                e.printStackTrace();
            }
        }
    }

    private void loadDataFromDatabase() {
        masterData.clear();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM produk")) {
            while (rs.next()) {
                masterData.add(new Produk(
                    rs.getString("nama"), 
                    rs.getInt("harga"), 
                    rs.getInt("stok"), 
                    rs.getInt("diskon")
                ));
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
}