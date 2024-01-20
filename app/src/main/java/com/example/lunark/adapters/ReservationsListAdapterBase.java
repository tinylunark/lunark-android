package com.example.lunark.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.fragments.AccountReportFragment;
import com.example.lunark.models.Profile;
import com.example.lunark.models.Property;
import com.example.lunark.models.PropertyImage;
import com.example.lunark.models.Reservation;
import com.example.lunark.models.ReservationStatus;
import com.example.lunark.repositories.ReservationRepository;
import com.example.lunark.util.ClientUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationsListAdapterBase extends RecyclerView.Adapter<ReservationsListAdapterBase.ViewHolder> {
    private Fragment fragment;
    private List<Reservation> reservations;

    @Inject
    ReservationRepository reservationRepository;

    public ReservationsListAdapterBase(Fragment fragment, List<Reservation> reservations) {
        this.fragment = fragment;
        ((LunarkApplication) fragment.getActivity().getApplication()).applicationComponent.inject(this);
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_card_base, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Long propertyId = reservation.getPropertyId();
        Profile profile = reservation.getGuest();

        holder.getPropertyName().setText(reservation.getPropertyName());
        holder.getDateRange().setText(dateFormat.format(reservation.getStartDate()) + " - " + dateFormat.format(reservation.getEndDate()));
        holder.getPrice().setText("Price: $" + String.valueOf(reservation.getPrice()));
        holder.getGuestName().setText(profile.getName() + " " + profile.getSurname());
        holder.getStatus().setText("Status: " + reservation.getStatus());

        fetchPropertyData(propertyId, new PropertyDataCallback() {
            @Override
            public void onPropertyDataFetched(Property property) {
                if (property.getImages().size() > 0) {
                    PropertyImage image = property.getImages().get(0);
                    Glide.with(fragment)
                            .load(ClientUtils.SERVICE_API_PATH + "properties/" + property.getId() + "/images/" + image.getId())
                            .into(holder.getPropertyImage());
                }
            }

            @Override
            public void onPropertyDataFetchFailed() {
            }
        });

        setUpReportButton(holder, reservation);
    }

    private void fetchPropertyData(Long propertyId, final PropertyDataCallback callback) {
        Call<Property> call = ClientUtils.propertyService.getProperty(propertyId);
        call.enqueue(new Callback<Property>() {
            @Override
            public void onResponse(Call<Property> call, Response<Property> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Property property = response.body();
                    callback.onPropertyDataFetched(property);
                } else {
                }
            }

            @Override
            public void onFailure(Call<Property> call, Throwable t) {
                callback.onPropertyDataFetchFailed();
            }
        });
    }

    interface PropertyDataCallback {
        void onPropertyDataFetched(Property propertyDto);

        void onPropertyDataFetchFailed();
    }


    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView propertyImage;
        private final TextView guestName;
        private final TextView propertyName;
        private final TextView dateRange;
        private final TextView price;
        private final TextView status;
        private final Button reportButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyImage = itemView.findViewById(R.id.property_image);
            guestName = itemView.findViewById(R.id.guest_name);
            propertyName = itemView.findViewById(R.id.property_name);
            dateRange = itemView.findViewById(R.id.date_range);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            reportButton = itemView.findViewById(R.id.report_button);
        }

        public ImageView getPropertyImage() {
            return propertyImage;
        }

        public TextView getGuestName() {
            return guestName;
        }

        public TextView getPropertyName() {
            return propertyName;
        }

        public TextView getDateRange() {
            return dateRange;
        }

        public TextView getStatus() {
            return status;
        }

        public TextView getPrice() {
            return price;
        }

    }

    public void setUpReportButton(ViewHolder holder, Reservation reservation) {
        if (!reservation.getStatus().equals(ReservationStatus.ACCEPTED) || reservation.getEndDate().after(new Date())) {
            holder.reportButton.setVisibility(View.GONE);
            return;
        }

        holder.reportButton.setVisibility(View.VISIBLE);

        holder.reportButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong(AccountReportFragment.ACCOUNT_ID_KEY, reservation.getGuestId());
            bundle.putBoolean(AccountReportFragment.IS_GUEST_KEY, true);
            fragment.getActivity().getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().setFragmentResult(AccountReportFragment.REQUEST_KEY, bundle);
        });
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }
}
