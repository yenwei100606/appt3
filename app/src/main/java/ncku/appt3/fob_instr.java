package ncku.appt3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class fob_instr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fob_instr);
        getSupportActionBar().hide();//隱藏tTitleBar
    }
    public void clickNext(View v)
    {
        Intent intent = new Intent(this,FOB.class);
        startActivity(intent);
    }

    public void backtoHome(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void clickBack(View v)
    {

    }
}
