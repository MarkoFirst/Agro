package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private Connection conn = null;

    public MyDataBase() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/Agro", "postgres",
                    "and1");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (conn == null) {
            System.out.println("Failed to make connection!");
        } else {
            //System.out.println("Connection OK");
        }
    }

    public Connection getConn() { return conn; }
    public void getCloseConn() throws SQLException { conn.close(); }
}

