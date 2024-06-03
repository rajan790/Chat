package com.example.chat.Adapter;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.Activity.Home;
import com.example.chat.Activity.chat_Activity;
import com.example.chat.ModelClass.Users;
import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder>
{
    Context home;
    ArrayList<Users> usersArrayList;
    public UserAdapter(Home home, ArrayList<Users> usersArrayList)
    {
        this.home=home;
        this.usersArrayList=usersArrayList;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(home).inflate(R.layout.item_user_row,parent,false);
         return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Users users = usersArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid()) || users.verify==0) {
            RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(0, 0);
            RelativeLayout layout = (RelativeLayout) holder.itemView.findViewById(R.id.user_outer_layout);
            layout.setLayoutParams(parms);
        }
        else
        {
            holder.user_name.setText(users.name);
            holder.user_status.setText(users.status);
            Picasso.get().load(users.imageUri).into(holder.userprofile);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(home, chat_Activity.class);
                    intent.putExtra("name", users.getName());
                    intent.putExtra("ReciverImage",users.getImageUri());
                    intent.putExtra("uid",users.getUid());
                    home.startActivity(intent);
                }
            });

        }

        }
    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }




    class Viewholder extends RecyclerView.ViewHolder
    {
        CircleImageView userprofile;
        TextView user_name,user_status;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            userprofile=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            user_status=itemView.findViewById(R.id.user_status);
        }
    }

}
