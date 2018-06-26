package com.rpd.AddJobFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.rpd.irepair.AddNewJobActivity;
import com.rpd.irepair.R;

/**
 * Created by Petar on 11/9/2017.
 */

public class AddJobSeverityFragment extends Fragment {

    Button nextButton;
    Button previousButton;
    Button cancelJob;

    Spinner severitySpinner;
    String jobSeverity;

    TextView progressView;

    //Fragment id
    int fragmentID = 1;

    AddNewJobActivity addNewJobActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_job_severity_fragment_layout, container, false);

        //Get add job activity
        addNewJobActivity = (AddNewJobActivity)getActivity();

        nextButton = (Button)view.findViewById(R.id.buttonAddJob);
        previousButton = (Button)view.findViewById(R.id.buttonPrevious);
        cancelJob = (Button)view.findViewById(R.id.buttonCancel);
        severitySpinner = (Spinner)view.findViewById(R.id.addJobSeveritySpinner);

        progressView = (TextView)view.findViewById(R.id.progressView);
        progressView.setText(Integer.valueOf(fragmentID+1).toString() + "/7");


        //Set button NEXT onClick
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewJobActivity.setJobSeverity(severitySpinner.getSelectedItem().toString());
                addNewJobActivity.replaceNextFragment(fragmentID);
            }
        });

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

        return view;
    }

}