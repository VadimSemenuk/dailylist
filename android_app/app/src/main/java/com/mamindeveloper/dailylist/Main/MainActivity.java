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
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.mamindeveloper.dailylist.AboutActivity;
import com.mamindeveloper.dailylist.Login.LoginActivity;
import com.mamindeveloper.dailylist.NoteEdit.NoteEditActivity;
import com.mamindeveloper.dailylist.NotesList.NoteListFragment;
import com.mamindeveloper.dailylist.NotesList.NoteTypes;
import com.mamindeveloper.dailylist.NotesList.NotesListFragment;
import com.mamindeveloper.dailylist.ProfileActivity;
import com.mamindeveloper.dailylist.R;
import com.mamindeveloper.dailylist.Repositories.AuthRepository;
import com.mamindeveloper.dailylist.SettingsActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.DateTime;

public class MainActivity
        extends AppCompatActivity
        implements NotesListFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener,
        OnDateSelectedListener {

    DrawerLayout drawer;
    DiaryFragment diaryFragment;
    NoteListFragment noteListFragment;
    Menu menu;
    NavigationView navigationView;
    MaterialCalendarView calendarView;
    LinearLayout calendarWrapperView;

    MainFragments activeFragment;
    Boolean isSearchMode = false;

    int NOTE_EDIT_REQUEST_CODE = 0;
    int SETTINGS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        calendarWrapperView = findViewById(R.id.calendar_wrapper);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_diary);

        diaryFragment = DiaryFragment.newInstance();
        noteListFragment = NoteListFragment.newInstance();
        noteListFragment.mode = NoteTypes.Note;
        noteListFragment.updateData();

        calendarView = findViewById(R.id.calendar_view);
        calendarView.setTopbarVisible(false);
        calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
        calendarView.setDateSelected(CalendarDay.today(), true);
        calendarView.setOnDateChangedListener(this);

        findViewById(R.id.move_to_today).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setDateSelected(calendarView.getSelectedDate(), false);
                calendarView.setDateSelected(CalendarDay.today(), true);
                calendarView.setCurrentDate(CalendarDay.today(), true);
                onDateSelected(calendarView, CalendarDay.today(), true);
            }
        });

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
                isSearchMode = true;
                setToolbarMenu();

                if (activeFragment == MainFragments.Diary) {
                    diaryFragment.setSearch("");
                } else if (activeFragment == MainFragments.Notes) {
                    noteListFragment.setSearch("");
                }

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                isSearchMode = false;
                setToolbarMenu();

                if (activeFragment == MainFragments.Diary) {
                    diaryFragment.setSearch(null);
                } else if (activeFragment == MainFragments.Notes) {
                    noteListFragment.setSearch(null);
                }

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
            CalendarMode nextMode;
            if (calendarView.getCalendarMode() == CalendarMode.MONTHS) {
                nextMode = CalendarMode.WEEKS;
            } else {
                nextMode = CalendarMode.MONTHS;
            }

            calendarView.state().edit()
                    .setCalendarDisplayMode(nextMode)
                    .commit();

        } else if (id == R.id.app_bar_main_add) {
            Intent intent = new Intent(this, NoteEditActivity.class);

            CalendarDay calendarDay = calendarView.getSelectedDate();
            DateTime currentDate = new DateTime().withDate(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay());
            intent.putExtra("date", currentDate);

            NoteTypes noteType = NoteTypes.Diary;
            if (activeFragment == MainFragments.Notes) {
                noteType = NoteTypes.Note;
            }
            intent.putExtra("noteType", noteType.getValue());

            startActivityForResult(intent, NOTE_EDIT_REQUEST_CODE);
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
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
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
        if (activeFragment == MainFragments.Diary) {
            diaryFragment.setSearch(query);
        } else if (activeFragment == MainFragments.Notes) {
            noteListFragment.setSearch(query);
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onDateSelected(MaterialCalendarView calendarView, CalendarDay calendarDay, boolean selected) {
        DateTime date = new DateTime().withDate(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay());
        diaryFragment.setDate(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        if (requestCode == NOTE_EDIT_REQUEST_CODE) {
            if (activeFragment == MainFragments.Diary) {
                diaryFragment.updateData();
            } else if (activeFragment == MainFragments.Notes) {
                noteListFragment.updateData();
            } else if (activeFragment == MainFragments.Trash) {
            }
        }

        if (requestCode == SETTINGS_REQUEST_CODE) {
            diaryFragment.updateData();
            noteListFragment.updateData();
        }
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

        if (isSearchMode) {
            setMenuOptionsVisibility(menu.findItem(R.id.app_bar_search), false);
            calendarWrapperView.setVisibility(View.GONE);
            return;
        } else {
            setMenuOptionsVisibility(menu.findItem(R.id.app_bar_search), true);
        }

        MenuItem calendarItem = menu.findItem(R.id.app_bar_main_calendar);

        if (activeFragment == MainFragments.Diary) {
            calendarItem.setVisible(true);
            calendarWrapperView.setVisibility(View.VISIBLE);
        } else if (activeFragment == MainFragments.Notes) {
            calendarItem.setVisible(false);
            calendarWrapperView.setVisibility(View.GONE);
        } else if (activeFragment == MainFragments.Trash) {
            calendarItem.setVisible(false);
            calendarWrapperView.setVisibility(View.GONE);
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