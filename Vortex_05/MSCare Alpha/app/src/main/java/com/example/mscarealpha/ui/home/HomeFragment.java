package com.example.mscarealpha.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mscarealpha.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView greetings, date, weatherTextView;
    private FusedLocationProviderClient fusedLocationClient;
    private String apiKey = "f6497f6aa2d09a3f20fab5e1c7f905a4";
    private final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private final int WEATHER_ICON_SIZE_DP = 48; // Adjust this value as needed

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        greetings = view.findViewById(R.id.txtGreetings);
        date = view.findViewById(R.id.textView5);
        weatherTextView = view.findViewById(R.id.weatherTextView);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        setCurrentDateTime();
        setGreeting();
        checkLocationPermission();

        return view;
    }

    private void setCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        date.setText(dateFormat.format(calendar.getTime()));
    }

    private void setGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 5 && hour < 12) {
            greetings.setText("Good Morning");
        } else if (hour >= 12 && hour < 17) {
            greetings.setText("Good Afternoon");
        } else if (hour >= 17 && hour < 21) {
            greetings.setText("Good Evening");
        } else {
            greetings.setText("Good Night");
        }
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                setErrorState("Weather unavailable (permission denied)");
                fetchWeatherData("https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=" + apiKey);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() +
                                "&lon=" + location.getLongitude() + "&units=metric&appid=" + apiKey;
                        fetchWeatherData(url);
                    } else {
                        setErrorState("Weather unavailable");
                        fetchWeatherData("https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=" + apiKey);
                    }
                })
                .addOnFailureListener(e -> {
                    setErrorState("Weather unavailable");
                    fetchWeatherData("https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=" + apiKey);
                });
    }

    private void fetchWeatherData(String weatherUrl) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, weatherUrl,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject main = jsonResponse.getJSONObject("main");
                        String temp = String.valueOf(Math.round(Float.parseFloat(main.getString("temp"))));
                        String tempMax = String.valueOf(Math.round(Float.parseFloat(main.getString("temp_max"))));
                        String tempMin = String.valueOf(Math.round(Float.parseFloat(main.getString("temp_min"))));
                        String city = jsonResponse.getString("name");
                        JSONArray weather = jsonResponse.getJSONArray("weather");
                        String desc = weather.getJSONObject(0).getString("main");
                        int weatherId = weather.getJSONObject(0).getInt("id");

                        String weatherText = String.format("%s\n%s°\n%s\nH:%s L:%s", city, temp, desc, tempMax, tempMin);
                        SpannableString spannable = new SpannableString(weatherText);
                        spannable.setSpan(new RelativeSizeSpan(1.5f), weatherText.indexOf(temp + "°"),
                                (weatherText.indexOf(temp + "°") + (temp + "°").length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        setWeatherIconAndText(getWeatherIcon(weatherId), spannable);

                    } catch (Exception e) {
                        setErrorState("Weather data error");
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    setErrorState("Weather unavailable");
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }

    private void setWeatherIconAndText(int iconRes, CharSequence text) {
        Drawable icon = ResourcesCompat.getDrawable(getResources(), iconRes, null);
        if (icon != null) {
            int pixelSize = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    WEATHER_ICON_SIZE_DP,
                    getResources().getDisplayMetrics());
            icon.setBounds(0, 0, pixelSize, pixelSize);
        }
        weatherTextView.setCompoundDrawables(icon, null, null, null);
        weatherTextView.setText(text);
    }

    private void setErrorState(String message) {
        Drawable errorIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_unknown, null);
        if (errorIcon != null) {
            int pixelSize = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    WEATHER_ICON_SIZE_DP,
                    getResources().getDisplayMetrics());
            errorIcon.setBounds(0, 0, pixelSize, pixelSize);
        }
        weatherTextView.setCompoundDrawables(errorIcon, null, null, null);
        weatherTextView.setText(message);
    }

    private int getWeatherIcon(int weatherId) {
        if (weatherId >= 200 && weatherId < 300) return R.drawable.rain_thunder;
        else if (weatherId >= 300 && weatherId < 600) return R.drawable.ic_weather_rainy;
        else if (weatherId >= 600 && weatherId < 700) return R.drawable.snow;
        else if (weatherId >= 700 && weatherId < 800) return R.drawable.fog;
        else if (weatherId == 800) return R.drawable.sunny;
        else if (weatherId == 801) return R.drawable.ic_weather_cloudy;
        else if (weatherId > 800) return R.drawable.day_partial_cloud;
        else return R.drawable.dry_clean;
    }
}