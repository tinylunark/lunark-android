package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.LunarkApplication;
import com.example.lunark.adapters.NotificationAdapter;
import com.example.lunark.databinding.FragmentNotificationsBinding;
import com.example.lunark.models.Notification;
import com.example.lunark.repositories.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NotificationsFragment extends Fragment {
    FragmentNotificationsBinding binding;
    NotificationAdapter adapter;
    RecyclerView notificationsRecyclerView;
    private Disposable subscription;
    @Inject
    NotificationRepository notificationRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getContext().getApplicationContext()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        notificationsRecyclerView = binding.notificationsRecyclerView;
        setUpNotificationsRecyclerView();
        this.notificationRepository.getNotifications().subscribe(new SingleObserver<List<Notification>>() {
            @Override
            public void onSubscribe(Disposable d) {
                subscription = d;
            }

            @Override
            public void onSuccess(List<Notification> notifications) {
                adapter.setNotifications(notifications);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
        return binding.getRoot();
    }

    private void setUpNotificationsRecyclerView() {
        adapter = new NotificationAdapter(new ArrayList<>(), this.getActivity().getApplicationContext());
        notificationsRecyclerView.setAdapter(adapter);
        int scrollPosition = 0;
        if (notificationsRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) notificationsRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationsRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onDestroy() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        super.onDestroy();
    }
}
