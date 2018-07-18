package com.rpd.services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rpd.customClasses.FriendlyMessage;
import com.rpd.customClasses.Job;
import com.rpd.irepair.ChatActivity;
import com.rpd.irepair.R;

import java.util.ArrayList;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class BackgroundService extends Service
{
    private static final String TAG = "iRepairService";
    static final String NOTIFICATION_TAG = "NOTIF_TAG_SERVICE";

    //Firebase chat database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference jobsperuserDatabaseReference;
    private ChildEventListener jobsperuserChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser currentUser;

    public static final String ANONYMOUS = "anonymous";
    private String mUsername;

    SharedPreferences chatPrefs;

    String jobID = "1513981715655";

    //All user's open jobs
    ArrayList<Job> jobs;
    //Database References for open jobs
    ArrayList<DatabaseReference> jobsDBRefs;
    ArrayList<ChildEventListener> jobsDBListeners;


    //Notification variables
    String channelID = "iRNC";
    String channelName = "iRepair Notification Channel";
    String channelDesc = "This is iRepair Notification Channel";
    int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service onCreate");

        chatPrefs = getSharedPreferences("chat_prefs", Activity.MODE_PRIVATE);

        jobs = new ArrayList<>();
        jobsDBRefs = new ArrayList<>();
        jobsDBListeners = new ArrayList<>();

        mUsername = ANONYMOUS;
        createNotificationChannel();

        //Initialize Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null){
                    //user is signed in
                    String displayName = currentUser.getDisplayName();
                    if (displayName != null && displayName.equalsIgnoreCase("")) {
                        displayName = currentUser.getEmail();
                    }
                    onSignInInitialize(displayName);
                }
                else {
                    //user is signed out
                    onSignOutCleanup();
                }
            }
        };

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignInInitialize(String displayName) {
        mUsername = displayName;
        getUsersOpenJobs();
    }

    private void getUsersOpenJobs() {
        jobsperuserDatabaseReference = mFirebaseDatabase.getReference().child("jobsperuser").child(currentUser.getUid()).child("open");
        //Define Child Event listener for the database reference
        jobsperuserChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, Objects.requireNonNull(dataSnapshot.getValue(Job.class)).toString());
                Job openJob = dataSnapshot.getValue(Job.class);
                if (openJob != null) {
                    createChatDatabaseReferenceForOpenJob(openJob);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        //Get jobs
        jobsperuserDatabaseReference.addChildEventListener(jobsperuserChildEventListener);

    }

    private void createChatDatabaseReferenceForOpenJob(final Job openJob) {
        DatabaseReference chatDatabaseReferenceForOpenJob = mFirebaseDatabase.getReference().child("chat").child(openJob.getJobId());
        jobsDBRefs.add(chatDatabaseReferenceForOpenJob);
        ChildEventListener chatDatabaseListenerForOpenJob = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                if (friendlyMessage != null) {
                    Log.d(TAG, friendlyMessage.getText());
                }


                //These checks can be useful in the background service
                    if (friendlyMessage != null) {
                            if(getIsChatAppForeground()){
                                //The application is in foreground
                                Log.d(NOTIFICATION_TAG, "App is in foreground ");
                                if (!isTheCurrentJob(friendlyMessage.getJobID(), getCurrentForegroundJobID())){
                                    //The user is not seeing the job that he received the message for
                                    createNotification(friendlyMessage.getName(), friendlyMessage.getText() + "Message for different chat", openJob);
                                }
                            }
                            else{
                                //The app is in background
                                Log.d(NOTIFICATION_TAG, "App is in background ");
                                if (friendlyMessage.getStatus()==1){
                                    //Message already read
                                    Log.d(NOTIFICATION_TAG, "Already read");
                                } else {
                                    Log.d(NOTIFICATION_TAG, "NEW");
                                    updateLastMessageReadTimestamp(friendlyMessage.getTimestamp());
                                    createNotification(friendlyMessage.getName(), friendlyMessage.getText() + "NEW", openJob);
                                }
                            }
                    }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        jobsDBListeners.add(chatDatabaseListenerForOpenJob);
        chatDatabaseReferenceForOpenJob.addChildEventListener(chatDatabaseListenerForOpenJob);
    }

    private void onSignOutCleanup() {
        mUsername = ANONYMOUS;
        onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service onBind");
        return null;
    }
    public void onDestroy() {
        Log.d(TAG, "Service onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service onStart");
        return START_STICKY;
    }

    private boolean isTheCurrentJob(String messageJobID, String jobId) {
        return messageJobID.equalsIgnoreCase(jobId);
    }

    private void updateLastMessageReadTimestamp(Long timestamp) {
        SharedPreferences.Editor editor = chatPrefs.edit();
        editor.putLong("LAST_MESSAGE_READ_TIMESTAMP", timestamp);
        editor.apply();
    }

    //Add notification for new recived message
    public void createNotification(String name, String text, Job job) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("JOB", job);
        Intent resultIntent = new Intent(this, ChatActivity.class);
        resultIntent.putExtras(bundle);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );



        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle(name + " says:")
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // Sets an ID for the notification
        long jobIDlong = Long.valueOf(jobID) / 10000;
        int notificationID = (int)jobIDlong;
        notificationManager.notify(notificationID, mBuilder.build());

    }

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

    //Check if ChatActivity is in foreground
    public boolean getIsChatAppForeground(){
        return chatPrefs.getBoolean("ISAPPFOREGROUND", false);
    }

    //Check if ChatActivity is in foreground
    public String getCurrentForegroundJobID(){
        return chatPrefs.getString("APPFOREGROUNDJOBID", "");
    }


}