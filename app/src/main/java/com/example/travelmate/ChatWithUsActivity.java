package com.example.travelmate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.travelmate.Adapter.ChatWithUsActivityAdapter;
import com.example.travelmate.utility.ChatModel;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatWithUsActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    EditText etMsg;
    FloatingActionButton btnSend;
    private FirebaseListAdapter<ChatModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_us);
        findIds();
        displayChatMsg();
    }

    private void displayChatMsg() {


        FirebaseDatabase.getInstance().getReference().child("Chat With Us").child("Send").child(decode(FirebaseAuth.getInstance().getCurrentUser().getEmail())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    ArrayList<String> list = new ArrayList<>();

                    Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                    Iterator myVeryOwnIterator = td.keySet().iterator();
                    while (myVeryOwnIterator.hasNext()) {
                        String key = (String) myVeryOwnIterator.next();
                        list.add(key);
                    }

                    Collections.sort(list);
                    for (int i = 0; i < list.size(); i++) {
                        Log.e("data from", list.get(i));
                    }

                    ChatWithUsActivityAdapter adapter = new ChatWithUsActivityAdapter(getApplicationContext(), list);
                    listView.setAdapter(adapter);
                } else {
                    Log.e("no msg", "no message found");
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        adapter = new FirebaseListAdapter<ChatModel>(this,ChatModel.class,R.layout.message,FirebaseDatabase.getInstance().getReference()) {
//            @Override
//            protected void populateView(View v, ChatModel model, int position) {
//                TextView tvDate = v.findViewById(R.id.tvDate);
//                TextView tvUser = v.findViewById(R.id.tvUser);
//                TextView tvMsg = v.findViewById(R.id.tvMsg);
//                tvDate.setText(String.valueOf(model.getMessageTime()));
//                tvUser.setText(model.getMessageUSer());
//                tvMsg.setText(model.getMessageText());
//            }
//        };

        listView.setAdapter(adapter);

    }

    private void findIds() {
        listView = findViewById(R.id.listview);
        etMsg = findViewById(R.id.etMsg);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                onSendMsg();
        }
    }

    private void onSendMsg() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String messageTime = df.format(c.getTime());

        FirebaseDatabase.getInstance().getReference().child("Chat With Us")
                .child("Send").child(decode(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .child(messageTime).setValue(new ChatModel(etMsg.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));
        etMsg.setText("");
    }


    public String decode(String email) {
        String decoded = email.replace('@', '_');
        return decoded.replace('.', '!');
    }


}
