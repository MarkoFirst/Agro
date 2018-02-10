package com.example.mfam.agroandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Objects;

import static android.R.layout.simple_spinner_item;

public class AddStorActivity extends AppCompatActivity {

    String[] dataGroup = {"Зерновые", "Дробленка", "Гранула", "Смеси зерновые", "Best mix", "Олкар", "Круг", "Крамар", "Продукты", "Разное"};
    String[] dataPriceIn = {"Склад", "Магазин", "Производство"};
    String[] dataPriceOut = {"Склад", "Производство", "Поставщик", "Магазин"};
    String[] dataName;
    Spinner nameSpin;
    ArrayAdapter<String> adapterName;
    EditText weight;
    Spinner groupSpin;
    Spinner placeInSpin;
    Spinner placeOutSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("console.encoding","Cp866");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stor);

        ArrayAdapter<String> adapterGroup = new ArrayAdapter<String>(this, simple_spinner_item, dataGroup);
        adapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterPlaceIn = new ArrayAdapter<String>(this, simple_spinner_item, dataPriceIn);
        adapterPlaceIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterPlaceOut = new ArrayAdapter<String>(this, simple_spinner_item, dataPriceOut);
        adapterPlaceOut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        groupSpin = (Spinner) findViewById(R.id.groupSpin);
        placeInSpin = (Spinner) findViewById(R.id.placeInSpin);
        placeOutSpin = (Spinner) findViewById(R.id.placeOutSpin);

        groupSpin.setAdapter(adapterGroup);
        placeInSpin.setAdapter(adapterPlaceIn);
        placeOutSpin.setAdapter(adapterPlaceOut);

        // выделяем элемент
        groupSpin.setSelection(0);
        placeInSpin.setSelection(0);
        placeOutSpin.setSelection(0);

        // устанавливаем обработчик нажатия
        groupSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                Client test = new Client();
                test.execute("0&name&names& id_group = "+(position+1));

                dataName = test.getSp();
                String[] dopNamesArr = new String[dataName.length-1];

                for(int i = 0; i < dopNamesArr.length; i++){
                    dopNamesArr[i] = dataName[i+1];
                }

                dataName = dopNamesArr;

                nameSpin = (Spinner) findViewById(R.id.nameSpin);
                namesChange();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void namesChange(){
        adapterName = new ArrayAdapter<String>(this, simple_spinner_item, dataName);
        adapterName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nameSpin.setAdapter(adapterName);
        nameSpin.setSelection(0);
    }

    public void nextAdd(View v){
        sentData();
        weight.setText("");
    }

    public void addAndExit(View v){
        sentData();
        weight.setText("");

        startActivity(new Intent("com.example.mfam.agroandroid.StorageActivity"));
    }

    private void sentData(){
        weight = (EditText) findViewById(R.id.weightET);

        if(Objects.equals(weight.getText().toString(), "")){
            return;
        }

        Client test = new Client();
        test.execute("3&"+placeInSpin.getSelectedItem().toString()+
                "&"+nameSpin.getSelectedItem().toString()+
                "&"+weight.getText().toString()+
                "&"+placeOutSpin.getSelectedItem().toString()
        );

    }

}
