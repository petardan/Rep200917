package com.rpd.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rpd.services.BackgroundService;

public class iRepairBootReceiver extends BroadcastReceiver {

    String TAG = "iRepairBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent arg1) {
        Intent intent = new Intent(context, BackgroundService.class);
        context.startService(intent);
        Log.d(TAG, "Background Service is started");
    }
}

