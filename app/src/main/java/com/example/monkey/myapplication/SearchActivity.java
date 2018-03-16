package com.example.monkey.myapplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.content.Intent;

import com.example.monkey.myapplication.backend.ItinerarySystem;
import com.example.monkey.myapplication.backend.User;
import com.example.monkey.myapplication.backend.UserSystem;

public class SearchActivity extends AppCompatActivity {
    private UserSystem userSystem;
    private ItinerarySystem itinerarySystem;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        userSystem = (UserSystem) intent.getSerializableExtra("userSystem");
        itinerarySystem = (ItinerarySystem) intent.getSerializableExtra("itinerarySystem");
        user = (User) intent.getSerializableExtra("user");


    }

    public void search(View view){
        EditText destinationText = (EditText) findViewById(R.id.destinationText);
        String destination = destinationText.getText().toString();

        EditText originText = (EditText) findViewById(R.id.originText);
        String origin = originText.getText().toString();

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(calendarView.getDate());

        String[] searchParams = {date,origin,destination};

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("searchParams", searchParams);
        intent.putExtra("user", user);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem",itinerarySystem);
        startActivity(intent);

    }

}
