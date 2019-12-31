package com.mamindeveloper.dailylist;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class NoteContentFieldTextAreaFragment extends Fragment {
    private OnNoteContentFieldTextAreaFragmentInteraction mListener;

    EditText inputView;
    ImageButton removeButton;

    public NoteContentFieldTextAreaFragment() {
    }

    public static NoteContentFieldTextAreaFragment newInstance() {
        NoteContentFieldTextAreaFragment fragment = new NoteContentFieldTextAreaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_content_field_list_item, container, false);

        inputView = view.findViewById(R.id.input);
        removeButton = view.findViewById(R.id.remove);

        final Fragment _this = this;

        inputView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                removeButton.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTextAreaRemove(_this);
            }
        });

        inputView.setFocusableInTouchMode(true);
        inputView.requestFocus();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnNoteContentFieldTextAreaFragmentInteraction) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getInputValue() {
        return inputView.getText().toString();
    }

    public interface OnNoteContentFieldTextAreaFragmentInteraction {
        void onTextAreaRemove(Fragment fragment);
    }
}
