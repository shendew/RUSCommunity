package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingdew.ruslcommunity.Adapters.EventAdapter;
import com.kingdew.ruslcommunity.Adapters.NewsAdapter;
import com.kingdew.ruslcommunity.Adapters.PostAdapter;
import com.kingdew.ruslcommunity.Adapters.SliderAdapter;
import com.kingdew.ruslcommunity.Models.EventData;
import com.kingdew.ruslcommunity.Models.NewsData;
import com.kingdew.ruslcommunity.Models.PostData;
import com.kingdew.ruslcommunity.Models.SliderData;
import com.kingdew.ruslcommunity.Services.NotificationSerivice;
import com.skydoves.elasticviews.ElasticImageView;
import com.smarteist.autoimageslider.SliderView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    SliderView sliderView;
    RecyclerView postBar,newsRecV;
    TextView post_seemore,hitext,morenews;
    ArrayList<PostData> array=new ArrayList<>();
    ElasticImageView home_user,home_notifications,home_menu,home_pannel;
    String phoneNumber;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ShimmerFrameLayout layout,layout2;
    ArrayList<NewsData> news=new ArrayList<>();
    RelativeLayout hiddentab;


    FirebaseUser user;
    String latestnewlink;
    String mainurl="http://www.rjt.ac.lk/news/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initial();
        setSlider();
        setupRecyclerView();
        post_seemore.setVisibility(View.INVISIBLE);
        layout2.startShimmerAnimation();
        layout.startShimmerAnimation();
        postBar.setVisibility(View.INVISIBLE);


        //user =FirebaseAuth.getInstance().getCurrentUser();
        //phoneNumber=user.getPhoneNumber();


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel= new NotificationChannel("RUSTLE","RUSL", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            NotificationChannel defaultvhannel= new NotificationChannel("default","Default", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager defaultmanager =getSystemService(NotificationManager.class);
            defaultmanager.createNotificationChannel(defaultvhannel);

        }
        newsRecV.setHasFixedSize(true);
        newsRecV.setLayoutManager(new LinearLayoutManager(this));

        //getData();
        getLatestNews();
        openHiddenTabs();
        getEvents();
        //checkData();


        navigationView.bringToFront();
        drawerLayout.closeDrawer(GravityCompat.START);
        home_menu.setOnClickListener(view -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });


        post_seemore.setOnClickListener(view -> {
            startActivity(new Intent(this,PostPageActivity.class));
        });
        home_user.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,UserProfileActivity.class));
            finish();

        });
        home_notifications.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,NotificationPageActivity.class));
        });
        morenews.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,NewsPageActivity.class));
        });
        home_pannel.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(HomeActivity.this,DashBoardActivity.class));
        });





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (id){
                    case R.id.nav_home:
                        break;
                    case R.id.nav_faculties:
                        startActivity(new Intent(getApplicationContext(),FacultiesActivity.class));
                        break;
                    case R.id.nav_posts:
                        startActivity(new Intent(getApplicationContext(),PostPageActivity.class));
                        break;
                    case R.id.nav_notifications:
                        startActivity(new Intent(getApplicationContext(),NotificationPageActivity.class));
                        break;
                    case R.id.nav_contact:
                        startActivity(new Intent(getApplicationContext(),ContactActivity.class));
                        break;
                    case  R.id.navi_about:
                        startActivity(new Intent(getApplicationContext(),AboutActivity.class));
                        break;
                    case R.id.nav_myposts:
                        startActivity(new Intent(getApplicationContext(),MyPostsActivity.class));
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void checkData() {
        FirebaseDatabase.getInstance().getReference("Users").child(user.getPhoneNumber()).child("ROLE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class).equals("media_unit")){
                    home_pannel.setVisibility(View.VISIBLE);

                }else {
                    home_pannel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getEvents() {
        RecyclerView eventRview=findViewById(R.id.eventRview);
        ArrayList<EventData> events=new ArrayList<>();
        eventRview.setHasFixedSize(true);
        eventRview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        DatabaseReference dr=FirebaseDatabase.getInstance().getReference("Events");


        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!events.isEmpty()){
                    events.clear();
                }
                for (DataSnapshot event:snapshot.getChildren()){
                    String title=event.child("title").getValue(String.class);
                    String image=event.child("image").getValue(String.class);
                    int id=event.child("id").getValue(Integer.class);

                    //events.add(new EventData(title,image,true));


                    try {
                        String d=event.child("joined").child(user.getUid()).getValue(String.class);

                            if (d.equals(user.getPhoneNumber())){

                                events.add(new EventData(id,title,image,true));
                            }else{
                                events.add(new EventData(id,title,image,false));

                            }


                    }catch (Exception e){
                        events.add(new EventData(id,title,image,false));
                    }

                }

                EventAdapter adapter=new EventAdapter(HomeActivity.this,events);
                eventRview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void openHiddenTabs() {
        TextView matchtitle,teams,score;
        matchtitle=findViewById(R.id.matchtitle);
        teams=findViewById(R.id.teams);
        score=findViewById(R.id.score);

        FirebaseDatabase.getInstance().getReference("HiddenTabs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    if (ds.child("visibility").getValue(Boolean.class).equals(true)){
                        String id= ds.child("id").getValue(String.class);
                        String stitle= ds.child("title").getValue(String.class);
                        String steams= ds.child("teams").getValue(String.class);
                        String sscore= ds.child("score").getValue(String.class);
                        if (id.equals("MatchTab")){
                            hiddentab.setVisibility(View.VISIBLE);
                            matchtitle.setText(stitle);
                            teams.setText(steams);
                            score.setText(sscore);

                        }
                    }else {
                        String id= ds.child("id").getValue(String.class);
                        if (id.equals("MatchTab")){
                            hiddentab.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLatestNews() {
        RequestQueue queue= Volley.newRequestQueue(this);

        StringRequest request=new StringRequest(Request.Method.GET, mainurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc= Jsoup.parse(response);
                //Elements data = doc.select("span.art-postheader");
                Elements datat=doc.select("div[class=row align-items-center no-gutter blog-item rs-blog-grid1]");


                for (int i=0;i<5;i++){
                    Elements data=datat.eq(i);

                    String imglink=data.select("div[class=col-md-6]").eq(0).select("a").eq(0).select("img").attr("src");
                    String title=data.select("div[class=col-md-6]").eq(1).select("a").eq(0).text();
                    //title dd
                    String tdate=data.select("div[class=col-md-6]").eq(1).select("div[class=blog-content]").select("ul").text();
                    Elements el=doc.select("div.art-post-inner").select("a");
                    latestnewlink=el.eq(i).attr("href");
                    String link=data.select("div[class=col-md-6]").eq(0).select("a").attr("href");
                    String tbody="t body";
                    news.add(new NewsData(title,tdate,imglink,link));
                }

                NewsAdapter adapter=new NewsAdapter(HomeActivity.this,news);
                newsRecV.setAdapter(adapter);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        });
        queue.add(request);

    }

//    private void getData() {
//        FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                hitext.setText("Welcome "+snapshot.child("NAME").getValue(String.class));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void initial() {
        sliderView = findViewById(R.id.slider);
        postBar=findViewById(R.id.home_post_bar);
        post_seemore=findViewById(R.id.post_seemore);
        home_user=findViewById(R.id.home_user);
        home_notifications=findViewById(R.id.home_notifications);
        home_menu=findViewById(R.id.home_menu);
        hitext=findViewById(R.id.hitext);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navi_view);
        layout2=findViewById(R.id.shmm2);
        layout=findViewById(R.id.shmm);
        newsRecV=findViewById(R.id.newsRecV);
        morenews=findViewById(R.id.morenews);
        hiddentab=findViewById(R.id.hiddentab);
        home_pannel=findViewById(R.id.home_pannel);

    }


    private void setupRecyclerView() {

        postBar.setHasFixedSize(true);

        postBar.setLayoutManager(new LinearLayoutManager(
                HomeActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false));

        FirebaseDatabase.getInstance().getReference("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!array.isEmpty()){
                    array.clear();
                }
                for (DataSnapshot item: snapshot.getChildren()){

                    array.add(new PostData(
                            item.child("auther").getValue(String.class),
                            item.child("id").getValue(String.class),
                            item.child("title").getValue(String.class),
                            item.child("url").getValue(String.class),
                            item.child("user").getValue(String.class),
                            item.child("desc").getValue(String.class)
                    ));
                }

                PostAdapter adapter=new PostAdapter(HomeActivity.this,array,"home");
                postBar.setAdapter(adapter);
                layout.stopShimmerAnimation();
                layout2.stopShimmerAnimation();
                post_seemore.setVisibility(View.VISIBLE);
                layout.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                postBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

    protected void setSlider(){
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.startAutoCycle();

        FirebaseDatabase.getInstance().getReference("Sliders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!sliderDataArrayList.isEmpty()){
                    sliderDataArrayList.clear();
                }
                for (DataSnapshot ds:snapshot.getChildren()){
                    sliderDataArrayList.add(new SliderData(ds.child("url").getValue(String.class),ds.child("title").getValue(String.class),ds.child("id").getValue(Integer.class)));
                }
                SliderAdapter adapter = new SliderAdapter(HomeActivity.this, sliderDataArrayList);

                sliderView.setSliderAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        if (!isMyServiceRunning(NotificationSerivice.class)){
            try {
                startService(new Intent(this, NotificationSerivice.class));

            }catch (Exception e){
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();

            }
        }

        super.onStart();
    }
}