package com.example.travelmate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.travelmate.utility.ChatModel;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
        adapter = new FirebaseListAdapter<ChatModel>(this, ChatModel.class, R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatModel model, int position) {
                TextView tvDate = v.findViewById(R.id.tvDate);
                TextView tvUser = v.findViewById(R.id.tvUser);
                TextView tvMsg = v.findViewById(R.id.tvMsg);
                tvDate.setText(String.valueOf(model.getMessageTime()));
                tvUser.setText(model.getMessageUSer());
                tvMsg.setText(model.getMessageText());


            }
        };

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

        FirebaseDatabase.getInstance().getReference().push().setValue(new ChatModel(etMsg.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));
        etMsg.setText("");
    }
}
