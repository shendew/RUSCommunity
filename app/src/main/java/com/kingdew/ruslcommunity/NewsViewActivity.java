package com.kingdew.ruslcommunity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skydoves.elasticviews.ElasticButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.OnClickATagListener;

public class NewsViewActivity extends AppCompatActivity {

    String url;
    ProgressDialog dialog;
    ElasticButton open_in;
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        open_in=findViewById(R.id.openin);
        web=findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);

        dialog=new ProgressDialog(this);

        url=getIntent().getStringExtra("url").toString();
        if (url != null){
            dialog.show();
            web.loadUrl(url);
            web.setWebViewClient(new WebViewClient(){

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    dialog.show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    dialog.dismiss();
                }
            });

        }else {
            Toast.makeText(this, "Process Error", Toast.LENGTH_SHORT).show();
        }

        open_in.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {

        if (web.canGoBack()){
            web.goBack();
        }else {
            super.onBackPressed();
        }
    }
}