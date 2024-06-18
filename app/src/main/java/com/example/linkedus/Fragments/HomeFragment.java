package com.example.linkedus.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.linkedus.Adapter.FeedAdapter;
import com.example.linkedus.Model.Feed;
import com.example.linkedus.Model.Order;
import com.example.linkedus.Model.User;
import com.example.linkedus.R;
import com.example.linkedus.databinding.FragmentHomeBinding;
import com.example.linkedus.databinding.HomefeedBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ArrayList<Feed> list;
    FirebaseRecyclerAdapter adapter;

    public HomeFragment() {
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
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Feeds")
                .limitToLast(100);

        FirebaseRecyclerOptions<Feed> options =
                new FirebaseRecyclerOptions.Builder<Feed>()
                        .setQuery(query, Feed.class)
                        .build();

      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
          @Override
          public void run() {
              binding.feedRV.hideShimmerAdapter();
          }
      },3000);

        adapter = new FirebaseRecyclerAdapter<Feed, FeedHolder>(options) {
            @Override
            public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.homefeed, parent, false);
                return new FeedHolder(view);
            }

            @Override
            protected void onBindViewHolder(FeedHolder holder, int position, Feed feed) {

                holder.binding.textStatus.setVisibility(View.GONE);
                holder.binding.status.setVisibility(View.GONE);

                if(feed.getDescription().isEmpty()){
                    holder.binding.textDesc.setVisibility(View.GONE);
                    holder.binding.description.setVisibility(View.GONE);
                }

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

                Log.d("ttttt",feed.toString());
                holder.binding.profession.setText(feed.getProfession());
                holder.binding.description.setText(feed.getDescription());
                holder.binding.time.setText(feed.getTiming()+"");
                holder.binding.views.setText(feed.getViews()+"");
                holder.binding.place.setText(feed.getPlace());
                holder.binding.rating.setText(feed.getRating()+"");
                holder.binding.success.setText(feed.getSuccess()+"");
                holder.binding.price.setText("Rs. "+feed.getPrice()+" /-");

                Picasso.get()
                        .load(feed.getFeedPhoto())
                        .placeholder(R.drawable.bg_rectangle)
                        .into(holder.binding.feedPhoto);

                FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(FirebaseAuth.getInstance().getUid())
                                .child("Services").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    if(snapshot1.child("feedId").getValue().toString().equals(feed.getFeedId())){
                                        holder.binding.getBtn.setClickable(false);
                                        holder.binding.getBtn.setBackgroundColor(Color.parseColor("#456756"));
                                        holder.binding.getBtn.setText("Service Added");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                holder.binding.getBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("Services")
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
                                        view = view + 1;
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
                                        Toast.makeText(getContext(), "Service Added to your account", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

            }



        };


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        binding.feedRV.setLayoutManager(linearLayoutManager1);
        binding.feedRV.setNestedScrollingEnabled(false);
        binding.feedRV.setAdapter(adapter);
        binding.feedRV.showShimmerAdapter();



        ////////////////////////////////////////////////////////////////////////
//        FirebaseDatabase.getInstance().getReference()
//                .child("Feeds").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        list.clear();
//                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                            Feed feed = snapshot1.getValue(Feed.class);
//                            list.add(feed);
//                        }
//                        Toast.makeText(getContext(),"CC  :: "+list.size(), Toast.LENGTH_SHORT).show();
//                        binding.feedRV.hideShimmerAdapter();
//                        feedAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}

class FeedHolder extends RecyclerView.ViewHolder {

    HomefeedBinding binding;

    public FeedHolder(@NonNull View itemView) {
        super(itemView);
        binding = HomefeedBinding.bind(itemView);
    }
}