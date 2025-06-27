package com.example.mscarenew.ui.symptoms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.mscarenew.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SymptomResults extends DialogFragment {

    public String SymptomsFinalTxt = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_symptom_results, null);

        // DB helper
        SymptomDbHelper dm = new SymptomDbHelper(getActivity());

        // TextView for results
        TextView textResults = dialogView.findViewById(R.id.results_textView);

        // Fetch and display symptom list
        List<Symptom> symptomList = dm.selectAll();
        String list = "";
        for (Symptom symptom : symptomList) {
            list += String.format("%s - %s (Pain: %d) - %s\n",
                    symptom.getBodyPart(), symptom.getSymptomName(), symptom.getPainLevel(), symptom.getTimestamp());
        }

        textResults.setText(list);
        SymptomsFinalTxt = list;

        // Buttons
        Button btnBack = dialogView.findViewById(R.id.btn_SymptomFrag);
        Button btnSave = dialogView.findViewById(R.id.btn_Save);
        Button btnDelete = dialogView.findViewById(R.id.btn_delete);

        // Back button
        btnBack.setOnClickListener(v -> dismiss());

        // Save button
        btnSave.setOnClickListener(v -> saveResultsToFile());

        // ✅ Delete button (clears DB + TextView)
        btnDelete.setOnClickListener(v -> {
            dm.deleteAll(); // Delete all data from DB
            textResults.setText(""); // Clear TextView
            Toast.makeText(getContext(), "All symptom logs deleted", Toast.LENGTH_SHORT).show();
        });

        builder.setView(dialogView).setMessage("Logged Symptoms");

        return builder.create();
    }

    private void saveResultsToFile() {
        try {
            String fileName = "symptom_results_" + System.currentTimeMillis() + ".txt";
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
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
