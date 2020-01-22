package com.mamindeveloper.dailylist.Repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mamindeveloper.dailylist.App;
import com.mamindeveloper.dailylist.DBHelper;
import com.mamindeveloper.dailylist.Enums.SortDirection;
import com.mamindeveloper.dailylist.Enums.SortTypes;
import com.mamindeveloper.dailylist.NotesList.Note;
import com.mamindeveloper.dailylist.Enums.NoteActions;
import com.mamindeveloper.dailylist.NotesList.NoteContentField;
import com.mamindeveloper.dailylist.NotesList.NoteContentFieldImage;
import com.mamindeveloper.dailylist.NotesList.NoteContentFieldListItem;
import com.mamindeveloper.dailylist.NotesList.NoteContentFieldTextArea;
import com.mamindeveloper.dailylist.NotesList.NoteTypes;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NoteRepository {
    private static final NoteRepository ourInstance = new NoteRepository();

    public static NoteRepository getInstance() {
        return ourInstance;
    }

    private NoteRepository() {
    }

    class SortByAddedTime implements Comparator<Object> {
        SortDirection direction;

        private SortByAddedTime(SortDirection direction) {
            this.direction = direction;
        }

        public int compare(Object a, Object b) {
            if (direction == SortDirection.ASC) {
                return ((Note) a).id - ((Note) b).id;
            } else {
                return ((Note) b).id - ((Note) a).id;
            }
        }
    }

    class SortByNoteTime implements Comparator<Object> {
        SortDirection direction;

        private SortByNoteTime(SortDirection direction) {
            this.direction = direction;
        }

        public int compare(Object a, Object b) {
            Note noteA = (Note) a;
            Note noteB = (Note) b;

            if (noteA.startDateTime == null && noteB.startDateTime == null) {
                return 0;
            }
            if (noteA.startDateTime == null) {
                return 1;
            }
            if (noteB.startDateTime == null) {
                return -1;
            }

            long noteAValue = noteA.startDateTime.getMillis();
            long noteBValue = noteB.startDateTime.getMillis();

            if (direction == SortDirection.ASC) {
                return (int) (noteAValue - noteBValue);
            } else {
                return (int) (noteBValue - noteAValue);
            }
        }
    }

    public ArrayList<Note> _getNotes(NoteTypes type, DateTime dateFilter, String search) {
        ArrayList<Note> notes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ArrayList<NoteContentField> contentFields = new ArrayList<>();

            contentFields.add(new NoteContentFieldTextArea("text area"));
            contentFields.add(new NoteContentFieldListItem("list item" + i, false));
            contentFields.add(new NoteContentFieldListItem("list item 1" + i, true));
            contentFields.add(new NoteContentFieldImage("https://static.boredpanda.com/blog/wp-content/uploads/2019/11/criminal-cat-solitary-confinement-quilty-fb-png__700.jpg"));

            Note note = new Note();
            note.id = i;
            note.colorId = 1;
            note.startDateTime = DateTime.now();
            note.startDateTime = DateTime.now();
            note.isFinished = false;
            note.isNotificationEnabled = false;
            note.title = "Title " + i;
            note.contentFields = contentFields;
            note.lastActionDate = DateTime.now();
            note.lastAction = NoteActions.ADD;

            notes.add(note);
        }

        return notes;
    }

    public ArrayList<Note> getNotes(NoteTypes type, DateTime dateFilter, String search) {
        if (search == "") {
            return new ArrayList<>();
        }

        ArrayList<Note> notes = new ArrayList<>();

        ArrayList<String> filterStatements = new ArrayList<>();
        ArrayList<String> params = new ArrayList<>();

        if (dateFilter != null && search == null) {
            filterStatements.add("date >= ?");
            params.add(String.valueOf(dateFilter.withTimeAtStartOfDay().getMillis()));
            filterStatements.add("date < ?");
            params.add(String.valueOf(dateFilter.plusDays(1).withTimeAtStartOfDay().getMillis()));
        }
        filterStatements.add("type = ?");
        params.add(String.valueOf(type.getValue()));

        String sql = "select id, colorId, date, startDateTime, endDateTime, isFinished, isNotificationEnabled, lastActionDate, lastAction, type, title, contentFields "
                + "from notes"
                + (filterStatements.size() > 0 ? (" where " + TextUtils.join(" AND ", filterStatements) + ";") : ";");

        String[] _params = new String[params.size()];
        _params = params.toArray(_params);
        Cursor cursor = DBHelper.getInstance().getWritableDatabase().rawQuery(sql, _params);

        if(cursor.moveToFirst()){
            do {
                Note note = new Note();

                note.id = cursor.getInt(cursor.getColumnIndex("id"));

                note.colorId = cursor.getInt(cursor.getColumnIndex("colorId"));

                long _date = cursor.getLong(cursor.getColumnIndex("date"));
                note.date = new DateTime(_date);

                if (!cursor.isNull(cursor.getColumnIndex("startDateTime"))) {
                    long _startDateTime = cursor.getLong(cursor.getColumnIndex("startDateTime"));
                    note.startDateTime = new DateTime(_startDateTime);
                }

                if (!cursor.isNull(cursor.getColumnIndex("endDateTime"))) {
                    long _endDateTime = cursor.getLong(cursor.getColumnIndex("endDateTime"));
                    note.endDateTime = new DateTime(_endDateTime);
                }

                note.isFinished = cursor.getInt(cursor.getColumnIndex("isFinished")) == 1 ? true : false;

                note.isNotificationEnabled = cursor.getInt(cursor.getColumnIndex("isNotificationEnabled")) == 1 ? true : false;

                long _lastActionDate = cursor.getLong(cursor.getColumnIndex("lastActionDate"));
                note.lastActionDate = new DateTime(_lastActionDate);

                note.lastAction = NoteActions.valueOf(cursor.getInt(cursor.getColumnIndex("lastAction")));

                note.type = NoteTypes.valueOf(cursor.getInt(cursor.getColumnIndex("type")));

                note.title = cursor.getString(cursor.getColumnIndex("title"));

                String contentFieldsJson = cursor.getString(cursor.getColumnIndex("contentFields"));
                ArrayList<NoteContentField> contentFields = new ArrayList<NoteContentField>();
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                if (contentFieldsJson.length() > 0) {
                    JsonArray array = parser.parse(contentFieldsJson).getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        NoteContentField contentField = null;

                        JsonObject obj = array.get(i).getAsJsonObject();
                        if (obj.get("isChecked") != null) {
                            contentField = gson.fromJson(array.get(i), NoteContentFieldListItem.class);
                        } else if (obj.get("text") != null) {
                            contentField = gson.fromJson(array.get(i), NoteContentFieldTextArea.class);

                        }
                        contentFields.add(contentField);
                    }
                }
                note.contentFields = contentFields;

                if (search != null) {
                    String _search = search.toLowerCase();

                    Boolean isTitleValid = note.title.toLowerCase().contains(_search);

                    Boolean isContentFieldsValid = false;
                    for (int i = 0; i < contentFields.size(); i++) {
                        NoteContentField _contentField = note.contentFields.get(i);

                        if (_contentField instanceof NoteContentFieldListItem) {
                            isContentFieldsValid = ((NoteContentFieldListItem) _contentField).text.toLowerCase().contains(_search);
                        } else if (_contentField instanceof NoteContentFieldTextArea) {
                            isContentFieldsValid = ((NoteContentFieldTextArea) _contentField).text.toLowerCase().contains(_search);
                        }

                        if (isContentFieldsValid) {
                            break;
                        }
                    }

                    if (isTitleValid || isContentFieldsValid) {
                        notes.add(note);
                    }
                } else {
                    notes.add(note);
                }

            }
            while (cursor.moveToNext());
        }
        cursor.close();

        SortTypes sortSetting = SortTypes.valueOf(Integer.parseInt(App.sharedPreferences.getString("sort", "1")));
        if (sortSetting == SortTypes.NOTE_TIME) {
            Collections.sort(notes, new SortByAddedTime(SortDirection.valueOf(Integer.parseInt(App.sharedPreferences.getString("sortDirection", "1")))));
        } else {
            Collections.sort(notes, new SortByNoteTime(SortDirection.valueOf(Integer.parseInt(App.sharedPreferences.getString("sortDirection", "1")))));
        }

        return notes;
    }

    public void addNote(Note note) {
        ContentValues cv = new ContentValues();
        cv.put("colorId", note.colorId);
        cv.put("date", note.date.getMillis());
        if (note.startDateTime != null) {
            cv.put("startDateTime", note.startDateTime.getMillis());
        }
        if (note.endDateTime != null) {
            cv.put("endDateTime", note.endDateTime.getMillis());
        }
        cv.put("isFinished", note.isFinished ? 1 : 0);
        cv.put("isNotificationEnabled", note.isNotificationEnabled ? 1 : 0);
        cv.put("lastActionDate", note.lastActionDate.getMillis());
        cv.put("lastAction", note.lastAction.getValue());
        cv.put("type", note.type.getValue());
        cv.put("title", note.title);

        Gson gson = new Gson();
        String contentFieldsJson = gson.toJson(note.contentFields);
        cv.put("contentFields", contentFieldsJson);

        if (note.id == -1) {
            note.id = (int) DBHelper.getInstance().getWritableDatabase().insert("notes", null, cv);
        } else {
            DBHelper.getInstance().getWritableDatabase().update("notes", cv, "id = ?", new String[] { String.valueOf(note.id) });
        }
    }

    public void deleteNote(Note note) {
        DBHelper.getInstance().getWritableDatabase().delete("notes", "id = " + note.id, null);
    }
}
