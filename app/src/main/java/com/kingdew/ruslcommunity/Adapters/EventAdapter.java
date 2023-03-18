package com.kingdew.ruslcommunity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingdew.ruslcommunity.AddPostActivity;
import com.kingdew.ruslcommunity.Models.EventData;
import com.kingdew.ruslcommunity.Models.PostData;
import com.kingdew.ruslcommunity.R;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    String url="https://script.google.com/macros/s/AKfycbwHMOtYstuEqDSdn3agatNw5p4WjpsrPb9lqKHzx9zRcf6ZBJyPZZXaYRQkmMfj8YhN/exec";
    String vurl="https://docs.google.com/spreadsheets/d/e/2PACX-1vQqbMLuTlgBzSsvzXO29rMFb9ZVFWnDtv79DOLhs_WGT5EfEs8A6dNlKkrLqNgi5bTbkzYvW6NpY5Hg/pubhtml#";
    String username;
    Context context;
    ArrayList<EventData> events;

    public EventAdapter(Context context, ArrayList<EventData> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.event_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        boolean isJoined=events.get(position).isJoined();
        if (isJoined){
            holder.join.setText("Joined");
        }else {

        }
        holder.title.setText(events.get(position).getTitle());
        Glide.with(context).load(events.get(position).getImage()).centerCrop().into(holder.image);

        holder.join.setOnClickListener(view -> {

            if (isJoined){
                dd(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),FirebaseAuth.getInstance().getCurrentUser().getUid());
            }else {
                ss(events.get(position).getTitle());
            }


        });
    }



    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        ElasticButton join;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
            join=itemView.findViewById(R.id.join);
        }
    }

    private void dd(String phoneNumber, String uid) {

        RequestQueue queue=Volley.newRequestQueue(context);

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, vurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject feedObj = response.getJSONObject("feed");
                    JSONArray entryArray = feedObj.getJSONArray("entry");
                    for(int i=0; i<entryArray.length(); i++){
                        JSONObject entryObj = entryArray.getJSONObject(i);


                        String id = entryObj.getJSONObject("gsx$id").getString("$t");
                        String username = entryObj.getJSONObject("gsx$username").getString("$t");
                        String phone = entryObj.getJSONObject("gsx$phone").getString("$t");
                        String event = entryObj.getJSONObject("gsx$event").getString("$t");
                        Toast.makeText(context, ""+username, Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

        };
        queue.add(request);
    }

    private void ss(String event){

        String phone= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        String id=FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference("Users").child(phone).child("NAME").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username=snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RequestQueue queue= Volley.newRequestQueue(context);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Event Registration is under review", Toast.LENGTH_SHORT).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("action","addEvent");
                params.put("id",id);
                params.put("username",username);
                params.put("phone",phone);
                params.put("event",event);


                return params;
            }
        };
        int socketTimeout=50000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(socketTimeout,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        queue.add(stringRequest);
    }
}
