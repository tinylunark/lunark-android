package com.example.lunark;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lunark.databinding.ActivityHomeBinding;
import com.example.lunark.fragments.AccountReportFragment;
import com.example.lunark.fragments.HostPageFragment;
import com.example.lunark.fragments.createProperty.IAllowBackPressed;
import com.example.lunark.models.Login;
import com.example.lunark.notifications.NotificationReceiver;
import com.example.lunark.notifications.NotificationService;
import com.example.lunark.repositories.LoginRepository;
import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class HomeActivity extends AppCompatActivity {
    @Inject
    LoginRepository loginRepository;
    private Disposable subscription;
    private ActivityHomeBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Set<Integer> topLevelDestinations = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getApplicationContext()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toolbar = binding.toolbar;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            actionBar.setHomeButtonEnabled(true);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setUpNotificationReceiver();

        setFragmentResultListeners();

        loginRepository.getLogin().subscribe(new SingleObserver<Login>() {
            @Override
            public void onSubscribe(Disposable d) {
                subscription = d;
            }

            @Override
            public void onSuccess(Login login) {
                String userRole = loginRepository.extractRoleFromJwt(login.getAccessToken());
                setupNavigationMenu(userRole);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });

    }
    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private boolean isActivityRunning(Class<?> activityClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            assert task.baseActivity != null;
            if (Objects.equals(activityClass.getCanonicalName(), task.baseActivity.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void logOut() {
        this.loginRepository.logOut().subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                HomeActivity.this.subscription = d;
            }

            @Override
            public void onComplete() {
                if (!isActivityRunning(SignUpScreenActivity.class)) {
                    Intent intent = new Intent(HomeActivity.this, LoginScreenActivity.class);
                    startActivity(intent);
                    stopNotificationService();
                    finish(); // Finish the current activity after logging out
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    private void setupNavigationMenu(String userRole) {
        Log.d("NAV", "Setting up navigation menu for role: " + userRole);
        navigationView.getMenu().clear();
        int menuResId = R.menu.nav_menu;
        if (userRole != null) {
            switch (userRole) {
                case "ADMIN":
                    menuResId = R.menu.admin_nav_menu;
                    break;
                case "HOST":
                    menuResId = R.menu.host_nav_menu;
                    break;
                case "GUEST":
                    menuResId = R.menu.nav_menu;
                    break;
            }
        }

        navigationView.inflateMenu(menuResId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_account) {
                    if (!isActivityRunning(AccountScreen.class)) {
                        Intent accountIntent = new Intent(HomeActivity.this, AccountScreen.class);
                        startActivity(accountIntent);
                    }
                } else if (itemId == R.id.menu_logout) {
                    logOut();
                } else if (itemId == R.id.nav_main) {
                    Navigation.findNavController(binding.fragmentContainerView).popBackStack(R.id.nav_main, false);
                } else {
                    NavigationUI.onNavDestinationSelected(item, navController);
                }
                drawer.closeDrawers();
                return true;
            }
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container_view);
        navController = navHostFragment.getNavController();
    }

    private Fragment getCurrentlyDisplayed() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.getFragments().get(0);
        return navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getCurrentlyDisplayed();
        if (!(fragment instanceof IAllowBackPressed) || ((IAllowBackPressed) fragment).allowBackPressed()) {
            super.onBackPressed();
        }
    }

    private void setUpNotificationReceiver() {

        NotificationReceiver notificationReceiver = new NotificationReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationReceiver.PUSH_NOTIFICATION);
        registerReceiver(notificationReceiver, filter);
    }

    private void setFragmentResultListeners() {
        this.getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().setFragmentResultListener("selectedProperty", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Navigation.findNavController(binding.fragmentContainerView).navigate(R.id.nav_property, bundle);
            }
        });

        this.getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().setFragmentResultListener("review", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Navigation.findNavController(binding.fragmentContainerView).navigate(R.id.nav_write_review, bundle);
            }
        });

        this.getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().setFragmentResultListener("updateProperty", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Navigation.findNavController(binding.fragmentContainerView).navigate(R.id.nav_update_property, bundle);
            }
        });

        this.getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().setFragmentResultListener(HostPageFragment.REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Navigation.findNavController(binding.fragmentContainerView).navigate(R.id.nav_host_page, bundle);
            }
        });

        this.getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().setFragmentResultListener("booking", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Navigation.findNavController(binding.fragmentContainerView).navigate(R.id.nav_booking_request, bundle);
            }
        });
        this.getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().setFragmentResultListener(AccountReportFragment.REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Navigation.findNavController(binding.fragmentContainerView).navigate(R.id.nav_account_report, bundle);
            }
        });
    }

    private void stopNotificationService() {
        Intent intent = new Intent(HomeActivity.this, NotificationService.class);
        intent.setAction(NotificationService.ACTION_STOP_NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Objects.requireNonNull(this).startForegroundService(intent);
        } else {
            Objects.requireNonNull(this).startService(intent);
        }
    }
}
