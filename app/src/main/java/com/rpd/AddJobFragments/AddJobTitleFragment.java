package com.rpd.AddJobFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rpd.irepair.AddNewJobActivity;
import com.rpd.irepair.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Petar on 11/9/2017.
 */

public class AddJobTitleFragment extends Fragment{

    Button nextButton;
    Button cancelJob;
    ImageView addJobRepairmanIcon;
    EditText addJobJobTitleEdit;
    EditText addJobDescriptionEdit;

    TextView progressView;

    //Fragment id
    int fragmentID = 0;

    AddNewJobActivity addNewJobActivity;

    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_job_title_fragment_layout, container, false);

        //Get add job activity
        addNewJobActivity = (AddNewJobActivity)getActivity();

        nextButton = (Button)view.findViewById(R.id.buttonAddJob);
        cancelJob = (Button)view.findViewById(R.id.buttonCancel);
        addJobRepairmanIcon = (ImageView)view.findViewById(R.id.addJobRepairmanIcon);
        addJobJobTitleEdit = (EditText)view.findViewById(R.id.addJobJobTitleEdit);
        addJobDescriptionEdit = (EditText)view.findViewById(R.id.addJobDescriptionEdit);

        progressView = (TextView)view.findViewById(R.id.progressView);
        progressView.setText(Integer.valueOf(fragmentID+1).toString() + "/7");

        //Set button NEXT onClick
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfStringIsEmpty(addJobJobTitleEdit.getText().toString())){
                    Toast.makeText(getActivity(), "Please insert job title", Toast.LENGTH_LONG).show();
                }
                if(checkIfStringIsEmpty(addJobDescriptionEdit.getText().toString())){
                    Toast.makeText(getActivity(), "Please insert job description", Toast.LENGTH_LONG).show();
                } else{
                    addNewJobActivity.setJobTitleAndDescription(addJobJobTitleEdit.getText().toString(), addJobDescriptionEdit.getText().toString());
                    addNewJobActivity.replaceNextFragment(fragmentID);
                }
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

    //Set selected repairman icon
    public void setRepairmanImage(String imageURL){
        Log.d("REPAIRMAN", addNewJobActivity.getSelectedRepairman().getFirstName());
        Picasso.with(addNewJobActivity).load(R.drawable.blankuser1).into(addJobRepairmanIcon);
    };

    //Check if Title or Description are empty
    public boolean checkIfStringIsEmpty(String string){
        if (string == null || string.isEmpty()) {
           return true;
        }
        else{
            return false;
        }
    };


}