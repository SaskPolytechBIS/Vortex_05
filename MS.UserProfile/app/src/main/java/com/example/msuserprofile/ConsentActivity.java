package com.example.msuserprofile;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

public class ConsentActivity extends AppCompatActivity {
    CheckBox consentCheckBox;
    Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        consentCheckBox = findViewById(R.id.consentCheckBox);
        proceedButton = findViewById(R.id.proceedButton);

        proceedButton.setOnClickListener(v -> {
            if (consentCheckBox.isChecked()) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }
}
