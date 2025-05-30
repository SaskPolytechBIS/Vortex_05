package com.example.mscarenew.ui.medtrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mscarenew.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewMedTrackFragment extends DialogFragment {

    private EditText editMed;
    private EditText editDosage;
    private EditText editTimes;
    private EditText editFoods;
    private EditText editDrinks;
    private EditText editTextReminderMessage;
    private Button btnSetReminder;
    private EditText editDate;
    private List<CheckBox> dayCheckBoxes;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_medtrack, null);

        editMed = dialogView.findViewById(R.id.editMed);
        editDosage = dialogView.findViewById(R.id.editDosage);
        editTimes = dialogView.findViewById(R.id.editTimes);
        editFoods = dialogView.findViewById(R.id.editFoods);
        editDrinks = dialogView.findViewById(R.id.editDrinks);
        editTextReminderMessage = dialogView.findViewById(R.id.editTextReminderMessage);
        editDate = dialogView.findViewById(R.id.txtDate);
        btnSetReminder = dialogView.findViewById(R.id.btn_set_reminder);

        String dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new java.util.Date());
        editDate.setText(dateFormat);

        dayCheckBoxes = new ArrayList<>();
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_monday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_tuesday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_wednesday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_thursday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_friday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_saturday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_sunday));

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave   = dialogView.findViewById(R.id.btnSave);

        builder.setView(dialogView)
                .setMessage("Add new medication");

        btnCancel.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
            com.example.mscarenew.ui.medtrack.MedTrack newMed = new com.example.mscarenew.ui.medtrack.MedTrack();
            newMed.setMed(editMed.getText().toString());
            newMed.setDos(editDosage.getText().toString());
            newMed.setTimes(editTimes.getText().toString());
            newMed.setFoods(editFoods.getText().toString());
            newMed.setDrinks(editDrinks.getText().toString());
            newMed.setDate2(editDate.getText().toString());

            MedTrackFragment callingActivity = (MedTrackFragment) getParentFragment();
            callingActivity.createNewMed(newMed);
            dismiss();
        });

        btnSetReminder.setOnClickListener(v -> showTimePickerDialog());
        return builder.create();
    }

    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(
                requireContext(),
                (TimePicker tp, int h, int m) -> setReminder(h, m),
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void setReminder(int hourOfDay, int minute) {
        String message = editTextReminderMessage.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a reminder message", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        intent.putExtra("EXTRA_REMINDER_MESSAGE", message);

        PendingIntent pi = PendingIntent.getBroadcast(
                getActivity(),
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(),
                    pi
            );
            Toast.makeText(getContext(),
                    String.format(Locale.getDefault(), "Reminder set for %02d:%02d", hourOfDay, minute),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}

