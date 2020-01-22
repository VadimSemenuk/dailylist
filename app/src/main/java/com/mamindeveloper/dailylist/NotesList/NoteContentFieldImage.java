package com.mamindeveloper.dailylist.NotesList;

import java.io.Serializable;

public class NoteContentFieldImage extends NoteContentField implements Serializable {
    public String url;

    public NoteContentFieldImage(String url) {
        this.url = url;
    }
}
