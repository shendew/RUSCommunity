package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.text.Editable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kusu.library.LoadingButton;
import com.skydoves.elasticviews.ElasticImageView;

public class LivePannel extends AppCompatActivity {
    SwitchCompat switch1;
    DatabaseReference db;
    EditText title,teams,score;
    LoadingButton update;
    String stitle,steams,sscore;
    ElasticImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_pannel);
        switch1=findViewById(R.id.switch1);
        title=findViewById(R.id.title);
        teams=findViewById(R.id.teams);
        score=findViewById(R.id.score);
        update=findViewById(R.id.update);
        back=findViewById(R.id.back);

        db=FirebaseDatabase.getInstance().getReference("HiddenTabs").child("MatchTab");
        loadData();

        switch1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("visibility").getValue(Boolean.class) == false){
                            snapshot.getRef().child("visibility").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isCanceled()){
                                        Toast.makeText(LivePannel.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else {
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("visibility").getValue(Boolean.class)){
                            snapshot.getRef().child("visibility").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isCanceled()){
                                        Toast.makeText(LivePannel.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        update.setOnClickListener(view -> {
            if(!title.getText().toString().equals(stitle)){
                updateData(title.getText().toString(),"title");
            }
            if (!teams.getText().toString().equals(steams)){
                updateData(teams.getText().toString(),"teams");
            }
            if (!score.getText().toString().equals(sscore)){
                updateData(score.getText().toString(),"score");
            }
        });


    }

    private void updateData(String data, String con) {
        if (con.equals("title")){
            db.child("title").setValue(data);
        }else if (con.equals("teams")){
            db.child("teams").setValue(data);
        }else {
            db.child("score").setValue(data);
        }

    }

    private void loadData() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stitle=snapshot.child("title").getValue(String.class);
                steams=snapshot.child("teams").getValue(String.class);
                sscore=snapshot.child("score").getValue(String.class);
                if(snapshot.child("visibility").getValue(Boolean.class)){
                    switch1.setChecked(true);
                }else {
                    switch1.setChecked(false);
                }
                title.setText(stitle);
                teams.setText(steams);
                score.setText(sscore);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}