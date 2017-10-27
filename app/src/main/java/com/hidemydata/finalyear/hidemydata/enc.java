/*
Encyption using caesor cipher. can use other algorithms.

 */

package com.hidemydata.finalyear.hidemydata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class enc extends AppCompatActivity {
    Uri imageuri;
    static String ALPHABET="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890./? ";
    static int i,k=0;
    String key, temp;
    String monokey="";
    int depth=3;
    long start,end,duration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_enc);
        imageuri=getIntent().getData();
        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("text");
        int level=Integer.parseInt(bundle.getString("level"));


        /**************************************************************
         //future use : if input by file
         /*

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,path);
        /
        String text="";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line="";

            while ((line = br.readLine()) != null) {
                text+=line;

            }
            br.close();
        }
        catch (IOException e) {

            //You'll need to add proper error handling here
        }

        */
        //*************************************************************

        //Encrypting the file
        Random rand = new Random();

        int  n = rand.nextInt(66) + 0;
        if(level==1) {
            start=System.nanoTime();
            text=encryptByRailFence(text);
            end=System.nanoTime();
            duration=end-start;
            Log.i("encode rail fence",""+duration);
        }
        else if (level==2) {
            start=System.nanoTime();
            text=encryptByCieser(text, n);
            end=System.nanoTime();
            duration=end-start;
            Log.i("encode cieser",""+duration);
        }
        else {
            start=System.nanoTime();
            text=encryptByMono(text);
            end=System.nanoTime();
            duration=end-start;
            Log.i("encode Mono",""+duration);
            monokey+=text;
            text=monokey;
        }

        text=makekey(text,n,level);
        Intent x=new Intent(this,hide.class);
        x.setData(imageuri);
        x.putExtra("key",temp);
        x.putExtra("text",text);
        startActivity(x);

    }

    private String makekey(String text, int n, int level) {
        int len=text.length();
         key="";
        if(len<10) {
            key+='0';
            key+=len;
        }
        else if (len>=100){
            Random rand = new Random();

            int  k = rand.nextInt(66) + 10;
            key+=k;
        }
        else {
            key+=len;
        }
        key+=String.valueOf(level);
        if(n<10){
            key+='0';
            key+=n;
        }
        else {
            key+=n;
        }
        temp=key;
        key+=text;
        return key;

    }

    public String encryptByCieser(String plainText, int shiftKey)
    {
        String cipherText = "";
        for (int i = 0; i < plainText.length(); i++)
        {

            int charPosition = ALPHABET.indexOf(plainText.charAt(i));
            int keyVal = (shiftKey + charPosition) % 66;
            char replaceVal = ALPHABET.charAt(keyVal);
            cipherText += replaceVal;
        }
        return cipherText;
    }

    public String encryptByRailFence(String plainText)
    {
        int r=depth,len=plainText.length();
        int c=len/depth;
        char mat[][]=new char[r][c];
        int k=0;

        String cipherText="";

        for(int i=0;i< c;i++)
        {
            for(int j=0;j< r;j++)
            {
                if(k!=len)
                    mat[j][i]=plainText.charAt(k++);
                else
                    mat[j][i]='*';
            }
        }
        for(int i=0;i< r;i++)
        {
            for(int j=0;j< c;j++)
            {
                cipherText+=mat[i][j];
            }
        }
        return cipherText;
    }

    public String encryptByMono(String text) {
        ArrayList al=new ArrayList(Arrays.asList(ALPHABET));
        Collections.shuffle(al);
         monokey=al.toString();

        char c[] = new char[(text.length())];
        for (int i = 0; i < text.length(); i++)
        {
            for (int j = 0; j < 66; j++)
            {
                if (text.charAt(i) == ALPHABET.charAt(j))
                {
                    c[i] = monokey.charAt(j);
                    break;
                }
            }
        }
        return (new String(c));

    }


}
