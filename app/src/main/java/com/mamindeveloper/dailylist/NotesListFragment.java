package com.mamindeveloper.dailylist;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class NotesListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    ArrayList<Note> notes = new ArrayList<>();

    View view;

    public NotesListFragment() {
    }

    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        notes = NoteRepository.getInstance().getNotes(DateTime.now());

        buildNotesList();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void buildNotesList() {
        LayoutInflater ltInflater = getLayoutInflater();
        LinearLayout notesList = view.findViewById(R.id.notes_list_wrapper);

        for (int i = 0; i < notes.size(); i++) {
            View noteView = ltInflater.inflate(R.layout.note, notesList, false);

            Note note = notes.get(i);

            noteView.findViewById(R.id.note_color).setBackgroundColor(Color.parseColor(note.getColor()));

            String noteTime = "";
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
            String startTimeFormatted = timeFormatter.print(note.startDateTime);
            noteTime += startTimeFormatted;
            if (note.endDateTime != null) {
                String endTimeFormatted = timeFormatter.print(note.endDateTime);
                noteTime += " - " + endTimeFormatted;
            }
            ((TextView) noteView.findViewById(R.id.note_time)).setText(noteTime);

            noteView.findViewById(R.id.note_notification_label).setVisibility(note.isNotificationEnabled ? View.VISIBLE : View.GONE);

            ((TextView) noteView.findViewById(R.id.note_title)).setText(note.title);

            LinearLayout contentFieldsWrapper = noteView.findViewById(R.id.note_content_fields_wrapper);
            for (int a = 0; a < note.contentFields.size(); a++) {
                NoteContentField _contentField = note.contentFields.get(a);

                if (_contentField instanceof NoteContentFieldTextArea) {
                    NoteContentFieldTextArea contentField = (NoteContentFieldTextArea) _contentField;
                    ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView textView = new TextView(getContext());
                    textView.setLayoutParams(linLayoutParam);
                    textView.setText(contentField.text);
                    contentFieldsWrapper.addView(textView, a);
                }
                if (_contentField instanceof NoteContentFieldListItem) {
                    NoteContentFieldListItem contentField = (NoteContentFieldListItem) _contentField;
                    ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    CheckBox checkBox = new CheckBox(getContext());
                    checkBox.setLayoutParams(linLayoutParam);
                    checkBox.setText(contentField.text);
                    checkBox.setChecked(contentField.isChecked);
                    contentFieldsWrapper.addView(checkBox, a);
                }
                if (_contentField instanceof NoteContentFieldImage) {
                    NoteContentFieldImage contentField = (NoteContentFieldImage) _contentField;
                    ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(linLayoutParam);
                    Picasso.get().load(contentField.url).into(imageView);
                    contentFieldsWrapper.addView(imageView, a);
                }
            }

            notesList.addView(noteView);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
