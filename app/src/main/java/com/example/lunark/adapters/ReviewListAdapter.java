package com.example.lunark.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.models.Review;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.repositories.ReviewRepository;

import java.util.List;

import javax.inject.Inject;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    private Fragment fragment;
    private List<Review> reviews;
    @Inject
    LoginRepository loginRepository;
    @Inject
    ReviewRepository reviewRepository;
    private Long currentUserId;

    public ReviewListAdapter(Fragment fragment, List<Review> reviews) {
        this.fragment = fragment;
        this.reviews = reviews;
        ((LunarkApplication) fragment.getActivity().getApplication()).applicationComponent.inject(this);
        this.currentUserId = this.loginRepository.getLogin().blockingGet().getProfileId();
    }

    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent, false);

        return new ReviewListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.ViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.getAuthor().setText(review.getAuthor());
        holder.getRating().setText(String.format("%d", review.getRating()));
        holder.getDate().setText(review.getDate().toString());
        holder.getComment().setText(review.getDescription());

        if (review.getAuthorId() == null || !review.getAuthorId().equals(this.currentUserId)) {
           holder.deleteButton.setVisibility(View.GONE);
        } else {
            holder.deleteButton.setOnClickListener(v -> {
                // TODO: Delete review
                Log.d("REVIEW_LIST_ADAPTER", "Tried to delete review with id: " + review.getId());
            });
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView author;
        private final TextView rating;
        private final TextView date;
        private final TextView comment;
        private final Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.author);
            rating = (TextView) itemView.findViewById(R.id.rating);
            date = (TextView) itemView.findViewById(R.id.date);
            comment = (TextView) itemView.findViewById(R.id.comment);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
        }

        public TextView getAuthor() {
            return author;
        }

        public TextView getRating() {
            return rating;
        }

        public TextView getDate() {
            return date;
        }

        public TextView getComment() {
            return comment;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public void setProperties(List<Review> reviews) {
        this.reviews = reviews;
    }
}
