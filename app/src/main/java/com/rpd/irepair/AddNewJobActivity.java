package com.rpd.irepair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rpd.AddJobFragments.AddJobAddressFragment;
import com.rpd.AddJobFragments.AddJobContactFragment;
import com.rpd.AddJobFragments.AddJobEndDateFragment;
import com.rpd.AddJobFragments.AddJobSeverityFragment;
import com.rpd.AddJobFragments.AddJobStartDateFragment;
import com.rpd.AddJobFragments.AddJobSummaryFragment;
import com.rpd.AddJobFragments.AddJobTitleFragment;
import com.rpd.customClasses.Job;
import com.rpd.customClasses.Repairman;

public class AddNewJobActivity extends AppCompatActivity{


    //Firebase
    private FirebaseAuth auth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference jobsperuserDatabaseReference;
    DatabaseReference jobsperrepairmanDatabaseReference;
    FirebaseUser currentFirebaseUser;

    private LinearLayout container;

    //Defining add job fragments
    final Fragment titleFragment = new AddJobTitleFragment();
    final Fragment severityFragment = new AddJobSeverityFragment();
    final Fragment startDateFragment = new AddJobStartDateFragment();
    final Fragment endDateFragment = new AddJobEndDateFragment();
    final Fragment addressFragment = new AddJobAddressFragment();
    final Fragment contactFragment = new AddJobContactFragment();
    final Fragment summaryFragment = new AddJobSummaryFragment();

    Context context;

    Repairman selectedRepairman;

    //Job parameters
    String jobId;
    String repairmanId;
    String userId;
    String jobTitle;
    String jobDesciption;
    String jobSeverity;
    String jobStartDate;
    String jobEndDate;
    String userAddress;
    String userEmail;
    String userPhone;

    Job job;

    public String getJobId() {
        return jobId;
    }

    public String getUserId() {
        return userId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobDesciption() {
        return jobDesciption;
    }

    public String getJobSeverity() {
        return jobSeverity;
    }

    public String getJobStartDate() {
        return jobStartDate;
    }

    public String getJobEndDate() {
        return jobEndDate;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getRepairmanId() {
        return repairmanId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle receive = getIntent().getExtras();
        selectedRepairman = (Repairman) receive.getSerializable("REPAIRMAN");
        repairmanId = selectedRepairman.getrepairmanId();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_job2);

        context = this;

        container = (LinearLayout) findViewById(R.id.fragment_container);

        //Get Firebase instances
        auth = FirebaseAuth.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userId = currentFirebaseUser.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        jobsperuserDatabaseReference = mFirebaseDatabase.getReference().child("jobsperuser");
        jobsperrepairmanDatabaseReference = mFirebaseDatabase.getReference().child("jobsperrepairman");




        //replace default fragment
        replaceFragment(titleFragment);

    }

    //Replace fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }

    //Replace fragment when user clicks on next
    public void replaceNextFragment(int fragmentID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);

        switch (fragmentID){
            case 0:
                transaction.replace(R.id.fragment_container, severityFragment);
                transaction.commit();
                break;
            case 1:
                transaction.replace(R.id.fragment_container, startDateFragment);
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.fragment_container, endDateFragment);
                transaction.commit();
                break;
            case 3:
                transaction.replace(R.id.fragment_container, addressFragment);
                transaction.commit();
                break;
            case 4:
                transaction.replace(R.id.fragment_container, contactFragment);
                transaction.commit();
                break;
            case 5:
                transaction.replace(R.id.fragment_container, summaryFragment);
                transaction.commit();
                break;
            default:

        }


    }


    //replace fragment when user clicks on Previous
    public void replacePreviousFragment(int fragmentID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_from_right);

        switch (fragmentID){
            case 1:
                transaction.replace(R.id.fragment_container, titleFragment);
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.fragment_container, severityFragment);
                transaction.commit();
                break;
            case 3:
                transaction.replace(R.id.fragment_container, startDateFragment);
                transaction.commit();
                break;
            case 4:
                transaction.replace(R.id.fragment_container, endDateFragment);
                transaction.commit();
                break;
            case 5:
                transaction.replace(R.id.fragment_container, addressFragment);
                transaction.commit();
                break;
            case 6:
                transaction.replace(R.id.fragment_container, contactFragment);
                transaction.commit();
                break;
            default:

        }
    }

    public Repairman getSelectedRepairman() {
        return selectedRepairman;
    }

    //Setting job parameter functions from the fragments
    public void setJobTitleAndDescription(String title, String description){
        jobTitle = title;
        jobDesciption = description;
    }

    public void setJobSeverity(String severity){
        jobSeverity = severity;
    }

    public void setJobStartDate(String date, String time){
        jobStartDate = date + " " + time;
    }

    public void setJobEndDate(String date, String time){
        jobEndDate = date + " " + time;
    }

    public void setJobAddress(String address) {
        userAddress = address;
    }

    public void setJobContact(String phone, String email) {
        userPhone = phone;
        userEmail = email;
    }

    public void addNewJob() {
        //To cancel the summary fragment after the job is added
        //onBackPressed();

        //Adding progress dialog
        final ProgressDialog addingJobProgressDialog = new ProgressDialog(context);
        addingJobProgressDialog.setCancelable(false);
        addingJobProgressDialog.setMessage("Adding new job...");
        addingJobProgressDialog.show();

        //Defining the job
        //Define unique job id - current time in milliseconds
        long time= System.currentTimeMillis();
        jobId = Long.valueOf(time).toString();
        job = new Job(getJobId(), getRepairmanId(), getUserId(), getJobTitle(), getJobDesciption(), getJobSeverity(), getJobStartDate(), getJobEndDate(), getUserAddress(), getUserEmail(), getUserPhone(), "1");

        //Add job per user to database
        jobsperuserDatabaseReference.child(currentFirebaseUser.getUid()).child("open").child(getJobId()).setValue(job).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    addingJobProgressDialog.dismiss();
                    addJobPerRepairman();
                } else{
                    addingJobProgressDialog.dismiss();
                    Toast.makeText(AddNewJobActivity.this, "Error adding job per user" + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addJobPerRepairman() {
        //Add job per user to database
        jobsperrepairmanDatabaseReference.child(selectedRepairman.getrepairmanId()).child("open").child(getJobId()).setValue(job).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(AddNewJobActivity.this, OpenJobsPerUserActivity.class));
                    finish();
                } else{
                    Toast.makeText(AddNewJobActivity.this, "Error adding job per repairman" + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}