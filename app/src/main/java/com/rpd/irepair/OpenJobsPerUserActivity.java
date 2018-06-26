package com.rpd.irepair;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rpd.customClasses.FriendlyMessage;
import com.rpd.customClasses.Job;
import com.rpd.customViews.OpenJobItemView;

import java.util.ArrayList;

public class OpenJobsPerUserActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth auth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference jobsperuserDatabaseReference;
    FirebaseUser currentFirebaseUser;
    private ChildEventListener jobsperuserChildEventListener;

    String currentJobID="";

    LinearLayout openJobsLinearLayout;

    ArrayList<OpenJobItemView> openJobItemViews;

    SharedPreferences chatPrefs;

    public boolean isAppForeground = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openjobsperuser);
        isAppForeground = true;

        //Initialize openJobItemViews array list
        openJobItemViews = new ArrayList<>();

        //Get Firebase instances
        auth = FirebaseAuth.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        jobsperuserDatabaseReference = mFirebaseDatabase.getReference().child("jobsperuser").child(currentFirebaseUser.getUid()).child("open");

        openJobsLinearLayout = (LinearLayout)findViewById(R.id.openJobsLinearLayout);

        chatPrefs = getSharedPreferences("open_jobs_chat_prefs", Activity.MODE_PRIVATE);

        //Define Child Event listener for the database reference
        jobsperuserChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addNewOpenJobItem(dataSnapshot.getValue(Job.class));
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

        //Get repairmans
        jobsperuserDatabaseReference.addChildEventListener(jobsperuserChildEventListener);
    }

    private void addNewOpenJobItem(Job job) {
        //First add job's database reference and listeners
        OpenJobItemView openJobItemView = new OpenJobItemView(this, job);
        openJobItemViews.add(openJobItemView);
        openJobsLinearLayout.addView(openJobItemView);
        addJobsDatabaseReferenceAndListener(job, openJobItemView);

    }

    private void addJobsDatabaseReferenceAndListener(final Job job, final OpenJobItemView openJobItemView) {

        //Chat's database reference and listeners
        DatabaseReference chatDatabaseReference;
        ChildEventListener chatChildEventListener;

        chatDatabaseReference = mFirebaseDatabase.getReference().child("chat").child(job.getJobId());

        //Initialize database listener
            chatChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    Log.d("Message Open Job", friendlyMessage.getName());

                    if(messageIsFromCurrentUser(friendlyMessage.getSenderID(), currentFirebaseUser.getUid())){
                        Log.d("NotificationChat", "From current user");
                        updateLastMessageReadTimestamp(friendlyMessage.getTimestamp());
                    }
                    else {

                        if(isAppForeground){
                            //The application is in foreground, do not show the notification
                            Log.d("Notification", "App is in foreground " + String.valueOf(isAppForeground));
                            if (isMessageRead(friendlyMessage.getTimestamp(), getLastMessageReadTimestamp())){
                                Log.d("NotificationChat", "Already read");
                                openJobItemView.setAllMessageRead();
                            }
                            else{
                                //TO DO: Show notification only if app is in background
                                Log.d("NotificationChat", "NEW");
                                updateLastMessageReadTimestamp(friendlyMessage.getTimestamp());
                                openJobItemView.setNewMessageReceivedAlert();
                            }
                        }
                        else{
                            //The app is in background, show the notification
                            Log.d("Notification", "App is in background " + String.valueOf(isAppForeground));
                            if (isTheCurrentJob(friendlyMessage.getJobID(), currentJobID)){
                                if (isMessageRead(friendlyMessage.getTimestamp(), getLastMessageReadTimestamp())){
                                    //Do nothing
                                    Log.d("NotificationChat", "Already read");
                                }
                                else{
                                    //TO DO: Show notification only if app is in background
                                    Log.d("NotificationChat", "NEW");
                                    updateLastMessageReadTimestamp(friendlyMessage.getTimestamp());
                                    createNotification(friendlyMessage.getName(), friendlyMessage.getText(), job);
                                }
                            } else{
                                //TO DO: Show notification if only if app is in foreground
                                Log.d("NotificationChat", "For another job");
                                createNotification(friendlyMessage.getName(), friendlyMessage.getText(), job);
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
            chatDatabaseReference.addChildEventListener(chatChildEventListener);
    }



    private boolean messageIsFromCurrentUser(String senderID, String uid) {
        if(senderID.equalsIgnoreCase(uid)){
            return true;
        } else{
            return false;
        }
    }

    private boolean isMessageRead(Long messageTimestamp, Long lastMessageReadTimestamp) {
        Log.d("Message read", String.valueOf(messageTimestamp) + " - " + String.valueOf(lastMessageReadTimestamp));
        if(messageTimestamp <= lastMessageReadTimestamp){
            return true;
        } else {
            return false;
        }
    }

    private boolean isTheCurrentJob(String messageJobID, String jobId) {
        if (messageJobID.equalsIgnoreCase(jobId)){
            return true;
        } else {
            return false;
        }
    }

    private void updateLastMessageReadTimestamp(Long timestamp) {
        SharedPreferences.Editor editor = chatPrefs.edit();
        editor.putLong("OPEN_JOBS_LAST_MESSAGE_READ_TIMESTAMP", timestamp);
        editor.commit();
    }

    private Long getLastMessageReadTimestamp(){
        Long lastMessageReadTimestamp = chatPrefs.getLong("OPEN_JOBS_LAST_MESSAGE_READ_TIMESTAMP", 0);
        return lastMessageReadTimestamp;
    }

    private void detachDatabaseReadListener(){
        if(jobsperuserChildEventListener != null){
            jobsperuserDatabaseReference.removeEventListener(jobsperuserChildEventListener);
            jobsperuserChildEventListener = null;
        }
    }

    public void startChatActivity(Job job){
        currentJobID = job.getJobId();
        Bundle bundle = new Bundle();
        bundle.putSerializable("JOB", job);
        Intent i = new Intent(this , ChatActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void showJobInfo(Job job) {
        final Dialog fbDialogue = new Dialog(OpenJobsPerUserActivity.this, android.R.style.Theme_Black_NoTitleBar);
        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.fragment_chat);
        fbDialogue.setCancelable(true);
        fbDialogue.show();
    }

    //Add notification for new recived message
    public void createNotification(String name, String text, Job job) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle(name + " says:")
                .setContentText(text);


        Bundle bundle = new Bundle();
        bundle.putSerializable("JOB", job);
        Intent resultIntent = new Intent(this, OpenJobsPerUserActivity.class);
        resultIntent.putExtras(bundle);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        long jobIDlong = Long.valueOf(job.getJobId()) / 10000;
        int mNotificationId = (int)jobIDlong;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        isAppForeground = false;
        Log.d("Notification", "App is in background");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAppForeground = true;
        currentJobID = "";
        Log.d("Notification", "App is in foreground");
    }
}
