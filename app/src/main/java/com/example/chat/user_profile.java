package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.Activity.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_profile extends AppCompatActivity {
String image,name;
ImageView profile,backbtn;
TextView Name,logout;
LinearLayout l2;
CircleImageView imageView;
FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        image=getIntent().getStringExtra("image");
        name=getIntent().getStringExtra("name");
        profile=findViewById(R.id.profile_image);
        Name=findViewById(R.id.Name);
        l2=findViewById(R.id.l2);
        logout=findViewById(R.id.logout);
        backbtn=findViewById(R.id.backbtn);
        name=name.substring(0,1).toUpperCase()+name.substring(1);
        Name.setText(name);
        Picasso.get().load(image).into(profile);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                custom_loading dialog = new custom_loading(user_profile.this,"logout");
                dialog.show();
                TextView yesbtn= dialog.findViewById(R.id.yesbtn);
                TextView nobtn=dialog.findViewById(R.id.nobtn);
                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        firebaseAuth.signOut();
                        Intent intent = new Intent(user_profile.this,com.example.chat.Activity.login.class);
                        startActivity(intent);
                    }
                });
                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_profile.this, Home.class));
                finish();
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}