package manajemenProduk.model;

// CLASS + ENKAPSULASI (parent)
public class Item {
    String nama;
    int harga;

    public Item(String nama, int harga) {
        this.nama = nama;
        this.harga = harga;
    }

    public String getKategoriHarga() {
        if (harga > 100000) return "Mahal";
        else if (harga > 50000) return "Sedang";
        else return "Murah";
    }

    // POLIMORFISME (overloading)
    public void tampil() {
        System.out.println(nama + " | Rp." + harga);
    }

    public void tampil(boolean detail) {
        tampil();
        if (detail) {
            System.out.println("Kategori: " + getKategoriHarga());
        }
    }

    public String getNama() {
        return nama;
    }
    public int getHarga() {
        return harga;
    }

}