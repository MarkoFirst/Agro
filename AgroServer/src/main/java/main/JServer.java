package main;

import classes.MyDataBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JServer extends javax.swing.JFrame {

    public JServer() {
        initComponents();
    }
    static private Socket s;
    static private ObjectOutputStream outPut;
    static private ObjectInputStream inPut;
    static ServerSocket ss;
    static InputStreamReader isr;
    static BufferedReader br;
    static String message;
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) throws InterruptedException, InvocationTargetException {
        
        java.awt.EventQueue.invokeAndWait (new Runnable() {
            public void run() {
                new JServer().setVisible(true);
            }
        });
     try {

            while (true) {

                ss = new ServerSocket(27016);

                if (jTextArea1.getText().equals("") ) {
                 jTextArea1.append("\nServer run");
                } 
                s = ss.accept();
                isr = new InputStreamReader(s.getInputStream());
                br = new BufferedReader(isr);
                message = br.readLine();

                 jTextArea1.append("\n" + message);

                checkData(message);

                isr.close();
                br.close();
                ss.close();
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void checkData(String message){
        String[] arrMessage = message.split("&");
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < arrMessage.length; i++) {
            jTextArea1.append("\n"+arrMessage[i]);
        }
        String[] newArr = new String[0];
        try {
            if(arrMessage[0].equals("0")){
                newArr = selectDB(arrMessage[1], arrMessage[2], arrMessage[3]);
            } else if(arrMessage[0].equals("1")){
                insertNewBooking(arrMessage[1], arrMessage[2], arrMessage[3], arrMessage[4]);
                sentData("Заказ успешно добавлен!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < newArr.length; i++) {
            result.append(newArr[i]).append("&");
        }

        sentData(result.toString());
    }

    private static void insertNewBooking(String seller, String cost_all, String bookingCount, String basketArray) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String[] basketArr = basketArray.split("#");
        try {
            MyDataBase mdb = new MyDataBase();
            Statement stmt = mdb.getConn().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT max(id_booking) FROM booking");
            int id = 0;
            while (rs.next()) {
                id = rs.getInt("max") + 1;
            }
            rs.close();

            stmt.execute("INSERT INTO booking(id_booking, id_seller, date, cost_all) " +
                    "VALUES( " + String.valueOf(id)+", "+
                    seller + ", " +
                    "'"+dateFormat.format(date) + "'," +
                    cost_all + ")"
            );

             jTextArea1.append("\n"+"booking count ___________________"+bookingCount);
            for(int i = 0; i < Integer.valueOf(bookingCount)*3; i+=3){
                String name = basketArr[i];
                rs = stmt.executeQuery("SELECT id_names FROM names WHERE name = '"+name+"'");
                while (rs.next()) {
                    name = String.valueOf(rs.getInt("id_names"));
                }
                rs.close();
                stmt.execute("INSERT INTO booking_dop (id_name, weight, id_booking, cost_this) " +
                        "VALUES( " + name +", "+
                        basketArr[i+1] + ", " +
                        String.valueOf(id) + "," +
                        basketArr[i+2] + ")"
                );

                stmt.execute("UPDATE names SET "+
                        "balance_shop = balance_shop - " + basketArr[i+1] +
                        "WHERE id_names = " + name);
            }
            mdb.getCloseConn();
            stmt.close();
            jTextArea1.append("\n"+"__________________New booking OK");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String[] selectDB(String select, String from, String where) throws SQLException {
        String[] arrReturn;
        String sel = "";

        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT "+select+" FROM "+from+" WHERE "+where);
        while (rs.next()) {
            sel += rs.getString(select) + "&";
        }
        mdb.getCloseConn();
        s.close();

        arrReturn = sel.split("&");

        return arrReturn;
    }
    
    
    private static void sentData(String str){
        try {
            jTextArea1.append("\n"+str);

            OutputStream out = s.getOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject("&"+str);
            oout.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
