package com.example.rakshith.multinotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AdapterNote extends RecyclerView.Adapter<ViewHolder> {

    private List<Notes> notesList;
    private MainActivity mainAct;

    public AdapterNote(List<Notes> notesList, MainActivity mainAct) {
        this.notesList = notesList;
        this.mainAct = mainAct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notes_layout, viewGroup, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Notes note = notesList.get(position);
        viewHolder.title.setText(note.getTitle());
        viewHolder.date.setText(note.getDate());
        if(note.getNote().length() > 80){
            String new_Note = note.getNote().substring(0, 79);
            new_Note = new_Note.concat("...");
            viewHolder.note.setText(new_Note);
        }
        else {
            viewHolder.note.setText(note.getNote());
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
