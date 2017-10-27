package com.hidemydata.finalyear.hidemydata;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class dec_key extends AppCompatActivity {
    Uri imageuri;
    EditText et;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dec_key);
        imageuri=getIntent().getData();
        et=(EditText) findViewById(R.id.editText);
    }

    public void back_dec_browse (View view) {
        Intent x=new Intent(this,dec_browse.class);
        x.setData(imageuri);
        startActivity(x);
    }

    public  void  next_dec (View view) {

            key=et.getText().toString();
            if(key.length()<4) {
                Toast t = Toast.makeText(getApplicationContext(), "Please Enter a Valid Key", Toast.LENGTH_SHORT);
                t.show();
            }
            Intent x= new Intent(this,dec.class);
            x.setData(imageuri);
            x.putExtra("key",key);
            startActivity(x);

    }
}
