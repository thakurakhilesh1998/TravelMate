package com.example.travelmate.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import java.util.HashMap;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.Holder> {

    Context context;
    ArrayList<String> list, list2, list3;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Dialog dialog;
    viewmytripactivity viewmytripactivity;
    Button edittrip;
    ImageView btnadditem;
    EditText tripname, destination1, pickdate, etItemname;
    LinearLayout itemlist;
    String listitem;
    TextView item;
    View view;

    public TripAdapter(viewmytripactivity viewmytripactivity, Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        this.viewmytripactivity = viewmytripactivity;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.mytrip, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int i) {
        list3 = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.child("Trips").child(mUser.getUid()).child("MyTrip").child(list.get(i)).addValueEventListener(new ValueEventListener() {
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

    private void getDataSetData(DataSnapshot dataSnapshot, final Holder holder, final int i) {
        if (dataSnapshot.hasChildren()) {
            String name, date, destination, date1;
            name = dataSnapshot.child("tripname").getValue().toString();
            date = dataSnapshot.child("date").getValue().toString();
            date1 = date.substring(0, 11);
            destination = dataSnapshot.child("destination").getValue().toString();
            holder.tvTripName.setText(name);
            holder.tvDate.setText(date1);
            holder.tvDestination.setText(destination);

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonEditClick(i, list3);
                }
            });
            databaseReference.child("Trips").child(mUser.getUid()).child("MyTrip").child(list.get(i)).child("list").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        list3 = (ArrayList<String>) dataSnapshot.getValue();
                        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(context, list3);
                        holder.expandableListView.setAdapter(adapter);
                        onExpandViewExapned(holder, list3);
                        onExpandViewCollapsed(holder, list3);
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

                holder.expandableListView.getLayoutParams().height = 80;
            }
        });
    }

    private void onExpandViewExapned(final Holder holder, final ArrayList<String> list1) {
        holder.expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int height = 0;
                for (int i = 0; i < list1.size(); i++) {
                    height += 100;
                }

                Log.e("height", String.valueOf(height));
                holder.expandableListView.getLayoutParams().height = height;
                height = 0;

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void onEditTrip(int adapterPosition, ArrayList<String> list13) {
        if (tripname.getText().toString().isEmpty() || tripname.getText().toString().length() < 3) {
            tripname.setError("tripname can not be empty");
            tripname.setFocusable(true);
        } else if (destination1.getText().toString().isEmpty() || destination1.getText().toString().length() < 3) {
            destination1.setError("destination can not be empty");
            destination1.setFocusable(true);
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("tripname", tripname.getText().toString().trim());
            map.put("destination", destination1.getText().toString().trim());
            Log.e("list2", String.valueOf(list2.size()));
            Log.e("list3", String.valueOf(list13.size()));
            if (list13.size() > 0) {
                list2.addAll(list13);
                map.put("list", list2);
            } else {
                map.put("list", list2);
            }
            tripname.getText().toString().trim();
            destination1.getText().toString().trim();
            FirebaseDatabase.getInstance().getReference().child("Trips").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyTrip").child(list.get(adapterPosition)).updateChildren(map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    Intent intent = viewmytripactivity.getIntent();
                    context.startActivity(intent);
                    viewmytripactivity.finish();
                }
            });

        }


    }

    private void onButtonEditClick(final int adapterPosition, final ArrayList<String> list13) {

        dialog = new Dialog(viewmytripactivity);
        dialog.setContentView(R.layout.edittrip);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        findDialogIds();
        FirebaseDatabase.getInstance().getReference().child("Trips").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyTrip").child(list.get(adapterPosition)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    tripname.setText(dataSnapshot.child("tripname").getValue().toString());
                    destination1.setText(dataSnapshot.child("destination").getValue().toString());
                    pickdate.setText(dataSnapshot.child("date").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        list2 = new ArrayList<>();
        itemlist = dialog.findViewById(R.id.itemlist);
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        onAddItem();
        edittrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditTrip(adapterPosition, list13);
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void findDialogIds() {
        btnadditem = dialog.findViewById(R.id.btnaddItem);
        tripname = dialog.findViewById(R.id.tripname);
        destination1 = dialog.findViewById(R.id.destination);
        pickdate = dialog.findViewById(R.id.pickdate);
        etItemname = dialog.findViewById(R.id.etItemName);
        edittrip = dialog.findViewById(R.id.edittrip);
    }

    private void onAddItem() {
        btnadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etItemname.getText().toString().trim().isEmpty()) {
                    etItemname.setError("please enter item name");
                } else {
                    item = new TextView(context);
                    item.setText(etItemname.getText().toString().trim());
                    list2.add(etItemname.getText().toString().trim());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(20, 6, 0, 0);
                    item.setLayoutParams(params);
                    item.setTextSize(24);
                    item.setTextColor(Color.parseColor("#4CABFF"));
                    item.setVisibility(View.VISIBLE);
                    itemlist.addView(item);
                    etItemname.setText("");
                }
            }
        });
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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
