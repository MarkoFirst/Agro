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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import classes.MyDataBase;
import classes.MyStage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tables.BookingObj;
import tables.Bookings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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
    private TableColumn sellerTC, dateTC, allPriceTC, nameTC, weightTC, priceTC, numTC;

    @FXML
    private Label summDay, excelLable, bookingLabel;

    @FXML
    private DatePicker dayDT, firstDayDT;

    @FXML
    private Button delBooking;

    private ObservableList<Bookings> data = FXCollections.observableArrayList();

    private void initData() throws SQLException {
        /*
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM booking WHERE ");
        while (rs.next()) {
            data.add(new Bookings(
                    rs.getInt("id_booking"),
                    getNameSeller(rs.getInt("id_seller")),
                    rs.getString("cost_all"),
                    rs.getString("date")
            ));
        }
        mdb.getCloseConn();
        s.close();
        */
        thisDay(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("console.encoding","Cp866");
        dayDT.setValue(LocalDate.now());
        bookingLabel.setText("Заказ");
        try {
            initData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        excelLable.setVisible(false);
        // устанавливаем тип и значение которое должно хранится в колонке
        numTC.setCellValueFactory(new PropertyValueFactory("num"));
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
                bookingLabel.setText("Заказ");
                delBooking.setVisible(true);
                excelLable.setVisible(false);
                dataBO = FXCollections.observableArrayList();
                dopItem = data.get(bookings.getSelectionModel().getSelectedIndex()).getIdBooking();
                if(dopItem < 1){ return; }
                try {
                    MyDataBase mdb = new MyDataBase();
                    Statement s = mdb.getConn().createStatement();
                    ResultSet rs = s.executeQuery("SELECT * FROM booking_dop WHERE id_booking = "+dopItem);
                    int item = 0;
                    while (rs.next()) {
                        item++;
                        dataBO.add(new BookingObj(
                                item,
                                BookingObj.getNameStr(rs.getInt("id_name")),
                                rs.getDouble("weight"),
                                rs.getString("cost_this")
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
    ObservableList<BookingObj> dataBO;

    private int dopItem = 0;

    public void removeBooking(ActionEvent actionEvent) {

        System.out.println(" data size: " + dataBO.size());
        for (int i = 0; i < dataBO.size(); i ++) {
            System.out.println("name: " + dataBO.get(i).getName());
            System.out.println("balance_shop: " + dataBO.get(i).getWeight());
            System.out.println("__________________________________________________");
        }

        excelLable.setVisible(false);
        if(dopItem == 0){return;}
        try {
            MyDataBase mdb = new MyDataBase();
            Statement s = mdb.getConn().createStatement();
            for (int i = 0; i < dataBO.size(); i++) {
                s.execute("UPDATE names SET " +
                        " balance_shop = balance_shop + " + dataBO.get(i).getWeight() +
                        " WHERE name = '" + dataBO.get(i).getName() + "'"
                );
            }

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
        excelLable.setVisible(false);
        double sum = 0;
        for ( int i = 0; i < bookings.getItems().size(); i++) { bookings.getItems().clear(); }
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM booking WHERE date = '" + dayDT.getValue().toString()+"' ORDER BY id_booking");
        int item = 1;
        while (rs.next()) {
            data.add(new Bookings(
                    rs.getInt("id_booking"),
                    item,
                    getNameSeller(rs.getInt("id_seller")),
                    rs.getString("cost_all"),
                    rs.getString("date")
            ));
            item++;
            sum += rs.getDouble("cost_all");
        }
        mdb.getCloseConn();
        s.close();

        bookings.setItems(data);
        summDay.setText("");
        if(sum>0) {
            summDay.setText((new BigDecimal(sum).setScale(1, RoundingMode.UP).doubleValue())  + " грн.");
        }
    }

    public void excel(ActionEvent actionEvent) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Storage");
        double sum = 0;

        for(int i = 0; i < dataBO.size()+1; i++){
            Row row = spreadsheet.createRow(i);
            if(i == 0){
                Cell head0 = row.createCell(0);
                Cell head1 = row.createCell(1);
                Cell head2 = row.createCell(2);
                Cell head3 = row.createCell(3);

                head0.setCellValue("№");
                head1.setCellValue("Наименование");
                head2.setCellValue("Вес");
                if(bookingLabel.getText().equals("Заказ")) {
                    head3.setCellValue("Цена");
                } else {
                    head3.setCellValue("Сумма");
                }
            } else {
                Cell newCell0 = row.createCell(0);
                Cell newCell1 = row.createCell(1);
                Cell newCell2 = row.createCell(2);

                newCell0.setCellValue(i);
                newCell1.setCellValue(dataBO.get(i-1).getName());
                newCell2.setCellValue(dataBO.get(i-1).getWeight());

//                if(bookingLabel.getText().equals("Заказ")) {
                    Cell newCell3 = row.createCell(3);
                    newCell3.setCellValue(dataBO.get(i-1).getPrice());

                    sum += Double.parseDouble(dataBO.get(i-1).getPrice());
//                }
            }
        }

        if(!bookingLabel.getText().equals("Заказ")) {
            Row row = spreadsheet.createRow(dataBO.size()+1);

            Cell newCell3 = row.createCell(3);
            newCell3.setCellValue(sum);
        }

        String dop = "";
        if(bookingLabel.getText().equals("Заказ")){
            Row row = spreadsheet.createRow(dataBO.size()+1);
            Cell newCell2 = row.createCell(2);
            Cell newCell3 = row.createCell(3);
            newCell2.setCellValue("Сумма");
            newCell3.setCellValue(data.get(bookings.getSelectionModel().getSelectedIndex()).getCostAll());

            dop = data.get(bookings.getSelectionModel().getSelectedIndex()).getCostAll()+" " +
                    data.get(bookings.getSelectionModel().getSelectedIndex()).getIdBooking();
        }

        FileOutputStream fileOut = new FileOutputStream(bookingLabel.getText()+" "+dop
                +" "+dayDT.getValue().toString() + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();
        excelLable.setVisible(true);

    }

    public void inDay(ActionEvent actionEvent){
        bookingLabel.setText("Продано за день");
        delBooking.setVisible(false);
        excelLable.setVisible(false);
        dataBO = FXCollections.observableArrayList();
        try {
            MyDataBase mdb = new MyDataBase();
            Statement s = mdb.getConn().createStatement();
            ResultSet rs = s.executeQuery("SELECT id_name, SUM(weight) " +
                    "FROM booking B, booking_dop D " +
                    "WHERE B.id_booking = D.id_booking " +
                        "AND B.date = '"+dayDT.getValue().toString()+"' " +
                    "GROUP BY id_name " +
                    "ORDER BY D.id_name "
            );
            int item = 0;
            while (rs.next()) {
                item++;
                dataBO.add(new BookingObj(
                        item,
                        BookingObj.getNameStr(rs.getInt("id_name")),
                        rs.getDouble("SUM"),
                        ""
                ));
            }
            mdb.getCloseConn();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bookingObj.setItems(dataBO);
    }


    public void forDays(ActionEvent actionEvent) {
        bookingLabel.setText("Дни");
        delBooking.setVisible(false);
        excelLable.setVisible(false);
        dataBO = FXCollections.observableArrayList();

        String start = firstDayDT.getValue().toString();
        String last = dayDT.getValue().toString();

        try {
            MyDataBase mdb = new MyDataBase();
            Statement s = mdb.getConn().createStatement();
            ResultSet rs = s.executeQuery("SELECT n.name, SUM(bd.weight) Вес, SUM(bd.cost_this) Цена " +
                    "FROM booking b " +
                    "left join booking_dop bd on bd.id_booking = b.id_booking " +
                    "left join names n on n.id_names = bd.id_name " +
                    "WHERE b.date between '" + start + "' and '" + last + "' " +
                    "GROUP BY n.id_names " +
                    "ORDER BY Вес DESC "
            );

            int item = 0;

            while (rs.next()) {
                item++;
                dataBO.add(new BookingObj(
                        item,
                        rs.getString(1),
                        rs.getDouble(2),
                        rs.getString(3)
                ));
            }

            mdb.getCloseConn();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bookingObj.setItems(dataBO);
    }
}
