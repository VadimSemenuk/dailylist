package com.mamindeveloper.dailylist.Enums;

public enum NoteActions {
    ADD(1),
    EDIT(2),
    DELETE(3);

    private final int value;
    private NoteActions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
