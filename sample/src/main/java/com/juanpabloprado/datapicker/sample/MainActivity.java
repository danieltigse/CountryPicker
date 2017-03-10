package com.juanpabloprado.datapicker.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.juanpabloprado.datapicker.Data;
import com.juanpabloprado.datapicker.DataPicker;
import com.juanpabloprado.datapicker.DataPickerListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

    public void onSelect(View view){
        final DataPicker picker = DataPicker.newInstance("Select Country");

        List<Data> dataList = new ArrayList<>();
        dataList.add(new Data("1", "Guayaquil"));
        dataList.add(new Data("2", "Quito"));

        picker.setDataList(dataList);
        picker.setListener(new DataPickerListener() {
            @Override
            public void onSelectCountry(String name, String code) {
                ((Button)findViewById(R.id.btnSelect)).setText(name);
                picker.dismiss();
            }
        });

        picker.show(getSupportFragmentManager(), "CountryPicker");
    }
}
