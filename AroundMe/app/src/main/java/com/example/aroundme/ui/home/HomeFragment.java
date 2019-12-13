package com.example.aroundme.ui.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.aroundme.EventInfo;
import com.example.aroundme.ListAdapter;
import com.example.aroundme.ListItem;
import com.example.aroundme.R;
import com.example.aroundme.ui.dashboard.DashboardFragment;
import com.example.aroundme.ui.notifications.NotificationsFragment;
import com.github.kimkevin.cachepot.CachePot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.aroundme.FirstActivity.navController;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private ListView mylist;
    private ListAdapter adapter;
    private Context context = getActivity();

    private Fragment mMyFragment;
    ///public static List<ListItem> EventList;

    List<ListItem> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
/*
        if (savedInstanceState != null) {
            Log.e("UZKRAUTA", "uzkrauta Event List (HOME)");
            //Restore the fragment's instance
            mMyFragment = getChildFragmentManager().getFragment(savedInstanceState, "HomeFragment");
        }*/

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        mylist = root.findViewById(R.id.eventList);

        if(DashboardFragment.EventList != null) {
            items = DashboardFragment.EventList;


        }

        adapter = new ListAdapter(getActivity(), items);
        mylist.setAdapter((adapter));


        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //Log.i("list item Click", "Position: "+position+" id: "+id);
                //Log.i("list item Click INFO", "Title: "+items.get(position).getTitle()+" desc: "+items.get(position).getDescription()+" image?: "+items.get(position).getImageId());
/*
                Intent intent = new Intent(context, NotificationsFragment.class);
                //based on item add info to intent
                intent.putExtra("Title", items.get(position).getTitle());
                intent.putExtra("Description", items.get(position).getDescription());
                intent.putExtra("ImageId", items.get(position).getImageId());
                intent.putExtra("myItemList", (Serializable) items);
                context.startActivity(intent);

                Bundle bundle=new Bundle();
                bundle.putString("message", "From FRAGMENT");
                //set Fragmentclass Arguments
                NotificationsFragment fragobj = new NotificationsFragment();
                fragobj.setArguments(bundle);*/

                CachePot.getInstance().clear("0");
                CachePot.getInstance().push("0", position);
                Log.e("PUSH", "stumiam: "+position);
                navController.navigate(R.id.navigation_notifications);

            }
        });

        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getChildFragmentManager().putFragment(outState, "HomeFragment", this);
    }

}

/*

<style name="BaseAppTheme" parent="Theme.AppCompat.DayNight.DarkActionBar">
        <!-- Customize your theme here. -->
        <item name="buttonStyle">@drawable/buttonstyle</item>
        <item name="colorPrimary">@color/colorPrimaryLight</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="android:textColor">@color/colorText</item>


    </style>
 */