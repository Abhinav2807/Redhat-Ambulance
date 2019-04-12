package com.example.android.redhatambulance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void signin_activity(View view)
    {
        Intent i = new Intent(this,signin.class);
        startActivity(i);
    }
    public void signin_driver_activity(View view)
    {
        Intent i = new Intent(this,driver_signin.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
