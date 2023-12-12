package com.example.capstone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdatingPayment extends AppCompatActivity {

    private DatabaseReference databaseReference;

    TextView qr, status;
    Button payed, again;
    String qrContent;
    String uniqueID, name, licenseNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating_payment);

        qr = findViewById(R.id.qrContent);
        status = findViewById(R.id.status);

        payed = findViewById(R.id.paymentButton);
        again = findViewById(R.id.scanAgain);

        qrContent = getIntent().getStringExtra("id");

        String[] qrFields = qrContent.split("\n");

        for (String field : qrFields) {
            if (field.startsWith("uniqueID")) {
                uniqueID = field.replace("uniqueID: ", "");
            } else if (field.startsWith("Name")) {
                name = field.replace("Name: ", "");
            } else if (field.startsWith("License Number")) {
                licenseNumber = field.replace("License Number: ", "");
            }
        }

        qr.setText("REFERENCE NO: " + uniqueID + "\nName: " + name + "\nLicense Number: " + licenseNumber);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        checkPaymentStatus(uniqueID);

        payed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePaymentStatus(uniqueID);
            }
        });

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatingPayment.this, TreasurerScanner.class);
                startActivity(intent);
            }
        });
    }

    private void checkPaymentStatus(String uniqueID) {
        DatabaseReference uploadsRef = databaseReference.child("uploads").child("Information");

        uploadsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot entrySnapshot : userSnapshot.getChildren()) {
                            String transactionID = entrySnapshot.child("TransactionID").getValue(String.class);

                            if (transactionID != null && transactionID.equals(uniqueID)) {
                                found = true;
                                String paymentStatus = entrySnapshot.child("Status").getValue(String.class);

                                if (paymentStatus != null) {
                                    status.setText("STATUS PAYMENT: " + paymentStatus);

                                    if ("Paid".equals(paymentStatus)) {
                                        status.setTextColor(Color.GREEN);
                                        again.setVisibility(View.VISIBLE);
                                        payed.setVisibility(View.INVISIBLE);
                                    }

                                } else {
                                    status.setText("STATUS PAYMENT: Unknown");
                                    Log.d("PaymentStatus", "Status from database: null");
                                }
                                break;
                            }
                        }
                    }

                    if (!found) {
                        status.setText("STATUS PAYMENT: Not Found");
                        again.setVisibility(View.VISIBLE);
                        payed.setVisibility(View.INVISIBLE);
                    }
                } else {
                    status.setText("STATUS PAYMENT: Not Found");
                    again.setVisibility(View.VISIBLE);
                    payed.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("PaymentStatus", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void updatePaymentStatus(String uniqueID) {
        DatabaseReference uploadsRef = databaseReference.child("uploads").child("Information");

        uploadsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot entrySnapshot : userSnapshot.getChildren()) {
                            String transactionID = entrySnapshot.child("TransactionID").getValue(String.class);

                            if (transactionID != null && transactionID.equals(uniqueID)) {
                                found = true;

                                // Update the payment status to "Paid"
                                entrySnapshot.getRef().child("Status").setValue("Paid");

                                // Update the UI accordingly
                                status.setText("STATUS PAYMENT: Paid");
                                status.setTextColor(Color.GREEN);
                                again.setVisibility(View.VISIBLE);
                                payed.setVisibility(View.INVISIBLE);

                                break;
                            }
                        }
                    }

                    if (!found) {
                        status.setText("STATUS PAYMENT: Not Found");
                        again.setVisibility(View.VISIBLE);
                        payed.setVisibility(View.INVISIBLE);
                    }
                } else {
                    status.setText("STATUS PAYMENT: Not Found");
                    again.setVisibility(View.VISIBLE);
                    payed.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("PaymentStatus", "Database error: " + databaseError.getMessage());
            }
        });
    }
}