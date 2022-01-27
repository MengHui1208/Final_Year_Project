package com.example.blooddonationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchDonorAdapter extends FirestoreRecyclerAdapter<DonorList, SearchDonorAdapter.MyViewHolder> {

    public SearchDonorAdapter(@NonNull FirestoreRecyclerOptions<DonorList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull DonorList model) {
        holder.fullname.setText(model.getFullname());
        holder.bloodType.setText(model.getBloodType());
        holder.contact.setText(model.getContact());
        holder.address.setText(model.getAddress());
        Glide.with(holder.profileImage.getContext()).load(model.getImageUrl()).into(holder.profileImage);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_donor,parent,false);
        return new MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImage;
        TextView fullname,bloodType,contact,address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            fullname = itemView.findViewById(R.id.fullname);
            bloodType = itemView.findViewById(R.id.bloodType);
            contact = itemView.findViewById(R.id.contact);
            address = itemView.findViewById(R.id.address);
        }
    }
}

