package com.example.mynote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository {
    private DatabaseHelper dbHelper;

    public NoteRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 插入笔记
    public long insertNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, note.getTitle());
        values.put(DatabaseHelper.COLUMN_CONTENT, note.getContent());
        values.put(DatabaseHelper.COLUMN_CREATED_TIME, note.getCreatedTime());
        values.put(DatabaseHelper.COLUMN_UPDATED_TIME, note.getUpdatedTime());
        long id = db.insert(DatabaseHelper.TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    // 更新笔记
    public int updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, note.getTitle());
        values.put(DatabaseHelper.COLUMN_CONTENT, note.getContent());
        values.put(DatabaseHelper.COLUMN_UPDATED_TIME, note.getUpdatedTime());
        int rows = db.update(DatabaseHelper.TABLE_NOTES, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return rows;
    }

    // 删除笔记
    public void deleteNote(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NOTES,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // 获取所有笔记（按更新时间倒序）
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NOTES,
                null, null, null, null, null,
                DatabaseHelper.COLUMN_UPDATED_TIME + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT)));
                note.setCreatedTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_TIME)));
                note.setUpdatedTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_TIME)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    // 根据ID获取单条笔记
    public Note getNoteById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NOTES,
                null,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        Note note = null;
        if (cursor.moveToFirst()) {
            note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
            note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)));
            note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT)));
            note.setCreatedTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_TIME)));
            note.setUpdatedTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_TIME)));
        }
        cursor.close();
        db.close();
        return note;
    }
}