/*
*
*
*  Code for splash screen. Will sleep for 2 seconds
*
*/



package com.hidemydata.finalyear.hidemydata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class splash extends AppCompatActivity {
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_splash);
        iv=(ImageView)findViewById(R.id.imageView4);
        iv.setImageResource(R.drawable.main);
        /****** Create Thread that will sleep for 2 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2*1000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(i);

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }
}
