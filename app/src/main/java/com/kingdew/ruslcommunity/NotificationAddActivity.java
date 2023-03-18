package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingdew.ruslcommunity.Models.NotificationData;
import com.kusu.library.LoadingButton;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationAddActivity extends AppCompatActivity{
    Spinner year,fac,event;
    EditText wtitle,wbody,wsimpbody;
    LoadingButton next,send;
    LinearLayout getdlay,getodeance;
    NotificationFilterTool tool;
    Dialog dialog;
    LoadingButton lb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_add);

        year=findViewById(R.id.year);
        fac=findViewById(R.id.fac);
        event=findViewById(R.id.event);
        wtitle=findViewById(R.id.title);
        wbody=findViewById(R.id.body);
        wsimpbody=findViewById(R.id.simpbody);
        next=findViewById(R.id.nextbtn);
        getdlay=findViewById(R.id.getdelay);
        getodeance=findViewById(R.id.getodeance);
        send=findViewById(R.id.sendbtn);

        tool=new NotificationFilterTool();

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.years_array,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        year.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,
                R.array.faculty_array,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        fac.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3=ArrayAdapter.createFromResource(this,
                R.array.event_array,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        event.setAdapter(adapter3);

        //year.setOnItemClickListener(getApplicationContext());



        next.setOnClickListener(view -> {
            next.showLoading();
            String title=wtitle.getText().toString();
            String simpbody=wsimpbody.getText().toString();
            String body=wbody.getText().toString();

            if (title.isEmpty()){
                wtitle.setError("Required");
                next.hideLoading();
            }else{
                if (simpbody.isEmpty()){
                    wsimpbody.setError("Required");
                    next.hideLoading();
                }else {
                    if (body.isEmpty()){
                        wbody.setError("Required");
                        next.hideLoading();
                    }else {
                        next.hideLoading();
                        getdlay.setVisibility(View.GONE);
                        getodeance.setVisibility(View.VISIBLE);
                    }
                }
            }

        });



        send.setOnClickListener(view -> {
            dialogShow();
        });
    }



    private void dialogShow(){
        dialog=new Dialog(NotificationAddActivity.this);

        dialog.setContentView(R.layout.send_not);

        lb=dialog.findViewById(R.id.confirm);
        TextView title=dialog.findViewById(R.id.title);
        TextView simpdesc=dialog.findViewById(R.id.simpdesc);
        TextView desc=dialog.findViewById(R.id.desc);
        TextView filter=dialog.findViewById(R.id.filter);

        title.setText(wtitle.getText().toString());
        simpdesc.setText(wsimpbody.getText().toString());
        desc.setText(wbody.getText().toString());
        String FILT=tool.NotificationFilterTool(year.getSelectedItem().toString(),fac.getSelectedItem().toString(),event.getSelectedItem().toString());
        filter.setText(FILT);

        lb.setOnClickListener(view -> {
            lb.showLoading();
            UpdateData(FILT);
        });
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        dialog.show();
    }

    private void UpdateData(String FILT){
        DatabaseReference dr=FirebaseDatabase.getInstance().getReference("Notifications");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    DatabaseReference ddr=dr.child(String.valueOf(snapshot.getChildrenCount()+1));
                    HashMap<String,String> data=new HashMap<>();
                    data.put("STATUS","newnot");
                    data.put("ID",String.valueOf(snapshot.getChildrenCount()+1));
                    data.put("TITLE",wtitle.getText().toString());
                    data.put("DESC",wbody.getText().toString());
                    data.put("NOT_DESC",wsimpbody.getText().toString());
                    data.put("IMAGE","img");
                    data.put("FILT",FILT);
                    ddr.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(NotificationAddActivity.this, "Notification  is running", Toast.LENGTH_SHORT).show();
                                lb.hideLoading();
                                dialog.dismiss();
                                finish();
                            }else {
                                lb.hideLoading();
                                Toast.makeText(NotificationAddActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });








            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}