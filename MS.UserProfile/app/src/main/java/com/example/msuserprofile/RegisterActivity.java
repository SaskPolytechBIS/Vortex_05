package com.example.msuserprofile;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password, gender, identify, dob, illness, symptom, diagnosisDate;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        gender = findViewById(R.id.gender);
        identify = findViewById(R.id.identify);
        dob = findViewById(R.id.dob);
        illness = findViewById(R.id.illness);
        symptom = findViewById(R.id.symptom);
        diagnosisDate = findViewById(R.id.diagnosisDate);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        String genderInput = gender.getText().toString();
        String identifyInput = identify.getText().toString();
        String dobInput = dob.getText().toString();
        String illnessInput = illness.getText().toString();
        String symptomInput = symptom.getText().toString();
        String diagnosisDateInput = diagnosisDate.getText().toString();

        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            // You can store user data in shared preferences or a database for persistence
            // After successful registration, proceed to the Login screen
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
