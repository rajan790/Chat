package com.example.chat.Adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.Activity.Home;
import com.example.chat.ModelClass.Messages;
import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    RecyclerView recyclerView;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;
    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList,RecyclerView recyclerView) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.recyclerView=recyclerView;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.reciver_layout_item,parent,false);
            return new ReciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Messages messages=messagesArrayList.get(position);

        if(holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder viewHolder=(SenderViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
            viewHolder.time.setText(messages.getTimeStamp());
        }
        else
        {
            ReciverViewHolder viewHolder=(ReciverViewHolder) holder;
            viewHolder.txtmessage.setText(messages.getMessage());
            viewHolder.time.setText(messages.getTimeStamp());
        }
    }
    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECIVE;
        }

    }
    public int pixtodp(int dp)
    {
        int width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, context.getResources().getDisplayMetrics());
        return width;
    }
    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView txtmessage;
        TextView time;
        CardView card;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmessage=itemView.findViewById(R.id.txtmessage);
            int x=pixtodp(290);
            txtmessage.setMaxWidth(x);
            time=itemView.findViewById(R.id.time);
            card=itemView.findViewById(R.id.card);
            card.setBackgroundResource(R.drawable.sender_shape);
        }
    }
    class ReciverViewHolder extends RecyclerView.ViewHolder {
        TextView txtmessage;
        TextView time;
        CardView card;
        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmessage=itemView.findViewById(R.id.txtmessage);
            int x=pixtodp(290);
            txtmessage.setMaxWidth(x);
            time=itemView.findViewById(R.id.time);
            card=itemView.findViewById(R.id.card);
            card.setBackgroundResource(R.drawable.reciver_shape);
        }
    }
}
