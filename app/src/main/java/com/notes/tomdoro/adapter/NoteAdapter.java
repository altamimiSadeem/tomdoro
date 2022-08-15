package com.notes.tomdoro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.notes.tomdoro.R;
import com.notes.tomdoro.model.CalenderData;
import com.notes.tomdoro.model.NoteData;

import java.util.ArrayList;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.VH> {

    private int rowLayout;
    private Context context;
    private ArrayList<NoteData> notes;
    private DeleteItem listner;


    public static class VH extends RecyclerView.ViewHolder {

        TextView noteTV;
        ImageView deleteButton;

        public VH(View v) {
            super(v);
            noteTV = v.findViewById(R.id.noteTV);
            deleteButton = v.findViewById(R.id.deleteButton);

        }
    }

    public NoteAdapter(ArrayList<NoteData> notes, int rowLayout, Context context,DeleteItem
                        listner) {

        this.notes = notes;
        this.rowLayout = rowLayout;
        this.context = context;
        this.listner = listner;

    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.noteTV.setText(notes.get(position).getName());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onDeleteItem(notes.get(position));
                notes.remove(notes.get(position)) ;
                notifyDataSetChanged();
            }
        });

    }

    public void addPlace(NoteData noteData) {
        notes.add(noteData);
        notifyDataSetChanged();
    }
    public interface DeleteItem {

        void onDeleteItem(NoteData calenderData);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}

