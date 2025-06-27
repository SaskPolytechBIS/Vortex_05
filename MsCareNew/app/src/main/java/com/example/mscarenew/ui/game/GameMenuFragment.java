package com.example.mscarenew.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.mscarenew.R;

public class GameMenuFragment extends Fragment {
    private Button btnNeuroCycle;
    private Button btnReactionTime;
    private Button btnMathChallenge;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnNeuroCycle    = view.findViewById(R.id.btnNeuroCycle);
        btnReactionTime  = view.findViewById(R.id.btnReactionTime);

        NavController nav = NavHostFragment.findNavController(this);

        btnNeuroCycle.setOnClickListener(v ->
                nav.navigate(R.id.neuroCycleGameFragment)
        );
        btnReactionTime.setOnClickListener(v ->
                nav.navigate(R.id.reactionTimeGameFragment)
        );
        btnMathChallenge = view.findViewById(R.id.btnMathChallenge);
        btnMathChallenge.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.mathChallengeGameFragment)
        );
        // new Memory Match button
        Button btnMemoryMatch = view.findViewById(R.id.btnMemoryMatch);
        btnMemoryMatch.setOnClickListener(v ->
                nav.navigate(R.id.memoryMatchGameFragment)
        );
    }
}
