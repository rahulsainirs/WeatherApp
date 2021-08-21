package com.rahulcodecamp.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

    }

    public void goToRegBtnClicked(View v){

        phoneNo = phoneTextInputEditText.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNo) || phoneNo.length() <=9){
            phoneTextInputEditText.setError("Please enter 10 digits phone number");
        }
        else {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            intent.putExtra("user'sPhoneNo", phoneNo);
            startActivity(intent);
        }
    }

    public void loginBtnClicked(View v){
        phoneNo = phoneTextInputEditText.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNo) || phoneNo.length() <10){
            phoneTextInputEditText.setError("Please enter 10 digits phone number");
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