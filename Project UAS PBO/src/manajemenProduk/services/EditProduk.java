package manajemenProduk.services;

import java.util.ArrayList;
import manajemenProduk.model.Produk;

// CLASS KHUSUS EDIT
public class EditProduk {

    public void editProduk(ArrayList<Produk> daftar, String nama, int hargaBaru, int stokBaru) {

        boolean ditemukan = false;

        for (Produk p : daftar) {
            if (p.getNama().equalsIgnoreCase(nama)) {

                // update data
                p.setingHarga(hargaBaru);
                p.setingStok(stokBaru);

                ditemukan = true;
                System.out.println("Produk berhasil diupdate!");
                break;
            }
        }

        if (!ditemukan) {
            System.out.println("Produk tidak ditemukan.");
        }
    }
}