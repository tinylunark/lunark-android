package com.example.lunark.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.Property;
import com.example.lunark.repositories.PropertyRepository;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

public class BookingRequestViewModel extends ViewModel {
    public static final String TAG = "BookingRequestViewModel";

    private final PropertyRepository mPropertyRepository;

    private LiveData<Property> mProperty = new MutableLiveData<>();

    private final MutableLiveData<Long> mPropertyId = new MutableLiveData<>();

    public BookingRequestViewModel(PropertyRepository propertyRepository) {
        mPropertyRepository = propertyRepository;

        mPropertyId.observeForever(id -> {
            if (id != null) {
                Log.d(TAG, "Property ID: " + id);
                mProperty = mPropertyRepository.getProperty(id);
            }
        });
    }

    public LiveData<Property> getProperty() {
        return mProperty;
    }

    public void setPropertyId(Long id) {
        mPropertyId.setValue(id);
    }

    public static final ViewModelInitializer<BookingRequestViewModel> initializer = new ViewModelInitializer<>(
            BookingRequestViewModel.class,
            creationExtras -> {
                LunarkApplication application = (LunarkApplication) creationExtras.get(APPLICATION_KEY);
                assert application != null;
                return new BookingRequestViewModel(application.getPropertyRepository());
            }
    );
}
