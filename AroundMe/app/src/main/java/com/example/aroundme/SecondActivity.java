package com.example.aroundme;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.CameraPosition;


public class SecondActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ListView mylist;
    private ListAdapter adapter;
    private Context context = this;


    List<ListItem> items = new ArrayList<>();
    private Button addButton;
    private Button removeButton;

    private GoogleMap mMap;
    private static final float DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Location mCurrentLocation;
    private Location  mLastKnownLocation;
    private CameraPosition  mCameraPosition;
    private FusedLocationProviderClient  mFusedLocationProviderClient;

    // The entry points to the Places API.
    //private GeoDataClient mGeoDataClient;
    //private PlaceDetectionClient mPlaceDetectionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondactivitydesign);


        addButton = (Button)findViewById(R.id.addButton);
        removeButton = (Button) findViewById(R.id.removeButton);

        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a GeoDataClient.
       // mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        //mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = (FusedLocationProviderClient)LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        addButton.setOnClickListener(addButtonClick);
        removeButton.setOnClickListener(removeButtonClick);

        mylist = (ListView) findViewById(R.id.listViewdepreceded);



        Intent intent = getIntent();/*
        if(getIntent().getBooleanExtra("flag", true))
        {
            items.add(new ListItem("Jack", R.drawable.ic_3d_rotation_black_48dp, "Mathematics, Chemistry"));
            items.add(new ListItem("Jane", R.drawable.ic_3d_rotation_black_48dp, "Physics, Informatics"));
            items.add(new ListItem("Bob", R.drawable.ic_3d_rotation_black_48dp, "Mathematics, Informatics"));
            items.add(new ListItem("Clara", R.drawable.ic_3d_rotation_black_48dp, "Geography, Chemistry"));
            items.add(new ListItem("Sam", R.drawable.ic_3d_rotation_black_48dp, "Mathematics, Physics"));
        }
        else
        {//ic_3d_add_black_48dp
            items.add(new ListItem("Mathematics", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasdfjhjhgdsv ughfdshufdsiafdshjkfarjhi" + "sapcee and change"));
            items.add(new ListItem("Physics", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasaaaaaaaaaaaadshjkfarjhi" + "sapcee and change"));
            items.add(new ListItem("Chemistry", R.drawable.ic_3d_rotation_black_48dp, "Mat testfabbbbbbbbbbbbbbbbshjkfarjhi" + "sapcee and change"));
            items.add(new ListItem("Informatics", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasdcccccccccccccccafdshjkfarjhi" + "sapcee and change"));
            items.add(new ListItem("Geography", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasdfjddddddddddddddddddfarjhi" + "sapcee and change"));
        }*/
        adapter = new ListAdapter(this, items);
        mylist.setAdapter((adapter));


        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //Log.i("list item Click", "Position: "+position+" id: "+id);
                //Log.i("list item Click INFO", "Title: "+items.get(position).getTitle()+" desc: "+items.get(position).getDescription()+" image?: "+items.get(position).getImageId());

                Intent intent = new Intent(context, EventInfo.class);
                //based on item add info to intent
                intent.putExtra("Title", items.get(position).getTitle());
                intent.putExtra("Description", items.get(position).getDescription());
                intent.putExtra("ImageId", items.get(position).getImageId());
                intent.putExtra("myItemList", (Serializable) items);
                context.startActivity(intent);
            }
        });
    }

    View.OnClickListener addButtonClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //items.add(new ListItem("Pridetas", R.drawable.ic_3d_add_black_48dp, "Added list item" + "sapcee and change"));
            adapter.notifyDataSetChanged();
        }
    };

    View.OnClickListener removeButtonClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(items.size() > 0)
            {
                items.remove(items.size()-1);
                adapter.notifyDataSetChanged();
            }
        }
    };



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            //if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //} else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            //}
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            //if (mLocationPermissionGranted) {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d("tagas", "Current location is null. Using defaults.");
                            Log.e("tagas", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            //}
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
}
