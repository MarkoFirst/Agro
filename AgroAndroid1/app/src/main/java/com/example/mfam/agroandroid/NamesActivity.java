package com.example.mfam.agroandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NamesActivity extends AppCompatActivity  {

    public static String nameBut = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        TextView groupeName = (TextView) findViewById(R.id.nameTV);
        groupeName.setText(""+GroupActivity.group);

        //Toast.makeText(getApplicationContext(),"ID = " + GroupActivity.group, Toast.LENGTH_LONG).show();

        Client test = new Client();
        test.execute("0&name&names&id_group = "+GroupActivity.group);

        String[] names = test.getSp();

        initButtons();
        clearBut();

        for(int k = 1; k<names.length; k++){
            arrBut[k-1].setText(names[k]);
            arrBut[k-1].setVisibility(View.VISIBLE);
        }
    }

    public void b1Act(View v) { stage3 (String.valueOf(arrBut[0].getText()));}
    public void b2Act(View v) { stage3 (String.valueOf(arrBut[1].getText()));}
    public void b3Act(View v) { stage3 (String.valueOf(arrBut[2].getText()));}
    public void b4Act(View v) { stage3 (String.valueOf(arrBut[3].getText()));}
    public void b5Act(View v) { stage3 (String.valueOf(arrBut[4].getText()));}
    public void b6Act(View v) { stage3 (String.valueOf(arrBut[5].getText()));}
    public void b7Act(View v) { stage3 (String.valueOf(arrBut[6].getText()));}
    public void b8Act(View v) { stage3 (String.valueOf(arrBut[7].getText()));}
    public void b9Act(View v) { stage3 (String.valueOf(arrBut[8].getText()));}
    public void b10Act(View v ) { stage3 (String.valueOf(arrBut[9].getText()));}
    public void b11Act(View v ) { stage3 (String.valueOf(arrBut[10].getText( )));}
    public void b12Act(View v ) { stage3 (String.valueOf(arrBut[11].getText( )));}
    public void b13Act(View v ) { stage3 (String.valueOf(arrBut[12].getText( )));}
    public void b14Act(View v ) { stage3 (String.valueOf(arrBut[13].getText( )));}
    public void b15Act(View v ) { stage3 (String.valueOf(arrBut[14].getText( )));}
    public void b16Act(View v ) { stage3 (String.valueOf(arrBut[15].getText( )));}
    public void b17Act(View v ) { stage3 (String.valueOf(arrBut[16].getText( )));}
    public void b18Act(View v ) { stage3 (String.valueOf(arrBut[17].getText( )));}

    private Button[] arrBut = new Button[18];

    private void initButtons(){
        arrBut[0] = (Button) findViewById(R.id.button2);
        arrBut[1] = (Button) findViewById(R.id.button3);
        arrBut[2] = (Button) findViewById(R.id.button4);
        arrBut[3] = (Button) findViewById(R.id.button5);
        arrBut[4] = (Button) findViewById(R.id.button6);
        arrBut[5] = (Button) findViewById(R.id.button7);
        arrBut[6] = (Button) findViewById(R.id.button8);
        arrBut[7] = (Button) findViewById(R.id.button9);
        arrBut[8] = (Button) findViewById(R.id.button10);
        arrBut[9] = (Button) findViewById(R.id.button11);
        arrBut[10] = (Button) findViewById(R.id.button12);
        arrBut[11] = (Button) findViewById(R.id.button13);
        arrBut[12] = (Button) findViewById(R.id.button14);
        arrBut[13] = (Button) findViewById(R.id.button15);
        arrBut[14] = (Button) findViewById(R.id.button16);
        arrBut[15] = (Button) findViewById(R.id.button17);
        arrBut[16] = (Button) findViewById(R.id.button18);
        arrBut[17] = (Button) findViewById(R.id.button19);
    }

    private void clearBut(){
        for(int i = 0; i<18; i++){
            arrBut[i].setText("");
            arrBut[i].setVisibility(View.INVISIBLE);
        }
    }

    private void stage3(String nameButton){
        nameBut = nameButton;
        startActivity(new Intent("com.example.mfam.agroandroid.AddActivity"));
    }
}
