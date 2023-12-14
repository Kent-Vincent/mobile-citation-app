package com.example.capstone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    CircleImageView image;
    TextView user;
    String email, newEmail;

    CardView createNewTicket, transaction, scan, logout;
    private String Name, LicenseNo, PlateNo;
    private String encryptedQRCode, decryptedQRCode;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1002;
    private static final int CAMERA_REQUEST = 1003;
    private static final int GALLERY_REQUEST = 1004;

    @Override
    protected void onStart() {
        super.onStart();
        user.setText(newEmail);
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference userImageRef = storageReference.child("user_images").child(userId + ".jpg");

            userImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri imageUri = task.getResult();
                        Picasso.get()
                                .load(imageUri)
                                .into(image, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        //Success
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Toast.makeText(Dashboard.this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Dashboard.this, "No Image Has Been Detected", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        image = findViewById(R.id.imageChange);

        createNewTicket = findViewById(R.id.ct1);
        transaction = findViewById(R.id.ct2);
        scan = findViewById(R.id.ct3);
        logout = findViewById(R.id.ct4);
        user = findViewById(R.id.text_dashboard);

        //FOR USER
        SharedPreferences sp = getApplicationContext().getSharedPreferences("userEmail", Context.MODE_PRIVATE);
        email = sp.getString("email", "");

        //WORD REMOVER
        String wordToRemove = "@gmail.com";
        newEmail = email.replace(wordToRemove, "");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        createNewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, CreateTicketNew.class);
                startActivity(intent);
            }
        });

        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, TransactionHistory.class);
                startActivity(intent);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCode();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageSourceDialog();
            }
        });

    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_choice_camera_gallery, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();

        CardView cameraButton = customLayout.findViewById(R.id.camera_button);
        CardView galleryButton = customLayout.findViewById(R.id.gallery_button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission();
                dialog.dismiss();
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, open the camera
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Storage permissions granted, open the gallery
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                uploadImageToFirebase(imageBitmap);
            } else if (requestCode == GALLERY_REQUEST) {
                Uri selectedImageUri = data.getData();
                uploadHeicImageToFirebase(selectedImageUri);
            }
        }
    }

    private void uploadImageToFirebase(Bitmap imageBitmap) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference userImageRef = storageReference.child("user_images").child(userId + ".jpg");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = userImageRef.putBytes(data);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //String imageUrl = uri.toString(); to display IMAGE URL
                                recreate();
                            }
                        });
                    } else {
                        Toast.makeText(Dashboard.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void uploadHeicImageToFirebase(Uri heicImageUri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(heicImageUri), null, options);

            // Check and adjust image orientation
            int orientation = getOrientation(heicImageUri);
            if (orientation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            // Convert to JPEG
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] jpegData = byteArrayOutputStream.toByteArray();

            if (currentUser != null) {
                String userId = currentUser.getUid();
                StorageReference userImageRef = storageReference.child("user_images").child(userId + ".jpg");

                UploadTask uploadTask = userImageRef.putBytes(jpegData);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //String imageUrl = uri.toString(); to display IMAGE URL
                                    recreate(); // Refresh the activity
                                }
                            });
                        } else {
                            // Handle the upload failure
                            Toast.makeText(Dashboard.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error converting HEIC to JPEG", Toast.LENGTH_SHORT).show();
        }
    }

    private int getOrientation(Uri uri) {
        try {
            ExifInterface exif = new ExifInterface(uri.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                case ExifInterface.ORIENTATION_NORMAL:
                    return 300;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void ScanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            try {
                encryptedQRCode = result.getContents();
                decryptedQRCode = Crypto.decrypt(encryptedQRCode);

                String[] qrFields = decryptedQRCode.split("\n");

                for (String field : qrFields) {
                    if (field.startsWith("Name")) {
                        Name = field.replace("Name: ", "");
                    } else if (field.startsWith("License Number")) {
                        LicenseNo = field.replace("License Number: ", "");
                    } else if (field.startsWith("Plate Number")) {
                        PlateNo = field.replace("Plate Number: ", "");
                    }
                }

                countMatchingEntries(LicenseNo);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Decryption Failed", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private void countMatchingEntries(String licenseNo) {
        DatabaseReference uploadsRef = databaseReference.child("uploads").child("Information");

        uploadsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot entrySnapshot : userSnapshot.getChildren()) {
                            String retrievedLicenseNo = entrySnapshot.child("LicenseNumber").getValue(String.class);

                            if (retrievedLicenseNo != null && retrievedLicenseNo.equals(licenseNo)) {
                                count++;
                            }
                        }
                    }
                }

                // Process the count as needed
                handleCountResult(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("CountEntries", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void handleCountResult(int count) {
        String countResult = String.valueOf(count);

        Toast.makeText(Dashboard.this, "Number of matching entries: " + countResult, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setMessage("Decrypted QR Code:\n" + decryptedQRCode +
                "\nNumber of matching entries: " + countResult);
        builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }
}