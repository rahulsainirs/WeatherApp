package com.rahulcodecamp.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    String phoneNo;

    TextView profileTextView;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Toolbar toolbar = findViewById(R.id.toolbar_1);
        toolbar.setTitle("User Profile");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        phoneNo = intent.getStringExtra("user'sPhoneNo");

        profileTextView =findViewById(R.id.profileTextView);

        db = new DBHelper(this);

        searchingData();
    }

    public void searchingData(){
        Cursor result = db.getUserData(phoneNo);

        while (result.moveToNext()){
            profileTextView.setText("Name : "+result.getString(1)+ "\n" + "Gender : "+result.getString(2)+"\n"+"Age : "
            +result.getString(3)+"\n"+"Address : "+result.getString(4)+ "\n" + "District : "+result.getString(5)+"\n" +"PinCode : "+result.getString(6));
        }
    }
}