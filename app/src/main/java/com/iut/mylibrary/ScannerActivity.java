package com.iut.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("RESULT", rawResult.getText()); // Prints scan results
        Toast.makeText(
                ScannerActivity.this,
                "ISBN : " + rawResult.getText(),
                Toast.LENGTH_LONG
        ).show();
        Log.v("FORMAT", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        String ISBN = rawResult.getText();
        String url = "http://www.worldcat.org/isbn/" + ISBN;
        Intent i = new Intent(ScannerActivity.this, ViewInfoBookActivity.class);
        i.putExtra("url", url);
        i.putExtra("isbn", ISBN);
        startActivity(i);
        finish();
    }
}
