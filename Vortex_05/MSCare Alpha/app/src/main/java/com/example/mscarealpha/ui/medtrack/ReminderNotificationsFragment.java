package com.example.mscarealpha.ui.medtrack;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mscarealpha.R;

import java.util.Calendar;
import java.util.Objects;

import android.widget.EditText; // Add this import for EditText

public class ReminderNotificationsFragment extends Fragment {

    private Button btnSetReminder;
    private EditText editTextReminderMessage; // Reference for EditText

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_notifications, container, false);

        btnSetReminder = view.findViewById(R.id.btn_set_reminder);
        editTextReminderMessage = view.findViewById(R.id.editTextReminderMessage); // Connect to EditText


        btnSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        return view;
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

    @SuppressLint("ScheduleExactAlarm")
    private void setReminder(int hourOfDay, int minute) {
        String reminderMessage = editTextReminderMessage.getText().toString(); // Get the custom message

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        try {
            AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), AlertReceiver.class);
            intent.putExtra("EXTRA_REMINDER_MESSAGE", reminderMessage); // Pass the message
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

            Toast.makeText(getActivity(), "Reminder set for " + hourOfDay + ":" + formatMinute(minute), Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Log.e("ReminderNotificationsFragment", "Error accessing the activity", e);
        }
    }


    private String formatMinute(int minute) {
        return minute < 10 ? "0" + minute : String.valueOf(minute);
    }

}
