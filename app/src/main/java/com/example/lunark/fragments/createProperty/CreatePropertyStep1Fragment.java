package com.example.lunark.fragments.createProperty;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lunark.BuildConfig;
import com.example.lunark.databinding.FragmentCreatePropertyStep1Binding;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class CreatePropertyStep1Fragment extends Fragment implements Step {

    FragmentCreatePropertyStep1Binding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePropertyStep1Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpNumberPickers();
        loadMap(45.2432787, 19.8467293);
        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

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

        //your items
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
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
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Log.d("Map", p.getLongitude() + " " + p.getLatitude());
                items.clear();
                items.add(new OverlayItem("Your property", "", p));
                mOverlay.setFocusedItem(items.get(0));
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

        mOverlay.setFocusItemsOnTap(true);
        map.getOverlayManager().add(mOverlay);
        map.getOverlayManager().add(mapEventsOverlay);
    }
}
