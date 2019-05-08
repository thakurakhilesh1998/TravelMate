package com.example.travelmate;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allyants.notifyme.NotifyMe;
import com.example.travelmate.utility.savetripdata;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class mytrip_activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    TextView tvPickDate;
    Button btnCreate;
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    int y, m, d, h, mi;
    EditText etTripname, etItemname, etDestination;
    ImageView btnadditem;
    TextView textView;
    LinearLayout linearLayout;
    ArrayList<String> list;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrip_activity);
        findIds();
        findDateTime();
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        tvPickDate.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnadditem.setOnClickListener(this);
    }

    private void findDateTime() {

        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );


    }

    private void findIds() {

        tvPickDate = findViewById(R.id.tvPickDate);
        btnCreate = findViewById(R.id.btnCreate);
        etItemname = findViewById(R.id.etItemName);
        etTripname = findViewById(R.id.etTripName);
        btnadditem = findViewById(R.id.btnaddItem);
        linearLayout = findViewById(R.id.linearLayout);
        etDestination = findViewById(R.id.etDestination);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tpd.show(getFragmentManager(), "Timepickerdialog");
        y = year;
        m = monthOfYear + 1;
        d = dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        Log.e("minute", String.valueOf(minute));
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);
        h = hourOfDay;
        mi = minute;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvPickDate:
                pickDate();

                break;
            case R.id.btnCreate:

                notification(v);

                break;
            case R.id.btnaddItem:
                addItem();
        }
    }

    private void addItem() {

        String textItem = etItemname.getText().toString();
        if (textItem.isEmpty()) {
            etItemname.setFocusable(true);
            etItemname.setError("Enter a item name");
        } else {
            textView = new TextView(this);
            list.add(textItem);
            etItemname.setText("");
            addItemToTextView(textItem);
        }

    }

    private void addItemToTextView(String textItem) {
        textView.setText(i + ". " + textItem);
        i++;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 6, 0, 0);
        textView.setLayoutParams(params);
        textView.setTextColor(getResources().getColor(R.color.background));
        textView.setTextSize(24);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.addView(textView);
    }

    private void notification(View v) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(now.getTime());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate1 = df1.format(c.getTime());

        if (etTripname.getText().toString().trim().isEmpty()) {
            etTripname.setError("Enter Trip Name");
            etTripname.setFocusable(true);
        } else if (etDestination.getText().toString().trim().isEmpty()) {
            etDestination.setError("Enter Destination Name");
            etDestination.setFocusable(true);
        } else if (tvPickDate.getText().toString().trim().isEmpty()) {
            tvPickDate.setError("Pick a date");
            tvPickDate.setFocusable(true);
        } else if (formattedDate.compareTo(formattedDate1) < 0) {
            tvPickDate.setError("pick right date");
            tvPickDate.setFocusable(true);
            Snackbar.make(v, "Please Pick Right Date", Snackbar.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), viewmytripactivity.class);
            String tripname = etTripname.getText().toString().trim();
            String destination = etDestination.getText().toString().trim();
            savetripdata data = new savetripdata(tripname, formattedDate, list, destination);
            reference = database.getReference();
            reference.child("User Profile").child(mUser.getUid()).child("MyTrip").child(formattedDate).setValue(data);
            NotifyMe notifyMe = new NotifyMe.Builder(this)
                    .title("Trip remainder,Your Trip"+etTripname.getText().toString()+"is started")
                    .content("Look at your item list so you can not miss items during your trip")
                    .color(255, 0, 0, 255)
                    .led_color(255, 255, 255, 255)
                    .small_icon(R.drawable.logo1)
                    .large_icon(R.drawable.tripicon)
                    .addAction(intent, "View My Trip")
                    .time(now)
                    .key("test")
                    .build();
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.mytripdialog);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
            Button btnOk = dialog.findViewById(R.id.btnok);
            ImageView image = dialog.findViewById(R.id.image1);
            image.startAnimation(animation);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearData();
                    dialog.dismiss();
                }
            });
        }
    }

    private void pickDate() {
        dpd.show(getFragmentManager(), "Datepickerdialog");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(now.getTime());
        tvPickDate.setText(formattedDate);
    }

    public void clearData() {

        etTripname.setText("");
        etDestination.setText("");
        tvPickDate.setText("");
    }
}
