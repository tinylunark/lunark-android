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
import com.example.lunark.models.ReviewReport;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.repositories.ReviewReportRepository;
import com.example.lunark.repositories.ReviewRepository;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    private Fragment fragment;
    private List<Review> reviews;
    @Inject
    LoginRepository loginRepository;
    @Inject
    ReviewRepository reviewRepository;
    @Inject
    ReviewReportRepository reviewReportRepository;
    private Long currentUserId;
    private static final String TAG = "REVIEW_LIST_ADAPTER";
    private final boolean reportingAllowed;

    public ReviewListAdapter(Fragment fragment, List<Review> reviews, boolean reportingAllowed) {
        this.fragment = fragment;
        this.reviews = reviews;
        ((LunarkApplication) fragment.getActivity().getApplication()).applicationComponent.inject(this);
        this.currentUserId = this.loginRepository.getLogin().blockingGet().getProfileId();
        this.reportingAllowed = reportingAllowed;
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
                Log.d(TAG, "Tried to delete review with id: " + review.getId());
                reviewRepository.deleteReview(review.getId()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        int currentPosition = holder.getAdapterPosition();
                        reviews.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error while deleting review: " + e.getMessage());
                    }
                });
            });
        }

        setUpReportButton(holder, review);
    }

    private void setUpReportButton(@NonNull ReviewListAdapter.ViewHolder holder, Review review) {
        if (!this.reportingAllowed) {
            holder.getReportButton().setVisibility(View.GONE);
            return;
        }
        holder.getReportButton().setVisibility(View.VISIBLE);
        holder.getReportButton().setOnClickListener(v -> reportReview(review));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    private void reportReview(Review review) {
        ReviewReport report = new ReviewReport(null, LocalDateTime.now().toString(), currentUserId, review.getId());
        reviewReportRepository.createReport(report).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Snackbar.make(fragment.getView(), R.string.review_reported_successfully, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                HttpException httpException = (HttpException) e;
                if (httpException.code() == 409) {
                    Snackbar.make(fragment.getView(), R.string.you_have_already_reported_this_review, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Snackbar.make(fragment.getView(), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView author;
        private final TextView rating;
        private final TextView date;
        private final TextView comment;
        private final Button deleteButton;
        private final Button reportButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.author);
            rating = (TextView) itemView.findViewById(R.id.rating);
            date = (TextView) itemView.findViewById(R.id.date);
            comment = (TextView) itemView.findViewById(R.id.comment);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
            reportButton = itemView.findViewById(R.id.report_button);
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
        public Button getReportButton() {
            return reportButton;
        }
    }

    public void setProperties(List<Review> reviews) {
        this.reviews = reviews;
    }
}
