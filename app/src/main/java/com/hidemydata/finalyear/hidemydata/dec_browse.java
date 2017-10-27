package com.hidemydata.finalyear.hidemydata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class dec_browse extends AppCompatActivity {
    ImageView imageView;
    private final int SELECT_PHOTO = 2;
    Bitmap selectedImage;
    Uri imageUri;
    static int flag;
    InputStream imageStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dec_browse);
        flag=0;
        Button buttonPick1 = (Button)findViewById(R.id.button4);
        imageView = (ImageView)findViewById(R.id.imageView2);
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

    public void dec_back (View view) {
        Intent x=new Intent(this,MainActivity.class);
        startActivity(x);
    }

    public void dec_key (View view) {
        if(flag == 0) {
            Toast t = Toast.makeText(getApplicationContext(), "Please select a Image First", Toast.LENGTH_SHORT);
            t.show();

        }
        else {
            Intent x = new Intent(this, dec_key.class);
            x.setData(imageUri);
            startActivity(x);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        flag = 1;
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
