package com.kingdew.ruslcommunity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kingdew.ruslcommunity.Models.NotificationData;
import com.kingdew.ruslcommunity.NotificationViewActivity;
import com.kingdew.ruslcommunity.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.OnClickATagListener;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    ArrayList<NotificationData> notifications;

    public NotificationAdapter(Context context, ArrayList<NotificationData> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(notifications.get(position).getIMAGE()).centerCrop().into(holder.image);
        holder.title.setText(notifications.get(position).getTITLE());
        holder.desc.setHtml(notifications.get(position).getDESC());

        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context,NotificationViewActivity.class);

            intent.putExtra("id",notifications.get(position).getID());
            intent.putExtra("title",notifications.get(position).getTITLE());
            intent.putExtra("desc",notifications.get(position).getDESC());
            intent.putExtra("image",notifications.get(position).getIMAGE());
            context.startActivity(intent);
        });
        holder.desc.setOnClickATagListener(new OnClickATagListener() {
            @Override
            public boolean onClick(View widget, String spannedText, @Nullable String href) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        HtmlTextView desc;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            image=itemView.findViewById(R.id.image);

        }
    }
}
