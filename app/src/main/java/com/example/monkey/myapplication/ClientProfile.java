package com.example.monkey.myapplication;


import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monkey.myapplication.backend.ItinerarySystem;
import com.example.monkey.myapplication.backend.User;
import com.example.monkey.myapplication.backend.UserSystem;

public class ClientProfile extends AppCompatActivity{
    private UserSystem userSystem;
    private ItinerarySystem itinerarySystem;
    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        Intent intent = getIntent();
        userSystem = (UserSystem) intent.getSerializableExtra("userSystem");
        itinerarySystem = (ItinerarySystem) intent.getSerializableExtra("itinerarySystem");
        user = (User) intent.getSerializableExtra("user");


        String firstname = user.getFirstName();
        String lastname = user.getLastName();
        String email = user.getEmail();
        String cardNum = user.getCardNum();
        String expirDate = user.getExpiryDate();
        String address = user.getAddress();

        EditText firstNameField = (EditText) findViewById(R.id.Firstname);
        firstNameField.getText().toString();

        EditText lastNameField = (EditText) findViewById(R.id.lastname);
        lastNameField.getText().toString();

        TextView emailField = (TextView) findViewById(R.id.email);
        emailField.setText(email);

        EditText cardNumField = (EditText) findViewById(R.id.cardNum);
        cardNumField.getText().toString();

        EditText expField = (EditText) findViewById(R.id.expireDate);
        expField.getText().toString();

        EditText addressField = (EditText) findViewById(R.id.Address);
        addressField.getText().toString();

        firstNameField.setText(firstname);
        lastNameField.setText(lastname);
        emailField.setText(email);
        cardNumField.setText(cardNum);
        expField.setText(expirDate);
        addressField.setText(address);


    }


    public void save(View view){

        User userChanged = userSystem.getUser(user.getEmail());

        EditText firstNameField = (EditText) findViewById(R.id.Firstname);
        userChanged.setFirstName(firstNameField.getText().toString());

        EditText lastNameField = (EditText) findViewById(R.id.lastname);
        userChanged.setLastName(lastNameField.getText().toString());

        TextView emailField = (TextView) findViewById(R.id.email);
        userChanged.setEmail(emailField.getText().toString());

        EditText cardNumField = (EditText) findViewById(R.id.cardNum);
        userChanged.setCardNum(cardNumField.getText().toString());

        EditText expField = (EditText) findViewById(R.id.expireDate);
        userChanged.setExpiryDate(expField.getText().toString());

        EditText addressField = (EditText) findViewById(R.id.Address);
        userChanged.setAddress(addressField.getText().toString());

        File userDataFolder = this.getApplicationContext().getDir("userData", MODE_PRIVATE);
        File userFile = new File(userDataFolder, "clients.txt");

        try{
            userSystem.saveFile(userFile);
            Toast.makeText(getApplicationContext(),"Client's Profile Changed", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, client.class);
        intent.putExtra("user", userChanged);
        intent.putExtra("userSystem", userSystem);
        intent.putExtra("itinerarySystem", itinerarySystem);
        startActivity(intent);

    }





}
