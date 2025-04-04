package com.example.mscarealpha.ui.symptoms;

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

import com.example.mscarealpha.R;

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

        bodyPartImageMap.put("Head and Cognitive", R.drawable.head_img);
        bodyPartImageMap.put("Neck and Shoulder", R.drawable.neck_and_shoulder_img);
        bodyPartImageMap.put("Chest", R.drawable.chest_img);
        bodyPartImageMap.put("Hand", R.drawable.hand_img);
        bodyPartImageMap.put("Stomach", R.drawable.stomach_img);
        bodyPartImageMap.put("Groin and Sexual", R.drawable.groin_img);
        bodyPartImageMap.put("Thigh and Upper Leg", R.drawable.thigh_and_upperleg_img);
        bodyPartImageMap.put("Lower Leg", R.drawable.lowerleg_img);
        bodyPartImageMap.put("Upper and Lower Back", R.drawable.upper_and_lower_img);
        bodyPartImageMap.put("Back Head and Neck", R.drawable.back_head_and_neck_img);


        // Initialize the symptom map (MS symptom data)
        symptomMap = new HashMap<>();

        // Head
        symptomMap.put("Head and Cognitive", Arrays.asList("Optic Neuritis", "Diplopia", "Nystagmus", "Ocular Dysmetria", "Internuclear Ophthalmoplegia", "Phosphenes", "Cognitive Dysfunction", "Mood swings", "Emotional lability", "Euphoria", "Depression", "Anxiety", "Aphasia", "Dysphasia"));

        // Neck and Shoulder
        symptomMap.put("Neck and Shoulder", Arrays.asList("Pain and stiffness", "Lhermitte's sign", "Muscle Atrophy", "Paresis", "Plegia", "Spasticity", "Dysarthria"));

        // Chest
        symptomMap.put("Chest", Arrays.asList("Tightness or banding sensation (MS hug)", "Respiratory problems"));

        // Hand
        symptomMap.put("Hand", Arrays.asList("Numbness and tingling", "Weakness", "Tremors", "Paraesthesia", "Anaesthesia", "Intention Tremor", "Dysmetria"));

        // Stomach
        symptomMap.put("Stomach", Arrays.asList("Bowel problems", "Pain or discomfort", "Gastroesophageal Reflux"));

        // Groin
        symptomMap.put("Groin and Sexual", Arrays.asList("Sexual dysfunction", "Bladder problems", "Frequent Micturation", "Bladder Spasticity", "Flaccid Bladder", "Detrusor-Sphincter Dyssynergia", "Erectile Dysfunction", "Anorgasmy", "Retrograde ejaculation", "Frigidity", "Constipation", "Fecal Urgency", "Fecal Incontinence"));

        // Thigh and Upper Leg
        symptomMap.put("Thigh and Upper Leg", Arrays.asList("Weakness", "Numbness and tingling", "Spasticity", "Paresis", "Plegia", "Muscle Atrophy", "Spasms/Cramps", "Hypotonia/Clonus"));

        // Lower Leg
        symptomMap.put("Lower Leg", Arrays.asList("Pain and stiffness","Weakness","Spasticity", "Numbness and tingling", "Foot drop", "Paresis", "Plegia", "Muscle Atrophy", "Spasms/Cramps", "Hypotonia/Clonus", "Restless Leg Syndrome"));

        // Upper Back
        symptomMap.put("Upper and Lower Back", Arrays.asList("Pain and stiffness", "Weakness", "Paresis", "Plegia", "Muscle Atrophy", "Spasms/Cramps"));

        // Back Head and Neck
        symptomMap.put("Back Head and Neck", Arrays.asList("Pain and stiffness", "Muscle spasms", "Paresis", "Plegia", "Spasticity"));


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
