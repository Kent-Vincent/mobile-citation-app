package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CreateTicket extends AppCompatActivity {

    private boolean isCv1Highlighted, isCv2Highlighted, isCv3Highlighted, isCv4Highlighted, isCv5Highlighted, isCv6Highlighted, isCv7Highlighted, isCv8Highlighted,
            isCv9Highlighted, isCv10Highlighted = false;


    //FOR CardView
    int priceDrivingWithoutLicence = 3000, priceDistractedDriving = 5000, priceDisregardingTrafficSign = 1000, priceWearingSlippers = 2000,
            priceArrogantDriver = 1000, priceDefectiveParts = 500, priceOverSpeeding = 500, priceChildInFrontSeat = 1000, priceMorePassenger = 1000, priceNoHelmet = 1500;
    String r1_Text = "Driving Without Driver's License", r2_Text = "Driving With SP(Not Accompanied by Professional Driver)", r3_Text = "Inappropriate Driver's License",
            r4_Text = "Reckless Driving", r5_Text = "Disregarding Traffic Sign/Officer", r6_Text = "Failure To Provide Canvass Cover", r7_Text = "Obstructing The Free Passage of Another Vehicle",
            r8_Text = "MC Carrying More Passenger", r9_Text = "Failure To Present OR/CR/DL", r10_Text = "Employing Insolent, Discourteous or Arrogant Driver",
            r11_Text = "Failure to Wear Helmet/Substandard Helmet", r12_Text = "Failure to Wear Seatbelt Private", r13_Text = "Failure to Wear Seatbelt Public", r14_Text = "Child In front Seat",
            r15_Text = "Child on Board of Motorcycle", r16_Text = "Operating The Units With Defective Parts and Accessories", r17_Text = "Wearing Slippers", r18_Text = "Distracted Driving(RA 10913)";


    //FOR RULE1
    int priceDrivingWithSP = 3000, priceInappropriateLicense = 3000, priceRecklessDriving = 2000, priceCanvassCover = 1000, priceObstructingFreePassage = 1000, priceFailureOR = 1000,
            priceFailureToWearSeatbeltPrivate = 1000, priceFailureToWearSeatbeltPublic = 3000, priceOnBoardOfMotorcycle = 3000;


    //FOR RULE2
    int priceDrivingUnregisteredMotorVehicle = 10000, priceImproperRegistration = 10000, priceIllegalAttachmentBodyType = 10000, priceUnauthorizedModification = 5000,
            priceImproperAttachmentMVPlates = 5000;

    String r19_Text = "Driving Unregistered Motor Vehicle", r20_Text = "Improper Registration", r21_Text = "Illegal Attachment of Body Type", r22_Text = "Unauthorized Modification",
            r23_Text = "Failure/Improper Attachment MV Plates";


    //FOR RULE4
    int priceColorumBus = 1000000, priceColorumTruckAndVan = 200000, priceColurumSeedan = 120000, priceColurumJeepney = 50000, priceTricycle = 6000,
            priceFailureCPC = 5000, priceFailureCapacityMarking = 5000, priceFailureSidePlate = 5000, priceBusinessNameMarking = 5000;

    String r24_Text = "Colorum Violation Bus", r25_Text = "Colorum Violation Trucks/Van", r26_Text = "Colorum Violation Seedan", r27_Text = "Colorum Violation Jeepney",
            r28_Text = "Colorum Violation Tricycle", r29_Text = "Failure to Present CPC", r30_Text = "Failure to Provide Proper Body Markings -Capacity Marking",
            r31_Text = "Failure to Provide Proper Body Markings -Side Plate", r32_Text = "Failure to Provide Proper Body Markings -Business Name Marking", r33_Text = "Over Speeding";

    //pricing string card view only!
    String p1 = "Driving Without \n"+"Driver's License          ₱3,000",
            p2 = "Distracted Driving\n"+"(RA 10913)                ₱5,000",
            p3 = "Disregarding Traffic\n"+"Sign/Officer              ₱1,000",
            p4 = "Wearing Slippers          ₱2,000",
            p5 = "Employing Insolent,\n"+"Discourteous or\n"+"Arrogant Driver           ₱1,000",
            p6 = "Operating The Units\n"+"With Defective Parts\n"+" and Accessories            ₱500",
            p7 = "Over Speeding               ₱500",
            p8 = "Child In front Seat       ₱1,000",
            p9 = "MC Carrying More  \n"+"Passenger                 ₱1,000",
            p10 = "Failure to Wear He-\n"+"lmet/Substandard\n"+"Helmet                    ₱1,500";

    //pricing for specify!
    //18, 16, 10
    String p11 = "Driving With SP   \n"+ "(Not Accompanied by\n"+"Professional Driver       ₱3,000",
            p12 = "Inappropriate Dr-\n"+"iver's License            ₱3,000",
            p13 = "Reckless Driving            ₱2,000",
            p14 = "Failure To Provide\n"+"Canvass Cover             ₱1,000",
            p15 = "Obstructing The Free\n"+"Passage of Another\n"+"Vehicle                   ₱1,000",
            p16 = "Failure To Present\n"+"OR/CR/DL                  ₱1,000",
            p17 = "Failure to Wear \n"+"Seatbelt Private          ₱1,000",
            p18 = "Failure to Wear \n"+"Seatbelt Public           ₱3,000",
            p19 = "Child on Board of\n"+"Motorcycle                ₱3,000";
    //RULE 2 SPECIFY!
    String p20 = "Driving Unregistered\n"+"Motor Vehicle             ₱10,000",
            p21 = "Improper Registration       ₱10,000",
            p22 = "Illegal Attachment\n"+"of Body Type              ₱10,000",
            p23 = "Unauthorized\n"+"Modification              ₱5,000",
            p24 = "Failure/Improper At-\n"+"tachment MV Plates        ₱5,000";
    //RULE 4 SPECIFY!
    String p25 = "Colorum Violation\n"+"Bus                  ₱1,000,000",
            p26 = "Colorum Violation\n"+"Trucks/Van              ₱200,000",
            p27 = "Colorum Violation\n"+"Seedan                  ₱120,000",
            p28 = "Colorum Violation\n"+"Jeepney                  ₱50,000",
            p29 = "Colorum Violation\n"+"Tricycle                  ₱6,000",
            p30 = "Failure to\n"+"Present CPC               ₱5,000",
            p31 = "Failure to Provide P-\n"+"roper Body Markings\n"+"-Capacity Marking         ₱5,000",
            p32 = "Failure to Provide P-\n"+"roper Body Markings\n"+"-Side Plate               ₱5,000",
            p33 = "Failure to Provide P-\n"+"roper Body Markings\n"+"-Business Name\n"+"Marking                   ₱5,000";

    //PRICING ISA-ISA
    String m1 = "₱3,000", m2 = "₱5,000", m3 = "₱1,000", m4 = "₱2,000", m5 = "₱1,000", m6 = "₱500", m7 = "₱500", m8 = "₱500";

    int dialogPrice = 0;
    int cardViewPrice = 0;
    int finalPrice = 0;
    StringBuilder selectedItems = new StringBuilder();

    StringBuilder priceBuilder = new StringBuilder();

    TextView cvPrice, dgPrice, combined, priceString;

    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14, cb15, cb16, cb17, cb18, cb19, cb20, cb21, cb22, cb23;

    Button cancel, next;
    CardView cv1, cv2, cv3, cv4, cv5, cv6, cv7, cv8, cv9, cv10, specify;

    SpecifyLoadingDialog loadingDialog = new SpecifyLoadingDialog(CreateTicket.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

        SharedPreferences sharedPreferences = getSharedPreferences("violation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        priceString = findViewById(R.id.stringPrice);
        dgPrice = findViewById(R.id.dialogPrice);
        cvPrice = findViewById(R.id.cardViewPrice);
        cancel = findViewById(R.id.cancel);
        next = findViewById(R.id.next);
        combined = findViewById(R.id.comined_textView);

        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);
        cv3 = findViewById(R.id.cv3);
        cv4 = findViewById(R.id.cv4);
        cv5 = findViewById(R.id.cv5);
        cv6 = findViewById(R.id.cv6);
        cv7 = findViewById(R.id.cv7);
        cv8 = findViewById(R.id.cv8);
        cv9 = findViewById(R.id.cv9);
        cv10 = findViewById(R.id.cv10);
        specify = findViewById(R.id.specify_cv);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv1, isCv1Highlighted);
                isCv1Highlighted = !isCv1Highlighted;
                updatedCardViewPrice();

                if (isCv1Highlighted) {
                    selectedItems.append(r1_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p1);
                    priceBuilder.append("\n");

                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r1_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p1, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv2, isCv2Highlighted);
                isCv2Highlighted = !isCv2Highlighted;
                updatedCardViewPrice();

                if (isCv2Highlighted){
                    selectedItems.append(r18_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p2);
                    priceBuilder.append("\n");
                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r18_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p2, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv3, isCv3Highlighted);
                isCv3Highlighted = !isCv3Highlighted;
                updatedCardViewPrice();

                if (isCv3Highlighted){
                    selectedItems.append(r5_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p3);
                    priceBuilder.append("\n");
                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r5_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p3, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv4, isCv4Highlighted);
                isCv4Highlighted = !isCv4Highlighted;
                updatedCardViewPrice();

                if (isCv4Highlighted){
                    selectedItems.append(r17_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p4);
                    priceBuilder.append("\n");
                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r17_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p4, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        cv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv5, isCv5Highlighted);
                isCv5Highlighted = !isCv5Highlighted;
                updatedCardViewPrice();

                if (isCv5Highlighted){
                    selectedItems.append(r10_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p5);
                    priceBuilder.append("\n");
                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r10_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p5, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        cv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv6, isCv6Highlighted);
                isCv6Highlighted = !isCv6Highlighted;
                updatedCardViewPrice();

                if (isCv6Highlighted){
                    selectedItems.append(r16_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p6);
                    priceBuilder.append("\n");
                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r16_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p6, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        cv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv7, isCv7Highlighted);
                isCv7Highlighted = !isCv7Highlighted;
                updatedCardViewPrice();

                if (isCv7Highlighted){
                    selectedItems.append(r33_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p7);
                    priceBuilder.append("\n");
                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r33_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p7, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        cv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv8, isCv8Highlighted);
                isCv8Highlighted = !isCv8Highlighted;
                updatedCardViewPrice();

                if (isCv8Highlighted){
                    selectedItems.append(r14_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p8);
                    priceBuilder.append("\n");
                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r14_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p8, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        cv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv9, isCv9Highlighted);
                isCv9Highlighted = !isCv9Highlighted;
                updatedCardViewPrice();

                if (isCv9Highlighted){
                    selectedItems.append(r8_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p9);
                    priceBuilder.append("\n");
                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r8_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p9, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        cv10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground(cv10, isCv10Highlighted);
                isCv10Highlighted = !isCv10Highlighted;
                updatedCardViewPrice();

                if (isCv10Highlighted){
                    selectedItems.append(r11_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p10);
                    priceBuilder.append("\n");
                }
                else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r11_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p10, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FOR CARD VIEW PRICE
                String cvPriceText = cvPrice.getText().toString();
                int cvPrice = Integer.parseInt(cvPriceText);

                //FOR DIALOG PRICE
                String dialogPriceText = dgPrice.getText().toString();
                int dialogPrice = Integer.parseInt(dialogPriceText);

                //FOR COMBINED STRINGS
                String final_string = combined.getText().toString();
                String combined_price = priceString.getText().toString();
                
                //FOR TIME
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:a", Locale.getDefault());
                String currentDateTime = dateFormat.format(currentDate);

                finalPrice = cvPrice + dialogPrice;

                if (finalPrice == 0){
                    Toast.makeText(CreateTicket.this, "Please Select A Violation Before Proceeding To Next Activity", Toast.LENGTH_SHORT).show();
                }
                else{
                    editor.putInt("final_price", finalPrice);
                    editor.putString("combined_strings", final_string);
                    editor.putString("combined_price", combined_price);
                    editor.putString("CurrentDateTime", currentDateTime);
                    editor.commit();
                    Intent intent = new Intent(CreateTicket.this, MoreDetails.class);
                    startActivity(intent);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCv1Highlighted = false;
                isCv2Highlighted = false;
                isCv3Highlighted = false;
                isCv4Highlighted = false;
                isCv5Highlighted = false;
                isCv6Highlighted = false;
                isCv7Highlighted = false;
                isCv8Highlighted = false;
                isCv9Highlighted = false;
                isCv10Highlighted = false;

                cv1.setBackgroundResource(android.R.color.white);
                cv2.setBackgroundResource(android.R.color.white);
                cv3.setBackgroundResource(android.R.color.white);
                cv4.setBackgroundResource(android.R.color.white);
                cv5.setBackgroundResource(android.R.color.white);
                cv6.setBackgroundResource(android.R.color.white);
                cv7.setBackgroundResource(android.R.color.white);
                cv8.setBackgroundResource(android.R.color.white);
                cv9.setBackgroundResource(android.R.color.white);
                cv10.setBackgroundResource(android.R.color.white);

                SharedPreferences preferences = getSharedPreferences("CheckBoxState", MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("cb1", false);
                editor.putBoolean("cb2", false);
                editor.putBoolean("cb3", false);
                editor.putBoolean("cb4", false);
                editor.putBoolean("cb5", false);
                editor.putBoolean("cb6", false);
                editor.putBoolean("cb7", false);
                editor.putBoolean("cb8", false);
                editor.putBoolean("cb9", false);
                editor.putBoolean("cb10", false);
                editor.putBoolean("cb11", false);
                editor.putBoolean("cb12", false);
                editor.putBoolean("cb13", false);
                editor.putBoolean("cb14", false);
                editor.putBoolean("cb15", false);
                editor.putBoolean("cb16", false);
                editor.putBoolean("cb17", false);
                editor.putBoolean("cb18", false);
                editor.putBoolean("cb19", false);
                editor.putBoolean("cb20", false);
                editor.putBoolean("cb21", false);
                editor.putBoolean("cb22", false);
                editor.putBoolean("cb23", false);
                editor.apply();

                dgPrice.setText("0");
                cvPrice.setText("0");

                selectedItems = new StringBuilder();
                combined.setText(selectedItems);

                Intent intent = new Intent(CreateTicket.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        specify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loadingDialog.loadingAlertDialog();
               Handler handler = new Handler();
               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       loadingDialog.dismissDialog();
                       createCustomDialog();
                   }
               }, 5000);
            }
        });

    }
    private void toggleBackground(View view, boolean isHighlighted) {
        if (isHighlighted) {
            view.setBackgroundResource(android.R.color.white);
        } else {
            view.setBackgroundResource(R.drawable.highlight_cardview);
        }
    }
    private void updatedCardViewPrice(){
        cardViewPrice = 0;

        if (isCv1Highlighted){
            cardViewPrice += priceDrivingWithoutLicence;
            cvPrice.setText(""+cardViewPrice);
        }
        if (isCv2Highlighted){
            cardViewPrice += priceDistractedDriving;
            cvPrice.setText(""+cardViewPrice);
        }
        if (isCv3Highlighted){
            cardViewPrice += priceDisregardingTrafficSign;
            cvPrice.setText(""+cardViewPrice);
        }
        if (isCv4Highlighted){
            cardViewPrice += priceWearingSlippers;
            cvPrice.setText(""+cardViewPrice);
        }
        if (isCv5Highlighted){
            cardViewPrice += priceArrogantDriver;
            cvPrice.setText(""+cardViewPrice);
        }
        if (isCv6Highlighted){
            cardViewPrice += priceDefectiveParts;
            cvPrice.setText(""+cardViewPrice);
        }
        if (isCv7Highlighted){
            cardViewPrice += priceOverSpeeding;
            cvPrice.setText(""+cardViewPrice);
        }
        if (isCv8Highlighted){
            cardViewPrice += priceChildInFrontSeat;
            cvPrice.setText(""+cardViewPrice);
        }
        if (isCv9Highlighted){
            cardViewPrice += priceMorePassenger;
            cvPrice.setText(""+cardViewPrice);
        }
        if (isCv10Highlighted){
            cardViewPrice += priceNoHelmet;
            cvPrice.setText(""+cardViewPrice);
        }
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###");
        String formattedPrice = decimalFormat.format(cardViewPrice);

        cvPrice.setText(""+cardViewPrice);
    }
    private void updateCombinedString() {
        String combinedString = selectedItems.toString().trim();
        if (combinedString.endsWith("\n")) {
            combinedString = combinedString.substring(0, combinedString.length() - 1);
        }

        combined.setText(combinedString);
    }

    private void updateCombinedPriceString() {
        String combinedStringPrice = priceBuilder.toString().trim();
        if (combinedStringPrice.endsWith("\n")) {
            combinedStringPrice = combinedStringPrice.substring(0, combinedStringPrice.length() - 1);
        }
        priceString.setText(combinedStringPrice);
    }

    private void createCustomDialog(){
        Dialog dialog = new Dialog(this);
        View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_please_specify, null);
        dialog.setContentView(customLayout);
        Button cancel_dialog, confirm_dialog;

        SharedPreferences preferences = getSharedPreferences("CheckBoxState", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences.edit();

        cb1 = customLayout.findViewById(R.id.r1);
        cb2 = customLayout.findViewById(R.id.r2);
        cb3 = customLayout.findViewById(R.id.r3);
        cb4 = customLayout.findViewById(R.id.r4);
        cb5 = customLayout.findViewById(R.id.r5);
        cb6 = customLayout.findViewById(R.id.r6);
        cb7 = customLayout.findViewById(R.id.r7);
        cb8 = customLayout.findViewById(R.id.r8);
        cb9 = customLayout.findViewById(R.id.r9);
        cb10 = customLayout.findViewById(R.id.r10);
        cb11 = customLayout.findViewById(R.id.r11);
        cb12 = customLayout.findViewById(R.id.r12);
        cb13 = customLayout.findViewById(R.id.r13);
        cb14 = customLayout.findViewById(R.id.r14);
        cb15 = customLayout.findViewById(R.id.r15);
        cb16 = customLayout.findViewById(R.id.r16);
        cb17 = customLayout.findViewById(R.id.r17);
        cb18 = customLayout.findViewById(R.id.r18);
        cb19 = customLayout.findViewById(R.id.r19);
        cb20 = customLayout.findViewById(R.id.r20);
        cb21 = customLayout.findViewById(R.id.r21);
        cb22 = customLayout.findViewById(R.id.r22);
        cb23 = customLayout.findViewById(R.id.r23);

        cancel_dialog = customLayout.findViewById(R.id.cancel_dialog);
        confirm_dialog = customLayout.findViewById(R.id.confirm_dialog);

        cb1.setChecked(preferences.getBoolean("cb1", false));
        cb2.setChecked(preferences.getBoolean("cb2", false));
        cb3.setChecked(preferences.getBoolean("cb3", false));
        cb4.setChecked(preferences.getBoolean("cb4", false));
        cb5.setChecked(preferences.getBoolean("cb5", false));
        cb6.setChecked(preferences.getBoolean("cb6", false));
        cb7.setChecked(preferences.getBoolean("cb7", false));
        cb8.setChecked(preferences.getBoolean("cb8", false));
        cb9.setChecked(preferences.getBoolean("cb9", false));
        cb10.setChecked(preferences.getBoolean("cb10", false));
        cb11.setChecked(preferences.getBoolean("cb11", false));
        cb12.setChecked(preferences.getBoolean("cb12", false));
        cb13.setChecked(preferences.getBoolean("cb13", false));
        cb14.setChecked(preferences.getBoolean("cb14", false));
        cb15.setChecked(preferences.getBoolean("cb15", false));
        cb16.setChecked(preferences.getBoolean("cb16", false));
        cb17.setChecked(preferences.getBoolean("cb17", false));
        cb18.setChecked(preferences.getBoolean("cb18", false));
        cb19.setChecked(preferences.getBoolean("cb19", false));
        cb20.setChecked(preferences.getBoolean("cb20", false));
        cb21.setChecked(preferences.getBoolean("cb21", false));
        cb22.setChecked(preferences.getBoolean("cb22", false));
        cb23.setChecked(preferences.getBoolean("cb23", false));

        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb1.isChecked();
                editor2.putBoolean("cb1", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceDrivingWithSP;
                    selectedItems.append(r2_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p11);
                    priceBuilder.append("\n");
                } else {
                    dialogPrice -= priceDrivingWithSP;
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r2_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p11, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);

            }
        });

        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb2.isChecked();
                editor2.putBoolean("cb2", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceInappropriateLicense;
                    selectedItems.append(r3_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p12);
                    priceBuilder.append("\n");
                } else {
                    dialogPrice -= priceInappropriateLicense;
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r3_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p12, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb3.isChecked();
                editor2.putBoolean("cb3", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceRecklessDriving;
                    selectedItems.append(r4_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p13);
                    priceBuilder.append("\n");
                } else {
                    dialogPrice -= priceRecklessDriving;
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r4_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p13, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb4.isChecked();
                editor2.putBoolean("cb4", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceCanvassCover;
                    selectedItems.append(r6_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p14);
                    priceBuilder.append("\n");
                } else {
                    dialogPrice -= priceCanvassCover;
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r6_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p14, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb5.isChecked();
                editor2.putBoolean("cb5", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceObstructingFreePassage;
                    selectedItems.append(r7_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p15);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r7_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p15, ""));
                    dialogPrice -= priceObstructingFreePassage;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb6.isChecked();
                editor2.putBoolean("cb6", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceFailureOR;
                    selectedItems.append(r9_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p16);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r9_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p16, ""));
                    dialogPrice -= priceFailureOR;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb7.isChecked();
                editor2.putBoolean("cb7", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceFailureToWearSeatbeltPrivate;
                    selectedItems.append(r12_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p17);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r12_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p17, ""));
                    dialogPrice -= priceFailureToWearSeatbeltPrivate;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb8.isChecked();
                editor2.putBoolean("cb8", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceFailureToWearSeatbeltPublic;
                    selectedItems.append(r13_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p18);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r13_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p18, ""));
                    dialogPrice -= priceFailureToWearSeatbeltPublic;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb9.isChecked();
                editor2.putBoolean("cb9", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceOnBoardOfMotorcycle;
                    selectedItems.append(r15_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p19);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r15_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p19, ""));
                    dialogPrice -= priceOnBoardOfMotorcycle;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb10.isChecked();
                editor2.putBoolean("cb10", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceDrivingUnregisteredMotorVehicle;
                    selectedItems.append(r19_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p20);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r19_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p20, ""));
                    dialogPrice -= priceDrivingUnregisteredMotorVehicle;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb11.isChecked();
                editor2.putBoolean("cb11", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceImproperRegistration;
                    selectedItems.append(r20_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p21);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r20_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p21, ""));
                    dialogPrice -= priceDrivingUnregisteredMotorVehicle;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb12.isChecked();
                editor2.putBoolean("cb12", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceIllegalAttachmentBodyType;
                    selectedItems.append(r21_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p22);
                    priceBuilder.append("\n");
                } else {
                    dialogPrice -= priceIllegalAttachmentBodyType;
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r21_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p22, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb13.isChecked();
                editor2.putBoolean("cb13", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceUnauthorizedModification;
                    selectedItems.append(r22_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p23);
                    priceBuilder.append("\n");
                } else {
                    dialogPrice -= priceUnauthorizedModification;
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r22_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p23, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb14.isChecked();
                editor2.putBoolean("cb14", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceImproperAttachmentMVPlates;
                    selectedItems.append(r23_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p24);
                    priceBuilder.append("\n");
                } else {
                    dialogPrice -= priceImproperAttachmentMVPlates;
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r23_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p24, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb15.isChecked();
                editor2.putBoolean("cb15", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceColorumBus;
                    selectedItems.append(r24_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p25);
                    priceBuilder.append("\n");
                } else {
                    dialogPrice -= priceColorumBus;
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r24_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p25, ""));
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb16.isChecked();
                editor2.putBoolean("cb16", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceColorumTruckAndVan;
                    selectedItems.append(r25_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p26);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r25_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p26, ""));
                    dialogPrice -= priceColorumTruckAndVan;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb17.isChecked();
                editor2.putBoolean("cb17", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceColurumSeedan;
                    selectedItems.append(r26_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p27);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r26_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p27, ""));
                    dialogPrice -= priceColurumSeedan;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb18.isChecked();
                editor2.putBoolean("cb18", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceColurumJeepney;
                    selectedItems.append(r27_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p28);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r27_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p28, ""));
                    dialogPrice -= priceColurumJeepney;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb19.isChecked();
                editor2.putBoolean("cb19", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceTricycle;
                    selectedItems.append(r28_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p29);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r28_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p29, ""));
                    dialogPrice -= priceTricycle;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb20.isChecked();
                editor2.putBoolean("cb20", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceFailureCPC;
                    selectedItems.append(r29_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p30);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r29_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p30, ""));
                    dialogPrice -= priceFailureCPC;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb21.isChecked();
                editor2.putBoolean("cb21", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceFailureCapacityMarking;
                    selectedItems.append(r30_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p31);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r30_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p31, ""));
                    dialogPrice -= priceFailureCapacityMarking;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb22.isChecked();
                editor2.putBoolean("cb22", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceFailureSidePlate;
                    selectedItems.append(r31_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p32);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r31_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p32, ""));
                    dialogPrice -= priceFailureSidePlate;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cb23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb23.isChecked();
                editor2.putBoolean("cb23", isChecked);
                editor2.apply();

                if (isChecked) {
                    dialogPrice += priceBusinessNameMarking;
                    selectedItems.append(r32_Text);
                    selectedItems.append("\n");
                    priceBuilder.append(p33);
                    priceBuilder.append("\n");
                } else {
                    selectedItems = new StringBuilder(selectedItems.toString().replace(r32_Text, ""));
                    priceBuilder = new StringBuilder(priceBuilder.toString().replace(p33, ""));
                    dialogPrice -= priceBusinessNameMarking;
                }
                updateCombinedString();
                updateCombinedPriceString();
                dgPrice.setText("" + dialogPrice);
            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("CheckBoxState", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("cb1", false);
                editor.putBoolean("cb2", false);
                editor.putBoolean("cb3", false);
                editor.putBoolean("cb4", false);
                editor.putBoolean("cb5", false);
                editor.putBoolean("cb6", false);
                editor.putBoolean("cb7", false);
                editor.putBoolean("cb8", false);
                editor.putBoolean("cb9", false);
                editor.putBoolean("cb10", false);
                editor.putBoolean("cb11", false);
                editor.putBoolean("cb12", false);
                editor.putBoolean("cb13", false);
                editor.putBoolean("cb14", false);
                editor.putBoolean("cb15", false);
                editor.putBoolean("cb16", false);
                editor.putBoolean("cb17", false);
                editor.putBoolean("cb18", false);
                editor.putBoolean("cb19", false);
                editor.putBoolean("cb20", false);
                editor.putBoolean("cb21", false);
                editor.putBoolean("cb22", false);
                editor.putBoolean("cb23", false);
                editor.apply();
                selectedItems = new StringBuilder();
                combined.setText(selectedItems);
                dgPrice.setText("0");
                dialog.dismiss();
            }
        });

        confirm_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}