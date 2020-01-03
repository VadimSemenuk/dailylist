package com.mamindeveloper.dailylist.NotesList;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mamindeveloper.dailylist.R;
import com.mamindeveloper.dailylist.Repositories.NoteRepository;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class NoteListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    ArrayList<Note> notes = new ArrayList<>();

    public NoteListFragment() {
    }

    @SuppressWarnings("unused")
    public static NoteListFragment newInstance() {
        NoteListFragment fragment = new NoteListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        notes = NoteRepository.getInstance().getNotes(NoteTypes.Note, null, null);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new NoteRecyclerViewAdapter(notes, mListener, false));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mListener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Note item);
    }
}
