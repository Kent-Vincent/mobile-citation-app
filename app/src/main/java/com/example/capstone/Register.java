package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    Button register;
    EditText name, email, password;
    FirebaseAuth auth;
    String Name, Email, Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        register = findViewById(R.id.btn_signup);

        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = name.getText().toString();
                Email = email.getText().toString();
                Password = password.getText().toString();

                if(Name.isEmpty() || Email.isEmpty() || Password.isEmpty()){
                    Toast.makeText(Register.this, "These fields cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(Email.isEmpty()){
                    Toast.makeText(Register.this, "Email cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(Password.isEmpty()){
                    Toast.makeText(Register.this, "Password cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(Name.isEmpty()){
                    Toast.makeText(Register.this, "Name cannot be Empty! ", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Register.this, "Registered!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Register.this, "Error" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}