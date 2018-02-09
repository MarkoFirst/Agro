package tables;

import classes.MyDataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Names {

    private String name;
    private String group;
    private String price_in;
    private String price_out;
    private String balanceStorage;
    private String balanceShop;

    public Names() {
    }

    public Names(String name, String group, String price_in, String price_out, String balanceStorage, String balanceShop) {
        this.name = name;
        this.group = group;
        this.price_in = price_in;
        this.price_out = price_out;
        this.balanceStorage = balanceStorage;
        this.balanceShop = balanceShop;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getPrice_in() {
        return price_in;
    }

    public String getPrice_out() {
        return price_out;
    }

    public String getBalanceStorage() {
        return balanceStorage;
    }

    public String getBalanceShop() {
        return balanceShop;
    }

    public static String getGroupName(int id_group) throws SQLException {
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT group_name FROM group_name WHERE id_group = " + id_group);
        while (rs.next()) {
            String ret = rs.getString("group_name");
            mdb.getCloseConn();
            s.close();
            return ret;
        }
        return null;
    }
}
