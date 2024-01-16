package com.example.lunark.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.R;
import com.example.lunark.models.AvailabilityEntry;
import com.example.lunark.models.Notification;
import com.example.lunark.notifications.NotificationReceiver;
import com.example.lunark.util.RelativeDateConverter;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<Notification> notifications;
    Context context;
    public NotificationAdapter(List<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications.clear();
        this.notifications.addAll(notifications);
        this.notifyDataSetChanged();
    }

    public void add(Notification notification) {
        this.notifications.add(0, notification);
        this.notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        Drawable icon = context.getResources().getDrawable(NotificationReceiver.iconResourceIds.get(notification.getType()), context.getTheme());
        holder.getIcon().setImageDrawable(icon);
        holder.getText().setText(notification.getText());
        holder.getTime().setText(RelativeDateConverter.convertToRelativeString(notification.getDate()));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView icon;
        private final TextView text;
        private final TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.notification_icon_image_view);
            text = itemView.findViewById(R.id.notification_text_text_view);
            time = itemView.findViewById(R.id.notification_time_text_view);

        }

        public ImageView getIcon() {
            return icon;
        }

        public TextView getText() {
            return text;
        }

        public TextView getTime() {
            return time;
        }
    }
}
