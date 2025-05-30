package com.example.mscarenew.ui.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mscarenew.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MathChallengeGameFragment extends Fragment {

    private static final String PREFS = "MathChallengePrefs";
    private static final String KEY_HIGH = "highScore";

    private TextView tvQuestion, tvScore;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;

    private int score = 0;
    private int questionCount = 0;
    private int highScore = 0;
    private int correctAnswer;

    private SharedPreferences prefs;
    private Random rnd = new Random();

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_math_challenge_game, container, false
        );
        prefs = requireActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        highScore = prefs.getInt(KEY_HIGH, 0);

        tvQuestion = v.findViewById(R.id.tvQuestion);
        tvScore    = v.findViewById(R.id.tvScore);
        btnOption1 = v.findViewById(R.id.btnOption1);
        btnOption2 = v.findViewById(R.id.btnOption2);
        btnOption3 = v.findViewById(R.id.btnOption3);
        btnOption4 = v.findViewById(R.id.btnOption4);

        View.OnClickListener listener = view -> {
            int selected = Integer.parseInt(((Button)view).getText().toString());
            if (selected == correctAnswer) score++;
            questionCount++;
            updateScoreDisplay();
            if (questionCount >= 10) showResults();
            else nextQuestion();
        };

        btnOption1.setOnClickListener(listener);
        btnOption2.setOnClickListener(listener);
        btnOption3.setOnClickListener(listener);
        btnOption4.setOnClickListener(listener);

        nextQuestion();
        return v;
    }

    private void nextQuestion() {
        // generate simple random arithmetic
        int a = rnd.nextInt(20) + 1;
        int b = rnd.nextInt(20) + 1;
        char op = "+-×÷".charAt(rnd.nextInt(4));
        switch (op) {
            case '+': correctAnswer = a + b; break;
            case '-': correctAnswer = a - b; break;
            case '×': correctAnswer = a * b; break;
            default:
                // ensure divisible
                correctAnswer = a;
                b = (b == 0) ? 1 : b;
                a = correctAnswer * b;
                op = '÷';
                break;
        }
        tvQuestion.setText(a + " " + op + " " + b + " = ?");

        // build options: correct + 3 distractors
        List<Integer> options = new ArrayList<>();
        options.add(correctAnswer);
        while (options.size() < 4) {
            int fake = correctAnswer + rnd.nextInt(11) - 5;
            if (fake != correctAnswer && !options.contains(fake)) {
                options.add(fake);
            }
        }
        Collections.shuffle(options);

        btnOption1.setText(String.valueOf(options.get(0)));
        btnOption2.setText(String.valueOf(options.get(1)));
        btnOption3.setText(String.valueOf(options.get(2)));
        btnOption4.setText(String.valueOf(options.get(3)));
    }

    private void updateScoreDisplay() {
        tvScore.setText("Score: " + score + "/" + questionCount +
                "   Best: " + highScore
        );
    }

    private void showResults() {
        if (score > highScore) {
            prefs.edit().putInt(KEY_HIGH, score).apply();
            highScore = score;
        }
        new AlertDialog.Builder(requireContext())
                .setTitle("Results")
                .setMessage("Your score: " + score + "/10\nBest: " + highScore)
                .setPositiveButton("Play Again", (d,i) -> {
                    score = 0;
                    questionCount = 0;
                    updateScoreDisplay();
                    nextQuestion();
                })
                .setNegativeButton("Close", null)
                .show();
    }
}
