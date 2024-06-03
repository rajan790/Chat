package com.example.chat.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.chat.CropperActivity;
import com.example.chat.R;
import com.example.chat.ModelClass.Users;
import com.example.chat.custom_loading;
import com.example.chat.forgot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {
Button signup;
custom_loading progressDialog;
ImageButton changeimage,spassoff,scpassoff;
CircleImageView profile_image;
EditText name,semail,spass,scpass;
FirebaseAuth firebaseAuth;
private int visibility_check=0;
FirebaseDatabase database;
FirebaseStorage storage;
Boolean create;
String imageURI;
TextView slogin;
Uri imageUri;
    boolean check;
    DatabaseReference reference;
    StorageReference storageReference;
    ActivityResultLauncher<String> mGetContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        slogin=findViewById(R.id.slogin);
        spassoff=findViewById(R.id.spassoff);
        scpassoff=findViewById(R.id.scpassoff);
        name=findViewById(R.id.Name);
        semail=findViewById(R.id.semail);
        spass=findViewById(R.id.spass);
        scpass=findViewById(R.id.scpass);
        signup=findViewById(R.id.signup);
        changeimage=findViewById(R.id.imageButton1);
        profile_image=findViewById(R.id.profile_image);
//        progressDialog=new ProgressDialog(this);
//        progressDialog.setMessage("Please Wait...");
//        progressDialog.setCancelable(false);
        final String[] Token = new String[1];
        RotateAnimation rotate = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        progressDialog =new custom_loading(Signup.this,"ls");
        ImageView imageView=progressDialog.findViewById(R.id.profile_image);
        rotate.setDuration(1000);
        rotate.setRepeatCount(-1);
        rotate.setInterpolator(new LinearInterpolator());
        imageView.setAnimation(rotate);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                create=true;
                String email=semail.getText().toString();
                String pass=spass.getText().toString();
                String Name=name.getText().toString();
                String Cpass=scpass.getText().toString();
                String status="Hey There I'm Using Gossips";
                if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(pass)||TextUtils.isEmpty(Name)|| TextUtils.isEmpty(Cpass))
                {
                    Toast.makeText(Signup.this, "Fill all the Fields", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    semail.setError("Invalid Email");
                    Toast.makeText(Signup.this, "Email not valid", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(!pass.equals(Cpass))
                {
                    Toast.makeText(Signup.this, "Password does't Match", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(pass.length()<6)
                {
                    Toast.makeText(Signup.this, "Password Must be more than 6 digit", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    firebaseAuth.fetchSignInMethodsForEmail(email.toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task)
                        {
                            check=!task.getResult().getSignInMethods().isEmpty();
                            if(check)
                            {
                                Toast.makeText(Signup.this,"Account already register With\nthis Email", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else
                            {
                                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            signup.setEnabled(false);
                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        reference = database.getReference().child("user").child(Objects.requireNonNull(firebaseAuth.getUid()));
                                                        storageReference = storage.getReference().child("upload").child(firebaseAuth.getUid());
                                                        Users users;
                                                        if (imageUri != null) {
                                                            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri uri) {
                                                                                imageURI = uri.toString();
                                                                                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                                                                    @Override
                                                                                    public void onSuccess(String s) {
                                                                                        Token[0] = s;
                                                                                    }
                                                                                });
                                                                                LocalDate date= null;
                                                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                                                    date = LocalDate.now();
                                                                                }
//                                                                                Toast.makeText(Signup.this, ""+date, Toast.LENGTH_SHORT).show();
                                                                                Users users = new Users(Name, email, firebaseAuth.getUid(), imageURI, status, 0, Token[0]);
                                                                                work(users);

                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            String status = "Hey There I'm Using Gossips";
                                                            imageURI = "https://firebasestorage.googleapis.com/v0/b/chat-bf264.appspot.com/o/user.png?alt=media&token=42810325-7fdd-4b88-910e-85d2f65bf4ad";
                                                            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                                                @Override
                                                                public void onSuccess(String s) {
                                                                    Token[0] = s;
                                                                }
                                                            });
                                                            LocalDate date= null;
                                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                                date = LocalDate.now();
                                                            }
                                                            users = new Users(Name, email, firebaseAuth.getUid(), imageURI, status, 0, Token[0]);
                                                            work(users);
                                                        }
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Signup.this, "Error Occured", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(Signup.this, "Error Occured", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Signup.this, "error: "+e, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                });
                            }
                        }
                    });



        scpassoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(visibility_check==0)
                {
                    scpassoff.setImageResource(R.drawable.ic_baseline_visibility_24);
                    scpass.setTransformationMethod(null);
                    scpass.setSelection(scpass.length());
                    visibility_check=1;
                }
                else
                {
                    scpassoff.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    scpass.setTransformationMethod(new PasswordTransformationMethod());
                    scpass.setSelection(scpass.length());
                    visibility_check=0;
                }
            }
        });
        slogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,login.class));
            }
        });
        spassoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(visibility_check==0)
                {
                    spassoff.setImageResource(R.drawable.ic_baseline_visibility_24);
                    spass.setTransformationMethod(null);
                    spass.setSelection(spass.length());
                    visibility_check=1;
                }
                else
                {
                    spassoff.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    spass.setTransformationMethod(new PasswordTransformationMethod());
                    spass.setSelection(spass.length());
                    visibility_check=0;
                }
            }
        });
        changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
                mGetContent.launch("image/*");

            }
        });
        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(Signup.this, CropperActivity.class);
                intent.putExtra("DATA",result.toString());
                startActivityForResult(intent,101);
            }
        });
    }

    private void work(Users users)
    {
        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Signup.this, "Confirm your Email", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    signup.setEnabled(true);
                    name.setText("");
                    semail.setText("");
                    spass.setText("");
                    scpass.setText("");
                    profile_image.setImageResource(R.drawable.ic_baseline_person_24);
                    firebaseAuth.signOut();

                    startActivity(new Intent(getApplicationContext(),login.class));
                } else {
                    Toast.makeText(Signup.this, "Error in Creating ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1 && requestCode==101)
        {
            String result=data.getStringExtra("RESULT");
            if(result!=null)
            {
                imageUri= Uri.parse(result);
            }
            profile_image.setImageURI(imageUri);
        }
    }
}