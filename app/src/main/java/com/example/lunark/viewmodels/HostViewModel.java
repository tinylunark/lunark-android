package com.example.lunark.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.lunark.LunarkApplication;
import com.example.lunark.datasources.AccountRepository;
import com.example.lunark.dtos.AccountDto;
import com.example.lunark.models.Host;
import com.example.lunark.models.Review;
import com.example.lunark.repositories.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class HostViewModel extends AndroidViewModel {

    @Inject
    ReviewRepository reviewRepository;

    @Inject
    AccountRepository accountRepository;

    private MutableLiveData<AccountDto> host;
    private MutableLiveData<List<Review>> reviews;

    private LiveData<AccountDto> remoteHostLiveData;

    public HostViewModel(@NonNull Application application) {
        super(application);
        ((LunarkApplication) application).applicationComponent.inject(this);
        host = new MutableLiveData<>();
        reviews = new MutableLiveData<>(new ArrayList<>());
    }

    public void init(Long id) {
        clearObservers();
        remoteHostLiveData = accountRepository.getAccount(id);
        accountRepository.getAccount(id).observeForever(hostObserver);
        reviewRepository.getHostReviews(id).subscribe(new SingleObserver<List<Review>>() {
            @Override
            public void onSubscribe(Disposable d) {
               reviewsSubscription = d;
            }

            @Override
            public void onSuccess(List<Review> reviews) {
                HostViewModel.this.reviews.setValue(reviews);
            }

            @Override
            public void onError(Throwable e) {
                HostViewModel.this.reviews.setValue(new ArrayList<>());
            }
        });
    }

    public LiveData<AccountDto> getHost() {
        return host;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private void clearObservers() {
        if (remoteHostLiveData != null) {
            remoteHostLiveData.removeObserver(hostObserver);
        }
        if (reviewsSubscription != null && !reviewsSubscription.isDisposed()){
            reviewsSubscription.dispose();
        }

    }

    private final Observer<AccountDto> hostObserver = new Observer<AccountDto>() {
        @Override
        public void onChanged(AccountDto accountDto) {
            host.setValue(accountDto);
        }
    };

    private Disposable reviewsSubscription;
}
