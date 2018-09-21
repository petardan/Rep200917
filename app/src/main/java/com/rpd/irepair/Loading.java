package com.rpd.irepair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rpd.customClasses.Profession;
import com.rpd.customClasses.Region;
import com.rpd.datawrappers.DataWrapperProfessions;
import com.rpd.datawrappers.DataWrapperRegions;
import com.rpd.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

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

    ArrayList<Profession> professions;
    ArrayList<Region> regions;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

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

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //Define elements
        backgroundLogo = (ImageView)findViewById(R.id.backgroundLogo);
        title = (TextView)findViewById(R.id.title);

        regions = getRegions();


        //Start animation
        startAnimation();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Toast.makeText(context, "Welcome "+user.getDisplayName(), Toast.LENGTH_LONG).show();
                    //Countdown timer that performs login check after animation is done (3sec)
                    timer = new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }
                        public void onFinish() {
                            checkingForUpdate();
                        }
                    }.start();
                }
                else {
                    Intent i = new Intent(Loading.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        };




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

                //For testing purposes - move on
                checkIfUserIslogged();
            }
        });

        // add the request object to the queue to be executed
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

            //For testing purposes - move on
            checkIfUserIslogged();
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
        }
    }

    private void versionUpToDate() {

        //For testing purposes - move on
        checkIfUserIslogged();
    }

    private ArrayList<Profession> getProfessions() {

        //Create dummy professions for testing purposes
        ArrayList<Profession> professions = new ArrayList<Profession>();
        Profession profession1 = new Profession(Integer.valueOf(1).toString(),Integer.valueOf(100).toString(), "profession1", "prof1_desc");
        Profession profession2 = new Profession(Integer.valueOf(2).toString(),Integer.valueOf(100).toString(), "profession2", "prof2_desc");
        Profession profession3 = new Profession(Integer.valueOf(3).toString(),Integer.valueOf(200).toString(), "profession3", "prof3_desc");
        Profession profession4 = new Profession(Integer.valueOf(4).toString(),Integer.valueOf(200).toString(), "profession4", "prof4_desc");

        professions.add(profession1);
        professions.add(profession2);
        professions.add(profession3);
        professions.add(profession4);
        return professions;
    }

    private ArrayList<Region> getRegions() {

        //Create dummy regions  for testing purposes
        ArrayList<Region> regions = new ArrayList<Region>();
        Region region1 = new Region("1", "Region1", "Skopje");
        Region region2 = new Region("2", "Region2", "Skopje");
        Region region3 = new Region("3", "Region3", "Ohrid");
        Region region4 = new Region("4", "Region4", "Bitola");

        regions.add(region1);
        regions.add(region2);
        regions.add(region3);
        regions.add(region4);
        return regions;
    }



    private void startAnimation() {
        backgroundLogo.setAlpha(0f);
        backgroundLogo.setVisibility(View.VISIBLE);
        backgroundLogo.animate().alpha(1f).setDuration(2000).setListener(null);

        title.setAlpha(0f);
        title.setVisibility(View.VISIBLE);
        title.animate().alpha(1f).setDuration(2000).setListener(null);
    }

    public void checkIfUserIslogged(){
        if (auth.getCurrentUser() != null) {
            Log.d("USER", "User logged in");
            Intent i = new Intent(Loading.this, MainActivity.class);
            i.putExtra("PROFESSIONS", new DataWrapperProfessions(professions));
            i.putExtra("REGIONS", new DataWrapperRegions(regions));
            startActivity(i);
            finish();
        }
        else{
            Log.d("USER", "User not logged in");
            startActivity(new Intent(Loading.this, LoginActivity.class));
            finish();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(mAuthStateListener);
    }
}
