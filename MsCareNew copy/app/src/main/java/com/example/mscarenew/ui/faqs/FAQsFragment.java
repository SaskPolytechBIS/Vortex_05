package com.example.mscarenew.ui.faqs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mscarenew.R;

public class FAQsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_faqs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText questionInput = view.findViewById(R.id.etQuestion);
        Button submitBtn = view.findViewById(R.id.btnSubmitQuestion);
        Button onlineFaqsBtn = view.findViewById(R.id.btnViewOnlineFaqs);

        submitBtn.setOnClickListener(v -> {
            String question = questionInput.getText().toString().trim();
            if (question.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your question first", Toast.LENGTH_SHORT).show();
                return;
            }
            // Send question via email
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:support@mscareapp.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FAQ Question");
            emailIntent.putExtra(Intent.EXTRA_TEXT, question);
            if (emailIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(getContext(), "No email app found", Toast.LENGTH_SHORT).show();
            }
        });

        onlineFaqsBtn.setOnClickListener(v -> {
            String url = "https://www.mscareapp.com/faqs";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}
