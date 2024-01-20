package com.example.lunark.fragments.updateProperty;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.lunark.BuildConfig;
import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.databinding.FragmentUpdatePropertyBinding;
import com.example.lunark.datasources.AccountRepository;
import com.example.lunark.dtos.ProfileDto;
import com.example.lunark.repositories.PropertyRepository;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.example.lunark.models.Property;
import com.google.android.material.textfield.TextInputEditText;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import javax.inject.Inject;
import io.reactivex.Observer;

public class UpdatePropertyFragment extends Fragment {

    private FragmentUpdatePropertyBinding binding;
    private static final String PROPERTY_ID = "propertyId";
    private Long propertyId;
    private PropertyDetailViewModel viewModel;
    LiveData<Property> property;

    @Inject
    AccountRepository accountRepository;

    @Inject
    PropertyRepository propertyRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((LunarkApplication) getActivity().getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PropertyDetailViewModel.class);

        if (getArguments() != null) {
            propertyId = getArguments().getLong(PROPERTY_ID);
            property = propertyRepository.getProperty(propertyId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdatePropertyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (property != null) {
            populateFields(property);

        }
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
    }

    private void populateFields(LiveData<Property> propertyLiveData) {
        propertyLiveData.observe(getViewLifecycleOwner(), property -> {
            if (property != null) {
                EditText addressEditText = binding.addressEditText;
                EditText cityEditText = binding.cityEditText;
                EditText countryEditText = binding.countryEditText;
                EditText nameEditText = binding.nameEdittext;
                EditText descriptionEditText = binding.descriptionEdittext;
                NumberPicker minGuestsNumberPicker = binding.minGuestsNumberPicker;
                NumberPicker maxGuestsNumberPicker = binding.maxGuestsNumberPicker;
                RadioButton roomRadio = binding.roomRadio;
                RadioButton wholeHouseRadio = binding.wholeHouseRadio;
                RadioButton sharedRoomRadio = binding.sharedRoomRadio;
                RadioButton perPersonRadio = binding.perPersonRadio;
                RadioButton wholeUnitRadio = binding.wholeUnitRadio;
                CheckBox autoApproveCheckBox = binding.autoApproveCheckbox;

                addressEditText.setText(property.getAddress().getStreet());
                cityEditText.setText(property.getAddress().getCity());
                countryEditText.setText(property.getAddress().getCountry());
                nameEditText.setText(property.getName());
                descriptionEditText.setText(property.getDescription());
                minGuestsNumberPicker.setValue(property.getMinGuests());
                maxGuestsNumberPicker.setValue(property.getMaxGuests());

                if(property.getType() != null) {
                    switch (property.getType()) {
                        case "ROOM":
                            roomRadio.setChecked(true);
                            break;
                        case "WHOLE_HOUSE":
                            wholeHouseRadio.setChecked(true);
                            break;
                        case "SHARED_ROOM":
                            sharedRoomRadio.setChecked(true);
                            break;
                        default:
                            break;
                    }
                }

                switch (property.getPricingMode()) {
                    case "PER_PERSON":
                        perPersonRadio.setChecked(true);
                        break;
                    case "WHOLE_UNIT":
                        wholeUnitRadio.setChecked(true);
                        break;
                }

                autoApproveCheckBox.setChecked(property.isAutoApproveEnabled());

                loadMap(property.getLatitude(), property.getLongitude());
            }
        });
    }
    private Property collectPropertyData() {
        Property property = new Property();

        EditText addressEditText = binding.addressEditText;
        EditText cityEditText = binding.cityEditText;
        EditText countryEditText = binding.countryEditText;
        EditText nameEditText = binding.nameEdittext;
        EditText descriptionEditText = binding.descriptionEdittext;
        EditText priceEditText = binding.priceEdittext;
        CheckBox freeParkingCheckBox = binding.freeParkingCheckbox;
        CheckBox poolCheckBox = binding.poolCheckbox;
        CheckBox workspaceCheckBox = binding.workspaceCheckbox;
        CheckBox wifiCheckBox = binding.wifiCheckbox;
        CheckBox acCheckBox = binding.acCheckbox;
        CheckBox medicalServicesCheckBox = binding.medicalServicesCheckbox;
        NumberPicker minGuestsNumberPicker = binding.minGuestsNumberPicker;
        NumberPicker maxGuestsNumberPicker = binding.maxGuestsNumberPicker;
        RadioGroup typeRadioGroup = binding.typeRadioGroup;
        RadioGroup pricingModeRadioGroup = binding.pricingModeRadioGroup;
        CheckBox autoApproveCheckBox = binding.autoApproveCheckbox;

        String address = addressEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String country = countryEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        double price;
        try {
            price = Double.parseDouble(priceEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            return null;
        }

        boolean freeParking = freeParkingCheckBox.isChecked();
        boolean pool = poolCheckBox.isChecked();
        boolean workspace = workspaceCheckBox.isChecked();
        boolean wifi = wifiCheckBox.isChecked();
        boolean ac = acCheckBox.isChecked();
        boolean medicalServices = medicalServicesCheckBox.isChecked();
        int minGuests = minGuestsNumberPicker.getValue();
        int maxGuests = maxGuestsNumberPicker.getValue();

        String type = "";
        int typeId = typeRadioGroup.getCheckedRadioButtonId();
        if (typeId != -1) {
            RadioButton selectedTypeRadioButton = binding.getRoot().findViewById(typeId);
            type = selectedTypeRadioButton.getText().toString().toUpperCase();
        }

        String pricingMode = "";
        int pricingModeId = pricingModeRadioGroup.getCheckedRadioButtonId();
        if (pricingModeId != -1) {
            RadioButton selectedPricingModeRadioButton = binding.getRoot().findViewById(pricingModeId);
            pricingMode = selectedPricingModeRadioButton.getText().toString().toUpperCase();
        }

        boolean autoApprove = autoApproveCheckBox.isChecked();

        // Populate the Property object with the collected data
        property.setAddress(new Address(address, city, country));
        property.setName(name);
        property.setDescription(description);
        property.set(price);
        property.setFreeParking(freeParking);
        property.setPool(pool);
        property.setWorkspace(workspace);
        property.setWifi(wifi);
        property.setAc(ac);
        property.setMedicalServices(medicalServices);
        property.setMinGuests(minGuests);
        property.setMaxGuests(maxGuests);
        property.setType(type);
        property.setPricingMode(pricingMode);
        property.setAutoApproveEnabled(autoApprove);

        return property;
    }



}
