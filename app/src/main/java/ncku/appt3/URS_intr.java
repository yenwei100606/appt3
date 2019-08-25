package ncku.appt3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

public class URS_intr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urs_intr);
        getSupportActionBar().hide();//隱藏tTitleBar
    }

    public void backtoHome(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void clickNext(View view)
    {
        Intent intent = new Intent(this,URS.class);
        startActivity(intent);
    }
    public void clickBack(View v)
    {
        Intent intent = new Intent(this,URS.class);
        startActivity(intent);
    }

}
