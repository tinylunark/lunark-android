package com.example.lunark.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.lunark.LunarkApplication;
import com.example.lunark.dtos.CreateReservationDto;
import com.example.lunark.models.Property;
import com.example.lunark.models.Reservation;
import com.example.lunark.repositories.PropertyRepository;
import com.example.lunark.repositories.ReservationRepository;
import com.google.android.material.datepicker.MaterialDatePicker;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.inject.Inject;

public class BookingRequestViewModel extends ViewModel {
    public static final String TAG = "BookingRequestViewModel";

    private final PropertyRepository mPropertyRepository;

    private final ReservationRepository mReservationRepository;

    private LiveData<Property> mProperty = new MutableLiveData<>();

    private final MutableLiveData<Long> mPropertyId = new MutableLiveData<>();

    private final MutableLiveData<Integer> mGuestNumber = new MutableLiveData<>(null);

    private final MutableLiveData<Long> mStartDate = new MutableLiveData<>(null);

    private final MutableLiveData<Long> mEndDate = new MutableLiveData<>(null);

    public BookingRequestViewModel(PropertyRepository propertyRepository, ReservationRepository reservationRepository) {
        mPropertyRepository = propertyRepository;
        mReservationRepository = reservationRepository;

        mPropertyId.observeForever(id -> {
            if (id != null) {
                Log.d(TAG, "Property ID: " + id);
                mProperty = mPropertyRepository.getProperty(id);
            }
        });
    }

    public LiveData<Reservation> createReservation() {
        if (mGuestNumber.getValue() == null || mStartDate.getValue() == null || mEndDate.getValue() == null) {
            throw new IllegalStateException("Guest number, start date, and end date must be set");
        }

        Instant startInstant = Instant.ofEpochMilli(mStartDate.getValue());
        Instant endInstant = Instant.ofEpochMilli(mEndDate.getValue());

        LocalDate startDate = startInstant.atZone(ZoneId.of("UTC")).toLocalDate();
        LocalDate endDate = endInstant.atZone(ZoneId.of("UTC")).toLocalDate();

        CreateReservationDto dto = new CreateReservationDto(mPropertyId.getValue(), startDate, endDate, mGuestNumber.getValue());
        return mReservationRepository.createReservation(dto);
    }

    public LiveData<Property> getProperty() {
        return mProperty;
    }

    public void setPropertyId(Long id) {
        mPropertyId.setValue(id);
    }

    public MutableLiveData<Integer> getGuestNumber() {
        return mGuestNumber;
    }

    public void setGuestNumber(Integer guestNumber) {
        mGuestNumber.setValue(guestNumber);
    }

    public MutableLiveData<Long> getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Long startDate) {
        mStartDate.setValue(startDate);
    }

    public MutableLiveData<Long> getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Long endDate) {
        mEndDate.setValue(endDate);
    }

    public static final ViewModelInitializer<BookingRequestViewModel> initializer = new ViewModelInitializer<>(
            BookingRequestViewModel.class,
            creationExtras -> {
                LunarkApplication application = (LunarkApplication) creationExtras.get(APPLICATION_KEY);
                assert application != null;
                return new BookingRequestViewModel(application.getPropertyRepository(), application.getReservationRepository());
            }
    );
}
