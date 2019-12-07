package com.example.aroundme.ui.dashboard;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.aroundme.EventInfo;
import com.example.aroundme.IndicatingView;
import com.example.aroundme.ListAdapter;
import com.example.aroundme.ListItem;
import com.example.aroundme.ModelPost;
import com.example.aroundme.R;
import com.example.aroundme.RequestOperator;
import com.github.kimkevin.cachepot.CachePot;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DashboardFragment extends Fragment implements OnMapReadyCallback, RequestOperator.RequestOperatorListener, AdapterView.OnItemSelectedListener{


    private DashboardViewModel dashboardViewModel;

    private Context context = getActivity();


    public static List<ListItem> EventList;

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
    Circle circle;
    // The entry points to the Places API.
    //private GeoDataClient mGeoDataClient;
    //private PlaceDetectionClient mPlaceDetectionClient;

    private ModelPost[] publication;
    //private ModelPost[] publication2;
    private ModelPost[] publicationfinal;

    Button searchButton;
    private static int inputDistance = 20;
    private static int inputOffset = 0;
    private static String fromDate = "";
    private static String toDate = "";
    private static String SelectedCategory = "";
    private boolean requestDone = false;

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();
    private String m_Text = "";
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date2;
    private String dateString = "";

    private Fragment mMyFragment;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        //get the spinner from the xml.
        //Spinner dropdown = root.findViewById(R.id.categoriesList);
        //create a list of items for the spinner.
        //String[] items = new String[]{"1", "2", "three"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        //ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_array, android.R.layout.simple_spinner_item);
        //set the spinners adapter to the previously created one.
        //dropdown.setAdapter(staticAdapter);




        if (savedInstanceState != null) {
            Log.e("UZKRAUTA", "uzkrauta Search (DashBoard)");

            mMyFragment = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "DashboardFragment");
            //Restore the fragment's instance
            Log.e("LOADED", "uzkrauta issaugota info");

            publicationfinal = (ModelPost[]) savedInstanceState.getParcelableArray("Publication");

            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);

            LatLng location;


            for(int i = 0; i < publicationfinal.length; i++) {
                double[] eventLoc = publicationfinal[i].getLocation();

                location = new LatLng(eventLoc[1], eventLoc[0]);
                mMap.addMarker(new MarkerOptions().position(location).title("\"" + publicationfinal[i].getTitle() + "\""));
            }
        }

        try{
            publicationfinal = CachePot.getInstance().pop("DashBoard");
        } catch (Exception ex)
        {
            Log.e("Exception", "null");
        }

        Log.e("Veikia", "veikia on create view");




        mFusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(getActivity());


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        searchButton = root.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(searchButtonListener);

        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });



        //EditText edittext= (EditText) root.findViewById(R.id.Birthday);





        return root;
    }
/*
    edittext.setOnClickListener(new DialogInterface.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            new DatePickerDialog(classname.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    });*/


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateString = sdf.format(myCalendar.getTime());
        //edittext.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        Log.e("Listas", "pasirinkta"+position);
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected

                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }


    final View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.searchtitleviewlayout, null);
            builder.setCustomTitle(view);
            //builder.setTitle("Filters");

            // Set up the input
            //final EditText input = new EditText(getContext());
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            //input.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
            //builder.setView(input);


            //Context context = mapView.getContext();
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);

            // Add a TextView here for the "Title" label, as noted in the comments
            final Spinner titleBox = new Spinner(getContext());
            //titleBox.setHint("Title");

            ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_array, R.layout.spinneritemstyle);
            //set the spinners adapter to the previously created one.
            titleBox.setAdapter(staticAdapter);

            layout.addView(titleBox); // Notice this is an add method


            final EditText distanceInput = new EditText(getContext());
            distanceInput.setHint("Distance from you (in km)");
            distanceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NULL);
            layout.addView(distanceInput);

            /*
            final EditText dateInputTo = new EditText(getContext());
            dateInputTo.setHint("To event date");
            dateInputTo.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_NULL);*/

            final TextView dateInputFrom = new TextView(getContext());
            dateInputFrom.setText("From event date");
            dateInputFrom.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
            dateInputFrom.setPadding(20, 30, 0, 0);
            layout.addView(dateInputFrom);


            final TextView dateInputTo = new TextView(getContext());
            dateInputTo.setText("To event date");
            dateInputTo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
            dateInputTo.setPadding(20, 20, 0, 0);
            layout.addView(dateInputTo);

            builder.setView(layout); // Again this is a set method, not add

//<item name="android:background">@color/colorBackgroundTransparent</item>
                /*
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/


            dateInputFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getContext(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            dateInputTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getContext(), date2, myCalendar2
                            .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                            myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    //updateLabel();

                    String myFormat = "Y-MM-dd"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                    dateString = sdf.format(myCalendar.getTime());
                    fromDate = dateString;
                    dateInputFrom.setText(dateString);
                }

            };

            date2 = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar2.set(Calendar.YEAR, year);
                    myCalendar2.set(Calendar.MONTH, monthOfYear);
                    myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    //updateLabel();

                    if(dateInputFrom.getText().toString().isEmpty())
                    {
                        dateInputTo.setText("");
                        dateInputTo.setHint("Fill From Date first");
                    }
                    else {

                        String myFormat = "Y-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        Date d = null;

                       // Log.e("testas", "pries vertima");

                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
                        try {
                            d = sdf2.parse(dateInputFrom.getText().toString());
                        } catch (ParseException ex) {
                            Log.v("Exception", ex.getLocalizedMessage());
                        }
                        long month = 657450000;//divided by 4

                        //Log.e("testas", "pries null");

                        if (d != null) {
                            //Log.e("testas", "toDate:"+myCalendar2.getTimeInMillis()+" FromDate: "+myCalendar.getTimeInMillis()+" addedTime:"+(myCalendar.getTimeInMillis() + (month * 4)));
                            //Log.e("testas", "toDate:"+dateInputFrom.getText().toString()+" FromDate: "+d.toString()+" addedTime:"+(d.getTime() + (month * 4)));
                            if (myCalendar2.getTimeInMillis() < (myCalendar.getTimeInMillis() + (month * 4)) && myCalendar2.getTimeInMillis() >= myCalendar.getTimeInMillis()) {
                                Log.e("testas", "successful");

                                dateString = sdf.format(myCalendar2.getTime());
                                toDate = dateString;
                                dateInputTo.setText(dateString);
                            } else {
                                Log.e("testas", "failed");
                                dateInputTo.setText("");
                                dateInputTo.setHint("FromDate <= ToDate < FromDate + 1 Month");
                            }
                        }
                    }
                }

            };

            // Set up the buttons
            builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //m_Text = input.getText().toString();
                    SelectedCategory = titleBox.getSelectedItem().toString();

                    //distance from user
                    String value= distanceInput.getText().toString();
                    inputDistance = Integer.parseInt(value);

                    //resetting request/ map markers/ map distance circle
                    requestDone = false;
                    inputOffset = 0;
                    mMap.clear();
                    getDeviceLocation();
                    sendRequest();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            //sendRequest();
        }
    };




    public void setIndicatorStatus(final IndicatingView Indicator, final int status)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Indicator.setState(status);
                Indicator.invalidate();
            }
        });
    }


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
               if(publicationfinal != null)
               {
                   getDeviceLocation();

                   LatLng location = new LatLng(0, 0);

                   if(publicationfinal.length > 0) location = new LatLng(publicationfinal[0].getLocation()[0], publicationfinal[0].getLocation()[1]);
                    else getDeviceLocation();

                    Log.i("aaa", "ilgis: "+publicationfinal.length);

                   CachePot.getInstance().push(1, publicationfinal);

                   for(int i = 0; i < publicationfinal.length; i++)
                   {
                       Log.i("bbb", "interation: "+i);
                       double[] eventLoc = publicationfinal[i].getLocation();

                       location = new LatLng(eventLoc[1], eventLoc[0]);
                       mMap.addMarker(new MarkerOptions().position(location).title("\""+publicationfinal[i].getTitle()+"\""));


                       items.add(new ListItem(publicationfinal[i].getTitle(), R.drawable.ic_people, publicationfinal[i].getName()+"\n"
                               + publicationfinal[i].getFormatted_address()));

                   }
                   EventList = items;

                   mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

               } else {
                    //FAILED
               }
           }
        });
    }

    @Override
    public void success(ModelPost[] publication)
    {
        //this.publication2 = publication;
        if(requestDone)
        {
            Log.e("testas", "3");
            publicationfinal = new ModelPost[this.publication.length + publication.length];
            int index = this.publication.length;

            for (int i = 0; i < this.publication.length; i++) {
                publicationfinal[i] = this.publication[i];
            }
            for (int i = 0; i < publication.length; i++) {
                publicationfinal[i + index] = publication[i];
            }
            Log.e("testas", "4");
            updatePublication();
        }
        else
        {
            this.publication = publication;
            requestDone = true;
            inputOffset = 10;
            sendRequest();
        }
    }

    @Override
    public void failed(int responseCode)
    {
        this.publication = null;
        publicationfinal = null;
        updatePublication();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("onSaveInstanceState","saugom dashboard");
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);

        }

        outState.putParcelableArray("Publication", publicationfinal);

        //Save the fragment's instance
        //getActivity().getSupportFragmentManager().putFragment(outState, "DashboardFragment", this);

    }

    @Override
    public void onPause() {
        super.onPause();
        CachePot.getInstance().clear("DashBoard");
        CachePot.getInstance().push("DashBoard", publicationfinal);
        Log.e("onPause","vykdom sita");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume","vykdom sita");


    }

    public void SetMarkers()
    {
        if(publicationfinal != null)
        {
            LatLng location;


            for(int i = 0; i < publicationfinal.length; i++) {
                double[] eventLoc = publicationfinal[i].getLocation();

                location = new LatLng(eventLoc[1], eventLoc[0]);
                mMap.addMarker(new MarkerOptions().position(location).title("\"" + publicationfinal[i].getTitle() + "\""));
            }
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


        SetMarkers();
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
                        if(circle != null)
                            circle.remove();

                        mLastKnownLocation = (Location) task.getResult();

                        LatLng pos = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                        float meters_per_pixel = (float)( 156543.03392 * Math.cos(pos.latitude * Math.PI / 180) / Math.pow(2, 13));

                        // Set the map's camera position to the current location of the device.

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), meters_per_pixel));


                        circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()))
                                .radius(inputDistance*1000)
                                .strokeColor(0x99FF0000)
                                .fillColor(0x5500BCD4));

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

    public static int GetRequestOffset(){ return inputOffset; }


    public static String GetFromDate()
    {
        return fromDate;
    }

    public static String GetToDate()
    {
        return toDate;
    }

    public static String GetCategory()
    {
        return SelectedCategory;
    }
}