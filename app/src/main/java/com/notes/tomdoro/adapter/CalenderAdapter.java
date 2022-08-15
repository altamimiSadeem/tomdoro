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

import java.util.ArrayList;


public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.VH> {

    private int rowLayout;
    private Context context;
    private ArrayList<CalenderData> calenders;
    private DeleteItem listner;

    public static class VH extends RecyclerView.ViewHolder {

        TextView dateTextView, eventNameTV;
        Button timeButton;
        ImageView deleteButton;

        public VH(View v) {
            super(v);
            dateTextView = v.findViewById(R.id.dateTextView);
            eventNameTV = v.findViewById(R.id.eventNameTV);
            timeButton = v.findViewById(R.id.timeButton);
            deleteButton = v.findViewById(R.id.deleteButton);

        }
    }

    public CalenderAdapter(ArrayList<CalenderData> calenders, int rowLayout, Context context, DeleteItem listner) {

        this.calenders = calenders;
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
        holder.dateTextView.setText(calenders.get(position).getYear() + " / " + calenders.get(position).getMonth() + " / " + calenders.get(position).getDay());
        holder.timeButton.setText(calenders.get(position).getHour() + " : " + calenders.get(position).getMinis());
        holder.eventNameTV.setText(calenders.get(position).getName());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             listner.onDeleteItem(calenders.get(position));
             calenders.remove(calenders.get(position)) ;
             notifyDataSetChanged();
            }
        });
    }

    public void addPlace(CalenderData commentModel) {
        calenders.add(commentModel);
        notifyDataSetChanged();
    }

    public interface DeleteItem {

        void onDeleteItem(CalenderData calenderData);
    }

    @Override
    public int getItemCount() {
        return calenders.size();
    }
}

