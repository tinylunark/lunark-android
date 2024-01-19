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
import com.example.lunark.models.AccountReportDisplay;
import com.example.lunark.models.Review;
import com.example.lunark.repositories.AccountReportRepository;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.util.ClientUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagementListAdapter extends RecyclerView.Adapter<UserManagementListAdapter.ViewHolder> {

    private Fragment fragment;
    private List<AccountReportDisplay> reviews;
    @Inject
    LoginRepository loginRepository;
    @Inject
    AccountReportRepository accountReportyRepository;
    private static final String TAG = "REVIEW_LIST_ADAPTER";


    public UserManagementListAdapter(Fragment fragment, List<AccountReportDisplay> reviews) {
        this.fragment = fragment;
        this.reviews = reviews;
        ((LunarkApplication) fragment.getActivity().getApplication()).applicationComponent.inject(this);
    }

    @NonNull
    @Override
    public UserManagementListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_card, parent, false);

        return new UserManagementListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserManagementListAdapter.ViewHolder holder, int position) {
        AccountReportDisplay review = reviews.get(position);

        fetchUserData(review.getReporterId(), new UserManagementListAdapter.UserDataCallback() {
            @Override
            public void onUserDataFetched(AccountDto reporter) {
                fetchUserData(review.getReporterId(), new UserManagementListAdapter.UserDataCallback() {
                    @Override
                    public void onUserDataFetched(AccountDto reported) {
                        String formattedDate = LocalDateTime.parse(review.getDate()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        holder.getDate().setText(formattedDate);
                        String reason = reported.getName() + " " + reported.getSurname()
                                        + "has been reported by" +
                                        reporter.getName() + " " + reporter.getSurname();

                        holder.getReason().setText(reason);
                        holder.getReportedAccount().setText(reported.getName() + " " + reported.getSurname());
                        holder.getRole().setText(reported.getRole().toString());
                    }
                    @Override
                    public void onUserDataFetchFailed() {
                    }
                });
            }
            @Override
            public void onUserDataFetchFailed() {
            }
        });

        String formattedDate = LocalDateTime.parse(review.getDate()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        holder.getDate().setText(formattedDate);

        holder.getBlockButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockAccount(review.getId());
                Toast.makeText(fragment.getContext(), new String("Review removed!"), Toast.LENGTH_SHORT).show();
                reviews.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    private void fetchUserData(Long userId, final UserManagementListAdapter.UserDataCallback callback) {
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

    public void blockAccount(long accountId) {
        accountReportyRepository.blockAccount(accountId);
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
        private final TextView role;
        private final TextView date;
        private final TextView reportedAccount;
        private final TextView reason;
        private final Button blockButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            role = (TextView) itemView.findViewById(R.id.accountRoleText);
            reason = (TextView) itemView.findViewById(R.id.reasonText);
            reportedAccount = (TextView) itemView.findViewById(R.id.accountNameText);
            date = (TextView) itemView.findViewById(R.id.timeText);
            blockButton = (Button) itemView.findViewById(R.id.blockButton);
        }

        public TextView getReason(){
            return reason;
        }

        public TextView getRole() {
            return role;
        }

        public TextView getReportedAccount() {
            return reportedAccount;
        }

        public TextView getDate() {
            return date;
        }

        public Button getBlockButton() {
            return blockButton;
        }
    }

    public void setReviews(List<AccountReportDisplay> reviews) {
        this.reviews = reviews;
    }
}
