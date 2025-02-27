package com.galuhsukma.kalendernya;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder>{

    private final List<ModelReminder> modelReminderList;
    private final DatabaseHelper databaseHelper;
    private final Context context;
    public ReminderAdapter(Context context, List<ModelReminder> modelReminderList) {
        this.context = context;
        this.modelReminderList = modelReminderList;
        this.databaseHelper = new DatabaseHelper(context);
    }
    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_cell, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ModelReminder reminder = modelReminderList.get(position); // Ambil objek berdasarkan posisi
        holder.bind(reminder);

        // Tombol Hapus
        Button hapusButton = holder.itemView.findViewById(R.id.hapusrbtn);
        hapusButton.setOnClickListener(v -> {
            databaseHelper.deleteReminder(reminder.getTanggalReminder(), context);
            modelReminderList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, modelReminderList.size());
        });

        // Tombol Edit
        Button editButton = holder.itemView.findViewById(R.id.editrbtn);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, TambahReminder.class);
            intent.putExtra("jenisreminder", reminder.getJenisReminder()); // Menggunakan objek yang benar
            intent.putExtra("tglreminder", reminder.getTanggalReminder());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return modelReminderList.size();
    }
}
