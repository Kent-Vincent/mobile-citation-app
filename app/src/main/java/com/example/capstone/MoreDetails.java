package com.example.capstone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MoreDetails extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST = 1003;
    private static final int LOCATION_REQUEST_CHECK_SETTINGS = 1004;

    Button submit, clearSign, submitSign;
    EditText name, license, plate, or, cer;
    ImageView barcodeCER, barcodeOR, licenseImageReview;
    TextView address;
    String QR_text;
    String savedCurrentDateTime;
    String finalLocation;
    String Name, License, Plate, Location, OR, CER;
    //GETTING USERNAME
    String Officer, newOfficer;
    //UNIQUE ID
    String uniqueIdentifier;
    LocationRequest locationRequest;
    private EditText currentTargetEditText;

    QRLoadingDialog loadingDialog = new QRLoadingDialog(MoreDetails.this);
    private Bitmap qrCodeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        checkLocationSettings();

        SharedPreferences preferences2 = getSharedPreferences("violation", Context.MODE_PRIVATE);
        savedCurrentDateTime = preferences2.getString("CurrentDateTime", "");

        //FOR USER
        SharedPreferences sp = getApplicationContext().getSharedPreferences("userEmail", Context.MODE_PRIVATE);
        Officer = sp.getString("email", "");

        //WORD REMOVER
        String wordToRemove = "@gmail.com";
        newOfficer = Officer.replace(wordToRemove, "");

        name = findViewById(R.id.name_violator);
        license = findViewById(R.id.license_violator);
        plate = findViewById(R.id.plate_violator);
        cer = findViewById(R.id.cr_Number);
        or = findViewById(R.id.or_Number);

        submit = findViewById(R.id.submit_ticket);
        barcodeCER = findViewById(R.id.barCode_CER);
        barcodeOR = findViewById(R.id.barCode_OR);

        address = findViewById(R.id.location);

        //LOCATION
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uniqueIdentifier = UUID.randomUUID().toString();
                Name = name.getText().toString();
                License = license.getText().toString().trim();
                Plate = plate.getText().toString().trim();
                CER = cer.getText().toString().trim();
                OR = or.getText().toString().trim();
                Location = address.getText().toString().trim();

                QR_text = "uniqueID: " + uniqueIdentifier +
                        "\nName: " + Name +
                        "\nLicense Number: " + License +
                        "\nPlate Number: " + Plate +
                        "\nLocation: " + Location +
                        "\nDate and Time: " + savedCurrentDateTime +
                        "\nCER: " + CER +
                        "\nOR: " + OR;

                if (Name.isEmpty() && License.isEmpty() && Plate.isEmpty() && CER.isEmpty() && OR.isEmpty()){
                    Toast.makeText(MoreDetails.this, "Don't Leave Blank!", Toast.LENGTH_SHORT).show();
                }
                else if (Name.isEmpty()){
                    Toast.makeText(MoreDetails.this, "Name cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if (License.isEmpty()){
                    Toast.makeText(MoreDetails.this, "License cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if (Plate.isEmpty()){
                    Toast.makeText(MoreDetails.this, "Plate Number cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if (CER.isEmpty()) {
                    Toast.makeText(MoreDetails.this, "CER cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if (OR.isEmpty()) {
                    Toast.makeText(MoreDetails.this, "OR cannot be Empty!", Toast.LENGTH_SHORT).show();
                } else{
                    String encryptedQRText = encrypt(QR_text);
                    if (encryptedQRText != null){
                        QR_text = encryptedQRText;
                        getLocation();
                        OpenDialog();
                    }else{
                        Toast.makeText(MoreDetails.this, "Encryption Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        barcodeCER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCode(cer);
            }
        });

        barcodeOR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCode(or);
            }
        });

    }
    private void openCamera(int requestCode) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, requestCode);
    }
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            locationRequest.setInterval(5000);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        android.location.Location location = locationResult.getLastLocation();
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(MoreDetails.this, Locale.getDefault());

                            try {
                                List<Address> addresses = geocoder.getFromLocation(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        1
                                );
                                if (addresses != null && addresses.size() > 0) {
                                    String addressLine = addresses.get(0).getAddressLine(0);
                                    address.setText(addressLine);
                                } else {
                                    Toast.makeText(MoreDetails.this, "No Location Found", Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, null);
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(MoreDetails.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLocationSettings() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result =  LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    getLocation();
                }
                catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MoreDetails.this, LOCATION_REQUEST_CHECK_SETTINGS);
                            }
                            catch (IntentSender.SendIntentException ex) {

                            }

                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                getLocation();
            } else {
                Toast.makeText(this, "Location services are required.", Toast.LENGTH_SHORT).show();
                resetActivity();
            }
        }
    }

    private void resetActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void ScanCode(EditText targetText) {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        currentTargetEditText = targetText;
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null) {
            try {
                String resultContents = result.getContents();
                if (currentTargetEditText != null) {
                    currentTargetEditText.setText(resultContents);

                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Scan Failed", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private String encrypt(String cleartext) {
        try {
            return Crypto.encrypt(cleartext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void QRGenerator(String QR_text) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(QR_text, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void OpenDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_signature, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        clearSign = customLayout.findViewById(R.id.reset_sign);
        submitSign = customLayout.findViewById(R.id.submit_sign);
        SignaturePad sign = customLayout.findViewById(R.id.signature);

        dialog.show();

        clearSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign.clear();
            }
        });

        submitSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = name.getText().toString();
                License = license.getText().toString().trim();
                Plate = plate.getText().toString().trim();
                CER = cer.getText().toString().trim();
                OR = or.getText().toString().trim();
                finalLocation = address.getText().toString().trim();

                SharedPreferences sharedPreferences = getSharedPreferences("more_details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (sign.isEmpty()){
                    Toast.makeText(MoreDetails.this, "Please Sign", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingDialog.loadingAlertDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            QRGenerator(QR_text);
                            loadingDialog.dismissDialog();
                            Bitmap signatureBitmap = sign.getSignatureBitmap();
                            File file = new File(getFilesDir(), "signature.png");
                            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            File qrCodeFile = new File(getFilesDir(), "qrcode.png");
                            try (FileOutputStream outputStream = new FileOutputStream(qrCodeFile)) {
                                qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                outputStream.flush();
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                            editor.putString("uniqueID", uniqueIdentifier);
                            editor.putString("Name", Name);
                            editor.putString("License", License);
                            editor.putString("Plate", Plate);
                            editor.putString("CER", CER);
                            editor.putString("OR", OR);
                            editor.putString("Location", finalLocation);
                            editor.putString("Officer", newOfficer);
                            editor.putString("qrContent", QR_text);
                            editor.apply();

                            Intent intent = new Intent(MoreDetails.this, Preview.class);
                            intent.putExtra("signatureBitmap", file.getAbsolutePath());
                            intent.putExtra("qrcodeBitmap", qrCodeFile.getAbsolutePath());
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }, 5000);
                }
            }
        });
    }
}