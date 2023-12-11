package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email,password;
    String Email, Password;
    Button login;
    FirebaseAuth auth;
    SharedPreferences sp;

    private String validatedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        login = findViewById(R.id.btn_login);

        sp = getSharedPreferences("userEmail", Context.MODE_PRIVATE);
        validatedEmail = getIntent().getStringExtra("emailFromRecycle");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = email.getText().toString();
                Password = password.getText().toString();

                if (Email.isEmpty() || Password.isEmpty()){
                    Toast.makeText(Login.this, "This fields cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if (Email.isEmpty()){
                    Toast.makeText(Login.this, "Email cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(Password.isEmpty()){
                    Toast.makeText(Login.this, "Password cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences.Editor editor = sp.edit();

                                editor.putString("email", Email);
                                editor.putString("pass", Password);
                                editor.commit();

                                Toast.makeText(Login.this, "Successfully logged in.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Login.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        email.setText(validatedEmail);
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish();
        }
    }
}