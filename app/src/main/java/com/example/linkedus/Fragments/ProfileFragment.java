package com.example.linkedus.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.linkedus.LogInActivity;
import com.example.linkedus.Model.User;
import com.example.linkedus.R;
import com.example.linkedus.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        binding.userName.setText(user.getName());
                        binding.company.setText(user.getCompany());

                        Picasso.get()
                                .load(user.getProfile())
                                .placeholder(R.drawable.loading)
                                .into(binding.profile);
                        Picasso.get()
                                .load(user.getCompanyPhoto())
                                .placeholder(R.drawable.loading)
                                .into(binding.companyPhoto);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_toolbar, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getTitle().toString()) {

                            case "Change Profile": {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 22);
                                break;
                            }
                            case "Change Company Photo": {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 11);
                                break;
                            }
                            case "Sign Out": {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getContext(), LogInActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case "Change Name":{

                                builder.setTitle("Change User Name");
                                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout, (ViewGroup) getView(), false);
                                EditText editText = view.findViewById(R.id.editText);
                                builder.setView(view);

                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        binding.userName.setText(editText.getText().toString());
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child(FirebaseAuth.getInstance().getUid())
                                                .child("name").setValue(editText.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getContext(),"User Name Changed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                builder.show();

                                break;
                            }

                            case "Change Company":{

                                builder.setTitle("Change Company");
                                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout, (ViewGroup) getView(), false);
                                EditText editText = view.findViewById(R.id.editText);
                                builder.setView(view);

                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        binding.company.setText(editText.getText().toString());
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child(FirebaseAuth.getInstance().getUid())
                                                .child("company").setValue(editText.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getContext(),"Company Changed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                builder.show();

                                break;
                            }

                        }

                        return false;
                    }
                });

            }
        });




        return binding.getRoot();
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==11){
            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.companyPhoto.setImageURI(uri);

                final StorageReference reference = FirebaseStorage.getInstance().getReference().child("company_photo").child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(),"Company Photo Saved", Toast.LENGTH_SHORT).show();

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("companyPhoto").setValue(uri.toString());
                            }
                        });

                    }
                });


            }
        }
        else {
            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.profile.setImageURI(uri);

                final StorageReference reference = FirebaseStorage.getInstance().getReference().child("profile_photo").child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(),"Profile Photo Saved", Toast.LENGTH_SHORT).show();

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("profile").setValue(uri.toString());
                            }
                        });

                    }

                });


            }
        }
    }


}