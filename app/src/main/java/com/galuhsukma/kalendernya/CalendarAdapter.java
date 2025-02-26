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
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;

    private List<String> listHaid, listIstihadhah;
    private final OnItemListener onItemListener;
    private int selectedPosition = -1; // -1 artinya belum ada yang dipilih
    private String currentMonthYear; // Format: "yyyy-MM"

    public CalendarAdapter(ArrayList<String> daysOfMonth, List<String> listHaid, List<String> listIstihadhah, OnItemListener onItemListener, String currentMonthYear) {
        this.daysOfMonth = daysOfMonth;
        this.listHaid = listHaid;
        this.listIstihadhah = listIstihadhah;
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
        Context context = holder.itemView.getContext();
        LocalDate hariIni = LocalDate.now();
        String today = hariIni.toString();

        if (!dayText.isEmpty()) {
            String day = dayText;
            // Ambil bagian hari saja, yaitu setelah karakter "-"
            String visibleDay = day.substring(day.lastIndexOf("-") + 1);
            holder.dayOfmonthDisplay.setText(visibleDay);
            holder.dayOfmonth.setText(day);
            if (position == selectedPosition) {
                // Warnai sebagai penanda (misalnya kuning)
                holder.dayOfmonthDisplay.setBackgroundColor(ContextCompat.getColor(context, R.color.pinkD));
                holder.dayOfmonthDisplay.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else if(today.equals(dayText) && (!listHaid.contains(dayText) && !listIstihadhah.contains(dayText))){
                holder.dayOfmonthDisplay.setTextColor(ContextCompat.getColor(context, R.color.yellow));}
            else if(listHaid.contains(dayText)){
                holder.dayOfmonthDisplay.setBackgroundColor(ContextCompat.getColor(context, R.color.greenL));
                holder.dayOfmonthDisplay.setTextColor(ContextCompat.getColor(context, R.color.white));}
            else if(listIstihadhah.contains(dayText)){
                holder.dayOfmonthDisplay.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                holder.dayOfmonthDisplay.setTextColor(ContextCompat.getColor(context, R.color.white));
            }else {
                // Tampilan default
                holder.dayOfmonthDisplay.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                holder.dayOfmonthDisplay.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        } else {
            // Sel kosong, misalnya tidak ada angka
            holder.dayOfmonthDisplay.setText("");
            holder.dayOfmonth.setText("");
            //kasih if else kalo dipencet nanti gaada apa/ atau nanti ada toastnya "pilih tanggal", kalo ga waktu dia mau klik tandai, kalo nilainya gaada, keluar toast
            holder.dayOfmonthDisplay.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            holder.dayOfmonthDisplay.setTextColor(ContextCompat.getColor(context, R.color.black));
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
