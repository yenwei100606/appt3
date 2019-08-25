package ncku.appt3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Start_scan extends AppCompatActivity {
    //private Button start,exit;
    private IntentIntegrator scanIntegrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_scan);

        //start = (Button)findViewById(R.id.btn_start);
        //exit = (Button)findViewById(R.id.btn_exit);

    }

    public void clickStart(View v)
    {
        scanIntegrator = new IntentIntegrator(Start_scan.this);
        scanIntegrator.setCaptureActivity(Scan_Activity.class);
        scanIntegrator.setPrompt("請掃描");
        scanIntegrator.initiateScan();
        scanIntegrator.setOrientationLocked(false);

    }

    public void clickExit(View v)
    {
        ConfirmExit();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String scanContent = result.getContents();
            String scanFormat = result.getFormatName();

            //background bg = new background(this);
            //bg.execute(scanContent);
        }


    }

    public void ConfirmExit(){

        AlertDialog.Builder ad=new AlertDialog.Builder(Start_scan.this); //創建訊息方塊

        ad.setTitle("離開");

        ad.setMessage("確定要離開?");

        AlertDialog.Builder 是 = ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

            public void onClick(DialogInterface dialog, int i) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }

        });

        ad.setNegativeButton("否",new DialogInterface.OnClickListener() { //按"否",則不執行任何操作

            public void onClick(DialogInterface dialog, int i) {

            }

        });

        ad.show();//顯示訊息視窗

    }



}
