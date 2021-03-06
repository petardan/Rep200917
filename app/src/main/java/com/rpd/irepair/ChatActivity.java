package com.rpd.irepair;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@TargetApi(Build.VERSION_CODES.N)
public class ChatActivity extends AppCompatActivity {

    private String jobID;
    private Job currentJob;

    private static final String TAG = "ChatActivity";
    static final String NOTIFICATION_TAG = "NOTIFICATION_TAG";

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

    //Notification variables
    String channelID = "iRNC";
    String channelName = "iRepair Notification Channel";
    String channelDesc = "This is iRepair Notification Channel";
    int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle receive = getIntent().getExtras();
        //jobID = (String) receive.getString("JOBID");
        if (receive != null) {
            currentJob = (Job) receive.getSerializable("JOB");
        }
        if (currentJob != null) {
            jobID = currentJob.getJobId();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatPrefs = getSharedPreferences("chat_prefs", Activity.MODE_PRIVATE);
        removeJobIDFromUnreadMessages(jobID);

        setIsChatAppForeground(true, jobID);

        //Create Notifications channel
        createNotificationChannel();

        mUsername = ANONYMOUS;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        // Initialize references to views
        mProgressBar = findViewById(R.id.progressBar);
        mMessageListView =  findViewById(R.id.messageListView);
        mPhotoPickerButton = findViewById(R.id.photoPickerButton);
        mMessageEditText = findViewById(R.id.messageEditText);
        mSendButton = findViewById(R.id.sendButton);

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

                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, null, currentUser.getUid(), System.currentTimeMillis(), currentJob.getJobId(), 1);
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
                    if (displayName != null && displayName.equalsIgnoreCase("")) {
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

    private void watchForNewMessages() {
        chatDatabaseReference = mFirebaseDatabase.getReference().child("chat").child(jobID);
        attachDatabaseReadListener();

    }

    private void stopReceivingMessages(){
        if(chatChildEventListener != null){
            chatDatabaseReference.removeEventListener(chatChildEventListener);
            chatChildEventListener = null;
        }
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
        watchForNewMessages();

    }

    private void onSignOutCleanup() {
        mUsername = ANONYMOUS;
        mMessageAdapter.clear();
        stopReceivingMessages();

    }

    private void attachDatabaseReadListener() {
        if(chatChildEventListener == null){
            chatChildEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    //Mark message as read (status -> 1)
                    dataSnapshot.getRef().child("status").setValue(1);
                    if (friendlyMessage != null) {
                        Log.d(NOTIFICATION_TAG, friendlyMessage.getText() + "!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                    mMessageAdapter.add(friendlyMessage);
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

    private void updateLastMessageReadTimestamp(Long timestamp) {
        SharedPreferences.Editor editor = chatPrefs.edit();
        editor.putLong("LAST_MESSAGE_READ_TIMESTAMP", timestamp);
        editor.apply();
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
            StorageReference photoRef = null;
            if (selectedImageUri != null) {
                photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
            }
            //Upload file to Firebase Storage
            if (photoRef != null) {
                photoRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        FriendlyMessage friendlyMessage = null;
                        if (downloadUrl != null) {
                            friendlyMessage = new FriendlyMessage(null, mUsername, downloadUrl.toString(), currentUser.getUid(), System.currentTimeMillis(), currentJob.getJobId(), 1);
                        }
                        chatDatabaseReference.push().setValue(friendlyMessage);
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        setIsChatAppForeground(true, jobID);
        Log.d(NOTIFICATION_TAG, "OnRESUME - App is in foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        stopReceivingMessages();
        setIsChatAppForeground(false, "");
        Log.d(NOTIFICATION_TAG, "OnPause - App is in background");
    }

    @Override
    protected void onStop() {
        super.onStop();
        setIsChatAppForeground(false, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setIsChatAppForeground(false, "");
    }

    //Create notification channel
    public void createNotificationChannel(){
        //Only for Android O+, API 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, channelImportance);
            channel.setDescription(channelDesc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void setIsChatAppForeground(boolean isAppForeground, String jobID){
        SharedPreferences.Editor editor = chatPrefs.edit();
        editor.putBoolean("ISAPPFOREGROUND", isAppForeground);
        editor.putString("APPFOREGROUNDJOBID", jobID);
        editor.apply();
    }

    private void removeJobIDFromUnreadMessages(String jobID) {
        Set<String> set = chatPrefs.getStringSet("UNREAD_MESSAGE_JOBID", null);
        if (set != null){
            ArrayList<String> arrayFromSet = new ArrayList<>(set);
            for (int i=0; i<arrayFromSet.size(); i++){
                if (arrayFromSet.get(i).equalsIgnoreCase(jobID)){
                    arrayFromSet.remove(i);
                }
            }
            //After jobID is removed, add new array into shared prefs
            SharedPreferences.Editor editor = chatPrefs.edit();
            Set<String> newSet = new HashSet<>();
            set.addAll(arrayFromSet);
            editor.putStringSet("UNREAD_MESSAGE_JOBID", newSet);
            editor.apply();
        }
    }
}
