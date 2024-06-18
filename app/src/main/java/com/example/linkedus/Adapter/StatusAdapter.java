package com.example.linkedus.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkedus.Model.Feed;
import com.example.linkedus.Model.Order;
import com.example.linkedus.Model.User;
import com.example.linkedus.R;
import com.example.linkedus.databinding.HomefeedBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder>  {

    ArrayList<Feed> list;
    Context context;

    public StatusAdapter(ArrayList<Feed> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.homefeed, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Feed feed = list.get(position);

        Log.d("sasa", feed.getPlace());
        Log.d("sasa", feed.getProfession());
        Log.d("sasa", feed.getUserId());
        Log.d("sasa", feed.getPrice());


        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(feed.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfile())
                                .placeholder(R.drawable.bg_rectangle)
                                .into(holder.binding.profile);
                        holder.binding.name.setText(user.getName());
                        holder.binding.company.setText(user.getCompany());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//        holder.binding.status.setVisibility(View.GONE);
        holder.binding.profession.setText(feed.getProfession());
        holder.binding.views.setText(feed.getViews()+"");
        holder.binding.place.setText(feed.getPlace());
        holder.binding.time.setText(feed.getTiming());
        holder.binding.price.setText(feed.getPrice());

        holder.binding.getBtn.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        HomefeedBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HomefeedBinding.bind(itemView);
        }
    }

}

