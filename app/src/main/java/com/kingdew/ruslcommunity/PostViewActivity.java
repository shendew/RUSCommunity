package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jsibbold.zoomage.ZoomageView;
import com.kingdew.ruslcommunity.Models.PostData;
import com.skydoves.elasticviews.ElasticImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PostViewActivity extends AppCompatActivity {
    String id;
    DatabaseReference db;
    TextView topbar_title,title,body,auther;
    ImageView main_image;
    ElasticImageView back,delete;
    PostData data;
    boolean access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        db=FirebaseDatabase.getInstance().getReference("Posts");
        initView();
        delete.setVisibility(View.INVISIBLE);

        if (savedInstanceState ==null){
            Bundle extras=getIntent().getExtras();
            if (extras==null){
                showNoData();
            }else {
                id=extras.getString("id");
                access=extras.getBoolean("auth");
                data=new PostData(
                        extras.getString("auther"),
                        extras.getString("id"),
                        extras.getString("title"),
                        extras.getString("url"),
                        extras.getString("user"),
                        extras.getString("desc")
                );
                loadData();
            }
        }else {
            id=savedInstanceState.getSerializable("id").toString();
        }

        if (access){
            delete.setVisibility(View.VISIBLE);
        }else {
            delete.setVisibility(View.INVISIBLE);
        }

        back.setOnClickListener(view -> {
            finish();
        });
        main_image.setOnClickListener(view -> {
            Dialog dialog=new Dialog(PostViewActivity.this);
            dialog.setContentView(R.layout.view_image_popup);
            ImageView imageView=dialog.findViewById(R.id.image);
            ImageView close=dialog.findViewById(R.id.close);

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            Glide.with(getApplicationContext()).load(data.getUrl()).centerCrop().into(imageView);


            close.setOnClickListener(view1 -> {
                dialog.dismiss();
            });

            dialog.show();
        });
        delete.setOnClickListener(view -> {
            deleteValue();
        });

    }

    private void deleteValue() {

        Query df=FirebaseDatabase.getInstance().getReference("Posts").orderByChild("id").equalTo(data.getId());
        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    if (ds.child("id").exists()){
                        ds.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PostViewActivity.this, ""+ds.child("id").getValue(String.class), Toast.LENGTH_SHORT).show();
                                Toast.makeText(PostViewActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostViewActivity.this, "Not Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(PostViewActivity.this, "else", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PostViewActivity.this, "full error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView() {
        topbar_title=findViewById(R.id.topbar_title);
        back=findViewById(R.id.back);
        main_image=findViewById(R.id.main_image);
        title=findViewById(R.id.title);
        body=findViewById(R.id.body);
        auther=findViewById(R.id.auther);
        delete=findViewById(R.id.delete);
    }

    private void loadData() {
        topbar_title.setText(data.getTitle());
        Glide.with(getApplicationContext()).load(data.getUrl()).centerCrop().into(main_image);
        title.setText(data.getTitle());
        body.setText(data.getDesc());
        auther.setText("by "+data.getAuther());
    }

    private void showNoData() {
    }


}