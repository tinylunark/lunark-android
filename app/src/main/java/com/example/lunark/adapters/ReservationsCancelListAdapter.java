package com.example.lunark.adapters;

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
import com.example.lunark.dtos.AccountDto;
import com.example.lunark.models.Property;
import com.example.lunark.models.PropertyImage;
import com.example.lunark.models.Reservation;
import com.example.lunark.repositories.ReservationRepository;
import com.example.lunark.util.ClientUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationsCancelListAdapter extends RecyclerView.Adapter<ReservationsCancelListAdapter.ViewHolder> {
    private Fragment fragment;
    private List<Reservation> reservations;

    @Inject
    ReservationRepository reservationRepository;

    public ReservationsCancelListAdapter(Fragment fragment, List<Reservation> reservations) {
        this.fragment = fragment;
        ((LunarkApplication) fragment.getActivity().getApplication()).applicationComponent.inject(this);
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ReservationsCancelListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_card_cancel, parent, false);
        return new ReservationsCancelListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationsCancelListAdapter.ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Long userId = reservation.getGuestId();
        Long reservationId = reservation.getId();
        Long propertyId = reservation.getPropertyId();

        holder.dateRange.setText(dateFormat.format(reservation.getStartDate()) + " - " + dateFormat.format(reservation.getEndDate()));
        holder.price.setText("Price: $" + String.valueOf(reservation.getPrice()));

        fetchPropertyData(propertyId, new ReservationsCancelListAdapter.PropertyDataCallback() {
            @Override
            public void onPropertyDataFetched(Property property) {
                if (property.getImages().size() > 0) {
                    PropertyImage image = property.getImages().get(0);
                    Glide.with(fragment)
                            .load(ClientUtils.SERVICE_API_PATH + "properties/" + property.getId() + "/images/" + image.getId())
                            .into(holder.getPropertyImage());
                }
                holder.getGuestName().setText(reservation.getPropertyName());
                holder.getPropertyName().setText(property.getAddress().getCity().toString() + ", " + property.getAddress().getCity().toString());
            }

            @Override
            public void onPropertyDataFetchFailed() {
            }
        });


        holder.getBtnCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReservation(reservationId);
                Toast.makeText(fragment.getContext(), new String("Reservation canceled!"), Toast.LENGTH_SHORT).show();
                reservations.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });


    }
    private void fetchPropertyData(Long propertyId, final ReservationsCancelListAdapter.PropertyDataCallback callback) {
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

    public void cancelReservation(long reservationId) {
        reservationRepository.cancelReservation(reservationId);
    }

    interface UserDataCallback {
        void onUserDataFetched(AccountDto accountDto);
        void onUserDataFetchFailed();
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
        private final Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyImage = itemView.findViewById(R.id.property_image);
            guestName = itemView.findViewById(R.id.guest_name);
            propertyName = itemView.findViewById(R.id.property_name);
            dateRange = itemView.findViewById(R.id.date_range);
            price = itemView.findViewById(R.id.price);
            btnCancel = itemView.findViewById(R.id.btnCancel);
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


        public TextView getPrice() {
            return price;
        }

        public Button getBtnCancel() {
            return btnCancel;
        }
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }
}
