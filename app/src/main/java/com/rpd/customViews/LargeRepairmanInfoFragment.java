package com.rpd.customViews;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rpd.irepair.AddNewJobActivity;
import com.rpd.irepair.R;
import com.rpd.customClasses.Repairman;

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

    Button selectRepairmanButton2;
    Button cancelRepairmanButton;
    Button ratingStarButton;

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

        selectRepairmanButton2 = (Button)rootView.findViewById(R.id.addJobAddButton2);

        cancelRepairmanButton = (Button)rootView.findViewById(R.id.cancelRepairmanButton);
        ratingStarButton = (Button)rootView.findViewById(R.id.ratingButton);

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

        ratingStarButton.setText(Double.valueOf(repairman.getAverageRating()).toString());


        //Accept add job button onClick listener
        selectRepairmanButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("REPAIRMAN", repairman);
                Intent i = new Intent(getActivity() , AddNewJobActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                LargeRepairmanInfoFragment.this.dismiss();
                /*Snackbar.make(rootView, "Repairman accepted!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        //Decline button onClick listener
        cancelRepairmanButton.setOnClickListener(new View.OnClickListener() {
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
