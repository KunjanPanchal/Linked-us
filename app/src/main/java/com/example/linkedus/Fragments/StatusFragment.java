package com.example.linkedus.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.linkedus.Adapter.StatusAdapter;
import com.example.linkedus.Model.Feed;
import com.example.linkedus.Model.Order;
import com.example.linkedus.Model.Services;
import com.example.linkedus.Model.User;
import com.example.linkedus.R;
import com.example.linkedus.databinding.FragmentStatusBinding;
import com.example.linkedus.databinding.HomefeedBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class StatusFragment extends Fragment {

    FragmentStatusBinding binding;
    ArrayList<Feed> list;
    int count = 0;
    FirebaseRecyclerAdapter adapter;
    Feed feed;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStatusBinding.inflate(inflater, container, false);

//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Users").child(FirebaseAuth.getInstance().getUid())
//                .child("Services")
//                .limitToLast(50);
//
//        FirebaseRecyclerOptions<Services> options =
//                new FirebaseRecyclerOptions.Builder<Services>()
//                        .setQuery(query, Services.class)
//                        .build();
//

//
//
//        adapter = new FirebaseRecyclerAdapter<Services, FeedHolder>(options) {
//            @Override
//            public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                // Create a new instance of the ViewHolder, in this case we are using a custom
//                // layout called R.layout.message for each item
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.homefeed, parent, false);
//
//                return new FeedHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(FeedHolder holder, int position, Services id) {
//
//                Log.d("sasasa", id.toString());
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child("Feeds").child(id.getFeedId())
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                feed = snapshot.getValue(Feed.class);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child("Users")
//                        .child(feed.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                User user = snapshot.getValue(User.class);
//                                Picasso.get()
//                                        .load(user.getProfile())
//                                        .placeholder(R.drawable.bg_rectangle)
//                                        .into(holder.binding.profile);
//                                holder.binding.name.setText(user.getName());
//                                holder.binding.company.setText(user.getCompany());
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
////        holder.binding.status.setVisibility(View.GONE);
//                holder.binding.profession.setText(feed.getProfession());
//                holder.binding.views.setText(feed.getViews() + "");
//                holder.binding.place.setText(feed.getPlace());
//                holder.binding.time.setText(feed.getTime());
//                holder.binding.price.setText(feed.getPrice());
//
//                holder.binding.getBtn.setVisibility(View.GONE);
//
//            }
//
//
//        };
//

        StatusAdapter statusAdapter = new StatusAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        binding.serviceRV.setLayoutManager(linearLayoutManager1);
        binding.serviceRV.setNestedScrollingEnabled(false);
        binding.serviceRV.setAdapter(statusAdapter);
        binding.serviceRV.showShimmerAdapter();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.serviceRV.hideShimmerAdapter();
                statusAdapter.notifyDataSetChanged();
            }
        }, 3000);


        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid())
                .child("Services").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        count=0;

                        for(DataSnapshot snapshot1 : snapshot.getChildren())count++;
//                        Log.d("tttt", "Cou"+""+count);

                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Feeds")
                                    .child(snapshot1.child("feedId").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot2) {

                                            Feed feed = snapshot2.getValue(Feed.class);
                                            list.add(feed);

//                                            Log.d("tttt", list.size()+"");
                                            if(count==list.size()) statusAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

//    class FeedHolder extends RecyclerView.ViewHolder {
//
//        HomefeedBinding binding;
//
//        public FeedHolder(@NonNull View itemView) {
//            super(itemView);
//            binding = HomefeedBinding.bind(itemView);
//        }
//    }
}