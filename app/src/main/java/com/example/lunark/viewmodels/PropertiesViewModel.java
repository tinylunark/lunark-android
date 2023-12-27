package com.example.lunark.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lunark.models.Property;
import com.example.lunark.repositories.PropertyRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PropertiesViewModel extends AndroidViewModel {
    private final PropertyRepository propertyRepository;
    private final LiveData<List<Property>> properties;

    public PropertiesViewModel(@NonNull Application application) {
        super(application);

        propertyRepository = new PropertyRepository();
        properties = propertyRepository.getProperties(new HashMap<>());
    }

    public LiveData<List<Property>> getProperties() {
        return properties;
    }
}
