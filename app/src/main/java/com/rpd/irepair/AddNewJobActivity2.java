package com.rpd.irepair;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rpd.AddJobFragments.AddJobAddressFragment;
import com.rpd.AddJobFragments.AddJobContactFragment;
import com.rpd.AddJobFragments.AddJobEndDateFragment;
import com.rpd.AddJobFragments.AddJobSeverityFragment;
import com.rpd.AddJobFragments.AddJobStartDateFragment;
import com.rpd.AddJobFragments.AddJobSummaryFragment;
import com.rpd.AddJobFragments.AddJobTitleFragment;
import com.rpd.AddJobFragments.ApplicationFragment;
import com.rpd.AddJobFragments.BookFragment;
import com.rpd.AddJobFragments.GameFragment;

public class AddNewJobActivity2 extends AppCompatActivity {


    ImageView addJobJobTitleHelp;
    TextView addJobJobTitle;
    LinearLayout addJobJobTitleHintLayout;

    private TabLayout tabLayout;
    private LinearLayout container;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_job_test);

        context = this;

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        container = (LinearLayout) findViewById(R.id.fragment_container);

        //Defining add job fragments
        final Fragment titleFragment = new AddJobTitleFragment();
        final Fragment severityFragment = new AddJobSeverityFragment();
        final Fragment startDateFragment = new AddJobStartDateFragment();
        final Fragment endDateFragment = new AddJobEndDateFragment();
        final Fragment addressFragment = new AddJobAddressFragment();
        final Fragment contactFragment = new AddJobContactFragment();
        final Fragment summaryFragment = new AddJobSummaryFragment();


        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Title"));
        tabLayout.addTab(tabLayout.newTab().setText("Severity"));
        tabLayout.addTab(tabLayout.newTab().setText("Start"));
        tabLayout.addTab(tabLayout.newTab().setText("End"));
        tabLayout.addTab(tabLayout.newTab().setText("Address"));
        tabLayout.addTab(tabLayout.newTab().setText("Contact"));
        tabLayout.addTab(tabLayout.newTab().setText("Summary"));


        //replace default fragment
        replaceFragment(new ApplicationFragment());

        //handling tab click event
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(titleFragment);
                } else if (tab.getPosition() == 1) {
                    replaceFragment(severityFragment);
                } else if (tab.getPosition() == 2){
                    replaceFragment(startDateFragment);
                } else if (tab.getPosition() == 3){
                    replaceFragment(endDateFragment);
                } else if (tab.getPosition() == 4){
                    replaceFragment(addressFragment);
                } else if (tab.getPosition() == 5){
                    replaceFragment(contactFragment);
                } else if (tab.getPosition() == 6){
                    replaceFragment(summaryFragment);
                }else {
                    replaceFragment(titleFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit);

        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }
}