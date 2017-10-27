package com.hidemydata.finalyear.hidemydata;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class enc_browse extends Activity {

    ImageView imageView;
    private final int SELECT_PHOTO = 2;
    Bitmap selectedImage;
    Uri imageUri;
    static int flag;
    InputStream imageStream;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enc_browse);


        Button buttonPick1 = (Button)findViewById(R.id.button3);
         imageView = (ImageView)findViewById(R.id.imageView);
        flag=0;
        imageUri=getIntent().getData();
        if(imageUri != null) {
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);

        }

        buttonPick1.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }


        });
    }

    public void next (View view) {
        if(flag == 0) {
            Toast t = Toast.makeText(getApplicationContext(), "Please select a Image First", Toast.LENGTH_SHORT);
            t.show();

        }
        else {
            Intent x = new Intent(this, file_browse.class);
            x.setData(imageUri);
            startActivity(x);
        }
    }

    public void back(View view) {
        Intent x=new Intent(this,MainActivity.class);
        startActivity(x);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        flag=1;
                         imageUri = data.getData();
                        imageStream = getContentResolver().openInputStream(imageUri);
                         selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

        }
    }



}