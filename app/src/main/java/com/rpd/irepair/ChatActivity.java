package com.rpd.irepair;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rpd.customAdapters.MessageAdapter;
import com.rpd.customClasses.FriendlyMessage;
import com.rpd.customClasses.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String jobID;
    private Job currentJob;

    private static final String TAG = "ChatActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final String FRIENDLY_MSG_LENGTH_KEY = "friendly_msg_length";
    public static final int RC_SIGN_IN = 1;
    public static final int RC_PHOTO_PICKER = 2;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;

    SharedPreferences chatPrefs;

    public boolean isAppForeground = false;

    //Firebase instancve variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference chatDatabaseReference;
    private ChildEventListener chatChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle receive = getIntent().getExtras();
        //jobID = (String) receive.getString("JOBID");
        currentJob = (Job) receive.getSerializable("JOB");
        jobID = currentJob.getJobId();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        isAppForeground = true;

        chatPrefs = getSharedPreferences("chat_prefs", Activity.MODE_PRIVATE);

        mUsername = ANONYMOUS;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        chatDatabaseReference = mFirebaseDatabase.getReference().child("chat").child(jobID);
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click

                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, null, currentUser.getUid(), System.currentTimeMillis(), currentJob.getJobId());
                updateLastMessageReadTimestamp(friendlyMessage.getTimestamp());
                chatDatabaseReference.push().setValue(friendlyMessage);

                // Clear input box
                mMessageEditText.setText("");
            }
        });


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null){
                    //user is signed in
                    String displayName = currentUser.getDisplayName();
                    if(displayName.equalsIgnoreCase("")){
                        displayName = currentUser.getEmail();
                    }
                    onSignInInitialize(displayName);
                }
                else {
                    //user is signed out
                    onSignOutCleanup();
                    Intent i = new Intent(ChatActivity.this , LoginActivity.class);
                    startActivity(i);
                }
            }
        };

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put(FRIENDLY_MSG_LENGTH_KEY, DEFAULT_MSG_LENGTH_LIMIT);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);
        fetchConfig();
    }

    public void fetchConfig(){
        long cacheExpiration = 3600;
        if(mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mFirebaseRemoteConfig.activateFetched();
                applyRetrievedLengthLimit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error fetching config", e);
                applyRetrievedLengthLimit();
            }
        });
    }

    private void applyRetrievedLengthLimit() {
        Long friendly_msg_length = mFirebaseRemoteConfig.getLong(FRIENDLY_MSG_LENGTH_KEY);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(friendly_msg_length.intValue())});
        Log.d(TAG, FRIENDLY_MSG_LENGTH_KEY + " = " + friendly_msg_length);
    }


    private void onSignInInitialize(String displayName) {
        mUsername = displayName;
        attachDatabaseReadListener();

    }

    private void onSignOutCleanup() {
        mUsername = ANONYMOUS;
        mMessageAdapter.clear();
        detachDatabaseReadListener();

    }

    private void attachDatabaseReadListener() {
        if(chatChildEventListener == null){
            chatChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    Log.d("Message", friendlyMessage.getName());
                    mMessageAdapter.add(friendlyMessage);



                    if(messageIsFromCurrentUser(friendlyMessage.getSenderID(), currentUser.getUid())){
                        //Do nothing
                        Log.d("NotificationChat", "From current user");
                        updateLastMessageReadTimestamp(friendlyMessage.getTimestamp());
                    }
                    else {

                        if(isAppForeground){
                            //The application is in foreground, do not show the notification
                            Log.d("Notification", "App is in foreground " + String.valueOf(isAppForeground));
                        }
                        else{
                            //The app is in background, show the notification
                            Log.d("Notification", "App is in background " + String.valueOf(isAppForeground));
                            if (isTheCurrentJob(friendlyMessage.getJobID(), currentJob.getJobId())){
                                if (isMessageRead(friendlyMessage.getTimestamp(), getLastMessageReadTimestamp())){
                                    //Do nothing
                                    Log.d("NotificationChat", "Already read");
                                }
                                else{
                                    //TO DO: Show notification only if app is in background
                                    Log.d("NotificationChat", "NEW");
                                    updateLastMessageReadTimestamp(friendlyMessage.getTimestamp());
                                    createNotification(friendlyMessage.getName(), friendlyMessage.getText(), currentJob);
                                }
                            } else{
                                //TO DO: Show notification if only if app is in foreground
                                Log.d("NotificationChat", "For another job");
                                createNotification(friendlyMessage.getName(), friendlyMessage.getText(), currentJob);
                            }
                        }


                    }


                   /* if (friendlyMessage.getSenderID().equalsIgnoreCase(currentUser.getUid())){
                        //Message  from the current user
                        Log.d("NotificationChat", "From current user");
                    } else if (friendlyMessage.getTimestamp() <= getLastMessageReadTimestamp()){
                        //Message is already read
                        Log.d("NotificationChat", "Already read");
                    }
                    else{
                        updateLastMessageReadTimestamp(friendlyMessage.getTimestamp());
                        createNotification(friendlyMessage.getName(), friendlyMessage.getText(), currentJob);
                    }*/


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            chatDatabaseReference.addChildEventListener(chatChildEventListener);
        }
    }

    private boolean isMessageRead(Long messageTimestamp, Long lastMessageReadTimestamp) {
        if(messageTimestamp <= lastMessageReadTimestamp){
            return true;
        } else {
            return false;
        }
    }

    private boolean isTheCurrentJob(String messageJobID, String jobId) {
        if (messageJobID.equalsIgnoreCase(jobId)){
            return true;
        } else {
            return false;
        }
    }


    private boolean messageIsFromCurrentUser(String senderID, String uid) {
        if(senderID.equalsIgnoreCase(uid)){
            return true;
        } else{
            return false;
        }
    }

    private void updateLastMessageReadTimestamp(Long timestamp) {
        SharedPreferences.Editor editor = chatPrefs.edit();
        editor.putLong("LAST_MESSAGE_READ_TIMESTAMP", timestamp);
        editor.commit();
    }

    private Long getLastMessageReadTimestamp(){
        Long lastMessageReadTimestamp = chatPrefs.getLong("LAST_MESSAGE_READ_TIMESTAMP", 0);
        return lastMessageReadTimestamp;
    }

    private void detachDatabaseReadListener(){
        if(chatChildEventListener != null){
            chatDatabaseReference.removeEventListener(chatChildEventListener);
            chatChildEventListener = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                //sign out
                mFirebaseAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
            //Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, downloadUrl.toString(), currentUser.getUid(), System.currentTimeMillis(), currentJob.getJobId());
                    chatDatabaseReference.push().setValue(friendlyMessage);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        isAppForeground = true;
        Log.d("Notification", "App is in foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        //detachDatabaseReadListener();
        //mMessageAdapter.clear();
        isAppForeground = false;
        Log.d("Notification", "App is in background");
    }

    //Add notification for new recived message
    public void createNotification(String name, String text, Job job) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle(name + " says:")
                        .setContentText(text);


        Bundle bundle = new Bundle();
        bundle.putSerializable("JOB", job);
        Intent resultIntent = new Intent(this, ChatActivity.class);
        resultIntent.putExtras(bundle);

    // Because clicking the notification opens a new ("special") activity, there's
    // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

    // Sets an ID for the notification
        long jobIDlong = Long.valueOf(currentJob.getJobId()) / 10000;
        int mNotificationId = (int)jobIDlong;
    // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }



}
