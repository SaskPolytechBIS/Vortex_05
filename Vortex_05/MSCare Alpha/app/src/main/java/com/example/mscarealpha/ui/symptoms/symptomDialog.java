package com.example.mscarealpha.ui.symptoms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mscarealpha.R;
import com.example.mscarealpha.ui.home.HomeFragment;

public class symptomDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Using the Builder class because this dialog has a simple UI

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Using the builder to configure a simple Dialog object
        // It has 3 parts; Title, Body and the button
        builder.setTitle(R.string.notify_dialog_journaling_title); // Setting the title of the notification Dialog

        builder.setMessage("Symptom Successfully Logged"); // Setting the message body

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Called when the user clicks the Positive button
                // Add code to clear the selections and get back to the symptom screen)

                // When onClick completed, Dialog closes automatically
                // Reset the fragment and Navigate to navigation_symptoms_and_journaling
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_symptoms_journaling);

                }
        });

//        builder.setNegativeButton("Home", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Called when the user clicks the negative button
//                // Add code to get back to the HomeFragment
//
//                // When onClick completed, Dialog closes automatically
//                // Navigate to the HomeFragment
//                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
//                navController.navigate(R.id.navigation_home);
//
//            }
//        });

        // return the created Dialog object

        return builder.create();
    }


}


