package com.notes.tomdoro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.notes.tomdoro.R;
import com.notes.tomdoro.model.ToDoData;

import java.util.ArrayList;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.VH> {

    private int rowLayout;
    private Context context;
    private ArrayList<ToDoData> toDoData;
    CheckList listner;

    public static class VH extends RecyclerView.ViewHolder {

        TextView todoTV;
        CheckBox todoCB;
        ImageView deleteButton;

        public VH(View v) {
            super(v);
            todoTV = v.findViewById(R.id.todoTV);
            todoCB = v.findViewById(R.id.todoCB);
            deleteButton = v.findViewById(R.id.deleteButton);

        }
    }

    public ToDoAdapter(ArrayList<ToDoData> toDoData, int rowLayout, Context context, CheckList listner) {
        this.toDoData = toDoData;
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
        holder.todoCB.setText(toDoData.get(position).getName());
        if (toDoData.get(position).getChecked()) {
            holder.todoCB.setChecked(true);
        } else {
            holder.todoCB.setChecked(false);
        }

        holder.todoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                         listner.onCheckList(isChecked, toDoData.get(position));
                                                     }
                                                 }
        );

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onDeleteItem(toDoData.get(position));
                toDoData.remove(toDoData.get(position));
                notifyDataSetChanged();
            }
        });

    }

    public void addToDo(ToDoData todoData) {
        toDoData.add(todoData);
        notifyDataSetChanged();
    }

    public void removeAll() {
        toDoData.removeAll(toDoData);
        toDoData.clear();
        notifyDataSetChanged();
    }

    public interface CheckList {
        void onCheckList(Boolean status, ToDoData toDoData);

        void onDeleteItem(ToDoData calenderData);

    }

    @Override
    public int getItemCount() {
        return toDoData.size();
    }
}

