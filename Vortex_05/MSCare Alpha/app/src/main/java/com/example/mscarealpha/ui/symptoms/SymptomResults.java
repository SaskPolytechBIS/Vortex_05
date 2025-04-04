package com.example.mscarealpha.ui.symptoms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.mscarealpha.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SymptomResults extends DialogFragment {

    public String SymptomsFinalTxt = "";

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Listen for fragment results
//        getParentFragmentManager().setFragmentResultListener("fromFirstFragment", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//
//
//            }
//        });
//    }
@Override

public Dialog onCreateDialog(Bundle savedInstance){

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();

    View dialogView = inflater.inflate(R.layout.fragment_symptom_results, null);

        // Printing the results into the text View
        SymptomDbHelper dm = new SymptomDbHelper(getActivity());

        TextView textResults = dialogView.findViewById(R.id.results_textView); // Get the TextView

        List<Symptom> symptomList = dm.selectAll(); // Fetch the symptom data
        String list = ""; // Prepare a string to hold the formatted results

        // Loop through the symptom list (not the Cursor)
        /* Old Formatting:
        for (Symptom symptom : symptomList) {
            list += ("Body Part Associated : " + symptom.getBodyPart() + " \nSymptom Type: " + symptom.getSymptomName() + " \nPain: " + symptom.getPainLevel() + "\n"); // Formatting the symptom data
        }*/

        for (Symptom symptom : symptomList) {
            list += String.format("%s - %s (Pain: %d) - %s\n",
                    symptom.getBodyPart(), symptom.getSymptomName(), symptom.getPainLevel(), symptom.getTimestamp());
        }

        textResults.setText(list); // Display the formatted data in the TextView

        SymptomsFinalTxt = list;

        Button btnBack = dialogView.findViewById(R.id.btn_SymptomFrag); // Find the back button (add this to your XML)
        Button btnSave = dialogView.findViewById(R.id.btn_Save); // Find the save button (add this to your XML)

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        btnSave.setOnClickListener(v -> saveResultsToFile()); // Save results

            builder.setView(dialogView).setMessage("Logged Symptoms");

        return builder.create();
    }

    private void saveResultsToFile() {

        try {
            // Create a file name
            String fileName = "symptom_results_" + System.currentTimeMillis() + ".txt";

            // Get the directory to save in (Downloads)
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // Create the file
            File file = new File(downloadsDir, fileName);
            FileWriter writer = new FileWriter(file);
            writer.write(SymptomsFinalTxt);
            writer.close();

            Toast.makeText(requireContext(), "Results saved to Downloads folder", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error saving results", Toast.LENGTH_SHORT).show();
        }
    }
}
