package com.example.lunark.fragments.createProperty;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lunark.BuildConfig;
import com.example.lunark.R;
import com.example.lunark.databinding.FragmentCreatePropertyStep1Binding;
import com.example.lunark.models.Address;
import com.example.lunark.models.Property;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class CreatePropertyStep1Fragment extends Fragment implements BlockingStep {

    PropertyDetailViewModel viewModel;
    FragmentCreatePropertyStep1Binding binding;
    Property property;
    ItemizedOverlayWithFocus<OverlayItem> mapOverlay;
    ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePropertyStep1Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        assert this.getParentFragment() != null;
        viewModel = new ViewModelProvider(this.getParentFragment()).get(PropertyDetailViewModel.class);
        setUpNumberPickers();
        loadMap(45.2432787, 19.8467293);
        setUpChangeListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readViewModel();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (binding.typeRadioGroup.getCheckedRadioButtonId() != -1 &&
                binding.minGuestsNumberPicker.getValue() <= binding.maxGuestsNumberPicker.getValue() &&
                !binding.addressEditText.getText().toString().equals("") &&
                overlayItems.size() > 0) {
            return null;
        }
        return new VerificationError(getString(R.string.some_fields_are_not_filled_or_are_filled_incorrectly));
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void setUpChangeListeners() {
        binding.typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateViewModel());
        binding.addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateViewModel();
            }
        });
        MapView map = binding.osmmap;
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Log.d("Map", p.getLongitude() + " " + p.getLatitude());
                setMarker(p);
                updateViewModel();
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(
                getActivity().getApplicationContext(),
                mapEventsReceiver
        );

        map.getOverlayManager().add(mapEventsOverlay);
    }

    private void updateViewModel() {
        if (binding.roomRadio.isChecked()) {
            property.setType("ROOM");
        }
        if (binding.wholeHouseRadio.isChecked()) {
            property.setType("WHOLE_HOUSE");
        }
        if (binding.sharedRoomRadio.isChecked()) {
            property.setType("SHARED_ROOM");
        }
        Pair<Double, Double> location = getSelectedLatLong();
        if (location != null) {
            property.setLatitude(location.first);
            property.setLongitude(location.second);
        }
    }

    private void readViewModel() {
        viewModel.getProperty().observe(getViewLifecycleOwner(), property -> {
            this.property = property;
            restorePropertyType(property);
            binding.minGuestsNumberPicker.setValue(property.getMinGuests());
            binding.maxGuestsNumberPicker.setValue(property.getMaxGuests());
            binding.addressEditText.setText(property.getAddress().getStreet());
            if(property.getType() != null) {
                restoreLocation(property);
            }
        });
    }

    private void restorePropertyType(Property property) {
        if (property.getType() != null) {
            switch (property.getType()) {
                case "ROOM":
                    binding.roomRadio.setChecked(true);
                    break;
                case "WHOLE_HOUSE":
                    binding.wholeHouseRadio.setChecked(true);
                    break;
                case "SHARED_ROOM":
                    binding.sharedRoomRadio.setChecked(true);
                    break;
            }
        }
    }

    private void restoreLocation(Property property) {
        setMarker(new GeoPoint(property.getLatitude(), property.getLongitude()));
    }

    private Pair<Double, Double> getSelectedLatLong() {
        if (overlayItems.size() == 0) {
            return null;
        }
        return new Pair<>(overlayItems.get(0).getPoint().getLatitude(), overlayItems.get(0).getPoint().getLongitude());
    }

    private void setUpNumberPickers() {
        binding.minGuestsNumberPicker.setMinValue(1);
        binding.minGuestsNumberPicker.setMaxValue(10);
        binding.maxGuestsNumberPicker.setMinValue(1);
        binding.maxGuestsNumberPicker.setMaxValue(10);
        binding.minGuestsNumberPicker.setValue(1);
        binding.maxGuestsNumberPicker.setValue(1);
    }

    private void loadMap(double latitude, double longitude) {
        MapView map = binding.osmmap;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(16);
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        mapOverlay = new ItemizedOverlayWithFocus<OverlayItem>(overlayItems,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, getActivity().getApplicationContext());
        mapOverlay.setFocusItemsOnTap(true);
        map.getOverlayManager().add(mapOverlay);
    }

    private void setMarker(GeoPoint p) {
        this.overlayItems.clear();
        this.overlayItems.add(new OverlayItem("Your property", "", p));
        this.mapOverlay.setFocusedItem(overlayItems.get(0));
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        updateViewModel();
        callback.goToNextStep();
    }


    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        updateViewModel();
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        updateViewModel();
        callback.goToPrevStep();
    }
}
