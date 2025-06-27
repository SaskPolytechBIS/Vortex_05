package com.example.mscarenew;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mscarenew.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "user_prefs";
    private static final String CHANNEL_ID = "MSCareChannel";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();
        requestNotificationPermission();

        setSupportActionBar(binding.appBarMain.toolbar);
        setupNavigation();
        updateDrawerHeader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDrawerHeader();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "MSCare Reminder Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminder notifications for MSCare");
            NotificationManager mgr = getSystemService(NotificationManager.class);
            if (mgr != null) mgr.createNotificationChannel(channel);
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.POST_NOTIFICATIONS },
                    1001
            );
        }
    }

    private void setupNavigation() {
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navView = binding.navView;
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_content_main);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_medtrack,
                R.id.nav_notes,
                R.id.nav_symptoms,
                R.id.nav_game,
                R.id.nav_profile,
                R.id.nav_signin,
                R.id.nav_faqs,
                R.id.nav_services,
                R.id.nav_about
        )
                .setOpenableLayout(drawer)
                .build();

        // Hook up default nav behavior
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        BottomNavigationView bottomNav = binding.bottomNavigation;
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Intercept Sign Out menu item
        navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_signout) {
                // 1) Clear *all* stored preferences (profile + login)
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                prefs.edit().clear().apply();

                // 2) Immediately refresh the header text
                updateDrawerHeader();

                // 3) Navigate to Sign-In
                navController.navigate(R.id.nav_signin);

                // 4) Close the drawer
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            // otherwise let NavController handle it
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) drawer.closeDrawer(GravityCompat.START);
            return handled;
        });

        // If launched via notification, open Home
        boolean openHome = getIntent().getBooleanExtra("open_hydration", false);
        if (openHome) {
            navView.setCheckedItem(R.id.nav_home);
            navController.navigate(R.id.nav_home);
        }
    }

    private void updateDrawerHeader() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String email = prefs.getString("logged_in_email", null);

        NavigationView navView = binding.navView;
        View header = navView.getHeaderView(0);
        TextView tvLoggedIn = header.findViewById(R.id.tvLoggedIn);
        tvLoggedIn.setText(
                (email != null && !email.isEmpty())
                        ? "Logged in as: " + email
                        : "Not signed in"
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
