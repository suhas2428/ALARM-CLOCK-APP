package com.example.alarmapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {
    private List<TimeEntry> timeList;
    private OnTimeEntryClickListener listener;

    public interface OnTimeEntryClickListener {
        void onToggleClick(int position);
        void onDeleteClick(int position);
    }

    public TimeAdapter(List<TimeEntry> timeList, OnTimeEntryClickListener listener) {
        this.timeList = timeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        TimeEntry entry = timeList.get(position);
        holder.timeTextView.setText(entry.getTime());
        holder.toggleSwitch.setChecked(entry.isActive());
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        Switch toggleSwitch;
        Button deleteButton;

        TimeViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            toggleSwitch = itemView.findViewById(R.id.toggleSwitch);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            toggleSwitch.setOnClickListener(v -> listener.onToggleClick(getAdapterPosition()));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(getAdapterPosition()));
        }
    }
}
