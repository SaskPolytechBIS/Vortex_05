package com.example.msuserprofile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class NeuroCycleGameActivity extends AppCompatActivity {

    private TextView tvColorPrompt, tvResult, tvScore;
    private String currentColor = "";
    private final String[] colors = {"RED", "GREEN", "BLUE"};
    private int score = 0;
    private int attempts = 0;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neuro_cycle_game);

        tvColorPrompt = findViewById(R.id.tvColorPrompt);
        tvResult = findViewById(R.id.tvResult);
        tvScore = findViewById(R.id.tvScore);

        Button btnRed = findViewById(R.id.btnRed);
        Button btnGreen = findViewById(R.id.btnGreen);
        Button btnBlue = findViewById(R.id.btnBlue);

        btnRed.setOnClickListener(v -> checkAnswer("RED"));
        btnGreen.setOnClickListener(v -> checkAnswer("GREEN"));
        btnBlue.setOnClickListener(v -> checkAnswer("BLUE"));

        setNewPrompt();
    }

    private void checkAnswer(String selectedColor) {
        attempts++;
        if (selectedColor.equals(currentColor)) {
            score++;
            tvResult.setText("✅ Correct!");
        } else {
            tvResult.setText("❌ Try Again!");
        }

        tvScore.setText("Score: " + score + "/" + attempts);
        setNewPrompt();
    }

    private void setNewPrompt() {
        currentColor = colors[random.nextInt(colors.length)];
        tvColorPrompt.setText("Tap the " + currentColor + " button");
    }
}
