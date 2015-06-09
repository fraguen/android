package com.iut.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by William Decool (william.decool@gmail.com) and  Alexandre Bouzat (alexandre.bouzat@gmail.com)
 * with BarcodeScanner library helps on https://github.com/dm77/barcodescanner
 */

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        //R.menu.menu est l'id de notre menu
        inflater.inflate(R.menu.menu_scanner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.action_flash_on:
                mScannerView.setFlash(true);
                return true;

            case R.id.action_flash_off:
                mScannerView.setFlash(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
