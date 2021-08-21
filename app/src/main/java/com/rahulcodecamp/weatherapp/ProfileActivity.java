package com.rahulcodecamp.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;
import com.rahulcodecamp.weatherapp.utility.NetworkChangeListener;

public class ProfileActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener(); // to check internetConnection

    String phoneNo;
    TextView profileTextView;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar_view);
        toolbar.setTitle("User Profile");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        phoneNo = intent.getStringExtra("user'sPhoneNo");

        profileTextView =findViewById(R.id.profileTextView);

        db = new DBHelper(this);

        searchingData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void searchingData(){
        Cursor result = db.getUserData(phoneNo);

        while (result.moveToNext()){
            profileTextView.setText("Name : "+result.getString(1)+ "\n" + "Gender : "+result.getString(2)+"\n"+"Age : "
            +result.getString(3)+"\n"+"Address : "+result.getString(4)+ "\n" + "District : "+result.getString(5)+"\n" +"PinCode : "+result.getString(6));
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}