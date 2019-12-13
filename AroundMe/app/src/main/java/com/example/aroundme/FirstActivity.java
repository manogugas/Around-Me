package com.example.aroundme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class FirstActivity extends AppCompatActivity
{
    private Button searchButton;
    private Button optionsButton;
    private Context context = this;

    public static NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate((savedInstanceState));
        setContentView(R.layout.firstactivitydesign);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

       // searchButton = (Button)findViewById(R.id.searchButton);
        //optionsButton = (Button)findViewById(R.id.optionsButton);

        //optionsButton.setOnClickListener(startOptionsActivity);
       // searchButton.setOnClickListener(startSearchActivity);
       // searchButton.setOnLongClickListener(startSearchActivityLong);
    }

    @Override
    protected void onStop() {
        super.onStop();

        startService(new Intent(this, NotificationService.class));
    }


    /*
    View.OnClickListener startOptionsActivity = new View.OnClickListener(){
        @Override
        public  void onClick(View v)
        {

        }
    };

    public void runSearchActivity(boolean b)
    {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra("flag", b);
        context.startActivity(intent);
    }

    View.OnClickListener startSearchActivity = new View.OnClickListener(){
        @Override
        public  void onClick(View v)
        {
            runSearchActivity(true);
        }
    };

    View.OnLongClickListener startSearchActivityLong = new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View v)
        {
            runSearchActivity(false);
            return true;
        }
    };*/




}
