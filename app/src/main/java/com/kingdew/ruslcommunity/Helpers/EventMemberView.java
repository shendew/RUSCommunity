package com.kingdew.ruslcommunity.Helpers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingdew.ruslcommunity.Models.EventData;
import com.kingdew.ruslcommunity.R;
import com.skydoves.elasticviews.ElasticImageView;

import java.util.ArrayList;

public class EventMemberView extends AppCompatActivity {
    int id;
    ElasticImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_member_view);
        back=findViewById(R.id.back);



            id=getIntent().getExtras().getInt("id");

            RecyclerView eventRview=findViewById(R.id.view);
            ArrayList<String> members=new ArrayList<>();
            eventRview.setHasFixedSize(true);
            eventRview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            DatabaseReference dr= FirebaseDatabase.getInstance().getReference("Events");

            dr.child(String.valueOf(id)).child("joined").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds:snapshot.getChildren()){
                        members.add(ds.getValue(String.class));
                    }
                    adapter ad=new adapter(getApplicationContext(),members);
                    eventRview.setAdapter(ad);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            back.setOnClickListener(view -> {
                finish();
            });
    }

    private class adapter extends RecyclerView.Adapter<adapter.ViewHolder>{

        Context context;
        ArrayList<String> members;

        public adapter(Context context, ArrayList<String> members) {
            this.context = context;
            this.members = members;
        }

        @NonNull
        @Override
        public adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.event_member_view_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull adapter.ViewHolder holder, int position) {
            holder.name.setText(members.get(position));
        }

        @Override
        public int getItemCount() {
            return members.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            ElasticImageView delete;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                delete=itemView.findViewById(R.id.delete);
            }
        }
    }
}