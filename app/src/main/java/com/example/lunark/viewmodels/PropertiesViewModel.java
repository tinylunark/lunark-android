package com.example.lunark.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.Property;
import com.example.lunark.repositories.PropertyRepository;

import java.util.HashMap;
import java.util.List;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

public class PropertiesViewModel extends AndroidViewModel {
    private final PropertyRepository mPropertyRepository;
    private final LiveData<List<Property>> mProperties;

    public PropertiesViewModel(@NonNull Application application, PropertyRepository propertyRepository) {
        super(application);

        this.mPropertyRepository = propertyRepository;
        this.mProperties = propertyRepository.getProperties(new HashMap<>());
    }

    public LiveData<List<Property>> getProperties() {
        return mProperties;
    }

    public static final ViewModelInitializer<PropertiesViewModel> initializer = new ViewModelInitializer<>(
            PropertiesViewModel.class,
            creationExtras -> {
                LunarkApplication application = (LunarkApplication) creationExtras.get(APPLICATION_KEY);
                assert application != null;

                return new PropertiesViewModel(application, application.getPropertyRepository());
            }
    );
}
