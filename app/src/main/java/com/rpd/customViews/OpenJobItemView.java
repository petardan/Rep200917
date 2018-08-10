package com.rpd.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rpd.customClasses.Job;
import com.rpd.irepair.OpenedJobsPerUserActivity;
import com.rpd.irepair.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Petar on 12/26/2017.
 */

public class OpenJobItemView extends RelativeLayout{

    TextView jobTitle;
    ImageView repairmanImageView;
    ImageView closeImageView;
    ImageView infoImageView;
    ImageView chatImageView;

    OpenedJobsPerUserActivity currentActivity;

    SharedPreferences chatPrefs;

    SharedPreferences chatPrefs;

    Job job;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public OpenJobItemView(final Context context, final Job job){
        super(context);

        setJob(job);

        chatPrefs = getContext().getSharedPreferences("chat_prefs", Activity.MODE_PRIVATE);

        init();

        //Get current activity from context
        currentActivity = (OpenedJobsPerUserActivity)context;

        //Initialize job openJobViewItem elements
        jobTitle.setText(job.getJobTitle());
        jobTitle.setTextColor(ContextCompat.getColor(currentActivity.getApplicationContext(), R.color.colorPrimaryDark));

        chatImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Chat", job.getJobTitle());
                setAllMessageRead();
                currentActivity.startChatActivity(job);
            }
        });

        infoImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.showJobInfo(job);
            }
        });

        closeImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.cancelJob(job);
            }
        });

    }


    private void init() {
        inflate(getContext(), R.layout.item_open_job, this);
        this.jobTitle = findViewById(R.id.jobTitle);
        this.repairmanImageView = findViewById(R.id.repairmanImageView);
        this.closeImageView = findViewById(R.id.closeImageView);
        this.infoImageView = findViewById(R.id.infoImageView);
        this.chatImageView = findViewById(R.id.chatImageView);


    }

    public void setNewMessageReceivedAlert() {
        Log.d("OpenJobItem","New Message Received");
        jobTitle.setTextColor(Color.RED);
    }

    public void setAllMessageRead() {
        jobTitle.setTextColor(ContextCompat.getColor(currentActivity.getApplicationContext(), R.color.colorPrimaryDark));
        removeJobIDFromUnreadMessages(job.getJobId());
    }

    private void removeJobIDFromUnreadMessages(String jobID) {
        Set<String> set = chatPrefs.getStringSet("UNREAD_MESSAGE_JOBID", null);
        if (set != null){
            ArrayList<String> arrayFromSet = new ArrayList<>(set);
            for (int i=0; i<arrayFromSet.size(); i++){
                if (arrayFromSet.get(i).equalsIgnoreCase(jobID)){
                    arrayFromSet.remove(i);
                }
            }
            //After jobID is removed, add new array into shared prefs
            SharedPreferences.Editor editor = chatPrefs.edit();
            Set<String> newSet = new HashSet<>();
            set.addAll(arrayFromSet);
            editor.putStringSet("UNREAD_MESSAGE_JOBID", newSet);
            editor.apply();
        }
    }
}
