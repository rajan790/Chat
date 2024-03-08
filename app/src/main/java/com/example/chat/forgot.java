package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat.Activity.login;
import com.example.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class forgot extends AppCompatActivity {
EditText email;
Button reset;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        email=findViewById(R.id.email);
        reset=findViewById(R.id.reset);
        auth=FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty())
                {
                    Toast.makeText(forgot.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                    {
                        email.setError("Invalid Email");
                        Toast.makeText(forgot.this, "Email not valid", Toast.LENGTH_SHORT).show();
                    }
                else
                {
                    auth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task)
                        {
                            boolean check=!task.getResult().getSignInMethods().isEmpty();
                            if(!check)
                            {
                                Toast.makeText(forgot.this, "User not exist", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(forgot.this, "Resent Link sent on your Email", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
    }
}