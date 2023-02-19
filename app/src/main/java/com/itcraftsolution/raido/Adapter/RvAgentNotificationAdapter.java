package com.itcraftsolution.raido.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.raido.Models.DemoModel;
import com.itcraftsolution.raido.R;
import com.itcraftsolution.raido.databinding.RvAdminNotificationBinding;
import com.itcraftsolution.raido.databinding.RvSearchRideBinding;
import com.itcraftsolution.raido.spf.SpfUserData;

import java.util.ArrayList;
import java.util.HashMap;

public class RvAgentNotificationAdapter extends RecyclerView.Adapter<RvAgentNotificationAdapter.viewHolder> {
    Context context;
    ArrayList<DemoModel> list;
    DatabaseReference reqDbRef;
    FirebaseAuth auth;
    FirebaseDatabase database;
    SpfUserData spfUserData;

    public RvAgentNotificationAdapter(Context context, ArrayList<DemoModel> list) {
        this.context = context;
        this.list = list;

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reqDbRef = database.getReference("Request");
        spfUserData = new SpfUserData(context);
    }

    @NonNull
    @Override
    public RvAgentNotificationAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_admin_notification, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvAgentNotificationAdapter.viewHolder holder, int position) {

        DemoModel model = list.get(position);
        Glide.with(context).load(model.getUserimage()).into(holder.binding.igAdminNotificationPic);
        holder.binding.txAdminNotiBookSeats.setText(model.getUserbookseat());
        holder.binding.txAdminNotificationJourney.setText(model.getUsersource() + "-" + model.getUserdestination());
        holder.binding.txAdminNotiJourneyDate.setText(model.getUserdate());
        holder.binding.txAdminNotificationName.setText(model.getUsername());

        holder.binding.btnAdminNotiAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hashMap = new HashMap();
                hashMap.put("agentid", auth.getUid());
                hashMap.put("status", "accept");
                hashMap.put("username", spfUserData.getSpfUserLoginDetails().getString("userName", null));
                hashMap.put("journey", model.getUsersource() + "-" + model.getUserdestination());
                reqDbRef.child(auth.getUid()).child(model.getUserid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Accept The User Request", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        holder.binding.btnAdminNotiCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hashMap = new HashMap();
                hashMap.put("agentid", auth.getUid());
                hashMap.put("status", "decline");
                hashMap.put("username", spfUserData.getSpfUserLoginDetails().getString("userName", null));
                hashMap.put("journey", model.getUsersource() + "-" + model.getUserdestination());
                reqDbRef.child(auth.getUid()).child(model.getUserid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Decline The User Request", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        RvAdminNotificationBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RvAdminNotificationBinding.bind(itemView);
        }
    }
}
