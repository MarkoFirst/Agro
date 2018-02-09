package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import classes.MyDataBase;
import classes.MyStage;
import tables.BookingObj;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static controllers.EnterControll.idSeller;
import static controllers.Menu.menuOut;

public class Booking {

    public void menu(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Menu", "Меню");
        menuOut.hide();
    }
    @FXML
    private void backToStage0() throws IOException {
        groupPane.setVisible(true);
        namePane.setVisible(false);
        weightLabel.setTextFill(Color.BLACK);
        clearBut();
    }
    public void openBasket(ActionEvent actionEvent) {
        basketPane.setVisible(true);
        namePane.setVisible(false);
        groupPane.setVisible(false);
        modifyPane.setVisible(false);

        basketName.setCellValueFactory(new PropertyValueFactory("name"));
        basketWeight.setCellValueFactory(new PropertyValueFactory("weight"));
        basketCost.setCellValueFactory(new PropertyValueFactory("price"));

        basketTable.setItems(data);
    }
    public void cancel(ActionEvent actionEvent) {
        groupPane.setVisible(true);
        modifyPane.setVisible(false);
        weightLabel.setTextFill(Color.BLACK);
    }
    public void backOfBasket(ActionEvent actionEvent) {
        groupPane.setVisible(true);
        basketPane.setVisible(false);
        weightLabel.setTextFill(Color.BLACK);
    }

    private ObservableList data = FXCollections.observableArrayList();

    public void addToBasket(ActionEvent actionEvent) throws SQLException {
        
        if (Double.valueOf(weightLabel.getText()) <= Double.valueOf(newWeightTF.getText())){
            weightLabel.setTextFill(Color.RED);
            return;
        }
         if(mishCB.isSelected()){
            MyDataBase mdb = new MyDataBase();
            Statement s = mdb.getConn().createStatement();
            ResultSet rs = s.executeQuery("SELECT price_mesh FROM names WHERE name = '"+addName+"' ");
            while (rs.next()) {
                price = rs.getDouble("price_mesh");
            }
            mdb.getCloseConn();
            s.close();
            mishCB.setSelected(false);
        }
        weightLabel.setTextFill(Color.BLACK);
        if(Objects.equals(newWeightTF.getText(), "")){return;}
        groupPane.setVisible(true);
        modifyPane.setVisible(false);
        price =price * Double.valueOf(newWeightTF.getText());
        data.add(new BookingObj(addName, Double.valueOf(newWeightTF.getText()), price));
        basketArr[bookingCount][0] = addName;
        basketArr[bookingCount][1] = newWeightTF.getText();
        basketArr[bookingCount][2] = String.valueOf(price);
         
        bookingCount++;
        allPriceD += price;
        basketBut.setText("Корзина ("+bookingCount+")");
        allPrice.setText(String.valueOf(allPriceD) + " грн.");
        clearBut();
    }

    private double allPriceD = 0;

    private MyStage stage = new MyStage();

    @FXML
    public static Stage BookingStage;

    @FXML
    private TableView basketTable;

    @FXML
    private TableColumn basketName, basketCost, basketWeight;

    @FXML
    private Pane groupPane, namePane, modifyPane, basketPane, skidPane;

    @FXML
    private Button basketBut, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16, b17, b18, b19, b20, b21, b22, b23, b24;

    @FXML
    private Label nameLabel, weightLabel, allPrice;

    @FXML
    private ChoiceBox skidCB;
    
    @FXML
    private CheckBox mishCB;

    @FXML
    TextField newWeightTF, skidTF;

    private String[][] basketArr = new String[24][3];

    private Button[] arrBut = new Button[24];

    private void initButtons(){
        arrBut[0] = b1 ;
        arrBut[1] = b2 ;
        arrBut[2] = b3 ;
        arrBut[3] = b4 ;
        arrBut[4] = b5 ;
        arrBut[5] = b6 ;
        arrBut[6] = b7 ;
        arrBut[7] = b8 ;
        arrBut[8] = b9 ;
        arrBut[9] = b10 ;
        arrBut[10] = b11 ;
        arrBut[11] = b12 ;
        arrBut[12] = b13 ;
        arrBut[13] = b14 ;
        arrBut[14] = b15 ;
        arrBut[15] = b16 ;
        arrBut[16] = b17 ;
        arrBut[17] = b18 ;
        arrBut[18] = b19 ;
        arrBut[19] = b20 ;
        arrBut[20] = b21 ;
        arrBut[21] = b22 ;
        arrBut[22] = b23 ;
        arrBut[23] = b24 ;
    }
    private void clearBut(){
        for(int i = 0; i<24; i++){
            arrBut[i].setText("");
            arrBut[i].setVisible(false);
        }
    }

    private void changeStage(Stage primaryStage, String scene, String nameScene) throws IOException {
        Booking.BookingStage = primaryStage;
        stage.setStage(primaryStage);
        stage.getStage().setTitle(nameScene);
        stage.getStage().setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/"+scene+".fxml"));
        Scene enterScene = new Scene(root);
        stage.getStage().setScene(enterScene);
        stage.getStage().show();
    }

    public void zernBut(ActionEvent actionEvent) throws SQLException { firstStage(1); }
    public void drobBut(ActionEvent actionEvent) throws SQLException { firstStage(2); }
    public void granBut(ActionEvent actionEvent) throws SQLException { firstStage(3); }
    public void smesBut(ActionEvent actionEvent) throws SQLException { firstStage(4); }
    public void bestMixBut(ActionEvent actionEvent) throws SQLException { firstStage(5); }
    public void olkarBut(ActionEvent actionEvent) throws SQLException { firstStage(6); }
    public void krugBut(ActionEvent actionEvent) throws SQLException { firstStage(7); }
    public void kramBut(ActionEvent actionEvent) throws SQLException { firstStage(8); }
    public void prodBut(ActionEvent actionEvent) throws SQLException { firstStage(9); }
    public void razBut(ActionEvent actionEvent) throws SQLException { firstStage(10); }

    private String[] names;

    private void firstStage(int group) throws SQLException {
        groupPane.setVisible(false);
        namePane.setVisible(true);
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT name FROM names WHERE id_group = "+String.valueOf(group));
        int i = 0;
        String[] dopNames = new String[24];
        while (rs.next()) {
            dopNames[i] = rs.getString("name");
            i++;
        }
        mdb.getCloseConn();
        s.close();
        initButtons();
        names = new String[i];
        for(int k = 0; k<i; k++){
            names[k] = dopNames[k];
            arrBut[k].setText(names[k]);
            arrBut[k].setVisible(true);
        }

    }
    private double price = 0;
    private String addName = "";
    private int bookingCount = 0;

    private void stage3(String name){
        modifyPane.setVisible(true);
        namePane.setVisible(false);
        nameLabel.setText(name);
        addName = name;
        try {
            MyDataBase mdb = new MyDataBase();
            Statement s = mdb.getConn().createStatement();
            ResultSet rs = s.executeQuery("SELECT balance_shop, price_out FROM names WHERE name = '"+name+"'");
            while (rs.next()) {
                weightLabel.setText(rs.getString("balance_shop"));
                price = rs.getDouble("price_out");
            }
            mdb.getCloseConn();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        newWeightTF.setText("");
    }

    public void b1Act(ActionEvent actionEvent) {stage3(b1.getText());}
    public void b2Act(ActionEvent actionEvent) {stage3(b2.getText());}
    public void b3Act(ActionEvent actionEvent) {stage3(b3.getText());}
    public void b4Act(ActionEvent actionEvent) {stage3(b4.getText());}
    public void b5Act(ActionEvent actionEvent) {stage3(b5.getText());}
    public void b6Act(ActionEvent actionEvent) {stage3(b6.getText());}
    public void b7Act(ActionEvent actionEvent) {stage3(b7.getText());}
    public void b8Act(ActionEvent actionEvent) {stage3(b8.getText());}
    public void b9Act(ActionEvent actionEvent) {stage3(b9.getText());}
    public void b10Act(ActionEvent actionEvent) {stage3(b10.getText());}
    public void b11Act(ActionEvent actionEvent) {stage3(b11.getText());}
    public void b12Act(ActionEvent actionEvent) {stage3(b12.getText());}
    public void b13Act(ActionEvent actionEvent) {stage3(b13.getText());}
    public void b14Act(ActionEvent actionEvent) {stage3(b14.getText());}
    public void b15Act(ActionEvent actionEvent) {stage3(b15.getText());}
    public void b16Act(ActionEvent actionEvent) {stage3(b16.getText());}
    public void b17Act(ActionEvent actionEvent) {stage3(b17.getText());}
    public void b18Act(ActionEvent actionEvent) {stage3(b18.getText());}
    public void b19Act(ActionEvent actionEvent) {stage3(b19.getText());}
    public void b20Act(ActionEvent actionEvent) {stage3(b20.getText());}
    public void b21Act(ActionEvent actionEvent) {stage3(b21.getText());}
    public void b22Act(ActionEvent actionEvent) {stage3(b22.getText());}
    public void b23Act(ActionEvent actionEvent) {stage3(b23.getText());}
    public void b24Act(ActionEvent actionEvent) {stage3(b24.getText());}

    public void createBooking(ActionEvent actionEvent) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {
            MyDataBase mdb = new MyDataBase();
            Statement s = mdb.getConn().createStatement();
            ResultSet rs = s.executeQuery("SELECT max(id_booking) FROM booking");
            int id = 0;
            while (rs.next()) {
                id = rs.getInt("max") + 1;
            }
            rs.close();

            s.execute("INSERT INTO booking(id_booking, id_seller, date, cost_all) " +
                    "VALUES( " + String.valueOf(id)+", "+
                    idSeller + ", " +
                    "'"+dateFormat.format(date) + "'," +
                    String.valueOf(allPriceD) + ")"
            );

            for(int i = 0; i < bookingCount; i++){
                String name = basketArr[i][0];
                rs = s.executeQuery("SELECT id_names FROM names WHERE name = '"+name+"'");
                while (rs.next()) {
                    name = String.valueOf(rs.getInt("id_names"));
                }
                rs.close();
                s.execute("INSERT INTO booking_dop (id_name, weight, id_booking, cost_this) " +
                        "VALUES( " + name +", "+
                        basketArr[i][1] + ", " +
                        String.valueOf(id) + "," +
                        basketArr[i][2] + ")"
                );

                s.execute("UPDATE names SET "+
                        "balance_shop = balance_shop - " + basketArr[i][1] +
                        "WHERE id_names = " + name);
            }
            mdb.getCloseConn();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resumeBasket();
        try {
            backToStage0();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void resumeBasket() {
        for ( int i = 0; i < basketTable.getItems().size(); i++) {
            basketTable.getItems().clear();
        }
        bookingCount = 0;
        basketBut.setText("Корзина");
        allPriceD = 0;
        allPrice.setText("");
    }

    private boolean sk = true;
    public void skidActive(ActionEvent actionEvent) {
        skidCB.setItems(FXCollections.observableArrayList("грн.", "%"));
        skidCB.getSelectionModel().selectFirst();
        if(sk){
            sk = false;
            skidPane.setVisible(true);
        } else {
            sk = true;
            skidPane.setVisible(false);
        }
    }

    public void skidOk(ActionEvent actionEvent) {
        sk = true;
        skidPane.setVisible(false);

        double newPrice;

        if(skidCB.getValue().toString().equals("%")){
            newPrice =(new BigDecimal(allPriceD * (100 - Double.valueOf(skidTF.getText())) / 100).setScale(1, RoundingMode.UP).doubleValue());
        } else {
            newPrice = allPriceD - Double.valueOf(skidTF.getText());
        }

        allPrice.setText(newPrice + " грн. ");
    }
}
