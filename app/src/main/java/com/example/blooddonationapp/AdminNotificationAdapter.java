package com.example.blooddonationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminNotificationAdapter extends FirestoreRecyclerAdapter<AdminNotificationList, AdminNotificationAdapter.MyViewHolder> {

    public AdminNotificationAdapter(@NonNull FirestoreRecyclerOptions<AdminNotificationList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull AdminNotificationList model) {
        holder.name.setText(model.getName());
        holder.content.setText(model.getContent());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_notification,parent,false);
        return new AdminNotificationAdapter.MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView content,name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
        }
    }
}
