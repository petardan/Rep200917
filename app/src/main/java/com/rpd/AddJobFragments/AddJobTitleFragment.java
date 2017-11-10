package com.rpd.AddJobFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rpd.irepair.AddNewJobActivity2;
import com.rpd.irepair.R;

/**
 * Created by Petar on 11/9/2017.
 */

public class AddJobTitleFragment extends Fragment {

    Button nextButton;
    Button previousButton;

    //Fragment id
    int fragmentID = 0;

    AddNewJobActivity2 addNewJobActivity2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_job_title_fragment_layout, container, false);

        //Get add job activity
        addNewJobActivity2 = (AddNewJobActivity2)getActivity();

        nextButton = (Button)view.findViewById(R.id.buttonNext);
        previousButton = (Button)view.findViewById(R.id.buttonPrevious);

        //Set button NEXT onClick
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewJobActivity2.replaceNextFragment(fragmentID);
            }
        });

        return view;
    }

}