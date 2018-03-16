package com.example.monkey.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.monkey.myapplication.backend.ItinerarySystem;
import com.example.monkey.myapplication.backend.User;
import com.example.monkey.myapplication.backend.UserSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class client extends AppCompatActivity{

    private UserSystem userSystem;
    private ItinerarySystem itinerarySystem;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Intent intent = getIntent();
        userSystem = (UserSystem) intent.getSerializableExtra("userSystem");
        itinerarySystem = (ItinerarySystem) intent.getSerializableExtra("itinerarySystem");
        user = (User) intent.getSerializableExtra("user");
        TextView userEmailField = (TextView) findViewById(R.id.userEmailText);
        userEmailField.setText(user.getEmail());
    }

    public void profileclick(View view){
        Intent intent = new Intent(this, ClientProfile.class);
        intent.putExtra("user", user);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem",itinerarySystem);
        startActivity(intent);
    }


    public void startSearch(View view){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem",itinerarySystem);
        startActivity(intent);
    }

    public void viewBookings(View view){
        File flightsFolder = this.getApplicationContext().getDir("flights", MODE_PRIVATE);
        File bookingFile = new File(flightsFolder, "bookings");

        ArrayList result = new ArrayList();
        try {
            result = itinerarySystem.viewBookedFlights(bookingFile,user.getEmail());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, ViewBookingsActivity.class);
        intent.putExtra("result",result);
        startActivity(intent);
    }

    public void searchFlights(View view){
        Intent intent = new Intent(this, SearchFlightsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem",itinerarySystem);
        startActivity(intent);
    }

}

