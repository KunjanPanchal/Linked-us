package com.example.linkedus.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.linkedus.Adapter.StatusAdapter;
import com.example.linkedus.Model.Feed;
import com.example.linkedus.R;
import com.example.linkedus.databinding.FragmentOrdersBinding;
import com.example.linkedus.databinding.FragmentStatusBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    FragmentOrdersBinding binding;
    ArrayList<Feed> list;
    int count = 0;
    FirebaseRecyclerAdapter adapter;
    Feed feed;

    public OrdersFragment() {
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

        binding = FragmentOrdersBinding.inflate(inflater, container, false);


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
                .child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
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
}