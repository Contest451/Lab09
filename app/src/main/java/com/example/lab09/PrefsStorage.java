package com.example.lab09;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsStorage {

    private SharedPreferences prefs;

    public PrefsStorage(Context context) {

        prefs = context.getSharedPreferences(
                "student_preferences",
                Context.MODE_PRIVATE
        );
    }

    public void save(Student student) {

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("id", student.getId());
        editor.putString("name", student.getName());
        editor.putString("email", student.getEmail());
        editor.putString("tel", student.getTel());

        editor.apply();
    }

    public Student load() {

        String id = prefs.getString("id", "");
        String name = prefs.getString("name", "");
        String email = prefs.getString("email", "");
        String tel = prefs.getString("tel", "");

        if (id.equals("")
                && name.equals("")
                && email.equals("")
                && tel.equals("")) {

            return null;
        }

        return new Student(
                id,
                name,
                email,
                tel
        );
    }
}