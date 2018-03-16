package com.example.monkey.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.monkey.myapplication.backend.FlightInfo;

import java.util.ArrayList;

public class ViewBookingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        Intent intent = getIntent();
        ArrayList<FlightInfo> arrayList = (ArrayList) intent.getSerializableExtra("result");

        String display = "";
        for(FlightInfo flightInfo: arrayList){
            display+="\n"+flightInfo.toString();
        }

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(display);

    }
}
