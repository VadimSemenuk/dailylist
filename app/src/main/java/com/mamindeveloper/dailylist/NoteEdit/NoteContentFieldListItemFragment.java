package com.mamindeveloper.dailylist.NoteEdit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mamindeveloper.dailylist.R;

import androidx.fragment.app.Fragment;

public class NoteContentFieldListItemFragment extends Fragment {
    private OnNoteContentFieldListItemFragmentInteraction mListener;

    EditText inputView;
    CheckBox checkboxView;
    ImageButton removeButton;

    public NoteContentFieldListItemFragment() {
    }

    public static NoteContentFieldListItemFragment newInstance() {
        NoteContentFieldListItemFragment fragment = new NoteContentFieldListItemFragment();
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
        checkboxView = view.findViewById(R.id.checkbox);

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
                mListener.onListItemRemove(_this);
            }
        });

        inputView.setFocusableInTouchMode(true);
        inputView.requestFocus();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnNoteContentFieldListItemFragmentInteraction) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getInputValue() {
        return inputView.getText().toString();
    }

    public Boolean getCheckedState() {
        return checkboxView.isChecked();
    }

    public interface OnNoteContentFieldListItemFragmentInteraction {
        void onListItemRemove(Fragment fragment);
    }
}
