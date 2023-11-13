package com.example.lunark.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lunark.R;
import com.example.lunark.models.Accommodation;
import com.example.lunark.util.AccommodationListMockup;

import org.w3c.dom.Text;

public class AccommodationListAdapter extends BaseAdapter {
    private Activity activity;

    public AccommodationListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return AccommodationListMockup.getAccommodations().size();
    }

    @Override
    public Object getItem(int position) {
        return AccommodationListMockup.getAccommodations().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Accommodation accommodation = AccommodationListMockup.getAccommodations().get(position);

        if (convertView == null)
            vi = activity.getLayoutInflater().inflate(R.layout.accommodation_card, null);

        TextView name = (TextView) vi.findViewById(R.id.name);
        TextView location = (TextView) vi.findViewById(R.id.location);
        TextView description = (TextView) vi.findViewById(R.id.description);
        TextView price = (TextView) vi.findViewById(R.id.price);
        ImageView thumbnail = (ImageView) vi.findViewById(R.id.thumbnail);

        name.setText(accommodation.getName());
        location.setText(accommodation.getLocation());
        description.setText(accommodation.getDescription());
        price.setText(String.format("$%.0f", accommodation.getPrice()));
        thumbnail.setImageResource(accommodation.getThumbnailId());

        return vi;
    }
}

