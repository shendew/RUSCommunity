package com.kingdew.ruslcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.android.gms.maps.MapView;
import com.skydoves.elasticviews.ElasticImageView;

public class AboutActivity extends AppCompatActivity {
    ImageView mapView;
    ElasticImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mapView=findViewById(R.id.mapView);
        back=findViewById(R.id.back);

        mapView.setOnClickListener(view ->{
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/H635DSnBUFArp6kY7"));
            startActivity(intent);
        });

        back.setOnClickListener(view -> {
            finish();
        });
    }
}