package com.mamindeveloper.dailylist;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Note> mValues;
    private final NoteListFragment.OnListFragmentInteractionListener mListener;

    public NoteRecyclerViewAdapter(ArrayList<Note> items, NoteListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Note note = mValues.get(position);
        holder.mItem = note;

        holder.mColorView.setBackgroundColor(Color.parseColor(note.getColor()));

        String noteTime = "";
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
        String startTimeFormatted = timeFormatter.print(note.startDateTime);
        noteTime += startTimeFormatted;
        if (note.endDateTime != null) {
            String endTimeFormatted = timeFormatter.print(note.endDateTime);
            noteTime += " - " + endTimeFormatted;
        }
        holder.mTimeView.setText(noteTime);

        holder.mNotificationLabelView.setVisibility(note.isNotificationEnabled ? View.VISIBLE : View.GONE);

        holder.mTitleView.setText(note.title);

        holder.mContentFieldsWrapperView.removeAllViews();
        for (int a = 0; a < note.contentFields.size(); a++) {
            NoteContentField _contentField = note.contentFields.get(a);

            if (_contentField instanceof NoteContentFieldTextArea) {
                NoteContentFieldTextArea contentField = (NoteContentFieldTextArea) _contentField;
                ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(holder.mView.getContext());
                textView.setLayoutParams(linLayoutParam);
                textView.setText(contentField.text);
                holder.mContentFieldsWrapperView.addView(textView, a);
            }
            if (_contentField instanceof NoteContentFieldListItem) {
                NoteContentFieldListItem contentField = (NoteContentFieldListItem) _contentField;
                ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CheckBox checkBox = new CheckBox(holder.mView.getContext());
                checkBox.setLayoutParams(linLayoutParam);
                checkBox.setText(contentField.text);
                checkBox.setChecked(contentField.isChecked);
                holder.mContentFieldsWrapperView.addView(checkBox, a);
            }
            if (_contentField instanceof NoteContentFieldImage) {
                NoteContentFieldImage contentField = (NoteContentFieldImage) _contentField;
                ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ImageView imageView = new ImageView(holder.mView.getContext());
                imageView.setLayoutParams(linLayoutParam);
                Picasso.get().load(contentField.url).into(imageView);
                holder.mContentFieldsWrapperView.addView(imageView, a);
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout mColorView;
        public final TextView mTimeView;
        public final ImageView mNotificationLabelView;
        public final TextView mTitleView;
        public final LinearLayout mContentFieldsWrapperView;
        public Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mColorView = (LinearLayout) view.findViewById(R.id.note_color);
            mTimeView = (TextView) view.findViewById(R.id.note_time);
            mNotificationLabelView = (ImageView) view.findViewById(R.id.note_notification_label);
            mTitleView = (TextView) view.findViewById(R.id.note_title);
            mContentFieldsWrapperView = (LinearLayout) view.findViewById(R.id.note_content_fields_wrapper);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
