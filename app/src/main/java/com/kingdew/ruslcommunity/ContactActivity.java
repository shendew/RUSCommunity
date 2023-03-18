package com.kingdew.ruslcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.elasticviews.ElasticImageView;

public class ContactActivity extends AppCompatActivity {
    ElasticImageView back;
    TextView gtel1,gtel2,gtel3,gem,vcstele1,vcstele2,vcsema1,vcsema2,regtel1,regtel2,regema;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        back=findViewById(R.id.back);
        initView();




        back.setOnClickListener(view -> {
            finish();
        });
    }



    private void initView() {
        gtel1=findViewById(R.id.gtel1);
        gtel2=findViewById(R.id.gtel2);
        gtel3=findViewById(R.id.gtel3);
        gem=findViewById(R.id.gem);
        vcstele1=findViewById(R.id.vcstele1);
        vcstele2=findViewById(R.id.vcstele2);
        vcsema1=findViewById(R.id.vcsema1);
        vcsema2=findViewById(R.id.vcsema2);
        regtel1=findViewById(R.id.regtel1);
        regtel2=findViewById(R.id.regtel2);
        regema=findViewById(R.id.regema);
    }

    public void onClick(View view) {
        int id=view.getId();

        switch (id){
            case R.id.gtel1:
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0252266645")));
                break;
            case R.id.gtel2:
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0252266646")));
                break;
            case R.id.gtel3:
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0252266650")));
                break;
            case R.id.gem:
                startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.fromParts("mailto","info@rjt.ac.lk",null)));
                break;
            case R.id.vcsema1:
                startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.fromParts("mailto","vc@rjt.ac.lk",null)));

                break;
            case R.id.vcsema2:
                startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.fromParts("mailto","vcrusl@yahoo.com",null)));

                break;
            case R.id.vcstele1:
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0252266644")));

                break;
            case R.id.vcstele2:
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0252266512")));

                break;
            case R.id.regtel1:
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0252266511")));

                break;
            case R.id.regtel2:
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0252266511")));

                break;
            case R.id.regema:
                startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.fromParts("mailto"," regofffice@rjt.ac.lk",null)));

                break;
            default:
                break;
        }
    }
}