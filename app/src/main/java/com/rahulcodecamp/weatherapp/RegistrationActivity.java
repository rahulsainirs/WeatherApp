package com.rahulcodecamp.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {
    TextInputLayout dobTextInputLayout, pinCodeTextInputLayout;
    TextInputEditText dobTextInputEditText, pinCodeTextInputEditText, phoneTextInputEditText,
            nameTextInputEditText,addressLine1TextInputEditText, addressLine2TextInputEditText;

    DBHelper db;

    Button checkButton, registerButton;

    TextView stateTextView, districtTextView;

    AutoCompleteTextView genderAutoCompleteTextView;

    String district;
    int age;
    String userPhoneNo;

    public void checkClicked(View view){

        if (pinCodeTextInputEditText.getText().length() <6){
            pinCodeTextInputEditText.setError("Please enter valid digits");
        }else if (pinCodeTextInputEditText.getText().equals("")){
            pinCodeTextInputEditText.setError("Please enter valid digits");
        }

        DownloadTask task = new DownloadTask();

        String PINCODE = pinCodeTextInputEditText.getText().toString();
        task.execute("https://api.postalpincode.in/pincode/"+PINCODE);

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1){
                    char current = (char)data;
                    result += current;
                    data = reader.read();

                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONArray arr = new JSONArray(s);
                String message ="";

                for (int i=0; i<arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);

                    if (!jsonPart.getString("Status").equals("Success")){
                        message += jsonPart.getString("Status")+ " : please enter correct PinCode";
                        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
                        break;
                    }

                    JSONArray jsonChild = jsonPart.getJSONArray("PostOffice");

                    for (int j = 0; j<jsonChild.length(); j++){
                        JSONObject obj1 = jsonChild.getJSONObject(j);
                        district = obj1.getString("District");
                        String state = obj1.getString("State");

                        if (!district.equals("") && !state.equals("")){
                            districtTextView.setText("District : "+district);
                            stateTextView.setText("State : "+state);
                            break;
                        }
                    }


                }
//                if (!message.equals("")){
//                    resultTextView.setText(message);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Intent intent = getIntent();
        userPhoneNo = intent.getStringExtra("user'sPhoneNo");


        Toolbar toolbar = findViewById(R.id.toolbar_1);
        toolbar.setTitle("Registration");
        setSupportActionBar(toolbar);

        checkButton = findViewById(R.id.checkButton);
        registerButton = findViewById(R.id.registerButton);

        genderAutoCompleteTextView = findViewById(R.id.genderAutoCompleteTextView);
        phoneTextInputEditText = findViewById(R.id.phoneTextInputEditText);
        phoneTextInputEditText.setText(userPhoneNo);
        addressLine1TextInputEditText = findViewById(R.id.addressLine1TextInputEditText);
        addressLine2TextInputEditText = findViewById(R.id.addressLine2TextInputEditText);
        nameTextInputEditText = findViewById(R.id.nameTextInputEditText);

        pinCodeTextInputLayout = findViewById(R.id.pinCodeTextInputLayout);
        pinCodeTextInputEditText = findViewById(R.id.pinCodeTextInputEditText);
        stateTextView = findViewById(R.id.stateTextView);
        districtTextView = findViewById(R.id.districtTextView);

        dobTextInputLayout = findViewById(R.id.dobTextInputLayout);
        dobTextInputEditText = findViewById(R.id.dobTextInputEditText);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int days = calendar.get(Calendar.DAY_OF_MONTH);



        String[] languages = getResources().getStringArray(R.array.Gender);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.dropdown_item,languages);
        genderAutoCompleteTextView.setAdapter(arrayAdapter);

        dobTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String dob = dayOfMonth+"/"+month+"/"+year;
                        age = calendar.get(Calendar.YEAR) - year;
                        Toast.makeText(RegistrationActivity.this, "age: "+age, Toast.LENGTH_SHORT).show();
                        dobTextInputEditText.setText(dob);

                    }
                }, year,month,days);
                datePickerDialog.show();
            }
        });

        pinCodeTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pinCodeTextInputEditText.getText().toString().length() >5){
                    checkButton.setEnabled(true);
                }
                else {
                    checkButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        db = new DBHelper(this); //SQLITE DB

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String phoneNo = phoneTextInputEditText.getText().toString().trim();
                    String name = nameTextInputEditText.getText().toString().trim();
                    String gender = genderAutoCompleteTextView.getText().toString().trim();
                    String dob = dobTextInputEditText.getText().toString().trim();
                    String addressLine1 = addressLine1TextInputEditText.getText().toString().trim();
                    String addressLine2 = addressLine2TextInputEditText.getText().toString().trim();
                    String address = addressLine1 + ",\n" + addressLine2; // complete address;
                    String pinCode = pinCodeTextInputEditText.getText().toString().trim();

                    if (TextUtils.isEmpty(phoneNo) || phoneNo.length() <=9) {

                        phoneTextInputEditText.setError("please enter phone number");
                    }
                    else if (TextUtils.isEmpty(name)) {

                        nameTextInputEditText.setError("please enter Your full name");
                    }else if (TextUtils.isEmpty(gender)) {

                        genderAutoCompleteTextView.setError("please select the gender");
                    } else if (TextUtils.isEmpty(dob)) {

                        Toast.makeText(RegistrationActivity.this, "please fill date of birth", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(addressLine1) || addressLine1.length() <=3) {

                        Toast.makeText(RegistrationActivity.this, "Address line1 must be above 3 characters", Toast.LENGTH_SHORT).show();
                        addressLine1TextInputEditText.setError("please fill Address line 1");
                    } else if (TextUtils.isEmpty(addressLine2)) {

                        addressLine2TextInputEditText.setError("please fill Address line 2");
                    } else if (TextUtils.isEmpty(pinCode) || pinCode.length() <=5) {

                        pinCodeTextInputEditText.setError("please enter Your pincode");
                    }
                    else if(district.length() <=1){
                        pinCodeTextInputEditText.setError("Please click on check button");
                        Toast.makeText(RegistrationActivity.this, "Please click on check button after filling pinCode", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(RegistrationActivity.this, TodayWeather.class));
                        Boolean checkuser = db.checkPhoneNo(phoneNo);
                        if (!checkuser){
                            Boolean insert = db.insertData(phoneNo, name, gender, age, address, district, pinCode);

                            if (insert == true){
                                Toast.makeText(RegistrationActivity.this, "Registered successfully...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this, TodayWeather.class);
                                intent.putExtra("user'sPhoneNo", phoneNo);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(RegistrationActivity.this, "Registration failed...", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(RegistrationActivity.this, "This phone number is already registered! please try another one", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(RegistrationActivity.this, "Please first click on check button to fetch the data"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}