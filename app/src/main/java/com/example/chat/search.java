package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.chat.Activity.Home;
import com.example.chat.Adapter.UserAdapter;
import com.example.chat.ModelClass.Users;

import java.util.ArrayList;

public class search extends AppCompatActivity {
    SearchView searchView;
    RecyclerView search_page;
    UserAdapter adapter;
    ArrayList <Users> usersArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        usersArrayList= (ArrayList<Users>) getIntent().getSerializableExtra("data");
//        search_page=findViewById(R.id.search_page);
//        search_page.setLayoutManager(new LinearLayoutManager(this));
//        adapter=new UserAdapter(search.this,usersArrayList);
//        search_page.setAdapter(adapter);
//        Users user=usersArrayList.get(0);
        Toast.makeText(this, ""+usersArrayList.size(), Toast.LENGTH_SHORT).show();

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

    }
}