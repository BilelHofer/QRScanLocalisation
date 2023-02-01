package com.example.qrscanlocalisation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class MapFragment extends Fragment {

    private LatLng lat;
    private LatLng oldLat;
    private PageViewModel pageViewModel;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    // constructor
    public MapFragment(LatLng _lat) {
        // Required empty public constructor
        lat = _lat;
        oldLat = new LatLng(0, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialise ViewModel
        pageViewModel = ViewModelProviders.of(requireActivity()).get(PageViewModel.class);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (oldLat != lat) {
                    pageViewModel.setCoordinateLat("Ma localisation: " + String.valueOf(df.format(lat.latitude)) + " : " + String.valueOf(df.format(lat.longitude)));
                    oldLat = lat;
                }

                // Permet au handler de ce répéter
                mHandler.postDelayed(mRunnable, 100);
            }
        };

        // Lance le handler
        mHandler.postDelayed(mRunnable, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        // Initialise la map
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // Crée le marker de la position
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(lat);
                markerOptions.title(lat.latitude + " : " + lat.longitude);
                // Anmiating to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        lat, 10
                ));
                // Add marker on map
                googleMap.addMarker(markerOptions);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When click on map
                        // Initialize marker options
                        MarkerOptions markerOptions = new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Upadte this.lat
                        lat = latLng;
                        // Set title of marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Anmiating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng, 10
                        ));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                    };
                });
            }
        });

        Button backButton = view.findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).goHome();
            }
        });

        // Return view
        return view;
    }
}