package com.example.travelmate.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelmate.R;
import com.example.travelmate.viewmytripactivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.Holder> implements View.OnClickListener {

    Context context;
    ArrayList<String> list;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String name, date, destination;
    Dialog dialog;
    viewmytripactivity viewmytripactivity;
    Button edittrip;
    ImageView btnadditem;
    EditText tripname, destination1, pickdate, etItemname;
    LinearLayout itemlist;
    String listitem;
    TextView item;
    public TripAdapter(viewmytripactivity viewmytripactivity, Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        this.viewmytripactivity = viewmytripactivity;

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
            holder.btnEdit.setOnClickListener(this);
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

                holder.expandableListView.getLayoutParams().height = 70;
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
                holder.expandableListView.getLayoutParams().height = height;
                Log.e("height", String.valueOf(height));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnEdit:
                onButtonEditClick();
        }


    }

    private void onEditTrip() {
        name = tripname.getText().toString().trim();
        destination = destination1.getText().toString().trim();
        listitem = etItemname.getText().toString().trim();
        btnadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("item",listitem);
                item = new TextView(context);
                item.setText(listitem);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 6, 0, 0);
                item.setLayoutParams(params);
                item.setTextSize(24);
                item.setTextColor(Color.parseColor("#4CABFF"));
                item.setVisibility(View.VISIBLE);
                itemlist.addView(item);
            }
        });

    }

    private void onButtonEditClick() {
        dialog = new Dialog(viewmytripactivity);
        dialog.setContentView(R.layout.edittrip);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btnadditem = dialog.findViewById(R.id.btnaddItem);
        tripname = dialog.findViewById(R.id.tripname);
        destination1 = dialog.findViewById(R.id.destination);
        pickdate = dialog.findViewById(R.id.pickdate);
        etItemname = dialog.findViewById(R.id.etItemName);
        edittrip = dialog.findViewById(R.id.edittrip);
        itemlist = dialog.findViewById(R.id.itemlist);
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        edittrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditTrip();
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
