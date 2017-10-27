/*
*
* Decypting is done here using reverse caesor cipher.
*
*/
package com.hidemydata.finalyear.hidemydata;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

public class dec extends AppCompatActivity {
    static String ALPHABET="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890./? ";
    Uri imageurl;
    int shiftkey;
    TextView status, message;
    String key, text;
    InputStream imageStream;
    Bitmap image;
    int flag, depth=3;
    int level;
     static int[] binary = { 16, 8, 0 };
     static byte[] andByte = { (byte) 0xC0, 0x30, 0x0C, 0x03 };
    static int[] toShift = { 6, 4, 2, 0 };
    public static String END_MESSAGE_COSTANT = "#!@";
    public static String START_MESSAGE_COSTANT = "@!#";
    long start, end,duration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dec);
        imageurl=getIntent().getData();
        Bundle bundle = getIntent().getExtras();
         key = bundle.getString("key");
        status=(TextView) findViewById(R.id.textView12);
        message=(TextView) findViewById(R.id.textView13);
        start=System.nanoTime();
        decyrpt();
        end=System.nanoTime();
        duration=end-start;
        Log.i("decode picture", "" + duration);
        //check if key is valid or not
        flag=validate(text, key);
        if(flag==0) {
            status.setText(R.string.Error);
            Toast t = Toast.makeText(getApplicationContext(),R.string.Error, Toast.LENGTH_SHORT);
            t.show();
            Intent x=new Intent(this,dec_key.class);
            startActivity(x);
        }
        else {
            status.setText("DECODED SUCCESSFULLY");
            if(level==1) {
                start=System.nanoTime();
                text=decryptByRailFence(text);
                end=System.nanoTime();
                duration=end-start;
                Log.i("decode rail fence",""+duration);

            }
            else if (level==2) {
                shiftkey=makeshiftkey(key);
                start=System.nanoTime();
                text=decryptBycaeser(text, shiftkey);
                end=System.nanoTime();
                duration=end-start;
                Log.i("decode caeser",""+duration);
            }
            else {
                start=System.nanoTime();
                text=decryptByMono(text);
                end=System.nanoTime();
                duration=end-start;
                Log.i("decode mono",""+duration);
            }

            ;
            message.setText(text);

        }

    }

    private int makeshiftkey(String key) {
        int p;
        StringBuilder sb=new StringBuilder(key);
        sb.deleteCharAt(0);
        sb.deleteCharAt(0);
        sb.deleteCharAt(0);
        key=sb.toString();
        p=Integer.parseInt(key);

        return p;
    }

    int validate(String text1, String key) {
        int tmp=0;
        for(int i=0;i<5;i++)
        {
            if(text1.charAt(i) == key.charAt(i)) {
                tmp++;
            }
        }

        if(tmp==5) {
            StringBuilder sb=new StringBuilder(text1);
            sb.deleteCharAt(0);
            sb.deleteCharAt(0);
            level=Integer.parseInt(String.valueOf(sb.charAt(0)));
            sb.deleteCharAt(0);
            sb.deleteCharAt(0);
            sb.deleteCharAt(0);
            text=sb.toString();
            return 1;
        }

        return 0;
    }


    public void decyrpt() {
        /*
        Bitmap image = null;
        try {
            Cursor cursor = getContentResolver().query(imageurl, null, null,
                    null, null);
            cursor.moveToFirst();

            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String absoluteFilePath = cursor.getString(idx);

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inDither = false;
            opt.inScaled = false;
            opt.inDensity = 0;
            opt.inJustDecodeBounds = false;
            opt.inPurgeable = false;
            opt.inSampleSize = 1;
            opt.inScreenDensity = 0;
            opt.inTargetDensity = 0;
            image = BitmapFactory.decodeFile(absoluteFilePath,opt);

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        try {
            imageStream = getContentResolver().openInputStream(imageurl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        image = BitmapFactory.decodeStream(imageStream);
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(),
                image.getHeight());

        //for logging purpose


        Log.v("Decode", "" + pixels[0]);
        Log.v("Decode Alpha", "" + (pixels[0] >> 24 & 0xFF));
        Log.v("Decode Red", "" + (pixels[0] >> 16 & 0xFF));
        Log.v("Decode Green", "" + (pixels[0] >> 8 & 0xFF));
        Log.v("Decode Blue", "" + (pixels[0] & 0xFF));
        Log.v("Decode", "" + pixels[0]);
        Log.v("Decode", "" + image.getPixel(0, 0));
        byte[] b = null;
        try {
            b = convertArray(pixels);
        } catch (OutOfMemoryError er) {

        }
         text = decodeMessage(b, image.getWidth(), image
                .getHeight());


    }
//helper method
    public  byte[] convertArray(int[] array) {
        byte[] newarray = new byte[array.length * 3];

        for (int i = 0; i < array.length; i++) {

			/*
			 * newarray[i * 3] = (byte) ((array[i]) & 0xFF); newarray[i * 3 + 1]
			 * = (byte)((array[i] >> 8)& 0xFF); newarray[i * 3 + 2] =
			 * (byte)((array[i] >> 16)& 0xFF);
			 */

            newarray[i * 3] = (byte) ((array[i] >> 16) & 0xFF);
            newarray[i * 3 + 1] = (byte) ((array[i] >> 8) & 0xFF);
            newarray[i * 3 + 2] = (byte) ((array[i]) & 0xFF);

        }
        return newarray;
    }


    //**************************************************************************

    public static String decodeMessage(byte[] oneDPix, int imgCols,
                                       int imgRows) {

        Vector<Byte> v = new Vector<Byte>();

        String builder = "";
        int shiftIndex = 4;
        byte tmp = 0x00;
        for (int i = 0; i < oneDPix.length; i++) {
            tmp = (byte) (tmp | ((oneDPix[i] << toShift[shiftIndex
                    % toShift.length]) & andByte[shiftIndex++ % toShift.length]));
            if (shiftIndex % toShift.length == 0) {
                v.addElement(new Byte(tmp));
                byte[] nonso = { (v.elementAt(v.size() - 1)).byteValue() };
                String str = new String(nonso);
                // if (END_MESSAGE_COSTANT.equals(str)) {
                if (builder.endsWith(END_MESSAGE_COSTANT)) {
                    break;
                } else {
                    builder = builder + str;
                    if (builder.length() == START_MESSAGE_COSTANT.length()
                            && !START_MESSAGE_COSTANT.equals(builder)) {
                        builder = null;
                        break;
                    }
                }

                tmp = 0x00;
            }

        }
        if (builder != null)
            builder = builder.substring(START_MESSAGE_COSTANT.length(), builder
                    .length()
                    - END_MESSAGE_COSTANT.length());
        return builder;

    }

    // ************************Decrypting Methods*********************************

    public  String decryptBycaeser(String cipherText, int shiftKey)
    {

        String plainText = "";
        for (int i = 0; i < cipherText.length(); i++)
        {
            int charPosition = ALPHABET.indexOf(cipherText.charAt(i));
            int keyVal = (charPosition - shiftKey) % 66;
            if (keyVal < 0)
            {
                keyVal = ALPHABET.length() + keyVal;
            }
            char replaceVal = ALPHABET.charAt(keyVal);
            plainText += replaceVal;
        }
        return plainText;
    }

    public  String decryptByRailFence(String cipherText) {
        int r=depth,len=cipherText.length();
        int c=len/depth;
        char mat[][]=new char[r][c];
        int k=0;

        String plainText="";


        for(int i=0;i< r;i++)
        {
            for(int j=0;j< c;j++)
            {
                mat[i][j]=cipherText.charAt(k++);
            }
        }
        for(int i=0;i< c;i++)
        {
            for(int j=0;j< r;j++)
            {
                plainText+=mat[j][i];
            }
        }

        return plainText;

    }

    public String decryptByMono(String text) {
        StringBuilder sb=new StringBuilder(text);
        String monokey="";
        int i;
        for(i=0;i<66;i++) {
            monokey+=String.valueOf(sb.charAt(0));
            sb.deleteCharAt(0);
        }
        text=sb.toString();

        char p1[] = new char[(text.length())];
        for ( i = 0; i < text.length(); i++)
        {
            for (int j = 0; j < 66; j++)
            {
                if (monokey.charAt(j) == text.charAt(i))
                {
                    p1[i] = ALPHABET.charAt(j);
                    break;
                }
            }
        }
        return (new String(p1));
    }

    public void gohome(View view) {
        Intent x= new Intent(this,MainActivity.class);
        startActivity(x);
    }

}
