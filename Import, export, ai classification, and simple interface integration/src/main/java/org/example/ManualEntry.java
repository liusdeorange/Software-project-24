package org.example;

public class ManualEntry {
    public Entry createManualEntry(String date, double amount, String category, String description) {
        return new Entry(date, amount, category, description);
    }
}
