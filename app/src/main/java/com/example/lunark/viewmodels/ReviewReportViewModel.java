package com.example.lunark.viewmodels;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.ReviewReport;
import com.example.lunark.repositories.ReviewReportRepository;

import java.util.List;

import javax.inject.Inject;

public class ReviewReportViewModel extends AndroidViewModel {
    
    @Inject
    ReviewReportRepository reviewReportRepository;

    private final LiveData<List<ReviewReport>> properties = new MutableLiveData<>();

    public ReviewReportViewModel(@NonNull Application application, ReviewReportRepository propertyRepository) {
        super(application);
        ((LunarkApplication) application).applicationComponent.inject(this);
    }

    public LiveData<List<ReviewReport>> getReviewReports() {
        return reviewReportRepository.getReviewReports();
    }

    public static final ViewModelInitializer<ReviewReportViewModel> initializer = new ViewModelInitializer<>(
            ReviewReportViewModel.class,
            creationExtras -> {
                LunarkApplication app = (LunarkApplication)  creationExtras.get(APPLICATION_KEY);
                assert app != null;

                return new ReviewReportViewModel(app, app.getReviewReportRepository());
            }
    );

}
