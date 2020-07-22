package com.ione.iseller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarCodeScanActivity extends AppCompatActivity {

    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private SurfaceView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scan);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        cameraView.setZOrderMediaOverlay(true);

        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();
        if(!barcodeDetector.isOperational()) {
            Toast.makeText(getApplicationContext(), "Sorry couldn't start. Try again!!"
                    , Toast.LENGTH_SHORT).show();
            this.finish();
        }

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedFps(24)
                .setAutoFocusEnabled(true).setRequestedPreviewSize(1920, 1024)
                .build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {}

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodeSparseArray= detections.getDetectedItems();
                if (barcodeSparseArray.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("barcode", barcodeSparseArray.valueAt(0));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.getHolder().addCallback( new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(BarCodeScanActivity.this
                            , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraView.getHolder());
                    }
                } catch (IOException e) {
                    Toast.makeText(BarCodeScanActivity.this, "Couldn't start camera. Try again"
                            , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
        barcodeDetector.release();
    }
}
