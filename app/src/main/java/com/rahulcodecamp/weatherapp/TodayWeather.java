package com.rahulcodecamp.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;

public class TodayWeather extends AppCompatActivity {

    String userPhoneNo;

    AutoCompleteTextView cityEditText; //1. this is used to use autocomplete feature. it behaves as edit text
    TextView weatherInfoTextView, mainTextView;
    ImageView weatherWidgetImageView;

    DecimalFormat df =new DecimalFormat("#.##");

    public void getWeather(View view){
        DownloadTask task = new DownloadTask();

        String city = cityEditText.getText().toString().toLowerCase().trim();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city+"&lang=en&appid=146952e5d828e5e5cf57ce6c586a9ff2");

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
                JSONObject jsonObject = new JSONObject(s);
                String latitude = jsonObject.getJSONObject("coord").getString("lat"); // to fetch out the object of jsonObject
                String longitude = jsonObject.getJSONObject("coord").getString("lon");
                double temp = jsonObject.getJSONObject("main").getDouble("temp");
                double celsiusTemp =  temp - 273.15;
                Double fahrenheitTemp =  (1.8*celsiusTemp)+32;

                String message = "Temperature in Centigrade : "+df.format(celsiusTemp)+"°C\nTemperature in Fahrenheit : "+df.format(fahrenheitTemp)+"°F\n\nLatitude : "+latitude+"\nLongitude : "+longitude;

                String weatherCondition = jsonObject.getString("weather");


                JSONArray arr = new JSONArray(weatherCondition); // to fetch array of object

                for (int i=0; i<arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main = jsonPart.getString("main"); //main == weather condition
                    int id = jsonPart.getInt("id");

                    String wIcon =updateWeatherIcon(id);
                    int resourceId = getResources().getIdentifier(wIcon, "drawable", getPackageName());
                    weatherWidgetImageView.setImageResource(resourceId);

                    mainTextView.setText(main);

                    if (main.equals("")){
                        Toast.makeText(TodayWeather.this, "city isn't found", Toast.LENGTH_SHORT).show();;
                    }
                }
                if (!message.equals("")){
                    weatherInfoTextView.setText(message);
                }
                else {
                    Toast.makeText(TodayWeather.this, "city isn't found", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(TodayWeather.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }
    private static String updateWeatherIcon(int condition){
        if (condition>=0 && condition<=300){
            return "thunderstorm";
        }else if (condition>=301 && condition<=500){
            return "light_rain";
        }else if (condition>=501 && condition<=600){
            return "rainy";
        }else if (condition>=601 && condition<=700){
            return "snow";
        }else if (condition>=701 && condition<=771){
            return "fog";
        }else if (condition>=772 && condition<800){
            return "overcast";
        }else if (condition==800){
            return "sun";
        }else if (condition>=801 && condition<=804){
            return "cloudy";
        }else if (condition>=900 && condition<=902){
            return "thunderstorm";
        }else if (condition==903){
            return "snow";
        }else if (condition==904){
            return "sun";
        }else if (condition>=905 && condition<=1000){
            return "thunderstorm1";
        }
        return "haze";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_weather);

        Intent intent = getIntent();
        userPhoneNo = intent.getStringExtra("user'sPhoneNo");

        Toolbar toolbar = findViewById(R.id.toolbar_1); // custom toolbar
        setSupportActionBar(toolbar);

        mainTextView = findViewById(R.id.mainTextView);

        weatherWidgetImageView = findViewById(R.id.weatherWidgetImageView);

        weatherInfoTextView = findViewById(R.id.weatherInfoTextView);
        cityEditText = findViewById(R.id.cityEditText);

        String [] cities = {"Andhra Pradesh",
                "Adilabad",
                "Anantapur",
                "Chittoor",
                "Kakinada",
                "Guntur",
                "Hyderabad",
                "Karimnagar",
                "Khammam",
                "Krishna",
                "Kurnool",
                "Mahbubnagar",
                "Medak",
                "Nalgonda",
                "Nizamabad",
                "Ongole",
                "Hyderabad",
                "Srikakulam",
                "Nellore",
                "Visakhapatnam",
                "Vizianagaram",
                "Warangal",
                "Eluru",
                "Kadapa",

                "Arunachal Pradesh",
                "Anjaw",
                "Changlang",
                "East Siang",
                "Kurung Kumey",
                "Lohit",
                "Lower Dibang Valley",
                "Lower Subansiri",
                "Papum Pare",
                "Tawang",
                "Tirap",
                "Dibang Valley",
                "Upper Siang",
                "Upper Subansiri",
                "West Kameng",
                "West Siang",

                "Assam",

                "Baksa",
                "Barpeta",
                "Bongaigaon",
                "Cachar",
                "Chirang",
                "Darrang",
                "Dhemaji",
                "Dima Hasao",
                "Dhubri",
                "Dibrugarh",
                "Goalpara",
                "Golaghat",
                "Hailakandi",
                "Jorhat",
                "Kamrup",
                "Kamrup Metropolitan",
                "Karbi Anglong",
                "Karimganj",
                "Kokrajhar",
                "Lakhimpur",
                "Marigaon",
                "Nagaon",
                "Nalbari",
                "Sibsagar",
                "Sonitpur",
                "Tinsukia",
                "Udalguri",

                "Bihar",

                "Araria",
                "Arwal",
                "Aurangabad",
                "Banka",
                "Begusarai",
                "Bhagalpur",
                "Bhojpur",
                "Buxar",
                "Darbhanga",
                "East Champaran",
                "Gaya",
                "Gopalganj",
                "Jamui",
                "Jehanabad",
                "Kaimur",
                "Katihar",
                "Khagaria",
                "Kishanganj",
                "Lakhisarai",
                "Madhepura",
                "Madhubani",
                "Munger",
                "Muzaffarpur",
                "Nalanda",
                "Nawada",
                "Patna",
                "Purnia",
                "Rohtas",
                "Saharsa",
                "Samastipur",
                "Saran",
                "Sheikhpura",
                "Sheohar",
                "Sitamarhi",
                "Siwan",
                "Supaul",
                "Vaishali",
                "West Champaran",
                "Chandigarh",

                "Chhattisgarh",
                "Bastar",
                "Bijapur",
                "Bilaspur",
                "Dantewada",
                "Dhamtari",
                "Durg",
                "Jashpur",
                "Janjgir-Champa",
                "Korba",
                "Koriya",
                "Kanker",
                "Kabirdham (Kawardha)",
                "Mahasamund",
                "Narayanpur",
                "Raigarh",
                "Rajnandgaon",
                "Raipur",
                "Surguja",
                "Dadra and Nagar Haveli",
                "Dadra and Nagar Haveli",
                "Daman and Diu",
                "Daman",
                "Diu",
                "Delhi",
                "Central Delhi",
                "East Delhi",
                "New Delhi",
                "North Delhi",
                "North East Delhi",
                "North West Delhi",
                "South Delhi",
                "South West Delhi",
                "West Delhi",
                "Goa",
                "North Goa",
                "South Goa",
                "Gujarat",
                "Ahmedabad",
                "Amreli district",
                "Anand",
                "Banaskantha",
                "Bharuch",
                "Bhavnagar",
                "Dahod",
                "The Dangs",
                "Gandhinagar",
                "Jamnagar",
                "Junagadh",
                "Kutch",
                "Kheda",
                "Mehsana",
                "Narmada",
                "Navsari",
                "Patan",
                "Panchmahal",
                "Porbandar",
                "Rajkot",
                "Sabarkantha",
                "Surendranagar",
                "Surat",
                "Vyara",
                "Vadodara",
                "Valsad",
                "Haryana",
                "Ambala",
                "Bhiwani",
                "Faridabad",
                "Fatehabad",
                "Gurgaon",
                "Hissar",
                "Jhajjar",
                "Jind",
                "Karnal",
                "Kaithal",
                "Kurukshetra",
                "Mahendragarh",
                "Mewat",
                "Palwal",
                "Panchkula",
                "Panipat",
                "Rewari",
                "Rohtak",
                "Sirsa",
                "Sonipat",
                "Yamuna Nagar",
                "Himachal Pradesh",
                "Bilaspur",
                "Chamba",
                "Hamirpur",
                "Kangra",
                "Kinnaur",
                "Kullu",
                "Lahaul and Spiti",
                "Mandi",
                "Shimla",
                "Sirmaur",
                "Solan",
                "Una",
                "Jammu and Kashmir",
                "Anantnag",
                "Badgam",
                "Bandipora",
                "Baramulla",
                "Doda",
                "Ganderbal",
                "Jammu",
                "Kargil",
                "Kathua",
                "Kishtwar",
                "Kupwara",
                "Kulgam",
                "Leh",
                "Poonch",
                "Pulwama",
                "Rajauri",
                "Ramban",
                "Reasi",
                "Samba",
                "Shopian",
                "Srinagar",
                "Udhampur",
                "Jharkhand",
                "Bokaro",
                "Chatra",
                "Deoghar",
                "Dhanbad",
                "Dumka",
                "East Singhbhum",
                "Garhwa",
                "Giridih",
                "Godda",
                "Gumla",
                "Hazaribag",
                "Jamtara",
                "Khunti",
                "Koderma",
                "Latehar",
                "Lohardaga",
                "Pakur",
                "Palamu",
                "Ramgarh",
                "Ranchi",
                "Sahibganj",
                "Seraikela Kharsawan",
                "Simdega",
                "West Singhbhum",
                "Karnataka",
                "Bagalkot",
                "Bangalore Rural",
                "Bangalore Urban",
                "Belgaum",
                "Bellary",
                "Bidar",
                "Bijapur",
                "Chamarajnagar",
                "Chikkamagaluru",
                "Chikkaballapur",
                "Chitradurga",
                "Davanagere",
                "Dharwad",
                "Dakshina Kannada",
                "Gadag",
                "Gulbarga",
                "Hassan",
                "Haveri district",
                "Kodagu",
                "Kolar",
                "Koppal",
                "Mandya",
                "Mysore",
                "Raichur",
                "Shimoga",
                "Tumkur",
                "Udupi",
                "Uttara Kannada",
                "Ramanagara",
                "Yadgir",
                "Kerala",

                "Alappuzha",
                "Ernakulam",
                "Idukki",
                "Kannur",
                "Kasaragod",
                "Kollam",
                "Kottayam",
                "Kozhikode",
                "Malappuram",
                "Palakkad",
                "Pathanamthitta",
                "Thrissur",
                "Thiruvananthapuram",
                "Wayanad",
                "Madhya Pradesh",
                "Alirajpur",
                "Anuppur",
                "Ashok Nagar",
                "Balaghat",
                "Barwani",
                "Betul",
                "Bhind",
                "Bhopal",
                "Burhanpur",
                "Chhatarpur",
                "Chhindwara",
                "Damoh",
                "Datia",
                "Dewas",
                "Dhar",
                "Dindori",
                "Guna",
                "Gwalior",
                "Harda",
                "Hoshangabad",
                "Indore",
                "Jabalpur",
                "Jhabua",
                "Katni",
                "Khandwa (East Nimar)",
                "Khargone (West Nimar)",
                "Mandla",
                "Mandsaur",
                "Morena",
                "Narsinghpur",
                "Neemuch",
                "Panna",
                "Rewa",
                "Rajgarh",
                "Ratlam",
                "Raisen",
                "Sagar",
                "Satna",
                "Sehore",
                "Seoni",
                "Shahdol",
                "Shajapur",
                "Sheopur",
                "Shivpuri",
                "Sidhi",
                "Singrauli",
                "Tikamgarh",
                "Ujjain",
                "Umaria",
                "Vidisha",
                "Maharashtra",
                "Ahmednagar",
                "Akola",
                "Amravati",
                "Aurangabad",
                "Bhandara",
                "Beed",
                "Buldhana",
                "Chandrapur",
                "Dhule",
                "Gadchiroli",
                "Gondia",
                "Hingoli",
                "Jalgaon",
                "Jalna",
                "Kolhapur",
                "Latur",
                "Mumbai City",
                "Mumbai suburban",
                "Nandurbar",
                "Nanded",
                "Nagpur",
                "Nashik",
                "Osmanabad",
                "Parbhani",
                "Pune",
                "Raigad",
                "Ratnagiri",
                "Sindhudurg",
                "Sangli",
                "Solapur",
                "Satara",
                "Thane",
                "Wardha",
                "Washim",
                "Yavatmal",
                "Manipur (MN)",
                "Bishnupur",
                "Churachandpur",
                "Chandel",
                "Imphal East",
                "Senapati",
                "Tamenglong",
                "Thoubal",
                "Ukhrul",
                "Imphal West",
                "Meghalaya",
                "East Garo Hills",
                "East Khasi Hills",
                "Jaintia Hills",
                "Ri Bhoi",
                "South Garo Hills",
                "West Garo Hills",
                "West Khasi Hills",
                "Mizoram",
                "Aizawl",
                "Champhai",
                "Kolasib",
                "Lawngtlai",
                "Lunglei",
                "Mamit",
                "Saiha",
                "Serchhip",
                "Nagaland",
                "Dimapur",
                "Kohima",
                "Mokokchung",
                "Mon",
                "Phek",
                "Tuensang",
                "Wokha",
                "Zunheboto",
                "Orissa",
                "Angul",
                "Boudh (Bauda)",
                "Bhadrak",
                "Balangir",
                "Bargarh (Baragarh)",
                "Balasore",
                "Cuttack",
                "Debagarh (Deogarh)",
                "Dhenkanal",
                "Ganjam",
                "Gajapati",
                "Jharsuguda",
                "Jajpur",
                "Jagatsinghpur",
                "Khordha",
                "Kendujhar (Keonjhar)",
                "Kalahandi",
                "Kandhamal",
                "Koraput",
                "Kendrapara",
                "Malkangiri",
                "Mayurbhanj",
                "Nabarangpur",
                "Nuapada",
                "Nayagarh",
                "Puri",
                "Rayagada",
                "Sambalpur",
                "Subarnapur (Sonepur)",
                "Sundergarh",
                "Pondicherry",
                "Karaikal",
                "Mahe",
                "Pondicherry",
                "Yanam",
                "Punjab (PB)",
                "Amritsar",
                "Barnala",
                "Bathinda",
                "Firozpur",
                "Faridkot",
                "Fatehgarh Sahib",
                "Fazilka",
                "Gurdaspur",
                "Hoshiarpur",
                "Jalandhar",
                "Kapurthala",
                "Ludhiana",
                "Mansa",
                "Moga",
                "Sri Muktsar Sahib",
                "Pathankot",
                "Patiala",
                "Rupnagar",
                "Ajitgarh (Mohali)",
                "Sangrur",
                "Nawanshahr",
                "Tarn Taran",
                "Rajasthan",
                "Ajmer",
                "Alwar",
                "Bikaner",
                "Barmer",
                "Banswara",
                "Bharatpur",
                "Baran",
                "Bundi",
                "Bhilwara",
                "Churu",
                "Chittorgarh",
                "Dausa",
                "Dholpur",
                "Dungapur",
                "Ganganagar",
                "Hanumangarh",
                "Jhunjhunu",
                "Jalore",
                "Jodhpur",
                "Jaipur",
                "Jaisalmer",
                "Jhalawar",
                "Karauli",
                "Kota",
                "Nagaur",
                "Pali",
                "Pratapgarh",
                "Rajsamand",
                "Sikar",
                "Sawai Madhopur",
                "Sirohi",
                "Tonk",
                "Udaipur",
                "Sikkim",
                "East Sikkim",
                "North Sikkim",
                "South Sikkim",
                "West Sikkim",
                "Tamil Nadu",
                "Ariyalur",
                "Chennai",
                "Coimbatore",
                "Cuddalore",
                "Dharmapuri",
                "Dindigul",
                "Erode",
                "Kanchipuram",
                "Kanyakumari",
                "Karur",
                "Madurai",
                "Nagapattinam",
                "Nilgiris",
                "Namakkal",
                "Perambalur",
                "Pudukkottai",
                "Ramanathapuram",
                "Salem",
                "Sivaganga",
                "Tirupur",
                "Tiruchirappalli",
                "Theni",
                "Tirunelveli",
                "Thanjavur",
                "Thoothukudi",
                "Tiruvallur",
                "Tiruvarur",
                "Tiruvannamalai",
                "Vellore",
                "Viluppuram",
                "Virudhunagar",
                "Tripura",
                "Dhalai",
                "North Tripura",
                "South Tripura",
                "Khowai",
                "West Tripura",

                "Uttar Pradesh",

                "Agra",
                "Allahabad",
                "Aligarh",
                "Ambedkar Nagar",
                "Auraiya",
                "Azamgarh",
                "Barabanki",
                "Budaun",
                "Bagpat",
                "Bahraich",
                "Bijnor",
                "Ballia",
                "Banda",
                "Balrampur",
                "Bareilly",
                "Basti",
                "Bulandshahr",
                "Chandauli",
                "Chhatrapati Shahuji Maharaj Nagar",
                "Chitrakoot",
                "Deoria",
                "Etah",
                "Kanshi Ram Nagar",
                "Etawah",
                "Firozabad",
                "Farrukhabad",
                "Fatehpur",
                "Faizabad",
                "Gautam Buddh Nagar",
                "Gonda",
                "Ghazipur",
                "Gorakhpur",
                "Ghaziabad",
                "Hamirpur",
                "Hardoi",
                "Mahamaya Nagar",
                "Jhansi",
                "Jalaun",
                "Jyotiba Phule Nagar",
                "Jaunpur district",
                "Ramabai Nagar (Kanpur Dehat)",
                "Kannauj",
                "Kanpur",
                "Kaushambi",
                "Kushinagar",
                "Lalitpur",
                "Lakhimpur Kheri",
                "Lucknow",
                "Mau",
                "Meerut",
                "Maharajganj",
                "Mahoba",
                "Mirzapur",
                "Moradabad",
                "Mainpuri",
                "Mathura",
                "Muzaffarnagar",

                "Noida",
                "Greater noida",

                "Panchsheel Nagar district (Hapur)",
                "Pilibhit",
                "Shamli",
                "Pratapgarh",
                "Rampur",
                "Raebareli",
                "Saharanpur",
                "Sitapur",
                "Shahjahanpur",
                "Sant Kabir Nagar",
                "Siddharthnagar",
                "Sahibabad",
                "Sonbhadra",
                "Sant Ravidas Nagar",
                "Sultanpur",
                "Shravasti",
                "Unnao",
                "Varanasi",

                "Uttarakhand",
                "Almora",
                "Bageshwar",
                "Chamoli",
                "Champawat",
                "Dehradun",
                "Haridwar",
                "Nainital",
                "Pauri Garhwal",
                "Pithoragarh",
                "Rudraprayag",
                "Tehri Garhwal",
                "Udham Singh Nagar",
                "Uttarkashi",
                "West Bengal",
                "Birbhum",
                "Bankura",
                "Bardhaman",
                "Darjeeling",
                "Dakshin Dinajpur",
                "Hooghly",
                "Howrah",
                "Jalpaiguri",
                "Cooch Behar",
                "Kolkata",
                "Maldah",
                "Paschim Medinipur",
                "Purba Medinipur",
                "Murshidabad",
                "Nadia",
                "North 24 Parganas",
                "South 24 Parganas",
                "Purulia",
                "Uttar Dinajpur"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,cities);
        cityEditText.setAdapter(arrayAdapter);  // array adapter is needed to store the string
                                                // in cityEditText suggestions in the form of array having simple layout
        cityEditText.setThreshold(1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case (R.id.logoutBtn):
                Dialog dialog = new Dialog(TodayWeather.this,R.style.Dialog);
                dialog.setContentView(R.layout.logout_dialog_layout);
                Button yesButton, noButton;
                yesButton = dialog.findViewById(R.id.yesButton);
                noButton = dialog.findViewById(R.id.noButton);

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    //    signOut();
                        startActivity(new Intent(TodayWeather.this, MainActivity.class));
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case (R.id.profileBtn):
                Intent intent = new Intent(TodayWeather.this, ProfileActivity.class);
                intent.putExtra("user'sPhoneNo", userPhoneNo);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}