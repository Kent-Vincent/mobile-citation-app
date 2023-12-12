package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Login_CT extends AppCompatActivity {
    private DatabaseReference databaseReference;
    DatabaseReference userRef;
    EditText email, password;
    String Email, Password;
    ImageView multipleuser;
    private List<Accounts> mUploads;
    private AccountsAdapter mAdapter;
    Button login;
    FirebaseAuth auth;
    SharedPreferences sp;

    private String validatedEmail;

    @Override
    protected void onStart() {
        super.onStart();
        email.setText(validatedEmail);
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

        multipleuser = findViewById(R.id.manyUser);

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

        multipleuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
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
                        Intent intent = new Intent(Login_CT.this, TreasurerScanner.class);
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
                        Toast.makeText(Login_CT.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if ("Officer".equals(accountType)) {
                        Intent officerIntent = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(officerIntent);
                        Toast.makeText(Login_CT.this, "You can't access!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login_CT.this, "Error checking account type.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_recycleview_users, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        ImageView cancel;
        RecyclerView recyclerView;
        cancel = customLayout.findViewById(R.id.cancel_button_recycle);
        recyclerView = customLayout.findViewById(R.id.recycleView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        dialog.show();
        userRef = FirebaseDatabase.getInstance().getReference("users");
        mUploads = new ArrayList<>();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        mAdapter = new AccountsAdapter(Login_CT.this, mUploads, auth,new AccountsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String userEmail) {
                email.setText(userEmail);
                dialog.dismiss();
            }
        });

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Accounts accounts = snapshot.getValue(Accounts.class);
                if ("Treasurer".equals(accounts.getAccountType())) {
                    mUploads.add(accounts);
                    mAdapter.notifyItemInserted(mUploads.size() - 1);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}