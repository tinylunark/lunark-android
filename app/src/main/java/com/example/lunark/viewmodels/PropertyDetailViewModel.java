package com.example.lunark.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.models.Property;
import com.example.lunark.repositories.PropertyRepository;

public class PropertyDetailViewModel extends AndroidViewModel {
    private final PropertyRepository propertyRepository;
    private MutableLiveData<Property> property = new MutableLiveData<>();

    public PropertyDetailViewModel(@NonNull Application application) {
        super(application);

        propertyRepository = new PropertyRepository();
    }

    public void initProperty(Long id) {
        propertyRepository.getProperty(id).observeForever(property1 -> property.setValue(property1));

    }
    public void initProperty() {
       property = new MutableLiveData<>(new Property());
    }

    public LiveData<Property> getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property.setValue(property);
    }
}
