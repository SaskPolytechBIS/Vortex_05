package com.example.mscarealpha.ui.medtrack;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mscarealpha.R;
import com.example.mscarealpha.ui.notes.AppointmentNotesFragment;

import java.util.ArrayList;
import java.util.List;

public class ShowMedFragment extends DialogFragment {

    private MedTrack mMedTrack;

    private List<MedTrack> medTrackList = new ArrayList<>();


    @Override

    public Dialog onCreateDialog(Bundle savedInstance){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_show_med, null);

        TextView viewMed = dialogView.findViewById(R.id.viewMed);

        TextView viewDos = dialogView.findViewById(R.id.viewDos);

        TextView viewTimes = dialogView.findViewById(R.id.viewTimes);

        TextView viewFood = dialogView.findViewById(R.id.viewFood);

        TextView viewDrink = dialogView.findViewById(R.id.viewDrink);

        TextView viewDate = dialogView.findViewById(R.id.viewShowDate);

        viewMed.setText(mMedTrack.getMed());

        viewDos.setText(mMedTrack.getDos());

        viewTimes.setText(mMedTrack.getTimes());

        viewFood.setText(mMedTrack.getFoods());

        viewDrink.setText(mMedTrack.getDrinks());

        viewDate.setText(mMedTrack.getDate2());

        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);

        Button btnDel = (Button) dialogView.findViewById(R.id.btnDel);

        builder.setView(dialogView).setMessage("Your Medicine");


        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelAlert();

            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dismiss();

            }

        });

        return builder.create();
    }

    private void showDelAlert() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete this Medication? This action cannot be undone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote();
                    }


                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }
    private void deleteNote() {
        // Get reference to the parent fragment/activity
        MedTrackFragment parentFragment = (MedTrackFragment) getParentFragment();

        // Call the remove method to delete the MedTrack object
        if (parentFragment != null) {
            parentFragment.remove(mMedTrack);
        }

        // Dismiss the dialog
        dismiss();
    }



    public void sendMedTrackSelected(MedTrack medTrackSelected) {

        mMedTrack = medTrackSelected;

    }
}
