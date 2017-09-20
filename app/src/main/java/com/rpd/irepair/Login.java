package com.rpd.irepair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rpd.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    EditText loginUsername;
    EditText loginPassword;
    Button btnLogin;
    Button btnLinkToRegistrationScreen;

    String username;
    String password;

    SharedPreferences mPrefs;
    Context context;
    String url = "http://80.77.147.21:81/iRepair/post_login_json.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_login);

        //Define elements
        loginUsername = (EditText)findViewById(R.id.loginUsername);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLinkToRegistrationScreen = (Button)findViewById(R.id.btnLinkToRegisterScreen);

        //Initializing
        username = loginUsername.getText().toString();
        password = loginPassword.getText().toString();

        mPrefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
        context = this;

        //Text Change Listeners if the user edits the textfields
        loginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                username = loginUsername.getText().toString();
            }
        });

        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                password = loginPassword.getText().toString();
            }
        });

        //OnClickListeners
        btnLinkToRegistrationScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.length()<4){
                    Toast.makeText(context, "Invalid username !", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(password.length()<8){
                        Toast.makeText(context, "Invalid password !", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        logUserIn(username, password);
                    }
                }
            }
        });


    }


    //Function that tryes to log the user in by sending JSON params with username and password
    private void logUserIn(final String username, final String password) {
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        //Sending JSON params using volley
        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response ",response.toString());
                        String code = "399";
                        String message = "Error receiving parameters";

                        try {
                            JSONObject status = response.getJSONObject("status");
                            code = status.getString("code");
                            message = status.getString("message");

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        checkCode(code, message);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorLoggingIn("1000", "Error Sending JSON to AAAVMU server");
            }
        });
        // add the request object to the queue to be executed
        Log.d("JSONOBJRequest",req.toString());

        AppController.getInstance().addToRequestQueue(req);
    }


    //Function that checks the received code statuses and decides if the logging process was succesful
    private void checkCode(String code, String message) {
        if(code.equalsIgnoreCase("200")){
            successLoggingIn();
        }
        else{
            errorLoggingIn(code, message);
        }

    }


    //Function that handles succesful logging
    private void successLoggingIn() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean("logedIn", true);
        editor.putString("username", username);
        editor.apply();

        Toast.makeText(context, "Logging succesful: " + mPrefs.getString("username", "user"), Toast.LENGTH_SHORT).show();

        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
        finish();
    }



    //Function that handles errors while logging
    private void errorLoggingIn(String code, String message) {
        if (Integer.parseInt(code)==400){
            Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show();
        }
        else {
            if (Integer.parseInt(code)==402){
                Toast.makeText(context, "Wrong username", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "Error connecting to server!", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }


}
