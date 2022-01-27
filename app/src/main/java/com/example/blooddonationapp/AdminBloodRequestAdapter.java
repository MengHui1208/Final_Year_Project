package com.example.blooddonationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdminBloodRequestAdapter extends FirestoreRecyclerAdapter<BloodRequestList, AdminBloodRequestAdapter.MyViewHolder>
{
    private AdminBloodRequestAdapter.OnItemClickListener listener;

    public AdminBloodRequestAdapter(@NonNull FirestoreRecyclerOptions<BloodRequestList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull BloodRequestList model) {
        holder.recipientName.setText(model.getRecipientName());
        holder.amount.setText(model.getAmount());
        holder.bloodType.setText(model.getBloodType());
        holder.contact.setText(model.getContact());
        holder.status.setText(model.getStatus());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_blood_request,parent,false);
        return new AdminBloodRequestAdapter.MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView recipientName,bloodType,amount,contact,status;
        ImageView buttonDetail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recipientName = itemView.findViewById(R.id.recipientNameText);
            bloodType = itemView.findViewById(R.id.bloodTypeText);
            amount = itemView.findViewById(R.id.amountText);
            contact = itemView.findViewById(R.id.contactText);
            status = itemView.findViewById(R.id.status);
            buttonDetail = itemView.findViewById(R.id.buttonDetail);

            buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position !=RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(DocumentSnapshot snapshot, int position);
    }

    public void setOnItemClickListener(AdminBloodRequestAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}

