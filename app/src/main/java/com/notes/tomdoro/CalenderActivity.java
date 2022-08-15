package com.notes.tomdoro;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
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
import com.notes.tomdoro.adapter.CalenderAdapter;
import com.notes.tomdoro.model.CalenderData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity implements CalenderAdapter.DeleteItem {
    ImageView addBTN;
    AlertDialog dialog;
    CalenderData calenderData = new CalenderData();

    static RecyclerView calenderRV;

    private FirebaseUser user;
    private CalenderAdapter adapter;
    private ArrayList<CalenderData> calenders;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;
    String userId = "";
    SharedPreferences prefs;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        prefs = getSharedPreferences("myPref", MODE_PRIVATE);
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalenderActivity.this.finish();
            }
        });
        userId = prefs.getString("userID", "");
        addBTN = findViewById(R.id.addBTN);
        calenderRV = findViewById(R.id.calenderRV);

        calenderRV.setLayoutManager
                (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getCalenders();


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                calenderData.setYear(String.valueOf(year));
                calenderData.setMonth(String.valueOf(monthOfYear + 1));
                calenderData.setDay(String.valueOf(dayOfMonth));

            }

        };
        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calenderData.setHour(String.valueOf(hourOfDay));
                calenderData.setMinis(String.valueOf(minute));
            }
        };


        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View addKidDialog = LayoutInflater.from(CalenderActivity.this).inflate(R.layout.add_calender_dialog, null);
                addKidDialog.setBackgroundColor(
                        ContextCompat.getColor(
                                CalenderActivity.this, R.color.transparent));
                //AlertDialogBuilder
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CalenderActivity.this).setView(addKidDialog);

                //show dialog
                dialog = dialogBuilder.show();

                final EditText reminderET = dialog.findViewById(R.id.reminderET);
                ImageView closeIV = dialog.findViewById(R.id.closeIV);
                Button doneBTN = dialog.findViewById(R.id.addBTN);
                Button addDateBTN = dialog.findViewById(R.id.addDateBTN);
                Button addTimeBTN = dialog.findViewById(R.id.addTimeBTN);

                closeIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                addDateBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialogDate();
                    }
                });

                addTimeBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialogTime();
                    }
                });

                doneBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calenderData.setName(reminderET.getText().toString());
                        calenderData.setUserId(userId);
                        saveCalenderData();
                    }
                });
            }
        });
    }

    final Calendar myCalendar = Calendar.getInstance();

    void setDialogDate() {
        new DatePickerDialog(CalenderActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    void setDialogTime() {
        new TimePickerDialog(CalenderActivity.this, time, myCalendar
                .get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), false).show();
    }

    void saveCalenderData() {
        if (calenderData.getDay().isEmpty()) {
            Toast.makeText(CalenderActivity.this, "Select date", Toast.LENGTH_LONG).show();
        } else if (calenderData.getName().isEmpty()) {
            Toast.makeText(CalenderActivity.this, "Select reminder", Toast.LENGTH_LONG).show();
        } else {
            dialog.dismiss();
            com.notes.tomdoro.Utilities.showLoadingDialog(this, R.color.colorPrimary);

            FirebaseDatabase.getInstance()
                    .getReference("CALENDER")
                    .push()
                    .setValue(calenderData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                com.notes.tomdoro.Utilities.dismissLoadingDialog();
                                Toast.makeText(CalenderActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                                getCalenders();
                            } else {
                                Toast.makeText(CalenderActivity.this, R.string.error_in_connection, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void getCalenders() {
        calenders = new ArrayList<>();
        adapter = new CalenderAdapter(calenders,
                R.layout.item_calender, this, this);
        FirebaseDatabase
                .getInstance()
                .getReference("CALENDER")
                .addChildEventListener(new ChildEventListener() {// listen to child
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                        Utilities.dismissLoadingDialog();
                        CalenderData commentModel = dataSnapshot.getValue(CalenderData.class);

                        if (commentModel != null && commentModel.getUserId().equals(userId)) {
                            adapter.addPlace(commentModel);
                            calenderRV.setAdapter(adapter);
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
    public void onDeleteItem(CalenderData calenderData) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


        Query applesQuery = ref.child("CALENDER").orderByChild("name").equalTo(calenderData.getName());

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
