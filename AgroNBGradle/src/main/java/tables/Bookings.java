package tables;

import classes.MyDataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Bookings {
    private int idBooking;
    private int num;
    private String seller;
    private String costAll;
    private String date;

    public Bookings(){}

    public Bookings(int idBooking, int num, String seller, String costAll, String date){
        this.idBooking = idBooking;
        this.num = num;
        this.seller = seller;
        this.costAll = costAll;
        this.date = date;
    }

    public int getIdBooking() { return idBooking; }

    public int getNum() { return num; }

    public String getSeller() { return seller; }

    public String getCostAll() {
        return costAll;
    }

    public String getDate() {
        return date;
    }

    public static String getNameSeller(int id_seller) throws SQLException {
        System.setProperty("console.encoding","Cp866");
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT seller_name FROM seller WHERE id_seller = " + id_seller);
        while (rs.next()) {
            String ret = rs.getString("seller_name");
            s.close();
            rs.close();
            mdb.getCloseConn();
            return ret;
        }

        return null;
    }
}
