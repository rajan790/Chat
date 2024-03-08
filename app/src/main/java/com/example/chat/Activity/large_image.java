package com.example.chat.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.R;
import com.squareup.picasso.Picasso;
public class large_image extends AppCompatActivity {
    String profile;
    ImageView imageView;
    TextView noprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);
        profile=getIntent().getStringExtra("profile");
        imageView = findViewById(R.id.large_image);
        noprofile=findViewById(R.id.noprofile);
        if(profile.equals("https://firebasestorage.googleapis.com/v0/b/chat-bf264.appspot.com/o/usericon6.png?alt=media&token=7501dd06-4d2b-4a89-81ab-28dd18af3b20"))
        {
            imageView.getLayoutParams().height=0;
            imageView.requestLayout();
        }
        else {
            noprofile.setText("");
            Picasso.get().load(profile).into(imageView);
        }
    }
}