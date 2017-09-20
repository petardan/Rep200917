package com.rpd.irepair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rpd.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Loading extends AppCompatActivity {

    ImageView backgroundLogo;
    TextView title;

    CountDownTimer timer;

    SharedPreferences mPrefs;
    Context context;

    //URL to check the latest version on server
    final String getLatestVersionUrl = "http://80.77.147.21:81/iRepair/get_app_latest_version.php";

    //Current version
    Float currentVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_loading);

        context = this;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(Loading.this);

        //Define elements
        backgroundLogo = (ImageView)findViewById(R.id.backgroundLogo);
        title = (TextView)findViewById(R.id.title);


        //Start animation
        startAnimation();

        //Countdown timer that performs login check after animation is done (3sec)
        timer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                //TODO when animation is done
                checkingForUpdate();
            }
        }.start();


    }

    //Start checking if update is needed
    private void checkingForUpdate() {
        //Get current app version
        try {
            //get app version
            currentVersion = Float.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
            Log.d("Current Version", currentVersion.toString());
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Get Version name", "Name Not Found exepction");
            currentVersion = (float)0;
            e.printStackTrace();
        }

        getLatestVersion(getLatestVersionUrl);
    }

    //Getting the latest version from the server
    private void getLatestVersion(String getLatestVersionUrl) {

        JsonObjectRequest req = new JsonObjectRequest(getLatestVersionUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response ",response.toString());
                        //Default values
                        String code = "399";
                        String versionNo = "Error receiving parameters";
                        String apkName = "neolive";
                        String apkLocation = "http://80.77.147.21:81/iRepair/update/";

                        try {
                            JSONObject status = response.getJSONObject("status");
                            code = status.getString("code");
                            versionNo = status.getString("version_no");
                            apkName = status.getString("apk_name");
                            apkLocation = status.getString("apk_location");

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        checkCode(code, versionNo, apkName, apkLocation);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(context, "Error getting update !", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // add the request object to the queue to be executed
        Log.d("JSONOBJRequest",req.toString());

        AppController.getInstance().addToRequestQueue(req);
    }

    //Check the code returned from server regarding the getLatestVersion HTTP POST
    private void checkCode(String code, String versionNo, String apkName, String apkLocation) {
        if(code.equalsIgnoreCase("200")){
            compareAppVersions(versionNo, apkName, apkLocation);
        }
        else{
            Toast toast = Toast.makeText(context, "Error getting update !", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    //Comparing the current app version and the latest version from the server
    public void compareAppVersions(String versionNo, String apkName, String apkLocation) {

        if (currentVersion<Float.valueOf(versionNo)){
            //Update will be initialised
            Toast toast = Toast.makeText(context, "New update Available !", Toast.LENGTH_LONG);
            toast.show();

            Intent i = new Intent(Loading.this, DownloadingUpdate.class);
            i.putExtra("apkName", apkName);
            i.putExtra("apkLocation", apkLocation);
            startActivity(i);
            finish();
        }
        else {
            //There is no need for update. Continue!
            //Delete the downloaded apk file, if any
            String PATH = Environment.getExternalStorageDirectory() + "/download/"+apkName+".apk";
            File file = new File(PATH);
            boolean deleted = file.delete();

            //Version is up to date, continue...
            versionUpToDate();
        };
    }

    private void versionUpToDate() {
        Intent i = new Intent(Loading.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void startAnimation() {
        backgroundLogo.setAlpha(0f);
        backgroundLogo.setVisibility(View.VISIBLE);
        backgroundLogo.animate().alpha(1f).setDuration(2000).setListener(null);

        title.setAlpha(0f);
        title.setVisibility(View.VISIBLE);
        title.animate().alpha(1f).setDuration(2000).setListener(null);
    }
}
