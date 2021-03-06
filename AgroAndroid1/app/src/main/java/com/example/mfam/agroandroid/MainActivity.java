package com.example.mfam.agroandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static String seller;
    public static String day;
    EditText ipET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        ipET = (EditText) findViewById(R.id.ipET);

        day = String.valueOf(dateFormat.format(date));
        System.out.println(day);
        System.setProperty("console.encoding","Cp866");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*for(int i = 0; i<5; i++){
            Client test = new Client();
            test.execute("0&id_seller&seller& id_seller = 1");
        }
        */
    }

    public void enter(View v){
        TextView pass = (TextView) findViewById(R.id.password);
        if(Objects.equals(pass.getText().toString(), "")){
            return;
        }

        try {
            Client test = new Client();
            //test.setIp(ipET.getText().toString());
            test.execute("0&id_seller&seller& pass = '"+pass.getText().toString()+"'");
            pass.setText("");
            if(test.getSp().length>1) {
                seller = test.getSp()[1];
                startActivity(new Intent("com.example.mfam.agroandroid.GroupActivity"));
            }
        } catch (IllegalStateException e){
            Toast.makeText(getApplicationContext(),"Неверный пароль!", Toast.LENGTH_LONG).show();
        }

    }

    public void send_text(View v) {
        Button test = (Button) findViewById(R.id.btnServer);

        Toast.makeText(getApplicationContext(),"Test: "+ test.getText(), Toast.LENGTH_LONG).show();
    }


    /* Переход на следующее активити
    startActivity(new Intent("com.example.mfam.agroandroid.GroupActivity"));
    */

}
