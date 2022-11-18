package com.example.rakshith.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private static final int B_REQUEST_CODE = 1;

    private List<Notes> notesList = new ArrayList<>();  // Main content is here

    private RecyclerView recyclerView; // Layout's recyclerview

    private AdapterNote adapterNote; // Data to recyclerview adapter

    private int positn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        adapterNote = new AdapterNote(notesList, this);

        recyclerView.setAdapter(adapterNote);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //call a async method and that method should return a noteList if exist otherwise null
        new MyAsyncTask(this).execute();
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks

        positn = recyclerView.getChildLayoutPosition(v);
        Notes extraNote = notesList.get(positn);
        //Send the content of the selected note to the edit view
        Intent intent1 = new Intent(MainActivity.this, ActivityEdit.class);
        intent1.putExtra("TITLE", extraNote.getTitle());
        intent1.putExtra("DATE", extraNote.getDate());
        intent1.putExtra("NOTE", extraNote.getNote());
        startActivityForResult(intent1, B_REQUEST_CODE);
    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        positn = recyclerView.getChildLayoutPosition(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notesList.remove(positn);
                adapterNote.notifyDataSetChanged();
                positn = -1;
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                positn = -1;
            }
        });

        builder.setMessage("Do you want to delete the note?");
        builder.setTitle("Note Delete");

        AlertDialog dialog = builder.create();
        dialog.show();


        return false;
    }

    @Override
    protected void onResume(){
        notesList.size();
        super.onResume();
        adapterNote.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {


        saveNotes();
        super.onPause();
    }

    private void saveNotes() {

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("notes");
            writeNotesArray(writer);
            writer.endObject();
            writer.close();


        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void writeNotesArray(JsonWriter writer) throws IOException {
        writer.beginArray();
        for (Notes value : notesList) {
            writeNotesObject(writer, value);
        }
        writer.endArray();
    }

    public void writeNotesObject(JsonWriter writer, Notes val) throws IOException{
        writer.beginObject();
        writer.name("title").value(val.getTitle());
        writer.name("date").value(val.getDate());
        writer.name("note").value(val.getNote());
        writer.endObject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:

                Intent intent = new Intent(MainActivity.this, Activityinfo.class);
                startActivity(intent);
                return true;
            case R.id.addNote:

                Intent intent1 = new Intent(MainActivity.this, ActivityEdit.class);
                startActivityForResult(intent1, B_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == B_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Notes notes_edit = (Notes) data.getExtras().getSerializable("EDIT_NOTE");
                String status = data.getStringExtra("STATUS");

                if(status.equals("NO_CHANGE")){
                    //Do nothing
                }
                else if(status.equals("CHANGE")){
                    //remove the old data
                    notesList.remove(positn);
                    notesList.add(0, notes_edit);
                }
                else if(status.equals("NEW")){
                    notesList.add(0, notes_edit);
                }


            }

        }
    }

    public List<Notes> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<Notes> notesList) {
        this.notesList = notesList;
        adapterNote.notifyDataSetChanged();
    }

}