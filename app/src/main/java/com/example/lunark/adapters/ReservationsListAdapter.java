package com.example.lunark.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lunark.R;
import com.example.lunark.models.Reservation;
import java.util.List;

public class ReservationsListAdapter extends RecyclerView.Adapter<ReservationsListAdapter.ViewHolder> {
    private Fragment fragment;
    private List<Reservation> reservations;

    public ReservationsListAdapter(Fragment fragment, List<Reservation> reservations) {
        this.fragment = fragment;
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);

        holder.propertyName.setText(reservation.getPropertyName());
        holder.dateRange.setText(reservation.getStartDate() + " - " + reservation.getEndDate());
        holder.guestName.setText(reservation.getGuest().getName() + " " + reservation.getGuest().getSurname());
        holder.price.setText("Price: $" + String.valueOf(reservation.getPrice()));
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
        private final TextView cancelCount;
        private final TextView price;
        private final Button btnAccept;
        private final Button btnDecline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyImage = itemView.findViewById(R.id.property_image);
            guestName = itemView.findViewById(R.id.guest_name);
            propertyName = itemView.findViewById(R.id.property_name);
            dateRange = itemView.findViewById(R.id.date_range);
            cancelCount = itemView.findViewById(R.id.cancel_count);
            price = itemView.findViewById(R.id.price);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
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

        public TextView getCancelCount() {
            return cancelCount;
        }

        public TextView getPrice() {
            return price;
        }

        public Button getBtnAccept() {
            return btnAccept;
        }

        public Button getBtnDecline() {
            return btnDecline;
        }
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }
}
