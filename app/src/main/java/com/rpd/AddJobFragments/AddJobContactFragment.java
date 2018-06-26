package com.rpd.AddJobFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rpd.irepair.AddNewJobActivity;
import com.rpd.irepair.R;

/**
 * Created by Petar on 11/9/2017.
 */

public class AddJobContactFragment extends Fragment {

    Button nextButton;
    Button previousButton;
    Button cancelJob;

    TextView progressView;

    EditText addJobEmailEdit;
    EditText addJobPhoneEdit;

    //Fragment id
    int fragmentID = 5;

    AddNewJobActivity addNewJobActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_job_contact_fragment_layout, container, false);

        //Get add job activity
        addNewJobActivity = (AddNewJobActivity)getActivity();

        nextButton = (Button)view.findViewById(R.id.buttonAddJob);
        previousButton = (Button)view.findViewById(R.id.buttonPrevious);
        cancelJob = (Button)view.findViewById(R.id.buttonCancel);

        addJobEmailEdit = (EditText)view.findViewById(R.id.addJobEmailEdit);
        addJobPhoneEdit = (EditText)view.findViewById(R.id.addJobPhoneEdit);

        progressView = (TextView)view.findViewById(R.id.progressView);
        progressView.setText(Integer.valueOf(fragmentID+1).toString() + "/7");

        //Set button NEXT onClick
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfStringIsEmpty(addJobEmailEdit.getText().toString())){
                    Toast.makeText(getActivity(), "Please insert email", Toast.LENGTH_LONG).show();
                }
                if(checkIfStringIsEmpty(addJobPhoneEdit.getText().toString())){
                    Toast.makeText(getActivity(), "Please insert phone number", Toast.LENGTH_LONG).show();
                } else{
                    addNewJobActivity.setJobContact(addJobPhoneEdit.getText().toString(), addJobEmailEdit.getText().toString());
                    addNewJobActivity.replaceNextFragment(fragmentID);
                }
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