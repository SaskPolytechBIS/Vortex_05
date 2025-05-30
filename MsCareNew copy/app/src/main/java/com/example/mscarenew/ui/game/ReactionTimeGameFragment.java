package com.example.mscarenew.ui.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mscarenew.R;
import java.util.Random;

public class ReactionTimeGameFragment extends Fragment {
    private TextView tvPrompt, tvBest;
    private Button btnAction;
    private Handler handler = new Handler();
    private long startTime;
    private SharedPreferences prefs;
    private static final String PREFS = "ReactPrefs";
    private static final String KEY_BEST = "bestTime";
    private Random rnd = new Random();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reaction_time_game, container, false);
        tvPrompt  = v.findViewById(R.id.tvPrompt);
        tvBest    = v.findViewById(R.id.tvBest);
        btnAction = v.findViewById(R.id.btnAction);
        prefs     = requireActivity()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        updateBest();
        resetGame();
        btnAction.setOnClickListener(view -> onActionClicked());
        return v;
    }

    private void resetGame() {
        tvPrompt.setText("Wait for GREEN...");
        btnAction.setEnabled(false);
        btnAction.setBackgroundColor(Color.RED);
        int delay = 1000 + rnd.nextInt(3000);
        handler.postDelayed(this::goGreen, delay);
    }

    private void goGreen() {
        tvPrompt.setText("TAP! NOW!");
        btnAction.setEnabled(true);
        btnAction.setBackgroundColor(Color.GREEN);
        startTime = System.currentTimeMillis();
    }

    private void onActionClicked() {
        long reaction = System.currentTimeMillis() - startTime;
        tvPrompt.setText("Reaction: " + reaction + " ms");
        saveBest(reaction);
        handler.postDelayed(this::resetGame, 2000);
    }

    private void saveBest(long reaction) {
        long best = prefs.getLong(KEY_BEST, Long.MAX_VALUE);
        if (reaction < best) {
            prefs.edit().putLong(KEY_BEST, reaction).apply();
            updateBest();
        }
    }

    private void updateBest() {
        long best = prefs.getLong(KEY_BEST, Long.MAX_VALUE);
        String text = best == Long.MAX_VALUE
                ? "Best: -- ms"
                : "Best: " + best + " ms";
        tvBest.setText(text);
    }
}
