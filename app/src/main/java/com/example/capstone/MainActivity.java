package com.example.capstone;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_REQUEST_CHECK_SETTINGS = 1001;
    Button cttmo, treasurer;
    ImageView multipleuser;

    private List<Accounts> mUploads;
    private AccountsAdapter mAdapter;
    DatabaseReference userRef;

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationSettings();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cttmo = findViewById(R.id.login_btn_officer);
        treasurer = findViewById(R.id.login_btn_city);

        multipleuser = findViewById(R.id.manyUser);

        treasurer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login_CT.class);
                startActivity(intent);
            }
        });

        cttmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        multipleuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
            
        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, LOCATION_REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

            } else {
                Toast.makeText(this, "Location services are required to navigate the application.", Toast.LENGTH_SHORT).show();
            }
        }
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
        mAdapter = new AccountsAdapter(MainActivity.this, mUploads, auth,new AccountsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String email) {
                //user email here
            }
        });

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Accounts accounts = snapshot.getValue(Accounts.class);
                if (!mUploads.contains(accounts)) {
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