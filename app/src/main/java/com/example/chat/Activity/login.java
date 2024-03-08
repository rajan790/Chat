package com.example.chat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chat.R;
import com.example.chat.custom_loading;
import com.example.chat.user_profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class login extends AppCompatActivity {
private int visibility_check=0;
Button login;
TextView lsignup,forgot;
EditText lemail,lpass;
FirebaseAuth firebaseAuth;
ImageButton passoff;
    Task<DataSnapshot> databaseReference;
Handler handler = new Handler();
private custom_loading loading ;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lsignup=findViewById(R.id.lsignup);
        login=findViewById(R.id.login);
        forgot=findViewById(R.id.forgot);
        lpass=findViewById(R.id.lpass);
        lemail=findViewById(R.id.lemail);
        passoff=findViewById(R.id.passoff);
//        loading= new custom_loading(this);
        RotateAnimation rotate = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        custom_loading custom_loading=new custom_loading(login.this,"ls");
//        progressDialog=new ProgressDialog(this);
//        progressDialog.setMessage("Please Wait...");
        firebaseAuth=FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
//
                String email=lemail.getText().toString();
                String pass=lpass.getText().toString();
                ImageView imageView=custom_loading.findViewById(R.id.profile_image);
                rotate.setDuration(1000);
                rotate.setRepeatCount(-1);
                rotate.setInterpolator(new LinearInterpolator());
                imageView.setAnimation(rotate);
                if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(pass))
                {
                    Toast.makeText(login.this, "Fill both the Fields", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {

                    lemail.setError("Invalid Email");
                    Toast.makeText(login.this, "Email not valid", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<6)
                {
                    lpass.setError("Invalid Password");
                    Toast.makeText(login.this, "Password Must be more than 6 digit", Toast.LENGTH_SHORT).show();
                }
                else
                {

                        custom_loading.show();
                        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                                        custom_loading.dismiss();
                                        String S=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        get(S);
                                        startActivity(new Intent(login.this, Home.class));
                                        finish();
                                    }
                                    else
                                    {
                                        custom_loading.dismiss();
                                        Toast.makeText(login.this, "Verify Your Email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    custom_loading.dismiss();
                                    Toast.makeText(login.this, "Account not found", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                }
            }
        });
        passoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(visibility_check==0)
                {
                    passoff.setImageResource(R.drawable.ic_baseline_visibility_24);
                    lpass.setTransformationMethod(null);
                    lpass.setSelection(lpass.length());
                    visibility_check=1;
                }
                else
                {
                    passoff.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    lpass.setTransformationMethod(new PasswordTransformationMethod());
                    lpass.setSelection(lpass.length());
                    visibility_check=0;
                }
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(login.this, com.example.chat.forgot.class));
            }
        });
        lsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(login.this, Signup.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
    public void get(String id)
    {
        HashMap user = new HashMap();
        user.put("verify",1);
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(id).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {

                }
            }
        });
    }
}