package com.example.lab09;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText edtId;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtTel;

    private Button btnPrefsSave;
    private Button btnPrefsLoad;

    private Button btnSqlSave;
    private Button btnSqlLoad;

    private PrefsStorage prefsStorage;
    private SqliteStorage sqliteStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        edtId = findViewById(R.id.edtId);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtTel = findViewById(R.id.edtTel);

        btnPrefsSave =
                findViewById(R.id.btnPrefsSave);

        btnPrefsLoad =
                findViewById(R.id.btnPrefsLoad);

        btnSqlSave =
                findViewById(R.id.btnSqlSave);

        btnSqlLoad =
                findViewById(R.id.btnSqlLoad);


        prefsStorage =
                new PrefsStorage(this);

        sqliteStorage =
                new SqliteStorage(this);


        btnPrefsSave.setOnClickListener(
                v -> savePrefs()
        );

        btnPrefsLoad.setOnClickListener(
                v -> loadPrefs()
        );

        btnSqlSave.setOnClickListener(
                v -> saveSqlite()
        );

        btnSqlLoad.setOnClickListener(
                v -> loadSqlite()
        );
    }


    private Student getStudentFromForm() {

        String id =
                edtId.getText()
                        .toString()
                        .trim();

        String name =
                edtName.getText()
                        .toString()
                        .trim();

        String email =
                edtEmail.getText()
                        .toString()
                        .trim();

        String tel =
                edtTel.getText()
                        .toString()
                        .trim();


        if (id.isEmpty()
                || name.isEmpty()
                || email.isEmpty()
                || tel.isEmpty()) {

            Toast.makeText(
                    this,
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT
            ).show();

            return null;
        }


        return new Student(
                id,
                name,
                email,
                tel
        );
    }


    private void showStudent(Student student) {

        edtId.setText(student.getId());

        edtName.setText(student.getName());

        edtEmail.setText(student.getEmail());

        edtTel.setText(student.getTel());
    }


    // =========================
    // SharedPreferences
    // =========================

    private void savePrefs() {

        Student student =
                getStudentFromForm();

        if (student == null) {
            return;
        }

        prefsStorage.save(student);

        Toast.makeText(
                this,
                "Đã lưu vào SharedPreferences",
                Toast.LENGTH_SHORT
        ).show();
    }


    private void loadPrefs() {

        Student student =
                prefsStorage.load();

        if (student == null) {

            Toast.makeText(
                    this,
                    "Chưa có dữ liệu SharedPreferences",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        showStudent(student);

        Toast.makeText(
                this,
                "Đã load từ SharedPreferences",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================
    // SQLite
    // =========================

    private void saveSqlite() {

        Student student =
                getStudentFromForm();

        if (student == null) {
            return;
        }


        if (sqliteStorage.exists(
                student.getId())) {

            Toast.makeText(
                    this,
                    "ID đã tồn tại trong SQLite",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        boolean result =
                sqliteStorage.save(student);


        if (result) {

            Toast.makeText(
                    this,
                    "Đã lưu vào SQLite",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Lưu SQLite thất bại",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    private void loadSqlite() {

        final EditText input = new EditText(this);

        input.setHint("ID");

        android.app.AlertDialog dialog =
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Load Student from SQLite")
                        .setMessage("Nhập ID của Student cần load")
                        .setView(input)
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .setPositiveButton(
                                "Load",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                new android.content.DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(
                            android.content.DialogInterface dialogInterface) {

                        dialog.getButton(
                                android.app.AlertDialog.BUTTON_POSITIVE
                        ).setOnClickListener(

                                new android.view.View.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            android.view.View view) {

                                        String id =
                                                input.getText()
                                                        .toString()
                                                        .trim();

                                        if (id.isEmpty()) {

                                            input.setError(
                                                    "Vui lòng nhập ID"
                                            );

                                            return;
                                        }

                                        Student student =
                                                sqliteStorage.load(id);

                                        if (student == null) {

                                            input.setError(
                                                    "Không tìm thấy Student này"
                                            );

                                            return;
                                        }

                                        showStudent(student);

                                        Toast.makeText(
                                                MainActivity.this,
                                                "Đã load từ SQLite",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        dialog.dismiss();
                                    }
                                }
                        );
                    }
                }
        );

        dialog.show();
    }
}