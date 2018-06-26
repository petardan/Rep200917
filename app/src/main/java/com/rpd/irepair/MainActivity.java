package com.rpd.irepair;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rpd.customClasses.Profession;
import com.rpd.customClasses.Region;
import com.rpd.customClasses.Repairman;
import com.rpd.customClasses.User;
import com.rpd.customViews.LargeRepairmanInfoFragment;
import com.rpd.customViews.SmallRepairmanItem;
import com.rpd.datawrappers.DataWrapperRegions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    SharedPreferences mPrefs;
    Context context;

    //Current loged-in user
    User user;

    ImageView headerProfilePictureImageView;
    TextView headerUsernameTextView;
    TextView headerEmailTextView;


    //Category navigationVIew
    NavigationView categoryNavigationView;
    Menu navigationDrawerMenu;

    //Define list of professions, regions and repairmans, for testing purpose
    ArrayList<Profession> professions;
    ArrayList<Region> regions;
    ArrayList<Repairman> repairmans;

    //Grid layout for the repairman list
    GridLayout repairmanList;

    //Firebase authentification
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser currentFirebaseUser;

    //Firebase database
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference professionsDatabaseReference;
    DatabaseReference userDatabaseReference;
    DatabaseReference repairmanDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Getting the professions list from Loading.class
        DataWrapperRegions dwReg = (DataWrapperRegions) getIntent().getSerializableExtra("REGIONS");

        professions = new ArrayList<Profession>();
        repairmans = new ArrayList<Repairman>();
        regions = dwReg.getParliaments();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Start of Initializing

        context = this;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);



        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Initiate Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        professionsDatabaseReference = mFirebaseDatabase.getReference().child("professions");
        userDatabaseReference = mFirebaseDatabase.getReference().child("users").child(currentFirebaseUser.getUid());
        repairmanDatabaseReference = mFirebaseDatabase.getReference().child("repairmans");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add job button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //Left side drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        categoryNavigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerMenu = categoryNavigationView.getMenu();


        categoryNavigationView.inflateMenu(R.menu.categories);

        categoryNavigationView.setNavigationItemSelectedListener(this);

        repairmanList = (GridLayout)findViewById(R.id.repairmen_list);
        //End of Initializing


        //Firebase Auth listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                }
                else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        //Adding the listener to the database reference
        professionsDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                professions.add(dataSnapshot.getValue(Profession.class)); //add result into array list
                addProfessionToDrawer(dataSnapshot.getValue(Profession.class));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                professions.add(dataSnapshot.getValue(Profession.class)); //add result into array list
                addProfessionToDrawer(dataSnapshot.getValue(Profession.class));
                Log.d("Profesion", s);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Error retreiving professions from database", Toast.LENGTH_LONG).show();
            }
        });

        //Get current user
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    user = dataSnapshot.getValue(User.class);
                    setUserHeaderProfile(user);
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Get repairmans
        repairmanDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                repairmans.add(dataSnapshot.getValue(Repairman.class)); //add result into array list
                addRepairmanItem(dataSnapshot.getValue(Repairman.class));
                Log.d("Test", dataSnapshot.toString());
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
        });

    }



    private void setUserHeaderProfile(User user) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //User profile elements initialization
        headerProfilePictureImageView = (ImageView) findViewById(R.id.profilePicture);
        headerUsernameTextView = (TextView) findViewById(R.id.userName);
        headerEmailTextView = (TextView) findViewById(R.id.userEmail);
        Picasso.with(context).load(user.getProfilePictureURL()).into(headerProfilePictureImageView);
        headerUsernameTextView.setText("Welcome " + user.getUserName());
        headerEmailTextView.setText(user.getEmail());

    }


    private void addRepairmanItem(final Repairman repairman) {
        SmallRepairmanItem smallReapiSmallRepairmanItem = new SmallRepairmanItem(context, repairmanList.getColumnCount(), repairman);
        smallReapiSmallRepairmanItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("REPAIRMAN", repairman);
                DialogFragment largeRepaimanFragment = new LargeRepairmanInfoFragment();
                largeRepaimanFragment.setArguments(bundle);
                largeRepaimanFragment.show(getFragmentManager(),"Large Repairman Fragment");
            }
        });

        repairmanList.addView(smallReapiSmallRepairmanItem);
    }


    private ArrayList<Repairman> setRepairmansToDatabase() {
        ArrayList<Repairman> repairmans = new ArrayList<Repairman>();

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

        //Create dummy repairmans
        ArrayList<Region> rep1reg = new ArrayList<Region>();
        rep1reg.add(regions.get(0));
        rep1reg.add(regions.get(1));
        ArrayList<Profession> rep1prof = new ArrayList<Profession>();
        rep1prof.add(professions.get(0));
        rep1prof.add(professions.get(1));
        Repairman repairman1 = new Repairman("1", "Rep", "1", "rep1@rep.com", "Addr1", "111", "112", "First repairman", "4.7" , rep1reg, rep1prof, "www.rep1.com/url" );

        ArrayList<Region> rep2reg = new ArrayList<Region>();
        rep2reg.add(regions.get(2));
        ArrayList<Profession> rep2prof = new ArrayList<Profession>();
        rep2prof.add(professions.get(0));
        Repairman repairman2 = new Repairman("2", "Rep", "2", "rep2@rep.com", "Addr2", "221", "222", "Second repairman", "3.7" , rep2reg, rep2prof, "www.rep2.com/url" );

        ArrayList<Region> rep3reg = new ArrayList<Region>();
        rep3reg.add(regions.get(2));
        ArrayList<Profession> rep3prof = new ArrayList<Profession>();
        rep3prof.add(professions.get(0));
        rep3prof.add(professions.get(2));
        Repairman repairman3 = new Repairman("3", "Rep", "3", "rep3@rep.com", "Addr3", "331", "332", "Third repairman", "2.7" , rep3reg, rep3prof, "www.rep3.com/url" );

        ArrayList<Region> rep4reg = new ArrayList<Region>();
        rep4reg.add(regions.get(0));
        rep4reg.add(regions.get(1));
        ArrayList<Profession> rep4prof = new ArrayList<Profession>();
        rep4prof.add(professions.get(2));
        rep4prof.add(professions.get(3));
        Repairman repairman4 = new Repairman("4", "Rep", "4", "rep4@rep.com", "Addr4", "441", "442", "Fourth repairman", "1.7" , rep4reg, rep4prof, "www.rep4.com/url" );

        repairmans.add(repairman1);
        repairmans.add(repairman2);
        repairmans.add(repairman3);
        repairmans.add(repairman4);


        repairmanDatabaseReference.child("Repairman1").setValue(repairman1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                } else{
                    Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        repairmanDatabaseReference.child("Repairman2").setValue(repairman1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Log.d("Test", "Do tuka stiga");

                } else{
                    Log.d("Test", "Do tuka ne stiga " + task.getException());

                    Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        repairmanDatabaseReference.child("Repairman3").setValue(repairman1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Log.d("Test", "Do tuka stiga");

                } else{
                    Log.d("Test", "Do tuka ne stiga " + task.getException());

                    Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        repairmanDatabaseReference.child("Repairman4").setValue(repairman1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Log.d("Test", "Do tuka stiga");

                } else{
                    Log.d("Test", "Do tuka ne stiga " + task.getException());

                    Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        return repairmans;

    }

    private void addProfessionToDrawer(Profession profession) {
        MenuItem professionMenuItem = navigationDrawerMenu.add(R.id.category_group, profession.getId(), profession.getCategoryId()+1, profession.getName());
        professionMenuItem.setVisible(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //Create menu in the top right corner (Settings)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //OnClick listener for top right corner (Settings) menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.sign_out){
            signoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //onClick listener for category selection
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Get strings.xml resource
        Resources res = getResources();

        //Set default menu item from the professions - first profession
        MenuItem menuItem = navigationDrawerMenu.findItem(professions.get(0).getId());;


        if (id == R.id.category_construction) {

            for(int i=0; i<professions.size(); i++){
                menuItem = navigationDrawerMenu.findItem(professions.get(i).getId());
                if((menuItem.getOrder() == item.getOrder()+1)&&(!menuItem.isVisible())){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }

            }
            //categoryNavigationView.getMenu().clear();

        } else if (id == R.id.category_appliances) {
            for(int i=0; i<professions.size(); i++){
                menuItem = navigationDrawerMenu.findItem(professions.get(i).getId());
                if((menuItem.getOrder() == item.getOrder()+1)&&(!menuItem.isVisible())){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }

            }
        } else if (id == R.id.category_cars) {
            for(int i=0; i<professions.size(); i++){
                menuItem = navigationDrawerMenu.findItem(professions.get(i).getId());
                if((menuItem.getOrder() == item.getOrder()+1)&&(!menuItem.isVisible())){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }

            }
        } else if (id == R.id.category_food) {
            for(int i=0; i<professions.size(); i++){
                menuItem = navigationDrawerMenu.findItem(professions.get(i).getId());
                if((menuItem.getOrder() == item.getOrder()+1)&&(!menuItem.isVisible())){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }

            }
        } else if (id == R.id.category_technology) {
            for(int i=0; i<professions.size(); i++){
                menuItem = navigationDrawerMenu.findItem(professions.get(i).getId());
                if((menuItem.getOrder() == item.getOrder()+1)&&(!menuItem.isVisible())){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }

            }
        } else if (id == R.id.category_style) {
            for(int i=0; i<professions.size(); i++){
                menuItem = navigationDrawerMenu.findItem(professions.get(i).getId());
                if((menuItem.getOrder() == item.getOrder()+1)&&(!menuItem.isVisible())){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }

            }
        } else if (id == R.id.category_other) {
            for(int i=0; i<professions.size(); i++){
                menuItem = navigationDrawerMenu.findItem(professions.get(i).getId());
                if((menuItem.getOrder() == item.getOrder()+1)&&(!menuItem.isVisible())){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }

            }

        } else if (id == R.id.category_logout){
            auth.signOut();
            // this listener will be called when there is change in firebase user session
            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        // user auth state is changed - user is null
                        // launch login activity
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            };
        } else if(id == R.id.category_user_settings){
            Intent i = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.category_assigned_jobs){
            Intent i = new Intent(MainActivity.this, OpenJobsPerUserActivity.class);
            startActivity(i);
        }
        else {
            setActionBarSubtitle(item.getTitle().toString());
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }



        return true;
    }

    //Setting Action Bar title and subtitle
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setActionBarSubtitle(String subtitle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.support.v7.app.ActionBar ab = getSupportActionBar();
            ab.setTitle("iRepair");
            ab.setSubtitle(subtitle);
        }
    }

    private void signoutUser(){
        auth.signOut();
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
