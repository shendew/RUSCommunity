package com.kingdew.ruslcommunity.Helpers;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoleChecker {

    boolean ret=true;

    public boolean RChecker(){

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("ROLE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.getValue(String.class)!= "media_unit"){
                        ret=false;
                    }else {
                        ret=true;
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return ret;
    }
}
