package com.rahulcodecamp.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rahulcodecamp.weatherapp.utility.NetworkChangeListener;

public class MainActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener(); // to check internetConnection

    TextInputEditText phoneTextInputEditText;
    String phoneNo;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneTextInputEditText = findViewById(R.id.phoneTextInputEditText);

        db = new DBHelper(this); // SQLITE DB

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // this will remove the statusBar

        Toolbar toolbar = findViewById(R.id.toolbar_view);
        toolbar.setTitle("WeatherApp");
        setSupportActionBar(toolbar);

    }

    public void goToRegBtnClicked(View v){

        phoneNo = phoneTextInputEditText.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNo) || phoneNo.length() <=9){
            phoneTextInputEditText.setError("Please enter phone number");
            Toast.makeText(this, "Please enter phone number first", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            intent.putExtra("user'sPhoneNo", phoneNo);
            startActivity(intent);
        }
    }

    public void loginBtnClicked(View v){

        phoneNo = phoneTextInputEditText.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNo) || phoneNo.length() <=9){
            phoneTextInputEditText.setError("Please enter phone number");
        }
        else {
            Boolean checkPhoneNo = db.checkPhoneNo(phoneNo);
            if (checkPhoneNo == true){
                Toast.makeText(this, "signedIn successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, TodayWeatherActivity.class);
                intent.putExtra("user'sPhoneNo", phoneNo);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Account not exist/Incorrect phone number", Toast.LENGTH_SHORT).show();
            }
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