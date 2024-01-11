package com.example.lunark.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.R;
import com.example.lunark.models.AvailabilityEntry;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AvailabilityEntryAdapter extends RecyclerView.Adapter<AvailabilityEntryAdapter.ViewHolder> {
    List<AvailabilityEntry> availabilityEntries;
    List<AvailabilityTableRow> rows;
    public AvailabilityEntryAdapter(List<AvailabilityEntry> availabilityEntries) {
       this.availabilityEntries = new ArrayList<>();
       this.availabilityEntries.addAll(availabilityEntries);
       this.rows = this.convertAvailabilityEntriesToRows(this.availabilityEntries);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.availability_entry_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AvailabilityTableRow row = rows.get(position);
        holder.getFrom().setText(row.getFrom().toString());
        holder.getTo().setText(row.getTo().toString());
        holder.getPrice().setText(row.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return this.rows.size();
    }

    public void setAvailabilityEntries(List<AvailabilityEntry> availabilityEntries) {
        this.availabilityEntries.clear();
        this.availabilityEntries.addAll(availabilityEntries);
        this.rows = convertAvailabilityEntriesToRows(this.availabilityEntries);
        notifyDataSetChanged();
    }

    public List<AvailabilityEntry> getAvailabilityEntries() {
        return availabilityEntries;
    }

    public List<AvailabilityTableRow> convertAvailabilityEntriesToRows(List<AvailabilityEntry> entries) {
        if (entries.size() == 0) {
            return new ArrayList<>();
        }

        Collections.sort(entries, Comparator.comparing(e -> e.getDate()));

        List<AvailabilityTableRow> rows = new ArrayList<>();

        AvailabilityTableRow currentRow = new AvailabilityTableRow(entries.get(0).getDate(), entries.get(0).getDate(), entries.get(0).getPrice());

        for (int i = 1; i < entries.size(); i++) {
            AvailabilityEntry entry = entries.get(i);

            boolean samePrice = currentRow.getPrice().equals(entry.getPrice());

            boolean within24Hours = Math.abs(ChronoUnit.DAYS.between(currentRow.getTo(), entry.getDate())) <= 1;

            if (samePrice && within24Hours) {
                currentRow.setTo(entry.getDate());
            } else {
                rows.add(currentRow);
                currentRow = new AvailabilityTableRow(entry.getDate(), entry.getDate(), entry.getPrice());
            }
        }

        rows.add(currentRow);
        return rows;
    }

    public class AvailabilityTableRow {
        private LocalDate from;
        private LocalDate to;
        private Double price;

        public AvailabilityTableRow(LocalDate from, LocalDate to, Double price) {
            this.from = from;
            this.to = to;
            this.price = price;
        }

        public LocalDate getFrom() {
            return from;
        }

        public LocalDate getTo() {
            return to;
        }

        public Double getPrice() {
            return price;
        }

        public void setTo(LocalDate to) {
            this.to = to;
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView from;
        private final TextView to;
        private final TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from_text_view);
            to = itemView.findViewById(R.id.to_text_view);
            price = itemView.findViewById(R.id.price_text_view);
        }

        public TextView getFrom() {
            return from;
        }

        public TextView getTo() {
            return to;
        }

        public TextView getPrice() {
            return price;
        }
    }
}
