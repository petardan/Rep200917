package com.rpd.irepair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    SharedPreferences mPrefs;
    Boolean logedIn = false;
    Context context;

    //Category navigationVIew
    NavigationView categoryNavigationView;
    Menu navigationDrawerMenu;

    ArrayList<Profession> professions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Getting the professions list from Loading.class
        DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra("PROFESSIONS");
        Bundle receive = getIntent().getExtras();
        professions = dw.getParliaments();

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

        //End of Initializing

        checkIfUserIslogged();
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

        final TextView testText = (TextView)findViewById(R.id.testText);
        //Get strings.xml resource
        Resources res = getResources();

        //Set default menu item from the professions - first profession
        MenuItem menuItem = navigationDrawerMenu.findItem(professions.get(0).getId());;


        if (id == R.id.category_construction) {
            testText.setText(res.getString(R.string.category_construction));

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
            testText.setText(res.getString(R.string.category_appliances));
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
            testText.setText(res.getString(R.string.category_cars));
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
            testText.setText(res.getString(R.string.category_food));
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
            testText.setText(res.getString(R.string.catecategory_technology));
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
            testText.setText(res.getString(R.string.category_style));
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
            testText.setText(res.getString(R.string.category_other));
            for(int i=0; i<professions.size(); i++){
                menuItem = navigationDrawerMenu.findItem(professions.get(i).getId());
                if((menuItem.getOrder() == item.getOrder()+1)&&(!menuItem.isVisible())){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }

            }

            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Log.d("Menu Item", menuItem.getTitle().toString() + " " + Integer.valueOf(menuItem.getOrder()).toString());

                    testText.setText(menuItem.getTitle().toString());
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }



        return true;
    }
}
