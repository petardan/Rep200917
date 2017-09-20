package com.rpd.irepair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rpd.volley.AppController;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    EditText registerUsername;
    EditText registerEmail;
    EditText registerPassword;
    Button btnRegister;
    Button btnLinkToLoginScreen;
    ImageView userImage;
    RelativeLayout inProgress;
    private final int SELECT_PHOTO = 1;

    String username;
    String email;
    String password;

    Context context;

    String url = "http://80.77.147.21:81/iRepair/post_register.php";
    String imageString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_registration);

        //Define elements
        registerUsername = (EditText)findViewById(R.id.registerUsername);
        registerEmail = (EditText)findViewById(R.id.registerEmail);
        registerPassword = (EditText)findViewById(R.id.registerPassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnLinkToLoginScreen = (Button)findViewById(R.id.btnLinkToLoginScreen);
        userImage = (ImageView)findViewById(R.id.userimage);
        inProgress = (RelativeLayout)findViewById(R.id.inProgress);
        username = registerUsername.getText().toString();
        email = registerEmail.getText().toString();
        password = registerPassword.getText().toString();


        //Initializing
        context = this;


        //Text Change Listeners if the user edits the textfields
        registerUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                username = registerUsername.getText().toString();
            }
        });

        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                email = registerEmail.getText().toString();
            }
        });

        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                password = registerPassword.getText().toString();
            }
        });


        //OnClick listeners

        btnLinkToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.length()<4){
                    Toast.makeText(context, "Username too short !", Toast.LENGTH_SHORT).show();
                }
                else {
                    if((!email.contains("@"))||(!email.contains("."))){
                        Toast.makeText(context, "Invalid email address !", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(password.length()<8){
                            Toast.makeText(context, "Password too short !", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(imageString.equals("")){
                                Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.user);
                                userImage.setImageBitmap(icon);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                icon.compress(Bitmap.CompressFormat.PNG, 50, baos); //bm is the bitmap object
                                byte[] b = baos.toByteArray();
                                imageString = Base64.encodeToString(b, 0);
                                registerUser(url, username, password, email, imageString);
                                inProgress.setVisibility(View.VISIBLE);
                            }
                            else{
                                registerUser(url, username, password, email, imageString);
                            }
                        }
                    }
                }
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECT_PHOTO);
            }
        });
    }

    //Function for sending params to server
    private void registerUser(String url, final String username,
                              final String password, final String email, final String imageString) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("processFinish", response);
                if(Integer.parseInt(response)==200){
                    Toast.makeText(context, "Registration Successfull! You can now log in!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registration.this, Login.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(context, "Error registering user on the server", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registration.this, Registration.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error sending params", "Error: " + error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                //HTTP POST params
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("image", imageString);
                Log.d("slika vo tekst", imageString);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }


    //Function for getting selected user image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode==RESULT_OK){
                    try{
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap sourceImage = BitmapFactory.decodeStream(imageStream);
                        final Bitmap selectedImage = Bitmap.createScaledBitmap(sourceImage, 200, 200, false);
                        userImage.setImageBitmap(selectedImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 50, baos); //bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        imageString = Base64.encodeToString(b, 0);

                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
        }
    }
}
