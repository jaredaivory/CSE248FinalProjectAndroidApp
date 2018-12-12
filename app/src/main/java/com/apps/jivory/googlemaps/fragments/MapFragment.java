package com.apps.jivory.googlemaps.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.activities.MainActivity;
import com.apps.jivory.googlemaps.models.LatitudeLongitude;
import com.apps.jivory.googlemaps.models.PlacesAutoCompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "MapFragment";
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds latLngBounds = new LatLngBounds(new com.google.android.gms.maps.model.LatLng(-85,-180), new com.google.android.gms.maps.model.LatLng(85,180));


    /** Places API **/
    private PlacesAutoCompleteAdapter placesAutoCompleteAdapter;
    private AutoCompleteTextView editTextSearch;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Marker currentMarker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        editTextSearch = v.findViewById(R.id.map_search);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }


    private void initializeViews(){
        Log.d(TAG, "init: initializing");



        placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getActivity(), MainActivity.googleApiClient, latLngBounds, null);
        editTextSearch.setAdapter(placesAutoCompleteAdapter);
        
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (    actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN ||
                    event.getAction() == KeyEvent.KEYCODE_ENTER) {
                editTextSearch.setText("");
            }
            return false;
        });

        editTextSearch.setOnItemClickListener((parent, view, position, id) -> {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            final AutocompletePrediction item = placesAutoCompleteAdapter.getItem(position);
            final String placeID = item.getPlaceId();

            PendingResult<PlaceBuffer> result = Places.GeoDataApi.getPlaceById(MainActivity.googleApiClient, placeID);
            result.setResultCallback(places -> {
                if(places.getStatus().isSuccess()){
                    Place place = places.get(0);
                    Log.d(TAG, "place: " + place.getName());
                    geoLocate();
                    closeKeyboard();
                } else {
                    Log.d(TAG, "autocomplete-:places");
                }
                places.release();
            });

        });
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocationg");

        String searchString = editTextSearch.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size() > 0){
            Address address = list.get(0);
            LatitudeLongitude latitudeLongitude = new LatitudeLongitude(address.getLatitude(), address.getLongitude());
            moveCamera(latitudeLongitude, DEFAULT_ZOOM);
            Log.d(TAG, "geoLocate: location " + address.toString());
        }
        editTextSearch.setText("");
    }

    private void closeKeyboard(){
        View view = getView();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getActivity(), "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json_retro));

        if (MainActivity.mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            initializeViews();

            mMap.getUiSettings().setAllGesturesEnabled(true);
        } else {
            Log.d(TAG, "onMapReady: Location Permissions not granted");
        }
    }


    private Location getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Location currentLocation = null;
        try{
            if(MainActivity.mLocationPermissionsGranted){
                Task location = mFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatitudeLongitude(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else{
                            Log.d(TAG, "onComplete: current location is null ");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: " + e.getMessage());
        }
        return currentLocation;
    }

    private void moveCamera(LatitudeLongitude latitudeLongitude, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latitudeLongitude.getLatitude() + ", lng: " + latitudeLongitude.getLongitude());
        LatLng googleLatLng = new LatLng(latitudeLongitude.getLatitude(), latitudeLongitude.getLongitude());

        if(currentMarker != null) {
            currentMarker.remove();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLatLng, zoom));
        currentMarker = mMap.addMarker(new MarkerOptions().position(googleLatLng));
    }

    private void moveCamera(LatitudeLongitude latitudeLongitude, float zoom, Place place){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latitudeLongitude.getLatitude() + ", lng: " + latitudeLongitude.getLongitude());
        LatLng googleLatLng = new LatLng(latitudeLongitude.getLatitude(), latitudeLongitude.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLatLng, zoom));
        currentMarker = mMap.addMarker(new MarkerOptions().position(googleLatLng));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
