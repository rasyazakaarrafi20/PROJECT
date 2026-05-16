package manajemenProduk.mainprogram;

import java.sql.Connection;

import manajemenProduk.services.DatabaseConfig;

public class TestConnection {

    public static void main(String[] args) {

        try {

            Connection conn =
                DatabaseConfig.getConnection();

            System.out.println(
                "Database berhasil connect!"
            );

            conn.close();

        } catch (Exception e) {

            System.out.println(
                "Database gagal connect!"
            );

            e.printStackTrace();
        }
    }
}