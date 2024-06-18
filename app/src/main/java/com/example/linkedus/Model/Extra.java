package com.example.linkedus.Model;
//
//import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
//
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.example.linkedus.Fragments.AddFragment;
//import com.example.linkedus.Model.Feed;
//import com.example.linkedus.Model.User;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Date;
//
public class Extra {
//
//
//    public  void repeat(){
//        i=0;
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                tt=false;
//                if(cc==idList.size() && idList.size()!=0)tt=true;
//                cc=idList.size();
//                Log.d("testtt","cc :"+cc);
//                if(tt)addFeed();
//                else repeat();
//
//            }
//        },1000);
//
//
//    }
//
//    public void zeroView() {
//        FirebaseDatabase.getInstance().getReference()
//                .child("Feeds").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("Feeds").child(dataSnapshot.getKey())
//                                    .child("views").setValue(0);
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//    }
//
//    public void addFeed(){
//        feed = new Feed();
//        prof = professions[i][0];
//        time = (random.nextInt(4)+9)+" AM to "+(random.nextInt(7)+1+" PM");
//        price = (random.nextInt(8)+2)*100+"";
//        desc = professions[i][1];
//        place = "--";
//
//        Log.d("testtt",i+" : e1 : "+idList.get(i%51)+"");
//        feed.setUserId(idList.get(i%51)+"");
//        Log.d("testtt",i+" : entering");
//        FirebaseDatabase.getInstance().getReference()
//                .child("Users").child(feed.getUserId()).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        place = snapshot.getValue().toString();
//
//                        key = FirebaseDatabase.getInstance().getReference()
//                                .child("Feeds")
//                                .push().getKey();
//
//
//
//
//                        Log.d("testtt",i+" : mid");
//
//                        feed.setProfession(prof);
//                        feed.setDescription(desc);
//                        feed.setTiming(time);
//                        feed.setTime(new Date().getTime());
//                        feed.setPlace(place);
//                        feed.setPrice(price);
//                        feed.setViews(0);
//                        feed.setStatus("Pending");
//                        feed.setRating(0.0);
//                        feed.setSuccess(0);
//                        feed.setFeedId(key);
//
//                        String tag[] =  prof.split(" ");
//
//                        new AddFragment.DownloadImageTask().execute("https://source.unsplash.com/random/300×300/?"+tag[0]);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//
//    }
//
//    public void addData() {
//        Log.d("sasasasa", "enter");
//        String split[] = names[i].split(" ");
//        split[0] = split[0].toLowerCase();
//        split[1] = split[1].toLowerCase();
//        email = split[0]+(random.nextInt(90)+10)+"@gmail.com";
//        password = split[1]+num[i]+"";
//
//        //////////////
//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if (task.isSuccessful()) {
//                    User user = new User();
//
//                    Log.d("sasasasa", "mid");
//                    user.setName(names[i]);
//                    user.setCompany(company[i]);
//                    user.setEmail(email);
//                    user.setPassword(password);
//                    user.setContact("+91 "+random.nextInt(1000000));
//                    user.setAddress(address[i]);
//
//                    String id = task.getResult().getUser().getUid();
//                    user.setId(id);
//                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).setValue(user);
//
//
//                    new AddFragment.DownloadImageTask().execute("https://xsgames.co/randomusers/avatar.php?g=male");
//
//                } else {
//
//                }
//            }
//        });
//
//
//
//
//        ////////////////////
//
//
//
////        new DownloadImageTask().execute("https://xsgames.co/randomusers/avatar.php?g=male");
//
//    }
//
//    public void delete() {
//
//        FirebaseDatabase.getInstance().getReference()
//                .child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("Users").child(snapshot1.getKey()).child("Services").removeValue();
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("Users").child(snapshot1.getKey()).child("Orders").removeValue();
//                            Log.d("delete1", "done");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//    }
//
//
//    private class DownloadImageTask extends AsyncTask<String, Void, byte[]> {
//
//        @Override
//        protected byte[] doInBackground(String... params) {
//            String imageUrl = params[0];
//            try {
//                URL url = new URL(imageUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int len;
//                while ((len = input.read(buffer)) != -1) {
//                    baos.write(buffer, 0, len);
//                }
//                return baos.toByteArray();
//            } catch (IOException e) {
//                Log.e(TAG, "Error downloading image from URL", e);
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(byte[] imageData) {
//            if (imageData != null) {
//
////                binding.profile.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
//
//                final StorageReference reference = FirebaseStorage.getInstance().getReference().child("feed_photo").child(feed.getFeedId());
//
////                String path = null;
////                try {
////                    path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), Arrays.toString(imageData), "LinkedUS", null);
////                } catch (FileNotFoundException e) {
////                    throw new RuntimeException(e);
////                }
////                Uri uri = Uri.parse(path);
//
////                reference.putFile(uri)
//                reference.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                        Toast.makeText(getContext(),"Profile Photo Saved", Toast.LENGTH_SHORT).show();
//
//                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                feed.setFeedPhoto(uri.toString());
//                                FirebaseDatabase.getInstance().getReference()
//                                        .child("Feeds").child(key).setValue(feed);
////                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("profile").setValue(uri.toString());
//                                i++;
//                                Log.d("testtt",i+" : exit done....");
////                                FirebaseAuth.getInstance().signOut();
//                                if(i<110)addFeed();
//                            }
//                        });
//
//                    }
//
//                });
//
//
//            } else {
//                Log.e(TAG, "Error: Image data is null");
//            }
//        }
//    }
//
//
//
//
//
//
//
}
