package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailTxt, passwordTxt;
    Button loginBtn;
    TextView registerBtn, resetPasswordBtn;
    ProgressBar progressBar;
    FirebaseAuth authenticate;
    CheckBox rememberUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordTxt = findViewById(R.id.password);
        emailTxt = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressBar);
        authenticate = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.loginButton);
        registerBtn = findViewById(R.id.registerButton);
        resetPasswordBtn = findViewById(R.id.resetPasswordButton);
        rememberUser = findViewById(R.id.remeberMeBox);

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkBox = preferences.getString("remember", "");

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    emailTxt.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    passwordTxt.setError("Password is required.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                authenticate.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.INVISIBLE);

                            if(authenticate.getCurrentUser().isEmailVerified())
                            {
                                startActivity(new Intent(Login.this, MainActivity.class));
                            }
                            else
                            {
                                Toast msg = Toast.makeText(Login.this, "Please verify your email address" , Toast.LENGTH_SHORT);
                                msg.setGravity(Gravity.CENTER, 0 ,0 );
                                msg.show();
                            }
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast msg = Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                            msg.setGravity(Gravity.CENTER, 0 ,0 );
                            msg.show();
                        }
                    }
                });
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ResetPassword.class));
            }
        });
    }
}
