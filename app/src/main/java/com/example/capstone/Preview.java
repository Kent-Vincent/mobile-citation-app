package com.example.capstone;

import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class Preview extends AppCompatActivity {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userUid = currentUser.getUid();
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    int printerDpi = 203;
    int printerWidth = 48;
    int printerNbrCharacters = 32;

    TextView name, violation, price, totalPrice, licenseNo, plateNo, location, dateTime, issueBy;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    BluetoothLoadingDialog loadingDialog = new BluetoothLoadingDialog(Preview.this);

    UploadingDialog uploadingDialog = new UploadingDialog(Preview.this);

    Button print;
    ImageView signPic, QRImage;
    String finalString, finalPriceString, combined_price;
    String formattedPrice;
    String savedName, savedLicense, savedPlate, savedCER, savedOR, savedLocation, savedOfficer, savedCurrentDateTime, savedUniqueID, savedQrContent;
    String filepathSign, filepathQR;
    String licenseFileName, licenseFilePath, signatureFileName, signatureFilePath, qrCodeFileName, qrCodeFilePath;
    Bitmap signatureBitmap, qrCodeBitmap, bitmapLicense;
    int finalPrice;
    String Pending = "Pending";
    String PaymentDetails = "Please Proceed To City Treasurer Office or Any Affiliated Government Branch This Will Serve as Your Receipt";
    String newFinalString;

    @Override
    protected void onStart() {
        super.onStart();

        name.setText("Name of Violator: " + savedName);
        violation.setText("Violation: \n" + finalString);
        price.setText("Price: \n" + combined_price);
        totalPrice.setText("Total: ₱" + finalPrice);
        licenseNo.setText("License No: "+savedLicense);
        plateNo.setText("Plate No: " + savedPlate);
        location.setText("Location: " + savedLocation);
        dateTime.setText("Date and Time: " + savedCurrentDateTime);
        issueBy.setText("Issued By: " + savedOfficer);

        Uri signatureUri = Uri.fromFile(new File(filepathSign));
        signPic.setImageURI(signatureUri);
        Uri qrUri = Uri.fromFile(new File(filepathQR));
        QRImage.setImageURI(qrUri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        name = findViewById(R.id.name_violator_print);
        violation = findViewById(R.id.violation_print);
        price = findViewById(R.id.price_print);
        totalPrice = findViewById(R.id.final_price_print);
        licenseNo = findViewById(R.id.licenseNo_print);
        plateNo = findViewById(R.id.plateNo_print);
        location = findViewById(R.id.location_print);
        dateTime = findViewById(R.id.dateTime_print);
        issueBy = findViewById(R.id.issueBy);

        signPic = findViewById(R.id.signature_print);
        QRImage = findViewById(R.id.qr_image_view);

        print = findViewById(R.id.print_ticket);

        //VIOLATION PREFERENCE
        SharedPreferences preferences1 = getSharedPreferences("violation", MODE_PRIVATE);
        finalPrice = preferences1.getInt("final_price", 0);
        finalString = preferences1.getString("combined_strings", "");
        combined_price = preferences1.getString("combined_price", "");
        savedCurrentDateTime = preferences1.getString("CurrentDateTime", "");
        //MORE DETAILS PREFERENCE
        SharedPreferences preferences2 = getSharedPreferences("more_details", Context.MODE_PRIVATE);
        savedUniqueID = preferences2.getString("uniqueID", "");
        savedName = preferences2.getString("Name", "");
        savedLicense = preferences2.getString("License", "");
        savedPlate = preferences2.getString("Plate", "");
        savedCER = preferences2.getString("CER", "");
        savedOR = preferences2.getString("OR", "");
        savedLocation = preferences2.getString("Location", "");
        savedOfficer = preferences2.getString("Officer", "");
        savedQrContent = preferences2.getString("qrContent", "");
        //SIGNATURE, QR CODE, CER, OR
        filepathSign = getIntent().getStringExtra("signatureBitmap");
        filepathQR = getIntent().getStringExtra("qrcodeBitmap");
        bitmapLicense = getIntent().getParcelableExtra("bitmapLicense");
        //FIXING FORMAT TOTAL PRICE
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###");
        formattedPrice = decimalFormat.format(finalPrice);
        //FIREBASE
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        //REPLACE \N
        newFinalString = finalString.replace("\n", "\\\\n");

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });
    }

    private void BluetoothLoading() {
        loadingDialog.loadingAlertDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
                selectBluetoothDevice();
            }
        }, 3000);
    }

    private void checkConnection() {
        if (ContextCompat.checkSelfPermission(this, BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{BLUETOOTH},
                    REQUEST_CODE);
        }

        if (bluetoothAdapter == null) {
            Toast.makeText(Preview.this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                BluetoothLoading();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth has been enabled, proceed to selectBluetoothDevice
                BluetoothLoading();
            } else {
                // The user did not enable Bluetooth, handle accordingly
                Toast.makeText(Preview.this, "Bluetooth was not enabled. Please enable it.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectBluetoothDevice() {
        if (ContextCompat.checkSelfPermission(this, BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{BLUETOOTH_CONNECT},
                    REQUEST_CODE);
        }

        Dialog dialog = new Dialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_device_selection, null);
        dialog.setContentView(dialogView);

        // Find the ListView in the dialog layout
        ListView deviceList = dialogView.findViewById(R.id.deviceList);

        // Get a list of paired Bluetooth devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        final List<BluetoothDevice> devicesList = new ArrayList<>(pairedDevices);

        // Create an ArrayAdapter to display the device names in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (BluetoothDevice device : devicesList) {
            adapter.add(device.getName());
        }

        deviceList.setAdapter(adapter);

        // Set an item click listener for device selection
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice selectedDevice = devicesList.get(position);
                openBluetoothConnection(selectedDevice);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void openBluetoothConnection(BluetoothDevice myDevice){

        DeviceConnection connection = new BluetoothConnection(myDevice);
        try {
            connection.connect();
            EscPosPrinter printer = new EscPosPrinter(connection, printerDpi, printerWidth, printerNbrCharacters);
             printer.printFormattedText(
                            "[L]\n" +
                            "[C]SCAN ME\n" +
                            "[C]<qrcode size='40'>"+savedQrContent+"</qrcode>\n" +
                            "[C]"+"================================\n" +
                            "[C]<u><font size='big'>VIOLATION</font></u>\n"+
                            "[L]"+finalString+"\n"+
                            "[C]"+"--------------------------------\n" +
                            "[R]TOTAL PRICE :[R]"+formattedPrice+"\n" +
                            "[C]"+"--------------------------------\n" +
                            "[C]"+"--------------------------------\n" +
                            "[L]"+PaymentDetails+"\n"

            );
            uploadingDialog.loadingAlertDialog();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    UploadFireBase();
                }
            }, 5000);
            connection.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void UploadFireBase() {
        signatureBitmap = BitmapFactory.decodeFile(filepathSign);
        qrCodeBitmap = BitmapFactory.decodeFile(filepathQR);

        //CONVERTING
        ByteArrayOutputStream signatureBaos = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, signatureBaos);
        byte[] signatureData = signatureBaos.toByteArray();
        ByteArrayOutputStream qrCodeBaos = new ByteArrayOutputStream();
        qrCodeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, qrCodeBaos);
        byte[] qrCodeData = qrCodeBaos.toByteArray();
        //STRINGS
        signatureFileName = "signature_" + System.currentTimeMillis() + ".jpg";
        signatureFilePath = "images/" + signatureFileName;
        qrCodeFileName = "qrcode_" + System.currentTimeMillis() + ".jpg";
        qrCodeFilePath = "images/" + qrCodeFileName;
        //STORAGE REFERENCE
        StorageReference signatureRef = mStorageRef.child(signatureFilePath);
        StorageReference qrCodeRef = mStorageRef.child(qrCodeFilePath);
        //UPLOADING IMAGE
        UploadTask signatureUploadTask = signatureRef.putBytes(signatureData);
        UploadTask qrCodeUploadTask = qrCodeRef.putBytes(qrCodeData);
        //CONVERTING TO URL
        signatureRef.getDownloadUrl();

        List<Task<?>> uploadTasks = new ArrayList<>();
        uploadTasks.add(signatureUploadTask);
        uploadTasks.add(qrCodeUploadTask);

        Task<?>[] tasksArray = new Task[uploadTasks.size()];
        uploadTasks.toArray(tasksArray);

        Task<Void> allTasks = Tasks.whenAll(tasksArray);

        allTasks.addOnSuccessListener(task -> {
                    signatureRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String signatureImageUrl = uri.toString();
                        qrCodeRef.getDownloadUrl().addOnSuccessListener(qrUri -> {
                            String qrCodeImageUrl = qrUri.toString();
                            additionalData(signatureImageUrl, qrCodeImageUrl);
                            uploadingDialog.dismissDialog();
                            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Preview.this, Dashboard.class);
                            startActivity(intent);
                        });
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Please Make Sure That You Are Connected To The Internet", Toast.LENGTH_SHORT).show();
                });
    }

    private void additionalData(String signatureImageUrl, String qrCodeImageUrl){
        DatabaseReference userRef = mDatabaseRef.child("Information").child(userUid);
        DatabaseReference newRecordRef = userRef.push();
        newRecordRef.child("TransactionID").setValue(savedUniqueID);
        newRecordRef.child("Name").setValue(savedName);
        newRecordRef.child("Violation").setValue(newFinalString);
        newRecordRef.child("TotalPrice").setValue(formattedPrice);
        newRecordRef.child("LicenseNumber").setValue(savedLicense);
        newRecordRef.child("PlateNumber").setValue(savedPlate);
        newRecordRef.child("Location").setValue(savedLocation);
        newRecordRef.child("DateAndTime").setValue(savedCurrentDateTime);
        newRecordRef.child("HandledBy").setValue(savedOfficer);
        newRecordRef.child("OR").setValue(savedOR);
        newRecordRef.child("CER").setValue(savedCER);
        newRecordRef.child("Status").setValue(Pending);

        newRecordRef.child("signatureImageUrl").setValue(signatureImageUrl);
        newRecordRef.child("qrCodeImageUrl").setValue(qrCodeImageUrl);
    }
}
