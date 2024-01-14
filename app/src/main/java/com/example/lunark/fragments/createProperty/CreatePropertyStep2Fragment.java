package com.example.lunark.fragments.createProperty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lunark.databinding.FragmentCreatePropertyStep2Binding;
import com.example.lunark.models.Amenity;
import com.example.lunark.models.Property;
import com.example.lunark.tools.GenericFileProvider;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreatePropertyStep2Fragment extends Fragment implements BlockingStep {
    PropertyDetailViewModel viewModel;
    Property property;
    private boolean updatesEnabled = false;
    private boolean readsEnabled = true;
    FragmentCreatePropertyStep2Binding binding;
    Map<String, CheckBox> amenityCheckboxes;
    Map<String, Long> amenityIds;

    private ActivityResultLauncher<Intent> startActivityForResult;
    private ActivityResultLauncher<String[]> permissionsResult;
    private String [] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Button takePictureButton;
    private Uri file;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePropertyStep2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        takePictureButton = binding.buttonImage;
        assert this.getParentFragment() != null;
        viewModel = new ViewModelProvider(this.getParentFragment(), ViewModelProvider.Factory.from(PropertyDetailViewModel.initializer)).get(PropertyDetailViewModel.class);
        setUpCheckBoxMap();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readViewModel();

        permissionsResult =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            // On Android >11, we don't need the WRITE_EXTERNAL_STORAGE_PERMISSION
                            if (list.get(0) && (list.get(1) || Build.VERSION.SDK_INT >= 30)) {
                                takePictureButton.setEnabled(true);
                            }
                            else {
                                takePictureButton.setEnabled(false);
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
                        }catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
        );
    }

    private void processPermission() {
        boolean permissionsGiven = true;
        for (int i = 0; i < permissions.length; i++){
            int perm = ContextCompat.checkSelfPermission(requireContext(), permissions[i]);
            if (perm != PackageManager.PERMISSION_GRANTED) {
                permissionsGiven = false;
            }
        }
        if (!permissionsGiven) {
            permissionsResult.launch(permissions);
        } else {
            takePictureButton.setEnabled(true);
        }

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


    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (!binding.nameEdittext.getText().toString().equals("") &&
                !binding.descriptionEdittext.getText().toString().equals("") &&
                this.viewModel.getImage() != null
        ) {
            return null;
        }
        return new VerificationError("Some fields were not filled");
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpChangeListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeChangeListeners();
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }


    private void readViewModel() {
        viewModel.getProperty().observe(getViewLifecycleOwner(), property -> {
            if (!this.readsEnabled) {
                return;
            }
            this.updatesEnabled = false;
            this.property = property;
            for (Amenity amenity: property.getAmenities()) {
                if (amenityCheckboxes.containsKey(amenity.getName())) {
                    amenityCheckboxes.get(amenity.getName()).setChecked(true);
                }
            }
            binding.nameEdittext.setText(property.getName());
            binding.descriptionEdittext.setText(property.getDescription());
            if (binding.imageview.getDrawable() == null && viewModel.getImage() != null) {
                binding.imageview.setImageBitmap(viewModel.getImage());
            }
            this.updatesEnabled = true;
        });

    }

    private void updateViewModel() {
        if (!updatesEnabled) {
            return;
        }
        this.readsEnabled = false;
        this.property.getAmenities().clear();
        for (String amenity: this.amenityCheckboxes.keySet()) {
            if (amenityCheckboxes.get(amenity).isChecked()) {
                this.property.getAmenities().add(new Amenity(amenityIds.get(amenity), amenity));
            }
            this.property.setAmenities(this.property.getAmenities());
        }

        this.property.setName(binding.nameEdittext.getText().toString());
        this.property.setDescription(binding.descriptionEdittext.getText().toString());

        this.viewModel.setProperty(this.property);
        this.readsEnabled = true;
    }

    private TextWatcher textWatcher = new TextWatcher() {
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
    };
    private void setUpChangeListeners() {
        for (CheckBox checkBox: amenityCheckboxes.values()) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> updateViewModel());
        }
        binding.nameEdittext.addTextChangedListener(textWatcher);
        binding.descriptionEdittext.addTextChangedListener(textWatcher);
        takePictureButton.setOnClickListener(view -> takePicture());
    }

    private void removeChangeListeners() {
        for (CheckBox checkBox: amenityCheckboxes.values()) {
            checkBox.setOnCheckedChangeListener(null);
        }
        binding.nameEdittext.removeTextChangedListener(textWatcher);
        binding.descriptionEdittext.removeTextChangedListener(textWatcher);
        takePictureButton.setOnClickListener(null);
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

    private File getOutputMediaFile(){
        File mediaStorageDir = new File(this.getContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), "Lunark");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        file = FileProvider.getUriForFile(requireContext(), GenericFileProvider.MY_PROVIDER,
                Objects.requireNonNull(getOutputMediaFile()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult.launch(intent);
    }
}
