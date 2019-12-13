package com.example.aroundme.ui.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.aroundme.FirstActivity;
import com.example.aroundme.ListItem;
import com.example.aroundme.ModelPost;
import com.example.aroundme.R;
import com.example.aroundme.ui.dashboard.DashboardFragment;
import com.github.kimkevin.cachepot.CachePot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.aroundme.FirstActivity.navController;

public class NotificationsFragment extends Fragment
{

    private NotificationsViewModel notificationsViewModel;

    // This is the Notification Channel ID. More about this in the next section
    public static final String NOTIFICATION_CHANNEL_ID = "101";
    //User visible Channel Name
    public static final String CHANNEL_NAME = "Around ME Notifications Channel";
    // Unique identifier for notification
    public static final int NOTIFICATION_ID = 101;


    TextView titleTextView;
    TextView descriptionTextView;
    TextView categoryTextView;
    TextView locationNameTextView;
    TextView startDateTextView;
    TextView endDateTextView;

    ImageButton shareButton;
    ImageButton navigateButton;
    ModelPost[] publication;
    int id = -1;

    private Fragment mMyFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        titleTextView = root.findViewById(R.id.titleTextView);
        descriptionTextView = root.findViewById(R.id.descriptionTextView);
        categoryTextView = root.findViewById(R.id.categoryTextView);
        locationNameTextView = root.findViewById(R.id.locationNameTextView);
        startDateTextView = root.findViewById(R.id.startDateTextView);
        endDateTextView = root.findViewById(R.id.endDateTextView);

        shareButton = root.findViewById(R.id.shareImageButton);
        shareButton.setOnClickListener(shareButtonListener);


        navigateButton = root.findViewById(R.id.navigaveImageButton);
        navigateButton.setOnClickListener(navigaveImageButtonListener);


        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        AnimationDrawable animDrawable = (AnimationDrawable) root.findViewById(R.id.linearLayoutNotifications).getBackground();

        animDrawable.setEnterFadeDuration(10);
        animDrawable.setExitFadeDuration(5000);
        animDrawable.start();

        /*

         items.add(new ListItem(
         publicationfinal[i].getTitle(),
         image,
          publicationfinal[i].getDescription(),
           publicationfinal[i].getCategory(),
           publicationfinal[i].getName(),
           publicationfinal[i].getStart(),
            publicationfinal[i].getEnd(),
             publicationfinal[i].getLocation()));
         */

        publication = DashboardFragment.GetPublication();

        id = -1;
        Log.e("VEIKIA", ""+id);

        try{
            //publication = CachePot.getInstance().pop("2");
            id = CachePot.getInstance().pop("0");

        } catch (Exception ex)
        {
            Log.e("Exception", "null");
        }

        Log.e("VEIKIA", "id: "+id+"  publication: "+publication);

        if(id != -1 && publication != null)
        {
            Log.e("gotVALUE", ""+id);
            Log.e("gotVALUE2", ""+publication[id].getTitle());
            titleTextView.setText(publication[id].getTitle());
            descriptionTextView.setText(publication[id].getDescription());
            categoryTextView.setText(publication[id].getCategory());
            locationNameTextView.setText(publication[id].getName());
            startDateTextView.setText(publication[id].getStart());
            endDateTextView.setText(publication[id].getEnd());

        }
        return root;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    View.OnClickListener shareButtonListener = new View.OnClickListener()
    {
        @Override
        public  void  onClick(View view)
        {
            Log.e("info","pries if");
            if(id != -1 && publication != null) {
                Log.e("info","po if");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                //# change the type of data you need to share,
                //# for image use "image/*"
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "https://events.predicthq.com/events/" + publication[id].getId());
                startActivity(Intent.createChooser(intent, "Share"));
            }
        }
    };

    View.OnClickListener navigaveImageButtonListener = new View.OnClickListener()
    {
        @Override
        public  void  onClick(View view)
        {
            Log.e("info","pries if");
            if(id != -1 && publication != null) {
                Log.e("info","Vazuojam");

                CachePot.getInstance().clear("5");
                CachePot.getInstance().push("5", publication[id]);
                navController.navigate(R.id.navigation_dashboard);

            }
        }
    };


    public static void SendNotification(Context context)//padaryt kad butu labiau random zinutes
    {

        NotificationChannel notificationChannel = CreateNotificationChannel(context);

        Notification notification = CreateNotification(NOTIFICATION_CHANNEL_ID, "Title", "description", R.drawable.ic_map_search_events, R.drawable.ic_map_search_events, context );

        //This is what will will issue the notification i.e.notification will be visible
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);

    }


    public static NotificationChannel CreateNotificationChannel(Context context)
    {
        // Importance applicable to all the notifications in this Channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel notificationChannel = null;

        //Notification channel should only be created for devices running Android 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);
            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);
            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.setVibrationPattern(new long[] {
                    200,
                    100,
                    50,
                    100,
                    50
            });
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        return  notificationChannel;
    }

    public static Notification CreateNotification(String NOTIFICATION_CHANNEL_ID, String heading, String description, int smallIcon, int bigIcon, Context context)
    {
        //Notification Channel ID passed as a parameter here will be ignored for all the Android versions below 8.0
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setContentTitle(heading);
        builder.setContentText(description);
        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), bigIcon));

        //This intent will be fired when the notification is tapped
        Intent intent = new Intent(context, FirstActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1001, intent, 0);
        //Following will set the tap action
        builder.setContentIntent(pendingIntent);



        Notification notification = builder.build();
        return notification;
    }
}