package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import classes.MyDataBase;
import classes.MyStage;
import tables.BookingObj;
import tables.Bookings;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static controllers.Menu.menuOut;
import static tables.Bookings.getNameSeller;

public class AllBookings implements Initializable {

    public void menu(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Menu", "Меню");
        menuOut.hide();
    }

    private MyStage stage = new MyStage();

    @FXML
    public static Stage AllBookingsStage;

    private void changeStage(Stage primaryStage, String scene, String nameScene) throws IOException {
        AllBookings.AllBookingsStage = primaryStage;
        stage.setStage(primaryStage);
        stage.getStage().setTitle(nameScene);
        stage.getStage().setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + scene + ".fxml"));
        Scene enterScene = new Scene(root);
        stage.getStage().setScene(enterScene);
        stage.getStage().show();
    }

    @FXML
    private TableView bookings, bookingObj;

    @FXML
    private TableColumn sellerTC, dateTC, allPriceTC, nameTC, weightTC, priceTC;

    @FXML
    private Label summDay;

    @FXML
    private DatePicker dayDT;

    private ObservableList<Bookings> data = FXCollections.observableArrayList();

    private void initData() throws SQLException {
        int item = 0;
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM booking");
        while (rs.next()) {
            data.add(new Bookings(
                    rs.getInt("id_booking"),
                    getNameSeller(rs.getInt("id_seller")),
                    rs.getString("cost_all"),
                    rs.getString("date")
            ));
            arrBookings[item] = rs.getInt("id_booking");
            item++;
        }
        mdb.getCloseConn();
        s.close();
    }

    private int[] arrBookings = new int[20];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // устанавливаем тип и значение которое должно хранится в колонке
        sellerTC.setCellValueFactory(new PropertyValueFactory("seller"));
        allPriceTC.setCellValueFactory(new PropertyValueFactory("costAll"));
        dateTC.setCellValueFactory(new PropertyValueFactory("date"));

        nameTC.setCellValueFactory(new PropertyValueFactory("name"));
        weightTC.setCellValueFactory(new PropertyValueFactory("weight"));
        priceTC.setCellValueFactory(new PropertyValueFactory("price"));
        // заполняем таблицу данными
        bookings.setItems(data);

        bookings.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObservableList dataBO = FXCollections.observableArrayList();
                dopItem = data.get(bookings.getSelectionModel().getSelectedIndex()).getIdBooking();
                if(dopItem < 1){ return; }
                try {
                    MyDataBase mdb = new MyDataBase();
                    Statement s = mdb.getConn().createStatement();
                    ResultSet rs = s.executeQuery("SELECT * FROM booking_dop WHERE id_booking = "+dopItem);
                    while (rs.next()) {
                        dataBO.add(new BookingObj(
                                BookingObj.getNameStr(rs.getInt("id_name")),
                                rs.getDouble("weight"),
                                rs.getDouble("cost_this")
                        ));
                    }
                    mdb.getCloseConn();
                    s.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                bookingObj.setItems(dataBO);
            }
        });
    }

    private int dopItem = 0;

    public void removeBooking(ActionEvent actionEvent) {
        if(dopItem == 0){return;}
        try {
            MyDataBase mdb = new MyDataBase();
            Statement s = mdb.getConn().createStatement();
            s.execute("DELETE FROM booking_dop WHERE id_booking = " + dopItem);
            s.execute("DELETE FROM booking WHERE id_booking = " + dopItem);
            s.close();
            for ( int i = 0; i < bookings.getItems().size(); i++) { bookings.getItems().clear(); }
            for ( int i = 0; i < bookingObj.getItems().size(); i++) { bookingObj.getItems().clear(); }
            if(!summDay.getText().equals("")) {
                thisDay(actionEvent);
            } else {
                initData();
            }
            dopItem = 0;
            mdb.getCloseConn();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void thisDay(ActionEvent actionEvent) throws SQLException {
        double sum = 0;
        int item = 0;
        for ( int i = 0; i < bookings.getItems().size(); i++) { bookings.getItems().clear(); }
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM booking WHERE date = '" + dayDT.getValue().toString()+"'");
        while (rs.next()) {
            data.add(new Bookings(
                    rs.getInt("id_booking"),
                    getNameSeller(rs.getInt("id_seller")),
                    rs.getString("cost_all"),
                    rs.getString("date")
            ));
            sum += rs.getDouble("cost_all");
            arrBookings[item] = rs.getInt("id_booking");
            item++;
        }
        mdb.getCloseConn();
        s.close();

        bookings.setItems(data);
        summDay.setText("");
        if(sum>0) {
            summDay.setText((new BigDecimal(sum).setScale(1, RoundingMode.UP).doubleValue())  + " грн.");
        }
    }
}
