package com.galuhsukma.kalendernya;

public class ModelReminder {
    private String jenisReminder;
    private String tanggalReminder;

    public ModelReminder(String jenisReminder, String tanggalReminder) {
        this.jenisReminder = jenisReminder;
        this.tanggalReminder = tanggalReminder;
    }

    public String getJenisReminder() {
        return jenisReminder;
    }

    public String getTanggalReminder() {
        return tanggalReminder;
    }
}
