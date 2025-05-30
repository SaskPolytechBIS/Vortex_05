package com.example.mscarenew.ui.medtrack;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mscarenew.R;
import com.example.mscarenew.ui.notes.ReminderBroadcastReceiver;

import java.util.Calendar;

public class ReminderNotificationsFragment extends Fragment {

    private EditText editTextReminderMessage;
    private Button btnSetReminder;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_reminder_notifications,
                container,
                false
        );

        // Bind views
        editTextReminderMessage = view.findViewById(R.id.editTextReminderMessage);
        btnSetReminder = view.findViewById(R.id.btn_set_reminder);

        // Show time picker when clicked
        btnSetReminder.setOnClickListener(v -> showTimePickerDialog());

        return view;
    }

    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        new TimePickerDialog(
                requireContext(),
                (TimePicker tp, int h, int m) -> setReminder(h, m),
                hour, minute, true
        ).show();
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setReminder(int hourOfDay, int minute) {
        String message = editTextReminderMessage.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(getContext(),
                    "Please enter a reminder message",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Build calendar for chosen time
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);

        AlarmManager alarmManager =
                (AlarmManager) requireActivity()
                        .getSystemService(Context.ALARM_SERVICE);

        // Use your actual BroadcastReceiver class here:
        Intent intent = new Intent(getActivity(), ReminderBroadcastReceiver.class);
        intent.putExtra("EXTRA_REMINDER_MESSAGE", message);

        PendingIntent pi = PendingIntent.getBroadcast(
                getActivity(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(),
                    pi
            );
        }

        Toast.makeText(
                getContext(),
                "Reminder set for "
                        + hourOfDay
                        + ":"
                        + (minute < 10 ? "0"+minute : minute),
                Toast.LENGTH_SHORT
        ).show();
    }
}
