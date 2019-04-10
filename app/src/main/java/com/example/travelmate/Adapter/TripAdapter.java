package com.example.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.Holder> {

    Context context;
    ArrayList<String> list;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ArrayList<String> list1 = new ArrayList<>();
    String name;


    public TripAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.mytrip, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int i) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelClick(name, i);
            }
        });
        if (list.get(i) != null) {
            databaseReference.child("User Profile").child(mUser.getUid()).child("MyTrip").child(list.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        getDataSetData(dataSnapshot, holder, i);
                    } catch (Exception e) {
                        Log.e("msg", e.getMessage());
                    }
                    Log.e("dbfhjg", "error");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void onDelClick(String name, final int i) {
        databaseReference.child("User Profile").child(mUser.getUid()).child("MyTrip").child(list.get(i)).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                notifyItemRemoved(i);
                list.remove(i);
            }
        });

    }

    private void getDataSetData(DataSnapshot dataSnapshot, final Holder holder, int i) {
        name = dataSnapshot.child("tripname").getValue().toString();
        String date = dataSnapshot.child("date").getValue().toString();

        holder.tvTripName.setText(name);
        holder.tvDate.setText(date);

        databaseReference.child("User Profile").child(mUser.getUid()).child("MyTrip").child(list.get(i)).child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> list = (ArrayList<String>) dataSnapshot.getValue();
                ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(context, name);
                holder.expandableListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ExpandableListView expandableListView;
        TextView tvTripName, tvDate;
        ImageView ivDel;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTripName = itemView.findViewById(R.id.tvTripName);
            tvDate = itemView.findViewById(R.id.tvDate);
            expandableListView = itemView.findViewById(R.id.expandableListView);
            ivDel = itemView.findViewById(R.id.ivDel);
        }
    }

}
