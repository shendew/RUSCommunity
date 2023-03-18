package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingdew.ruslcommunity.Adapters.NotificationAdapter;
import com.kingdew.ruslcommunity.Models.NotificationData;
import com.skydoves.elasticviews.ElasticImageView;

import java.util.ArrayList;

public class NotificationPageActivity extends AppCompatActivity {
    ElasticImageView back;
    RecyclerView recyclerView;
    ArrayList<NotificationData> notifications=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);
        back=findViewById(R.id.back);
        recyclerView=findViewById(R.id.not_recycler);

        recyclerView.setHasFixedSize(true);
        loadData();
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
                NotificationAdapter adapter=new NotificationAdapter(NotificationPageActivity.this,notifications);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}