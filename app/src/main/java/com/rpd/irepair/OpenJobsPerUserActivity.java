package com.rpd.irepair;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rpd.broadcastReceivers.ActivityReceiver;
import com.rpd.customClasses.FriendlyMessage;
import com.rpd.customClasses.Job;
import com.rpd.customViews.OpenJobItemView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class OpenJobsPerUserActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth auth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference jobsperuserDatabaseReference;
    FirebaseUser currentUser;
    private ChildEventListener jobsperuserChildEventListener;

    String currentJobID="";

    LinearLayout openJobsLinearLayout;

    ArrayList<OpenJobItemView> openJobItemViews;

    //Unread messages jobID array
    ArrayList<String> unreadMessagesJobIDArray;

    SharedPreferences openJobChatPrefs;
    SharedPreferences chatPrefs;

    //Message Broadcast receiver
    ActivityReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openjobsperuser);

        clearNotifications();

        openJobChatPrefs = getSharedPreferences("open_jobs_chat_prefs", Activity.MODE_PRIVATE);
        chatPrefs = getSharedPreferences("chat_prefs", Activity.MODE_PRIVATE);

        messageReceiver = new ActivityReceiver();
        IntentFilter filter = new IntentFilter("NEW_MESSAGE_RECEIVED");
        this.registerReceiver(messageReceiver, filter);

        //Initialize openJobItemViews array list
        openJobItemViews = new ArrayList<>();

        //Get all unread messages job id
        unreadMessagesJobIDArray = new ArrayList<>();
        if (getUnreadMessagesJobIDFromSharedPrefs() != null){
            unreadMessagesJobIDArray = getUnreadMessagesJobIDFromSharedPrefs();
        }
        for (int i=0; i<unreadMessagesJobIDArray.size(); i++){
            Log.d("UNREADSET", unreadMessagesJobIDArray.get(i));
        }

        //Get Firebase instances
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        jobsperuserDatabaseReference = mFirebaseDatabase.getReference().child("jobsperuser").child(currentUser.getUid()).child("open");

        openJobsLinearLayout = findViewById(R.id.openJobsLinearLayout);



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

        //Get jobs
        jobsperuserDatabaseReference.addChildEventListener(jobsperuserChildEventListener);
    }

    private ArrayList<String> getUnreadMessagesJobIDFromSharedPrefs() {
        Set<String> set = chatPrefs.getStringSet("UNREAD_MESSAGE_JOBID", null);
        ArrayList<String> arrayFromSet = new ArrayList<>();
        if (set != null){
            arrayFromSet.addAll(set);
            for (int i=0; i<arrayFromSet.size(); i++){
                Log.d("UNREADSETget", arrayFromSet.get(i));
            }
        }

        return arrayFromSet;

    }

    private void addNewOpenJobItem(Job job) {
        //First add job's database reference and listeners
        OpenJobItemView openJobItemView = new OpenJobItemView(this, job);
        if(jobHasUnreadMessages(job)){
            openJobItemView.setNewMessageReceivedAlert();
        }
        openJobItemViews.add(openJobItemView);
        openJobsLinearLayout.addView(openJobItemView);
    }

    private boolean jobHasUnreadMessages(Job job) {
        if (unreadMessagesJobIDArray.contains(job.getJobId())){
            return true;
        }
        else {
            return false;
        }
    }

    private void detachDatabaseReadListener(){
        if(jobsperuserChildEventListener != null){
            jobsperuserDatabaseReference.removeEventListener(jobsperuserChildEventListener);
            jobsperuserChildEventListener = null;
        }
    }

    public void startChatActivity(Job job){
        detachDatabaseReadListener();
        currentJobID = job.getJobId();
        Bundle bundle = new Bundle();
        bundle.putSerializable("JOB", job);
        Intent i = new Intent(this , ChatActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void showJobInfo(Job job) {
        final Dialog fbDialogue = new Dialog(OpenJobsPerUserActivity.this, android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(fbDialogue.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.fragment_chat);
        fbDialogue.setCancelable(true);
        fbDialogue.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        Log.d("Notification", "App is in background");
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentJobID = "";
        Log.d("Notification", "App is in foreground");
        clearNotifications();
    }

    public void messageReceived(FriendlyMessage message){
        Toast.makeText(this, "Activity broadcast receiver called !" + message.getText(),
                Toast.LENGTH_LONG).show();

        for (int i=0; i<openJobItemViews.size(); i++){
            OpenJobItemView ojiv = openJobItemViews.get(i);
            Job job = ojiv.getJob();
            if (job.getJobId().equalsIgnoreCase(message.getJobID())){
                //Log.d("AAAAAAA", job.getJobId() + " " + message.getJobID());
                ojiv.setNewMessageReceivedAlert();
            }
        }
    }

    public void clearNotifications(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

}
