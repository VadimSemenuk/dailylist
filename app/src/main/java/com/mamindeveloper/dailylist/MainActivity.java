package com.mamindeveloper.dailylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements NotesListFragment.OnFragmentInteractionListener {

    ViewPager notesListPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesListPager = findViewById(R.id.notes_list_pager);
        pagerAdapter = new NotesListPagerAdapter(getSupportFragmentManager());
        notesListPager.setAdapter(pagerAdapter);
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