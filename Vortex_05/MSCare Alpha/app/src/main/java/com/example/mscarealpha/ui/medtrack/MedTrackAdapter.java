package com.example.mscarealpha.ui.medtrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mscarealpha.R;

import java.util.List;

public class MedTrackAdapter extends RecyclerView.Adapter<MedTrackAdapter.ListItemHolder> {

    private List<MedTrack> mMedList;

    private MedTrackFragment mMedTrackFragment;

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        MedTrack medTrack = mMedList.get(position);

        holder.mMed.setText(medTrack.getMed());

        holder.mDos.setText(medTrack.getDos());

        holder.mTimes.setText(medTrack.getTimes());

        holder.mDate.setText(medTrack.getDate2());


        // Show the first 15 characters of the actual note

        // Unless a short note then show half
    }

    @Override
    public int getItemCount() {
        return mMedList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mMed;
        TextView mDos;
        TextView mTimes;
        TextView mDate;


        public ListItemHolder(View view) {

            super(view);

            mMed =view.findViewById(R.id.txtViewMed);

            mDos=view.findViewById(R.id.txtViewDosage);

            mTimes=view.findViewById(R.id.txtViewTimes);

            mDate=view.findViewById(R.id.txtMedDate);

           view.setClickable(true);

          view.setOnClickListener(this);
        }

       @Override

        public void onClick(View view) { mMedTrackFragment.showMed(getAdapterPosition());}
    }
    public MedTrackAdapter(MedTrackFragment medTrackFragment,

                       List<MedTrack> medTrackList) {



        mMedTrackFragment = medTrackFragment;

        mMedList = medTrackList;

    }
}
