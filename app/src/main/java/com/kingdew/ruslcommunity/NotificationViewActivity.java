package com.kingdew.ruslcommunity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kingdew.ruslcommunity.Models.PostData;
import com.skydoves.elasticviews.ElasticImageView;

import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.OnClickATagListener;

public class NotificationViewActivity extends AppCompatActivity {
    ImageView image;
    TextView title;
    HtmlTextView desc;
    ElasticImageView back;
    String idd,titlee,descc,imagee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        image=findViewById(R.id.image);
        title=findViewById(R.id.title);
        desc=findViewById(R.id.desc);
        back=findViewById(R.id.back);
        desc.setMovementMethod(LinkMovementMethod.getInstance());

        if (savedInstanceState ==null){
            Bundle extras=getIntent().getExtras();
            if (extras==null){

            }else {
                idd=extras.getString("id");
                titlee=extras.getString("title");
                descc=extras.getString("desc");
                imagee=extras.getString("image");


                Glide.with(getApplicationContext()).load(imagee).centerCrop().into(image);
                title.setText(titlee);
                desc.setHtml(descc);



            }}

        desc.setOnClickATagListener(new OnClickATagListener() {
            @Override
            public boolean onClick(View widget, String spannedText, @Nullable String href) {

                Toast.makeText(NotificationViewActivity.this, "if link doesn't work\npaste the link and press \"Enter/Go\" on another browser", Toast.LENGTH_LONG).show();
                return false;
            }
        });


        back.setOnClickListener(view -> {
            finish();

        });

    }
}