package com.mamindeveloper.dailylist;

public class NoteContentFieldListItem extends NoteContentField {
    public String text;
    public Boolean isChecked;

    public NoteContentFieldListItem(String text, Boolean isChecked) {
        this.text = text;
        this.isChecked = isChecked;
    }
}
