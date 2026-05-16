package manajemenProduk.model;

// PEWARISAN LANJUTAN
public class ProdukDiskon extends Produk {
    int diskon;

    public ProdukDiskon(String nama, int harga, int stok, int diskon) {
        super(nama, harga, stok, 0);
        this.diskon = diskon;
    }

    public int hargaSetelahDiskon() {
        return harga - (harga * diskon / 100);
    }

    public int getDiskon() {
        return diskon;
    }

    @Override
    public void tampil() {
        super.tampil();
        System.out.println("Diskon: " + diskon + "%");
        System.out.println("Harga setelah diskon: Rp." + hargaSetelahDiskon());
    }
}