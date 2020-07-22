package com.ione.iseller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

public class BarCodeLauncherActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_CODE = 100;
    private TextView barcodeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_launcher);

        Button scan = (Button) findViewById(R.id.scan_button);
        barcodeStatus = (TextView) findViewById(R.id.scan_status);

        scan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scan_button) {
            Intent intent = new Intent(this, BarCodeScanActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null){
                barcodeStatus.setVisibility(View.GONE);
                final Barcode barcode = data.getParcelableExtra("barcode");
                String barcode_content = barcode.rawValue;
                Intent intent = new Intent();
                intent.putExtra("BarcodeContent", barcode_content);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                barcodeStatus.setVisibility(View.VISIBLE);
            }
        }
    }
}
