package com.apps.jivory.googlemaps.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Observable;
import android.os.Bundle;

import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.UserObserver;
import com.apps.jivory.googlemaps.fragments.EditPostFragment;
import com.apps.jivory.googlemaps.fragments.MapFragment;
import com.apps.jivory.googlemaps.fragments.PostsFragment;
import com.apps.jivory.googlemaps.fragments.UserFragment;
import com.apps.jivory.googlemaps.models.LatitudeLongitude;
import com.apps.jivory.googlemaps.models.User;
import com.apps.jivory.googlemaps.viewmodels.MainViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.apps.jivory.googlemaps.models.Post;
import com.google.firebase.database.DataSnapshot;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, EditPostFragment.EditPostFragmentListener, UserFragment.UserFragmentListener, UserObserver.UserListener {
    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2001;

    public static Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    public static MainViewModel mainViewModel;
    private List<Post> postList;

    private MapFragment mapFragment;

    public static GoogleApiClient googleApiClient;

    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initializeViews();
        getLocationPerms();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }


    @Override
    protected void onStart() {
        super.onStart();
        /**
         * Start Listening for data changes.
         */

        // Observes the userdata provided from the database.
        LiveData<DataSnapshot> userData = mainViewModel.getUserData();
        userData.observe(this, new UserObserver(this));

    }

    private void initializeViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditPostFragment dialogFragment = new EditPostFragment();
                dialogFragment.show(getSupportFragmentManager(),"CreatePost");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
    }

    /**Overrided methods for the navigation menu */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch(item.getItemId()){
            case(R.id.nav_account):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserFragment(currentUser)).commit();
                break;
            case(R.id.nav_map):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapFragment() ).commit();
                break;
            case(R.id.nav_posts):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new PostsFragment()).commit();
            default:
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /** */

    /** Location permissions **/
    private void getLocationPerms(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionsGranted = true;
        } else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                for(int i: grantResults){
                    if(i != PackageManager.PERMISSION_GRANTED){
                        mLocationPermissionsGranted = false;
                        return;
                    }
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /** CreatePostFragment Result**/
    @Override
    public void onPostSaved(Post p) {
        if(p.getCreator()!=null) {
            mainViewModel.updateNewPost(p);
            Toast.makeText(this, "Updated Post", Toast.LENGTH_SHORT).show();
        } else{
            mainViewModel.insertNewPost(p);
            Toast.makeText(this, "Posted Event" + p.getTitle(), Toast.LENGTH_SHORT).show();
        }

    }

    /** UserFragment Result**/
    @Override
    public void onUserSave(User user) {
        Log.d(TAG, "onUserSave: " + user.toString());
        mainViewModel.updateUser(user);
    }

    /** Listener for user data changes**/
    @Override
    public void onUserChanged(User user) {
        currentUser = user;

        String fullname = currentUser.getFullname();
        TextView textViewName = findViewById(R.id.header_Username);
        textViewName.setText(fullname);
        String emailaddress = currentUser.getEmailaddress();
        TextView textViewEmail = findViewById(R.id.header_EmailAddress);
        textViewEmail.setText(emailaddress);
    }
    /**  **/


}
