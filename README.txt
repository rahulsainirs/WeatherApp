~App name = WeatherApp

~Introduction:

This is an android application which supports minSdkVersion = 21 and targetSdkVersion 30,

~Layouts/UI:
1. Attractive, Responsive layouts, custom dialog, custom editTEXT, custom Button, custom Toolbar etc.
2. Xml Lyouts: MainActivity, RegistrationActivity, activityTodayWeather, ProfileActivity.

~Library/dependency used:

1. 'com.google.android.material:material:1.4.0'
2. 'com.airbnb.android:lottie:3.7.0'

~DataBase used:

Sqlite database is used for registration and login also to store the data so that it can be retrieved on profile page/profile activity.


~API used:

1. openWeatherMap api used to get the weather report with weatherIcon/WIDGET.
http://api.openweathermap.org/data/2.5/weather?q="+{CITY}+"&lang=en&appid={API KEY}");


2. postalPinCode api is used to get the State and District from pincode.
"https://api.postalpincode.in/pincode/"+{PINCODE}

        