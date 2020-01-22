package com.mamindeveloper.dailylist.NotesList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mamindeveloper.dailylist.Main.MainActivity;
import com.mamindeveloper.dailylist.R;
import com.mamindeveloper.dailylist.Repositories.NoteRepository;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class NoteListFragment extends Fragment implements NoteRecyclerViewAdapter.OnListFragmentInteractionListener {
    ArrayList<Note> notes = new ArrayList<>();

    public NoteTypes mode;
    public DateTime date;
    public String search;

    RecyclerView recyclerView;
    TextView emptyListView;
    NoteRecyclerViewAdapter noteRecyclerViewAdapter;

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

        Context context = view.getContext();

        noteRecyclerViewAdapter = new NoteRecyclerViewAdapter(notes, this, false, true);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(noteRecyclerViewAdapter);

        emptyListView = (TextView) view.findViewById(R.id.empty_list);

        setListView();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateData() {
        ArrayList<Note> nextNotes = NoteRepository.getInstance().getNotes(mode, date, search);
        notes.clear();
        notes.addAll(nextNotes);
        if (noteRecyclerViewAdapter != null) {
            noteRecyclerViewAdapter.notifyDataSetChanged();
        }

        setListView();
    }

    private void setListView() {
        if (emptyListView != null && recyclerView != null) {
            if (notes.isEmpty()) {
                emptyListView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyListView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setDate(DateTime date) {
        this.date = date;
        updateData();
    }

    public void onNoteClick(Note note) {
    }

    public void onNoteListItemCheckedStateChange(Note note, int contentFieldIndex, Boolean nextState) {
        ((NoteContentFieldListItem) note.contentFields.get(contentFieldIndex)).isChecked = nextState;
        NoteRepository.getInstance().addNote(note);
    }

    public void onNoteFinishedStateChange(Note note, Boolean nextState) {
        note.isFinished = nextState;
        NoteRepository.getInstance().addNote(note);
    }

    public void onContextMenuRequest(final Note note) {
        final String[] mCatsName = {getString(R.string.edit_note), getString(R.string.remove_note)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(mCatsName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {

                } else if (item == 1) {
                    NoteRepository.getInstance().deleteNote(note);
                    updateData();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
