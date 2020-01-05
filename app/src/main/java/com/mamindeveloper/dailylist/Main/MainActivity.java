package com.mamindeveloper.dailylist.Main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.mamindeveloper.dailylist.AboutActivity;
import com.mamindeveloper.dailylist.Login.LoginActivity;
import com.mamindeveloper.dailylist.NoteEdit.NoteEditActivity;
import com.mamindeveloper.dailylist.NotesList.NoteListFragment;
import com.mamindeveloper.dailylist.NotesList.NotesListFragment;
import com.mamindeveloper.dailylist.ProfileActivity;
import com.mamindeveloper.dailylist.R;
import com.mamindeveloper.dailylist.Repositories.AuthRepository;
import com.mamindeveloper.dailylist.SettingsActivity;

public class MainActivity
        extends AppCompatActivity
        implements NotesListFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener {

    DrawerLayout drawer;
    DiaryFragment diaryFragment;
    NoteListFragment noteListFragment;
    Menu menu;
    NavigationView navigationView;

    MainFragments activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_diary);
        activeFragment = MainFragments.Diary;

        diaryFragment = DiaryFragment.newInstance();
        noteListFragment = NoteListFragment.newInstance();

        setDiaryFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu = menu;

        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getResources().getString(R.string.app_bar_main_search_hint));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setBackgroundResource(android.R.color.transparent);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                setMenuOptionsVisibility(searchItem, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                setMenuOptionsVisibility(searchItem, true);
                return true;
            }
        });

        setToolbarMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_main_calendar) {
            diaryFragment.triggerCalendarMode();
        } else if (id == R.id.app_bar_main_add) {
            Intent intent = new Intent(this, NoteEditActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_diary) {
            setDiaryFragment();
        } else if (id == R.id.nav_notes) {
            setNoteListFragment();
        } else if (id == R.id.nav_trash) {
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            if (AuthRepository.getInstance().hasToken()) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void setDiaryFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, diaryFragment).commit();

        activeFragment = MainFragments.Diary;

        setToolbarMenu();
    }

    private void setNoteListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, noteListFragment).commit();

        activeFragment = MainFragments.Notes;

        setToolbarMenu();
    }

    private void setToolbarMenu() {
        if (menu == null) {
            return;
        }

        MenuItem calendarItem = menu.findItem(R.id.app_bar_main_calendar);

        if (activeFragment == MainFragments.Diary) {
            calendarItem.setVisible(true);
        } else if (activeFragment == MainFragments.Notes) {
            calendarItem.setVisible(false);
        } else if (activeFragment == MainFragments.Trash) {
            calendarItem.setVisible(false);
        }
    }

    private void setMenuOptionsVisibility(MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item != exception)  {
                item.setVisible(visible);
            }
        }
    }

    public void onFragmentInteraction(Uri uri) {

    }
}