package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText  emailTxt, passwordTxt;
    Button registerBtn;
    TextView loginBtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        passwordTxt = findViewById(R.id.password);
        emailTxt = findViewById(R.id.email);
        registerBtn = findViewById(R.id.registerButton);
        loginBtn = findViewById(R.id.loginButton);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        registerBtn.setOnClickListener(new View.OnClickListener() {
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

                hideKeyboard();
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast msg = Toast.makeText(Register.this, "Registered successfully. Please check your email for verification", Toast.LENGTH_SHORT);
                                        msg.setGravity(Gravity.CENTER, 0 ,0 );
                                        msg.show();
                                        emailTxt.setText("");
                                        passwordTxt.setText("");
                                    }
                                    else
                                    {
                                        Toast msg = Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                                        msg.setGravity(Gravity.CENTER, 0 ,0 );
                                        msg.show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast msg = Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                            msg.setGravity(Gravity.CENTER, 0 ,0 );
                            msg.show();
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

    public void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
