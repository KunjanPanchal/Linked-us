package com.example.linkedus.Adapter;

import android.content.Context;
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
import com.google.firebase.auth.TotpSecret;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>  {

    ArrayList<Feed> list;
    Context context;

    public FeedAdapter(ArrayList<Feed> list, Context context) {
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

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(feed.getUserId()).addValueEventListener(new ValueEventListener() {
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
        holder.binding.textStatus.setVisibility(View.GONE);
        holder.binding.status.setVisibility(View.GONE);
        holder.binding.profession.setText(feed.getProfession());
        holder.binding.views.setText(feed.getViews()+"");
        holder.binding.place.setText(feed.getPlace());
        holder.binding.time.setText(feed.getTiming());
        holder.binding.price.setText("Rs. "+feed.getPrice()+" /-");

        holder.binding.getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(FirebaseAuth.getInstance().getUid()).child("Service")
                        .push().child("feedId").setValue(feed.getFeedId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });


                FirebaseDatabase.getInstance().getReference()
                        .child("Feeds")
                        .child(feed.getFeedId()).child("views").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int view = 0;
                                view = snapshot.getValue(Integer.class);
                                view = view+1;
//                              Toast.makeText(context, "ss : "+view, Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Feeds")
                                        .child(feed.getFeedId()).child("views").setValue(view).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                Order order = new Order(FirebaseAuth.getInstance().getUid(), feed.getFeedId());
                FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(feed.getUserId()).child("Orders").push().setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Service Added to your account", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


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
