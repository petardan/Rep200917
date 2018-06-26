package com.rpd.AddJobFragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rpd.irepair.AddNewJobActivity;
import com.rpd.irepair.R;

import java.util.Calendar;

/**
 * Created by Petar on 11/9/2017.
 */

public class AddJobEndDateFragment extends Fragment {

    Button nextButton;
    Button previousButton;
    Button cancelJob;

    TextView progressView;

    EditText addJobEndDate;
    EditText addJobEndTime;

    //Fragment id
    int fragmentID = 3;

    AddNewJobActivity addNewJobActivity;
    AddJobEndDateFragment addJobEndDateFragment;

    int currentYear;
    int currentMonth;
    int currentDay;
    int currentHour;
    int currentMinute;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_job_end_date_fragment_layout, container, false);

        //Get add job activity
        addNewJobActivity = (AddNewJobActivity)getActivity();
        addJobEndDateFragment = this;

        nextButton = (Button)view.findViewById(R.id.buttonAddJob);
        previousButton = (Button)view.findViewById(R.id.buttonPrevious);
        cancelJob = (Button)view.findViewById(R.id.buttonCancel);

        progressView = (TextView)view.findViewById(R.id.progressView);
        progressView.setText(Integer.valueOf(fragmentID+1).toString() + "/7");

        addJobEndDate = (EditText)view.findViewById(R.id.addJobEndDateEdit);
        addJobEndTime = (EditText)view.findViewById(R.id.addJobEndTimeEdit);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DAY_OF_MONTH);
        currentHour = c.get(Calendar.HOUR_OF_DAY);
        currentMinute = c.get(Calendar.MINUTE);

        //Select date and time pickers
        addJobEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(addNewJobActivity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                addJobEndDateFragment.setSelectedDate(Integer.valueOf(dayOfMonth).toString() + "-" + Integer.valueOf(monthOfYear+1).toString() + "-" + Integer.valueOf(year).toString());
                            }
                        }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
            }
        });

        addJobEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(addNewJobActivity,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if(minute < 10){
                                    addJobEndDateFragment.setSelectedTime(Integer.valueOf(hourOfDay).toString() + ":0" + Integer.valueOf(minute).toString());
                                } else{
                                    addJobEndDateFragment.setSelectedTime(Integer.valueOf(hourOfDay).toString() + ":" + Integer.valueOf(minute).toString());
                                }                            }
                        }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });

        //Set button NEXT onClick
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfStringIsEmpty(addJobEndDate.getText().toString())){
                    Toast.makeText(getActivity(), "Please insert end job date", Toast.LENGTH_LONG).show();
                }
                else{
                    addNewJobActivity.setJobEndDate(addJobEndDate.getText().toString(), addJobEndTime.getText().toString());
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

    //Set selected date from date picker to edit text
    public void setSelectedDate(String date){
        addJobEndDate.setText(date);
    }

    //Set selected time from time picker to edit text
    public void setSelectedTime(String date){
        addJobEndTime.setText(date);
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