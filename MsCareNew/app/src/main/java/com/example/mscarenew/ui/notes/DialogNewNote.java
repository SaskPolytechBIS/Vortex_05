// DialogNewNote.java
package com.example.mscarenew.ui.notes;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.mscarenew.R;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DialogNewNote extends DialogFragment {

    private Button btnSetReminder;
    private EditText editTitle;
    private EditText editDescription;
    private RadioButton radioButtonQuestions;
    private RadioButton radioButtonNotes;
    private RadioButton radioButtonTodo;
    private TextView txtDate;

    private long selectedDateMillis = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_note, null);

        editTitle = dialogView.findViewById(R.id.editTitle);
        editDescription = dialogView.findViewById(R.id.editDescription);
        radioButtonQuestions = dialogView.findViewById(R.id.rbQuestions);
        radioButtonNotes = dialogView.findViewById(R.id.rbNotes);
        radioButtonTodo = dialogView.findViewById(R.id.rbTodo);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnOK = dialogView.findViewById(R.id.btnOK);
        btnSetReminder = dialogView.findViewById(R.id.btnSetReminder);

        txtDate = dialogView.findViewById(R.id.txtViewDate);
        txtDate.setVisibility(View.VISIBLE);

        String dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        txtDate.setText(dateFormat);

        txtDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select appointment date")
                    .build();

            datePicker.show(getParentFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDateMillis = selection;

                // Convert UTC date to local properly
                Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                utcCalendar.setTimeInMillis(selection);

                Calendar localCalendar = Calendar.getInstance();
                localCalendar.set(Calendar.YEAR, utcCalendar.get(Calendar.YEAR));
                localCalendar.set(Calendar.MONTH, utcCalendar.get(Calendar.MONTH));
                localCalendar.set(Calendar.DAY_OF_MONTH, utcCalendar.get(Calendar.DAY_OF_MONTH));

                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(localCalendar.getTime());

                txtDate.setText(formattedDate);
            });
        });

        builder.setView(dialogView).setMessage("Add a new note");

        btnCancel.setOnClickListener(v -> dismiss());

        btnOK.setOnClickListener(v -> {
            createNote();
            dismiss();
        });

        btnSetReminder.setOnClickListener(v -> showTimePickerDialog());

        return builder.create();
    }

    private void createNote() {
        AppointmentNotes newNote = new AppointmentNotes();
        newNote.setTitle(editTitle.getText().toString());
        newNote.setDescription(editDescription.getText().toString());
        newNote.setQuestions(radioButtonQuestions.isChecked());
        newNote.setNotes(radioButtonNotes.isChecked());
        newNote.setTodo(radioButtonTodo.isChecked());
        newNote.setDate(txtDate.getText().toString());

        AppointmentNotesFragment callingActivity = (AppointmentNotesFragment) getParentFragment();
        callingActivity.createNewNote(newNote);
    }

    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minute1) -> {
            setReminder(hourOfDay, minute1);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void setReminder(int hourOfDay, int minute) {
        String reminderTitle   = editTitle.getText().toString();
        String reminderMessage = editDescription.getText().toString();

        if (reminderTitle.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1) Build the calendar for the chosen date & time
        Calendar calendar = Calendar.getInstance();
        if (selectedDateMillis > 0) {
            calendar.setTimeInMillis(selectedDateMillis);
        } else {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        try {
            // 2) Grab the AlarmManager
            AlarmManager alarmManager = (AlarmManager) requireActivity()
                    .getSystemService(Context.ALARM_SERVICE);

            // 3) Create your broadcast Intent and **add your extras here**
            Intent intent = new Intent(getActivity(), ReminderBroadcastReceiver.class);
            intent.putExtra(ReminderBroadcastReceiver.EXTRA_REMINDER_TITLE,   reminderTitle);
            intent.putExtra(ReminderBroadcastReceiver.EXTRA_REMINDER_MESSAGE, reminderMessage);

            // 4) Wrap it in a PendingIntent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getContext(),
                    (int) System.currentTimeMillis(),  // unique request code
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // 5) Schedule it
            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
                Toast.makeText(
                        getActivity(),
                        "Reminder set for " + hourOfDay + ":" + formatMinute(minute),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                Toast.makeText(getActivity(), "Failed to set reminder", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to set reminder", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatMinute(int minute) {
        return minute < 10 ? "0" + minute : String.valueOf(minute);
    }
}
