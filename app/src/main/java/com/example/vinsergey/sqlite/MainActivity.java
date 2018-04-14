package com.example.vinsergey.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<User> users;
    EditText eName, eEmail;
    String name, email;
    AlertDialog.Builder ad;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    final String LOG = "mLog";
    RecyclerView rv;
    LinearLayoutManager llm;
    RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eName = findViewById(R.id.txtName);
        eEmail = findViewById(R.id.txtEmail);
        dbHelper = new DBHelper(this);
        users = new ArrayList<>();
        readData();
        rv = findViewById(R.id.rv);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        adapter = new RVAdapter(users);
        rv.setAdapter(adapter);
    }

    public void addData(View view) {
        name = eName.getText().toString();
        email = eEmail.getText().toString();

        database = dbHelper.getWritableDatabase();
        contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_NAME, name);
        contentValues.put(DBHelper.KEY_EMAIL, email);

        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

        eName.setText("");
        eEmail.setText("");

        dbHelper.close();
        readData();
        rv.getAdapter().notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    public void readData() {
        users.clear();
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int emailIndex = cursor.getColumnIndex(DBHelper.KEY_EMAIL);

            int id;
            String name, email;

            do {
                id = cursor.getInt(idIndex);
                name = cursor.getString(nameIndex);
                email = cursor.getString(emailIndex);
                users.add(new User(id, name, email));
            } while (cursor.moveToNext());
        } else {
            Log.d(LOG, "no rows");
        }
        cursor.close();
        dbHelper.close();
    }

    public void deleteUser(String idUser) {
        database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "=" + idUser, null);
        dbHelper.close();
        readData();
    }

    public void updateUser(View view) {
        String id = "1";
        name = "Sasha";
        email = "sasha@mail.ru";
        contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, name);
        contentValues.put(DBHelper.KEY_EMAIL, email);
        database = dbHelper.getWritableDatabase();
        database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_ID + "= ?", new String[] {id});
        dbHelper.close();
        readData();
    }

    public void clearData(View view) {

        String title = "WARNING!";
        String message = "Delete database?";
        String button1String = "Yes";
        String button2String = "No";

        ad = new AlertDialog.Builder(this);
        ad.setTitle(title);  // заголовок
        ad.setMessage(message); // сообщение

        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database = dbHelper.getWritableDatabase();
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                dbHelper.close();
                users.clear();
                rv.getAdapter().notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Database destroyed!",
                        Toast.LENGTH_LONG).show();
            }
        });

        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Фух, пронесло!", Toast.LENGTH_LONG)
                        .show();
            }
        });

        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(getApplicationContext(), "Вы ничего не выбрали",
                        Toast.LENGTH_LONG).show();
            }
        });

        ad.show();
    }
}
