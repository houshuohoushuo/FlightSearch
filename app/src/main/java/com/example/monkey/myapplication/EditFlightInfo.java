package com.example.monkey.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monkey.myapplication.backend.FlightInfo;
import com.example.monkey.myapplication.backend.ItinerarySystem;
import com.example.monkey.myapplication.backend.User;
import com.example.monkey.myapplication.backend.UserSystem;

import java.io.File;
import java.io.IOException;

public class EditFlightInfo extends AppCompatActivity {
    private FlightInfo flightInfo;
    private UserSystem userSystem;
    private ItinerarySystem itinerarySystem;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flight_info);

        Intent intent = getIntent();
        userSystem = (UserSystem) intent.getSerializableExtra("userSystem");
        itinerarySystem = (ItinerarySystem) intent.getSerializableExtra("itinerarySystem");
        user = (User) intent.getSerializableExtra("user");
        String flightNum = (String) intent.getSerializableExtra("Flightnum");
        flightInfo = itinerarySystem.getFlightInfo(flightNum);

        String flightnum = flightInfo.getFlightnum();
        String departureDateTime = flightInfo.getDepartureDateTime();
        String arrivalDateTime = flightInfo.getArrivalDateTime();
        String airline = flightInfo.getAirline();
        String origin = flightInfo.getOrigin();
        String destination = flightInfo.getDestination();
        String cost = flightInfo.getCost();

        TextView flightNumField = (TextView) findViewById(R.id.FlightNumAirline);
        EditText departureField = (EditText) findViewById(R.id.DepartureField);
        EditText arrivalField = (EditText) findViewById(R.id.ArrivalField);
        EditText originField = (EditText) findViewById(R.id.OriginField);
        EditText destinationField = (EditText) findViewById(R.id.DestinationField);
        EditText costField = (EditText) findViewById(R.id.CostField);

        flightNumField.setText(flightnum + " " + airline);
        departureField.setText(departureDateTime);
        arrivalField.setText(arrivalDateTime);
        originField.setText(origin);
        destinationField.setText(destination);
        costField.setText(cost);
    }


    public void saveToFile(View view){
        EditText departureField = (EditText) findViewById(R.id.DepartureField);
        EditText arrivalField = (EditText) findViewById(R.id.ArrivalField);
        EditText originField = (EditText) findViewById(R.id.OriginField);
        EditText destinationField = (EditText) findViewById(R.id.DestinationField);
        EditText costField = (EditText) findViewById(R.id.CostField);

        flightInfo.setDepartureDateTime(departureField.getText().toString());
        flightInfo.setArrivalDateTime(arrivalField.getText().toString());
        flightInfo.setOrigin(originField.getText().toString());
        flightInfo.setDestination(destinationField.getText().toString());
        flightInfo.setCost(costField.getText().toString());


        File flightsFolder = this.getApplicationContext().getDir("flights", MODE_PRIVATE);
        File flightsFile = new File(flightsFolder, "flights.txt");

        try {
            itinerarySystem.saveToFile(flightsFile);
            Toast.makeText(getApplicationContext(), "Flight information saved", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, AdminActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem", itinerarySystem);
        startActivity(intent);


    }
}
