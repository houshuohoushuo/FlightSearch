package com.example.monkey.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.monkey.myapplication.backend.ItinerarySystem;
import com.example.monkey.myapplication.backend.User;
import com.example.monkey.myapplication.backend.UserSystem;

import java.text.ParseException;

public class SearchFlightsResultActivity extends AppCompatActivity {

    private UserSystem userSystem;
    private ItinerarySystem itinerarySystem;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights_result);
        ListView listView = (ListView) findViewById(R.id.listView);
        Intent intent;
        intent = getIntent();
        String[] searchParams = (String[]) intent.getSerializableExtra("searchParams");
        userSystem = (UserSystem) intent.getSerializableExtra("userSystem");
        itinerarySystem = (ItinerarySystem) intent.getSerializableExtra("itinerarySystem");
        user = (User) intent.getSerializableExtra("user");

        String[] result = new String[0];
        try {
            result = itinerarySystem.getFlights(searchParams[0], searchParams[1], searchParams[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, result));

    }}
