package com.example.mscarenew.ui.game; // Update to your package

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.mscarenew.R;

import java.util.Random;

public class NeuroCycleGameFragment extends Fragment {

    private SharedPreferences sharedPref;
    private static final String PREFS_NAME = "NeuroGamePrefs";
    private static final String KEY_HIGH_SCORE = "highScore";
    private TextView tvColorPrompt, tvResult, tvScore;
    private Button btnRed, btnGreen, btnBlue;
    private String currentColor = "";
    private int score = 0;
    private int attempts = 0;
    private final Random random = new Random();
    private final String[] colors = {"RED", "GREEN", "BLUE"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neuro_cycle_game, container, false);

        sharedPref = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        initViews(view);
        setButtonListeners();
        resetGame(); // Start fresh

        return view;
    }

    private void initViews(View view) {
        tvColorPrompt = view.findViewById(R.id.tvColorPrompt);
        tvResult = view.findViewById(R.id.tvResult);
        tvScore = view.findViewById(R.id.tvScore);

        btnRed = view.findViewById(R.id.btnRed);
        btnGreen = view.findViewById(R.id.btnGreen);
        btnBlue = view.findViewById(R.id.btnBlue);
    }

    private void setButtonListeners() {
        btnRed.setOnClickListener(v -> checkAnswer("RED"));
        btnGreen.setOnClickListener(v -> checkAnswer("GREEN"));
        btnBlue.setOnClickListener(v -> checkAnswer("BLUE"));
    }

    private void checkAnswer(String selectedColor) {
        attempts++;

        if (selectedColor.equals(currentColor)) {
            score++;
            tvResult.setText("✅ Correct! +1 point");
        } else {
            tvResult.setText("❌ Wrong! The correct was " + currentColor);
        }

        updateScore();
        setNewPrompt();

        // Optional: End game after certain attempts
        if (attempts >= 10) {
            showFinalResults();
            resetGame();
        }
    }

    private void updateScore() {
        double accuracy = attempts > 0 ? (score * 100.0 / attempts) : 0;

        // Get current high score
        int highScore = sharedPref.getInt(KEY_HIGH_SCORE, 0);

        // Update high score if current is better
        if (score > highScore) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(KEY_HIGH_SCORE, score);
            editor.apply();
        }

        String scoreText = String.format(
                "Score: %d/%d (%.1f%%)\nHigh Score: %d",
                score,
                attempts,
                accuracy,
                Math.max(score, highScore)
        );

        tvScore.setText(scoreText);
    }

    private void showFinalResults() {
        double accuracy = attempts > 0 ? (score * 100.0 / attempts) : 0;

        new AlertDialog.Builder(requireContext())
                .setTitle("Game Results")
                .setMessage(String.format(
                        "Final Score: %d/%d\nAccuracy: %.1f%%\nHigh Score: %d",
                        score,
                        attempts,
                        accuracy,
                        sharedPref.getInt(KEY_HIGH_SCORE, 0)
                ))
                .setPositiveButton("OK", null)
                .show();
    }

    private void resetGame() {
        score = 0;
        attempts = 0;
        updateScore();
        setNewPrompt();
    }

    private void setNewPrompt() {
        currentColor = colors[random.nextInt(colors.length)];
        tvColorPrompt.setText("Tap the " + currentColor + " button");
    }
}