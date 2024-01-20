package com.example.lunark.viewmodels;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.Property;
import com.example.lunark.repositories.PropertyRepository;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PropertyDetailViewModel extends AndroidViewModel {
    private final PropertyRepository propertyRepository;
    private MutableLiveData<Property> property = new MutableLiveData<>();
    private List<Bitmap> images = new ArrayList<>();

    public PropertyDetailViewModel(@NonNull Application application) {
        super(application);
        propertyRepository = new PropertyRepository();
    }

    public PropertyDetailViewModel(@NonNull Application application, PropertyRepository propertyRepository) {
        super(application);

        this.propertyRepository = propertyRepository;
    }

    public void initProperty(Long id) {
        propertyRepository.getProperty(id).observeForever(property1 -> property.setValue(property1));

    }
    public void initProperty() {
        images.clear();
        property.setValue(new Property());
    }

    public LiveData<Property> getProperty() {
        return property;
    }

    public LiveData<Property> getProperty(Long id) {
        return propertyRepository.getProperty(id);
    }

    public void setProperty(Property property) {
        this.property.setValue(property);
    }

    public void addImage(Bitmap image) {
        this.images.add(image);
    }

    public Bitmap getImage() {
       if (this.images.size() == 0)  {
           return null;
       } else {
           return this.images.get(this.images.size() - 1);
       }
    }

    public Completable uploadProperty() {
        return this.propertyRepository.createProperty(this.property.getValue()).flatMapCompletable(property1 -> {
            Completable imageUploadCompletable = null;
            for (Bitmap image: this.images) {
                if (imageUploadCompletable == null) {
                    imageUploadCompletable = this.propertyRepository.uploadImage(property1.getId(), image);
                } else {
                    imageUploadCompletable = imageUploadCompletable.andThen(this.propertyRepository.uploadImage(property1.getId(), image));
                }
            }
            return imageUploadCompletable;
        });
    }

    public Completable updateProperty(Property property) {
        return this.propertyRepository.updateProperty(property).flatMapCompletable(property1 -> {
            Completable imageUploadCompletable = null;
            for (Bitmap image: this.images) {
                if (imageUploadCompletable == null) {
                    imageUploadCompletable = this.propertyRepository.uploadImage(property1.getId(), image);
                } else {
                    imageUploadCompletable = imageUploadCompletable.andThen(this.propertyRepository.uploadImage(property1.getId(), image));
                }
            }
            return imageUploadCompletable;
        });
    }

    public LiveData<List<Property>> getMyProperties(String hostId) {
        return propertyRepository.getMyProperties(hostId);
    }

    public static final ViewModelInitializer<PropertyDetailViewModel> initializer = new ViewModelInitializer<>(
            PropertyDetailViewModel.class,
            creationExtras -> {
                LunarkApplication app = (LunarkApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;

                return new PropertyDetailViewModel(app, app.getPropertyRepository());
            }
    );
}
