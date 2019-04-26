package com.example.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
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
    String name, date, destination;


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
        databaseReference.child("User Profile").child(mUser.getUid()).child("MyTrip").child(list.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    getDataSetData(dataSnapshot, holder, i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getDataSetData(DataSnapshot dataSnapshot, final Holder holder, int i) {
        if (dataSnapshot.hasChildren()) {
            name = dataSnapshot.child("tripname").getValue().toString();
            date = dataSnapshot.child("date").getValue().toString().substring(0, 11);
            destination = dataSnapshot.child("destination").getValue().toString();
            holder.tvTripName.setText(name);
            holder.tvDate.setText(date);
            holder.tvDestination.setText(destination);
            databaseReference.child("User Profile").child(mUser.getUid()).child("MyTrip").child(list.get(i)).child("list").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        ArrayList<String> list = (ArrayList<String>) dataSnapshot.getValue();
                        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(context, list);
                        holder.expandableListView.setAdapter(adapter);
                        onExpandViewExapned(holder, list);
                        onExpandViewCollapsed(holder, list);
                    } else {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {

        }
    }

    private void onExpandViewCollapsed(final Holder holder, final ArrayList<String> list1) {
        holder.expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                holder.expandableListView.getLayoutParams().height =70;
            }
        });


    }

    private void onExpandViewExapned(final Holder holder, final ArrayList<String> list1) {
        holder.expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int height = 0;
                for (int i = 0; i < list1.size(); i++) {
                    height += 70;
                }
                holder.expandableListView.getLayoutParams().height =height;
                Log.e("height", String.valueOf(height));
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
        Button btnEdit;
        LinearLayout lineartrip;
        TextView tvnotrip, tvDestination;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTripName = itemView.findViewById(R.id.tvTripName);
            tvDate = itemView.findViewById(R.id.tvDate);
            expandableListView = itemView.findViewById(R.id.expandableListView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            lineartrip = itemView.findViewById(R.id.lineartrip);
            tvDestination = itemView.findViewById(R.id.tvDestination);

        }
    }

}
