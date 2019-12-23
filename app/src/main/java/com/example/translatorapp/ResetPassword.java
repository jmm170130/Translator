package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    EditText emailTxt;
    Button resetBtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailTxt = findViewById(R.id.email);
        resetBtn = findViewById(R.id.resetPasswordButton);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.loginButton);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();
                emailTxt.setText("");

                if(TextUtils.isEmpty(email))
                {
                    emailTxt.setError("Email is Required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast msg = Toast.makeText(ResetPassword.this, "Password Reset Email Sent" , Toast.LENGTH_SHORT);
                            msg.setGravity(Gravity.CENTER, 0 ,0 );
                            msg.show();
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast msg = Toast.makeText(ResetPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                            msg.setGravity(Gravity.CENTER, 0 ,0 );
                            msg.show();
                            return;
                        }
                    }
                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}
