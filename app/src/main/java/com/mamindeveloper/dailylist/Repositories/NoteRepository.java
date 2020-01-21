package com.mamindeveloper.dailylist.Repositories;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mamindeveloper.dailylist.DBHelper;
import com.mamindeveloper.dailylist.NotesList.Note;
import com.mamindeveloper.dailylist.Enums.NoteActions;
import com.mamindeveloper.dailylist.NotesList.NoteContentField;
import com.mamindeveloper.dailylist.NotesList.NoteContentFieldImage;
import com.mamindeveloper.dailylist.NotesList.NoteContentFieldListItem;
import com.mamindeveloper.dailylist.NotesList.NoteContentFieldTextArea;
import com.mamindeveloper.dailylist.NotesList.NoteTypes;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class NoteRepository {
    private static final NoteRepository ourInstance = new NoteRepository();

    public static NoteRepository getInstance() {
        return ourInstance;
    }

    private NoteRepository() {
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
        ArrayList<Note> notes = new ArrayList<>();

        String sql = "select id, colorId, startDateTime, endDateTime, isFinished, isNotificationEnabled, lastActionDate, lastAction, type, title, contentFields " +
                "from notes;";
//        + "where start_date_time >= ? AND start_date_time < ?";

        String[] params = new String[] { String.valueOf(dateFilter.withTimeAtStartOfDay().getMillis()), String.valueOf(dateFilter.plusDays(1).withTimeAtStartOfDay().getMillis()) };

//        Cursor cursor = DBHelper.getInstance().getWritableDatabase().rawQuery(sql, params);
        Cursor cursor = DBHelper.getInstance().getWritableDatabase().rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Note note = new Note();

                note.id = cursor.getInt(cursor.getColumnIndex("id"));

                note.colorId = cursor.getInt(cursor.getColumnIndex("colorId"));

                long _startDateTime = cursor.getLong(cursor.getColumnIndex("startDateTime"));
                note.startDateTime = new DateTime(_startDateTime);

                long _endDateTime = cursor.getLong(cursor.getColumnIndex("endDateTime"));
                note.endDateTime = new DateTime(_endDateTime);

                note.isFinished = cursor.getInt(cursor.getColumnIndex("isFinished")) == 1 ? true : false;

                note.isNotificationEnabled = cursor.getInt(cursor.getColumnIndex("isNotificationEnabled")) == 1 ? true : false;

                long _lastActionDate = cursor.getLong(cursor.getColumnIndex("lastActionDate"));
                note.lastActionDate = new DateTime(_lastActionDate);

                note.lastAction = NoteActions.values()[cursor.getInt(cursor.getColumnIndex("lastAction"))];

                note.type = NoteTypes.values()[cursor.getInt(cursor.getColumnIndex("type"))];

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

                notes.add(note);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return notes;
    }

    public void addNote(Note note) {
        ContentValues cv = new ContentValues();
        cv.put("id", note.id);
        cv.put("colorId", note.colorId);
        cv.put("startDateTime", note.startDateTime.getMillis());
        cv.put("endDateTime", note.endDateTime.getMillis());
        cv.put("isFinished", note.isFinished ? 1 : 0);
        cv.put("isNotificationEnabled", note.isNotificationEnabled ? 1 : 0);
        cv.put("lastActionDate", note.lastActionDate.getMillis());
        cv.put("lastAction", note.lastAction.getValue());
        cv.put("type", note.type.getValue());
        cv.put("title", note.title);

        Gson gson = new Gson();
        String contentFieldsJson = gson.toJson(note.contentFields);
        cv.put("contentFields", contentFieldsJson);

        note.id = (int) DBHelper.getInstance().getWritableDatabase().insert("notes", null, cv);
    }
}
