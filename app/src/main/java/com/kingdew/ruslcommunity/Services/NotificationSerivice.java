package com.kingdew.ruslcommunity.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingdew.ruslcommunity.Models.NotificationData;
import com.kingdew.ruslcommunity.NotificationPageActivity;
import com.kingdew.ruslcommunity.R;

import java.util.ArrayList;

public class NotificationSerivice extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        getNotifications();

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service was stopped", Toast.LENGTH_SHORT).show();
    }
    private void getNotifications() {
        ArrayList<NotificationData> notifactions=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Notifications")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!notifactions.isEmpty()){
                            notifactions.clear();
                        }
                        for (DataSnapshot data:snapshot.getChildren()){

                            if (data.child("STATUS").getValue(String.class).equals("newnot")){
                                String puredesc=data.child("NOT_DESC").getValue(String.class);

                                String title=data.child("TITLE").getValue(String.class);
                                String desc=puredesc;
                                String image=data.child("IMAGE").getValue(String.class);
                                String filt=data.child("FILT").getValue(String.class);
                                notifactions.add(new NotificationData("0",title,"desc",image,desc,"",filt));




                            }
                        }
                        Notification(notifactions);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void Notification(ArrayList<NotificationData> notifications){

        for (int i = 0; i < notifications.size(); i++) {
            NotificationCompat.Builder builder=new NotificationCompat.Builder(NotificationSerivice.this,"RUSTLE");
            builder.setContentText(notifications.get(i).getNOT_TITLE());
            builder.setContentTitle(notifications.get(i).getTITLE());

            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);

            //builder.setAutoCancel(false);

            Intent intent=new Intent(NotificationSerivice.this,NotificationPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent=PendingIntent.getActivity(NotificationSerivice.this,2001,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);


            NotificationManagerCompat managerCompat= NotificationManagerCompat.from(NotificationSerivice.this);
            managerCompat.notify(i,builder.build());

        }


    }

    private void startForeground() {
        Intent notificationIntent = new Intent(this, NotificationPageActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(101, new NotificationCompat.Builder(this,
                "default") // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
    }

}
