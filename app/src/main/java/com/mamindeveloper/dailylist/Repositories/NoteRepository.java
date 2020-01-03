package com.mamindeveloper.dailylist.Repositories;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

    public ArrayList<Note> getNotes(NoteTypes type, DateTime dateFilter, String search) {
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

    public ArrayList<Note> _getNotes(DateTime dateFilter) {
        ArrayList<Note> notes = new ArrayList<>();

        String sql = "select id, color_id, start_date_time, end_date_time, title, content_fields " +
                "from note " +
                "where start_date_time >= ? AND start_date_time < ?";


        String[] params = new String[] { String.valueOf(dateFilter.withTimeAtStartOfDay().getMillis()), String.valueOf(dateFilter.plusDays(1).withTimeAtStartOfDay().getMillis()) };

        Cursor cursor = DBHelper.getInstance().getWritableDatabase().rawQuery(sql, params);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("id"));

                int colorId = cursor.getInt(cursor.getColumnIndex("color_id"));

                long _startDateTime = cursor.getLong(cursor.getColumnIndex("start_date_time"));
                DateTime startDateTime = new DateTime(_startDateTime);
                long _endDateTime = cursor.getLong(cursor.getColumnIndex("end_date_time"));
                DateTime endDateTime = new DateTime(_endDateTime);

                String title = cursor.getString(cursor.getColumnIndex("title"));

                String contentFieldsJson = cursor.getString(cursor.getColumnIndex("content_fields"));
                ArrayList<NoteContentField> contentFields = new ArrayList<NoteContentField>();
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                if (contentFieldsJson.length() > 0) {
                    JsonArray array = parser.parse(contentFieldsJson).getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        NoteContentField contentField = gson.fromJson(array.get(i), NoteContentField.class);
                        contentFields.add(contentField);
                    }
                }

                Note note = new Note();
                note.id = id;
                note.colorId = colorId;
                note.startDateTime = startDateTime;
                note.startDateTime = endDateTime;
                note.isFinished = false;
                note.isNotificationEnabled = false;
                note.title = title;
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
        cv.put("start_date_time", note.startDateTime.getMillis());
        cv.put("end_date_time", note.endDateTime.getMillis());
        cv.put("title", note.title);

        Gson gson = new Gson();
        String contentFieldsJson = gson.toJson(note.contentFields);
        cv.put("content_fields", contentFieldsJson);

        note.id = (int) DBHelper.getInstance().getWritableDatabase().insert("note", null, cv);
    }
}
