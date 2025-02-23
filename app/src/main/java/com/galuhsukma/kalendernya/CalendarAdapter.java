package com.galuhsukma.kalendernya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;

    private List<String> markedDates;
    private final OnItemListener onItemListener;
    private int selectedPosition = -1; // -1 artinya belum ada yang dipilih
    private String currentMonthYear; // Format: "yyyy-MM"

    public CalendarAdapter(ArrayList<String> daysOfMonth, List<String> markedDates, OnItemListener onItemListener, String currentMonthYear) {
        this.daysOfMonth = daysOfMonth;
        this.markedDates = markedDates;
        this.onItemListener = onItemListener;
        this.currentMonthYear = currentMonthYear;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calender_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);
        holder.dayOfmonth.setText(dayText);
        Context context = holder.itemView.getContext();

        if (position == selectedPosition) {
            // 🔹 Jika tanggal dipilih, warnai pinkD (terlepas dari markedDates atau tidak)
            holder.dayOfmonth.setBackgroundColor(ContextCompat.getColor(context, R.color.pinkD));
            holder.dayOfmonth.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (markedDates.contains(dayText)) {
            // 🔹 Jika tanggal ada di markedDates tapi tidak dipilih, warnai kuning
            holder.dayOfmonth.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
            holder.dayOfmonth.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            // 🔹 Tampilan default (tidak dipilih & tidak ditandai)
            holder.dayOfmonth.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            holder.dayOfmonth.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            if (onItemListener != null) {
                onItemListener.onItemClick(position, dayText);
            }
        });
        if (!dayText.isEmpty()) {
            // Pastikan dayText selalu dua digit (misalnya "07" jika kurang dari 10)
            String formattedDay = dayText.length() == 1 ? "0" + dayText : dayText;
            // Misalnya currentMonthYear sudah dalam format "yyyy-MM"
            String fullDate = currentMonthYear + "-" + formattedDay; // hasil: "yyyy-MM-dd"

            if (markedDates.contains(fullDate)) {
                // Warnai sebagai penanda (misalnya kuning)
                holder.dayOfmonth.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
                holder.dayOfmonth.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else if (position == selectedPosition) {
                // 🔹 Jika tanggal dipilih, warnai pinkD (terlepas dari markedDates atau tidak)
                holder.dayOfmonth.setBackgroundColor(ContextCompat.getColor(context, R.color.pinkD));
                holder.dayOfmonth.setTextColor(ContextCompat.getColor(context, R.color.white));}
            else {
                // Tampilan default
                holder.dayOfmonth.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                holder.dayOfmonth.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        } else {
            // Sel kosong, misalnya tidak ada angka
            holder.dayOfmonth.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            holder.dayOfmonth.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            if (onItemListener != null) {
                onItemListener.onItemClick(position, dayText);
            }
        });
    }

        @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }
    public interface OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
