package com.example.rakshith.multinotes;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyAsyncTask extends android.os.AsyncTask<Void, Void, Void> { //  <Parameter, Progress, Result>


    private MainActivity mainActivity;



    public MyAsyncTask(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected Void doInBackground(Void... voids) {


        try {

            InputStream is = mainActivity.getApplicationContext().openFileInput(mainActivity.getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(is, mainActivity.getString(R.string.encoding)));
            String name;

            reader.beginObject();

            while (reader.hasNext()) {
                name = reader.nextName();
                if (name.equals("notes")) {

                    reader.beginArray();
                    while (reader.hasNext()) {
                        Notes tempNotes = new Notes();
                        reader.beginObject();
                        while(reader.hasNext()) {
                            name = reader.nextName();
                            if (name.equals("title")) {
                                tempNotes.setTitle(reader.nextString());
                            } else if (name.equals("date")) {
                                tempNotes.setDate(reader.nextString());
                            } else if (name.equals("note")) {
                                tempNotes.setNote(reader.nextString());
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                        mainActivity.getNotesList().add(tempNotes);

                    }
                    reader.endArray();
                }
                else{
                    reader.skipValue();
                }

            }
            reader.endObject();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: "+e);
        }

        return null;
    }



}

