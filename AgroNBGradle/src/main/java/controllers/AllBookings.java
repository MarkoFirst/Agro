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
    private Label summDay, excelLable;

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
        excelLable.setVisible(false);
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
                excelLable.setVisible(false);
                dataBO = FXCollections.observableArrayList();
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
    ObservableList<BookingObj> dataBO;

    private int dopItem = 0;

    public void removeBooking(ActionEvent actionEvent) {
        excelLable.setVisible(false);
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
        excelLable.setVisible(false);
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

    public void excel(ActionEvent actionEvent) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Storage");

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
                head3.setCellValue("Цена");
            } else {
                Cell newCell0 = row.createCell(0);
                Cell newCell1 = row.createCell(1);
                Cell newCell2 = row.createCell(2);
                Cell newCell3 = row.createCell(3);

                newCell0.setCellValue(i-1);
                newCell1.setCellValue(dataBO.get(i-1).getName());
                newCell2.setCellValue(dataBO.get(i-1).getWeight());
                newCell3.setCellValue(dataBO.get(i-1).getPrice());
            }
        }
        Row row = spreadsheet.createRow(dataBO.size()+1);
        Cell newCell2 = row.createCell(2);
        Cell newCell3 = row.createCell(3);
        newCell2.setCellValue("Сумма");
        newCell3.setCellValue(data.get(bookings.getSelectionModel().getSelectedIndex()).getCostAll());

        FileOutputStream fileOut = new FileOutputStream("Заказ "+data.get(bookings.getSelectionModel().getSelectedIndex()).getCostAll()
                +" "+data.get(bookings.getSelectionModel().getSelectedIndex()).getDate()
                +" "+data.get(bookings.getSelectionModel().getSelectedIndex()).getIdBooking()+ ".xlsx");
        workbook.write(fileOut);
        fileOut.close();
        excelLable.setVisible(true);

    }
}
