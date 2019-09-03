package ncku.appt3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

public class login extends AppCompatActivity {
    private EditText account,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();//隱藏tTitleBar

        account = (EditText) findViewById(R.id.edt_act);
        password = (EditText)findViewById(R.id.edt_psw);
    }
    public void clickLogin(View view)
    {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("資料");
        builder.setMessage("Your Account is\t"+account.getText()+"\nYour password is\t"+password.getText());
        builder.show();*/
        String act = account.getText().toString();
        String psw = password.getText().toString();

        login_info lg = new login_info(this);
        lg.execute(act,psw);

    }
    public void clickCreateAccount(View view)
    {
        Intent intent = new Intent(this,create_account.class);
        startActivity(intent);
    }
    public void clickExit(View view)
    {
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    protected void onDestroy() {

        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    private class login_info extends AsyncTask<String,Void,String> {
        AlertDialog dialog;
        Context context;
        public login_info(Context context){this.context = context;}

        @Override
        protected void onPreExecute()
        {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle("Error");
            //dialog.setMessage("Successful");
        }

        protected  void onPostExecute(String s)
        {
            //dialog.setMessage(s);
            //dialog.show();
            if(s.contains("success"))
            {
                Intent intent = new Intent();
                intent.setClass(login.this,MainActivity.class);
                startActivity(intent);

            }else{
                //dialog.setMessage("帳號密碼有誤");
                dialog.setMessage(s);
                dialog.show();
            }

        }

        @Override
        protected String doInBackground(String... voids) {
            String result="";
            String user=voids[0];
            String pass=voids[1];

            //String connstr="http://192.168.0.108:8080/psw_login.php";
            //String connstr="http://192.168.0.113:8080/psw_login.php";
            //String connstr="http://140.116.226.182/mems_client/355758080228806//psw_login.php";
            //String connstr="http://192.168.0.108:8080/CatchDataTest";
            String connstr="http://140.116.226.182/mems_main/selectDB2.php";
            try{
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter write = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
                String data = URLEncoder.encode("loginEmail","UTF-8")+"="+URLEncoder.encode(user,"UTF-8")
                        + "&&" +URLEncoder.encode("loginPassword","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");

                write.write(data);
                write.flush();
                write.close();
                ops.close();

                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
                String line="";
                while((line=reader.readLine()) != null) {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

    }
}
