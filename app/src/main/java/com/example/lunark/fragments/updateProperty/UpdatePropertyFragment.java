package com.example.lunark.fragments.updateProperty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lunark.BuildConfig;
import com.example.lunark.R;
import com.example.lunark.databinding.FragmentUpdatePropertyBinding;
import com.example.lunark.models.Amenity;
import com.example.lunark.tools.GenericFileProvider;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.example.lunark.models.Property;
import com.example.lunark.models.AvailabilityEntry;
import com.example.lunark.repositories.*;
import com.example.lunark.models.Address;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

public class UpdatePropertyFragment extends Fragment {

    private FragmentUpdatePropertyBinding binding;
    private PropertyDetailViewModel viewModel;
    private Property property;
    private Long propertyId;
    private int minNum = 1;
    private int maxNum = 1;

    private Map<String, CheckBox> amenityCheckboxes;
    private Pair<LocalDate, LocalDate> selectedDates;

    private List<AvailabilityEntry> availabilityList = new ArrayList<>();

    private ActivityResultLauncher<Intent> startActivityForResult;

    private ActivityResultLauncher<String[]> permissionsResult;

    private Uri file;

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    Map<String, Long> amenityIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(PropertyDetailViewModel.initializer)).get(PropertyDetailViewModel.class);
        if (getArguments() != null) {
            propertyId = getArguments().getLong("propertyId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdatePropertyBinding.inflate(inflater, container, false);
        binding.datePicker.setOnClickListener(v -> openDateRangePicker());
        binding.buttonImage.setOnClickListener(view -> takePicture());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (propertyId != null) {
            property = viewModel.getProperty().getValue();
            viewModel.getProperty(propertyId).observe(getViewLifecycleOwner(), this::populateFields);
        }

        setUpCheckBoxMap();

        permissionsResult =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            // On Android >11, we don't need the WRITE_EXTERNAL_STORAGE_PERMISSION
                            if (list.get(0) && (list.get(1) || Build.VERSION.SDK_INT >= 30)) {
                                binding.buttonImage.setEnabled(true);
                            } else {
                                binding.buttonImage.setEnabled(false);
                                Toast.makeText(getActivity(), "No permission.", Toast.LENGTH_SHORT).show();
                            }
                        });
        processPermission();

        startActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        binding.imageview.setImageURI(file);
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContext().getApplicationContext().getContentResolver().openInputStream(file));
                            viewModel.addImage(bitmap);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );

        NumberPicker minGuestsPicker = binding.minGuestsNumberPicker;
        NumberPicker maxGuestsPicker = binding.maxGuestsNumberPicker;

        minGuestsPicker.setMinValue(1);
        minGuestsPicker.setMaxValue(10);
        maxGuestsPicker.setMinValue(1);
        maxGuestsPicker.setMaxValue(10);

        minGuestsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minNum = newVal;
            }
        });

        maxGuestsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                maxNum = newVal;
            }
        });
        setUpChangeListeners();
        binding.updateButton.setOnClickListener(v -> onPropertyUpdate());
    }
    private void onPropertyUpdate() {
        viewModel.updateProperty(this.collectPropertyData());
        Toast.makeText(requireContext(), "Property updated successfully", Toast.LENGTH_SHORT).show();
    }


    private boolean isPropertyDataValid(Property property) {
    /*
        return !TextUtils.isEmpty(property.getName())
                && !TextUtils.isEmpty(property.getDescription())
                && property.getMinGuests() > 0
                && property.getMaxGuests() >= property.getMinGuests()
                && selectedDates != null
                && property.getCancellationDeadline().equals(" ")
                && property.getPricingMode() != null;
     */
        return true;
    }


    private void processPermission() {
        boolean permissionsGiven = true;
        for (int i = 0; i < permissions.length; i++) {
            int perm = ContextCompat.checkSelfPermission(requireContext(), permissions[i]);
            if (perm != PackageManager.PERMISSION_GRANTED) {
                permissionsGiven = false;
            }
        }
        if (!permissionsGiven) {
            permissionsResult.launch(permissions);
        } else {
            binding.buttonImage.setEnabled(true);
        }

    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        file = FileProvider.getUriForFile(requireContext(), GenericFileProvider.MY_PROVIDER,
                Objects.requireNonNull(getOutputMediaFile()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult.launch(intent);
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(this.getContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), "Lunark");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    private void setUpCheckBoxMap() {
        amenityCheckboxes = new HashMap<>();
        amenityCheckboxes.put("Air condtioning", binding.acCheckbox);
        amenityCheckboxes.put("Free parking", binding.freeParkingCheckbox);
        amenityCheckboxes.put("WiFi", binding.wifiCheckbox);
        amenityCheckboxes.put("Pool", binding.poolCheckbox);
        amenityCheckboxes.put("Medical Services", binding.medicalServicesCheckbox);
        amenityCheckboxes.put("Dedicated workspace", binding.workspaceCheckbox);
        amenityIds = new HashMap<>();
        amenityIds.put("Air condtioning", 1L);
        amenityIds.put("Free parking", 2L);
        amenityIds.put("WiFi", 3L);
        amenityIds.put("Pool", 4L);
        amenityIds.put("Medical Services", 5L);
        amenityIds.put("Dedicated workspace", 6L);
    }

    private void openDateRangePicker() {
        MaterialDatePicker picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(R.string.choose_date)
                .build();

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> selectedDateTimestamps = (Pair<Long, Long>) selection;
                LocalDate beginDate = convertTime(selectedDateTimestamps.first);
                LocalDate endDate = convertTime(selectedDateTimestamps.second);
                LocalDate current = LocalDate.now();
                if (!endDate.isAfter(current) || !beginDate.isAfter(current)) {
                    Toast.makeText(getActivity(), R.string.property_availability_can_not_be_set_changed_for_dates_in_the_past, Toast.LENGTH_SHORT).show();
                    return;
                }

                binding.startDateTextview.setText(beginDate.toString());
                binding.endDateTextview.setText(endDate.toString());
                selectedDates = new Pair<>(beginDate, endDate);
            }
        });
        picker.show(getParentFragmentManager(), "DATE_RANGE_PICKER");
    }

    private LocalDate convertTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    private boolean isPriceValid() {
        if (binding.priceEdittext.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), R.string.please_input_a_price, Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            Double.parseDouble(binding.priceEdittext.getText().toString());
            return true;
        } catch (NumberFormatException ex) {
            Toast.makeText(getActivity(), R.string.the_entered_price_is_invalid, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void populateFields(Property property) {
        if (property != null) {
            Address address = property.getAddress();

            if (address != null) {
                binding.addressEditText.setText(address.getStreet());
                binding.cityEditText.setText(address.getCity());
                binding.countryEditText.setText(address.getCountry());
            }

            binding.nameEdittext.setText(property.getName());
            binding.descriptionEdittext.setText(property.getDescription());

            for (Map.Entry<String, CheckBox> entry : amenityCheckboxes.entrySet()) {
                String amenityName = entry.getKey();
                CheckBox checkBox = entry.getValue();
                checkBox.setChecked(hasAmenity(property, amenityName));
            }

            binding.minGuestsNumberPicker.setValue(property.getMinGuests());
            binding.maxGuestsNumberPicker.setValue(property.getMaxGuests());

            setSelectedRadioButton(binding.typeRadioGroup, property.getType());
            setSelectedRadioButton(binding.pricingModeRadioGroup, property.getPricingMode());

            binding.autoApproveCheckbox.setChecked(property.isAutoApproveEnabled());

            loadMap(property.getLatitude(), property.getLongitude());
        }
    }

    private boolean hasAmenity(Property property, String amenityName) {
        List<Amenity> amenities = property.getAmenities();
        for (Amenity amenity : amenities) {
            if (amenity.getName().equalsIgnoreCase(amenityName)) {
                return true;
            }
        }
        return false;
    }

    private void setSelectedRadioButton(RadioGroup radioGroup, String selectedValue) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View radioButton = radioGroup.getChildAt(i);
            if (radioButton instanceof RadioButton) {
                RadioButton radioButtonItem = (RadioButton) radioButton;
                if (radioButtonItem.getText().toString().equalsIgnoreCase(selectedValue)) {
                    radioButtonItem.setChecked(true);
                    break;
                }
            }
        }
    }

    private Property collectPropertyData() {
        Property property = new Property();
        property.setId(this.propertyId);

        if(binding.roomRadio.isChecked()) {
            property.setType("ROOM");
        }
        if(binding.wholeHouseRadio.isChecked()) {
            property.setType("WHOLE_HOUSE");
        }
        if(binding.sharedRoomRadio.isChecked()){
            property.setType("SHARED_ROOM");
        }

        property.setAddress(new Address(
                binding.addressEditText.getText().toString(),
                binding.cityEditText.getText().toString(),
                binding.countryEditText.getText().toString()
                )
        );
        property.setName(binding.nameEdittext.getText().toString());
        property.setDescription(binding.descriptionEdittext.getText().toString());
        property.setMinGuests(binding.minGuestsNumberPicker.getValue());
        property.setMaxGuests(binding.maxGuestsNumberPicker.getValue());

        List<Amenity> amenities = new ArrayList<>();
        for (Map.Entry<String, CheckBox> entry : amenityCheckboxes.entrySet()) {
            String amenityName = entry.getKey();
            CheckBox checkBox = entry.getValue();
            if (checkBox.isChecked()) {
                amenities.add(new Amenity(amenityIds.get(amenityName), amenityName));
            }
        }
        property.setAmenities(amenities);

        property.setLatitude(binding.osmmap.getLatitudeSpanDouble());
        property.setLongitude(binding.osmmap.getLongitudeSpanDouble());
        property.setAvailabilityEntries(availabilityList);

        property.setCancellationDeadline(8);
        property.setAutoApproveEnabled(binding.autoApproveCheckbox.isChecked());
        if(binding.perPersonRadio.isChecked()) {
            property.setPricingMode("PER_PERSON");
        }
        if(binding.wholeUnitRadio.isChecked()) {
            property.setPricingMode("WHOLE_UNIT");
        }
        if(binding.autoApproveCheckbox.isChecked()) {
            property.setAutoApproveEnabled(true);
        }

        Log.w("AYO", "UPDATING PROPERTY");
        Log.w("AYO", property.toString());

        return property;
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

    private void setUpChangeListeners() {
        binding.addButton.setOnClickListener(v -> {
            if (!areDatesValid() || !isPriceValid()) {
                return;
            }

            Double price = Double.parseDouble(binding.priceEdittext.getText().toString());
            LocalDate startDate = selectedDates.first;
            LocalDate endDate = selectedDates.second;

            List<AvailabilityEntry> entries = new ArrayList<>();
            while (!startDate.isAfter(endDate)) {
                entries.add(new AvailabilityEntry(startDate, price));
                startDate = startDate.plusDays(1);
            }
            this.availabilityList.addAll(entries);
        });


        binding.deleteButton.setOnClickListener(v -> {
            if (!areDatesValid()) {
                return;
            }
            Pair<LocalDate, LocalDate> availabilityToDelete = new Pair<>(selectedDates.first, selectedDates.second);
            availabilityList.remove(availabilityToDelete);
        });
    }
    private boolean areDatesValid() {
        if (selectedDates == null) {
            Toast.makeText(getActivity(), R.string.please_choose_the_start_and_end_dates, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}