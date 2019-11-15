package com.example.aroundme.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.aroundme.EventInfo;
import com.example.aroundme.ListAdapter;
import com.example.aroundme.ListItem;
import com.example.aroundme.ModelPost;
import com.example.aroundme.R;
import com.example.aroundme.RequestOperator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class DashboardFragment extends Fragment implements OnMapReadyCallback, RequestOperator.RequestOperatorListener {


    private DashboardViewModel dashboardViewModel;


    private ListView mylist;
    private ListAdapter adapter;
    private Context context = getActivity();


    List<ListItem> items = new ArrayList<>();


    private GoogleMap mMap;
    private static final float DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Location mCurrentLocation;
    private static Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The entry points to the Places API.
    //private GeoDataClient mGeoDataClient;
    //private PlaceDetectionClient mPlaceDetectionClient;

    private ModelPost[] publication;
    TextView title;
    TextView bodyText;

    Button searchButton;
    private static int inputDistance = 20;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
      // final TextView textView = root.findViewById(R.id.text_dashboard);


        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        mFusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(getActivity());


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mylist = root.findViewById(R.id.listview);

        searchButton = root.findViewById(R.id.searchButton);
        title = root.findViewById(R.id.title);
        bodyText = root.findViewById(R.id.bodyText);

        searchButton.setOnClickListener(searchButtonListener);


        items.add(new ListItem("Mathematics", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasdfjhjhgdsv ughfdshufdsiafdshjkfarjhi" + "sapcee and change"));
        items.add(new ListItem("Physics", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasaaaaaaaaaaaadshjkfarjhi" + "sapcee and change"));
        items.add(new ListItem("Chemistry", R.drawable.ic_3d_rotation_black_48dp, "Mat testfabbbbbbbbbbbbbbbbshjkfarjhi" + "sapcee and change"));
        items.add(new ListItem("Informatics", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasdcccccccccccccccafdshjkfarjhi" + "sapcee and change"));
        items.add(new ListItem("Geography", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasdfjddddddddddddddddddfarjhi" + "sapcee and change"));

        adapter = new ListAdapter(getActivity(), items);
        mylist.setAdapter((adapter));


        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //Log.i("list item Click", "Position: "+position+" id: "+id);
                Log.i("list item Click INFO", "Title: "+items.get(position).getTitle()+" desc: "+items.get(position).getDescription()+" image?: "+items.get(position).getImageId());
                /*
                Intent intent = new Intent(context, EventInfo.class);
                //based on item add info to intent
                intent.putExtra("Title", items.get(position).getTitle());
                intent.putExtra("Description", items.get(position).getDescription());
                intent.putExtra("ImageId", items.get(position).getImageId());
                intent.putExtra("myItemList", (Serializable) items);
                context.startActivity(intent);*/



            }
        });


        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });

        return root;
    }

    View.OnClickListener searchButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendRequest();
            }
    };


/*

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondactivitydesign);


        //addButton = (Button)findViewById(R.id.addButton);
        //removeButton = (Button) findViewById(R.id.removeButton);

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

        mylist = (ListView) findViewById(R.id.listView);



        Intent intent = getIntent();
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
        }
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
            items.add(new ListItem("Pridetas", R.drawable.ic_3d_add_black_48dp, "Added list item" + "sapcee and change"));
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
    };*/


    public void sendRequest()
    {
        Log.i("Sent Request", "issiusta uzklausa");
        RequestOperator ro = new RequestOperator();
        ro.setListener(this);
        ro.start();
    }

    public void updatePublication()
    {
        getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run()
           {
               if(publication != null)
               {

                   adapter.clear();

                   LatLng location = new LatLng(0, 0);

                   if(publication.length > 0) location = new LatLng(publication[0].getLocation()[0], publication[0].getLocation()[1]);
                    else getDeviceLocation();

                    Log.i("aaa", "ilgis: "+publication.length);

                   for(int i = 0; i < publication.length; i++)
                   {
                       Log.i("bbb", "interation: "+i);
                       double[] eventLoc = publication[i].getLocation();

                       location = new LatLng(eventLoc[1], eventLoc[0]);
                       mMap.addMarker(new MarkerOptions().position(location).title("\""+publication[i].getTitle()+"\""));



                       items.add(new ListItem(publication[i].getTitle(), R.drawable.ic_people, publication[i].getName()+"\n"
                               + publication[i].getFormatted_address()));


                       /*items.add(new ListItem("Physics", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasaaaaaaaaaaaadshjkfarjhi" + "sapcee and change"));
                       items.add(new ListItem("Chemistry", R.drawable.ic_3d_rotation_black_48dp, "Mat testfabbbbbbbbbbbbbbbbshjkfarjhi" + "sapcee and change"));
                       items.add(new ListItem("Informatics", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasdcccccccccccccccafdshjkfarjhi" + "sapcee and change"));
                       items.add(new ListItem("Geography", R.drawable.ic_3d_rotation_black_48dp, "Mat testfasdfjddddddddddddddddddfarjhi" + "sapcee and change"));
                        */


                   }

                   adapter = new ListAdapter(getActivity(), items);
                   mylist.setAdapter((adapter));

                   mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                   //title.setText(publication[0].getTitle());
                   //bodyText.setText(publication[0].getName());



               } else {
                   title.setText("");
                   bodyText.setText("");
               }
           }
        });
    }

    @Override
    public void success(ModelPost[] publication)
    {
        this.publication = publication;
        updatePublication();
    }

    @Override
    public void failed(int responseCode)
    {
        this.publication = null;
        updatePublication();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        // Add a marker in Sydney and move the camera
        /*
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
            getLocationPermission();
            }
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
            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {
                        mLastKnownLocation = (Location) task.getResult();

                        LatLng pos = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                        float meters_per_pixel = (float)( 156543.03392 * Math.cos(pos.latitude * Math.PI / 180) / Math.pow(2, mMap.getMinZoomLevel()));

                        // Set the map's camera position to the current location of the device.

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), meters_per_pixel));


                        Circle circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()))
                                .radius(inputDistance*1000)
                                .strokeColor(0xBBFF0000)
                                .fillColor(0xAA00BCD4));

                    } else
                        {
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

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
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


    public static Location GetCurrentLocation()
    {
        return mLastKnownLocation;
    }

    public static int GetEventDistance()
    {
        return inputDistance;
    }
}