package com.mamindeveloper.dailylist.Enums;

import java.util.HashMap;
import java.util.Map;

public enum NoteActions {
    ADD(1),
    EDIT(2),
    DELETE(3);

    private final int value;
    private static Map map = new HashMap<>();

    private NoteActions(int value) {
        this.value = value;
    }

    static {
        for (NoteActions noteAction : NoteActions.values()) {
            map.put(noteAction.value, noteAction);
        }
    }

    public static NoteActions valueOf(int noteAction) {
        return (NoteActions) map.get(noteAction);
    }

    public int getValue() {
        return value;
    }
}
