package com.mamindeveloper.dailylist.NotesList;

public enum NoteTypes {
    Diary(1),
    Note(2);

    private final int value;
    private NoteTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
