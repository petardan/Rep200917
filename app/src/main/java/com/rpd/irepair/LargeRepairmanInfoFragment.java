package com.rpd.irepair;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Petar on 9/29/2017.
 */

public class LargeRepairmanInfoFragment extends DialogFragment {

    TextView repairmanNameAndSurname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.large_repairman_fragment, container, false);

        repairmanNameAndSurname = (TextView)rootView.findViewById(R.id.largeFragmentNameAndSurname);
        getDialog().setTitle("Title");

        Repairman repairman = (Repairman) getArguments().getSerializable("REPAIRMAN");
        repairmanNameAndSurname.setText(repairman.getFirstName() + " " + repairman.getLastName());
        //parentLayout = (RelativeLayout) rootView.findViewById(R.id.parent_layout);
        //addTestLayout();



        //Dismiss alarm fragment
        /*stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/



        return rootView;
    }

}
