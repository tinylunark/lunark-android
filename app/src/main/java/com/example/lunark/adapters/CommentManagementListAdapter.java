package com.example.lunark.adapters;

import android.util.Log;
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
import com.example.lunark.dtos.AccountDto;
import com.example.lunark.models.Review;
import com.example.lunark.models.ReviewReport;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.repositories.ReviewReportRepository;
import com.example.lunark.util.ClientUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentManagementListAdapter extends RecyclerView.Adapter<CommentManagementListAdapter.ViewHolder> {

    private Fragment fragment;
    private List<ReviewReport> reviews;
    @Inject
    LoginRepository loginRepository;
    @Inject
    ReviewReportRepository reviewReportRepository;
    private static final String TAG = "REVIEW_LIST_ADAPTER";


    public CommentManagementListAdapter(Fragment fragment, List<ReviewReport> reviews) {
        this.fragment = fragment;
        this.reviews = reviews;
        ((LunarkApplication) fragment.getActivity().getApplication()).applicationComponent.inject(this);
    }

    @NonNull
    @Override
    public CommentManagementListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_report_card, parent, false);

        return new CommentManagementListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentManagementListAdapter.ViewHolder holder, int position) {
        ReviewReport review = reviews.get(position);

        fetchUserData(review.getReporterId(), new CommentManagementListAdapter.UserDataCallback() {
            @Override
            public void onUserDataFetched(AccountDto accountDto) {
                holder.getAuthor().setText(accountDto.getName() + " " + accountDto.getSurname() + " has made a report.");
            }
            @Override
            public void onUserDataFetchFailed() {
            }
        });

        fetchReviewData(review.getReviewId(), new CommentManagementListAdapter.ReviewDataCallback() {
            @Override
            public void onReviewDataFetched(Review review) {
                holder.getComment().setText("Comment: " + review.getDescription());
                holder.getRating().setText("Rating: " + review.getRating() + "/5");
            }
            @Override
            public void onReviewDataFetchFailed() {
            }
        });
        String formattedDate = LocalDateTime.parse(review.getDate()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        holder.getDate().setText(formattedDate);


        holder.getRemoveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeReview(review.getId());
                Toast.makeText(fragment.getContext(), new String("Review removed!"), Toast.LENGTH_SHORT).show();
                reviews.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    private void fetchUserData(Long userId, final CommentManagementListAdapter.UserDataCallback callback) {
        Call<AccountDto> call = ClientUtils.accountService.getAccount(userId);
        call.enqueue(new Callback<AccountDto>() {
            @Override
            public void onResponse(Call<AccountDto> call, Response<AccountDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountDto accountDto = response.body();
                    Log.d("REVIEW: ", accountDto.toString());
                    callback.onUserDataFetched(accountDto);
                } else {
                    callback.onUserDataFetchFailed();
                }
            }

            @Override
            public void onFailure(Call<AccountDto> call, Throwable t) {
                callback.onUserDataFetchFailed();
            }
        });
    }

    private void fetchReviewData(Long reviewId, final CommentManagementListAdapter.ReviewDataCallback callback) {
        Call<Review> call = ClientUtils.reviewService.getReview(reviewId);
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Review review = response.body();
                    Log.d("REVIEW: ", review.toString());
                    callback.onReviewDataFetched(review);
                } else {
                    callback.onReviewDataFetchFailed();
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                callback.onReviewDataFetchFailed();
            }
        });
    }

    public void removeReview(long reviewId) {
        reviewReportRepository.deleteReview(reviewId);
    }

    interface UserDataCallback {
        void onUserDataFetched(AccountDto accountDto);
        void onUserDataFetchFailed();
    }

    interface ReviewDataCallback {
        void onReviewDataFetched(Review review);
        void onReviewDataFetchFailed();
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
        private final Button removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.nameTextView);
            rating = (TextView) itemView.findViewById(R.id.ratingTextView);
            comment = (TextView) itemView.findViewById(R.id.commentTextView);
            date = (TextView) itemView.findViewById(R.id.reportDateTextView);
            removeButton = (Button) itemView.findViewById(R.id.removeButton);
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

        public Button getRemoveButton() {
            return removeButton;
        }
    }

    public void setReviews(List<ReviewReport> reviews) {
        this.reviews = reviews;
    }
}
