package com.mamindeveloper.dailylist.NotesList;

import java.io.Serializable;

public class NoteContentFieldListItem extends NoteContentField implements Serializable {
    public String text;
    public Boolean isChecked;

    public NoteContentFieldListItem(String text, Boolean isChecked) {
        this.text = text;
        this.isChecked = isChecked;
    }
}
