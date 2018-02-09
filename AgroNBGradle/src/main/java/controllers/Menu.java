package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import classes.MyStage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static controllers.Add.AddStage;
import static controllers.AllBookings.AllBookingsStage;
import static controllers.Booking.BookingStage;
import static controllers.EnterControll.MenuStage;
import static controllers.EnterControll.name;
import static controllers.Storage.StorageStage;

public class Menu  implements Initializable {

    @FXML
    static Stage menuOut;

    @FXML
    Label user;

    public void storage(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Storage", "Склад");
        closeAll();
    }

    public void add(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Add", "Поставка");
        closeAll();
    }

    public void allBookings(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "AllBookings", "Все заказы");
        closeAll();
    }

    public void booking(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Booking", "Заказ");
        closeAll();
    }

    public void deleteBooking(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "DeleteBooking", "Удаление заказа");
        closeAll();
    }

    public void basket(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Basket", "Корзина");
        closeAll();
    }

    public void exit(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "enter", "Agro");
        closeAll();
    }

    private MyStage stage = new MyStage();

    private void closeAll() {
        try {
            MenuStage.hide();
        } catch (NullPointerException ignored) {
        }
        try {
            AddStage.hide();
        } catch (NullPointerException ignored) {
        }
        try {
            AllBookingsStage.hide();
        } catch (NullPointerException ignored) {
        }
        try {
            BookingStage.hide();
        } catch (NullPointerException ignored) {
        }
        try {
            StorageStage.hide();
        } catch (NullPointerException ignored) {
        }
    }

    private void changeStage(Stage primaryStage, String scene, String nameScene) throws IOException {
        Menu.menuOut = primaryStage;
        stage.setStage(primaryStage);
        stage.getStage().setTitle(nameScene);
        stage.getStage().setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + scene + ".fxml"));
        Scene enterScene = new Scene(root);
        stage.getStage().setScene(enterScene);
        stage.getStage().show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user.setText(name);
    }
}