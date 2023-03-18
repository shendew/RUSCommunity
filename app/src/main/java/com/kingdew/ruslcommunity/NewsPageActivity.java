package com.kingdew.ruslcommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.kingdew.ruslcommunity.Adapters.NewsAdapter;
import com.kingdew.ruslcommunity.Models.NewsData;
import com.skydoves.elasticviews.ElasticImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class NewsPageActivity extends AppCompatActivity {

    String mainurl="http://www.rjt.ac.lk/news/";
    String latestnewlink;
    ArrayList<NewsData> news=new ArrayList<>();
    RecyclerView recyclerView;
    ElasticImageView back;
    ShimmerFrameLayout shmm2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        recyclerView=findViewById(R.id.rv);
        back=findViewById(R.id.back);
        shmm2=findViewById(R.id.shmm2);
        recyclerView.setHasFixedSize(true);
        shmm2.startShimmerAnimation();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getLatestNews();
        back.setOnClickListener(view -> {
            finish();
        });
    }

    private void getLatestNews() {


        RequestQueue queue= Volley.newRequestQueue(this);


        StringRequest request=new StringRequest(Request.Method.GET, mainurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc= Jsoup.parse(response);
                Elements datat = doc.select("div[class=row align-items-center no-gutter blog-item rs-blog-grid1]");


                for (int i=0;i<100;i++){
//                    String title=data.eq(i).text();
//                    String tdate=doc.select("div.art-postmetadataheader").eq(i).text();
//                    Elements el=doc.select("div.art-post-inner").select("a");
//                    latestnewlink=el.eq(i).attr("href");
//                    String tbody=data.select("div.art-postcontent").eq(i).text();
//                    news.add(new NewsData(title,tdate,tbody,mainurl+latestnewlink));


                    Elements data=datat.eq(i);

                    String imglink=data.select("div[class=col-md-6]").eq(0).select("a").eq(0).select("img").attr("src");

                    String title=data.select("div[class=col-md-6]").eq(1).select("a").eq(0).text();
                    //title dd
                    String tdate=data.select("div[class=col-md-6]").eq(1).select("div[class=blog-content]").select("ul").text();
                    Elements el=doc.select("div.art-post-inner").select("a");
                    latestnewlink=el.eq(i).attr("href");
                    String link=data.select("div[class=col-md-6]").eq(0).select("a").attr("href");
                    String tbody="t body";
                    news.add(new NewsData(title,tdate,tbody,link));

                }
                shmm2.stopShimmerAnimation();
                shmm2.setVisibility(View.GONE);
                NewsAdapter adapter=new NewsAdapter(NewsPageActivity.this,news);
                recyclerView.setAdapter(adapter);





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        });
        queue.add(request);

    }
}