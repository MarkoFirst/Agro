package com.example.mfam.agroandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StorageActivity extends AppCompatActivity {

    TableLayout storageTable;
    String[] storArr;
    TextView pageTV;
    int item;
    int page;
    TableRow[] trArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("console.encoding","Cp866");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        storageTable = (TableLayout) findViewById(R.id.storageTL);
        pageTV = (TextView) findViewById(R.id.pageTV);
        page = 1;
        item = 1;


        Client test = new Client();
        test.execute("2");
        storArr = test.getSp();
        initData();

    }

    private void initData(){
        int dop = 0;
        trArr = new TableRow[30];
        pageTV.setText(String.valueOf(page));
        for(int i = item ; i < (150 * page); i += 5) {
            if(i > storArr.length - 5){
                return;
            }

            TableRow newRow = new TableRow(this);
            trArr[dop] =newRow;
            dop++;

            TextView name = new TextView(this);
            TextView price = new TextView(this);
            TextView shop = new TextView(this);
            TextView stor = new TextView(this);
            TextView man = new TextView(this);

            name.setText("  " + storArr[i]);
            price.setText("  " + storArr[i+1]);
            shop.setText("  " + storArr[i+2]);
            stor.setText("  " + storArr[i+3]);
            man.setText("  " + storArr[i+4]);

            name.setTextSize(24);
            price.setTextSize(24);
            shop.setTextSize(24);
            stor.setTextSize(24);
            man.setTextSize(24);

            name.setTextColor(0xff000000);
            price.setTextColor(0xff000000);
            shop.setTextColor(0xff000000);
            stor.setTextColor(0xff000000);
            man.setTextColor(0xff000000);

            newRow.addView(name);
            newRow.addView(price);
            newRow.addView(shop);
            newRow.addView(stor);
            newRow.addView(man);

            storageTable.addView(newRow);
        }

    }

    public void nextPage(View v){
        if( page*150 > storArr.length ){
            return;
        }
        page++;
        item+=150;
        cleanData();
        initData();
    }

    private void cleanData() {
        for(int i = 0; i<30; i++){
            storageTable.removeView(trArr[i]);
        }
    }

    public void backPage(View v){
        if( page < 2 ){
            return;
        }
        page --;
        item -= 150;
        cleanData();
        initData();
    }

    public void goToAddStor(View v){
        startActivity(new Intent("com.example.mfam.agroandroid.AddStorActivity"));
    }

}
