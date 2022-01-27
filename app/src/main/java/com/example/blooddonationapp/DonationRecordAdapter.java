package com.example.blooddonationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class DonationRecordAdapter extends FirestoreRecyclerAdapter <PublicDonationRecordList,DonationRecordAdapter.MyViewHolder>{

    private OnItemClickListener listener;

    public DonationRecordAdapter(@NonNull FirestoreRecyclerOptions<PublicDonationRecordList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull PublicDonationRecordList model) {
        holder.date.setText(model.getDate());
        holder.amount.setText(model.getAmount());
        holder.venue.setText(model.getVenue());
        holder.approvedBy.setText(model.getApprovedBy());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_public_donation_record,parent,false);
        return new DonationRecordAdapter.MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date,amount,venue,approvedBy;
        ImageView buttonDetail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateText);
            amount = itemView.findViewById(R.id.amountText);
            venue = itemView.findViewById(R.id.venueText);
            approvedBy = itemView.findViewById(R.id.approvedByText);

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
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}