package ncku.appt3;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {

    private IntentIntegrator scanIntegrator,scanIntegrator1;
    //ImageView mainImage;
    String check="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//隱藏tTitleBar

    }

    public void clickFOB(View v){
        Intent i = new Intent(this,FOB.class);
        startActivity(i);
        /*scanIntegrator = new IntentIntegrator(MainActivity.this);
        scanIntegrator.setCaptureActivity(Scan_Activity.class);
        scanIntegrator.setPrompt("請掃描");
        scanIntegrator.initiateScan();
        scanIntegrator.setOrientationLocked(false);
        check="FOB";*/
    }

    public void clickURS(View v){
        Intent i = new Intent(this,URS.class);
        startActivity(i);
        /*scanIntegrator1 = new IntentIntegrator(MainActivity.this);
        scanIntegrator1.setCaptureActivity(Scan_Activity.class);
        scanIntegrator1.setPrompt("請掃描");
        scanIntegrator1.initiateScan();
        scanIntegrator1.setOrientationLocked(false);
        check="URS";*/
    }

    public void clickHIS(View v){
        Intent i = new Intent(this,history_re.class);
        startActivity(i);
    }

    public void clickTRE(View v){
        Intent i = new Intent(this,trend.class);
        startActivity(i);
    }
    public void clickEXIT(View v)
    {
        //ConfirmExit();
        MainActivity.this.finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode==KeyEvent.KEYCODE_BACK))
        {

            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    public void ConfirmExit(){

        AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this); //創建訊息方塊

        ad.setTitle("離開");

        ad.setMessage("確定要離開?");

        AlertDialog.Builder 是 = ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

            public void onClick(DialogInterface dialog, int i) {
                /*
                                 MainActivity.this.finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                                */
                //finish();
                //
                MainActivity.this.finish();
            }

        });

        ad.setNegativeButton("否",new DialogInterface.OnClickListener() { //按"否",則不執行任何操作

            public void onClick(DialogInterface dialog, int i) {

            }

        });

        ad.show();//顯示訊息視窗

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String scanContent = result.getContents();
            String scanFormat = result.getFormatName();

            String title = scanContent.substring(0,2);
            String qrcode = scanContent.substring(2,scanContent.length());

            background bg = new background(this);
            bg.execute(title,qrcode,check);
            check="";
        }

    }
    public class background extends AsyncTask<String, Void,String> {

        android.app.AlertDialog dialog;
        Context context;
        public Boolean login = false;
        public background(Context context)
        {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            dialog = new android.app.AlertDialog.Builder(context).create();
            dialog.setTitle("Error");
            //dialog.setTitle("Login Status");
            //AlertDialog.Builder dialog = new AlertDialog.Builder(context);


        }
        @Override
        protected void onPostExecute(String s) {


            if(s.contains("FOBsuccessful"))
            {
                Intent intent_name = new Intent();
                intent_name.setClass(context.getApplicationContext(),fob_instr.class);
                context.startActivity(intent_name);

            }else if(s.contains("URSsuccessful")){
                Intent intent_name = new Intent();
                intent_name.setClass(context.getApplicationContext(),URS_intr.class);
                //intent_name.setClass(context.getApplicationContext(),URS.class);
                context.startActivity(intent_name);

            }else if(s.contains("FOBError")){
                dialog.setTitle("Error");
                dialog.setMessage("請注意，您所購買之產品為檢測FOB，請選擇FOB");
                dialog.show();
            }else if(s.contains("URSError")){
                //dialog.setTitle("Error");
                dialog.setMessage("請注意，您所購買之產品為檢測URS，請選擇URS");
                dialog.show();
            }else if(s.contains("QRcode exceed")){
                //dialog.setTitle("Error");
                dialog.setMessage("此QRcode已超過使用次數");
                dialog.show();
            }else{
                //dialog.setTitle("Error");
                dialog.setMessage("此QRcode已超過使用次數");
                dialog.show();
            }

        }
        @Override
        protected String doInBackground(String... voids) {
            String result = "";
            String title = voids[0];
            String qrcode = voids[1];
            String check = voids[2];
            //String pass = voids[1];

            //String connstr = "http://10.0.2.2:8080/Login.php";
            //String connstr = "http://140.116.226.171:8080/Login.php";
            //String connstr="http://192.168.0.108:8080/Login.php";
            String connstr="http://140.116.226.182/mems_main/qr_login.php";
            //String connstr="http://192.168.0.113:8080/qr_login.php";
            //String connstr="http://140.116.226.182/mems_client/355758080228806//Login.php";

            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
                String data = URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(title,"UTF-8")
                        + "&&" +URLEncoder.encode("qrcode","UTF-8")+"="+URLEncoder.encode(qrcode,"UTF-8")
                        + "&&" +URLEncoder.encode("check","UTF-8")+"="+URLEncoder.encode(check,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;

            } catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }


            return result;
        }

    }

    protected void onDestroy() {

        super.onDestroy();

        System.exit(0);
    }
}
