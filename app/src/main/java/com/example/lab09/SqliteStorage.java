package com.example.lab09;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteStorage extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "student";

    public SqliteStorage(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        "id TEXT PRIMARY KEY, " +
                        "name TEXT, " +
                        "email TEXT, " +
                        "tel TEXT)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_NAME
        );

        onCreate(db);
    }

    public boolean save(Student student) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("id", student.getId());
        values.put("name", student.getName());
        values.put("email", student.getEmail());
        values.put("tel", student.getTel());

        long result =
                db.insert(
                        TABLE_NAME,
                        null,
                        values
                );

        return result != -1;
    }

    public Student load(String id) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                "id=?",
                new String[]{id},
                null,
                null,
                null
        );

        Student student = null;

        if (cursor.moveToFirst()) {

            String studentId =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("id")
                    );

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            String email =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("email")
                    );

            String tel =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("tel")
                    );

            student = new Student(
                    studentId,
                    name,
                    email,
                    tel
            );
        }

        cursor.close();

        return student;
    }

    public boolean exists(String id) {

        Student student = load(id);

        return student != null;
    }
}