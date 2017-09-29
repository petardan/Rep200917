package com.rpd.irepair;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Petar on 9/29/2017.
 */

public class LargeRepairmanInfoFragment extends DialogFragment {

    Repairman repairman;

    TextView repairmanNameAndSurname;

    TextView largeFragmentDescriptionView;
    TextView largeFragmentProfessionsView;
    TextView largeFragmentRegionsView;
    TextView largeFragmentAddressView;
    TextView largeFragmentEmailView;
    TextView largeFragmentMobile1View;
    TextView largeFragmentMobile2View;

    Button acceptRepairmanButton;
    Button declineRepairmanButton;

    RatingBar ratingBar;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.large_repairman_fragment, container, false);

        repairmanNameAndSurname = (TextView)rootView.findViewById(R.id.largeFragmentNameAndSurname);

        largeFragmentDescriptionView = (TextView)rootView.findViewById(R.id.largeFragmentDescriptionView);
        largeFragmentProfessionsView = (TextView)rootView.findViewById(R.id.largeFragmentProfessionsView);
        largeFragmentRegionsView = (TextView)rootView.findViewById(R.id.largeFragmentRegionsView);
        largeFragmentAddressView = (TextView)rootView.findViewById(R.id.largeFragmentAddressView);
        largeFragmentEmailView = (TextView)rootView.findViewById(R.id.largeFragmentEmailView);
        largeFragmentMobile1View = (TextView)rootView.findViewById(R.id.largeFragmentMobile1View);
        largeFragmentMobile2View = (TextView)rootView.findViewById(R.id.largeFragmentMobile2View);

        acceptRepairmanButton = (Button)rootView.findViewById(R.id.acceptRepairmanButton);
        declineRepairmanButton = (Button)rootView.findViewById(R.id.declineRepairmanButton);

        ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBar);

        getDialog().setTitle("Title");

        repairman = (Repairman) getArguments().getSerializable("REPAIRMAN");
        repairmanNameAndSurname.setText(repairman.getFirstName() + " " + repairman.getLastName());

        largeFragmentDescriptionView.setText(repairman.getDescription());
        largeFragmentProfessionsView.setText(repairman.getProfessionsString());
        largeFragmentRegionsView.setText(repairman.getRegionsString());
        largeFragmentAddressView.setText(repairman.getAddress());
        largeFragmentEmailView.setText(repairman.getEmail());
        largeFragmentMobile1View.setText(repairman.getMobilePhone1());
        largeFragmentMobile2View.setText(repairman.getMobilePhone2());


        //Accept button onClick listener
        acceptRepairmanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(rootView, "Repairman accepted!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Decline button onClick listener
        declineRepairmanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Rating bar number of filled stars
        ratingBar.setRating(Float.parseFloat(Double.valueOf(repairman.getAverageRating()).toString()));

        return rootView;
    }

}
