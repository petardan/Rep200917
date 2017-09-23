package com.rpd.irepair;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.rpd.customViews.smallRepairmanItem;
import com.rpd.datawrappers.DataWrapperProfessions;
import com.rpd.datawrappers.DataWrapperRegions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    SharedPreferences mPrefs;
    Boolean logedIn = false;
    Context context;

    //Category navigationVIew
    NavigationView categoryNavigationView;
    Menu navigationDrawerMenu;

    //Define list of professions, regions and repairmans, for testing purpose
    ArrayList<Profession> professions;
    ArrayList<Region> regions;
    ArrayList<Repairman> repairmans;

    //Grid layout for the repairman list
    GridLayout repairmanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Getting the professions list from Loading.class
        DataWrapperProfessions dw = (DataWrapperProfessions) getIntent().getSerializableExtra("PROFESSIONS");
        DataWrapperRegions dwReg = (DataWrapperRegions) getIntent().getSerializableExtra("REGIONS");

        Bundle receive = getIntent().getExtras();
        professions = dw.getParliaments();
        regions = dwReg.getParliaments();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Start of Initializing

        context = this;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        //Get sharedPrefs values
        logedIn = mPrefs.getBoolean("logedIn", false);

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

        addAllProfessionsToDrawer(professions);
        categoryNavigationView.inflateMenu(R.menu.categories);

        categoryNavigationView.setNavigationItemSelectedListener(this);

        repairmanList = (GridLayout)findViewById(R.id.repairmen_list);
        //End of Initializing


        repairmanList.addView(new smallRepairmanItem(this, repairmanList.getColumnCount(), "Pecko Peckovski", "trt mrt", 4.7, "Sutrak"));
        repairmanList.addView(new smallRepairmanItem(this, repairmanList.getColumnCount(), "Pecko Peckovski", "trt mrt", 4.7, "Sutrak"));
        repairmanList.addView(new smallRepairmanItem(this, repairmanList.getColumnCount(), "Pecko Peckovski", "trt mrt", 4.7, "Sutrak"));
        repairmanList.addView(new smallRepairmanItem(this, repairmanList.getColumnCount(), "Pecko Peckovski", "trt mrt", 4.7, "Sutrak"));
        repairmanList.addView(new smallRepairmanItem(this, repairmanList.getColumnCount(), "Pecko Peckovski", "trt mrt", 4.7, "Sutrak"));
        repairmanList.addView(new smallRepairmanItem(this, repairmanList.getColumnCount(), "Pecko Peckovski", "trt mrt", 4.7, "Sutrak"));


        repairmans = getRepairmans();

        checkIfUserIslogged();
    }

    private ArrayList<Repairman> getRepairmans() {
        ArrayList<Repairman> repairmans = new ArrayList<Repairman>();

        ArrayList<Region> rep1reg = new ArrayList<Region>();
        rep1reg.add(regions.get(0));
        rep1reg.add(regions.get(1));
        ArrayList<Profession> rep1prof = new ArrayList<Profession>();
        rep1prof.add(professions.get(0));
        rep1prof.add(professions.get(1));
        Repairman repairman1 = new Repairman(1, "Rep", "1", "rep1@rep.com", "Addr1", "111", "112", "First repairman", 4.7 , rep1reg, rep1prof, "www.rep1.com/url" );

        ArrayList<Region> rep2reg = new ArrayList<Region>();
        rep2reg.add(regions.get(2));
        ArrayList<Profession> rep2prof = new ArrayList<Profession>();
        rep2prof.add(professions.get(0));
        Repairman repairman2 = new Repairman(2, "Rep", "2", "rep2@rep.com", "Addr2", "221", "222", "Second repairman", 3.7 , rep2reg, rep2prof, "www.rep2.com/url" );

        ArrayList<Region> rep3reg = new ArrayList<Region>();
        rep3reg.add(regions.get(2));
        ArrayList<Profession> rep3prof = new ArrayList<Profession>();
        rep3prof.add(professions.get(0));
        rep3prof.add(professions.get(2));
        Repairman repairman3 = new Repairman(3, "Rep", "3", "rep3@rep.com", "Addr3", "331", "332", "Third repairman", 2.7 , rep3reg, rep3prof, "www.rep3.com/url" );

        ArrayList<Region> rep4reg = new ArrayList<Region>();
        rep4reg.add(regions.get(0));
        rep4reg.add(regions.get(1));
        ArrayList<Profession> rep4prof = new ArrayList<Profession>();
        rep4prof.add(professions.get(2));
        rep4prof.add(professions.get(3));
        Repairman repairman4 = new Repairman(4, "Rep", "4", "rep4@rep.com", "Addr4", "441", "442", "Fourth repairman", 1.7 , rep4reg, rep4prof, "www.rep4.com/url" );

        repairmans.add(repairman1);
        repairmans.add(repairman2);
        repairmans.add(repairman3);
        repairmans.add(repairman4);


        return repairmans;

    }

    private void addAllProfessionsToDrawer(ArrayList<Profession> professions) {


        for(int i=0; i<professions.size(); i++){
            navigationDrawerMenu.add(R.id.category_group, professions.get(i).getId(), professions.get(i).getCategoryId()+1, professions.get(i).getName().toString());
            MenuItem menuItem = navigationDrawerMenu.getItem(i);
            menuItem.setVisible(false);
        }

    }

    private void checkIfUserIslogged() {
        if(logedIn){
        }
        else{
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
        }

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

        } else {
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
}
