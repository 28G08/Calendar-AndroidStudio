package com.galuhsukma.kalendernya;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderViewHolder extends RecyclerView.ViewHolder{
    public final TextView judulreminder, tglreminder;
    public ReminderViewHolder(@NonNull View itemView) {
        super(itemView);
        judulreminder = itemView.findViewById(R.id.judulreminder);
        tglreminder = itemView.findViewById(R.id.tglreminder);
    }

    public void bind(ModelReminder modelReminder) {
        judulreminder.setText(modelReminder.getJenisReminder());
        tglreminder.setText(modelReminder.getTanggalReminder());
    }

}
