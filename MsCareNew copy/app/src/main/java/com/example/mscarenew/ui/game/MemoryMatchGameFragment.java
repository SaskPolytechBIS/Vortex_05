package com.example.mscarenew.ui.game;

import android.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MemoryMatchGameFragment extends Fragment {

    private Button[] buttons = new Button[12];
    private String[] faces = {"🐶","🐱","🐭","🐹","🐰","🦊"};
    private List<String> cards = new ArrayList<>();
    private int firstIndex = -1, secondIndex = -1;
    private int matches = 0, moves = 0;
    private TextView tvStats;
    private Handler handler = new Handler();

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memory_match_game, container, false);
        // initialize buttons by id
        for (int i = 0; i < 12; i++) {
            int resId = getResources().getIdentifier("btnCard" + i, "id", requireContext().getPackageName());
            buttons[i] = v.findViewById(resId);
            int idx = i;
            buttons[i].setOnClickListener(view -> onCardClicked(idx));
        }
        tvStats = v.findViewById(R.id.tvStats);
        v.findViewById(R.id.btnRestart).setOnClickListener(x -> resetGame());
        resetGame();
        return v;
    }

    private void resetGame() {
        // build deck: two of each face
        cards.clear();
        for (String f : faces) {
            cards.add(f);
            cards.add(f);
        }
        Collections.shuffle(cards, new Random());
        firstIndex = secondIndex = -1;
        matches = moves = 0;
        updateStats();
        // hide all cards
        for (Button b : buttons) {
            b.setText("");
            b.setEnabled(true);
        }
    }

    private void onCardClicked(int index) {
        if (firstIndex != -1 && secondIndex != -1) return;
        buttons[index].setText(cards.get(index));
        if (firstIndex == -1) {
            firstIndex = index;
            return;
        }
        secondIndex = index;
        moves++;
        // check match after short delay
        handler.postDelayed(() -> {
            if (cards.get(firstIndex).equals(cards.get(secondIndex))) {
                buttons[firstIndex].setEnabled(false);
                buttons[secondIndex].setEnabled(false);
                matches++;
            } else {
                buttons[firstIndex].setText("");
                buttons[secondIndex].setText("");
            }
            firstIndex = secondIndex = -1;
            updateStats();
            if (matches == faces.length) showWinDialog();
        }, 500);
    }

    private void updateStats() {
        tvStats.setText("Matches: " + matches + "  Moves: " + moves);
    }

    private void showWinDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("You Win!")
                .setMessage("You matched all pairs in " + moves + " moves.")
                .setPositiveButton("Restart", (d, w) -> resetGame())
                .show();
    }
}
