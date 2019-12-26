package com.mamindeveloper.dailylist;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class Note {
    public int id;

    public int colorId;
    public DateTime startDateTime;
    public DateTime endDateTime;
    public Boolean isFinished;
    public Boolean isNotificationEnabled;

    public String title;
    ArrayList<NoteContentField> contentFields;

    public Note(int id, int colorId, DateTime startDateTime, DateTime endDateTime, Boolean isFinished, Boolean isNotificationEnabled, String title, ArrayList<NoteContentField> contentFields) {
        this.id = id;
        this.colorId = colorId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isFinished = isFinished;
        this.isNotificationEnabled = isNotificationEnabled;
        this.title = title;
        this.contentFields = contentFields;
    }

    public String getColor() {
        return Constants.NOTE_COLORS.get(this.colorId);
    }
}
