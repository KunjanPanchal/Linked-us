package com.example.linkedus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.linkedus.Model.User;
import com.example.linkedus.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database  = FirebaseDatabase.getInstance();



        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailET.getText().toString();
                String password = binding.passwordET.getText().toString();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User();

                            user.setName(binding.nameET.getText().toString());
                            user.setCompany(binding.companyET.getText().toString());
                            user.setEmail(email);
                            user.setPassword(password);


                            String id = task.getResult().getUser().getUid();
                            user.setId(id);
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignUpActivity.this, "User Data Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.goToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });


    }
}