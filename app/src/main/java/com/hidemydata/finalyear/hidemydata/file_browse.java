package com.hidemydata.finalyear.hidemydata;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class file_browse extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText et;
    Uri uri;
    static int flag;
    String text;
    static int level=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browse);
        et=(EditText) findViewById(R.id.editText2);
         uri=getIntent().getData();
        flag=0;
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Normal");
        categories.add("Medium");
        categories.add("Ninja");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void next_enc (View view) {
            text=et.getText().toString();
            if(text.length() == 0) {
                Toast t = Toast.makeText(getApplicationContext(), "Please Enter the message First", Toast.LENGTH_SHORT);
                t.show();
         } else {
                Intent x = new Intent(this, enc.class);
                x.putExtra("text", text);
                x.putExtra("level",String.valueOf(level));
                x.setData(uri);
                startActivity(x);
            }
        }


    public void back_img(View view) {
        Intent x=new Intent(this,enc_browse.class);
        x.setData(uri);
        startActivity(x);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if(item.equals("Normal")) {
            level=1;
        }
        else if (item.equals("Medium")) {
            level=2;
        }
        else {
            level=3;
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        level=1;
    }

}
