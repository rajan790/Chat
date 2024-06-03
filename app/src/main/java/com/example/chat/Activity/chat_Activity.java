package com.example.chat.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.Adapter.MessagesAdapter;
import com.example.chat.CropperActivity;
import com.example.chat.ModelClass.Messages;
import com.example.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.view.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


public class chat_Activity extends AppCompatActivity {

    String ReciverImage,ReciverUID,ReciverName,SenderUID;
    CircleImageView profileImage;
    TextView receiver_name;
    FirebaseDatabase database;
    RecyclerView message_adapter;
    ImageView backbtn;
    ImageView background;
    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    ArrayList<Messages> messagesArrayList;
    public static String simage;
    public static String rimage;
    MessagesAdapter adapter;
    ImageView sendbtn;
    EditText editMessage;
    ImageButton change;
    int k=0;
    String senderRoom,reciverRoom;
    ActivityResultLauncher<String> mGetContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        backbtn=findViewById(R.id.backbtn);
        change=findViewById(R.id.change);
        background=findViewById(R.id.background);
        messagesArrayList=new ArrayList<>();
        message_adapter=findViewById(R.id.message_adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        message_adapter.setLayoutManager(linearLayoutManager);
        adapter=new MessagesAdapter(chat_Activity.this,messagesArrayList,message_adapter);
        message_adapter.setAdapter(adapter);
        database=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        ReciverName=getIntent().getStringExtra("name");
        ReciverImage=getIntent().getStringExtra("ReciverImage");
        ReciverUID=getIntent().getStringExtra("uid");
        profileImage=findViewById(R.id.profile_image);
        sendbtn=findViewById(R.id.sendbtn);
        editMessage=findViewById(R.id.edittextMessage);
        Picasso.get().load(ReciverImage).into(profileImage);
        receiver_name=findViewById(R.id.receiver_name);
        receiver_name.setText(" "+ReciverName);
        SenderUID=firebaseAuth.getUid();
        senderRoom=SenderUID+ReciverUID;
        reciverRoom=ReciverUID+SenderUID;
        DatabaseReference reference= database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference= database.getReference().child("chats").child(senderRoom).child("messages");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Messages messages=dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                k=messagesArrayList.size();
                message_adapter.smoothScrollToPosition(messagesArrayList.size());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        });

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                simage=snapshot.child("imageUri").getValue().toString();
                rimage=ReciverImage;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mGetContent.launch("image/*");
            }
        });
        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(chat_Activity.this, CropperActivity.class);
                intent.putExtra("DATA",result.toString());
                startActivityForResult(intent,101);
            }
        });
        editMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editMessage.getText().toString().trim().length()==0)
                {

                }
                else
                {

                }
            }
            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });
        editMessage.setMaxHeight(dptopix(90));
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(chat_Activity.this, Home.class));
                finish();
            }
        });
        RelativeLayout relativeLayout;
        relativeLayout=findViewById(R.id.mainlayout);
        ViewTreeObserver.OnGlobalLayoutListener keyboardVisibiltyListener =new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int heightDiff=relativeLayout.getRootView().getHeight()- relativeLayout.getHeight();
                boolean isKeyboardOpen= heightDiff > dptopix(200);

                    message_adapter.smoothScrollToPosition(k);
                }

        };
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardVisibiltyListener);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_Activity.this,large_image.class);
                intent.putExtra("profile",rimage);
                startActivity(intent);
            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=editMessage.getText().toString();
                if(message.isEmpty())
                {
                    Toast.makeText(chat_Activity.this, "Please enter valid message", Toast.LENGTH_SHORT).show();
                    return;
                }
                message_adapter.smoothScrollToPosition(message_adapter.getBottom());
                editMessage.setText("");
                String display=message;
                if(message.length()>10)
                {
                    display=message.substring(0,10)+"........";
                }
                database.getReference().child("user").child(Objects.requireNonNull(firebaseAuth.getUid())).child("status").setValue(display);
                database.getReference().child("user").child(ReciverUID).child("status").setValue(display);
//                Date date = new Date();
                Calendar calendar =Calendar.getInstance();
//                SimpleDateFormat mdformat =new SimpleDateFormat("HH:mm");
//                String time=mdformat.format(calendar.getTime());
                String hour=String.valueOf(calendar.get(Calendar.HOUR));
                if(hour.equals("0"))
                {
                    hour="12";
                }

                String minute;
                if (String.valueOf(calendar.get(Calendar.MINUTE)).length()==1)
                {
                    minute= "0"+String.valueOf(calendar.get(Calendar.MINUTE));
                }
                else
                {
                    minute= String.valueOf(calendar.get(Calendar.MINUTE));
                }
                int am_pm= calendar.get(Calendar.AM_PM);
                String ap;
                if (am_pm==0)
                {
                    ap="am";
                }
                else
                {
                    ap="pm";
                }
                Messages messages =new Messages(message,firebaseAuth.getUid(),hour+":"+minute+" "+ap);

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child(reciverRoom)
                                        .child("messages")
                                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        });
                            }
                        });
            }
        });
    }

    public int dptopix(int dp)
    {
        int width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,chat_Activity.this.getResources().getDisplayMetrics());
        return width;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1 && requestCode==101)
        {
            String result=data.getStringExtra("RESULT");
            Uri imageUri;
            if(data!=null)
            {
                imageUri= Uri.parse(result);
                background.setImageURI(imageUri);
            }
        }
    }
}