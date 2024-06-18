package com.example.linkedus.Fragments;

import static android.view.View.getDefaultSize;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkedus.Model.Feed;
import com.example.linkedus.Model.User;
import com.example.linkedus.R;
import com.example.linkedus.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class AddFragment extends Fragment {

    FragmentAddBinding binding;
    String prof, time, place, price, desc;

    Random random;
    String key;
    Feed feed;
    byte[] feedImg;
    Uri feedUri;
    boolean autoSelect=true;

    ArrayList<String> idList;
    ProgressDialog dialog;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        random = new Random();
        idList = new ArrayList<>();
        dialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Feed Uploading...");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);

        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfile())
                                .placeholder(R.drawable.bg_rectangle)
                                .into(binding.profile);
                        binding.nameAdd.setText(user.getName());
                        binding.companyAdd.setText(user.getCompany());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_add, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getTitle().toString())
                        {
                            case "Select From Gallary":
                                autoSelect=false;
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 22);

                                break;

                            case "Auto Select":
                                autoSelect=true;
                                break;

                        }

                        return true;
                    }
                });
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               if(autoSelect){
                   if(!binding.profET.getText().toString().equals(prof)){
                       String ss = binding.profET.getText().toString();
                       if(ss.indexOf(" ")==-1) new DownloadImageTask().execute("https://source.unsplash.com/random/300×300/?"+ss);
                       else {
                           new DownloadImageTask().execute("https://source.unsplash.com/random/300×300/?"+ss.substring(0,ss.indexOf(" ")));
                       }
                   }
                   prof=binding.profET.getText().toString();
               }
            }
        },1000,2000);

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prof = binding.profET.getText().toString();
                time = binding.timing1ET.getText().toString();
                String time2  = binding.timing2ET.getText().toString();
                place = binding.placeET.getText().toString();
                price = binding.priceET.getText().toString();
                desc = binding.desET.getText().toString();

                if(!(prof.equals("") || time.equals("") || time2.equals("") || place.equals("") || price.equals(""))){
                    dialog.show();

                    time = binding.timing1ET.getText().toString()+" AM to "+binding.timing2ET.getText().toString()+" PM";

                    key = FirebaseDatabase.getInstance().getReference()
                            .child("Feeds")
                            .push().getKey();

                    feed = new Feed();

                    feed.setProfession(prof);
                    feed.setDescription(desc);
                    feed.setTiming(time);
                    feed.setTime(new Date().getTime());
                    feed.setPlace(place);
                    feed.setPrice(price);
                    feed.setViews(0);
                    feed.setStatus("Pending");
                    feed.setRating(0.0);
                    feed.setSuccess(0);
                    feed.setUserId(FirebaseAuth.getInstance().getUid());
                    feed.setFeedId(key);

                    final StorageReference reference = FirebaseStorage.getInstance().getReference().child("feed_photo").child(feed.getFeedId());
                    if(autoSelect){
                        reference.putBytes(feedImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        feed.setFeedPhoto(uri.toString());
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Feeds").child(key).setValue(feed);

                                        binding.profET.setText("");
                                        binding.desET.setText("");
                                        binding.timing1ET.setText("");
                                        binding.timing2ET.setText("");
                                        binding.placeET.setText("");
                                        binding.priceET.setText("");

                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Feed Added Successfully", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                        });
                    }
                    else{
                        reference.putFile(feedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        feed.setFeedPhoto(uri.toString());
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Feeds").child(key).setValue(feed);

                                        binding.profET.setText("");
                                        binding.desET.setText("");
                                        binding.timing1ET.setText("");
                                        binding.timing2ET.setText("");
                                        binding.placeET.setText("");
                                        binding.priceET.setText("");

                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Feed Added Successfully", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                        });
                    }


//                    new DownloadImageTask().execute("https://source.unsplash.com/random/300×300/?"+tag[0]);

//                FirebaseDatabase.getInstance().getReference()
//                        .child("Feeds").child(key).setValue(feed).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(getContext(), "Added Feed", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                }
                else{
                        Toast.makeText(getContext(), "Please Fill data in all field", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    private class DownloadImageTask extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... params) {
            String imageUrl = params[0];
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = input.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                return baos.toByteArray();
            } catch (IOException e) {
                Log.e(TAG, "Error downloading image from URL", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(byte[] imageData) {
            if (imageData != null) {

                feedImg = imageData;

              if(autoSelect)  binding.feedPhoto.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));

//                final StorageReference reference = FirebaseStorage.getInstance().getReference().child("feed_photo").child(feed.getFeedId());

//                String path = null;
//                try {
//                    path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), Arrays.toString(imageData), "LinkedUS", null);
//                } catch (FileNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//                Uri uri = Uri.parse(path);

            } else {
                Log.e(TAG, "Error: Image data is null");
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==22){
            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.feedPhoto.setImageURI(uri);
                feedUri = uri;
            }
        }
    }

}