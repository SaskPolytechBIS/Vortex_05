package com.example.mscarealpha.ui.notes;


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

public class AppointmentNotesFragment extends Fragment {

    private List<AppointmentNotes> noteList = new ArrayList<>();

    private NoteAdapter mAdapter;

    private JSONSerializerForNotes mSerializer;

    private SharedPreferences mPrefs;

    private RecyclerView recyclerView;

    public static AppointmentNotesFragment newInstance() {
        return new AppointmentNotesFragment();
    }

    OvershootInterpolator interpolator = new OvershootInterpolator();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        FloatingActionButton fabAdd_Notes = view.findViewById(R.id.fabAdd_Notes);
        FloatingActionButton fabDel_Notes = view.findViewById(R.id.fabDel_Notes);
        FloatingActionButton fabOpenClose = view.findViewById(R.id.fab_open_close2);

        TextView viewDel = view.findViewById(R.id.viewDel2);
        TextView viewAdd = view.findViewById(R.id.viewAdd2);

        Float translationYaxis = 100f;

        final Boolean[] menuOpen = {false};

        fabDel_Notes.setAlpha(0f);
        fabAdd_Notes.setAlpha(0f);
        viewDel.setAlpha(0f);
        viewAdd.setAlpha(0f);

        fabDel_Notes.setTranslationY(translationYaxis);
        fabAdd_Notes.setTranslationY(translationYaxis);
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
                fabDel_Notes.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                viewDel.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                fabAdd_Notes.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                viewAdd.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                fabReminders.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                viewReminders.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
            }

            private void CloseMenu() {
                menuOpen[0] = !menuOpen[0];
                fabOpenClose.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                fabDel_Notes.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                viewDel.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                fabAdd_Notes.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                viewAdd.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                fabReminders.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                viewReminders.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            }
        });

        fabAdd_Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewNote dialog = new DialogNewNote ();
                dialog.show(getChildFragmentManager(), "");
            }
        });

        fabDel_Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearAllConfirmationDialog();
            }
        });

        mSerializer = new JSONSerializerForNotes("AppointmentNotes.json", requireContext());

        try {
            noteList = mSerializer.load();
        } catch (Exception e) {
            noteList = new ArrayList<AppointmentNotes>();
            Log.e("Error loading notes: ", "", e);
        }
        recyclerView =
                view.findViewById(R.id.recyclerView);

        mAdapter = new NoteAdapter(this, noteList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


// set the adapter

        recyclerView.setAdapter(mAdapter);




        return view;


    }

    private void showClearAllConfirmationDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete all notes? This action cannot be undone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteList.clear();
                        mAdapter.notifyDataSetChanged();
                    }


                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }
    public void createNewNote(AppointmentNotes an){
        // Temporary code
        //mTempNote = n;

        noteList.add(an);
        mAdapter.notifyDataSetChanged();
    }
    public void onResume(){
        super.onResume();
        mPrefs = requireActivity().getSharedPreferences("Appointment Notes", MODE_PRIVATE);
    }

    public void update(){
        mAdapter.notifyDataSetChanged();
    }
    public void showNote(int noteToShow){
        DialogShowNote dialog = new DialogShowNote();
        dialog.sendNoteSelected(noteList.get(noteToShow));
        dialog.show(getChildFragmentManager(), "");
    }

    public void saveNotes(){
        try{
            mSerializer.save(noteList);

        }catch(Exception e){
            Log.e("Error Saving Notes","", e);
        }
    }


    @Override
    public void onPause(){
        super.onPause();

        saveNotes();

    }

    public void remove(AppointmentNotes an) {
        noteList.remove(an);

        mAdapter.notifyDataSetChanged();
        saveNotes();
    }
//    public void onNoteDeleted(AppointmentNotes note) {
//        noteList.remove(note);
//        mAdapter.notifyDataSetChanged();
//    }

}
