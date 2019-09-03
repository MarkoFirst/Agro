package controllers;

import classes.MyDataBase;
import classes.MyStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tables.Names;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static controllers.Add.AddStage;
import static controllers.Menu.menuOut;

public class Storage implements Initializable {

    public void menu(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Menu", "Меню");

        menuOut.hide();
        try {
            AddStage.hide();
        } catch (Exception e){

        }
    }

    public void addNew(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Add", "Добавление");

        if(AddStage != null){
            AddStage.hide();
        }
        if(menuOut != null){
            menuOut.hide();
        }

    }

    private MyStage stage = new MyStage();

    @FXML
    static Stage StorageStage;

    private void changeStage(Stage primaryStage, String scene, String nameScene) throws IOException {
        Storage.StorageStage = primaryStage;
        stage.setStage(primaryStage);
        stage.getStage().setTitle(nameScene);
        stage.getStage().setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + scene + ".fxml"));
        Scene enterScene = new Scene(root);
        stage.getStage().setScene(enterScene);
        stage.getStage().show();
    }

    @FXML
    private TableView storageTableView;

    @FXML
    Label excelLable;

    @FXML
    private TableColumn name, group, price_in, price_out, balanceStorage, balanceShop, balanceMan;

    private ObservableList<Names> data = FXCollections.observableArrayList();

    private void initData() throws SQLException {
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM names ORDER BY id_group, name");
        while (rs.next()) {
            data.add(new Names(
                    rs.getString("name"),
                    Names.getGroupName(rs.getInt("id_group")),
                    rs.getString("price_in"),
                    rs.getString("price_out") +" / " + rs.getString("price_mesh"),
                    rs.getString("balance_stor"),
                    rs.getString("balance_manufacture"),
                    rs.getString("balance_shop")
            ));
        }
        mdb.getCloseConn();
        s.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("console.encoding","Cp866");
        excelLable.setVisible(false);
        try {
            initData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        storageTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // устанавливаем тип и значение которое должно хранится в колонке
        name.setCellValueFactory(new PropertyValueFactory("name"));
        group.setCellValueFactory(new PropertyValueFactory("group"));
        price_in.setCellValueFactory(new PropertyValueFactory("price_in"));
        price_out.setCellValueFactory(new PropertyValueFactory("price_out"));
        balanceStorage.setCellValueFactory(new PropertyValueFactory("balanceStorage"));
        balanceMan.setCellValueFactory(new PropertyValueFactory("balanceMan"));
        balanceShop.setCellValueFactory(new PropertyValueFactory("balanceShop"));

        // заполняем таблицу данными
        storageTableView.setItems(data);

    }

    public void excel(ActionEvent actionEvent) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Storage");

        for(int i = 0; i < data.size(); i++){
            Row row = spreadsheet.createRow(i);
            if(i == 0){
                Cell head0 = row.createCell(0);
                Cell head1 = row.createCell(1);
                Cell head2 = row.createCell(2);
                Cell head3 = row.createCell(3);
                Cell head4 = row.createCell(4);
                Cell head5 = row.createCell(5);
                Cell head6 = row.createCell(6);
                Cell head7 = row.createCell(7);

                head0.setCellValue("Наименование");
                head1.setCellValue("Группа");
                head2.setCellValue("Цена (приход)");
                head3.setCellValue("Цена (розница)");
                head4.setCellValue("Цена (опт)");
                head5.setCellValue("Склад");
                head6.setCellValue("Производство");
                head7.setCellValue("Магазин");
            } else {
                Cell newCell0 = row.createCell(0);
                Cell newCell1 = row.createCell(1);
                Cell newCell2 = row.createCell(2);
                Cell newCell3 = row.createCell(3);
                Cell newCell4 = row.createCell(4);
                Cell newCell5 = row.createCell(5);
                Cell newCell6 = row.createCell(6);
                Cell newCell7 = row.createCell(7);

                newCell0.setCellValue(data.get(i-1).getName());
                newCell1.setCellValue(data.get(i-1).getGroup());
                newCell2.setCellValue(data.get(i-1).getPrice_in());

                String[] priceArr = data.get(i-1).getPrice_out().split(" / ");
                newCell3.setCellValue(priceArr[0]);
                newCell4.setCellValue(priceArr[1]);

                newCell5.setCellValue(data.get(i-1).getBalanceStorage());
                newCell6.setCellValue(data.get(i-1).getBalanceMan());
                newCell7.setCellValue(data.get(i-1).getBalanceShop());
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        Date date = new Date();
        FileOutputStream fileOut = new FileOutputStream("Наличие на "+dateFormat.format(date)+".xlsx");
        workbook.write(fileOut);
        fileOut.close();
        excelLable.setVisible(true);

    }

}
