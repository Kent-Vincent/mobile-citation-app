package com.example.capstone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CreateTicketNew extends AppCompatActivity {
    TextView violation_name, violation_price, violation_nameSpecify, violation_priceSpecify;
    Button cancel, next;
    private Set<Integer> highlightedPositions = new HashSet<>();
    private int finalPrice;

    TransactionLoadingDialog loadingDialog = new TransactionLoadingDialog(CreateTicketNew.this);
    SpecifyLoadingDialog specifyLoadingDialog = new SpecifyLoadingDialog(CreateTicketNew.this);

    DatabaseReference userRef;

    private List<ViolationAndPricing> mUploads;
    private RecyclerView mRecycleView;
    private ViolationAndPricingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket_new);

        next = findViewById(R.id.next);
        cancel = findViewById(R.id.cancel);

        violation_name = findViewById(R.id.violationTxt);
        violation_price = findViewById(R.id.priceTxt);
        violation_nameSpecify = findViewById(R.id.violationTxtSpecify);
        violation_priceSpecify = findViewById(R.id.priceTxtSpecify);

        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecycleView.setHasFixedSize(true);

        userRef = FirebaseDatabase.getInstance().getReference("violations");
        mUploads = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("violation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        loadingDialog.loadingAlertDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000);

        mAdapter = new ViolationAndPricingAdapter(CreateTicketNew.this, mUploads, new ViolationAndPricingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String name, String price) {
                if (name.equals("Please Specify")) {
                    specifyLoadingDialog.loadingAlertDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showPleaseSpecifyDialog();
                        }
                    }, 3000);
                } else {
                    toggleBackground(mRecycleView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.cardViewViolations), !isItemHighlighted(position));

                    if (isItemHighlighted(position)) {
                        highlightedPositions.remove(position);
                    } else {
                        highlightedPositions.add(position);
                    }
                    violation_name.setText("" + name);
                    violation_price.setText("" + price);
                }
            }
        });

        highlightedPositions.clear();

        userRef.orderByChild("SortOrder").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ViolationAndPricing violationAndPricing = snapshot.getValue(ViolationAndPricing.class);
                if (!mUploads.contains(violationAndPricing)) {
                    mUploads.add(violationAndPricing);
                    mAdapter.notifyItemInserted(mUploads.size() - 1);
                }
                loadingDialog.dismissDialog();
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {
                // Handle child changed event
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle child removed event
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {
                // Handle child moved event
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });

        mRecycleView.setAdapter(mAdapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FOR CARDVIEWS PRICES
                String cvPriceText = violation_price.getText().toString();
                String[] priceArray1 = cvPriceText.split("\n");
                int sumForCV = 0;
                for (String price : priceArray1) {
                    sumForCV += Integer.parseInt(price);
                }

                //FOR PLEASE SPECIFY PRICES
                String dialogPriceText = violation_priceSpecify.getText().toString();
                String[] priceArray2 = dialogPriceText.split("\n");
                int sumForSpecify = 0;
                for (String price : priceArray2) {
                    int priceValue = Integer.parseInt(price);
                    if (priceValue != 0) {
                        sumForSpecify += priceValue;
                    }
                }

                //FOR CARDVIEW STRING AND PLEASE SPECIFY
                String violationNameText = violation_name.getText().toString();
                String violationNameSpecify = violation_nameSpecify.getText().toString();

                //FOR EXCLUDING 0
                String cvPriceText2 = violation_price.getText().toString();
                String dialogPriceText2 = violation_priceSpecify.getText().toString();

                String[] priceArray3 = dialogPriceText2.split("\n");
                List<String> filteredPrices = new ArrayList<>();
                for (String price : priceArray2) {
                    if (!price.equals("0")) {
                        filteredPrices.add(price);
                    }
                }
                //FOR COMBINING
                String combinedString = violationNameText + "\n" + violationNameSpecify;
                String combined_price = cvPriceText2 + "\n" + TextUtils.join("\n", filteredPrices);
                //FOR TIME
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:a", Locale.getDefault());
                String currentDateTime = dateFormat.format(currentDate);

                finalPrice = sumForCV + sumForSpecify;

                if (finalPrice == 0){
                    Toast.makeText(CreateTicketNew.this, "Please Select A Violation Before Proceeding To Next Activity", Toast.LENGTH_SHORT).show();
                }
                else{
                    editor.putInt("final_price", finalPrice);
                    editor.putString("combined_strings", combinedString);
                    editor.putString("combined_price", combined_price);
                    editor.putString("CurrentDateTime", currentDateTime);
                    editor.commit();
                    Intent intent = new Intent(CreateTicketNew.this, MoreDetails.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void toggleBackground(View view, boolean isHighlighted) {
        if (isHighlighted) {
            view.setBackgroundResource(R.drawable.highlight_cardview);
        } else {
            view.setBackgroundResource(android.R.color.white);
        }
    }

    private boolean isItemHighlighted(int position) {
        return highlightedPositions.contains(position);
    }

    private void showPleaseSpecifyDialog() {
        Dialog dialog = new Dialog(this);
        View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_please_specify_manual, null);
        dialog.setContentView(customLayout);
        specifyLoadingDialog.dismissDialog();
        Button cancel_dialog, confirm_dialog;
        EditText name, price;

        cancel_dialog = customLayout.findViewById(R.id.cancel);
        confirm_dialog = customLayout.findViewById(R.id.next);
        name = customLayout.findViewById(R.id.name_violation);
        price = customLayout.findViewById(R.id.price_violation);

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirm_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString();
                String Price = price.getText().toString();

                if (Name.isEmpty() || Price.isEmpty()){
                    Toast.makeText(CreateTicketNew.this, "Please Do Not Leave Blank", Toast.LENGTH_SHORT).show();
                }
                else if (Name.isEmpty()){
                    Toast.makeText(CreateTicketNew.this, "Please Input A Name", Toast.LENGTH_SHORT).show();
                }
                else if (Price.isEmpty()){
                    Toast.makeText(CreateTicketNew.this, "Please Input A Price", Toast.LENGTH_SHORT).show();
                }
                else{
                    violation_nameSpecify.setText(Name);
                    violation_priceSpecify.setText(Price);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}