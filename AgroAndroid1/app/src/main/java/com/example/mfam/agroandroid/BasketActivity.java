package com.example.mfam.agroandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static com.example.mfam.agroandroid.MainActivity.day;

public class BasketActivity extends AppCompatActivity {

    TableLayout basketTable;
    TextView allSum;
    TextView skidTV;

    double sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("console.encoding","Cp866");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        basketTable = (TableLayout) findViewById(R.id.basketTL);


        for(int i = 0; i < AddActivity.basketArrItem; i++) {
            TableRow newRow = new TableRow(this);

            if (i % 2 == 0) {
                newRow.setBackgroundColor(Color.rgb(177, 194, 221));
            }
// add views to the row
            TextView n = new TextView(this);
            TextView w = new TextView(this);
            TextView p = new TextView(this);

            n.setText("  "+AddActivity.basketArr[i][0]);
            w.setText("  "+AddActivity.basketArr[i][1]);
            p.setText("  "+AddActivity.basketArr[i][2]);

            sum += Double.valueOf(AddActivity.basketArr[i][2]);

            n.setTextSize(24);
            w.setTextSize(24);
            p.setTextSize(24);

            n.setTextColor(0xff000000);
            w.setTextColor(0xff000000);
            p.setTextColor(0xff000000);

            newRow.addView(n);
            newRow.addView(w);
            newRow.addView(p);

            basketTable.addView(newRow);
        }

        TableRow newRow = new TableRow(this);

        skidTV = new TextView(this);
        TextView w = new TextView(this);
        allSum = new TextView(this);

        skidTV.setText("  ");
        w.setText("  Сумма:");
        allSum.setText("  "+String.valueOf(sum));

        skidTV.setTextSize(30);
        w.setTextSize(30);
        allSum.setTextSize(30);

        skidTV.setTextColor(0xff000000);
        w.setTextColor(0xff000000);
        allSum.setTextColor(0xff000000);

        newRow.addView(skidTV);
        newRow.addView(w);
        newRow.addView(allSum);

        basketTable.addView(newRow);

    }

    public void addBooking(View v){
        String basketStrArray = "";
        addBookingBool = true;
        for (int i = 0; i < AddActivity.basketArrItem; i++){
            basketStrArray += AddActivity.basketArr[i][0]+"#";
            basketStrArray += AddActivity.basketArr[i][1]+"#";
            basketStrArray += AddActivity.basketArr[i][2]+"#";
        }
        Client test = new Client();

        test.execute("1&"+MainActivity.seller+"&"+(new BigDecimal(Double.valueOf(sum-skid)).setScale(1, RoundingMode.UP).doubleValue())+"&"+ AddActivity.basketArrItem+"&"+basketStrArray+"&"+day);

        Toast.makeText(getApplicationContext(),""+test.getSp()[1], Toast.LENGTH_LONG).show();

        resumeBasket(v);

        startActivity(new Intent("com.example.mfam.agroandroid.GroupActivity"));

    }

    boolean addBookingBool = false;
    boolean resume = false;
    public void resumeBasket(View v){
        EditText passAn = (EditText) findViewById(R.id.passAn);
        if (resume && Objects.equals(passAn.getText().toString(), "0702") || addBookingBool){
            addBookingBool = false;
            AddActivity.basketArrItem = 0;
            AddActivity.basketArr = new String[24][3];
            resume = false;
            passAn.setVisibility(View.INVISIBLE);
            passAn.setText("");
            startActivity(new Intent("com.example.mfam.agroandroid.GroupActivity"));
        } else if (!resume){
            resume = true;
            passAn.setVisibility(View.VISIBLE);
        } else {
            resume = false;
            passAn.setText("");
            passAn.setVisibility(View.INVISIBLE);
        }
    }

    double skid = 0;
    boolean resumeSkid = false;
    public void skidBasket(View v){
        EditText skidET = (EditText) findViewById(R.id.skidET);
        Switch skidSwitch = (Switch) findViewById(R.id.switch1);
        TextView grn = (TextView) findViewById(R.id.grn);

        if (resumeSkid){

            if(Objects.equals(skidET.getText().toString(), "")){
                resumeSkid = false;
                skidET.setVisibility(View.INVISIBLE);
                skidSwitch.setVisibility(View.INVISIBLE);
                grn.setVisibility(View.INVISIBLE);
                return;
            }

            if(skidSwitch.isChecked()){
                skid = Double.valueOf(String.valueOf(skidET.getText()));
                allSum.setText("  "+(sum-skid) + " грн");
                skidTV.setText("Скидка  "+skid + " грн");
            } else {
                skid = (sum*Double.valueOf(String.valueOf(skidET.getText())))/100;
                allSum.setText("  "+(new BigDecimal(sum - skid).setScale(1, RoundingMode.UP).doubleValue()) + " грн");
                skidTV.setText("Скидка  "+ (new BigDecimal(skid).setScale(1, RoundingMode.UP).doubleValue()) + " грн ("+skidET.getText()+"%)");
            }
            resumeSkid = false;
            skidET.setVisibility(View.INVISIBLE);
            skidSwitch.setVisibility(View.INVISIBLE);
            grn.setVisibility(View.INVISIBLE);
            skidET.setText("");
        } else if (!resumeSkid){
            resumeSkid = true;
            skidET.setVisibility(View.VISIBLE);
            skidSwitch.setVisibility(View.VISIBLE);
            grn.setVisibility(View.VISIBLE);
        } else {
            skidET.setText("");
        }
    }
}
