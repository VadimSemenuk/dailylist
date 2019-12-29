package com.mamindeveloper.dailylist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class DiaryFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    ViewPager notesListPager;
    PagerAdapter pagerAdapter;
    MaterialCalendarView calendarView;

    public DiaryFragment() {
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

        calendarView = view.findViewById(R.id.calendar_view);
        calendarView.setTopbarVisible(false);
        calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();

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

    public void triggerCalendarMode() {
        CalendarMode nextMode;
        if (calendarView.getCalendarMode() == CalendarMode.MONTHS) {
            nextMode = CalendarMode.WEEKS;
        } else {
            nextMode = CalendarMode.MONTHS;
        }

        calendarView.state().edit()
                .setCalendarDisplayMode(nextMode)
                .commit();
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
            return NotesListFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 1;
        }

    }
}
