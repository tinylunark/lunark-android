package com.example.lunark.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.Property;
import com.example.lunark.repositories.PropertyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

public class PropertiesViewModel extends AndroidViewModel {
    private final PropertyRepository mPropertyRepository;
    private final MutableLiveData<PropertySearchState> state = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> mParams = new MutableLiveData<>(new HashMap<>());
    private final LiveData<List<Property>> properties;

    public PropertiesViewModel(@NonNull Application application, PropertyRepository propertyRepository) {
        super(application);

        this.mPropertyRepository = propertyRepository;
        state.setValue(new PropertySearchState());
        properties = Transformations.switchMap(mParams, mPropertyRepository::getProperties);
    }

    public void search() {
        Map<String, String> params = new HashMap<>();
        if (state.getValue().getGuestNumber() != null) {
            params.put("guestNumber", state.getValue().getGuestNumber().toString());
        } else {
            params.put("guestNumber", "");
        }
        if (state.getValue().getLocation() != null) {
            params.put("location", state.getValue().getLocation());
        } else {
            params.put("location", "");
        }
        if (state.getValue().getStartDate() != null) {
            params.put("startDate", state.getValue().getStartDate().toString());
        } else {
            params.put("startDate", "");
        }
        if (state.getValue().getEndDate() != null) {
            params.put("endDate", state.getValue().getEndDate().toString());
        } else {
            params.put("endDate", "");
        }
        if (state.getValue().getType() != null) {
            switch (state.getValue().getType()) {
                case 0:
                    params.put("type", "");
                    break;
                case 1:
                    params.put("type", "WHOLE_HOUSE");
                    break;
                case 2:
                    params.put("type", "ROOM");
                    break;
                case 3:
                    params.put("type", "SHARED_ROOM");
                    break;
                default:
                    params.put("type", "");
                    break;
            }
        } else {
            params.put("type", "");
        }
        if (state.getValue().getMinPrice() != null) {
            params.put("minPrice", state.getValue().getMinPrice().toString());
        } else {
            params.put("minPrice", "");
        }
        if (state.getValue().getMaxPrice() != null) {
            params.put("maxPrice", state.getValue().getMaxPrice().toString());
        } else {
            params.put("maxPrice", "");
        }

        mParams.setValue(params);
    }

    public LiveData<List<Property>> getProperties() {
        return properties;
    }

    public static final ViewModelInitializer<PropertiesViewModel> initializer = new ViewModelInitializer<>(
            PropertiesViewModel.class,
            creationExtras -> {
                LunarkApplication application = (LunarkApplication) creationExtras.get(APPLICATION_KEY);
                assert application != null;

                return new PropertiesViewModel(application, application.getPropertyRepository());
            }
    );

    public MutableLiveData<PropertySearchState> getState() {
        return state;
    }

    public void setState(PropertySearchState value) {
        this.state.setValue(value);
    }
}
