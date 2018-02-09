package tables;

import classes.MyDataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookingObj {
    private String name;
    private double weight;
    private double price;

    public BookingObj(){}

    public BookingObj(String name, double weight, double price){
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }

    public static String getNameStr(int id_name) throws SQLException {
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT name FROM names WHERE id_names = " + id_name);
        while (rs.next()) {
            String ret = rs.getString("name");
            mdb.getCloseConn();
            s.close();
            return ret;
        }
        return null;
    }
}
