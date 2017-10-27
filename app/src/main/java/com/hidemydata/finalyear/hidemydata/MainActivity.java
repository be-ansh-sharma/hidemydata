package com.hidemydata.finalyear.hidemydata;

/*
*
* Main activity [encrypt or decrypt]
*
*/
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void encrypt (View view) {
        Intent x = new Intent(this,enc_browse.class);

        startActivity(x);
    }

    public void decrypt (View view) {
        Intent x=new Intent(this,dec_browse.class);
        startActivity(x);
    }
}
