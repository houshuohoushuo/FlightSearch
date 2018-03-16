package com.example.monkey.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.monkey.myapplication.backend.ItinerarySystem;
import com.example.monkey.myapplication.backend.User;
import com.example.monkey.myapplication.backend.UserSystem;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private UserSystem userSystem;
    private ItinerarySystem itinerarySystem;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File userDataFolder = this.getApplicationContext().getDir("userData", MODE_PRIVATE);
        File passwordFile = new File(userDataFolder, "password.txt");
        File userFile = new File(userDataFolder,"clients.txt");

        File flightsFolder = this.getApplicationContext().getDir("flights", MODE_PRIVATE);
        File flightsFile = new File(flightsFolder, "flights.txt");

        userSystem = new UserSystem();
        itinerarySystem = new ItinerarySystem();

        try {
            itinerarySystem.uploadFlightInfo(flightsFile);
            userSystem.uploadAccount(passwordFile);
            userSystem.uploadClientInfo(userFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void clientLogin(View view){

        EditText accountField = (EditText) findViewById(R.id.loginAccount);
        String account = accountField.getText().toString();


        EditText clientPWField = (EditText) findViewById(R.id.password);
        String clientPW = clientPWField.getText().toString();
        if (userSystem.checkAccount(account, clientPW)) {
            if (userSystem.getUserType(account).equals("Client")) {
                user = userSystem.getUser(account);
                Intent intent = new Intent(this,client.class);
                intent.putExtra("user", user);
                intent.putExtra("userSystem", userSystem);
                intent.putExtra("itinerarySystem", itinerarySystem);
                startActivity(intent);
            }
            else if (userSystem.getUserType(account).equals("Admin")) {
                Intent intent = new Intent(this, AdminActivity.class);
                user = userSystem.getUser(account);
                intent.putExtra("user", user);
                intent.putExtra("userSystem", userSystem);
                intent.putExtra("itinerarySystem",itinerarySystem);
                startActivity(intent);
            }

        }
        else {
            TextView textElement = (TextView) findViewById(R.id.clientPrompt);
            textElement.setText(R.string.accountPrompt);
        }
    }

}