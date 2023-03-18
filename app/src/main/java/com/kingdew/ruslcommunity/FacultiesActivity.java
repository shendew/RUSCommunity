package com.kingdew.ruslcommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skydoves.elasticviews.ElasticImageView;

public class FacultiesActivity extends AppCompatActivity {

    CardView technology,agriculture,applied,management,medical,social;
    ElasticImageView back;
    TextView tectext,agritext,appsctext,mngtext,medtext,soctext;
    LinearLayout tecdropdown,agridropdown,appscdropdown,mngdropdown,meddropdown,socdropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);
        initView();

        back.setOnClickListener(view -> {
            finish();
        });


    }

    private void initView() {
        technology=findViewById(R.id.technology);
        agriculture=findViewById(R.id.agriculture);
        applied=findViewById(R.id.applied);
        management=findViewById(R.id.management);
        medical=findViewById(R.id.medical);
        social=findViewById(R.id.social);

        tectext=findViewById(R.id.tectext);
        agritext=findViewById(R.id.agritext);
        appsctext=findViewById(R.id.appsctext);
        mngtext=findViewById(R.id.mngtext);
        medtext=findViewById(R.id.medtext);
        soctext=findViewById(R.id.soctext);

        tecdropdown=findViewById(R.id.tecdropdown);
        agridropdown=findViewById(R.id.agridropdown);
        appscdropdown=findViewById(R.id.appscdropdown);
        mngdropdown=findViewById(R.id.mngdropdown);
        meddropdown=findViewById(R.id.meddropdown);
        socdropdown=findViewById(R.id.socdropdown);

        back=findViewById(R.id.back);
    }


    public void onclick(View view) {
        int id=view.getId();

        switch (id){
            case R.id.tectext:
                tecdropdown.setVisibility(View.VISIBLE);
                break;
            case R.id.agritext:
                agridropdown.setVisibility(View.VISIBLE);
                break;
            case R.id.appsctext:
                appscdropdown.setVisibility(View.VISIBLE);
                break;
            case R.id.mngtext:
                mngdropdown.setVisibility(View.VISIBLE);
                break;
            case R.id.medtext:
                meddropdown.setVisibility(View.VISIBLE);
                break;
            case R.id.soctext:
                socdropdown.setVisibility(View.VISIBLE);
                break;

            case R.id.tecdropdown:
                tecdropdown.setVisibility(View.GONE);
                break;
            case R.id.agridropdown:
                agridropdown.setVisibility(View.GONE);
                break;
            case R.id.mngdropdown:
                mngdropdown.setVisibility(View.GONE);
                break;
            case R.id.appscdropdown:
                appscdropdown.setVisibility(View.GONE);
                break;
            case R.id.meddropdown:
                meddropdown.setVisibility(View.GONE);
                break;
            case R.id.socdropdown:
                socdropdown.setVisibility(View.GONE);
                break;

        }
    }
}