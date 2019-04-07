package com.example.travelmate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    EditText etTripname, etItemname;
    Button btnadditem;
    TextView textView;
    LinearLayout linearLayout;
    ArrayList<String> list;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

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
                notification();
                break;
            case R.id.btnaddItem:
                addItem();
        }
    }

    private void addItem() {
        String textItem = etItemname.getText().toString();
        textView = new TextView(this);
        list.add(textItem);
        addItemToTextView(textItem);
    }

    private void addItemToTextView(String textItem) {
        textView.setText(textItem);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(textView);
    }

    private void notification() {
        String tripname = etTripname.getText().toString().trim();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(now.getTime());
        savetripdata data = new savetripdata(tripname, formattedDate, list);
        reference = database.getReference();
        reference.child("User Profile").child(mUser.getUid()).child("MyTrip").child(formattedDate).setValue(data);
        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title("hello")
                .content("hi")
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .key("test")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
    }

    private void pickDate() {
        dpd.show(getFragmentManager(), "Datepickerdialog");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(now.getTime());
        tvPickDate.setText(formattedDate);
    }
}
