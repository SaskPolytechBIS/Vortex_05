package com.example.mscarenew.ui.symptoms;

import android.annotation.SuppressLint;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mscarenew.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SymptomsFragment extends Fragment{


    Spinner bodyPartSpinner, symptomSpinner;
    private List<Symptom> symptomlist= new ArrayList<>(); // the datasource
    private SymptomAdapter mAdapter; // for the Recycler.ViewAdapter

    // for Database
    private SymptomDbHelper dbHelper;
    private SQLiteDatabase db;

    private RecyclerView recyclerView; // to reference the recyclerview UI Widget
    HashMap<String, List<String>> symptomMap; // Map body parts to symptoms

    // Hashmap to store the reference for the images for the body part
    HashMap<String, Integer> bodyPartImageMap;

    // Array of strings for menu items (body parts)
    String[] bodyParts = {
            "Head and Cognitive", "Neck and Shoulder", "Chest", "Hand", "Stomach", "Groin and Sexual",
            "Thigh and Upper Leg", "Lower Leg", "Upper and Lower Back",
            "Back Head and Neck"
    };

    public static SymptomsFragment newInstance() {
        return new SymptomsFragment();
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_symptoms_journaling, container, false);

        Button btnResults = root.findViewById(R.id.btnResults);

        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SymptomResults symptomResults = new SymptomResults();
                symptomResults.show(getChildFragmentManager(), "");
            }
        });

        // Get the reference to the SymptomDbHelper
        SymptomDbHelper dbHelper = new SymptomDbHelper(getContext());
        // Getting references to the UI widgets
        Button btnLog = root.findViewById(R.id.logging_button);
        // Button btnResults = root.findViewById(R.id.results_button);
        bodyPartSpinner = root.findViewById(R.id.bodypart_dropdown);
        symptomSpinner = root.findViewById(R.id.symptom_dropdown);
        SeekBar pain_range_bar = root.findViewById(R.id.pain_range_bar);
        TextView notes_and_comments = root.findViewById(R.id.notes_and_comments_textbox);
        TextView seekbarValue = root.findViewById(R.id.seekbar_value);
        bodyPartImageMap = new HashMap<>();
        bodyPartImageMap.put("", R.drawable.main_paper_doll_img);
        bodyPartImageMap.put("ForeHead", R.drawable.forehead_front);
        bodyPartImageMap.put("Side Head Front", R.drawable.head_sides_front);
        bodyPartImageMap.put("Side Head Back", R.drawable.head_sides_back);
        bodyPartImageMap.put("Neck", R.drawable.neck_front);
        bodyPartImageMap.put("Neck Back", R.drawable.neck_back);
        bodyPartImageMap.put("Shoulder Right Front", R.drawable.right_shoulder_front);
        bodyPartImageMap.put("Shoulder Left Front", R.drawable.left_shoulder);
        bodyPartImageMap.put("Shoulder Right Back", R.drawable.shoulder_right_back);
        bodyPartImageMap.put("Shoulder Left Back", R.drawable.shoulder_left_back);
        bodyPartImageMap.put("Chest Right", R.drawable.chest_right);
        bodyPartImageMap.put("Chest Left", R.drawable.chest_left_heart);
        bodyPartImageMap.put("Back", R.drawable.human_body_paper_doll);
        bodyPartImageMap.put("Hand Right", R.drawable.hand_right_front);
        bodyPartImageMap.put("Hand Left Front", R.drawable.hand_left_front);
        bodyPartImageMap.put("Hand Right Back", R.drawable.hand_right_back);
        bodyPartImageMap.put("Hand Left Back", R.drawable.hand_left_back);
        bodyPartImageMap.put("Stomach", R.drawable.stomach_front);
        bodyPartImageMap.put("Groin and Sexual Front", R.drawable.genetal_front);
        bodyPartImageMap.put("Thigh Right Front", R.drawable.thai_right_front);
        bodyPartImageMap.put("Thigh Left Front", R.drawable.thai_left_front);
        bodyPartImageMap.put("Thigh Right Back", R.drawable.thai_right_back);
        bodyPartImageMap.put("Thigh Left Back", R.drawable.thai_left_back);
        bodyPartImageMap.put("Lower Leg Right Front", R.drawable.lower_leg_right_front);
        bodyPartImageMap.put("Lower Leg Left Front", R.drawable.lower_leg_left_front);
        bodyPartImageMap.put("Lower Leg Right Back", R.drawable.lower_leg_right_back);
        bodyPartImageMap.put("Lower Leg Left Back", R.drawable.lower_leg_left_back);
        bodyPartImageMap.put("Feet Right Front", R.drawable.feet_right_front);
        bodyPartImageMap.put("Feet Left Front", R.drawable.feet_left_front);
        bodyPartImageMap.put("Feet Right Back", R.drawable.feet_right_back);
        bodyPartImageMap.put("Feet Left Back", R.drawable.feet_left_back);


        symptomMap = new HashMap<>();
        symptomMap.put("",Arrays.asList(""));
        symptomMap.put("ForeHead", Arrays.asList("Optic Neuritis", "Headache", "Cognitive Dysfunction"));
        symptomMap.put("Side Head Front", Arrays.asList("Facial Pain", "Temporal Pain"));
        symptomMap.put("Side Head Back", Arrays.asList("Occipital Neuralgia", "Dizziness"));
        symptomMap.put("Neck", Arrays.asList("Lhermitte's Sign", "Neck Stiffness"));
        symptomMap.put("Neck Back", Arrays.asList("Neck Rigidity", "Balance Issues"));
        symptomMap.put("Shoulder Right Front", Arrays.asList("Muscle Weakness", "Limited Range of Motion"));
        symptomMap.put("Shoulder Left Front", Arrays.asList("Muscle Weakness", "Limited Range of Motion"));
        symptomMap.put("Shoulder Right Back", Arrays.asList("Shoulder Pain", "Spasms"));
        symptomMap.put("Shoulder Left Back", Arrays.asList("Shoulder Pain", "Spasms"));
        symptomMap.put("Chest Right", Arrays.asList("Chest Tightness", "Shortness of Breath"));
        symptomMap.put("Chest Left", Arrays.asList("Palpitations", "MS Hug"));
        symptomMap.put("Back", Arrays.asList("Back Pain", "Muscle Spasms"));
        symptomMap.put("Hand Right", Arrays.asList("Numbness", "Tremors"));
        symptomMap.put("Hand Left Front", Arrays.asList("Numbness", "Tremors"));
        symptomMap.put("Hand Right Back", Arrays.asList("Weak Grip", "Claw Hand"));
        symptomMap.put("Hand Left Back", Arrays.asList("Weak Grip", "Claw Hand"));
        symptomMap.put("Stomach", Arrays.asList("Bloating", "Bowel Dysfunction"));
        symptomMap.put("Groin and Sexual Front", Arrays.asList("Bladder Dysfunction", "Sexual Dysfunction"));
        symptomMap.put("Thigh Right Front", Arrays.asList("Weakness", "Spasticity"));
        symptomMap.put("Thigh Left Front", Arrays.asList("Weakness", "Spasticity"));
        symptomMap.put("Thigh Right Back", Arrays.asList("Sciatica", "Hyperreflexia"));
        symptomMap.put("Thigh Left Back", Arrays.asList("Sciatica", "Hyperreflexia"));
        symptomMap.put("Lower Leg Right Front", Arrays.asList("Foot Drop", "Muscle Cramps"));
        symptomMap.put("Lower Leg Left Front", Arrays.asList("Foot Drop", "Muscle Cramps"));
        symptomMap.put("Lower Leg Right Back", Arrays.asList("Spasticity", "Clonus"));
        symptomMap.put("Lower Leg Left Back", Arrays.asList("Spasticity", "Clonus"));
        symptomMap.put("Feet Right Front", Arrays.asList("Burning Sensation", "Loss of Balance"));
        symptomMap.put("Feet Left Front", Arrays.asList("Burning Sensation", "Loss of Balance"));
        symptomMap.put("Feet Right Back", Arrays.asList("Toe Drag", "Sensory Loss"));
        symptomMap.put("Feet Left Back", Arrays.asList("Toe Drag", "Sensory Loss"));


        ArrayAdapter<String> bodyPartAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>(symptomMap.keySet()));
        bodyPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bodyPartSpinner.setAdapter(bodyPartAdapter);

        bodyPartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBodyPart = parent.getItemAtPosition(position).toString();
                updateSymptomSpinner(selectedBodyPart);

                // Update the image based on the selected body part
                ImageView imageView = root.findViewById(R.id.imageView2);
                Integer imageResource = bodyPartImageMap.get(selectedBodyPart);
                if (imageResource != null) {
                    imageView.setImageResource(imageResource);
                } else {
                    imageView.setImageResource(R.drawable.main_paper_doll_img); // Set a default image if not found
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the recycler view with some data in a loop

                // Save Symptom Data
                // 1. Get Selected Values from Spinners

                String selectedBodyPart = bodyPartSpinner.getSelectedItem().toString();
                String selectedSymptom = symptomSpinner.getSelectedItem().toString();

                // 2. Get Pain Level from SeekBar
                int painLevel = pain_range_bar.getProgress();

                // 3. Get Notes from EditText
                String notes = notes_and_comments.getText().toString();

                // 4. Get Current Date and Time
                //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                //String timestamp = dateFormat.format(new Date());
                // 4. Alternative: Get Timestamp
                //long timestamp = System.currentTimeMillis();

                String timestamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                System.out.println("Log entry at: " + timestamp);
                // Create a new Symptom object
                Symptom newSymptom = new Symptom(selectedBodyPart, selectedSymptom, painLevel, notes, timestamp);



                // Database Code to insert the data into the database through the helper class that we have built


                // Insert the symptom into the database using the SymptomDbHelper
                try {
                    dbHelper.insert(newSymptom);
                } catch (SQLException e) {
                    Log.e("SymptomsFragment", "Error inserting symptom: " + e.getMessage());
                    Toast.makeText(requireContext(), "Error logging symptom", Toast.LENGTH_SHORT).show();
                }



                // Create an instance of the Dialog Fragment
                // Opens Up a dialog that says the Symptom is logged successfully
                symptomDialog myDialog = new symptomDialog();

                // show the dialog using this Activity's Fragment Manager
                myDialog.show(getActivity().getSupportFragmentManager(), "123");
            }
        });

        pain_range_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValue.setText(String.format("%d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // (Code if default value is to be displayed when the fragment is being interacted on touch )
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // (Optional)
            }
        });

        return root;
    }

    private void updateSymptomSpinner(String bodyPart) {
        List<String> symptoms = symptomMap.get(bodyPart);
        ArrayAdapter<String> symptomAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, symptoms);
        symptomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        symptomSpinner.setAdapter(symptomAdapter);
    }


}
