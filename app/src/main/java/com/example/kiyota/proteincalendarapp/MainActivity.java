package com.example.kiyota.proteincalendarapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.kiyota.proteincalendarapp.activity.TotalizationActivity;
import com.example.kiyota.proteincalendarapp.dto.ProteinEntity;
import com.example.kiyota.proteincalenderapp.R;
import com.example.kiyota.proteincalendarapp.adapter.CalendarGridAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CalendarGridAdapter cg;
    Context mContext;
    Calendar mCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mCalendar = Calendar.getInstance();

        List<ProteinEntity> list = new ArrayList<ProteinEntity>();

        GridView grid = findViewById(R.id.calendar_grid);

        cg = new CalendarGridAdapter(this,mCalendar,list);

        grid.setAdapter(cg);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout linearLayout = findViewById(R.id.appear_select_protein);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        Button button = findViewById(R.id.totalization_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TotalizationActivity.class);
                startActivity(intent);
            }
        });

        ImageView lastMonth = findViewById(R.id.calender_last_month);
        lastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
