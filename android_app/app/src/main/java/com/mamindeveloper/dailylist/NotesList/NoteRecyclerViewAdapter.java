package com.mamindeveloper.dailylist.NotesList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mamindeveloper.dailylist.R;
import com.squareup.picasso.Picasso;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter sectionTitleFormatter;

    private final ArrayList<Note> mValues;
    public Boolean showHeader;
    private Boolean isCollapseNotesAllowed;

    private final OnListFragmentInteractionListener mListener;

    public NoteRecyclerViewAdapter(ArrayList<Note> items, OnListFragmentInteractionListener listener, Boolean showHeader, Boolean isCollapseNotesAllowed) {
        mValues = items;
        mListener = listener;
        this.showHeader = showHeader;
        this.isCollapseNotesAllowed = isCollapseNotesAllowed;

        timeFormatter = DateTimeFormat.forPattern("HH:mm");
        sectionTitleFormatter = DateTimeFormat.forPattern("d MMMM, yyyy");
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

        if (note.isNotificationEnabled || note.startDateTime != null || note.endDateTime != null) {
            holder.mNoteMetaInfoView.setVisibility(View.VISIBLE);

            if (note.startDateTime != null || note.endDateTime != null) {
                String noteTime = "";
                String startTimeFormatted = timeFormatter.print(note.startDateTime);
                noteTime += startTimeFormatted;
                if (note.endDateTime != null) {
                    String endTimeFormatted = timeFormatter.print(note.endDateTime);
                    noteTime += " - " + endTimeFormatted;
                }
                holder.mTimeView.setText(noteTime);
                holder.mTimeView.setVisibility(View.VISIBLE);
            } else {
                holder.mTimeView.setVisibility(View.GONE);
            }

            holder.mNotificationLabelView.setVisibility(note.isNotificationEnabled ? View.VISIBLE : View.GONE);
        } else {
            holder.mNoteMetaInfoView.setVisibility(View.GONE);
        }

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

                final int index = a;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mListener.onNoteListItemCheckedStateChange(holder.mItem, index, isChecked);
                    }
                });
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

        if (isCollapseNotesAllowed) {
            holder.cardView.setTag("collapsed");
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.cardView.getLayoutParams();
            layoutParams.height = 180;
            holder.cardView.setLayoutParams(layoutParams);
        } else {
            holder.cardView.setTag("expanded");
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.cardView.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            holder.cardView.setLayoutParams(layoutParams);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollapseNotesAllowed) {
                    if ((String) holder.cardView.getTag() == "expanded") {
                        holder.cardView.setTag("collapsed");
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.cardView.getLayoutParams();
                        layoutParams.height = 180;
                        holder.cardView.setLayoutParams(layoutParams);
                    } else {
                        holder.cardView.setTag("expanded");
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.cardView.getLayoutParams();
                        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        holder.cardView.setLayoutParams(layoutParams);
                    }
                }

                mListener.onNoteClick(holder.mItem);
            }
        });

        if (showHeader && (position == 0 || ! mValues.get(position - 1).date.withTimeAtStartOfDay().isEqual(note.date.withTimeAtStartOfDay()))) {
            holder.mHeaderView.setVisibility(View.VISIBLE);
            holder.mHeaderView.setText(sectionTitleFormatter.print(note.date));
        } else {
            holder.mHeaderView.setVisibility(View.GONE);
        }

        holder.mNoteFinishedCheckBox.setChecked(note.isFinished);

        holder.mNoteFinishedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onNoteFinishedStateChange(holder.mItem, isChecked);
            }
        });

        holder.contextMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onContextMenuRequest(holder.mItem);
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
        public final LinearLayout mNoteMetaInfoView;
        public final TextView mTitleView;
        public final LinearLayout mContentFieldsWrapperView;
        public final TextView mHeaderView;
        public final CheckBox mNoteFinishedCheckBox;
        public final CardView cardView;
        public final ImageButton contextMenuButton;
        public Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mColorView = (LinearLayout) view.findViewById(R.id.note_color);
            mTimeView = (TextView) view.findViewById(R.id.note_time);
            mNotificationLabelView = (ImageView) view.findViewById(R.id.note_notification_label);
            mNoteMetaInfoView = (LinearLayout) view.findViewById(R.id.note_meta_info);
            mTitleView = (TextView) view.findViewById(R.id.note_title);
            mContentFieldsWrapperView = (LinearLayout) view.findViewById(R.id.note_content_fields_wrapper);
            mHeaderView = (TextView) view.findViewById(R.id.title);
            mNoteFinishedCheckBox = (CheckBox) view.findViewById(R.id.note_finished_trigger);
            cardView = (CardView) view.findViewById(R.id.note_card);
            contextMenuButton = (ImageButton) view.findViewById(R.id.note_context_menu);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    public interface OnListFragmentInteractionListener {
        void onNoteClick(Note note);
        void onNoteListItemCheckedStateChange(Note note, int contentFieldIndex, Boolean nextState);
        void onNoteFinishedStateChange(Note note, Boolean nextState);
        void onContextMenuRequest(Note note);
    }
}
