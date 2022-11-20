package com.example.glacomplex.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.glacomplex.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = this.getSharedPreferences("com.example.app31_s7sharpreferences", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //sharedPreferences = getSharedPreferences();

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();
            }
        });

    }

    private void performAuth(){
        String email = binding.userMail.getText().toString();
        String password = binding.password.getText().toString();
        String confirmPassword = binding.confirmPassword.getText().toString();
        if(!email.matches(emailPattern)){
            binding.userMail.setError("Enter Correct Email");
            binding.userMail.requestFocus();
        }else if(password.isEmpty() || password.length() < 6) {
            binding.password.setError("Enter Proper Password");
        } else if(confirmPassword.isEmpty()){
            binding.confirmPassword.setError("Required");
        } else if(!password.equals(confirmPassword)){
            binding.confirmPassword.setError("Password Not Mached");
        } else {
            progressDialog.setMessage("Please wait while Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sharedPreferences.edit().putInt("registeredUser", 1).apply();
                        sharedPreferences.edit().putString("userName", email.substring(0, email.indexOf("@"))).apply();
                        Toast.makeText(SignUp.this, "Registaration Successfull", Toast.LENGTH_SHORT).show();
                        sendToMain();
                    } else {
                        progressDialog.dismiss();
                        sharedPreferences.edit().putInt("registeredUser", 0).apply();
                        Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        
    }

    private void sendToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}