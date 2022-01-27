package com.example.blooddonationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PublicNotificationAdapter extends FirestoreRecyclerAdapter<PublicNotificationList, PublicNotificationAdapter.MyViewHolder> {

    public PublicNotificationAdapter(@NonNull FirestoreRecyclerOptions<PublicNotificationList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PublicNotificationAdapter.MyViewHolder holder, int position, @NonNull PublicNotificationList model) {
        holder.name.setText(model.getName());
        holder.content.setText(model.getContent());
        holder.date.setText(model.getDate());
    }

    @NonNull
    @Override
    public PublicNotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_public_notification,parent,false);
        return new PublicNotificationAdapter.MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView content,name,date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.txtDate);

        }
    }
}
