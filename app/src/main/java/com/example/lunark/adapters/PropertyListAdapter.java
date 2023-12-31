package com.example.lunark.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lunark.R;
import com.example.lunark.models.Property;
import com.example.lunark.models.PropertyImage;
import com.example.lunark.util.ClientUtils;

import org.w3c.dom.Text;

import java.util.List;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.ViewHolder> {
    private Fragment fragment;
    private List<Property> properties;

    public PropertyListAdapter(Fragment fragment, List<Property> properties) {
        this.fragment = fragment;
        this.properties = properties;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.property_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = properties.get(position);

        holder.getName().setText(property.getName());
        holder.getDescription().setText(property.getDescription());
        holder.getLocation().setText(property.getAddress().getCity() + ", " + property.getAddress().getCountry());

        if (property.getImages().size() > 0) {
            PropertyImage image = property.getImages().get(0);
            Glide.with(fragment)
                    .load(ClientUtils.SERVICE_API_PATH + "properties/" + property.getId() + "/images/" + image.getId())
                    .into(holder.getThumbnail());
        }
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumbnail;
        private final TextView name;
        private final TextView location;
        private final TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            name = (TextView) itemView.findViewById(R.id.name);
            location = (TextView) itemView.findViewById(R.id.location);
            description = (TextView) itemView.findViewById(R.id.description);
        }

        public ImageView getThumbnail() {
            return thumbnail;
        }

        public TextView getName() {
            return name;
        }

        public TextView getLocation() {
            return location;
        }

        public TextView getDescription() {
            return description;
        }
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}

