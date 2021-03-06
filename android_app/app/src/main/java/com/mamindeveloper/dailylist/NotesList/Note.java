package com.mamindeveloper.dailylist.NotesList;

import com.mamindeveloper.dailylist.Constants;
import com.mamindeveloper.dailylist.Enums.NoteActions;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class Note implements Serializable {
    public int id;

    public int colorId;
    public DateTime date;
    public DateTime startDateTime;
    public DateTime endDateTime;
    public Boolean isFinished;
    public Boolean isNotificationEnabled;
    public DateTime lastActionDate;
    public NoteActions lastAction;
    public NoteTypes type;

    public String title;
    public ArrayList<NoteContentField> contentFields;

    public Note() {}

    public String getColor() {
        return Constants.NOTE_COLORS.get(this.colorId);
    }
}
