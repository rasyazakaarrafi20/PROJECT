package manajemenProduk.services;

import java.sql.*;
import java.util.ArrayList;
import manajemenProduk.model.Produk;

public class GudangManager {

    private ArrayList<Produk> daftar = new ArrayList<>();
    private ArrayList<Produk> produkDiskon = new ArrayList<>();

    // Konstruktor: Otomatis ambil data dari DB saat aplikasi dijalankan
    public GudangManager() {
        muatDataDariDatabase();
    }

    // --- LOGIKA DATABASE ---

    private void muatDataDariDatabase() {
        daftar.clear();
        String query = "SELECT * FROM produk";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Sesuaikan dengan konstruktor Produk Anda (nama, harga, stok)
                Produk p = new Produk(
                    rs.getString("nama"),
                    rs.getInt("harga"),
                    rs.getInt("stok"),
                    rs.getInt("diskon") // Asumsi ada kolom diskon di DB
                );
                daftar.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat data: " + e.getMessage());
        }
    }

    // TAMBAH ke Database dan ArrayList
    public void tambahProduk(Produk p) {
        String query = "INSERT INTO produk (nama, harga, stok) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, p.getNama());
            pstmt.setInt(2, p.getHarga());
            pstmt.setInt(3, p.getStok());
            pstmt.executeUpdate();

            daftar.add(p); // Update cache lokal
            System.out.println("Produk berhasil disimpan ke MySQL.");
        } catch (SQLException e) {
            throw new RuntimeException("Gagal simpan ke database: " + e.getMessage());
        }
    }

    // HAPUS dari Database dan ArrayList
    public void hapusProduk(String nama) {
        String query = "DELETE FROM produk WHERE nama = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, nama);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                // Hapus dari ArrayList lokal juga
                daftar.removeIf(p -> p.getNama().equalsIgnoreCase(nama));
                System.out.println("Produk berhasil dihapus dari MySQL.");
            } else {
                System.out.println("Produk tidak ditemukan di database.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal hapus data: " + e.getMessage());
        }
    }

    // --- LOGIKA LOKAL (TETAP SAMA) ---

    public void tampilSemua() {
        if (daftar.isEmpty()) {
            System.out.println("Belum ada produk.");
            return;
        }
        for (Produk p : daftar) {
            p.tampil();
        }
    }

    public void tambahProdukDiskon(Produk p) {
        if (!produkDiskon.contains(p)) {
            produkDiskon.add(p);
        }
    }

    public ArrayList<Produk> getProdukDiskon() {
        return produkDiskon;
    }

    public int totalProduk() {
        return daftar.size();
    }

    public int totalStok() {
        int total = 0;
        for (Produk p : daftar) {
            total += p.getStok();
        }
        return total;
    }

    public void sortHarga() {
        for (int i = 0; i < daftar.size() - 1; i++) {
            for (int j = 0; j < daftar.size() - 1 - i; j++) {
                if (daftar.get(j).getHarga() > daftar.get(j + 1).getHarga()) {
                    Produk temp = daftar.get(j);
                    daftar.set(j, daftar.get(j + 1));
                    daftar.set(j + 1, temp);
                }
            }
        }
    }

    public Produk cariNama(String nama) {
        for (Produk p : daftar) {
            if (p.getNama().equalsIgnoreCase(nama)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Produk> getDaftar() {
        return daftar;
    }
}