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
import com.notes.tomdoro.adapter.NoteAdapter;
import com.notes.tomdoro.model.NoteData;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity implements NoteAdapter.DeleteItem {
    ImageView addBTN;
    AlertDialog dialog;
    NoteData noteData = new NoteData();

    static RecyclerView noteRV;
    ImageView home;
    private FirebaseUser user;
    private NoteAdapter adapter;
    private ArrayList<NoteData> notes;
    String userId = "";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        prefs = getSharedPreferences("myPref", MODE_PRIVATE);

        userId = prefs.getString("userID", "").toString();
        addBTN = findViewById(R.id.addBTN);
        noteRV = findViewById(R.id.noteRV);

        noteRV.setLayoutManager
                (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getNotes();
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteActivity.this.finish();
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View addKidDialog = LayoutInflater.from(NoteActivity.this).inflate(R.layout.add_note_dialog, null);
                addKidDialog.setBackgroundColor(
                        ContextCompat.getColor(
                                NoteActivity.this, R.color.transparent));
                //AlertDialogBuilder
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NoteActivity.this).setView(addKidDialog);

                //show dialog
                dialog = dialogBuilder.show();

                final EditText noteET = dialog.findViewById(R.id.noteET);
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
                        noteData.setName(noteET.getText().toString());
                        noteData.setUserId(userId);
                        saveNoteData();
                    }
                });
            }
        });
    }

    void saveNoteData() {
        if (noteData.getName().isEmpty()) {
            Toast.makeText(NoteActivity.this, "Add name", Toast.LENGTH_LONG).show();
        } else {
            dialog.dismiss();
            com.notes.tomdoro.Utilities.showLoadingDialog(this, R.color.colorPrimary);

            FirebaseDatabase.getInstance()
                    .getReference("NOTE").push()
                    .setValue(noteData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                com.notes.tomdoro.Utilities.dismissLoadingDialog();
                                Toast.makeText(NoteActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                                getNotes();
                            } else {
                                Toast.makeText(NoteActivity.this, R.string.error_in_connection, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void getNotes() {
        notes = new ArrayList<>();
        adapter = new NoteAdapter(notes,
                R.layout.item_note, this ,this);
        FirebaseDatabase
                .getInstance()
                .getReference("NOTE")
                .addChildEventListener(new ChildEventListener() {// listen to child
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                        Utilities.dismissLoadingDialog();
                        NoteData commentModel = dataSnapshot.getValue(NoteData.class);

                        if (commentModel != null && commentModel.getUserId().equals(userId)) {

                            adapter.addPlace(commentModel);
                            noteRV.setAdapter(adapter);
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
    public void onDeleteItem(NoteData calenderData)  {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


            Query applesQuery = ref.child("NOTE").orderByChild("name").equalTo(calenderData.getName());

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
