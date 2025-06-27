package com.example.mscarenew.ui.signin;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mscarenew.DbContract;
import com.example.mscarenew.DbHelper;
import com.example.mscarenew.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SigninFragment extends Fragment {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_EMAIL  = "logged_in_email";

    private TextInputEditText etEmail, etPassword;
    private MaterialButton    btnSignIn;
    private DbHelper          dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String saved = prefs.getString(KEY_EMAIL, null);
        if (saved != null) {
            // already signed in
            NavHostFragment.findNavController(this)
                    .navigate(R.id.nav_profile);
            return;
        }

        dbHelper   = new DbHelper(requireContext());
        etEmail    = view.findViewById(R.id.etSignInEmail);
        etPassword = view.findViewById(R.id.etSignInPassword);
        btnSignIn  = view.findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass  = etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(getContext(),
                        "Please enter both email and password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection = { DbContract.UserEntry.COL_ID };
            String selection = DbContract.UserEntry.COL_EMAIL + " = ? AND " +
                    DbContract.UserEntry.COL_PASSWORD + " = ?";
            String[] args = { email, pass };

            try (Cursor cursor = db.query(
                    DbContract.UserEntry.TABLE_NAME,
                    projection, selection, args,
                    null, null, null)) {
                if (cursor.moveToFirst()) {
                    prefs.edit()
                            .putString(KEY_EMAIL, email)
                            .apply();
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.nav_profile);
                } else {
                    Toast.makeText(getContext(),
                            "Invalid email or password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.tvGoToSignUp)
                .setOnClickListener(v ->
                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_signInFragment_to_signUpFragment)
                );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbHelper.close();
    }
}
