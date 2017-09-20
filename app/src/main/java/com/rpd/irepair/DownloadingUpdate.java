package com.rpd.irepair;



import java.io.File;

import com.rpd.irepair.R;
import com.rpd.irepair.DownloadingProcess.AsyncResponse;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;

public class DownloadingUpdate extends Activity implements AsyncResponse{

    String apkName = "neolive";
    String apkLocation = "http://80.77.147.21:81/NeoLiveHeadend/NeoLivePHP/update/";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            apkName = extras.getString("apkName");
            apkLocation = extras.getString("apkLocation");
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_downloading_update);

        DownloadingProcess download = new DownloadingProcess(this);
        download.execute(apkLocation+apkName+".apk");

    }

    @Override
    public void processFinish(String result) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + apkName + ".apk")), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }



}
