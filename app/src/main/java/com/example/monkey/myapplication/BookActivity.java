package com.example.monkey.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monkey.myapplication.backend.ItinerarySystem;
import com.example.monkey.myapplication.backend.User;
import com.example.monkey.myapplication.backend.UserSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    private UserSystem userSystem;
    private ItinerarySystem itinerarySystem;
    private User user;
    private String[] itineraryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Intent intent = getIntent();
        userSystem = (UserSystem) intent.getSerializableExtra("userSystem");
        itinerarySystem = (ItinerarySystem) intent.getSerializableExtra("itinerarySystem");
        user = (User) intent.getSerializableExtra("user");
        itineraryString = ((String) intent.getSerializableExtra("itinerary")).split("\n");
        String itineraryText = "";
        int i = 0;
        while (i<itineraryString.length-2){
            itineraryText += itineraryString[i++] + "\n";
        }
        String costText = itineraryString[itineraryString.length-2];
        String durationText = itineraryString[itineraryString.length-1];

        TextView itineraryTextField = (TextView) findViewById(R.id.itineraryText);
        TextView costTextField = (TextView) findViewById(R.id.costText);
        TextView durationField = (TextView) findViewById(R.id.durationText);

        itineraryTextField.setText(itineraryText);
        costTextField.setText(costText);
        durationField.setText(durationText);

    }

    public void book(View view){
        ArrayList<String> flightNums= new ArrayList<String>();
        int i = 0;
        while (i<itineraryString.length-2){
            flightNums.add(itineraryString[i++].split(",")[0]);
        }


        File flightsFolder = this.getApplicationContext().getDir("flights", MODE_PRIVATE);
        File flightsFile = new File(flightsFolder, "flights.txt");
        File bookingFile = new File(flightsFolder, "bookings");

        try {
            itinerarySystem.book(flightNums,user.getEmail(),bookingFile,flightsFile);
            Toast.makeText(getApplicationContext(), "Itinerary has been booked", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
