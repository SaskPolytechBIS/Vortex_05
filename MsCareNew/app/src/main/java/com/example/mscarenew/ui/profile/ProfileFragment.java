package com.example.mscarenew.ui.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mscarenew.MainActivity;
import com.example.mscarenew.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class ProfileFragment extends Fragment {
    private static final int    REQUEST_PICK_IMAGE = 1001;
    private static final String PREFS_NAME         = "user_prefs";
    private static final String KEY_LOGIN_EMAIL    = "logged_in_email";

    private ImageView ivProfilePic;
    private EditText  etFirstName, etLastName,
            etBirthdate, etAge,
            etAddress, etEmail, etPhone, etBio;
    private RadioGroup rgSex;
    private Button     btnChangePic, btnSaveProfile;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs         = requireActivity()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        ivProfilePic   = view.findViewById(R.id.ivProfilePic);
        etFirstName    = view.findViewById(R.id.etFirstName);
        etLastName     = view.findViewById(R.id.etLastName);
        etBirthdate    = view.findViewById(R.id.etBirthdate);
        etAge          = view.findViewById(R.id.etAge);
        rgSex          = view.findViewById(R.id.rgSex);
        etAddress      = view.findViewById(R.id.etAddress);
        etEmail        = view.findViewById(R.id.etEmail);
        etPhone        = view.findViewById(R.id.etPhone);
        etBio          = view.findViewById(R.id.etBio);
        btnChangePic   = view.findViewById(R.id.btnChangePic);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);

        // Load saved values
        etFirstName.setText(prefs.getString("first_name", ""));
        etLastName .setText(prefs.getString("last_name", ""));
        etBirthdate.setText(prefs.getString("birthdate", ""));
        etAge      .setText(prefs.getString("age", ""));
        etAddress  .setText(prefs.getString("address", ""));
        etEmail    .setText(prefs.getString("profile_email", ""));
        etPhone    .setText(prefs.getString("phone", ""));
        etBio      .setText(prefs.getString("bio", ""));

        String sex = prefs.getString("sex", "Other");
        if      (sex.equals("Male"))   rgSex.check(R.id.rbMale);
        else if (sex.equals("Female")) rgSex.check(R.id.rbFemale);
        else                            rgSex.check(R.id.rbOther);

        // Date picker
        etBirthdate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(requireContext(),
                    (DatePicker dp, int y, int m, int d) ->
                            etBirthdate.setText(String.format("%04d-%02d-%02d",
                                    y, m+1, d)),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Change picture
        btnChangePic.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pick, REQUEST_PICK_IMAGE);
        });

        // Save profile
        btnSaveProfile.setOnClickListener(v -> {
            String newEmail = etEmail.getText().toString().trim();

            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("first_name",    etFirstName.getText().toString().trim());
            edit.putString("last_name",     etLastName .getText().toString().trim());
            edit.putString("birthdate",     etBirthdate.getText().toString().trim());
            edit.putString("age",           etAge      .getText().toString().trim());
            edit.putString("address",       etAddress  .getText().toString().trim());
            edit.putString("profile_email", newEmail);
            edit.putString(KEY_LOGIN_EMAIL, newEmail);  // keep session in sync
            edit.putString("phone",         etPhone    .getText().toString().trim());
            edit.putString("bio",           etBio      .getText().toString().trim());

            String selectedSex = rgSex.getCheckedRadioButtonId() == R.id.rbMale   ? "Male"
                    : rgSex.getCheckedRadioButtonId() == R.id.rbFemale ? "Female"
                    : "Other";
            edit.putString("sex", selectedSex);

            edit.apply();
            Toast.makeText(getContext(),
                    "Profile saved successfully!",
                    Toast.LENGTH_SHORT).show();

            // update the drawer header immediately
            NavigationView navView = requireActivity().findViewById(R.id.nav_view);
            View header = navView.getHeaderView(0);
            TextView tvLoggedIn = header.findViewById(R.id.tvLoggedIn);
            tvLoggedIn.setText("Logged in as: " + newEmail);
        });
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onActivityResult(int req, int res, @Nullable Intent data) {
        super.onActivityResult(req, res, data);
        if (req == REQUEST_PICK_IMAGE
                && res == getActivity().RESULT_OK
                && data != null) {

            Uri uri = data.getData();
            ivProfilePic.setImageURI(uri);
            // Save URI string
            prefs.edit()
                    .putString("profile_pic_uri", uri.toString())
                    .apply();
        }
    }
}
