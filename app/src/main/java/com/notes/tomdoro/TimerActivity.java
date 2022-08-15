package com.notes.tomdoro;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import cn.iwgang.countdownview.CountdownView;


public class TimerActivity extends AppCompatActivity implements CountdownView.OnCountdownIntervalListener {

    TextView numberIntervalTV;
    EditText intervalNumEditText;
    ImageView home;
    CountdownView countdownView;
    Button button, pausebutton;
    int interval = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        numberIntervalTV = findViewById(R.id.numberIntervalTV);
        button = findViewById(R.id.button);

        pausebutton = findViewById(R.id.pausebutton);

        countdownView = findViewById(R.id.countdownView);
        intervalNumEditText = findViewById(R.id.intervalNumEditText);

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerActivity.this.finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button.getText().equals("start")) {

                    if (intervalNumEditText.getText().toString().isEmpty()) {
                        Toast.makeText(TimerActivity.this, "Enter interval times ", Toast.LENGTH_SHORT).show();
                    } else {
                        interval = Integer.parseInt(intervalNumEditText.getText().toString());

                        button.setText("stop");
                        countdownView.start(interval * 1500000);
                        countdownView.setVisibility(View.VISIBLE);
                        intervalNumEditText.setVisibility(View.GONE);
                        numberIntervalTV.setVisibility(View.GONE);

                    }
                } else {
                    button.setText("start");
                    countdownView.setVisibility(View.GONE);
                    intervalNumEditText.setVisibility(View.VISIBLE);
                    numberIntervalTV.setVisibility(View.VISIBLE);
                    countdownView.stop();
                }
            }
        });

        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pausebutton.getText() == "pause") {
                    countdownView.pause();
                    pausebutton.setText("Unpause");

                } else {
                    countdownView.restart();
                    pausebutton.setText("pause");
                }

            }
        });


        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                Toast.makeText(TimerActivity.this, "Great Job ", Toast.LENGTH_LONG).show();

            }
        });


        countdownView.setOnCountdownIntervalListener(1500000, this);

    }


    @Override
    public void onInterval(CountdownView cv, long remainTime) {
        countdownView.pause();

        showAlertDialogButtonClicked(TimerActivity.this);
    }


    public void showAlertDialogButtonClicked(Activity view) {

        TextView textView = new TextView(getApplicationContext());
        textView.setText("Break Time");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(Color.parseColor("#F8EFEF"));
        textView.setTextColor(Color.parseColor("#DF2037"));

        // create an alert builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Break Time");
        builder.setCustomTitle(textView);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_timer_interval, null);
        builder.setView(customLayout);
        // add a button

        Button button = customLayout.findViewById(R.id.button);
        CountdownView countdownViewbreak = customLayout.findViewById(R.id.countdownView);

        countdownViewbreak.start(300000);
        final AlertDialog dialog = builder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                countdownView.restart();
            }
        });

        // create and show the alert dialog

    }
}
