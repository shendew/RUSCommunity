package com.kingdew.ruslcommunity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingdew.ruslcommunity.Helpers.EventMemberView;
import com.kingdew.ruslcommunity.Models.EventData;
import com.kingdew.ruslcommunity.R;
import com.skydoves.elasticviews.ElasticImageView;

import java.util.ArrayList;

public class EventCenterAdapter extends RecyclerView.Adapter<EventCenterAdapter.ViewHolder> {

    Context context;
    ArrayList<EventData> events;

    public EventCenterAdapter(Context context, ArrayList<EventData> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.center_event_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventData item= events.get(position);
        holder.head.setText(item.getTitle());
        Glide.with(context).load(item.getImage()).centerCrop().into(holder.image);

        holder.edit.setOnClickListener(view -> {

        });
        holder.del.setOnClickListener(view -> {
            deleteItem(item.getId());
        });
        holder.viewmemb.setOnClickListener(view -> {
            context.startActivity(new Intent(context, EventMemberView.class).putExtra("id",item.getId()));
        });

    }

    private void deleteItem(int id) {

        FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    if (ds.child("id").getValue(Integer.class).equals(id)){
                        ds.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView head,viewmemb;
        ElasticImageView edit,del;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            head=itemView.findViewById(R.id.head);
            viewmemb=itemView.findViewById(R.id.viewmem);
            edit=itemView.findViewById(R.id.edit);
            del=itemView.findViewById(R.id.del);
            image=itemView.findViewById(R.id.image);
        }
    }
}
