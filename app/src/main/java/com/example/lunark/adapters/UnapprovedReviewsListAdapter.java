package com.example.lunark.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class UnapprovedReviewsListAdapter extends RecyclerView.Adapter<UnapprovedReviewsListAdapter.ViewHolder> {
    private Fragment fragment;
    private List<Review> reviews;
    @Inject
    LoginRepository loginRepository;
    @Inject
    ReviewRepository reviewRepository;
    private Long currentUserId;
    private static final String TAG = "REVIEW_LIST_ADAPTER";

    public UnapprovedReviewsListAdapter(Fragment fragment, List<Review> reviews) {
        this.fragment = fragment;
        this.reviews = reviews;
        ((LunarkApplication) fragment.getActivity().getApplication()).applicationComponent.inject(this);
        this.currentUserId = this.loginRepository.getLogin().blockingGet().getProfileId();
    }

    @NonNull
    @Override
    public UnapprovedReviewsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.approve_review_card, parent, false);

        return new UnapprovedReviewsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnapprovedReviewsListAdapter.ViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.getAuthor().setText("From: " + review.getAuthor());
        holder.getRating().setText("Rating: " + String.format("%d", review.getRating())+ "/5");
        holder.getDate().setText(review.getDate().toString());
        holder.getComment().setText("Comment: " + review.getDescription());

        holder.getApproveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveReview(review.getId());
                Toast.makeText(fragment.getContext(), new String("Reservation accepted!"), Toast.LENGTH_SHORT).show();
                reviews.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    public void approveReview(long reviewId) {
        reviewRepository.approveReview(reviewId);
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
        private final Button approveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.author);
            rating = (TextView) itemView.findViewById(R.id.rating);
            date = (TextView) itemView.findViewById(R.id.dateReview);
            comment = (TextView) itemView.findViewById(R.id.description);
            approveButton = (Button) itemView.findViewById(R.id.approveButton);
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


        public Button getApproveButton() {
            return approveButton;
        }
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
