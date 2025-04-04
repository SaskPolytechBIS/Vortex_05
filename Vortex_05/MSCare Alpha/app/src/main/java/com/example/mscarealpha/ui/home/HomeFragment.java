package com.example.mscarealpha.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
    private ImageView weatherIcon;
    private FusedLocationProviderClient fusedLocationClient;
    private String apiKey = "f6497f6aa2d09a3f20fab5e1c7f905a4"; // Replace with your API key
    private final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        greetings = view.findViewById(R.id.txtGreetings);
        date = view.findViewById(R.id.textView5);
        weatherTextView = view.findViewById(R.id.weatherTextView);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Set current date and greeting
        setCurrentDateTime();
        setGreeting();

        // Automatically fetch weather based on location
        checkLocationPermission();

        return view;
    }

    private void setCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        date.setText(currentDate);
    }

    private void setGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

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
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                weatherTextView.setText("Weather unavailable (location permission denied)");
                weatherIcon.setImageResource(R.drawable.ic_unknown);
                // Fallback to a default city if permission is denied
                fetchWeatherData("https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=" + apiKey);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() +
                                "&lon=" + location.getLongitude() + "&units=metric&appid=" + apiKey;
                        fetchWeatherData(weatherUrl);
                    } else {
                        weatherTextView.setText("Weather unavailable (location error)");
                        weatherIcon.setImageResource(R.drawable.ic_unknown);
                        // Fallback to a default city if location is null
                        fetchWeatherData("https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=" + apiKey);
                    }
                })
                .addOnFailureListener(e -> {
                    weatherTextView.setText("Weather unavailable (location error)");
                    weatherIcon.setImageResource(R.drawable.ic_unknown);
                    // Fallback to a default city if location fails
                    fetchWeatherData("https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=" + apiKey);
                });
    }

    private void fetchWeatherData(String weatherUrl) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, weatherUrl,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject main = jsonResponse.getJSONObject("main");
                        String temperature = main.getString("temp");
                        String city = jsonResponse.getString("name");
                        String country = jsonResponse.getJSONObject("sys").getString("country");
                        JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                        String description = weatherArray.getJSONObject(0).getString("description");
                        int weatherId = weatherArray.getJSONObject(0).getInt("id");

                        // Update UI with weather information
                        String weatherText = String.format("%s°C in %s, %s\n%s",
                                temperature, city, country, description);
                        weatherTextView.setText(weatherText);

                        // Set weather icon based on condition code
                        weatherIcon.setImageResource(getWeatherIcon(weatherId));

                    } catch (Exception e) {
                        weatherTextView.setText("Weather data error");
                        weatherIcon.setImageResource(R.drawable.ic_unknown);
                        Toast.makeText(getContext(), "Error parsing weather data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    weatherTextView.setText("Weather unavailable");
                    weatherIcon.setImageResource(R.drawable.ic_unknown);
                    Toast.makeText(getContext(), "Network error fetching weather", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }

    private int getWeatherIcon(int weatherId) {
        // Thunderstorm
        if (weatherId >= 200 && weatherId < 300) {
            return R.drawable.rain_thunder;
        }
        // Drizzle/Rain
        else if (weatherId >= 300 && weatherId < 600) {
            return R.drawable.ic_weather_rainy;
        }
        // Snow
        else if (weatherId >= 600 && weatherId < 700) {
            return R.drawable.snow;
        }
        // Atmosphere (Fog, Mist, etc.)
        else if (weatherId >= 700 && weatherId < 800) {
            return R.drawable.fog;
        }
        // Clear
        else if (weatherId == 800) {
            return R.drawable.sunny;
        }
        // Clouds
        else if (weatherId > 800) {
            if (weatherId == 801) {
                return R.drawable.ic_weather_cloudy;
            } else {
                return R.drawable.day_partial_cloud;
            }
        }
        // Default
        else {
            return R.drawable.dry_clean;
        }
    }
}