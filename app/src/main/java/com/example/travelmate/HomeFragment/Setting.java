package com.example.travelmate.HomeFragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelmate.MainActivity;
import com.example.travelmate.R;
import com.example.travelmate.utility.util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Setting extends Fragment implements View.OnClickListener {
    Button btnchangepass, btndelete;
    Dialog dialog;
    String uid;

    public Setting() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnchangepass = view.findViewById(R.id.btnchangepass);
        btndelete = view.findViewById(R.id.btndelete);
        btnchangepass.setOnClickListener(this);
        btndelete.setOnClickListener(this);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnchangepass:
                onChangePass();
                break;
            case R.id.btndelete:
                onDelete();

        }

    }

    private void onDelete() {

        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    util.toast(getContext(), "User Deleted Successfully");
                    deleteUser();
                }
            }
        });


    }

    private void deleteUser() {
        FirebaseDatabase.getInstance().getReference().child("User Profile").child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                } catch (Exception e) {
                    Log.e("msg", e.getMessage());
                }

            }
        });

    }

    private void onChangePass() {

        dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.resetpass);
        final EditText etEmail = dialog.findViewById(R.id.etEmail);
        etEmail.setEnabled(false);
        etEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Button btnResetPass = dialog.findViewById(R.id.btnResetPass);
        final ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        final TextView error = dialog.findViewById(R.id.error);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()) {

                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(etEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Password Reset Link Sent to your Email", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            error.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

    }


}
