package com.example.lunark.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.repositories.PropertyRepository;
import com.example.lunark.models.Property;
import com.example.lunark.models.PropertyImage;
import com.example.lunark.util.ClientUtils;

import java.util.List;

import javax.inject.Inject;

public class UnapprovedPropertiesListAdapter extends RecyclerView.Adapter<UnapprovedPropertiesListAdapter.ViewHolder> {
    private Fragment fragment;
    private List<Property> properties;

    @Inject
    PropertyRepository propertyRepository;

    public UnapprovedPropertiesListAdapter(Fragment fragment, List<Property> properties) {
        this.fragment = fragment;
        ((LunarkApplication) fragment.getActivity().getApplication()).applicationComponent.inject(this);
        this.properties = properties;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unapproed_property_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = properties.get(position);
        Long propertyId = property.getId();
        holder.getName().setText(property.getName().toString());
        holder.getLocation().setText(property.getAddress().getCity().toString() + ", " + property.getAddress().getCity().toString());

        if (property.getImages().size() > 0) {
            PropertyImage image = property.getImages().get(0);
            Glide.with(fragment)
                    .load(ClientUtils.SERVICE_API_PATH + "properties/" + property.getId() + "/images/" + image.getId())
                    .into(holder.getThumbnail());
        }

        holder.getBtnApprove().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveReservation(propertyId);
                Toast.makeText(fragment.getContext(), new String("Property approved!"), Toast.LENGTH_SHORT).show();
                properties.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });


        holder.getBtnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("propertyId", property.getId());
                Log.d("RecyclerViewOnClick", property.getId().toString());

                fragment.getParentFragmentManager().setFragmentResult("selectedProperty", bundle);
            }
        });
    }

    public void approveReservation(long propertyId) {
        propertyRepository.approveProperty(propertyId);
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumbnail;
        private final TextView name;
        private final TextView location;

        private final Button btnApprove;
        private final Button btnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            name = (TextView) itemView.findViewById(R.id.titleTextView);
            location = (TextView) itemView.findViewById(R.id.locationTextView);
            btnApprove = (Button)  itemView.findViewById(R.id.approveButton);
            btnView = (Button)  itemView.findViewById(R.id.viewButton);
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

        public Button getBtnApprove() {
            return btnApprove;
        }

        public Button getBtnView() {
            return btnView;
        }
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}

