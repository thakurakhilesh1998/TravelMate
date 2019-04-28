package com.example.travelmate.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelmate.R;
import com.example.travelmate.utility.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatWithUsActivityAdapter extends BaseAdapter {
    Context context;

    ArrayList<String> list;

    public ChatWithUsActivityAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.message, parent, false);

        final TextView tvDate = view.findViewById(R.id.tvDate);
        //final TextView tvUser = view.findViewById(R.id.tvUser);
        final LinearLayout linearmessage = view.findViewById(R.id.linearmessage);
        final TextView tvMsg = view.findViewById(R.id.tvMsg);
        final LinearLayout linearchat = view.findViewById(R.id.linearchat);
        FirebaseDatabase.getInstance().getReference().child("Chat With Us").child("Send").child(decode(FirebaseAuth.getInstance().getCurrentUser().getEmail())).child(list.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.RIGHT;
                ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                if (chatModel.getMessageUSer().equals("admin")) {
                    linearchat.setLayoutParams(params);
                    linearmessage.setBackgroundColor(Color.parseColor("#4CABFF"));
                }
                else {
                    linearmessage.setBackgroundColor(Color.parseColor("#4ed50a"));

                }
                tvDate.setText(chatModel.getMessageTime().substring(0, 11));
                // tvUser.setText(chatModel.getMessageUSer());
                tvMsg.setText(chatModel.getMessageText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    public String decode(String email) {
        String decoded = email.replace('@', '_');
        return decoded.replace('.', '!');
    }


}
