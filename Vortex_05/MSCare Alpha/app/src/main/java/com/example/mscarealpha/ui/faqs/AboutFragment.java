package com.example.mscarealpha.ui.faqs;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mscarealpha.R;
import com.example.mscarealpha.ui.medtrack.MedTrackFragment;

public class AboutFragment extends DialogFragment {

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_about, null);


        Button btnOK = dialogView.findViewById(R.id.btnAboutOK);
        Button btnAboutSourceCode = dialogView.findViewById(R.id.btnAboutSourceCode);


        builder.setView(dialogView).

                setMessage("About");

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {


                dismiss();

            }

        });

        btnAboutSourceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://github.com/SaskPolytechBIS/253project-bisfinal-ms-care-app";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        return builder.create();
    }
}

