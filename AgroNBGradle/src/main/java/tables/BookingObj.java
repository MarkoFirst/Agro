package tables;

import classes.MyDataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookingObj {
    private int number;
    private String name;
    private double weight;
    private String  price;

    public BookingObj(){}

    public BookingObj(int number, String name, double weight, String price){
        this.number = number;
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public int getNumber() { return number; }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(Double newWeight){
        this.weight = newWeight;
    }

    public String getPrice() {
        return price;
    }

    public static String getNameStr(int id_name) throws SQLException {
        System.setProperty("console.encoding","Cp866");
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
