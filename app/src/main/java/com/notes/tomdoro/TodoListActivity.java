package com.notes.tomdoro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.notes.tomdoro.adapter.ToDoAdapter;
import com.notes.tomdoro.model.ToDoData;

import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity implements ToDoAdapter.CheckList {
    ImageView addBTN;
    AlertDialog dialog;
    ToDoData todoData = new ToDoData();

    static RecyclerView todoRV;
    ImageView home;
    private FirebaseUser user;
    private ToDoAdapter adapter;
    private ArrayList<ToDoData> todos;
    String userId = "";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        prefs = getSharedPreferences("myPref", MODE_PRIVATE);

        userId = prefs.getString("userID", "").toString();
        addBTN = findViewById(R.id.addBTN);
        todoRV = findViewById(R.id.todoRV);
        getTodo();
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListActivity.this.finish();
            }
        });

        todoRV.setLayoutManager
                (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View addKidDialog = LayoutInflater.from(TodoListActivity.this).inflate(R.layout.add_todo_dialog, null);
                addKidDialog.setBackgroundColor(
                        ContextCompat.getColor(
                                TodoListActivity.this, R.color.transparent));
                //AlertDialogBuilder
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TodoListActivity.this).setView(addKidDialog);

                //show dialog
                dialog = dialogBuilder.show();

                final EditText taskET = dialog.findViewById(R.id.taskET);
                ImageView closeIV = dialog.findViewById(R.id.closeIV);
                Button doneBTN = dialog.findViewById(R.id.addBTN);


                closeIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                doneBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        todoData.setName(taskET.getText().toString());
                        todoData.setUserId(userId);
                        todoData.setChecked(false);
                        todoData.setId(userId + taskET.getText().toString());
                        saveToDoData();
                    }
                });
            }
        });
    }

    void saveToDoData() {
        if (todoData.getName().isEmpty()) {
            Toast.makeText(TodoListActivity.this, "Add name", Toast.LENGTH_LONG).show();
        } else {
            dialog.dismiss();
            Utilities.showLoadingDialog(TodoListActivity.this, R.color.colorPrimary);

            FirebaseDatabase.getInstance()
                    .getReference("TODO")
                    .child(todoData.getId()).setValue(todoData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                com.notes.tomdoro.Utilities.dismissLoadingDialog();
                                Toast.makeText(TodoListActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                                adapter.removeAll();
                                getTodo();
                            } else {
                                Toast.makeText(TodoListActivity.this, R.string.error_in_connection, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void getTodo() {
        Utilities.showLoadingDialog(TodoListActivity.this, R.color.colorPrimary);

        todos = new ArrayList<>();
        adapter = new ToDoAdapter(todos,
                R.layout.item_todo, this, this);
        FirebaseDatabase
                .getInstance()
                .getReference("TODO")
                .addChildEventListener(new ChildEventListener() {// listen to child
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                        Utilities.dismissLoadingDialog();
                        ToDoData commentModel = dataSnapshot.getValue(ToDoData.class);

                        if (commentModel != null && commentModel.getUserId().equals(userId)) {

                            adapter.addToDo(commentModel);
                            todoRV.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    @Override
    public void onCheckList(Boolean status, ToDoData toDoData) {
        ToDoData newtodo = toDoData;

        if (status) {
            Utilities.showLoadingDialog(TodoListActivity.this, R.color.colorPrimary);
            toDoData.setChecked(true);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("TODO");
            myRef.child(toDoData.getId()).setValue(newtodo).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Utilities.dismissLoadingDialog();
                            adapter.removeAll();

                            getTodo();
                        }
                    });

        } else {
            Utilities.showLoadingDialog(TodoListActivity.this, R.color.colorPrimary);
            toDoData.setChecked(false);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("TODO");
            myRef.child(toDoData.getId()).setValue(newtodo).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Utilities.dismissLoadingDialog();
                            adapter.removeAll();

                            getTodo();
                        }
                    });
        }


    }

    @Override
    public void onDeleteItem(ToDoData calenderData) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


        Query applesQuery = ref.child("TODO").orderByChild("name").equalTo(calenderData.getName());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    Log.e("cccc", appleSnapshot.toString());

                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }
}
