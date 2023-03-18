package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingdew.ruslcommunity.Adapters.PostAdapter;
import com.kingdew.ruslcommunity.Models.PostData;
import com.skydoves.elasticviews.ElasticFloatingActionButton;
import com.skydoves.elasticviews.ElasticImageView;

import java.util.ArrayList;

public class PostPageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ElasticImageView back,myposts;
    ArrayList<PostData> array=new ArrayList<>();
    ElasticFloatingActionButton addPost;

    ShimmerFrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);



        initView();
        layout.startShimmerAnimation();
        setupRecyclerView();

        layout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        back.setOnClickListener(view -> {
            finish();
        });
        addPost.setOnClickListener(view -> {
            startActivity(new Intent(this,AddPostActivity.class));
        });
        myposts.setOnClickListener(view -> {
            startActivity(new Intent(this,MyPostsActivity.class));
        });
    }

    private void setupRecyclerView() {

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        FirebaseDatabase.getInstance().getReference("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!array.isEmpty()){
                    array.clear();
                }
                for (DataSnapshot item: snapshot.getChildren()){

                    array.add(new PostData(
                            item.child("auther").getValue(String.class),
                            item.child("id").getKey(),
                            item.child("title").getValue(String.class),
                            item.child("url").getValue(String.class),
                            item.child("user").getValue(String.class),
                            item.child("desc").getValue(String.class)
                    ));
                }

                PostAdapter adapter=new PostAdapter(PostPageActivity.this,array,"postpage");
                recyclerView.setAdapter(adapter);
                layout.stopShimmerAnimation();
                layout.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

    private void initView() {
        recyclerView=findViewById(R.id.post_page_rView);
        back=findViewById(R.id.back);
        addPost=findViewById(R.id.floatadd);
        myposts=findViewById(R.id.myposts);
        layout=findViewById(R.id.shmm);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}