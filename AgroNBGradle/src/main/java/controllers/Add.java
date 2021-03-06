package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import classes.MyDataBase;
import classes.MyStage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

import static controllers.Menu.menuOut;
import static controllers.Storage.StorageStage;

public class Add implements Initializable {

    public void menu(ActionEvent actionEvent) throws IOException {
        changeStage(stage, "Menu", "Меню");
        menuOut.hide();
        try {
            StorageStage.hide();
        } catch (Exception e) {

        }

    }

    public void done(ActionEvent actionEvent) throws IOException, SQLException, InterruptedException {
        pricePane.setVisible(false);
        add();
        changeStage(stage, "Storage", "Склад");
        menuOut.hide();
        try {
            StorageStage.hide();
        } catch (Exception e) {

        }
    }

    public void next(ActionEvent actionEvent) throws IOException, SQLException {
        add();
    }

    public void add() throws SQLException {
        pricePane.setVisible(false);
        String[] forExcel = new String[4];
        if (!Objects.equals(amount.getText(), "")) {
            MyDataBase mdb = new MyDataBase();
            Statement stmt = mdb.getConn().createStatement();
            if (triger) {
                ResultSet rs = stmt.executeQuery("SELECT id_group FROM group_name WHERE group_name='" + groupActive + "'");
                int groupId = 1;
                while (rs.next()) {
                    groupId = rs.getInt("id_group");
                }
                double shop = 0;
                double stor = 0;
                double man = 0;

                if (placeActive.equals("balance_shop")) {
                    shop = Double.valueOf(amount.getText());
                } else if (placeActive.equals("balance_stor")) {
                    stor = Double.valueOf(amount.getText());
                } else {
                    man = Double.valueOf(amount.getText());
                }
                stmt.execute("INSERT INTO names (name, id_group, price_in, price_out,price_mesh, balance_shop, balance_stor, balance_manufacture) " +
                        "VALUES(" +
                        "'" + newNameTF.getText() + "', " +
                        "" + groupId + ", " +
                        "0, " +
                        "0, " +
                        "0, " +
                        "" + shop + ", " +
                        "" + stor + ", " +
                        "" + man + ")"
                );
                nameActive = newNameTF.getText();
                System.out.println(groupActive);
                names(groupActive);
                newNameTF.setVisible(false);
                nameChoice.setVisible(true);
                triger = false;
                newNameTF.setText("");
            } else {
                String stringForPlace = " ";
                if (!placeActiveIn.equals("")) {
                    stringForPlace = ", " + placeActiveIn + " = " + placeActiveIn + " - " + amount.getText();
                }
                stmt.execute("UPDATE names " +
                        "SET " + placeActive + "=" + placeActive + "+" + amount.getText() +
                        stringForPlace +
                        "WHERE name = '" + nameActive + "';");
                forExcel[0] = nameActive;
                forExcel[1] = amount.getText();
                forExcel[2] = placeChoiceIn.getValue().toString();
                forExcel[3] = placeChoice.getValue().toString();
                try {
                    excel(forExcel);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidFormatException e) {
                    e.printStackTrace();
                }
            }
            mdb.getCloseConn();
            stmt.close();


        } else {
            System.out.println("Пусто");
        }
        amount.setText("");
    }

    String nameActive;
    String placeActive = "balance_stor";
    String placeActiveIn = "balance_stor";
    String groupActive = "Зерновые";

    MyStage stage = new MyStage();

    @FXML
    static Stage AddStage;

    @FXML
    ChoiceBox groupChoice, nameChoice, placeChoice, placeChoiceIn;

    @FXML
    TextField amount, newNameTF, priceInTF, priceOutTF, priceMeshTF;

    @FXML
    Pane pricePane;

    public void changeStage(Stage primaryStage, String scene, String nameScene) throws IOException {
        Add.AddStage = primaryStage;
        stage.setStage(primaryStage);
        stage.getStage().setTitle(nameScene);
        stage.getStage().setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + scene + ".fxml"));
        Scene enterScene = new Scene(root);
        stage.getStage().setScene(enterScene);
        stage.getStage().show();
    }

    private boolean triger = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("console.encoding", "Cp866");
        placeChoice.setItems(FXCollections.observableArrayList("Склад", "Магазин", "Производство"));
        placeChoice.getSelectionModel().selectFirst();

        placeChoiceIn.setItems(FXCollections.observableArrayList("Склад", "Производство", "Поставщик", "Магазин"));
        placeChoiceIn.getSelectionModel().selectFirst();

        String[] groupNames = new String[10];
        try {
            MyDataBase mdb = new MyDataBase();
            Statement s = mdb.getConn().createStatement();
            ResultSet rs = s.executeQuery("SELECT group_name, id_group FROM group_name");
            for (int i = 0; i < 10; i++) {
                if (rs.next()) {
                    groupNames[i] = rs.getString("group_name");
                } else {
                    break;
                }
            }
            mdb.getCloseConn();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        groupChoice.setItems(FXCollections.observableArrayList(groupNames));
        groupChoice.getSelectionModel().selectFirst();

        try {
            names("Зерновые");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        nameChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                pricePane.setVisible(false);
                try {
                    nameActive = String.valueOf(nameChoice.getItems().get((Integer) number2));
                } catch (Exception e) {
                }
                if (Objects.equals(nameActive, "Другое")) {
                    newNameTF.setVisible(true);
                    nameChoice.setVisible(false);
                    triger = true;
                }
            }
        });
        groupChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                pricePane.setVisible(false);
                groupActive = String.valueOf(groupChoice.getItems().get((Integer) number2));
                try {
                    names(groupActive);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        placeChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                pricePane.setVisible(false);
                if (Objects.equals(String.valueOf(placeChoice.getItems().get((Integer) number2)), "Склад")) {
                    placeActive = "balance_stor";
                } else if (Objects.equals(String.valueOf(placeChoice.getItems().get((Integer) number2)), "Магазин")) {
                    placeActive = "balance_shop";
                } else {
                    placeActive = "balance_manufacture";
                }
            }
        });
        placeChoiceIn.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                pricePane.setVisible(false);
                if (Objects.equals(String.valueOf(placeChoiceIn.getItems().get((Integer) number2)), "Склад")) {
                    placeActiveIn = "balance_stor";
                } else if (Objects.equals(String.valueOf(placeChoiceIn.getItems().get((Integer) number2)), "Производство")) {
                    placeActiveIn = "balance_manufacture";
                } else if (Objects.equals(String.valueOf(placeChoiceIn.getItems().get((Integer) number2)), "Магазин")) {
                    placeActiveIn = "balance_shop";
                } else {
                    placeActiveIn = "";
                }
            }
        });

        nameActive = nameChoice.getValue().toString();
    }

    public void names(String idGroup) throws SQLException {

        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT name FROM names WHERE id_group = (" +
                "SELECT id_group FROM group_name WHERE group_name = '" + idGroup + "') " +
                "ORDER BY name");
        String[] namesForChoice = new String[24];
        int i = 0;
        while (rs.next()) {
            namesForChoice[i] = rs.getString("name");
            i++;
        }
        String[] namesForChoiceOK = new String[i + 1];
        for (int k = 0; k < i; k++) {
            namesForChoiceOK[k] = namesForChoice[k];
        }
        namesForChoiceOK[i] = "Другое";
        mdb.getCloseConn();
        s.close();
        nameChoice.setItems(FXCollections.observableArrayList(namesForChoiceOK));
        nameChoice.getSelectionModel().selectFirst();
    }

    public void priceDone(ActionEvent actionEvent) throws SQLException {
        pricePane.setVisible(false);

        if (priceInTF.getText().equals("")) {
            return;
        }
        if (priceOutTF.getText().equals("")) {
            return;
        }
        if (priceMeshTF.getText().equals("")) {
            return;
        }

        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        s.execute("UPDATE names SET " +
                "price_in =  " + priceInTF.getText() +
                ", price_out =  " + priceOutTF.getText() +
                ", price_mesh =  " + priceMeshTF.getText() +
                "WHERE name = '" + nameActive + "'");
        mdb.getCloseConn();
        s.close();
    }

    public void changePrice(ActionEvent actionEvent) throws SQLException {

        if (nameActive.equals("Другое")) {
            return;
        }
        pricePane.setVisible(true);

        MyDataBase mdb = new MyDataBase();
        Statement s = mdb.getConn().createStatement();
        ResultSet rs = s.executeQuery("SELECT price_in, price_out, price_mesh FROM names WHERE name = '" + nameActive + "'");
        while (rs.next()) {
            priceInTF.setText(rs.getString("price_in"));
            priceOutTF.setText(rs.getString("price_out"));
            priceMeshTF.setText(rs.getString("price_mesh"));
        }
        if (priceInTF.getText().equals("")) {
            priceInTF.setText("0.00");
        }
        if (priceOutTF.getText().equals("")) {
            priceInTF.setText("0.00");
        }
        if (priceMeshTF.getText().equals("")) {
            priceInTF.setText("0.00");
        }
        mdb.getCloseConn();
        s.close();
    }

    public void excel(String[] data) throws IOException, InvalidFormatException {

        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        Date date = new Date();

        String logXlsx = "./Перемещения/Перемещения " + dateFormat.format(date) + ".xlsx";
        Workbook workbook;
        if (new File(logXlsx).exists()) {
            InputStream in = new FileInputStream(logXlsx);
            workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);

            Row row = sheet.createRow(sheet.getLastRowNum()+1);

            Cell newCell0 = row.createCell(0);
            Cell newCell1 = row.createCell(1);
            Cell newCell2 = row.createCell(2);
            Cell newCell3 = row.createCell(3);
            Cell newCell4 = row.createCell(4);

            newCell0.setCellValue(row.getRowNum());
            newCell1.setCellValue(data[0]);
            newCell2.setCellValue(Double.valueOf(data[1]));
            newCell3.setCellValue(data[2]);
            newCell4.setCellValue(data[3]);

            in.close();
        } else {
            workbook = new XSSFWorkbook();
            Sheet spreadsheet = workbook.createSheet("Logs");

            Row row = spreadsheet.createRow(0);

            Cell head0 = row.createCell(0);
            Cell head1 = row.createCell(1);
            Cell head2 = row.createCell(2);
            Cell head3 = row.createCell(3);
            Cell head4 = row.createCell(4);

            head0.setCellValue("№");
            head1.setCellValue("Наименование");
            head2.setCellValue("Вес");
            head3.setCellValue("Откуда");
            head4.setCellValue("Куда");

            row = spreadsheet.createRow(spreadsheet.getLastRowNum()+1);

            Cell newCell0 = row.createCell(0);
            Cell newCell1 = row.createCell(1);
            Cell newCell2 = row.createCell(2);
            Cell newCell3 = row.createCell(3);
            Cell newCell4 = row.createCell(4);

            newCell0.setCellValue(row.getRowNum());
            newCell1.setCellValue(data[0]);
            newCell2.setCellValue(Double.valueOf(data[1]));
            newCell3.setCellValue(data[2]);
            newCell4.setCellValue(data[3]);
        }

        FileOutputStream fileOut = new FileOutputStream(logXlsx);
        workbook.write(fileOut);
        fileOut.close();
    }
}
