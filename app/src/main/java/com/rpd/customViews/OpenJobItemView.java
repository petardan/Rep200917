package com.rpd.customViews;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rpd.customClasses.Job;
import com.rpd.irepair.OpenJobsPerUserActivity;
import com.rpd.irepair.R;


/**
 * Created by Petar on 12/26/2017.
 */

public class OpenJobItemView extends RelativeLayout{

    TextView jobTitle;
    ImageView repairmanImageView;
    ImageView closeImageView;
    ImageView infoImageView;
    ImageView chatImageView;

    OpenJobsPerUserActivity currentActivity;

    Job job;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public OpenJobItemView(final Context context, final Job job){
        super(context);
        init();

        //Get current activity from context
        currentActivity = (OpenJobsPerUserActivity)context;

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

    }


    private void init() {
        inflate(getContext(), R.layout.item_open_job, this);
        this.jobTitle = (TextView)findViewById(R.id.jobTitle);
        this.repairmanImageView = (ImageView)findViewById(R.id.repairmanImageView);
        this.closeImageView = (ImageView)findViewById(R.id.closeImageView);
        this.infoImageView = (ImageView)findViewById(R.id.infoImageView);
        this.chatImageView = (ImageView)findViewById(R.id.chatImageView);
    }

    public void setNewMessageReceivedAlert() {
        Log.d("OpenJobItem","New Message Received");
        jobTitle.setTextColor(Color.RED);
    }

    public void setAllMessageRead() {
        jobTitle.setTextColor(ContextCompat.getColor(currentActivity.getApplicationContext(), R.color.colorPrimaryDark));

    }
}
