package com.example.msuserprofile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GameMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_game); // Correct layout reference

        Button btnColorGame = findViewById(R.id.btnColorGame);
        btnColorGame.setOnClickListener(v -> {
            startActivity(new Intent(GameMenuActivity.this, NeuroCycleGameActivity.class)); // Start the game
        });
    }
}


