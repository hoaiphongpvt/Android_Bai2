package com.example.rakshith.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import java.text.SimpleDateFormat;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import java.util.Date;
import android.widget.Toast;

public class ActivityEdit extends AppCompatActivity {

    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d, hh:mm a");
    private EditText title;
    private EditText note;
    private Menu mOptionsMenu;
    private String titleText = "" , noteText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = (EditText) findViewById(R.id.title_edit);
        note = (EditText) findViewById(R.id.notes_edit);

        Intent intent = getIntent();
        if (intent.hasExtra("TITLE")) {
            titleText = intent.getStringExtra("TITLE");
            title.setText(titleText);
        }
        if (intent.hasExtra("NOTE")) {
            noteText = intent.getStringExtra("NOTE");
            note.setText(noteText);
        }

        note.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onBackPressed() {

        if(title.getText().toString().isEmpty()){
            Toast.makeText(this, "Untitled note was not saved", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        else if(titleText.equals(title.getText().toString()) && noteText.equals(note.getText().toString())){
            super.onBackPressed();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    saveNote();

                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    
                    finish();
                }
            });

            builder.setMessage("Do you want to save the note?");
            builder.setTitle("Note Save");

            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_save_menu, menu);
        mOptionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveNote(){
        Notes newNote = new Notes();
        newNote.setTitle(title.getText().toString());
        newNote.setNote(note.getText().toString());
        newNote.setDate(sdf.format(new Date()));
        Intent resultIntent = new Intent();
        resultIntent.putExtra("EDIT_NOTE", newNote);

        if(title.getText().toString().isEmpty()) {
            resultIntent.putExtra("STATUS", "NO_CHANGE");
            Toast.makeText(this, "Untitled note was not saved", Toast.LENGTH_SHORT).show();
        }
        else if(titleText.isEmpty() && noteText.isEmpty())
            resultIntent.putExtra("STATUS", "NEW");
        else if(titleText.equals(title.getText().toString()) && noteText.equals(note.getText().toString()))
            resultIntent.putExtra("STATUS", "NO_CHANGE");
        else
            resultIntent.putExtra("STATUS", "CHANGE");


        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
