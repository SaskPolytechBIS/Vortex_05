package com.example.mscarealpha.ui.medtrack;


import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.example.mscarealpha.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MedTrackFragment extends Fragment{

    private JSONSerializer mSerializer;
    private List<MedTrack> medTrackList = new ArrayList<>();

    private RecyclerView recyclerView;

    private MedTrackAdapter mAdapter;

    private SharedPreferences mPrefs;


    public static MedTrackFragment newInstance() {
        return new MedTrackFragment();
    }

    OvershootInterpolator interpolator = new OvershootInterpolator();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medtrack, container, false);

        FloatingActionButton fabAdd, fabDel, fabOpenClose, fabReminders;

        fabDel = view.findViewById(R.id.fab_clear_all);

        fabOpenClose = view.findViewById(R.id.fab_open_close);

        fabAdd = view.findViewById(R.id.fab_add_medtrack);

//        fabReminders = view.findViewById(R.id.fab_reminders);

        TextView viewDel = view.findViewById(R.id.viewDel);

        TextView viewAdd = view.findViewById(R.id.viewAdd);

//        TextView viewReminders = view.findViewById(R.id.viewReminders);

        Float translationYaxis = 100f;

        final Boolean[] menuOpen = {false};

        fabDel.setAlpha(0f);
        fabAdd.setAlpha(0f);
        viewDel.setAlpha(0f);
        viewAdd.setAlpha(0f);


        fabDel.setTranslationY(translationYaxis);
        fabAdd.setTranslationY(translationYaxis);
        viewDel.setTranslationY(translationYaxis);
        viewAdd.setTranslationY(translationYaxis);


        fabOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuOpen[0]){
                    CloseMenu();
                }
                else{
                    OpenMenu();
                }
            }

            private void OpenMenu() {
                menuOpen[0] = !menuOpen[0];
                fabOpenClose.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                fabDel.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                viewDel.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                fabAdd.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                viewAdd.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                fabReminders.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                viewReminders.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
            }

            private void CloseMenu() {
                menuOpen[0] = !menuOpen[0];
                fabOpenClose.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                fabDel.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                viewDel.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                fabAdd.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                viewAdd.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                fabReminders.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                viewReminders.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewMedTrackFragment dialog = new NewMedTrackFragment();
                dialog.show(getChildFragmentManager(), "");
            }
        });

        fabDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showClearAllMedTrackConfirmationDialog();
            }
        }
        );


//        fabReminders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navigateToReminderSettings();
//            }
//        });




        mSerializer = new JSONSerializer("NoteToSelf.json", requireContext());

        try {
            medTrackList = mSerializer.load();
        } catch (Exception e) {
            medTrackList = new ArrayList<MedTrack>();
            Log.e("Error loading notes: ", "", e);
        }

        recyclerView =
                view.findViewById(R.id.recyclerView);

        mAdapter = new MedTrackAdapter(this, medTrackList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


// set the adapter

        recyclerView.setAdapter(mAdapter);



        return view;

    }
    private void navigateToReminderSettings() {
        ReminderNotificationsFragment reminderFragment = new ReminderNotificationsFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, reminderFragment)  // Assume 'container' is the ID of your FrameLayout where fragments are swapped
                .addToBackStack(null)
                .commit();
    }

    private void showClearAllMedTrackConfirmationDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete all medications? This action cannot be undone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        medTrackList.clear();
                        mAdapter.notifyDataSetChanged();
                    }


                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }

    public void createNewMed(MedTrack mt){
        // Temporary code
        // mTempNote = n;
        medTrackList.add(mt);

        mAdapter.notifyDataSetChanged();
    }

    public void onResume() {
        super.onResume();
        mPrefs = requireActivity().getSharedPreferences("Note to self", MODE_PRIVATE);
    }

    public void saveNotes(){
        try{
            mSerializer.save(medTrackList);

        }catch(Exception e){
            Log.e("Error Saving Notes","", e);
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        saveNotes();

    }

    public void remove(MedTrack mt) {
        medTrackList.remove(mt);

        mAdapter.notifyDataSetChanged();
        saveNotes();
    }


    public void showMed(int noteToShow){

        ShowMedFragment dialog = new ShowMedFragment();

        dialog.sendMedTrackSelected(medTrackList.get(noteToShow));

        dialog.show(getChildFragmentManager(), "");

    }




    }

