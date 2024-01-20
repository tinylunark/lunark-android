package com.example.lunark.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lunark.BuildConfig;
import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.adapters.ReviewListAdapter;
import com.example.lunark.databinding.FragmentPropertyDetailBinding;
import com.example.lunark.datasources.AccountRepository;
import com.example.lunark.models.Host;
import com.example.lunark.models.Property;
import com.example.lunark.models.Review;
import com.example.lunark.models.ReviewType;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.repositories.ReviewRepository;
import com.example.lunark.util.ClientUtils;
import com.example.lunark.viewmodels.PropertyDetailViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class PropertyDetailFragment extends Fragment {
    private FragmentPropertyDetailBinding binding;
    private static final String PROPERTY_ID = "propertyId";
    private static final String TAG = "PROPERTY_DETAIL_FRAGMENT";
    private Long propertyId;
    private PropertyDetailViewModel viewModel;
    @Inject
    AccountRepository accountRepository;
    @Inject
    LoginRepository loginRepository;
    @Inject
    ReviewRepository reviewRepository;
    private Disposable subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((LunarkApplication) getActivity().getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PropertyDetailViewModel.class);

        if (getArguments() != null) {
            propertyId = getArguments().getLong(PROPERTY_ID);
            viewModel.initProperty(propertyId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPropertyDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getProperty().observe(getViewLifecycleOwner(), property -> {
            binding.name.setText(property.getName());
            binding.hostNameTextview.setText(property.getHost().getFullName());
            binding.location.setText(property.getAddress().getCity() + ", " + property.getAddress().getCountry());
            binding.description.setText(property.getDescription());
            binding.minGuestsValue.setText(String.format("%d", property.getMinGuests()));
            binding.maxGuestsValue.setText(String.format("%d", property.getMaxGuests()));
            checkEligibilityToReviewProperty();

            if (property.getAverageRating() != null) {
                binding.rating.setText(String.format("%.2f", property.getAverageRating()));
            } else {
                binding.rating.setText(R.string.no_ratings);
            }

            if (property.getImages().size() > 0) {
                Glide.with(this)
                        .load(ClientUtils.SERVICE_API_PATH + "properties/" + property.getId() + "/images/" + property.getImages().get(0).getId())
                        .into(binding.thumbnail);
            }

            loadHostProfilePicture(property.getHost());

            if (property.getAmenities().isEmpty()) {
                TextView tv = new TextView(getContext());
                tv.setText(R.string.none);
                binding.amenitiesList.addView(tv);
            } else {
                property.getAmenities().forEach(amenity -> {
                    TextView tv = new TextView(getContext());
                    tv.setText(amenity.getName());
                    binding.amenitiesList.addView(tv);
                });
            }

            loadMap(property.getLatitude(), property.getLongitude());
            viewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> setUpReviewsRecyclerView(reviews, property.getHost().getId()));
            setUpHostLink(property);
        });


        accountRepository.getFavoriteProperties().observe(getViewLifecycleOwner(), favorites -> {
            boolean isFavorite = favorites.stream().anyMatch(favorite -> favorite.getId().equals(propertyId));
            binding.favoriteCheckbox.setChecked(isFavorite);
        });

        binding.favoriteCheckbox.setOnClickListener(v -> {
            if (binding.favoriteCheckbox.isChecked()) {
                accountRepository.addFavoriteProperty(propertyId);
            } else {
                accountRepository.deleteFavoriteProperty(propertyId);
            }
        });

        binding.writeReviewButton.setOnClickListener(v -> openWriteReviewFragment());

        binding.bookButton.setOnClickListener(v -> openBookingRequestFragment());
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

    private void setUpReviewsRecyclerView(List<Review> reviews, Long hostId) {
        RecyclerView recyclerView = binding.reviews;
        boolean reportingAllowed = hostId.equals(loginRepository.getLogin().blockingGet().getProfileId());
        ReviewListAdapter adapter = new ReviewListAdapter(this, reviews, reportingAllowed);
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void checkEligibilityToReviewProperty() {
        this.reviewRepository.isEligibleToReviewProperty(propertyId).subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                subscription = d;
            }

            @Override
            public void onSuccess(Boolean eligible) {
                binding.writeReviewButton.setEnabled(eligible);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Error while checking property review eligibility: " + e.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        if(subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        super.onDestroy();
    }

    private void openWriteReviewFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong(WriteReviewFragment.REVIEWED_ENTITY_ID, this.propertyId);
        bundle.putString(WriteReviewFragment.REVIEW_TYPE, ReviewType.PROPERTY.toString());
        getParentFragmentManager().setFragmentResult("review", bundle);
    }

    private void loadHostProfilePicture(Host host) {
        Glide.with(this)
                .load(ClientUtils.SERVICE_API_PATH + "accounts/" + host.getId() + "/profile-image")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_account)
                .into(binding.hostProfilePictureImageview);
    }

    private void setUpHostLink(Property property) {
        binding.hostedByLinear.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong(HostPageFragment.HOST_ID_KEY, property.getHost().getId());
            getParentFragmentManager().setFragmentResult(HostPageFragment.REQUEST_KEY, bundle);
        });
    }

    private void openBookingRequestFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong(BookingRequestFragment.PROPERTY_ID, this.propertyId);
        getParentFragmentManager().setFragmentResult("booking", bundle);
    }
}