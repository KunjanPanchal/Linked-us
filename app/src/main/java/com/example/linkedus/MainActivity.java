package com.example.linkedus;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.linkedus.Fragments.AddFragment;
import com.example.linkedus.Fragments.HomeFragment;
import com.example.linkedus.Fragments.OrdersFragment;
import com.example.linkedus.Fragments.ProfileFragment;
import com.example.linkedus.Fragments.StatusFragment;
import com.example.linkedus.Model.User;
import com.example.linkedus.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();

        binding.navCons.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                switch (item.getTitle().toString()) {
                    case "Home":
                        transaction.replace(R.id.container, new HomeFragment());
                        break;
                    case "Add":
                        transaction.replace(R.id.container, new AddFragment());
                        break;
                    case "Status":
                        transaction.replace(R.id.container, new StatusFragment());
                        break;
                    case "Orders":
                       transaction.replace(R.id.container, new OrdersFragment());
                        break;
                    case "Profile":
                        transaction.replace(R.id.container, new ProfileFragment());
                        break;
                }
                transaction.commit();

                return true;
            }
        });


    }


}