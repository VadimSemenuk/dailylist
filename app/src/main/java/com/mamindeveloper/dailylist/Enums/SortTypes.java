package com.mamindeveloper.dailylist.Enums;

import java.util.HashMap;
import java.util.Map;

public enum SortTypes {
    ADDED_TIME(1),
    NOTE_TIME(2);

    private final int value;
    private static Map map = new HashMap<>();

    private SortTypes(int value) {
        this.value = value;
    }

    static {
        for (SortTypes noteAction : SortTypes.values()) {
            map.put(noteAction.value, noteAction);
        }
    }

    public static SortTypes valueOf(int noteAction) {
        return (SortTypes) map.get(noteAction);
    }

    public int getValue() {
        return value;
    }
}
