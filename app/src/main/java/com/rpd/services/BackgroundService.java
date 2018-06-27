package com.rpd.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.rpd.irepair.Loading;
import com.rpd.irepair.MainActivity;
import com.rpd.irepair.R;

@RequiresApi(api = Build.VERSION_CODES.N)
public class BackgroundService extends Service
{
    private static final String TAG = "iRepair";

    String channelID = "iRNCID";
    String channelName = "iRepair Notification Channel";
    String channelDesc = "iRepair Notification Channel Description";
    int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
    int notificationID = 0;

    //Create notification channel
    public void createNotificationChannel(){
        //Only for Android O+, API 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, channelImportance);
            channel.setDescription(channelDesc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Background Service Binded", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onBind");
        return null;
    }
    public void onDestroy() {
        Toast.makeText(this, "Background Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Context context = this;
        CountDownTimer timer = new CountDownTimer(60000, 10000) {

            int i = 0;

            public void onTick(long millisUntilFinished) {
                Toast.makeText(context, "My Service Started " + Integer.toString(i), Toast.LENGTH_LONG).show();
                Log.d(TAG, "onStart");
                i++;
            }

            public void onFinish() {
                Toast.makeText(context, "FINISH", Toast.LENGTH_LONG).show();
                Log.d(TAG, "FINISH");

                //Set the intent that will be called
                Intent intent = new Intent(context, Loading.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent,0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelID)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle("Service Notification")
                        .setContentText("The countdown timer has finished")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("This is much longer notification text that cannot be dispalyed in one line"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);


                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(notificationID, mBuilder.build());
                notificationID++;
            }

        };
        timer.start();
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
        return START_STICKY;
    }


}