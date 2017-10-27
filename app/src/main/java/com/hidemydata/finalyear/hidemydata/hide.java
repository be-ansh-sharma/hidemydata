/*
*
* This part sucks. Read the code carefully
*
*/


package com.hidemydata.finalyear.hidemydata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class hide extends AppCompatActivity {
    private static int[] binary = { 16, 8, 0 };
    private static byte[] andByte = { (byte) 0xC0, 0x30, 0x0C, 0x03 };
    private static int[] toShift = { 6, 4, 2, 0 };
    public static String END_MESSAGE_COSTANT = "#!@";
    public static String START_MESSAGE_COSTANT = "@!#";
    Uri imageuri, result;
    public static ImageView imageView;
    static int density, width, height;
    String text, key;
    Bitmap selectedImage;
    InputStream imageStream;
    public TextView tv, tv1;
    long start, end, duration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide);
        imageuri=getIntent().getData();
        Bundle bundle = getIntent().getExtras();
        text= bundle.getString("text");
        key=bundle.getString("key");
        imageView = (ImageView)findViewById(R.id.imageView3);
        tv=(TextView) findViewById(R.id.textView9);
        tv1=(TextView) findViewById(R.id.textView11);
        try {
            imageStream = getContentResolver().openInputStream(imageuri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        selectedImage = BitmapFactory.decodeStream(imageStream);

         width = selectedImage.getWidth();
         height = selectedImage.getHeight();
        density=selectedImage.getDensity();
        int[] oneD = new int[width * height];
        selectedImage.getPixels(oneD, 0, width, 0, 0, width, height);
        selectedImage.recycle();

        start=System.nanoTime();
        encodeMessage(oneD, width, height, text);
        end=System.nanoTime();
        duration=end-start;
        Log.i("encode picture",""+duration);
        tv.setText("KEY : " +key);



    }

    public  void encodeMessage(int[] oneDPix, int imgCols, int imgRows,
                                       String str) {
        str += END_MESSAGE_COSTANT;
        str = START_MESSAGE_COSTANT + str;
        byte[] msg = str.getBytes();
        int channels = 3;
        int shiftIndex = 4;
        //Array.newInstance(Byte.class, imgRows * imgCols * channels);
        byte[] result = new byte[imgRows * imgCols * channels];



        int msgIndex = 0;
        int resultIndex = 0;
        boolean msgEnded = false;
        for (int row = 0; row < imgRows; row++) {
            for (int col = 0; col < imgCols; col++) {
                int element = row * imgCols + col;
                byte tmp = 0;

                for (int channelIndex = 0; channelIndex < channels; channelIndex++) {
                    if (!msgEnded) {
                        tmp = (byte) ((((oneDPix[element] >> binary[channelIndex]) & 0xFF) & 0xFC) | ((msg[msgIndex] >> toShift[(shiftIndex++)
                                % toShift.length]) & 0x3));// 6
                        if (shiftIndex % toShift.length == 0) {
                            msgIndex++;
                        }
                        if (msgIndex == msg.length) {
                            msgEnded = true;
                        }
                    } else {
                        tmp = (byte) ((((oneDPix[element] >> binary[channelIndex]) & 0xFF)));
                    }
                    result[resultIndex++] = tmp;

                }

            }

        }

        int[] oneDMod =byteArrayToIntArray(result);
        //clearing the memory
        result=null;
        oneDPix=null;

        Bitmap destBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // garbage collection
        System.gc();
        destBitmap.setDensity(density);
        int masterIndex = 0;
        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++){
                // The unique way to write correctly the sourceBitmap, android bug!!!
                destBitmap.setPixel(i, j, Color.argb(0xFF,
                        oneDMod[masterIndex] >> 16 & 0xFF,
                        oneDMod[masterIndex] >> 8 & 0xFF,
                        oneDMod[masterIndex++] & 0xFF));

            }

        //*********************************LSB END*****************************
        SaveImage(destBitmap);
        imageView.setImageBitmap(destBitmap);
    }

//helper method
    public static int[] byteArrayToIntArray(byte[] b) {
        Log.v("Size byte array", b.length+"");
        int size=b.length / 3;
        Log.v("Size Int array",size+"");

        System.runFinalization();
        System.gc();
        Log.v("FreeMemory", Runtime.getRuntime().freeMemory()+"");
        int[] result = new int[size];
        int off = 0;
        int index = 0;
        while (off < b.length) {
            result[index++] = byteArrayToInt(b, off);
            off = off + 3;
        }

        return result;
    }
//overloaded method and helper method
    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }
    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0x00000000;
        for (int i = 0; i < 3; i++) {
            int shift = (3 - 1 - i) * 8;
            value |= (b[i + offset] & 0x000000FF) << shift;
        }
        value = value & 0x00FFFFFF;
        return value;
    }
// save image to external card and scan the media
    public void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/HideMyData");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".png";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, new String[]{"image/png"}, null);

            tv1.setText("Saved in : " +root + "/HideMyData/"+"Image-"+n+".png");
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gohome(View view) {
        Intent x= new Intent(this,MainActivity.class);
        startActivity(x);
    }

}
