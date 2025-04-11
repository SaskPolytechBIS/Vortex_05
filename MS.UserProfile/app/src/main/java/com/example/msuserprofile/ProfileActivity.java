package com.example.msuserprofile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView profileDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // The layout file for ProfileActivity

        // Make sure the ID matches what you have in the XML
        profileDisplay = findViewById(R.id.profileDisplay);  // This should match the ID in your XML

        // Load profile data or display something in the profileDisplay TextView
        loadProfile();
    }

    private void loadProfile() {
        // Retrieve data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String profileText = "Email: " + prefs.getString("email", "N/A") + "\n" +
                "Gender: " + prefs.getString("gender", "N/A") + "\n" +
                "Identify as: " + prefs.getString("identify", "N/A") + "\n" +
                "Date of Birth: " + prefs.getString("dob", "N/A") + "\n" +
                "Illness: " + prefs.getString("illness", "N/A") + "\n" +
                "First Symptom: " + prefs.getString("symptom", "N/A") + "\n" +
                "Diagnosis Date: " + prefs.getString("diagnosisDate", "N/A");

        // Set the text to the profileDisplay TextView
        profileDisplay.setText(profileText);
    }
}
