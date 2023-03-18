package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kusu.library.LoadingButton;
import com.skydoves.elasticviews.ElasticImageView;

public class UserProfileActivity extends AppCompatActivity {

    TextView profile_name,profile_nic,profile_address,profile_dob;
    LoadingButton logout;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    ElasticImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initView();
        getData();
        logout.setOnClickListener(view -> {
            logout.showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(UserProfileActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

                    finish();
                    logout.hideLoading();
                }
            },2000);


        });
        back.setOnClickListener(view -> {
            startActivity(new Intent(this,HomeActivity.class));
            finish();

        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }

    private void getData() {
        FirebaseDatabase.getInstance().getReference("Users").child(user.getPhoneNumber().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address=snapshot.child("ADDRESS").getValue(String.class);
                profile_name.setText(snapshot.child("NAME").getValue(String.class));
                profile_nic.setText(snapshot.child("NIC").getValue().toString());
                profile_dob.setText(snapshot.child("DOB").getValue(String.class));
                profile_address.setText(address.replace(",",",\n"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        profile_name=findViewById(R.id.profile_name);
        profile_address=findViewById(R.id.profile_address);
        profile_nic=findViewById(R.id.profile_nic);
        profile_dob=findViewById(R.id.profile_dob);
        logout=findViewById(R.id.logout);
        back=findViewById(R.id.back);

    }


}