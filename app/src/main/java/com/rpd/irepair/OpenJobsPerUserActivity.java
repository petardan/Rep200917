package com.rpd.irepair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.rpd.customClasses.Job;
import com.rpd.customViews.OpenJobItemView;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openjobsperuser);

        //Initialize openJobItemViews array list
        openJobItemViews = new ArrayList<>();

        //Get Firebase instances
        auth = FirebaseAuth.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        jobsperuserDatabaseReference = mFirebaseDatabase.getReference().child("jobsperuser").child(currentFirebaseUser.getUid()).child("open");

        openJobsLinearLayout = findViewById(R.id.openJobsLinearLayout);

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
    }
}
