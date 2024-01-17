package com.example.lunark.fragments.createProperty;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.R;
import com.example.lunark.adapters.AvailabilityEntryAdapter;
import com.example.lunark.databinding.FragmentCreatePropertyStep3Binding;
import com.example.lunark.models.Property;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class CreatePropertyStep3Fragment extends Fragment implements Step {
    PropertyDetailViewModel viewModel;
    AvailabilityEntryAdapter adapter;
    Property property;
    Pair<LocalDate, LocalDate> selectedDates;
    FragmentCreatePropertyStep3Binding binding;
    boolean updatesEnabled = false;
    boolean readsEnabled = true;

    EditText priceEditText;
    RecyclerView availabilityEntriesRecyclerView;
    RadioButton perPersonRadio;
    RadioButton wholeUnitRadio;
    CheckBox autoApproveCheckbox;
    EditText cancellationDeadlineEditText;
    RadioGroup pricingModeRadioGroup;
    Button addButton;
    Button deleteButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePropertyStep3Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.datePicker.setOnClickListener(v -> openDateRangePicker());
        assert this.getParentFragment() != null;
        viewModel = new ViewModelProvider(this.getParentFragment(), ViewModelProvider.Factory.from(PropertyDetailViewModel.initializer)).get(PropertyDetailViewModel.class);
        setUpElementBindings();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new AvailabilityEntryAdapter(new ArrayList<>());
        availabilityEntriesRecyclerView.setAdapter(adapter);
        int scrollPosition = 0;
        if (availabilityEntriesRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) availabilityEntriesRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        availabilityEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        availabilityEntriesRecyclerView.scrollToPosition(scrollPosition);
        readViewModel();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (this.property.getAvailabilityEntries().size() > 0 &&
                this.property.getCancellationDeadline() != null &&
                this.property.getCancellationDeadline() >= 0 &&
                !this.property.getPricingMode().isEmpty()
        ) {
            return null;
        }
        return new VerificationError("Not all fields were filled");
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpChangeListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeListeners();
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
        addButton.setOnClickListener(v -> {
            if (!areDatesValid() || !isPriceValid()) {
                return;
            }
            Double price = Double.parseDouble(priceEditText.getText().toString());
            this.property.addAvailability(selectedDates.first, selectedDates.second, price);
            this.viewModel.setProperty(this.property);
        });
        deleteButton.setOnClickListener(v -> {
            if (!areDatesValid()) {
                return;
            }
            this.property.deleteAvailability(selectedDates.first, selectedDates.second);
            this.viewModel.setProperty(this.property);
        });
        pricingModeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updateViewModel();
        });
        cancellationDeadlineEditText.addTextChangedListener(textWatcher);
        autoApproveCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> updateViewModel());
    }

    private boolean areDatesValid() {
        if (selectedDates == null) {
            Toast.makeText(getActivity(), R.string.please_choose_the_start_and_end_dates, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isPriceValid() {
        if (priceEditText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), R.string.please_input_a_price, Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            Double.parseDouble(priceEditText.getText().toString());
            return true;
        } catch (NumberFormatException ex) {
            Toast.makeText(getActivity(), R.string.the_entered_price_is_invalid, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void removeListeners() {
        addButton.setOnClickListener(null);
        deleteButton.setOnClickListener(null);
        cancellationDeadlineEditText.removeTextChangedListener(textWatcher);
        pricingModeRadioGroup.setOnCheckedChangeListener(null);
        autoApproveCheckbox.setOnCheckedChangeListener(null);
    }

    private void readViewModel() {
        viewModel.getProperty().observe(getViewLifecycleOwner(), property -> {
            if (!readsEnabled) {
                return;
            }
            Log.d("CREATE_PROPERTY", "Reading values from view model");
            this.updatesEnabled = false;
            this.property = property;
            perPersonRadio.setChecked(false);
            wholeUnitRadio.setChecked(false);
            if (this.property.getPricingMode().equals("PER_PERSON")) {
                perPersonRadio.setChecked(true);
            }
            if (this.property.getPricingMode().equals("WHOLE_UNIT")) {
                wholeUnitRadio.setChecked(true);
            }
            cancellationDeadlineEditText.setText("");
            if (this.property.getCancellationDeadline() != null) {
                cancellationDeadlineEditText.setText(this.property.getCancellationDeadline().toString());
            }
            autoApproveCheckbox.setChecked(this.property.isAutoApproveEnabled());
            this.adapter.setAvailabilityEntries(property.getAvailabilityEntries());
            this.updatesEnabled = true;
        });
    }

    private void updateViewModel() {
        if (!updatesEnabled) {
            return;
        }
        this.readsEnabled = false;
        if (perPersonRadio.isChecked()) {
            property.setPricingMode("PER_PERSON");
        }
        if (wholeUnitRadio.isChecked()) {
            property.setPricingMode("WHOLE_UNIT");
        }
        try {
            int cancellationDeadline = Integer.parseInt(cancellationDeadlineEditText.getText().toString());
            property.setCancellationDeadline(cancellationDeadline);
        } catch (NumberFormatException ex) {

        }
        property.setAutoApproveEnabled(autoApproveCheckbox.isChecked());
        viewModel.setProperty(property);
        this.readsEnabled = true;
    }

    private LocalDate convertTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setUpElementBindings() {
        priceEditText = binding.priceEdittext;
        perPersonRadio = binding.perPersonRadio;
        wholeUnitRadio = binding.wholeUnitRadio;
        autoApproveCheckbox = binding.autoApproveCheckbox;
        cancellationDeadlineEditText = binding.cancellationDeadlineEditText;
        pricingModeRadioGroup = binding.pricingModeRadioGroup;
        addButton = binding.addButton;
        deleteButton = binding.deleteButton;
        availabilityEntriesRecyclerView = binding.availabilityEntriesRecyclerView;
    }
}
