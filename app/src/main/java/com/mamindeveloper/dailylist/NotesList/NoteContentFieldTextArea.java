package com.mamindeveloper.dailylist.NotesList;

import java.io.Serializable;

public class NoteContentFieldTextArea extends NoteContentField implements Serializable {
    public String text;

    public NoteContentFieldTextArea(String text) {
        this.text = text;
    }
}
