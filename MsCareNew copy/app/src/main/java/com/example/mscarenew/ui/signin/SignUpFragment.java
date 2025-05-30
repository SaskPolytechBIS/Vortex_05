package com.example.mscarenew.ui.signin;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mscarenew.DbContract;
import com.example.mscarenew.DbHelper;
import com.example.mscarenew.R;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignUpFragment extends Fragment {
    private TextInputEditText etFullName, etUsername, etPhone, etEmail, etPassword, etConfirm;
    private MaterialCheckBox cbTerms;
    private Button btnSignUp;
    private TextView tvGoToSignIn;
    private DbHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        dbHelper = new DbHelper(requireContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etFullName   = view.findViewById(R.id.etFullName);
        etUsername   = view.findViewById(R.id.etUsername);
        etPhone      = view.findViewById(R.id.etPhone);
        etEmail      = view.findViewById(R.id.etSignUpEmail);
        etPassword   = view.findViewById(R.id.etSignUpPassword);
        etConfirm    = view.findViewById(R.id.etSignUpConfirm);
        cbTerms      = view.findViewById(R.id.cbTerms);
        btnSignUp    = view.findViewById(R.id.btnSignUp);
        tvGoToSignIn = view.findViewById(R.id.tvGoToSignIn);

        btnSignUp.setOnClickListener(v -> {
            String name    = etFullName.getText().toString().trim();
            String user    = etUsername.getText().toString().trim();
            String phone   = etPhone.getText().toString().trim();
            String email   = etEmail.getText().toString().trim();
            String pass    = etPassword.getText().toString().trim();
            String confirm = etConfirm.getText().toString().trim();

            // basic validation
            if (TextUtils.isEmpty(name)
                    || TextUtils.isEmpty(user)
                    || TextUtils.isEmpty(phone)
                    || TextUtils.isEmpty(email)
                    || TextUtils.isEmpty(pass)
                    || TextUtils.isEmpty(confirm)) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(confirm)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!cbTerms.isChecked()) {
                Toast.makeText(getContext(), "You must agree to the terms", Toast.LENGTH_SHORT).show();
                return;
            }

            // insert into the users table
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DbContract.UserEntry.COL_EMAIL,    email);
            cv.put(DbContract.UserEntry.COL_PASSWORD, pass);
            cv.put(DbContract.UserEntry.COL_CREATED,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            .format(new Date()));

            long newId = db.insert(
                    DbContract.UserEntry.TABLE_NAME,
                    null,
                    cv
            );

            if (newId == -1) {
                Toast.makeText(getContext(),
                        "Sign-up failed (email may already exist)",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),
                        "Account created! Please sign in.",
                        Toast.LENGTH_SHORT).show();
                NavController nav = NavHostFragment.findNavController(this);
                nav.navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });

        tvGoToSignIn.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_signUpFragment_to_signInFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbHelper.close();
    }
}
