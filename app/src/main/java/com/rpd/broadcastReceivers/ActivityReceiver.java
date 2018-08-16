package com.rpd.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rpd.customClasses.FriendlyMessage;
import com.rpd.irepair.OpenedJobsPerUserActivity;

public class ActivityReceiver extends BroadcastReceiver {

    OpenedJobsPerUserActivity currentActivity;

    @Override
    public void onReceive(Context context, Intent intent) {

        currentActivity = (OpenedJobsPerUserActivity)context;

        Bundle receive = intent.getExtras();
        //jobID = (String) receive.getString("JOBID");
        if (receive != null) {
            FriendlyMessage message = (FriendlyMessage) receive.getSerializable("FRIENDLY_MESSAGE");
            currentActivity.messageReceived(message);
        }
    }

}
