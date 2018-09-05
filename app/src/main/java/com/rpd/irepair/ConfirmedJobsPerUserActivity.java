package com.rpd.irepair;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.rpd.customViews.ConfirmedJobItemView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class ConfirmedJobsPerUserActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth auth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference jobsperuserDatabaseReference;
    FirebaseUser currentUser;
    private ChildEventListener jobsperuserChildEventListener;

    String currentJobID="";

    LinearLayout confirmJobsLinearLayout;

    ArrayList<ConfirmedJobItemView> confirmJobItemViews;

    //Unread messages jobID array
    ArrayList<String> unreadMessagesJobIDArray;

    SharedPreferences chatPrefs;

    //Message Broadcast receiver
    ActivityReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmedjobsperuser);

        setActionBarTitleAndSubtitle("Confirmed Jobs", "");

        //clearNotifications();

        chatPrefs = getSharedPreferences("chat_prefs", Activity.MODE_PRIVATE);

        messageReceiver = new ActivityReceiver();
        IntentFilter filter = new IntentFilter("NEW_MESSAGE_RECEIVED");
        this.registerReceiver(messageReceiver, filter);

        //Initialize openJobItemViews array list
        confirmJobItemViews = new ArrayList<>();

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

        confirmJobsLinearLayout = findViewById(R.id.confirmedJobsLinearLayout);



        //Define Child Event listener for the database reference
        jobsperuserChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue(Job.class).getJobStatus().equalsIgnoreCase("2")){
                    addNewConfirmJobItem(dataSnapshot.getValue(Job.class));
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

    private void setActionBarTitleAndSubtitle(String title, String subtitle) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setSubtitle(subtitle);

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

    private void addNewConfirmJobItem(Job job) {
        //First add job's database reference and listeners
        ConfirmedJobItemView confirmJobItemView = new ConfirmedJobItemView(this, job);
        if(jobHasUnreadMessages(job)){
            confirmJobItemView.setNewMessageReceivedAlert();
        }
        confirmJobItemViews.add(confirmJobItemView);
        confirmJobsLinearLayout.addView(confirmJobItemView);
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
        final Dialog fbDialogue = new Dialog(ConfirmedJobsPerUserActivity.this, android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(fbDialogue.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.fragment_info);
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

        for (int i=0; i<confirmJobItemViews.size(); i++){
            ConfirmedJobItemView cjiv = confirmJobItemViews.get(i);
            Job job = cjiv.getJob();
            if (job.getJobId().equalsIgnoreCase(message.getJobID())){
                cjiv.setNewMessageReceivedAlert();
            }
        }
    }

    public void clearNotifications(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public void cancelJob(Job job) {
        String jobId = job.getJobId();
        try {
            jobsperuserDatabaseReference.child(jobId).child("jobStatus").setValue("4");
            jobsperuserDatabaseReference.child(jobId).child("jobCancelledBy").setValue("user");
            removeViewBasedOnJobID(jobId);
            Toast.makeText(this, "Job canceled! Check cancelled job for details", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ConfirmedJobsPerUserActivity.this, CanceledJobsPerUserActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e){
            Toast.makeText(this, "Job is not canceled!", Toast.LENGTH_LONG).show();
        }
    }

    public void removeViewBasedOnJobID(String jobID){
        for (int i = 0; i< confirmJobItemViews.size(); i++){
            ConfirmedJobItemView cjiv = confirmJobItemViews.get(i);
            if (cjiv.getJob().getJobId().equalsIgnoreCase(jobID)){
                cjiv.setVisibility(View.GONE);
            }
        }
    }

    public void jobFinished(Job job) {
        String jobId = job.getJobId();
        try {
            jobsperuserDatabaseReference.child(jobId).child("jobStatus").setValue("3");
            removeViewBasedOnJobID(jobId);
            Toast.makeText(this, "Job done!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ConfirmedJobsPerUserActivity.this, FinishedJobsPerUserActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e){
            Toast.makeText(this, "Job is not finished!", Toast.LENGTH_LONG).show();
        }
    }
}
