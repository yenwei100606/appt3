package ncku.appt3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class urs_result extends AppCompatActivity{

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private final String tag = getClass().getName();

    MyDBHandler_re dbHandler;

    TextView urs_resultTV,urs_resultTime;

    public String sqlt,result;
    private Calendar mCalendar;
    private ListView list;
    DateFormat sdf;
    private String str;
    long i;
    String[] item_name={"(白血球)","(亞硝酸鹽)","(URO,尿膽元素)","(尿蛋白)","(酸鹼值)","(OB,滑血反應)","(比重)","(酮體)","(膽紅素)","(尿糖)"};
    String[] item_en_name={"Leukocytes","Nitrite","Urobilinogen","Protein","pH","Blood","Specific Gravity","Ketone","Bilirubin","Glucose"};
    String[][] detect_result={{"NEGATIVE 0","TRACE 15","SMALL(+) 70","MODERATE(++)125","LARGE(+++)500ca CELL S/μL"},
                            {"NEGATIVE","POSITIVE","POSITIVE",},
                            {"0.2 EHRLICH UNITS/dL URINE\n3.2μmol/L","1 EHRLICH UNITS/dL URINE\n16μmol/L","2 EHRLICH UNITS/dL URINE\n32μmol/L","4EHRLICH UNITS/dL URINE\n64μmol/L","8EHRLICH UNITS/dL URINE\n128μmol/L"},
                            {"NEGATIVE","TRACE","30(+)mg/dL\n0.3g/L","100(++)mg/dL\n1.0g/L","300(+++)mg/dL\n3.0g/L",">2000(++++)mg/dL\n>20g/L"},
                            {"5.0","6.0","6.5","7.0","7.5","8.0","8.5"},
                            {"NEGATIVE","NON-HEMOLYZED\n10 TRACE","HEMOLYZED\nTRACE","SMALL(+)\n25 ca cells/μL","MODERATE(++)\n80 ca cells/μL ","LARGE(+++)\n200ca cells/μL"},
                            {"1.000","1.005","1.010","","1.015","1.020","1.025","1.030"},
                            {"NEGATIVE","TRACE(5mg/dL)\n0.5mmol/L","SMALL(15mg/dL)\n1.5mmol/L","MODERATE(40mg/dL)\n4.0mmol/L","LARGE\n8.0mmol/L","LARGE(160mg/dL)\n16mmol/L"},
                            {"NEGATIVE","SMALL(+)","MODERATE(++)","LARGE(+++)",},
                            {"NEGATIVE","100mg/dL\n5mmol/L","250(+)mg/dL\n15mmol/L","500(++)mg/dL\n30mmol/L","1000(+++)mg/dL\n60mmol/L",">2000(++++)mg/dL\n110mmol/L"}};
    String test = "1  3  5  5  6  6  7  6  4  6";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urs_result);
        getSupportActionBar().hide();//隱藏TitleBar

        urs_resultTime = (TextView)findViewById(R.id.urs_result);
        list = (ListView)findViewById(R.id.list);

        dbHandler = new MyDBHandler_re(this, null, null, 1);

        new urs_result.AsyncRetrieve().execute();
    }

    private class AsyncRetrieve extends AsyncTask<String,String,String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(tag,"OnPreExecute");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://140.116.226.182/mems_client/355167087073043/urs.php");
                //url = new URL("http://140.116.226.182/mems_client/testAndroid/urs.php");
                //SAMSUNG
                //SONY 358096071301846

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d(tag,"doInBackground 1 error");
                return e.toString();
            }

            try {

                // Setup HttpURLConnection class to send and receive data from php
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.d(tag,"doInBackground 2 error");
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line ="";

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    sqlt=result.toString();

                    return sqlt;

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(tag,"doInBackground 3 error");
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result){

            //String[] result_arr=sqlt.split("  ");
            String[] result_arr=test.split("  ");
            //Log.e("result","結果"+sqlt);
            Log.e("Tag","結果"+test);
            Log.e("Tag","結果"+result_arr.length+"個");
            mCalendar = Calendar.getInstance();
            //sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            sdf = new SimpleDateFormat("yyyy/MM/dd");
            str = sdf.format(mCalendar.getTime());
            urs_resultTime.setText(str+"\nURS測試結果");
            Log.d("Tag","Time"+str);
            object_re object = new object_re(sqlt,str);
            i=0;
            i=dbHandler.addProduct(object);
            List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
            if(result_arr.length==10)
            {
                for (int i = 0; i < item_name.length; i++) {
                    Map item = new HashMap();
                    item.put("item_name", item_name[i]);
                    item.put("item_en_name", item_en_name[i]);
                    //item.put("result_arr",detect_result[i][0]);
                    /*for(int j=0;j<8;j++) {
                        if (Integer.parseInt(result_arr[i]) == j) {
                            Log.e("Tag","項目"+i+j);
                            item.put("result_arr", detect_result[i][j-1]);
                        }*/
                    if(Integer.parseInt(result_arr[i])==1)
                    {
                        item.put("result_arr","正常");
                    }else{
                        item.put("result_arr","異常");
                    }
                    //item.put("result_arr",detect_result[i][0]);
                    items.add(item);
                }
            }else{
                Toast.makeText(urs_result.this,sqlt,Toast.LENGTH_LONG).show();
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(urs_result.this,items,R.layout.urs_result_list,new String[]{"item_name","item_en_name","result_arr"},new int[]{R.id.item_name,R.id.item_en_name,R.id.result});
            //SimpleAdapter simpleAdapter = new SimpleAdapter(urs_result.this,items,R.layout.urs_result_list,new String[]{"item_name","item_en_name",},new int[]{R.id.item_name,R.id.item_en_name});
            list.setAdapter(simpleAdapter);

        }

    }

    public void clickBack(View v){
        //Intent i = new Intent(this,MainActivity.class);
        Intent i = new Intent();
        i.setClass(this, URS.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    public void backtoHome(View v)
    {
        Intent i = new Intent();
        i.setClass(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
