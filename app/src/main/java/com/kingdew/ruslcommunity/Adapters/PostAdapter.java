package com.kingdew.ruslcommunity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.kingdew.ruslcommunity.Models.PostData;
import com.kingdew.ruslcommunity.PostViewActivity;
import com.kingdew.ruslcommunity.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context context;
    ArrayList<PostData> array;
    String data;

    public PostAdapter(Context context, ArrayList<PostData> array,String data) {
        this.context = context;
        this.array = array;
        this.data=data;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (data.equals("home")){
            v =LayoutInflater.from(context).inflate(R.layout.home_post_item,parent,false);
        }else {
            v =LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        Glide.with(context)
                .load(array.get(position).getUrl())
                .centerCrop()
                .into(holder.image);
        holder.title.setText(array.get(position).getTitle());
        holder.auther.setText("by "+array.get(position).getAuther());
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, PostViewActivity.class);
            if (array.get(position).getUser().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                intent.putExtra("auth",true);
            }else {
                intent.putExtra("auth",false);
            }
            intent.putExtra("id",array.get(position).getId());
            intent.putExtra("title",array.get(position).getTitle());
            intent.putExtra("url",array.get(position).getUrl());
            intent.putExtra("auther",array.get(position).getAuther());
            intent.putExtra("desc",array.get(position).getDesc());
            intent.putExtra("user",array.get(position).getUser());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title,auther;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
            auther=itemView.findViewById(R.id.auther);

        }
    }
}
