package com.example.mscarealpha.ui.medtrack;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.mscarealpha.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class NewMedTrackFragment extends DialogFragment {

    private EditText editMed;
    private EditText editDosage;
    private EditText editTimes;
    private EditText editFoods;
    private EditText editDrinks;
    private EditText editTextReminderMessage;
    private Button btnSetReminder;
    private List<CheckBox> dayCheckBoxes;

    private EditText editDate;

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

        dayCheckBoxes = new ArrayList<>();
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_monday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_tuesday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_wednesday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_thursday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_friday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_saturday));
        dayCheckBoxes.add(dialogView.findViewById(R.id.check_sunday));


        String dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        //Set the formatted date to the TextView
        editDate.setText(dateFormat);

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        builder.setView(dialogView).setMessage("Add new medication");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedTrack newMed = new MedTrack();
                newMed.setMed(editMed.getText().toString());
                newMed.setDos(editDosage.getText().toString());
                newMed.setTimes(editTimes.getText().toString());
                newMed.setFoods(editFoods.getText().toString());
                newMed.setDrinks(editDrinks.getText().toString());
                newMed.setDate2(editDate.getText().toString());

                MedTrackFragment callingActivity = (MedTrackFragment) getParentFragment();
                callingActivity.createNewMed(newMed);

                dismiss();
            }
        });

        btnSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        return builder.create();
    }

    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setReminder(hourOfDay, minute);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void setReminder(int hourOfDay, int minute) {
        String reminderMessage = editTextReminderMessage.getText().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        List<Integer> selectedDays = new ArrayList<>();
        if (dayCheckBoxes.get(0).isChecked()) selectedDays.add(Calendar.MONDAY);
        if (dayCheckBoxes.get(1).isChecked()) selectedDays.add(Calendar.TUESDAY);
        if (dayCheckBoxes.get(2).isChecked()) selectedDays.add(Calendar.WEDNESDAY);
        if (dayCheckBoxes.get(3).isChecked()) selectedDays.add(Calendar.THURSDAY);
        if (dayCheckBoxes.get(4).isChecked()) selectedDays.add(Calendar.FRIDAY);
        if (dayCheckBoxes.get(5).isChecked()) selectedDays.add(Calendar.SATURDAY);
        if (dayCheckBoxes.get(6).isChecked()) selectedDays.add(Calendar.SUNDAY);

        if (selectedDays.isEmpty()) {
            Toast.makeText(getActivity(), "Please select at least one day of the week.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int day : selectedDays) {
            setRepeatingAlarm(day, hourOfDay, minute, reminderMessage);
        }

        Toast.makeText(getActivity(), "Reminder set for selected days at " + hourOfDay + ":" + formatMinute(minute), Toast.LENGTH_SHORT).show();
    }

    private void setRepeatingAlarm(int dayOfWeek, int hourOfDay, int minute, String reminderMessage) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        intent.putExtra("EXTRA_REMINDER_TYPE", "MEDICATION");
        intent.putExtra("EXTRA_REMINDER_MESSAGE", reminderMessage);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        }
    }

    private String formatMinute(int minute) {
        return minute < 10 ? "0" + minute : String.valueOf(minute);
    }
}
