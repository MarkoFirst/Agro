package com.example.mfam.agroandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class AddActivity extends AppCompatActivity {

    public static String[][] basketArr = new String[24][3];
    public static int basketArrItem = 0;

    EditText newWeight;
    TextView weight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("console.encoding","Cp866");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        newWeight = (EditText) findViewById(R.id.newWeight);
        TextView nameNow = (TextView) findViewById(R.id.textView4);
        weight = (TextView) findViewById(R.id.weightInShop);

        nameNow.setText(NamesActivity.nameBut);

        Client test = new Client();
        test.execute("0&balance_shop&names& name = '"+ NamesActivity.nameBut+"'");
        weight.setText(test.getSp()[1]);

    }

    public void add(View v){

        if(Double.valueOf(String.valueOf(newWeight.getText())) > Double.valueOf(String.valueOf(weight.getText()))){
            weight.setTextColor(Color.RED);
            return;
        }

        Client test = new Client();
        Switch kgSwitch = (Switch) findViewById(R.id.kgSwitch);

        if(kgSwitch.isChecked()) {
            test.execute("0&price_mesh&names& name = '" + NamesActivity.nameBut + "'");
        } else {
            test.execute("0&price_out&names& name = '" + NamesActivity.nameBut + "'");
        }
        basketArr[basketArrItem][0] = NamesActivity.nameBut;
        basketArr[basketArrItem][1] = String.valueOf(newWeight.getText());
        basketArr[basketArrItem][2] = String.valueOf(new BigDecimal(Double.valueOf(test.getSp()[1]) * Double.valueOf(String.valueOf(newWeight.getText()))).setScale(1, RoundingMode.UP).doubleValue());
        basketArrItem++;

        System.out.println("_______________________________"+Arrays.toString(test.getSp()));


        startActivity(new Intent("com.example.mfam.agroandroid.GroupActivity"));
    }
}
