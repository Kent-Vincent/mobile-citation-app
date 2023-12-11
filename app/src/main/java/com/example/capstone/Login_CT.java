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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_CT extends AppCompatActivity {
    private DatabaseReference databaseReference;
    EditText email, password;
    String Email, Password;
    Button login;
    FirebaseAuth auth;
    SharedPreferences sp;

    private String validatedEmail;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            checkAccountTypeAndRedirect(userEmail);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ct);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        login = findViewById(R.id.btn_login);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        sp = getSharedPreferences("userEmail", Context.MODE_PRIVATE);
        validatedEmail = getIntent().getStringExtra("emailFromRecycle");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = email.getText().toString();
                Password = password.getText().toString();

                if (Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(Login_CT.this, "This fields cannot be Empty!", Toast.LENGTH_SHORT).show();
                } else if (Email.isEmpty()) {
                    Toast.makeText(Login_CT.this, "Email cannot be Empty!", Toast.LENGTH_SHORT).show();
                } else if (Password.isEmpty()) {
                    Toast.makeText(Login_CT.this, "Password cannot be Empty!", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("email", Email);
                                editor.putString("pass", Password);
                                editor.apply();
                                checkAccountType(Email);
                            } else {
                                Toast.makeText(Login_CT.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkAccountType(String userEmail) {
        databaseReference.orderByChild("Email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean foundUser = false;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    foundUser = true;
                    String accountType = userSnapshot.child("AccountType").getValue(String.class);
                    if ("Treasurer".equals(accountType)) {
                        Intent intent = new Intent(Login_CT.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login_CT.this, "You don't have permission to log in.", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut(); // Sign out the user
                    }
                }

                if (!foundUser) {
                    Toast.makeText(Login_CT.this, "User not found in the database.", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut(); // Sign out the user
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
                Toast.makeText(Login_CT.this, "Error checking account type.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //FOR ON START
    private void checkAccountTypeAndRedirect(String userEmail) {
        databaseReference.orderByChild("Email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String accountType = userSnapshot.child("AccountType").getValue(String.class);

                    if ("Treasurer".equals(accountType)) {
                        Intent treasurerIntent = new Intent(getApplicationContext(), TreasurerScanner.class);
                        startActivity(treasurerIntent);
                        finish();
                    } else if ("Officer".equals(accountType)) {
                        Intent officerIntent = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(officerIntent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
                Toast.makeText(Login_CT.this, "Error checking account type.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}