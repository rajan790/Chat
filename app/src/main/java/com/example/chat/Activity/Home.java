package com.example.chat.Activity;

import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.Adapter.UserAdapter;
import com.example.chat.ModelClass.Users;
import com.example.chat.search;
import com.example.chat.user_profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class Home extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    SearchView searchView;
    ImageView profile,request;
    TextView app_name;
    FloatingActionButton logout;
    FirebaseDatabase database;
    RelativeLayout layout1;
    ArrayList<Users> usersArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        usersArrayList=new ArrayList<>();
        profile=findViewById(R.id.profile);
        logout=findViewById(R.id.logout);
        request=findViewById(R.id.request);
        app_name=findViewById(R.id.app_name);
        searchView=findViewById(R.id.search_view);
        final String[] up = new String[1];
        final String[] un = new String[1];
        layout1=findViewById(R.id.layout1);
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


        RelativeLayout.LayoutParams pf= (RelativeLayout.LayoutParams) profile.getLayoutParams();
        RelativeLayout.LayoutParams app= (RelativeLayout.LayoutParams) app_name.getLayoutParams();
        RelativeLayout.LayoutParams sh= (RelativeLayout.LayoutParams) searchView.getLayoutParams();
        RelativeLayout.LayoutParams rt= (RelativeLayout.LayoutParams) request.getLayoutParams();
        RelativeLayout.LayoutParams layout= (RelativeLayout.LayoutParams) layout1.getLayoutParams();
        searchView.setOnSearchClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(0,0);
                profile.setLayoutParams(layoutParams);
                app_name.setLayoutParams(layoutParams);
                request.setLayoutParams(layoutParams);
                int width= pixtodp(39);
                RelativeLayout.LayoutParams layoutParams2 =new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,width);
                searchView.setBackgroundResource(R.drawable.edit_text2);
                layout1.setGravity(Gravity.CENTER_VERTICAL);
                searchView.setLayoutParams(layoutParams2);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                profile.setLayoutParams(pf);
                request.setLayoutParams(rt);
                searchView.setLayoutParams(sh);
                app_name.setLayoutParams(app);
                layout1.setLayoutParams(layout);
                searchView.setBackgroundResource(R.drawable.shape_circle2);
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
    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            firebaseAuth.signOut();
            Intent intent =new Intent(Home.this,login.class);
            startActivity(intent);
            finish();
        }
    });
    profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent =new Intent(Home.this, user_profile.class);
            intent.putExtra("image",up[0]);
            intent.putExtra("name",un[0]);
            startActivity(intent);
        }
    });
    }
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
        adapter=new UserAdapter(Home.this,fulllist);
        mainUserRecyclerView.setAdapter(adapter);
    }
    public int pixtodp(int dp)
    {
        int width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,Home.this.getResources().getDisplayMetrics());
        return width;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
}