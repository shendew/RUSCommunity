package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kingdew.ruslcommunity.Models.PostData;
import com.kusu.library.LoadingButton;
import com.skydoves.elasticviews.ElasticImageView;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddPostActivity extends AppCompatActivity {
    int PICK_IMAGE=10;
    ImageView addimage;
    ElasticImageView back;
    LoadingButton btn_add;
    EditText etitle,edesc;
    Uri imageURI;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String auther;
    String Durl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        back=findViewById(R.id.back);
        addimage=findViewById(R.id.add_image);
        btn_add=findViewById(R.id.btn_add);
        etitle=findViewById(R.id.e_title);
        edesc=findViewById(R.id.e_desc);

        auther=getAuther();


        back.setOnClickListener(view -> {
            finish();
        });
        addimage.setOnClickListener(view -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        });


        btn_add.setOnClickListener(view -> {
            String title=etitle.getText().toString();
            String desc=edesc.getText().toString();
            if (title.isEmpty()){
                etitle.setError("Required");
            }else {
                if (desc.isEmpty()){
                    edesc.setError("Required");
                }else {
                    btn_add.showLoading();
                    sendRequest(title,desc,imageURI,user.getUid());
                }
            }
        });
    }

    private void sendRequest(String title, String desc,Uri uri,String uid) {


        long time=System.currentTimeMillis();
        String timee=String.valueOf(time);



        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Gallery").child("PostPhotos").child(String.valueOf(timee)+".jpg");

        storageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Durl=String.valueOf(uri);
                            FirebaseDatabase.getInstance().getReference("Users").child(String.valueOf(user.getPhoneNumber())).child("NAME").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String auther=snapshot.getValue(String.class);

                                    Random rand = new Random();
                                    int id= rand.nextInt(1000);
                                    PostData post=new PostData();
                                    post.setAuther(auther);
                                    post.setTitle(title);
                                    post.setDesc(desc);
                                    post.setUrl(Durl);
                                    post.setId(String.valueOf(id));
                                    post.setUser(uid);

                                    ss(post);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPostActivity.this, "get error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });






    }

    private void ss(PostData post){
        RequestQueue queue=Volley.newRequestQueue(this);
        String url="https://script.google.com/macros/s/AKfycbwbKzzlrOJTdk7v4hZzjioRW4iYgKzEDpuarnIXPT3usodASRzXYiaJeTlpWShKgVoG/exec";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddPostActivity.this, "You're Post is under review", Toast.LENGTH_SHORT).show();
                        etitle.setText("");
                        edesc.setText("");
                        addimage.setImageDrawable(getDrawable(R.drawable.ic_baseline_add_photo_alternate_24));
                        btn_add.hideLoading();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPostActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("action","addPost");
                params.put("id",post.getId());
                params.put("title",post.getTitle());
                params.put("desc",post.getDesc());
                params.put("url",post.getUrl());
                params.put("user",post.getUser());
                params.put("auther",post.getAuther());

                return params;
            }
        };
        int socketTimeout=50000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(socketTimeout,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        queue.add(stringRequest);
    }

    private String getAuther() {

        FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("NAME").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                auther=snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return auther;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==PICK_IMAGE){
            imageURI=data.getData();
            addimage.setImageURI(imageURI);
        }
    }
}