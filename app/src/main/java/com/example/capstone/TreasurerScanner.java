package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class TreasurerScanner extends AppCompatActivity {
    private String qrResultEncrypted;
    private String qrResultDecrypted;
    private DecoratedBarcodeView zxingScannerView;
    private Button next, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasurer_scanner);

        zxingScannerView = findViewById(R.id.zxingScannerView);

        zxingScannerView.getStatusView().setText("SCAN QR CODE");
        next = findViewById(R.id.toNextActivity);
        logout = findViewById(R.id.logout);
        BarcodeCallback callback = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                try {
                    qrResultEncrypted = result.getText();
                    qrResultDecrypted = Crypto.decrypt(qrResultEncrypted);

                    if (!qrResultDecrypted.isEmpty()){
                        logout.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);
                        Toast.makeText(TreasurerScanner.this, "Successfully Scanned", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(TreasurerScanner.this, "Decryption Failed", Toast.LENGTH_SHORT).show();
                }

                zxingScannerView.resume();
            }

            @Override
            public void possibleResultPoints(List resultPoints) {

            }
        };

        zxingScannerView.decodeContinuous(callback);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TreasurerScanner.this, UpdatingPayment.class);
                intent.putExtra("id", qrResultDecrypted);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TreasurerScanner.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        zxingScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zxingScannerView.pause();
    }
}