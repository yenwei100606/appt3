package ncku.appt3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Scan_QRcode extends AppCompatActivity {

    private IntentIntegrator scanIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__qrcode);


        scanIntegrator = new IntentIntegrator(Scan_QRcode.this);
        scanIntegrator.setPrompt("請掃描");
        scanIntegrator.initiateScan();
        scanIntegrator.setOrientationLocked(false);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String scanContent = result.getContents();
            String scanFormat = result.getFormatName();
            background bg = new background(this);
            bg.execute(scanContent);

        }
    }
}
