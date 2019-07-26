package ncku.appt3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.app.Activity;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {

    ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainImage = findViewById(R.id.mainImage);
    }

    public void clickFOB(View v){
        Intent i = new Intent(this,FOB.class);
        startActivity(i);
    }

    public void clickURS(View v){
        Intent i = new Intent(this,URS.class);
        startActivity(i);
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
        ConfirmExit();

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


    protected void onDestroy() {

        super.onDestroy();

        System.exit(0);
    }
}
