package com.example.mscarenew.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mscarenew.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.provider.Settings;
import android.net.Uri;

public class HomeFragment extends Fragment {

    // Weather & greeting
    private TextView greetings, date, weatherTextView;
    private FusedLocationProviderClient fusedLocationClient;
    private final String apiKey = "f6497f6aa2d09a3f20fab5e1c7f905a4";
    private final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private final int WEATHER_ICON_SIZE_DP = 48;

    // Heat tolerance UI
    private TextView tvHeatIndex, tvAlertLevel, tvUVIndex;
    private View viewIndicator;
    private ProgressBar progressHydration;
    private Button btnLogWater, btnRemoveWater;
    private Switch switchAutoRemind;
    private ImageButton btnHeatSettings;

    // Hydration prefs
    private SharedPreferences prefs;
    private static final String PREFS = "HeatPrefs";
    private static final String KEY_WATER = "waterCount";
    private static final String KEY_AUTO = "autoRemind";

    // Hydration & heat thresholds
    private static final int DAILY_GOAL = 8;             // glasses
    private static final double REMIND_INTERVAL_HOURS = 0.30;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // bind greetings & date
        greetings      = view.findViewById(R.id.txtGreetings);
        date           = view.findViewById(R.id.textView5);
        weatherTextView= view.findViewById(R.id.weatherTextView);

        // bind heat-tolerance views
        tvHeatIndex       = view.findViewById(R.id.tvHeatIndex);
        tvAlertLevel      = view.findViewById(R.id.tvAlertLevel);
        tvUVIndex         = view.findViewById(R.id.tvUVIndex);
        viewIndicator     = view.findViewById(R.id.viewIndicator);
        progressHydration = view.findViewById(R.id.progressHydration);
        btnLogWater       = view.findViewById(R.id.btnLogWater);
        btnRemoveWater    = view.findViewById(R.id.btnRemoveWater);
        switchAutoRemind  = view.findViewById(R.id.switchAutoRemind);
        btnHeatSettings   = view.findViewById(R.id.btnHeatSettings);

        // location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // initialize date & greeting
        setCurrentDateTime();
        setGreeting();

        // initialize prefs & hydration UI state
        prefs = requireActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int waterCount = prefs.getInt(KEY_WATER, 0);
        boolean autoRemind = prefs.getBoolean(KEY_AUTO, false);
        progressHydration.setProgress(waterCount);
        switchAutoRemind.setChecked(autoRemind);

        // hydration +1
        btnLogWater.setOnClickListener(v -> {
            int count = prefs.getInt(KEY_WATER, 0) + 1;
            if (count > DAILY_GOAL) count = DAILY_GOAL;
            prefs.edit().putInt(KEY_WATER, count).apply();
            progressHydration.setProgress(count);
        });

        // hydration -1
        btnRemoveWater.setOnClickListener(v -> {
            int count = prefs.getInt(KEY_WATER, 0) - 1;
            if (count < 0) count = 0;
            prefs.edit().putInt(KEY_WATER, count).apply();
            progressHydration.setProgress(count);
        });

        // auto-reminders toggle
        switchAutoRemind.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean(KEY_AUTO, checked).apply();
            if (checked) scheduleHydrationReminder();
            else cancelHydrationReminder();
        });

        // settings stub
        btnHeatSettings.setOnClickListener(v ->
                Toast.makeText(getContext(), "Settings coming soon", Toast.LENGTH_SHORT).show()
        );

        // request location & fetch weather
        checkLocationPermission();

        return view;
    }

    private void setCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        date.setText(df.format(cal.getTime()));
    }

    private void setGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) greetings.setText("Good Morning");
        else if (hour < 17) greetings.setText("Good Afternoon");
        else if (hour < 21) greetings.setText("Good Evening");
        else greetings.setText("Good Night");
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int code,
                                           @NonNull String[] perms,
                                           @NonNull int[] results) {
        if (code == LOCATION_PERMISSION_REQUEST_CODE
                && results.length > 0
                && results[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            fetchWeatherData(
                    "https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=" + apiKey
            );
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), loc -> {
                    if (loc != null) {
                        String url = String.format(
                                Locale.US,
                                "https://api.openweathermap.org/data/2.5/weather?lat=%.6f&lon=%.6f&units=metric&appid=%s",
                                loc.getLatitude(), loc.getLongitude(), apiKey
                        );
                        fetchWeatherData(url);
                    } else {
                        fetchWeatherData(
                                "https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=" + apiKey
                        );
                    }
                })
                .addOnFailureListener(e -> fetchWeatherData(
                        "https://api.openweathermap.org/data/2.5/weather?q=London&units=metric&appid=" + apiKey
                ));
    }

    private void fetchWeatherData(String url) {
        StringRequest req = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        JSONObject main = json.getJSONObject("main");
                        JSONArray weatherArr = json.getJSONArray("weather");

                        int temp = (int) Math.round(main.getDouble("temp"));
                        int tempMax = (int) Math.round(main.getDouble("temp_max"));
                        int tempMin = (int) Math.round(main.getDouble("temp_min"));
                        double humidity = main.getDouble("humidity");
                        double uvIndex = 7.5; // placeholder or fetch real UV

                        // update weather block
                        String city = json.getString("name");
                        String desc = weatherArr.getJSONObject(0).getString("main");
                        int id = weatherArr.getJSONObject(0).getInt("id");

                        String wt = String.format(
                                Locale.US,
                                "%s\n%d°\n%s\nH:%d L:%d",
                                city, temp, desc, tempMax, tempMin
                        );
                        SpannableString ss = new SpannableString(wt);
                        String tStr = temp + "°";
                        int s = wt.indexOf(tStr), e = s + tStr.length();
                        ss.setSpan(new RelativeSizeSpan(1.5f), s, e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        setWeatherIconAndText(getWeatherIcon(id), ss);

                        // update heat card
                        updateHeatCard(temp, humidity, uvIndex);

                    } catch (Exception ex) {
                        Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                err -> Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(requireContext()).add(req);
    }

    private void setWeatherIconAndText(int resId, CharSequence text) {
        Drawable icon = ResourcesCompat.getDrawable(getResources(), resId, null);
        if (icon != null) {
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, WEATHER_ICON_SIZE_DP,
                    getResources().getDisplayMetrics()
            );
            icon.setBounds(0, 0, px, px);
        }
        weatherTextView.setCompoundDrawables(icon, null, null, null);
        weatherTextView.setText(text);
    }

    private int getWeatherIcon(int weatherId) {
        if (weatherId < 300) return R.drawable.rain_thunder;
        if (weatherId < 600) return R.drawable.ic_weather_rainy;
        if (weatherId < 700) return R.drawable.snow;
        if (weatherId < 800) return R.drawable.fog;
        if (weatherId == 800) return R.drawable.sunny;
        if (weatherId == 801) return R.drawable.ic_weather_cloudy;
        return R.drawable.day_partial_cloud;
    }

    @SuppressLint("DefaultLocale")
    private void updateHeatCard(int temperature, double humidity, double uv) {
        double hiC = computeHeatIndex(temperature, humidity);
        String level = mapAlertLevel(hiC);

        tvHeatIndex.setText(String.format("Heat Index: %.1f°C", hiC));
        tvUVIndex.setText(String.format("UV Index: %.1f", uv));
        tvAlertLevel.setText("Status: " + level);
        viewIndicator.setBackgroundColor(mapLevelColor(level));
    }

    // heat index formula in °C
    private double computeHeatIndex(double T, double R) {
        double Tf = T * 9/5 + 32;
        double HIf = -42.379
                + 2.04901523 * Tf
                + 10.14333127 * R
                - 0.22475541 * Tf * R
                - 0.00683783 * Tf * Tf
                - 0.05481717 * R * R
                + 0.00122874 * Tf * Tf * R
                + 0.00085282 * Tf * R * R
                - 0.00000199 * Tf * Tf * R * R;
        return (HIf - 32) * 5/9;
    }

    private String mapAlertLevel(double hiC) {
        double hiF = hiC * 9/5 + 32;
        if (hiF < 50) return "Fair";
       if (hiF < 80) return "Caution";
        if (hiF < 90) return "High Caution";
        if (hiF < 103) return "Danger";
        return " High Danger";
    }

    private int mapLevelColor(String level) {
        switch (level) {
            case "Good":         return Color.parseColor("#98FB98");
            case "Caution":         return Color.parseColor("#FFEB3B");
            case "High Caution": return Color.parseColor("#FFC107");
            case "Danger":          return Color.parseColor("#EE4B2B");
            default:                return Color.parseColor("#F44336");
        }
    }



    @SuppressLint("ScheduleExactAlarm")
    private void scheduleHydrationReminder() {
        AlarmManager am = (AlarmManager) requireContext()
                .getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        // On API 31+, check if exact alarms are allowed:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && !am.canScheduleExactAlarms()) {
            // Send user to the system page to grant exact-alarm permission:
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                    Uri.parse("package:" + requireContext().getPackageName()));
            startActivity(intent);
            Toast.makeText(getContext(),
                            "Please allow exact alarms in Settings",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, (int) REMIND_INTERVAL_HOURS);
        Intent i = new Intent(getContext(), HydrationReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                getContext(),
                0,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Now schedule exactly
        am.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                pi
        );
        Toast.makeText(getContext(),
                "Exact auto-reminder set in " + REMIND_INTERVAL_HOURS + "h",
                Toast.LENGTH_SHORT
        ).show();
    }



    private void cancelHydrationReminder() {
        Intent intent = new Intent(getContext(), HydrationReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                getContext(), 0, intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );
        if (pi != null) {
            AlarmManager am = (AlarmManager) requireContext()
                    .getSystemService(Context.ALARM_SERVICE);
            if (am != null) am.cancel(pi);
            pi.cancel();
            Toast.makeText(getContext(),
                    "Auto-reminders canceled",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
