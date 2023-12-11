package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory extends AppCompatActivity {
    TransactionLoadingDialog loadingDialog = new TransactionLoadingDialog(TransactionHistory.this);
    FirebaseUser currentUser;
    String userUid;
    DatabaseReference userRef;

    private List<TrafficViolation> mUploads;
    private RecyclerView mRecycleView;
    private TrafficViolationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setHasFixedSize(true);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userUid = currentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference("uploads").child("Information").child(userUid);

        loadingDialog.loadingAlertDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000);

        mUploads = new ArrayList<>();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                        TrafficViolation trafficViolation = data.getValue(TrafficViolation.class);
                        mUploads.add(trafficViolation);
                    }
                mAdapter = new TrafficViolationAdapter(TransactionHistory.this, mUploads);
                mRecycleView.setAdapter(mAdapter);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TransactionHistory.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}