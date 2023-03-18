package com.kingdew.ruslcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kusu.library.LoadingButton;
import com.skydoves.elasticviews.ElasticImageView;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    LoadingButton next,verify;
    EditText login_input,verifycode;
    ConstraintLayout mainLay;
    private String verificationId;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    LinearLayout nextLay,verifyLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        next=findViewById(R.id.button);
        login_input=findViewById(R.id.login_input);
        mainLay=findViewById(R.id.mainLay);
        verify=findViewById(R.id.verify);
        nextLay=findViewById(R.id.nextlay);
        verifyLay=findViewById(R.id.verifylay);
        verifycode=findViewById(R.id.verifyinput);

        verifyLay.setVisibility(View.INVISIBLE);
        nextLay.setVisibility(View.VISIBLE);


        verify.setOnClickListener(view -> {
//            if (!verifycode.getText().toString().isEmpty()){
//                verifyCode(verifycode.getText().toString());
//            }

            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();

        });

        next.setOnClickListener(view -> {
            String number = login_input.getText().toString();
            if (number.isEmpty()) {
                snackBar("Cannot be empty!");
            } else{
                next.showLoading();
                FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean check = false;
                        for (DataSnapshot item : snapshot.getChildren()) {
                            if (item.child("CONTACT_NUMBER").getValue(String.class).equals(number)) {
                                check = true;
                                break;

                            } else {
                                check = false;
                            }

                        }
                        if (check) {
                            loginProsses(number);
                        } else {
                            next.hideLoading();
                            snackBar("You're not registered,\nplease contact admin");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }
        });


    }

    private void loginProsses(String number) {
        String phone="+94"+number.substring(1);
        //send verification code

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                final String code=phoneAuthCredential.getSmsCode();
                verifyCode(code);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                snackBar("Verification Faild");
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                next.hideLoading();
                nextLay.setVisibility(View.INVISIBLE);
                verifyLay.setVisibility(View.VISIBLE);
                login_input.setEnabled(false);
                verificationId=s;
            }
        };
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(LoginActivity.this)
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void snackBar(String msg){

        Snackbar snackbar=Snackbar.make(mainLay,"", Snackbar.LENGTH_LONG);
        View customview=getLayoutInflater().inflate(R.layout.snack_bar,null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);

        // now change the layout of the snackbar
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        CardView go=customview.findViewById(R.id.go);
        TextView text=customview.findViewById(R.id.text);
        text.setText(msg);
        go.setOnClickListener(view1 -> {
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/mr_puse"));
            startActivity(intent);
            snackbar.dismiss();
        });
        snackbarLayout.addView(customview,0);
        snackbar.show();
    }

    private void verifyCode(String inputcode){
        if (inputcode!=null){
            verify.showLoading();
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,inputcode);

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                snackBar("Creditional Failed");
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mAuth=FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth!=null){
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

    }
}