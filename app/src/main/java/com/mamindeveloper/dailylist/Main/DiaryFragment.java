package com.mamindeveloper.dailylist.Main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mamindeveloper.dailylist.NotesList.NoteListFragment;
import com.mamindeveloper.dailylist.NotesList.NoteTypes;
import com.mamindeveloper.dailylist.NotesList.NotesListFragment;
import com.mamindeveloper.dailylist.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.joda.time.DateTime;

public class DiaryFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    ViewPager notesListPager;
    PagerAdapter pagerAdapter;
    NoteListFragment noteListFragment;

    public DiaryFragment() {
        noteListFragment = NoteListFragment.newInstance();
        noteListFragment.mode = NoteTypes.Diary;
        noteListFragment.date = DateTime.now();
        noteListFragment.updateData();
    }

    public static DiaryFragment newInstance() {
        DiaryFragment fragment = new DiaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        notesListPager = view.findViewById(R.id.notes_list_pager);
        pagerAdapter = new NotesListPagerAdapter(getChildFragmentManager());
        notesListPager.setAdapter(pagerAdapter);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        mListener.onFragmentInteraction(uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        pagerAdapter = null;
        notesListPager = null;
    }

    public void updateData() {
        noteListFragment.updateData();
    }

    public void setDate(DateTime date) {
        noteListFragment.setDate(date);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class NotesListPagerAdapter extends FragmentPagerAdapter {

        public NotesListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return noteListFragment;
        }

        @Override
        public int getCount() {
            return 1;
        }

    }
}
