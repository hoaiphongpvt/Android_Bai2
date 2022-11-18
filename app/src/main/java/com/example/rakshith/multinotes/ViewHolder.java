package com.example.rakshith.multinotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder{


    public TextView title;
    public TextView note;
    public TextView date;

    public ViewHolder(@NonNull View view) {
        super(view);

       note = (TextView) view.findViewById(R.id.notes);
       title = (TextView) view.findViewById(R.id.title);
       date = (TextView) view.findViewById(R.id.date);
    }
}
