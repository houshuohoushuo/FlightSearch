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

public class ResultActivity extends AppCompatActivity {
    private String[] resultByCost = new String[0];
    private String[] resultByTime = new String[0];
    private UserSystem userSystem;
    private ItinerarySystem itinerarySystem;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ListView listView = (ListView) findViewById(R.id.listView);
        Intent intent;
        intent = getIntent();
        String[] searchParams = (String[]) intent.getSerializableExtra("searchParams");
        userSystem = (UserSystem) intent.getSerializableExtra("userSystem");
        itinerarySystem = (ItinerarySystem) intent.getSerializableExtra("itinerarySystem");
        user = (User) intent.getSerializableExtra("user");

        try {
            resultByCost = itinerarySystem.getItinerariesSortedByCost(searchParams[0], searchParams[1], searchParams[2]);
            resultByTime = itinerarySystem.getItinerariesSortedByTime(searchParams[0], searchParams[1], searchParams[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resultByCost));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book(resultByCost[position]);
            }
        });

    }
    private void book(String itinerary){
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra("itinerary", itinerary);
        intent.putExtra("user", user);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem",itinerarySystem);
        startActivity(intent);
    }

    public void sortByCost(View view){
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resultByCost));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book(resultByCost[position]);
            }
        });
    }

    public void sortByDuration(View view){
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resultByTime));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book(resultByCost[position]);
            }
        });    }

}
