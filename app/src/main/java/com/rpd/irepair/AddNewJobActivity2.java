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


        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Applications"));
        tabLayout.addTab(tabLayout.newTab().setText("Books"));
        tabLayout.addTab(tabLayout.newTab().setText("Games"));
        tabLayout.addTab(tabLayout.newTab().setText("Job Title"));

        //replace default fragment
        replaceFragment(new ApplicationFragment());

        //handling tab click event
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new ApplicationFragment());
                } else if (tab.getPosition() == 1) {
                    replaceFragment(new BookFragment());
                } else if (tab.getPosition() == 2){
                    replaceFragment(new GameFragment());
                } else {
                    replaceFragment(new AddJobTitleFragment());
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