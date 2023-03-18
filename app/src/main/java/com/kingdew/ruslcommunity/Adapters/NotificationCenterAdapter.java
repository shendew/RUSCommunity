package com.kingdew.ruslcommunity.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.kingdew.ruslcommunity.Models.NotificationData;
import com.kingdew.ruslcommunity.R;
import com.kusu.library.LoadingButton;
import com.skydoves.elasticviews.ElasticImageView;

import java.util.ArrayList;


public class NotificationCenterAdapter extends RecyclerView.Adapter<NotificationCenterAdapter.ViewHolder> {

    Context context;
    ArrayList<NotificationData> notifications;
    Dialog dialog;
    LoadingButton del;

    public NotificationCenterAdapter(Context context, ArrayList<NotificationData> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationCenterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.center_notification_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationCenterAdapter.ViewHolder holder, int position) {


        holder.title.setText(notifications.get(position).getTITLE());
        holder.body.setText(notifications.get(position).getDESC());
        Glide.with(context).load(notifications.get(position).getIMAGE()).into(holder.img);
        if (notifications.get(position).getSTATUS().equals("newnot")){
            holder.isrun.setVisibility(View.VISIBLE);
            holder.btn_play.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_stop_24));
        }else {
            holder.isrun.setVisibility(View.GONE);
            holder.btn_play.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_play_arrow_24));
        }
        holder.od.setText(notifications.get(position).getFILT());
        holder.edit.setOnClickListener(view -> {

        });
        holder.delete.setOnClickListener(view -> {
            showDelDialog(position);

        });

    }

    private void showDelDialog(int position) {

        dialog=new Dialog(context);
        dialog.setContentView(R.layout.conf_delete);
        del=dialog.findViewById(R.id.delete);
        LoadingButton canc=dialog.findViewById(R.id.cancle);

        del.setOnClickListener(view -> {
            del.showLoading();
            deleteNot(position);
        });
        canc.setOnClickListener(view -> {
            dialog.dismiss();
        });


        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();
    }

    private void deleteNot(int position) {
        FirebaseDatabase.getInstance().getReference("Notifications").child(notifications.get(position).getID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                del.hideLoading();
                dialog.dismiss();
                if (task.isSuccessful()){

                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,body,isrun,od;
        ElasticImageView edit,delete,btn_play;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.item_title);
            body=itemView.findViewById(R.id.item_body);
            edit=itemView.findViewById(R.id.btn_edit);
            delete=itemView.findViewById(R.id.btn_del);
            img=itemView.findViewById(R.id.img);
            isrun=itemView.findViewById(R.id.isrun);
            od=itemView.findViewById(R.id.od);
            btn_play=itemView.findViewById(R.id.btn_play);
        }
    }
}
