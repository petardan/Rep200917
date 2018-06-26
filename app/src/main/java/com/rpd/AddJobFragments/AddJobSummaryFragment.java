package com.rpd.AddJobFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rpd.irepair.AddNewJobActivity;
import com.rpd.irepair.R;

/**
 * Created by Petar on 11/9/2017.
 */

public class AddJobSummaryFragment extends Fragment {

    Button addJob;
    Button previousButton;
    Button cancelJob;

    TextView progressView;

    TextView addJobJobTitleSummary;
    TextView addJobDescriptionSummary;
    TextView addJobSeveritySummary;
    TextView addJobStartDateSummary;
    TextView addJobEndDateSummary;
    TextView addJobAddressSummary;
    TextView addJobEmailSummary;
    TextView addJobPhoneSummary;

    String jobTitle;


    //Fragment id
    int fragmentID = 6;

    AddNewJobActivity addNewJobActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_job_summary_fragment_layout, container, false);

        //Get add job activity
        addNewJobActivity = (AddNewJobActivity)getActivity();

        previousButton = (Button)view.findViewById(R.id.buttonPrevious);
        cancelJob = (Button)view.findViewById(R.id.buttonCancel);
        addJob = (Button)view.findViewById(R.id.buttonAddJob);

        addJobJobTitleSummary = (TextView)view.findViewById(R.id.addJobJobTitleSummary);
        addJobDescriptionSummary = (TextView)view.findViewById(R.id.addJobDescriptionSummary);
        addJobSeveritySummary = (TextView)view.findViewById(R.id.addJobSeveritySummary);
        addJobStartDateSummary = (TextView)view.findViewById(R.id.addJobStartDateSummary);
        addJobEndDateSummary = (TextView)view.findViewById(R.id.addJobEndDateSummary);
        addJobAddressSummary = (TextView)view.findViewById(R.id.addJobAddressSummary);
        addJobEmailSummary = (TextView)view.findViewById(R.id.addJobEmailSummary);
        addJobPhoneSummary = (TextView)view.findViewById(R.id.addJobPhoneSummary);


        progressView = (TextView)view.findViewById(R.id.progressView);
        progressView.setText(Integer.valueOf(fragmentID+1).toString() + "/7");

        //Set summary data
        addJobJobTitleSummary.setText(addNewJobActivity.getJobTitle());
        addJobDescriptionSummary.setText(addNewJobActivity.getJobDesciption());
        addJobSeveritySummary.setText(addNewJobActivity.getJobSeverity());
        addJobStartDateSummary.setText(addNewJobActivity.getJobStartDate());
        addJobEndDateSummary.setText(addNewJobActivity.getJobEndDate());
        addJobAddressSummary.setText(addNewJobActivity.getUserAddress());
        addJobEmailSummary.setText(addNewJobActivity.getUserEmail());
        addJobPhoneSummary.setText(addNewJobActivity.getUserPhone());

        //Set button PREVIOUS onClick
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewJobActivity.replacePreviousFragment(fragmentID);
            }
        });

        //Set button CANCEL onClick
        cancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewJobActivity.onBackPressed();
            }
        });

        //Add New job
        //Set button CANCEL onClick
        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewJobActivity.addNewJob();
                //addNewJobActivity.onBackPressed();
            }
        });


        return view;
    }

    public void setAddJobJobTitleSummary(String jobTitle){
        //addJobJobTitleSummary.setText(jobTitle);
        Log.d("Job title", jobTitle);
    }

    public void setAddJobDescriptionSummary(String jobDescription){
        addJobDescriptionSummary.setText(jobDescription);
    }

    public void setAddJobSeveritySummary(String jobSeverity){
        addJobSeveritySummary.setText(jobSeverity);
    }

    public void setAddJobStartDateSummary(String jobStart){
        addJobStartDateSummary.setText(jobStart);
    }

    public void setAddJobEndDateSummary(String jobEnd){
        addJobEndDateSummary.setText(jobEnd);
    }

    public void setAddJobEmailSummary(String jobEmail){
        addJobEmailSummary.setText(jobEmail);
    }

    public void setAddJobPhoneSummary(String jobPhone){
        addJobPhoneSummary.setText(jobPhone);
    }




}