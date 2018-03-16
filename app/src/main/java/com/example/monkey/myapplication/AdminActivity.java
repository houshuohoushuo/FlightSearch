package com.example.monkey.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.example.monkey.myapplication.backend.ItinerarySystem;
import com.example.monkey.myapplication.backend.User;
import com.example.monkey.myapplication.backend.UserSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class AdminActivity extends AppCompatActivity {
    private UserSystem userSystem;
    private ItinerarySystem itinerarySystem;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Intent intent = getIntent();
        userSystem = (UserSystem) intent.getSerializableExtra("userSystem");
        itinerarySystem = (ItinerarySystem) intent.getSerializableExtra("itinerarySystem");
        user = (User) intent.getSerializableExtra("user");

    }
    //Get the FlightNumber from the EditTextFile and constructs an intent and packs
    //the FlightNumber into it.Pass the intent to SearchActivity.
    public void editFlightInfo(View view){
        EditText FlightNumberlText = (EditText) findViewById(R.id.flightnumber);
        String Flightnum = FlightNumberlText.getText().toString();
        Intent intent = new Intent(this,EditFlightInfo.class);
        intent.putExtra("Flightnum", Flightnum);
        intent.putExtra("user", user);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem",itinerarySystem);
        startActivity(intent);
    }

    public void signInAsUser(View view){
        EditText userEmailField = (EditText) findViewById(R.id.useremail);
        String userEmail = userEmailField.getText().toString();
        user = userSystem.getUser(userEmail);

        Intent intent = new Intent(this,client.class);
        intent.putExtra("user", user);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem",itinerarySystem);
        startActivity(intent);

    }

    public void uploadflight(View view){

        File uploadFolder = this.getApplicationContext().getDir("upload_file",MODE_PRIVATE);
        File uploadFlightInfo = new File(uploadFolder,"flight.txt");

        File flightsFolder = this.getApplicationContext().getDir("flights", MODE_PRIVATE);
        File flightsFile = new File(flightsFolder, "flights.txt");

        try {
            itinerarySystem.uploadFlightInfo(uploadFlightInfo);
            itinerarySystem.saveToFile(flightsFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Flight info uploaded", Toast.LENGTH_LONG).show();
    }

    public void uploadclient(View view){

        File uploadFolder = this.getApplicationContext().getDir("upload_file",MODE_PRIVATE);
        File uploadUserInfo = new File(uploadFolder,"clients.txt");
        File uploadPassword = new File(uploadFolder,"password.txt");

        File userDataFolder = this.getApplicationContext().getDir("userData", MODE_PRIVATE);
        File passwordFile = new File(userDataFolder, "password.txt");
        File userFile = new File(userDataFolder,"clients.txt");

        try {
            userSystem.uploadClientInfo(uploadUserInfo);
            userSystem.uploadAccount(uploadPassword);
            userSystem.saveFile(userFile);
            userSystem.savePasswordFile(passwordFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Client info uploaded", Toast.LENGTH_LONG).show();
    }

    public void searchFlights(View view){
        Intent intent = new Intent(this, SearchFlightsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem",itinerarySystem);
        startActivity(intent);
    }

}
