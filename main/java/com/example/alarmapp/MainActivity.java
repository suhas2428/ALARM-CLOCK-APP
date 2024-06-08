package com.example.alarmapp;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TimeAdapter.OnTimeEntryClickListener {

    private TextView clockTextView;
    private Button selectTimeButton;
    private RecyclerView timeRecyclerView;
    private List<TimeEntry> timeList;
    private TimeAdapter adapter;
    private Handler handler;
    private Runnable runnable;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clockTextView = findViewById(R.id.clockTextView);
        selectTimeButton = findViewById(R.id.selectTimeButton);
        timeRecyclerView = findViewById(R.id.timeRecyclerView);

        timeList = new ArrayList<>();
        adapter = new TimeAdapter(timeList, this);
        timeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timeRecyclerView.setAdapter(adapter);

        selectTimeButton.setOnClickListener(v -> openTimePicker());

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateClock();
                checkAlarms();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
    }

    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        clockTextView.setText(currentTime);
    }

    private void openTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            timeList.add(new TimeEntry(time, true));
            adapter.notifyDataSetChanged();
        }, 0, 0, true);
        timePickerDialog.show();
    }

    private void checkAlarms() {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        for (TimeEntry entry : timeList) {
            if (entry.isActive() && entry.getTime().equals(currentTime)) {
                mediaPlayer.start();
                showAlarmNotification();
                entry.setActive(false);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void showAlarmNotification() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.toggleSwitch), "Alarm!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", v -> mediaPlayer.pause())
                .setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void onToggleClick(int position) {
        TimeEntry entry = timeList.get(position);
        entry.setActive(!entry.isActive());
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onDeleteClick(int position) {
        timeList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        mediaPlayer.release();
        super.onDestroy();
    }
}
