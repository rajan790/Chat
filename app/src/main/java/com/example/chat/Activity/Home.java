package com.example.chat.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chat.CropperActivity;
import com.example.chat.R;
import com.example.chat.Adapter.UserAdapter;
import com.example.chat.ModelClass.Users;
import com.example.chat.user_profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class Home extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ImageView profile;
    ArrayList<Users> usersArrayList;
    SearchView searchView;
    TextView logo;
    ActivityResultLauncher<String> mGetContent;
    private static final int PICK_VIDEO_REQUEST = 1;
    final String[] up = new String[1];
    final String[] un = new String[1];
    RecyclerView mainUserRecyclerView;
    RelativeLayout layout1;
    UserAdapter adapter;
    Uri statusUri;
    boolean check_search=false;
    RelativeLayout.LayoutParams pf;
    RelativeLayout.LayoutParams lo;
    ImageButton add_status;
    RelativeLayout.LayoutParams se;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        overridePendingTransition(0,0);
        firebaseAuth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        usersArrayList=new ArrayList<>();
        profile=findViewById(R.id.profile);
        searchView=findViewById(R.id.search_view);
        logo=findViewById(R.id.logo);
        layout1=findViewById(R.id.layout1);
        add_status=findViewById(R.id.add_status);
        pf= (RelativeLayout.LayoutParams) profile.getLayoutParams();
        lo= (RelativeLayout.LayoutParams) logo.getLayoutParams();
        se= (RelativeLayout.LayoutParams) searchView.getLayoutParams();
        if(firebaseAuth.getCurrentUser()==null)
        {
            Intent intent =new Intent(Home.this,login.class);
            startActivity(intent);
            finish();
        }
        else
        {
            if(!firebaseAuth.getCurrentUser().isEmailVerified())
            {
                Intent intent =new Intent(Home.this,login.class);
                startActivity(intent);
                finish();
            }
        }
        DatabaseReference reference=database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users=dataSnapshot.getValue(Users.class);
                    if(users.uid.equals(firebaseAuth.getUid()))
                    {
                        up[0] =users.imageUri;
                        un[0] =users.name;
                        Picasso.get().load(users.imageUri).into(profile);
                    }
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        mainUserRecyclerView=findViewById(R.id.userRecyclerview);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserAdapter(Home.this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),user_profile.class));
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_search=true;
                RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(0,0);
                RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) layout1.getLayoutParams();
                RelativeLayout.LayoutParams main_view = new RelativeLayout.LayoutParams(layout);
                main_view.addRule(RelativeLayout.BELOW,R.id.search_view);
                layout1.setLayoutParams(main_view);
                profile.setLayoutParams(lp);
                logo.setLayoutParams(lp);
                int height=pixtodp(45);
                searchView.setBackgroundResource(R.drawable.search_bar_back);
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
                layoutParams.leftMargin=45;
                layoutParams.rightMargin=15;
                searchView.setLayoutParams(layoutParams);
            }
        });
        add_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mGetContent.launch("image/*");

            }
        });
        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(getApplicationContext(), CropperActivity.class);
                intent.putExtra("DATA",result.toString());
                startActivityForResult(intent,101);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
            check_search=false;
            profile.setLayoutParams(pf);
            logo.setLayoutParams(lo);
            searchView.setLayoutParams(se);
            searchView.setBackgroundResource(R.drawable.shape_circle2);
                RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) layout1.getLayoutParams();
                RelativeLayout.LayoutParams main_view = new RelativeLayout.LayoutParams(layout);
                main_view.addRule(RelativeLayout.BELOW,R.id.logo);
                layout1.setLayoutParams(main_view);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        } //exit
    public void filter(String character) {
        ArrayList<Users> fulllist;
        if (character.isEmpty()) {
            fulllist = usersArrayList;
        } else {
            ArrayList<Users> filterList = new ArrayList<>();
            for (Users item : usersArrayList) {
                String name = item.name.toString().toLowerCase().trim();
                if (name.startsWith(character)) {
                    filterList.add(item);
                }
            }
            fulllist = filterList;
        }
        adapter = new UserAdapter(Home.this, fulllist);
        mainUserRecyclerView.setAdapter(adapter);
    }
    public int pixtodp(int dp)
    {
        int width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,Home.this.getResources().getDisplayMetrics());
        return width;
    }
    protected void onPause() {
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (check_search)
        {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String result=data.getStringExtra("RESULT");
            if(result!=null)
            {
                statusUri= Uri.parse(result);
            }
        }
    }




}