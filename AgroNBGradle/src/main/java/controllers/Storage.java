package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import classes.MyDataBase;
import classes.MyStage;
import tables.Names;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static controllers.Add.AddStage;
import static controllers.Menu.menuOut;

public class Storage implements Initializable {

    public void menu(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Menu", "Меню");
        try{
            menuOut.hide();
            AddStage.hide();
        } catch (Exception ignored){ }
    }

    public void add(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Add", "Добавление");
        try{
            menuOut.hide();
            AddStage.hide();
        } catch (Exception ignored){ }

    }

    public void createExcel(){

    }

    private MyStage stage = new MyStage();

    @FXML
    static Stage StorageStage;

    private void changeStage(Stage primaryStage, String scene, String nameScene) throws IOException {
        Storage.StorageStage = primaryStage;
        stage.setStage(primaryStage);
        stage.getStage().setTitle(nameScene);
        stage.getStage().setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/" + scene + ".fxml"));
        Scene enterScene = new Scene(root);
        stage.getStage().setScene(enterScene);
        stage.getStage().show();
    }

    @FXML
    private TableView storageTableView;

    @FXML
    private TableColumn name, group, price_in, price_out, balanceStorage, balanceShop;

    private ObservableList data = FXCollections.observableArrayList();

    private void initData() throws SQLException {
        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM names");
        while (rs.next()) {
            data.add(new Names(
                    rs.getString("name"),
                    Names.getGroupName(rs.getInt("id_group")),
                    rs.getString("price_in"),
                    rs.getString("price_out"),
                    rs.getString("balance_stor"),
                    rs.getString("balance_shop")
            ));
        }
        mdb.getCloseConn();
        s.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // устанавливаем тип и значение которое должно хранится в колонке
        name.setCellValueFactory(new PropertyValueFactory("name"));
        group.setCellValueFactory(new PropertyValueFactory("group"));
        price_in.setCellValueFactory(new PropertyValueFactory("price_in"));
        price_out.setCellValueFactory(new PropertyValueFactory("price_out"));
        balanceStorage.setCellValueFactory(new PropertyValueFactory("balanceStorage"));
        balanceShop.setCellValueFactory(new PropertyValueFactory("balanceShop"));

        // заполняем таблицу данными
        storageTableView.setItems(data);

    }
}
