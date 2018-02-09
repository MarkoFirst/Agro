package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import classes.MyDataBase;
import classes.MyStage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.Main.EnterStage;
import static controllers.Menu.menuOut;

public class EnterControll {

    @FXML
    TextField enterInput;

    static String name = "";
    static int idSeller = 0;

    @FXML
    public void enter(ActionEvent actionEvent) throws IOException {
        System.setProperty("console.encoding","Cp866");
        String pass = "";
        try {
            MyDataBase mdb = new MyDataBase();
            Statement s = mdb.getConn().createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM seller WHERE pass = '"+enterInput.getText()+"'");
            while (rs.next()) {
                EnterControll.idSeller = rs.getInt("id_seller");
                EnterControll.name = rs.getString("seller_name");
                pass = rs.getString("pass");
            }
            mdb.getCloseConn();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!pass.equals("")){
            enterInput.setText("");
            EnterStage.close();
            try { menuOut.hide(); } catch (NullPointerException ignored) { }
            changeStage(stage, "Menu", "Меню");
        }
        enterInput.setText("");
    }

    private MyStage stage = new MyStage();

    @FXML
    static Stage MenuStage;

    private void changeStage(Stage primaryStage, String scene, String nameScene) throws IOException {
        EnterControll.MenuStage = primaryStage;
        stage.setStage(primaryStage);
        stage.getStage().setTitle(nameScene);
        stage.getStage().setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/"+scene+".fxml"));
        Scene enterScene = new Scene(root);
        stage.getStage().setScene(enterScene);
        stage.getStage().show();
    }

}