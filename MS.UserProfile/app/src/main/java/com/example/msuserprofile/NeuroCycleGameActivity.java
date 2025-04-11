package com.example.msuserprofile;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class NeuroCycleGameActivity extends AppCompatActivity {

    private TextView tvColorPrompt, tvResult;
    private String currentColor = "";
    private final String[] colors = {"RED", "GREEN", "BLUE"};
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neuro_cycle_game);

        tvColorPrompt = findViewById(R.id.tvColorPrompt);
        tvResult = findViewById(R.id.tvResult);
        Button btnRed = findViewById(R.id.btnRed);
        Button btnGreen = findViewById(R.id.btnGreen);
        Button btnBlue = findViewById(R.id.btnBlue);

        setNewColorPrompt();



        View.OnClickListener buttonClickListener = view -> {


            String clickedColor = "";

            if (view.getId() == R.id.btnRed) clickedColor = "RED";
            else if (view.getId() == R.id.btnGreen) clickedColor = "GREEN";
            else if (view.getId() == R.id.btnBlue) clickedColor = "BLUE";

            if (clickedColor.equals(currentColor)) {
                tvResult.setText("✅ Correct!");
            } else {
                tvResult.setText("❌ Try Again!");
            }

            setNewColorPrompt();
        };

        btnRed.setOnClickListener(buttonClickListener);
        btnGreen.setOnClickListener(buttonClickListener);
        btnBlue.setOnClickListener(buttonClickListener);
    }

    private void setNewColorPrompt() {
        currentColor = colors[random.nextInt(colors.length)];
        tvColorPrompt.setText("Tap the " + currentColor + " button");
    }
}
