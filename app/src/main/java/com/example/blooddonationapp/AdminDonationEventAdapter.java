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

public class AdminDonationEventAdapter extends FirestoreRecyclerAdapter<AdminDonationEventList, AdminDonationEventAdapter.MyViewHolder>
{
    private OnItemClickListener listener;

    public AdminDonationEventAdapter(@NonNull FirestoreRecyclerOptions<AdminDonationEventList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final AdminDonationEventList model) {
        holder.date.setText(model.getStartDate()+ " - " +model.getEndDate());
        holder.eventName.setText(model.getEventName());
        holder.venue.setText(model.getVenue());
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_donation_event, parent, false);
        return new MyViewHolder(v);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, date, venue;
        ImageView buttonDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.eventNameText);
            date = itemView.findViewById(R.id.selectedDateText);
            venue = itemView.findViewById(R.id.venueText);

            buttonDetails = itemView.findViewById(R.id.buttonDetail);
            buttonDetails.setOnClickListener(new View.OnClickListener() {
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
    //Create interface for clickable to new activity
    public interface  OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


}
