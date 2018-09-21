package com.rpd.irepair;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.rpd.customClasses.Job;
import com.rpd.customViews.CanceledJobItemView;

import java.util.ArrayList;
import java.util.Objects;

public class CanceledJobsPerUserActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth auth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference jobsperuserDatabaseReference;
    FirebaseUser currentUser;
    private ChildEventListener jobsperuserChildEventListener;

    String currentJobID="";

    LinearLayout canceledJobsLinearLayout;

    ArrayList<CanceledJobItemView> canceledJobItemViews;

    //Unread messages jobID array
    ArrayList<String> unreadMessagesJobIDArray;

    SharedPreferences chatPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canceledjobsperuser);

        chatPrefs = getSharedPreferences("chat_prefs", Activity.MODE_PRIVATE);

        //Initialize cancelJobItemViews array list
        canceledJobItemViews = new ArrayList<>();

        //Get Firebase instances
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        jobsperuserDatabaseReference = mFirebaseDatabase.getReference().child("jobsperuser").child(currentUser.getUid()).child("open");

        canceledJobsLinearLayout = findViewById(R.id.canceledJobsLinearLayout);

        //Define Child Event listener for the database reference
        jobsperuserChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue(Job.class).getJobStatus().equalsIgnoreCase("4")){
                    addNewCanceledJobItem(dataSnapshot.getValue(Job.class));
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

    private void addNewCanceledJobItem(Job job) {
        //First add job's database reference and listeners
        CanceledJobItemView canceledJobItemView = new CanceledJobItemView(this, job);
        canceledJobItemViews.add(canceledJobItemView);
        canceledJobsLinearLayout.addView(canceledJobItemView);
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
        final Dialog fbDialogue = new Dialog(CanceledJobsPerUserActivity.this, android.R.style.Theme_Black_NoTitleBar);
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
    }


}
