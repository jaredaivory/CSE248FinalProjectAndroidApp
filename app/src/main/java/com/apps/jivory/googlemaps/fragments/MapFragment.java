package com.apps.jivory.googlemaps.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.activities.MainActivity;
import com.apps.jivory.googlemaps.models.PlaceData;
import com.apps.jivory.googlemaps.models.LatitudeLongitude;
import com.apps.jivory.googlemaps.models.PlacesAutoCompleteAdapter;
import com.apps.jivory.googlemaps.models.Post;
import com.apps.jivory.googlemaps.models.PostHashMap;
import com.apps.jivory.googlemaps.observers.FirebaseObserver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, FirebaseObserver {
    public static final String TAG = "MapFragment";
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds latLngBounds = new LatLngBounds(
            new com.google.android.gms.maps.model.LatLng(39.116312, -81.139465),
            new com.google.android.gms.maps.model.LatLng(45.308249, -71.417172));
    private final int PLACE_PICKER_REQUEST = 1;

    private PlacesAutoCompleteAdapter placesAutoCompleteAdapter;
    private AutoCompleteTextView editTextSearch;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;

    private View view;
    private Marker currentMarker;

    private PostHashMap postHashMap;
    private ArrayList<Post> posts;

    public MapFragment(){

    }

    public MapFragment(PostHashMap posts){
        this.postHashMap = posts;
    }

    /** View **/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        editTextSearch = view.findViewById(R.id.map_search);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }
    private void initializeViews() {
        Log.d(TAG, "init: initializing");

        FloatingActionButton fabPost = (FloatingActionButton) view.findViewById(R.id.fab);
        fabPost.setOnClickListener(createEventListener);

        FloatingActionButton fabMyLocation = (FloatingActionButton) view.findViewById(R.id.myLocation);
        fabMyLocation.setOnClickListener(view -> {
            getDeviceLocation();
            Log.d(TAG, "Moving to Mylocation");
        });


        placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getActivity(), MainActivity.googleApiClient, latLngBounds, null);
        editTextSearch.setAdapter(placesAutoCompleteAdapter);

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN ||
                    event.getAction() == KeyEvent.KEYCODE_ENTER) {
                editTextSearch.setText("");
            }
            return false;
        });

        editTextSearch.setOnItemClickListener(locationSearchItemClickListener);
        refresh();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        postHashMap.registerObserver(this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getActivity(), "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json_retro));

        if (MainActivity.mLocationPermissionsGranted) {
            Location currentLocation = getDeviceLocation();
            resetCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            initializeViews();
        } else {
            Log.d(TAG, "onMapReady: Location Permissions not granted");
        }
    }

    public void refresh(){
        this.posts = new ArrayList<>(postHashMap.values());
        mMap.clear();
        for(Post p: posts){
            LatitudeLongitude latitudeLongitude = p.getPlaceData().getLatLng();
            LatLng latLng = new LatLng(latitudeLongitude.getLatitude(), latitudeLongitude.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(p.getTitle())
                    .snippet(p.getDescription())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
        }
    }


    /** Called once an activity returns its result**/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
                PlaceData placeData = new PlaceData(place);

                Post p = new Post(placeData);

                EditPostFragment dialogFragment = new EditPostFragment(p);
                dialogFragment.show(getFragmentManager(),"CreatePost");
            }
        }
    }
    /** **/



    /** Listeners *************************/
    private AdapterView.OnItemClickListener locationSearchItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            final AutocompletePrediction item = placesAutoCompleteAdapter.getItem(position);
            final String placeID = item.getPlaceId();


            PendingResult<PlaceBuffer> result = Places.GeoDataApi.getPlaceById(MainActivity.googleApiClient, placeID);
            result.setResultCallback(places -> {
                if (places.getStatus().isSuccess()) {
                    Place place = places.get(0);
                    Log.d(TAG, "place: " + place.toString());
                    geoLocate();
                    closeKeyboard();
                    moveCamera(place.getLatLng(), DEFAULT_ZOOM);
                } else {
                    Log.d(TAG, "autocomplete-:places");
                }
            });
        }
    };
    private View.OnClickListener createEventListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(currentMarker!= null){
                showPlaces(currentMarker.getPosition());
            }
            showPlaces();
        }
    };
    /***************************/




    /** Place Picker **/
    public void showPlaces(LatLng latLng) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        LatLng northeast = new LatLng(latLng.latitude+.002, latLng.longitude+.002);
        LatLng southwest = new LatLng(latLng.latitude-.002, latLng.longitude-.002);
        builder.setLatLngBounds(new LatLngBounds(southwest, northeast));
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }
    public void showPlaces() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }
    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocationg");

        String searchString = editTextSearch.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            LatitudeLongitude latitudeLongitude = new LatitudeLongitude(address.getLatitude(), address.getLongitude());
            moveCamera(latitudeLongitude, DEFAULT_ZOOM);
            Log.d(TAG, "geoLocate: location " + address.toString());
        }
        editTextSearch.setText("");
    }
    /** **/

    /** Camera functions*/
    private Location getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        final Location currentLocation = new Location("");
        try {
            if (MainActivity.mLocationPermissionsGranted) {
                Task location = mFusedLocationClient.getLastLocation();
                Log.d(TAG, "getDeviceLocation: " + location);
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation.set((Location) task.getResult());
                            resetCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            Log.d(TAG, "onComplete: " + currentLocation.toString());
                        } else {
                            Log.d(TAG, "onComplete: current location is null ");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: " + e.getMessage());
        }
        return currentLocation;
    }
    private void moveCamera(LatitudeLongitude latitudeLongitude, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latitudeLongitude.getLatitude() + ", lng: " + latitudeLongitude.getLongitude());
        LatLng googleLatLng = new LatLng(latitudeLongitude.getLatitude(), latitudeLongitude.getLongitude());

        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(googleLatLng));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLatLng, zoom));

    }
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: " + latLng.toString());

        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }
    private void moveCamera(LatitudeLongitude latitudeLongitude, float zoom, Place place) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latitudeLongitude.getLatitude() + ", lng: " + latitudeLongitude.getLongitude());
        LatLng googleLatLng = new LatLng(latitudeLongitude.getLatitude(), latitudeLongitude.getLongitude());

        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(googleLatLng));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLatLng, zoom));
    }
    private void resetCamera(LatLng latLng, float zoom){
        Log.d(TAG, "resetCamera:");
        if (currentMarker != null) {
            currentMarker.remove();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /** Extra functions**/
    private void closeKeyboard() {
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /** Firebase posts observer **/
    @Override
    public void onChanged() {
        this.posts = new ArrayList<>(postHashMap.values());
        Log.d(TAG, "onChanged: ");
        refresh();
    }
}
