package com.example.igor.mygradetracker;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> listGrades;
    private ArrayAdapter<String> adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = openOrCreateDatabase("MyDatabase", MODE_PRIVATE, null);
        db.execSQL("Create table if not exists MyTable (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "                                       StudentInfo TEXT);");

       // final ArrayList<String> listGrades;
        //final ArrayAdapter<String> adapter;
        final EditText studentId;
        final EditText grade;

        ListView listView = (ListView) findViewById(R.id.listv);
        listGrades = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_items, R.id.item, listGrades);
        listView.setAdapter(adapter);
        studentId = (EditText)findViewById(R.id.studentID);
        grade = (EditText) findViewById(R.id.grade);

        Button addItem = (Button) findViewById(R.id.add);

        Cursor cursor = db.query("MyTable", new String[]{"StudentInfo"}, null, null,null,null,null );
        if (cursor.moveToFirst()){
            do{
                String student = cursor.getString(0);
                listGrades.add(student);
            } while (cursor.moveToNext());
        }
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float newGrade = Float.parseFloat(grade.getText().toString());
                int newId = Integer.parseInt(studentId.getText().toString());

                if (newGrade > 100){
                    Toast.makeText(getApplicationContext(), "Grade is not valid", Toast.LENGTH_LONG).show();
                }else {
                    String newItem = "Student ID: " + newId + ", Grade: " + newGrade;
                    //db.execSQL("Insert into MyTable values(" + newItem + ");");
                    listGrades.add(newItem);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void dialog(){
        AlertDialog.Builder alertBox = new AlertDialog.Builder(MainActivity.this);

        alertBox.setMessage("Maximum number of records");
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertBox.setView(input);

        alertBox.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertBox.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Done pressed", Toast.LENGTH_LONG).show();
                    }
                });


        alertBox.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.max_records) {
            dialog();
            return true;
        }
        if ( id == R.id.page_next){
            return true;
        }
        if (id == R.id.page_prev){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
