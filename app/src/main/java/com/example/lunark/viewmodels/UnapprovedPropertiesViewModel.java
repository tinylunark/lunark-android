package com.example.lunark.viewmodels;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.Property;
import com.example.lunark.repositories.PropertyRepository;

import java.util.List;

import javax.inject.Inject;

public class UnapprovedPropertiesViewModel extends AndroidViewModel {
    @Inject
    PropertyRepository propertyRepository;
    private final LiveData<List<Property>> properties = new MutableLiveData<>();

    public UnapprovedPropertiesViewModel(@NonNull Application application, PropertyRepository propertyRepository) {
        super(application);
        ((LunarkApplication) application).applicationComponent.inject(this);
    }

    public LiveData<List<Property>> getProperties() {
        return properties;
    }

    public LiveData<List<Property>> getUnapprovedProperties() {
        return propertyRepository.getUnapprovedProperties();
    }
    public LiveData<List<Property>> getMyProperties(Long hostId) {
        return propertyRepository.getMyProperties(hostId.toString());
    }

    public static final ViewModelInitializer<UnapprovedPropertiesViewModel> initializer =
            new ViewModelInitializer<>(
                    UnapprovedPropertiesViewModel.class,
                    creationExtras -> {

                        LunarkApplication app = (LunarkApplication)  creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new UnapprovedPropertiesViewModel(app, app.getPropertyRepository());
                    }
            );
}
