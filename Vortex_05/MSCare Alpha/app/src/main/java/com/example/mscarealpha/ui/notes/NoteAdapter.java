package com.example.mscarealpha.ui.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mscarealpha.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ListItemHolder> {

    private List<AppointmentNotes> mNoteList;
    private AppointmentNotesFragment mAppointmentNotesFragment;

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem2, parent, false);

        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        AppointmentNotes note = mNoteList.get(position);

        holder.mTitle.setText(note.getTitle());

        holder.mDescription.setText(note.getDescription());

        holder.mDate.setText(note.getDate());


        // What is the status of the note?
        if(note.isQuestions()){
            holder.mStatus.setText(R.string.questions_text);
        }
        else if(note.isNotes()){
            holder.mStatus.setText(R.string.notes_text);
        }
        else if(note.isTodo()){
            holder.mStatus.setText(R.string.todo_text);
        }

    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitle;
        TextView mDescription;
        TextView mStatus;
        TextView mDate;


        public ListItemHolder(View view) {
            super(view);

            mTitle = 
                    view.findViewById(R.id.textViewTitle);

            mDescription = 
                    view.findViewById(R.id.textViewDescription);

            mStatus = 
                    view.findViewById(R.id.textViewStatus);

            mDate = view.findViewById(R.id.tvDate);


            view.setClickable(true);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mAppointmentNotesFragment.showNote(getAdapterPosition());
        }
    }

        public NoteAdapter (AppointmentNotesFragment appointmentNotesFragment, List<AppointmentNotes> appointmentNotesList){
            mAppointmentNotesFragment = appointmentNotesFragment;
            mNoteList = appointmentNotesList;
        }

    }



