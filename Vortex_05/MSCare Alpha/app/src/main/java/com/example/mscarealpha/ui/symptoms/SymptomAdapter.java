package com.example.mscarealpha.ui.symptoms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mscarealpha.MainActivity;
import com.example.mscarealpha.R;

import java.util.List;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.ListItemHolder>{

    private List<Symptom> mSymptomsList; // a reference to the data source
    private MainActivity mSymptomActivity; // a reference to the parent Activity



    public SymptomAdapter(List<Symptom> symptomsList, MainActivity symptomActivity) {
        mSymptomsList = symptomsList;
        mSymptomActivity = symptomActivity;
    }


    @NonNull
    @Override
    public SymptomAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflate the listitem layout into aan item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_symptoms,parent, false);



        // pass this view to create and return and instance of a ViewHolder (ListItemHolder)
        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SymptomAdapter.ListItemHolder holder, int position) {
        // get the Symptom object at the requested index/ position
        Symptom symptom = mSymptomsList.get(position);

        // Load the holder with the data from that symptom object
        holder.mSymptom.setText(symptom.getSymptomName()); // using the full symptom
        holder.mBodyPart.setText(symptom.getBodyPart());
        holder.mPainLevel.setText(symptom.getPainLevel());
    }

    @Override
    public int getItemCount() {
        // report the number of items in the datasource
        return mSymptomsList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Declare some widget variables to reference the layout
        TextView mSymptom;
        TextView mBodyPart;
        TextView mPainLevel;
        public ListItemHolder(@NonNull View itemView) {
            super(itemView);

            // instantiate the widget variables
            mSymptom = itemView.findViewById(R.id.symptom_menu);
            mBodyPart = itemView.findViewById(R.id.bodypart_dropdown);
            mPainLevel = itemView.findViewById(R.id.pain_range_bar);

            // make the whole view clickable
            itemView.setClickable(true);
            itemView.setOnClickListener(this); // use the ViewHolder's onClick
        }

        @Override
        public void onClick(View v) {
            // Called when the user taps an item in the RecyclerView


            // we will call a method on the MainActivity to show the tapped Symptom Items Details
            //mSymptomActivity.show();
        }
    }
}
