package com.example.mfam.agroandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.example.mfam.agroandroid.MainActivity.day;

public class GroupActivity extends AppCompatActivity {

    public static int group = 0;
    EditText dateET;

    TextView sumDay;
    Button basketBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        System.setProperty("console.encoding","Cp866");

        dateET = (EditText) findViewById(R.id.dateET);

        dateET.setText(String.valueOf(day));



        TextView sellerTV = (TextView) findViewById(R.id.sellerTV);

        Client test = new Client();
        test.execute("0&seller_name&seller& id_seller ="+MainActivity.seller);
        sellerTV.setText(test.getSp()[1]);

        sumDay = (TextView) findViewById(R.id.sumForDay);
        basketBtn = (Button) findViewById(R.id.basketBtn);
        String dop = "";
        if(AddActivity.basketArrItem > 0){
            dop = "  ("+AddActivity.basketArrItem+")";
        }
        basketBtn.setText("Корзина" + dop);
    }

    public void zern(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 1;}
    public void drob(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 2;}
    public void gran(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 3;}
    public void smesZern(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 4;}
    public void bestMix(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 5;}
    public void olkar(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 6;}
    public void krug(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 7;}
    public void kramar(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 8;}
    public void prod(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 9;}
    public void raz(View v){ startActivity(new Intent("com.example.mfam.agroandroid.NamesActivity")); group = 10;}



    public void summDay(View v){

        Client test = new Client();
        test.execute("0&cost_all&booking& date = '"+day+"'");

        double sum = 0;
        for(int i = 1; i < test.getSp().length; i++){
            sum += Double.valueOf(test.getSp()[i]);
        }

        sumDay.setText((new BigDecimal(Double.valueOf(sum)).setScale(1, RoundingMode.UP).doubleValue())+" грн. / Заказов: " + (test.getSp().length-1));

    }

    public void goToBasket(View v){
        startActivity(new Intent("com.example.mfam.agroandroid.BasketActivity"));
    }

    public void goToStorage(View v){
        startActivity(new Intent("com.example.mfam.agroandroid.StorageActivity"));
    }

    public void exit(View v){
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    boolean dateC = false;
    public void dateChange(View v){

        if(dateC){
            day = String.valueOf(dateET.getText());
            dateET.setVisibility(View.INVISIBLE);
            dateC = false;
        } else {
            dateET.setVisibility(View.VISIBLE);
            dateC = true;
        }

    }



}
