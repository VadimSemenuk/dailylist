package com.mamindeveloper.dailylist;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainActivity extends AppCompatActivity implements NotesListFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    ViewPager notesListPager;
    PagerAdapter pagerAdapter;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_diary);

        notesListPager = findViewById(R.id.notes_list_pager);
        pagerAdapter = new NotesListPagerAdapter(getSupportFragmentManager());
        notesListPager.setAdapter(pagerAdapter);

        MaterialCalendarView mcv = findViewById(R.id.calendar_view);
        mcv.setTopbarVisible(false);
        mcv.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_diary) {
            Toast.makeText(getApplicationContext(), "nav_diary", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_notes) {

        } else if (id == R.id.nav_trash) {

        } else if (id == R.id.nav_settings) {
            Toast.makeText(getApplicationContext(), "nav_settings", Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onFragmentInteraction(Uri uri) {

    }

    private class NotesListPagerAdapter extends FragmentPagerAdapter {

        public NotesListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return NotesListFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 1;
        }

    }
}