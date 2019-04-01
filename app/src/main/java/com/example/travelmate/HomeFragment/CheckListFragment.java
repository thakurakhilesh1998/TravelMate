package com.example.travelmate.HomeFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelmate.R;
import com.example.travelmate.utility.savetripdata;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CheckListFragment extends Fragment implements View.OnClickListener {

    EditText etName, etDestination, etCB;
    Button btncreatetrip;
    String dateTime;
    TextView tvdateTime;
    CheckBox cb, cb1;
    Button btnAdd;
    String field;
    LinearLayout linearLayout;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    public CheckListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_check_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.etName);
        etDestination = view.findViewById(R.id.etDestination);
        tvdateTime = view.findViewById(R.id.tvdateTime);
        etCB = view.findViewById(R.id.etCB);
        btncreatetrip = view.findViewById(R.id.btnCreateTrip);
        btnAdd = view.findViewById(R.id.btnAdd);
        linearLayout = view.findViewById(R.id.linear);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        btnAdd.setOnClickListener(this);
        tvdateTime.setOnClickListener(this);
        btncreatetrip.setOnClickListener(this);
    }

    private void addDataToFirebase() {
        String tripname = etName.getText().toString();
        String destination = etDestination.getText().toString();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        savetripdata trip = new savetripdata(tripname, destination, dateTime);
        if (mUser != null) {
            reference = database.getReference("User Profile");
            reference.child(uid).child("MyTrips").setValue(trip);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvdateTime:
                showDatetime();
                break;
            case R.id.btnAdd:
                onBtnAdd();
            case R.id.btnCreateTrip:
                addDataToFirebase();

        }


    }

    private void onBtnAdd() {


        cb = new CheckBox(getContext());
        field = etCB.getText().toString();
        addCheckBox(field);


    }

    private void addCheckBox(String field) {

        cb.setText(field);
        cb.setVisibility(View.VISIBLE);
        cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(cb);


    }

    private void showDatetime() {
        new SingleDateAndTimePickerDialog.Builder(getContext())

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                    }
                })

                .title("Pick date And Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        Log.e("date", String.valueOf(date.getDate()));
                        Log.e("month", String.valueOf(date.getMonth()));
                        Log.e("year", String.valueOf(date.getYear()));
                        dateTime = date.getDate() + "/" + date.getMonth() + "/" + date.getYear() + "::" + date.getHours() + ":" + date.getMinutes();
                        tvdateTime.setText(dateTime);
                    }
                }).display();
    }

}
