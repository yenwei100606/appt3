package ncku.appt3;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

public class create_account extends AppCompatActivity {
    private EditText name,birthday,email,password,password_check,address;
    private RadioButton sexF,sexM;
    private RadioGroup radiosex;
    private  String[] type = new String[] {"台北市","新北市","桃園市","台中市","台南市","高雄市"};
    private String[] city = new String[]{};
    private String[][] type2 = new String[][]{{"中正區","大同區","中山區","中山區","松山區","大安區","萬華區","信義區","士林區","北投區","內湖區","南港區","文山區"},{""},{},{},{},{}};
    private Spinner sp,sp2;
    private Context context;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().hide();//隱藏TitleBar

        context = this;
        name=(EditText)findViewById(R.id.edt_name);
        birthday=(EditText)findViewById(R.id.edt_bir);
        email=(EditText)findViewById(R.id.edt_mail);
        //address=(EditText)findViewById(R.id.edt_address);
        password=(EditText)findViewById(R.id.edt_psw);
        password_check=(EditText)findViewById(R.id.edt_checkpsw);
        sexM=(RadioButton)findViewById(R.id.radio_M);
        sexF=(RadioButton)findViewById(R.id.radio_F);
        radiosex = (RadioGroup)findViewById(R.id.radio_Sex);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.type);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(selectListener);

        //因為下拉選單第一個為茶類，所以先載入茶類群組進第二個下拉選單
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, city);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2 = (Spinner) findViewById(R.id.type2);
        sp2.setAdapter(adapter2);

    }


    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View v, int position,long id){
            //讀取第一個下拉選單是選擇第幾個
            int pos = sp.getSelectedItemPosition();
            //重新產生新的Adapter，用的是二維陣列type2[pos]
            adapter2 = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, type2[pos]);
            //載入第二個下拉選單Spinner
            sp2.setAdapter(adapter2);

        }

        public void onNothingSelected(AdapterView<?> arg0){

        }

    };


    public void clickCreate(View view)
    {
        int sexID=radiosex.getCheckedRadioButtonId();
        String Get_name=name.getText().toString();
        String Get_bir=birthday.getText().toString();
        String Get_email=email.getText().toString();
        String Get_addr=sp.getSelectedItem().toString()+sp2.getSelectedItem().toString();
        String Get_password="";
        String Get_sex="";
        if(sexID==0)
            Get_sex="F";
        else
            Get_sex="M";

        //確認格式
        boolean check_Empty=name.getText().toString().matches("") || email.getText().toString().matches("")||password.getText().toString().matches("") ||
                password_check.getText().toString().matches("");
        boolean check_pswEqual=password.getText().toString().equals(password_check.getText().toString());
        boolean check_emailRegExp=Get_email.matches("[a-zA-Z0-9._]+@([a-zA-Z0-9_]+.[a-zA-Z0-9_]+)+");
        boolean check_birthday=Get_bir.matches("[1-9]{4}-[0-1][0-9]-[0-9]{2}");

        if(check_Empty)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(create_account.this)
                    .setTitle("Error")
                    .setMessage("重要欄位不可為空")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }else if(!check_pswEqual)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(create_account.this)
                    .setTitle("Error")
                    .setMessage("密碼確認錯誤\n請重新輸入")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            password.setText("");
                            password_check.setText("");
                        }
                    });
            builder.show();
        } else if(!check_emailRegExp){
            AlertDialog.Builder builder = new AlertDialog.Builder(create_account.this)
                    .setTitle("Error")
                    .setMessage("信箱格式錯誤\n請按照參考格式重新輸入")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            email.setText("");

                        }
                    });
            builder.show();

        } else if(!check_birthday)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(create_account.this)
                    .setTitle("Error")
                    .setMessage("生日格式錯誤\n請按照參考格式重新輸入")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            birthday.setText("");
                        }
                    });
            builder.show();
        } else {
            Get_password=password.getText().toString();
            CreateAcc CA = new CreateAcc(this);
            CA.execute(Get_name, Get_sex, Get_bir, Get_email, Get_password,Get_addr);

        }
    }

    public class CreateAcc extends AsyncTask<String,Void,String> {


        android.app.AlertDialog dialog;
        Context context;
        public CreateAcc(Context context){this.context = context;}

        @Override
        protected void onPreExecute()
        {
            dialog = new android.app.AlertDialog.Builder(context).create();
            dialog.setTitle("Login Result");
            //dialog.setMessage("Successful");

        }

        @Override
        protected void onPostExecute(String s)
        {
            dialog.setMessage(s);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... data) {String result="";
            String name=data[0];
            String sex=data[1];
            String bir=data[2];
            String email=data[3];
            String password=data[4];
            String addr = data[5];

            //String connstr="http://140.116.226.182/mems_client/355758080228806//create_account.php";
            String connstr="http://192.168.0.108:8080/create_account.php";
            //String connstr="http://192.168.0.117/mySQL_exe/addInformation.php";
            //String connstr="http://192.168.0.108:8080/psw_login.php";
            //String connstr="http://140.116.226.182/mems_main/addInformation.php";
            try{
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter write = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));

                String all_data = URLEncoder.encode("addName","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")
                        + "&&" +URLEncoder.encode("addSex","UTF-8")+"="+URLEncoder.encode(sex,"UTF-8")
                        + "&&" +URLEncoder.encode("addBirth","UTF-8")+"="+URLEncoder.encode(bir,"UTF-8")
                        + "&&" +URLEncoder.encode("addEmail","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")
                        + "&&" +URLEncoder.encode("addPassword","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")
                        + "&&" +URLEncoder.encode("addAdd","UTF-8")+"="+URLEncoder.encode(addr,"UTF-8");

                //String all_data = URLEncoder.encode("addName","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");

                write.write(all_data);
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

    public void backtoHome(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
