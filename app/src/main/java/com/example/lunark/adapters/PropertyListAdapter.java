package com.example.lunark.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lunark.R;
import com.example.lunark.models.Property;

import java.util.List;

public class PropertyListAdapter extends BaseAdapter {
    private Activity activity;
    private final List<Property> properties;

    public PropertyListAdapter(Activity activity, List<Property> properties) {
        this.activity = activity;
        this.properties = properties;
    }

    @Override
    public int getCount() {
        return properties.size();
    }

    @Override
    public Object getItem(int position) {
        return properties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Property property = properties.get(position);

        if (convertView == null)
            vi = activity.getLayoutInflater().inflate(R.layout.property_card, null);

        TextView name = (TextView) vi.findViewById(R.id.name);
        TextView location = (TextView) vi.findViewById(R.id.location);
        TextView description = (TextView) vi.findViewById(R.id.description);
        TextView price = (TextView) vi.findViewById(R.id.price);
        ImageView thumbnail = (ImageView) vi.findViewById(R.id.thumbnail);

        name.setText(property.getName());
        location.setText(property.getLocation());
        description.setText(property.getDescription());
        price.setText(String.format("$%.0f", property.getPrice()));
        thumbnail.setImageResource(property.getThumbnailId());

        return vi;
    }
}

