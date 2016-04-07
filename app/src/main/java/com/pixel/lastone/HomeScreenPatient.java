package com.pixel.lastone;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeScreenPatient extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String admNoImportedString, JSON_STRING;
    SaveSharedPreference saveSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SaveSharedPreference.getUserName(HomeScreenPatient.this).length() == 0) {
            Intent intent = new Intent(this, loginAsPatient.class);
            startActivity(intent);
        } else {
            admNoImportedString = SaveSharedPreference.getUserName(HomeScreenPatient.this).toString();
            Message.message(this, admNoImportedString);
        }

        saveSharedPreference = new SaveSharedPreference();
        //admNoImported = getIntent().getExtras();
        //admNoImportedString = admNoImported.getString("admissionNo");
        //Message.message(this, admNoImportedString);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView navHeaderName = (TextView) headerLayout.findViewById(R.id.nav_header_name);
        TextView navHeaderAdmissionNo = (TextView) headerLayout.findViewById(R.id.nav_header_admission_no);
        navHeaderAdmissionNo.setText(admNoImportedString);

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_profile) {
            fragmentClass = FragmentProfile.class;
            FragmentProfile pFragment = new FragmentProfile();
            pFragment.setAdmissionNo(admNoImportedString);
            // Bundle bundle = new Bundle();
            //bundle.putString("AdmissionNo", admNoImportedString);
            //FragmentProfile fragmentProfileObj = new FragmentProfile();
            //fragmentProfileObj.setArguments(bundle);

        } else if (id == R.id.nav_edit_profile) {
            fragmentClass = FragmentEditProfile.class;

        } else if (id == R.id.nav_log_out) {
            saveSharedPreference.setUserName(this, "");
            Intent intent = new Intent(this, HomeScreenPatient.class);
            startActivity(intent);
        }


        if (fragmentClass != null) {

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();

            item.setChecked(true);
            setTitle(item.getTitle());


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
