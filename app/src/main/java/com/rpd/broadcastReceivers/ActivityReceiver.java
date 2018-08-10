package com.rpd.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.rpd.customClasses.FriendlyMessage;
import com.rpd.irepair.OpenJobsPerUserActivity;

public class ActivityReceiver extends BroadcastReceiver {

    OpenJobsPerUserActivity currentActivity;

    @Override
    public void onReceive(Context context, Intent intent) {

        currentActivity = (OpenJobsPerUserActivity)context;

        Bundle receive = intent.getExtras();
        //jobID = (String) receive.getString("JOBID");
        if (receive != null) {
            FriendlyMessage message = (FriendlyMessage) receive.getSerializable("FRIENDLY_MESSAGE");
            currentActivity.messageReceived(message);
        }
    }

}
