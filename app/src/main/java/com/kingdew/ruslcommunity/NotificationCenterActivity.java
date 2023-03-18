package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingdew.ruslcommunity.Adapters.NotificationAdapter;
import com.kingdew.ruslcommunity.Adapters.NotificationCenterAdapter;
import com.kingdew.ruslcommunity.Models.NotificationData;
import com.skydoves.elasticviews.ElasticFloatingActionButton;
import com.skydoves.elasticviews.ElasticImageView;

import java.util.ArrayList;

public class NotificationCenterActivity extends AppCompatActivity {
    ElasticFloatingActionButton addnoti;
    ElasticImageView back,btn_edit,btn_del;
    ArrayList<NotificationData> notifications=new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);

        recyclerView=findViewById(R.id.not_recycler);

        addnoti=findViewById(R.id.addnoti);
        back=findViewById(R.id.back);
        btn_edit=findViewById(R.id.btn_edit);
        btn_del=findViewById(R.id.btn_del);
        recyclerView.setHasFixedSize(true);
        loadData();

        addnoti.setOnClickListener(view -> {
            startActivity(new Intent(this,NotificationAddActivity.class));
        });
        back.setOnClickListener(view -> {
            finish();
        });
    }

    private void loadData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseDatabase.getInstance().getReference("Notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!notifications.isEmpty()){
                    notifications.clear();
                }
                for (DataSnapshot ds:snapshot.getChildren()){
                    NotificationData data=ds.getValue(NotificationData.class);
                    notifications.add(data);
                }
                NotificationCenterAdapter adapter=new NotificationCenterAdapter(NotificationCenterActivity.this,notifications);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}