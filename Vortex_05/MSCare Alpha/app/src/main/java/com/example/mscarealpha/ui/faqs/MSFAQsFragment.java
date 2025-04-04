package com.example.mscarealpha.ui.faqs;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.example.mscarealpha.R;


public class MSFAQsFragment extends Fragment {
    OvershootInterpolator interpolator = new OvershootInterpolator();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faqs, container, false);

        CardView cvMSFAQs = view.findViewById(R.id.cvMSFAQs);
        CardView cvContact = view.findViewById(R.id.cvContact);
        CardView cvCall = view.findViewById(R.id.cvCall);
        CardView cvAbout = view.findViewById(R.id.cvAbout);

        cvMSFAQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://mscanada.ca/common-questions";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        cvContact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String url = "https://www.canada.ca/en/public-health/services/mental-health-services/mental-health-get-help.html";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        cvCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:988"));
                startActivity(intent);
            }
        });

        cvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("About \n\nMS Care Release Candidate\nVersion 1.0 Build 300\n© 2024 Saskatchewan Polytechnic\n\n\nStaff Credit:\n\nProject Lead\nAlvi Najiyat\n(najiyat7041@saskpolytech.ca) \n\nProgrammer\nBimal Kaji Shiwakoti (shiwakoti7364@saskpolytech.ca)\n\nProject Analyst\nGladys Pormento (pormento4957@saskpolytech.ca)\n\nMedia Manager & Additional Programming\nJorge Carretero (carretero3453@saskpolytech.ca)")
                        .setPositiveButton("Source Code", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "https://github.com/SaskPolytechBIS/253project-bisfinal-ms-care-app";

                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }


                        })
                        .setNegativeButton("OK", null)
                        .create()
                        .show();
            }
        });



//        WebView webview = view.findViewById(R.id.webView);
//        webview.setWebViewClient(new WebViewClient());
//        webview.loadUrl("https://mscanada.ca/common-questions");
//
//        FloatingActionButton fabAbout = view.findViewById(R.id.fabAbout);
//        FloatingActionButton fabOpenClose = view.findViewById(R.id.fab_open_close_2);
//        FloatingActionButton fabCall = view.findViewById(R.id.fab_call);
//        FloatingActionButton fabContact = view.findViewById(R.id.fab_contact);
//        TextView viewContact = view.findViewById(R.id.viewContact);
//        TextView viewCall = view.findViewById(R.id.viewCall);
//        TextView viewAbout = view.findViewById(R.id.viewAbout);
//
//        Float translationYaxis = 100f;
//
//        final Boolean[] menuOpen = {false};
//
//
//        fabAbout.setAlpha(0f);
//        fabCall.setAlpha(0f);
//        fabContact.setAlpha(0f);
//        viewContact.setAlpha(0f);
//        viewCall.setAlpha(0f);
//        viewAbout.setAlpha(0f);
//
//
//
//        fabAbout.setTranslationY(translationYaxis);
//        fabCall.setTranslationY(translationYaxis);
//        fabContact.setTranslationY(translationYaxis);
//        viewContact.setAlpha(0f);
//        viewCall.setAlpha(0f);
//        viewAbout.setAlpha(0f);
//
//        fabOpenClose.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//              if (menuOpen[0]) {
//                CloseMenu();
//              } else {
//              OpenMenu();
//              }
//              }
//
//            private void OpenMenu() {
//                menuOpen[0] = !menuOpen[0];
//                fabOpenClose.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
//                fabAbout.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                viewAbout.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                fabCall.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                viewCall.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                fabContact.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                viewContact.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//            }
//
//            private void CloseMenu() {
//                menuOpen[0] = !menuOpen[0];
//                fabOpenClose.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
//                fabAbout.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                viewAbout.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                fabCall.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                viewCall.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                fabContact.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                viewContact.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//            }
//        });
//
//        fabContact.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                String url = "https://www.canada.ca/en/public-health/services/mental-health-services/mental-health-get-help.html";
//
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
//            }
//        });
//
//
//        fabCall.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:988"));
//                startActivity(intent);
//            }
//        });
//
//
//
//        fabAbout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AboutFragment dialog = new AboutFragment();
//                dialog.show(getChildFragmentManager(), "");
//            }
//        });



        return view;


    }


}