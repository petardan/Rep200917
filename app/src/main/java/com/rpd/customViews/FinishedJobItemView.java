package com.rpd.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rpd.customClasses.Job;
import com.rpd.irepair.CanceledJobsPerUserActivity;
import com.rpd.irepair.FinishedJobsPerUserActivity;
import com.rpd.irepair.R;


/**
 * Created by Petar on 12/26/2017.
 */

public class FinishedJobItemView extends RelativeLayout{

    TextView jobTitle;
    TextView jobStatus;
    ImageView repairmanImageView;
    ImageView infoImageView;
    ImageView chatImageView;
    ImageView reviewImageView;

    FinishedJobsPerUserActivity currentActivity;

    SharedPreferences chatPrefs;

    Job job;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public FinishedJobItemView(final Context context, final Job job){
        super(context);

        setJob(job);

        chatPrefs = getContext().getSharedPreferences("chat_prefs", Activity.MODE_PRIVATE);

        init();

        //Get current activity from context
        currentActivity = (FinishedJobsPerUserActivity) context;

        //Initialize job finishedJobViewItem elements
        jobTitle.setText(job.getJobTitle());
        jobTitle.setTextColor(ContextCompat.getColor(currentActivity.getApplicationContext(), R.color.colorPrimaryDark));
        jobStatus.setText("Finished");

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

        reviewImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.showJobRivew(job);
            }
        });
    }


    private void init() {
        inflate(getContext(), R.layout.item_finished_job, this);
        this.jobTitle = findViewById(R.id.jobTitle);
        this.repairmanImageView = findViewById(R.id.repairmanImageView);
        this.infoImageView = findViewById(R.id.infoImageView);
        this.chatImageView = findViewById(R.id.chatImageView);
        this.reviewImageView = findViewById(R.id.reviewImageView);
        this.jobStatus = findViewById(R.id.jobStatus);
    }

    public void setAllMessageRead() {
        jobTitle.setTextColor(ContextCompat.getColor(currentActivity.getApplicationContext(), R.color.colorPrimaryDark));
    }

}
