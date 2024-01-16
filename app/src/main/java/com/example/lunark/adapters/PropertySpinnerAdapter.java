package com.example.lunark.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lunark.models.Property;

import java.util.List;

public class PropertySpinnerAdapter extends ArrayAdapter<Property> {
    private final List<Property> mProperties;

    public PropertySpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Property> properties) {
        super(context, resource, properties);
        this.mProperties = properties;
    }

    @Override
    public int getCount() {
        return mProperties.size();
    }

    @Nullable
    @Override
    public Property getItem(int position) {
        return mProperties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mProperties.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(mProperties.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(mProperties.get(position).getName());
        return label;
    }
}
